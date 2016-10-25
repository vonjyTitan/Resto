package com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.mapping.AnnulationMenu;
import com.mapping.Commande;
import com.mapping.ConstantEtat;
import com.mapping.MenuCommande;

import dao.Connecteur;
import dao.DaoModele;
import utilitaire.SessionUtil;
import utilitaire.UtileAffichage;

public class CommandeService {
	private static CommandeService instance;
	private CommandeService(){
		
	}
	public static CommandeService getInstance(){
		if(instance==null)
			return instance=new CommandeService();
		return instance;
	}
	public void reduireQuantiteStock(MenuCommande mc,Connection conn)throws Exception{
		//TODO reduire la quantite en stock par rapport aux menu livreEs
	}
	public void annulerMenu(AnnulationMenu annulation)throws Exception{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection();
			conn.setAutoCommit(false);
			
			annulerMenu(annulation,conn);
			
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
	public void annulerMenu(AnnulationMenu annulation,Connection conn)throws Exception{
		
		DaoModele.getInstance().save(annulation, conn);
		calculeQuantiteAnnuler(annulation.getIdcommande_menu(), conn);
		
	}
	public void calculeQuantiteAnnuler(int idmenucommande,Connection conn)throws Exception{
		conn.createStatement().executeUpdate("update "+(new MenuCommande().findReference())+" set annuler=(select sum(quantite) from annulation_menu where annulation_menu.idcommande_menu="+(new MenuCommande().findReference())+".idcommande_menu)");
	}
	public void calculeQuantiteAnnuler(int idmenucommande)throws Exception{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection();
			conn.setAutoCommit(false);
			calculeQuantiteAnnuler(idmenucommande,conn);
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
	public void calculeQuantiteLivrer(int idmenucommande,Connection conn)throws Exception{
		conn.createStatement().executeUpdate("update "+(new MenuCommande().findReference())+" set livrer=(select sum(quantite) from livraison_menu where livraison_menu.idmenu_commande="+(new MenuCommande().findReference())+".idcommande_menu)");
	}
	public void calculeQuantiteLivrer(int idmenucommande)throws Exception{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection();
			conn.setAutoCommit(false);
			calculeQuantiteLivrer(idmenucommande,conn);
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
	public void addMenuForCommande(MenuCommande mc,int idensemble,Connection conn)throws Exception
	{
		List<MenuCommande> already=DaoModele.getInstance().findPageGenerique(1, mc, conn, "");
		if(already.size()>0){
			MenuCommande alr=already.get(0);
			alr.setQuantite(alr.getQuantite()+mc.getQuantite());
			DaoModele.getInstance().update(alr, conn);
			return;
		}
		List<Commande> coms=DaoModele.getInstance().findPageGenerique(1, new Commande(), conn, " and idensemble="+idensemble);
		if(coms.size()==0)
			throw new Exception("Probleme au niveau serveur, commande introuvable");
		mc.setIdcommande(coms.get(0).getIdcommande());
		DaoModele.getInstance().save(mc, conn);
	}
	public void addMenuForCommande(MenuCommande mc,int idensemble)throws Exception
	{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection();
			conn.setAutoCommit(false);
			addMenuForCommande(mc,idensemble,conn);
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
	public void annulerComande(int idensemble,String cause) throws Exception {
		Connection conn=null;
		try{
			conn=Connecteur.getConnection();
			conn.setAutoCommit(false);
			
			MenuCommande crit=new MenuCommande();
			List<MenuCommande> mcs=DaoModele.getInstance().findPageGenerique(1, crit,conn," and idcommande in (select idcommande from commande where idensemble="+idensemble+") ");
			AnnulationMenu annulation=null;
			for(MenuCommande mc:mcs){
				annulation = new AnnulationMenu();
				annulation.setIdcommande_menu(mc.getIdcommande_menu());
				annulation.setCause(cause);
				annulation.setQuantite(mc.getQuantite()-mc.getAnnuler()-mc.getLivrer());
				annulation.setDate(new java.sql.Date(UtileAffichage.getDateNow().getTime()));
				annulerMenu(annulation, conn);
			}
			DaoModele.getInstance().executeUpdate("update table_liste set etat="+ConstantEtat.ETAT_TABLE_OCCUPER_SANS_COMMANDE+" where idtable in (select idtable from commande_ensemble_table where idensemble="+idensemble+") ", conn);
			DaoModele.getInstance().executeUpdate("update commande set etat="+ConstantEtat.ETAT_COMMANDE_ANNULER+" where idensemble="+idensemble, conn);
			
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
	public void jumellerCommande(int destination,int idensemble,Connection conn) throws Exception{
		//TODO save in notification that the commande was jummelled
		Commande crit=new Commande();
		crit.setIdensemble(idensemble);
		List<Commande> coms=DaoModele.getInstance().findPageGenerique(1,crit,conn,"");
		if(coms.get(0).getEtat()!=ConstantEtat.ETAT_COMMANDE_EN_COUR){
			throw new Exception("L ensemble numero "+idensemble+" n est plus en cour");
		}
		DaoModele.getInstance().executeUpdate("update commande set idensemble="+destination+" where idensemble="+idensemble+" ",conn);
		DaoModele.getInstance().executeUpdate("update commande_ensemble_table set idensemble="+destination+" where idensemble="+idensemble+" ",conn);
		
	}
	public void jumellerCommande(int destination,int []idensembles,Connection conn) throws Exception{
		for(int idensemble : idensembles){
			jumellerCommande(destination,idensemble,conn);
		}
	}
	public void jumellerCommande(int[] idensembles) throws Exception{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection();
			conn.setAutoCommit(false);
			int dest=idensembles[0];
			
			List<Commande> coms=DaoModele.getInstance().findPageGenerique(1,new Commande(),conn," and idensemble="+dest);
			if(coms.get(0).getEtat()!=ConstantEtat.ETAT_COMMANDE_EN_COUR){
				throw new Exception("L ensemble numero "+dest+" n est plus en cour");
			}
			
			int[]from=new int[idensembles.length-1];
			for(int i=1;i<idensembles.length;i++){
				from[i-1]=idensembles[i];
			}
			jumellerCommande(dest,from,conn);
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
}
