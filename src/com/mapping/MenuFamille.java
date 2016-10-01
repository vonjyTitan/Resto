package com.mapping;

import com.annotations.Entity;
import com.annotations.Required;

@Entity(pkName="idfamille",reference="menu_famille")
public class MenuFamille extends DataEntity {
	private int idfamille;
	@Required
	private String libelle;
	private String description;
	public int getIdfamille() {
		return idfamille;
	}
	public void setIdfamille(int idfamille) {
		this.idfamille = idfamille;
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
}
