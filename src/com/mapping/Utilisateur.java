package com.mapping;

import java.lang.reflect.InvocationTargetException;

import com.annotations.Entity;
import com.annotations.Parameter;

@Entity(reference="utilisateur",pkName="idutilisateur")
public class Utilisateur extends DataEntity {
	private int idutilisateur;
	private String nom;
	private String prenom;
	private String login;
	private String passe;
	private int active;
	@Parameter(libelle="Role de l'utilisateur",reference="idrole")
	private int idrole;
	public int getIdutilisateur() {
		return idutilisateur;
	}
	public void setIdutilisateur(int idutilisateur) {
		this.idutilisateur = idutilisateur;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPasse() {
		return passe;
	}
	public void setPasse(String passe) {
		this.passe = passe;
	}
	public int getIdrole() {
		return idrole;
	}
	public void setIdrole(int idrole) {
		this.idrole = idrole;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public String findActive(){
		if(active==1)
			return "<span class=\"label label-success label-mini\">active</span>";
		return "<span class=\"label label-danger label-mini\">desactive</span>";
	}
	public String getOption() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String reponse=super.getOption();
		if(active==2){
			reponse+= "<a class=\"btn btn-success btn-xs\" href=\"activeCompte?id="+getIdutilisateur()+"\"><i class=\"fa fa-check\"></i></a>";
		}
		else reponse+="<a class=\"btn btn-danger btn-xs\" href=\"desactiveCompte?id="+getIdutilisateur()+"\"><i class=\"fa fa-trash-o \"></i></a>";
		return reponse;
	}
	
}
