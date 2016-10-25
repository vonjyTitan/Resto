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
	public void saveNotification(String description,int idutilisateur,Connection connex,DataEntity other)throws Exception{
		Notification notif=new Notification(description,"",0,idutilisateur);
		//DaoModele.getInstance().save(notif, connex);
	}
	public void saveNotification(String description,int idutilisateur,DataEntity other)throws Exception{
		Connection connex=null;
		try{
			connex=Connecteur.getConnection();
			saveNotification(description,idutilisateur,connex,other);
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
