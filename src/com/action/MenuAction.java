package com.action;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.affichage.HTMLBuilder;
import com.mapping.Menu;
import com.mapping.MenuArticle;
import com.rooteur.Action;

import dao.Connecteur;
import dao.DaoModele;

public class MenuAction extends Action {
	public void ajout(HttpServletRequest request,HttpServletResponse response)throws Exception{
		Menu menu=new HTMLBuilder<Menu>(new Menu(), request).getValue();
		String[] article=request.getParameterValues("article");
		String[] quantite=request.getParameterValues("quantite");
		if(!menu.isValide()){
			goTo(request, response, "post", "main.jsp?cible=configuration/menu-ajout&erreur=Champ invalide");
			return;
		}
		if(quantite==null || article==null || article.length==0 || quantite.length==0 || article.length!=quantite.length){
			throw new Exception("Article en besoin obligatoire");
		}
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(menu.findDataBaseKey());
			conn.setAutoCommit(false);
			//TODO save file
			DaoModele.getInstance().save(menu, conn);
			
			List<MenuArticle> ma=new ArrayList<MenuArticle>();
			for(int i=0;i<article.length;i++){
				int idarticle=Integer.valueOf("0"+article[i]);
				double q=Double.valueOf("0"+quantite[i]);
				if(q<=0.0){
					throw new Exception("Quantite d'article obligatoirement positif");
				}
				if(idarticle==0){
					throw new Exception("Selection d'article obligatoire ou effacer les lignes unitiles");
				}
				MenuArticle menuArticle=new MenuArticle();
				menuArticle.setIdarticle(idarticle);
				menuArticle.setQuantite(q);
				menuArticle.setIdmenu(menu.getIdmenu());
				ma.add(menuArticle);
			}
			DaoModele.getInstance().save(ma, conn);
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
		goTo(request, response, "get", "main.jsp?cible=configuration/menu-liste&id="+menu.getIdmenu());
	}
	public void modif(HttpServletRequest request,HttpServletResponse response)throws Exception{
		Menu menu=new HTMLBuilder<Menu>(new Menu(), request).getValue();
		String[] article=request.getParameterValues("article");
		String[] quantite=request.getParameterValues("quantite");
		if(!menu.isValide()){
			goTo(request, response, "post", "main.jsp?cible=configuration/menu-modif&erreur=Champ invalide");
			return;
		}
		if(quantite==null || article==null || article.length==0 || quantite.length==0 || article.length!=quantite.length){
			throw new Exception("Article en besoin obligatoire");
		}
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(menu.findDataBaseKey());
			conn.setAutoCommit(false);
			//TODO save file
			DaoModele.getInstance().update(menu, conn);
			
			List<MenuArticle> ma=new ArrayList<MenuArticle>();
			for(int i=0;i<article.length;i++){
				int idarticle=Integer.valueOf("0"+article[i]);
				double q=Double.valueOf("0"+quantite[i]);
				if(q<=0.0 && idarticle>0){
					throw new Exception("Quantite d'article obligatoirement positif");
				}
				if(idarticle==0 && q>0){
					throw new Exception("Selection d'article obligatoire");
				}
				MenuArticle menuArticle=new MenuArticle();
				menuArticle.setIdarticle(idarticle);
				menuArticle.setQuantite(q);
				menuArticle.setIdmenu(menu.getIdmenu());
				conn.createStatement().executeUpdate("delete from menu_article where idmenu="+menu.getIdmenu());
				ma.add(menuArticle);
			}
			DaoModele.getInstance().save(ma, conn);
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
		goTo(request, response, "get", "main.jsp?cible=configuration/menu-fiche&id="+menu.getIdmenu());
	}
}
