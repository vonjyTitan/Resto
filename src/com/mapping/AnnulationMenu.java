package com.mapping;

import java.sql.Date;

import com.annotations.Entity;
import com.annotations.Parameter;

@Entity(reference="annulation_menu",pkName="idannulation")
public class AnnulationMenu extends DataEntity{
	private int idannulation;
	private int idcommande_menu;
	private int quantite;
	@Parameter(reference="daty")
	private Date date;
	private String cause;
	private int idutilisateur;
	private String utilisateur;
	public int getIdannulation() {
		return idannulation;
	}
	public void setIdannulation(int idannulation) {
		this.idannulation = idannulation;
	}
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getIdcommande_menu() {
		return idcommande_menu;
	}
	public void setIdcommande_menu(int idcommande_menu) {
		this.idcommande_menu = idcommande_menu;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public int getIdutilisateur() {
		return idutilisateur;
	}
	public void setIdutilisateur(int idutilisateur) {
		this.idutilisateur = idutilisateur;
	}
	public String getUtilisateur() {
		return utilisateur;
	}
	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}
}
