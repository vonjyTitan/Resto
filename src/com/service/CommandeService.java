package com.service;

import java.sql.Connection;

import com.mapping.MenuCommande;

import dao.Connecteur;

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
}
