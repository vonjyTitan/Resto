package com.mapping;

import com.annotations.Entity;

@Entity(reference="menu_commande",pkName="idcommande_menu")
public class MenuCommande extends DataEntity {
	private int idcommande_menu;
	private int idcommande;
	private int idmenu;
	private int quantite;
	private String remarque;
	private int annuler;
	private int livrer;
	public int getIdcommande_menu() {
		return idcommande_menu;
	}
	public void setIdcommande_menu(int idcommande_menu) {
		this.idcommande_menu = idcommande_menu;
	}
	public int getIdcommande() {
		return idcommande;
	}
	public void setIdcommande(int idcommande) {
		this.idcommande = idcommande;
	}
	public int getIdmenu() {
		return idmenu;
	}
	public void setIdmenu(int idmenu) {
		this.idmenu = idmenu;
	}
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	public String getRemarque() {
		return remarque;
	}
	public void setRemarque(String remarque) {
		this.remarque = remarque;
	}
	public int getAnnuler() {
		return annuler;
	}
	public void setAnnuler(int annuler) {
		this.annuler = annuler;
	}
	public int getLivrer() {
		return livrer;
	}
	public void setLivrer(int livrer) {
		this.livrer = livrer;
	}
}
