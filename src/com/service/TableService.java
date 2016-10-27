package com.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.mapping.Commande;
import com.mapping.ConstantEtat;
import com.mapping.Table;

import dao.Connecteur;
import dao.DaoModele;
import utilitaire.Utilitaire;

public class TableService {
	private static TableService instance=null;
	private TableService(){
		
	}
	public static TableService getInstance(){
		if(instance==null)
			return instance=new TableService();
		return instance;
	}
	public void transfererTable(Integer[] depart,int destination)throws Exception{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection();
			conn.setAutoCommit(false);
			
			String inter=Utilitaire.concatListe(depart, ",", "");
			List<Table> toup=DaoModele.getInstance().findPageGenerique(1,new Table(),conn," and idtable in ("+inter+") ");
			if(toup.size()!=depart.length){
				throw new Exception("Table introuvable");
			}
			Table dest=DaoModele.getInstance().findById(new Table(), destination, conn);
			if(dest==null){
				throw new Exception("Table de destination introuvable");
			}
			for(Table tu:toup)
			{
				if(tu.getEtat()==ConstantEtat.ETAT_TABLE_OCCUPER_SANS_COMMANDE || tu.getEtat()==ConstantEtat.ETAT_TABLE_LIBRE){
					throw new Exception("Impossible de transferer une table qui n'a aucun commande en cour");
				}
				transfererTable(tu, dest, conn);
			}
			
			conn.commit();
		}
		catch(Exception ex){
			if(conn!=null)
				conn.rollback();
			throw ex;
		}
		finally{
			if(conn!=null)
				conn.close();
		}
	}
	public void transfererTable(Table depart,Table destination,Connection conn)throws Exception{
		if(destination.getEtat()==ConstantEtat.ETAT_TABLE_RESERVER){
			throw new Exception("la table cible est reservée");
		}
		//TODO save transfert historique
		List<Commande> coms=DaoModele.getInstance().findPageGenerique(1, new Commande(),conn," and idtableassi="+depart.getIdtable());
		if(coms.size()>0){
			coms.get(0).setIdtableassi(destination.getIdtable());
			DaoModele.getInstance().executeUpdate("delete commande_ensemble_table where idtable="+depart.getIdtable()+" and idensemble="+coms.get(0).getIdensemble(), conn);
			List<Map<String,Object>> already=DaoModele.getInstance().excecuteQuery("select * from commande_ensemble_table where idtable="+destination.getIdtable()+" and idensemble="+coms.get(0).getIdensemble()+" ", conn);
			if(already.size()==0){
				DaoModele.getInstance().executeUpdate("insert into commande_ensemble_table(idtable,idensemble) values ("+destination.getIdtable()+","+coms.get(0).getIdensemble()+") ", conn);
			}
			else{
				List<Commande> comsAct=DaoModele.getInstance().findPageGenerique(1, new Commande(),conn," and idtableassi="+destination.getIdtable());
				Commande ca=comsAct.get(0);
				ca.setNbPersonnes(ca.getNbPersonnes()+coms.get(0).getNbPersonnes());
				DaoModele.getInstance().update(ca, conn);
			}
		}
		
	}
}
