package com.mapping;

import com.annotations.Entity;
import com.annotations.Required;

@Entity(pkName="idunite",reference="unite_article")
public class Unite extends DataEntity {
	private int idunite;
	@Required
	private String libelle;
	private String description;
	public int getIdunite() {
		return idunite;
	}
	public void setIdunite(int idunite) {
		this.idunite = idunite;
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
