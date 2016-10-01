package com.mapping;

import com.annotations.Entity;

@Entity(reference="menu_article")
public class MenuArticle extends DataEntity{
	private int idmenu;
	private int idarticle;
	private String article;
	private String unite;
	private double quantite;
	public int getIdmenu() {
		return idmenu;
	}
	public void setIdmenu(int idmenu) {
		this.idmenu = idmenu;
	}
	public int getIdarticle() {
		return idarticle;
	}
	public void setIdarticle(int idarticle) {
		this.idarticle = idarticle;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public String getUnite() {
		return unite;
	}
	public void setUnite(String unite) {
		this.unite = unite;
	}
	public double getQuantite() {
		return quantite;
	}
	public void setQuantite(double quantite) {
		this.quantite = quantite;
	}
}
