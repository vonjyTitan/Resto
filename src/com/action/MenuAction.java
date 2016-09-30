package com.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.affichage.HTMLBuilder;
import com.mapping.Menu;
import com.rooteur.Action;

public class MenuAction extends Action {
	public void ajout(HttpServletRequest request,HttpServletResponse response)throws Exception{
		Menu menu=new HTMLBuilder<Menu>(new Menu(), request).getEntity();
		String[] article=request.getParameterValues("article");
		String[] quantite=request.getParameterValues("quantite");
		if(!menu.isValide()){
			goTo(request, response, "post", "main.jsp?cible=configuration/menu-ajout&erreur=Champ invalide");
			return;
		}
		if(quantite==null || article==null || article.length==0 || quantite.length==0){
			goTo(request, response, "post", "main.jsp?cible=configuration/menu-ajout&erreur=article");
			return;
		}
		
	}
}
