package com.action;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mapping.ConstantEtat;
import com.mapping.Table;
import com.rooteur.Action;

import dao.Connecteur;
import dao.DaoModele;

public class CommandeAction extends Action {
	public void ajout(HttpServletRequest request,HttpServletResponse response)throws Exception{
		String [] menus=request.getParameterValues("menu");
		String [] quantite=request.getParameterValues("quantite");
		String [] remarque=request.getParameterValues("remarque");
		if(menus==null || menus.length==0){
			throw new Exception("Une commande doit avoir un menu au moins");
		}
		String nbClient=request.getParameter("client");
		int client=Integer.valueOf("0"+nbClient);
		if(client<=0){
			throw new Exception("le nombre de client doit etre positif");
		}
		String table=request.getParameter("idtable");
		int idtable=Integer.valueOf("0"+table);
		Table t=DaoModele.getInstance().findById(new Table(), idtable);
		if(t==null || t.getEtat()!=ConstantEtat.ETAT_OCCUPER_SANS_COMMANDE){
			throw new Exception("Table selectionnEe invalide");
		}
		Connection conn=null;
		try{
			conn=Connecteur.getConnection();
			conn.setAutoCommit(false);
			
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
