package com.service;

import java.sql.Connection;
import java.util.List;

import com.mapping.DataEntity;
import com.mapping.Notification;
import com.mapping.RoleActivite;
import com.mapping.Utilisateur;

import dao.Connecteur;
import dao.DaoModele;

public class LoginService {
	private static LoginService instance=null;
	private LoginService(){
		instance=this;
	}
	public static LoginService getInstance(){
		if(instance==null)
			new LoginService();
		return instance;
	}
	public boolean isAllowed(Utilisateur utilisateur,String activite)throws Exception{
		List<RoleActivite> rep=DaoModele.getInstance().findPageGenerique(1, new RoleActivite(), " and idrole="+utilisateur.getIdrole()+" and activite='"+activite+"'");
		if(rep.size()==0)
			return false;
		return true;
	}
	public Utilisateur testLogin(String login,String passe)throws Exception{
		Connection connex=null;
		try{
			connex=Connecteur.getConnection();
			Utilisateur ob=new Utilisateur();
			List<Utilisateur> rep=DaoModele.getInstance().findPageGenerique(1, ob,connex," and login='"+login.replace("'", "")+"' and passe='"+passe.replace("'", "")+"' and active=1");
			if(rep.size()>0)
			{
				NotificationService.getInstance().saveNotification(Notification.LOGIN, rep.get(0).getIdutilisateur(), connex, rep.get(0));
				return rep.get(0);
			}
			connex.commit();
		}
		catch(Exception ex){
			if(connex!=null)
				connex.rollback();
		}
		finally{
			if(connex!=null)
				connex.close();
		}
		return null;
	}
}
