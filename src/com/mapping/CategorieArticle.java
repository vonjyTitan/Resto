package com.mapping;

import com.annotations.Entity;
import com.annotations.Required;

@Entity(pkName="idcategorie",reference="categorie_article")
public class CategorieArticle extends DataEntity {
	private int idcategorie;
	@Required
	private String libelle;
	private String description;
	public int getIdcategorie() {
		return idcategorie;
	}
	public void setIdcategorie(int idcategorie) {
		this.idcategorie = idcategorie;
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
