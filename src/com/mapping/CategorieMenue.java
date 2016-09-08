package com.mapping;

import com.annotations.Entity;
import com.annotations.Parameter;
import com.annotations.Required;

@Entity(pkName="idcategorie",reference="categorie")
public class CategorieMenue extends DataEntity{
	private int idcategorie;
	@Parameter(libelle="Nom de la categorie")
	@Required
	private String libelle;
	private String caracteristique;
	
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
	public String getCaracteristique() {
		return caracteristique;
	}
	public void setCaracteristique(String caracteristique) {
		this.caracteristique = caracteristique;
	}
	
}
