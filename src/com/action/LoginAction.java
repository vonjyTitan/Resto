package com.action;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.affichage.HTMLBuilder;
import com.mapping.Utilisateur;
import com.rooteur.Action;
import com.service.LoginService;

import dao.DaoModele;
import utilitaire.SessionUtil;
import utilitaire.UtilCrypto;

public class LoginAction extends Action {
	public String testLogin(HttpServletRequest request,HttpServletResponse response){
		if(request.getParameter("login")==null || request.getParameter("passe")==null)
		{
			goTo(request, response, "login.jsp?erreur=Mot de passe ou login obligatoire");
			return "error";
		}
		if(request.getParameter("login").isEmpty() || request.getParameter("passe").isEmpty())
		{
			goTo(request, response, "login.jsp?erreur=Mot de passe ou login obligatoire");
			return "error";
		}
		Utilisateur u=null;
		try {
			u=LoginService.getInstance().testLogin(request.getParameter("login"), request.getParameter("passe"));
		} catch (Exception e) {
			goTo(request, response, "login.jsp?erreur="+e.getMessage());
			return "error";
		}
		if(u==null){
			goTo(request, response, "login.jsp?erreur=Login ou mot de passe inconnue");
			return "error";
		}
		request.getSession().setAttribute("utilisateur", u);
		String cible=(String) request.getParameter("cible");
		if(cible==null || cible.isEmpty())
			cible="stat";
		goTo(request, response, "main.jsp?cible="+cible);
		return "ok";
	}
	public void desactive(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			int id=Integer.valueOf("0"+SessionUtil.getValForAttr(request, "id"));
			Utilisateur user=DaoModele.getInstance().findById(new Utilisateur(), id);
			if(user==null)
			{
				throw new Exception("Utilisateur introuvable");
			}
			user.setActive(2);
			DaoModele.getInstance().update(user);
			goTo(request, response, "get","main.jsp?cible=configuration/utilisateur-fiche&id="+id);
		}
		catch(Exception ex){
			throw ex;
		}
	}
	public void active(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		try{
			int id=Integer.valueOf("0"+SessionUtil.getValForAttr(request, "id"));
			Utilisateur user=DaoModele.getInstance().findById(new Utilisateur(), id);
			if(user==null)
			{
				throw new Exception("Utilisateur introuvable");
			}
			user.setActive(1);
			DaoModele.getInstance().update(user);
			goTo(request, response, "get","main.jsp?cible=configuration/utilisateur-fiche&id="+id);
		}
		catch(Exception ex){
			throw ex;
		}
	}
	public String ajoutUtilisateur(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			Utilisateur user=new HTMLBuilder<Utilisateur>(new Utilisateur(), request).getValue();
			if(!user.isValide()){
				goTo(request,response,"main.jsp?cible=configuration/utilisateur-ajout&erreur=Champ manquant");
				return "error";
			}
			user.setPasse(UtilCrypto.encrypt(user.getLogin()));
			user.setActive(2);
			DaoModele.getInstance().save(user);
			goTo(request,response,"get","main.jsp?cible=configuration/liste-utilisateur&"+String.valueOf(user.getIdutilisateur()));
			return "ok";
		}
		catch(Exception ex){
			throw ex;
		}
	}
	public void modif(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try{
			Utilisateur user=new HTMLBuilder<Utilisateur>(new Utilisateur(), request).getValue();
			Utilisateur usernw=DaoModele.getInstance().findById(new Utilisateur(), user.getIdutilisateur());
			if(!user.isValide())
			{
				goTo(request, response, "main.jsp?cible=configuration/utilisateur-modif&erreur=Champ manquant");
				return;
			}
			user.setPasse(usernw.getPasse());
			DaoModele.getInstance().update(user);
			goTo(request, response,"get", "main.jsp?cible=configuration/utilisateur-fiche&id="+user.getIdutilisateur());
		}
		catch(Exception ex){
			throw ex;
		}
	}
	
}
