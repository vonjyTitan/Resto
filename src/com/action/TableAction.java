package com.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.affichage.HTMLBuilder;
import com.mapping.ConstantEtat;
import com.mapping.Table;
import com.rooteur.Action;

import dao.Connecteur;
import dao.DaoModele;
import utilitaire.SessionUtil;
import utilitaire.Utilitaire;

public class TableAction extends Action {
	public void modifplace(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Connection conn=null;
		try{
			Table crit=new Table();
			
			conn=Connecteur.getConnection(crit.findDataBaseKey());
			conn.setAutoCommit(false);
			
			String []ids= request.getParameterValues("id");
			String []positionx= request.getParameterValues("posx");
			String []positiony= request.getParameterValues("posy");
			if(ids==null || ids.length==0){
				throw new Exception("Liste de table invalide");
			}
			List<Table> tables=new ArrayList<Table>();
			Table t=null;
			for(int i=0;i<ids.length;i++){
				t=DaoModele.getInstance().findById(crit, Integer.valueOf("0"+ids[i]));
				t.setPositionx(Integer.valueOf("0"+positionx[i]));
				t.setPositiony(Integer.valueOf("0"+positiony[i]));
				tables.add(t);
			}
			DaoModele.getInstance().update(tables,conn,"");
			conn.commit();
			goTo(request,response,"get","main.jsp?cible=configuration/table-gestion");
			return;
		}
		catch(Exception ex){
			if(conn!=null)
					conn.rollback();
			throw ex;
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	public void modif(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			Table table=(Table) new HTMLBuilder(new Table(), request).getEntity();
			Table ancien=DaoModele.getInstance().findById(new Table(), table.getIdtable());
			if(ancien==null)
			{
				throw new Exception("Table introuvable");
			}
			table.setPositionx(ancien.getPositionx());
			table.setPositiony(ancien.getPositiony());
			table.setEtat(ancien.getEtat());
			
			if(!table.isValide())
			{
				goTo(request,response,"main.jsp?cible=configuration/table-modif&erreur=Champ invalide");
				return;
			}
			
			DaoModele.getInstance().update(table);
			
			goTo(request,response,"get","main.jsp?cible=configuration/table-gestion");
			return;
		}
		catch(Exception ex){
			throw ex;
		}
	}
	public void ajout(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		Table table = new HTMLBuilder<Table>(new Table(),request).getEntity();
		if(!table.isValide())
		{
			goTo(request,response,"main.jsp?cible=configuration/table-ajout&erreur=Champ invalide");
			return;
		}
		table.setEtat(ConstantEtat.ETAT_TABLE_LIBRE);
		DaoModele.getInstance().save(table);
		goTo(request,response,"get","main.jsp?cible=configuration/table-gestion");
	}
	public void occuper(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(new Table().findDataBaseKey());
			conn.setAutoCommit(false);
			
			String[] id=request.getParameterValues("id");
			if(id==null || id.length==0){
				throw new Exception("Aucune table selectinner");
			}
			String inter=Utilitaire.concatListe(id, ",", "");
			List<Table> toup=DaoModele.getInstance().findPageGenerique(1,new Table(),conn," and idtable in ("+inter+") ");
			if(toup.size()!=id.length){
				throw new Exception("Table introuvable");
			}
			for(Table tu:toup)
			{
				if(tu.getEtat()==ConstantEtat.ETAT_TABLE_OCCUPER_SANS_COMMANDE || tu.getEtat()==ConstantEtat.ETAT_TABLE_OCCUPER_AVEC_COMMANDE)
				{
					throw new Exception("Impossible d'occuper la table car elle est deja pris");
				}
				if( tu.getEtat()==ConstantEtat.ETAT_TABLE_RESERVER)
				{
					throw new Exception("Impossible d'occuper la table car elle est reserver");
				}
				tu.setEtat(ConstantEtat.ETAT_TABLE_OCCUPER_SANS_COMMANDE);
			}
			DaoModele.getInstance().update(toup,conn,"");
			conn.commit();
			goTo(request,response,"get","main.jsp?cible=configuration/table-gestion");
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
	public void liberer(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(new Table().findDataBaseKey());
			conn.setAutoCommit(false);
			
			String[] id=request.getParameterValues("id");
			if(id==null || id.length==0){
				throw new Exception("Aucune table selectinner");
			}
			String inter=Utilitaire.concatListe(id, ",", "");
			List<Table> toup=DaoModele.getInstance().findPageGenerique(1,new Table(),conn," and idtable in ("+inter+") ");
			if(toup.size()!=id.length){
				throw new Exception("Table introuvable");
			}
			for(Table tu:toup)
			{
				
				if(tu.getEtat()==ConstantEtat.ETAT_TABLE_OCCUPER_AVEC_COMMANDE){
					throw new Exception("Impossible de liberer la table car il y a encore des commandes en cours pour elle");
				}
				tu.setEtat(ConstantEtat.ETAT_TABLE_LIBRE);
			}
			DaoModele.getInstance().update(toup,conn,"");
			conn.commit();
			goTo(request,response,"get","main.jsp?cible=configuration/table-gestion");
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
	public void transferer(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(new Table().findDataBaseKey());
			conn.setAutoCommit(false);
			
			String[] id=request.getParameterValues("id");
			if(id==null || id.length==0){
				throw new Exception("Aucune table selectinner");
			}
			String inter=Utilitaire.concatListe(id, ",", "");
			List<Table> toup=DaoModele.getInstance().findPageGenerique(1,new Table(),conn," and idtable in ("+inter+") ");
			if(toup.size()!=id.length){
				throw new Exception("Table introuvable");
			}
			for(Table tu:toup)
			{
				if(tu.getEtat()==ConstantEtat.ETAT_TABLE_OCCUPER_SANS_COMMANDE || tu.getEtat()==ConstantEtat.ETAT_TABLE_LIBRE){
					throw new Exception("Impossible de transferer une table qui n'a aucun commande en cour");
				}
			}
		//TRANSFERER
		goTo(request,response,"get","main.jsp?cible=configuration/table-gestion");
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
