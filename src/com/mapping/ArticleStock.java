package com.mapping;

import com.annotations.Entity;
import com.annotations.ForeignKey;
import com.annotations.Parameter;
import com.annotations.Required;

import utilitaire.UtileAffichage;

@Entity(pkName="idarticle",reference="article_stock")
public class ArticleStock extends DataEntity{
	private int idarticle;
	@Required
	private String libelle;
	private String description;
	@Parameter(libelle="Categorie")
	@ForeignKey(toclasse=CategorieArticle.class,libtable="libelle",pktable="idcategorie")
	private int idcategorie;
	@Parameter(libelle="Unite")
	@ForeignKey(toclasse=Unite.class,libtable="libelle",pktable="idunite")
	private int idunite;
	private double quantite;
	private  String unite;
	private String categorie;
	public String getUnite() {
		return unite;
	}
	public void setUnite(String unite) {
		this.unite = unite;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public int getIdarticle() {
		return idarticle;
	}
	public void setIdarticle(int idarticle) {
		this.idarticle = idarticle;
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
	public int getIdcategorie() {
		return idcategorie;
	}
	public void setIdcategorie(int idcategorie) {
		this.idcategorie = idcategorie;
	}
	public int getIdunite() {
		return idunite;
	}
	public void setIdunite(int idunite) {
		this.idunite = idunite;
	}
	public double getQuantite() {
		return quantite;
	}
	public void setQuantite(double quantite) {
		this.quantite = quantite;
	}
	public String findQuantite(){
		return quantite+" "+((unite!=null) ? unite : "");
	}
}
