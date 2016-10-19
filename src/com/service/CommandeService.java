package com.service;

import java.sql.Connection;
import java.util.List;

import com.mapping.Commande;
import com.mapping.MenuCommande;

import dao.Connecteur;
import dao.DaoModele;

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
}
