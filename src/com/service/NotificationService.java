package com.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.mapping.DataEntity;
import com.mapping.Notification;

import dao.Connecteur;
import dao.DaoModele;

public class NotificationService {
	private static NotificationService instance=null;
	private NotificationService(){
		instance=this;
	}
	public static NotificationService getInstance(){
		if(instance==null)
			new NotificationService();
		return instance;
	}
	public void saveNotification(int type,int idutilisateur,Connection connex,DataEntity other)throws Exception{
		String description="";
		switch(type){
			case Notification.AJOUT_COMMANDE:{
				description+=" a ajouté une commande pour la table ";
			}
			case Notification.AJOUT_RESERVATION:{
				description+=" a ajouté une reservation venant de M(r/me) ";
			}
			case Notification.ANNULATION_RESERVATION:{
				description+=" a annulé une reservation venant de M(r/me) ";
			}
			case Notification.CONFIRMATION_RESERVATION:{
				description+=" la reservation de M(r/me) a été confirmer par ";
			}
			case Notification.ANNULATION_COMMANDE:{
				description+=" a annulé une commande pour la table ";
			}
			case Notification.ANNULATION_MENUE:{
				description+=" a annulé un menu pour la table ";
			}
			case Notification.AJOUT_MENUE:{
				description+=" a ajouté un menu pour la table ";
			}
		}
		Notification notif=new Notification(description,"",type,0,idutilisateur);
		DaoModele.getInstance().save(notif, connex);
		
	}
	public void saveNotification(int type,int idutilisateur,DataEntity other)throws Exception{
		Connection connex=null;
		try{
			connex=Connecteur.getConnection();
			saveNotification(type,idutilisateur,connex,other);
		}
		catch(Exception ex){
			throw ex;
		}
		finally{
			if(connex!=null)
				connex.close();
		}
	}
	public List<Notification> findNotificationForAlert(int lastId) throws Exception{
		Connection connex=null;
		try{
			Notification crit=new Notification();
			crit.setPackSize(5);
			crit.setNomChampOrder("idnotification");
			crit.setOrdering(Notification.ASC);
			List<Notification> rep=DaoModele.getInstance().findPageGenerique(1, new Notification(), connex, " and vue=0 and idnotification>"+lastId);
			DaoModele.getInstance().excecuteQuery("update notification set vue=1 where idnotification in (select idnotification from notification where vue=0 and idnotification>"+lastId+" order by idnotification asc offset 0 limit 5)", connex);
			connex.commit();
			return rep;
		}
		catch(Exception ex){
			if(connex!=null)
				connex.rollback();
			throw ex;
		}
		finally{
			if(connex!=null)
				connex.close();
		}
	}
}
