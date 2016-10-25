package com.mapping;

import com.annotations.Entity;

@Entity(reference="notification",pkName="idnotification")
public class Notification extends DataEntity {
	private int idnotification;
	private int vue;
	private String description;
	private String lien;
	private int idutilisateur;
	
	public Notification(String descrption,String lien,int vue,int idutilisateur){
		this.setLien(lien);
		this.setDescription(descrption);
		this.setVue(vue);
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
	public int getIdutilisateur() {
		return idutilisateur;
	}
	public void setIdutilisateur(int idutilisateur) {
		this.idutilisateur = idutilisateur;
	}
	
}
