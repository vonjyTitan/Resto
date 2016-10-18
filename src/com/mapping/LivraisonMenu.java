package com.mapping;

import java.sql.Date;

import com.annotations.Entity;
import com.annotations.Parameter;

@Entity(reference="livraison_menu",pkName="idlivraison")
public class LivraisonMenu extends DataEntity {
	private int idlivraison;
	private int idmenu_commande;
	private int quantite;
	@Parameter(reference="daty")
	private Date date;
	public int getIdlivraison() {
		return idlivraison;
	}
	public void setIdlivraison(int idlivraison) {
		this.idlivraison = idlivraison;
	}
	public int getIdmenu_commande() {
		return idmenu_commande;
	}
	public void setIdmenu_commande(int idmenu_commande) {
		this.idmenu_commande = idmenu_commande;
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
}
