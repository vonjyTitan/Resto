package com.mapping;

import com.annotations.Entity;

@Entity(reference="utilisateur",pkName="idutilisateur")
public class Utilisateur extends DataEntity {
	private int idutilisateur;
	private String nom;
	private String prenom;
	private String login;
	private String passe;
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
	
}
