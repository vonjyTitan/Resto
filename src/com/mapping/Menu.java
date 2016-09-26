package com.mapping;

import com.annotations.Entity;
import com.annotations.ForeignKey;
import com.annotations.Parameter;

@Entity(pkName="idmenu",reference="menu")
public class Menu extends DataEntity {
	private int idmenu;
	private String libelle;
	private String description;
	private double prix;
	@Parameter(libelle="Famille")
	@ForeignKey(toclasse=MenuFamille.class,libtable="libelle",pktable="idfamille")
	private int idfamille;
	private String famille;
	public int getIdmenu() {
		return idmenu;
	}
	public void setIdmenu(int idmenu) {
		this.idmenu = idmenu;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public int getIdfamille() {
		return idfamille;
	}
	public void setIdfamille(int idfamille) {
		this.idfamille = idfamille;
	}
	public String getFamille() {
		return famille;
	}
	public void setFamille(String famille) {
		this.famille = famille;
	}
}
