package com.mapping;

import com.annotations.Entity;

@Entity(reference="notification",pkName="idnotification")
public class Notification extends DataEntity {
	private int idnotification;
	private int vue;
	private String description;
	private String lien;
	private int type;
	private int idutilisateur;
	
	public static final int ANNULATION_COMMANDE=1;
	public static final int AJOUT_COMMANDE=2;
	public static final int ANNULATION_MENUE=3;
	public static final int LIBERATION_TABLE=4;
	public static final int OCCUPATION_TABLE=5;
	public static final int TRANSFERT_TABLE=6;
	public static final int JUMELLAGE_TABLE=7;
	public static final int AJOUT_RESERVATION=8;
	public static final int CONFIRMATION_RESERVATION=9;
	public static final int ANNULATION_RESERVATION=10;
	public static final int AJOUT_MENUE = 11;
	public static final int LOGIN=12;
	
	public Notification(String descrption,String lien,int type,int vue,int idutilisateur){
		this.setLien(lien);
		this.setDescription(descrption);
		this.setVue(vue);
		this.setType(type);
		this.setIdutilisateur(idutilisateur);
	}
	public Notification(){
		
	}
	
	public int getIdnotification() {
		return idnotification;
	}
	public void setIdnotification(int idnotification) {
		this.idnotification = idnotification;
	}
	public int getVue() {
		return vue;
	}
	public void setVue(int vue) {
		this.vue = vue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLien() {
		return lien;
	}
	public void setLien(String lien) {
		this.lien = lien;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIdutilisateur() {
		return idutilisateur;
	}
	public void setIdutilisateur(int idutilisateur) {
		this.idutilisateur = idutilisateur;
	}
	
}
