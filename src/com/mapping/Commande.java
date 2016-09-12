package com.mapping;

import java.util.Date;

import com.annotations.Entity;

@Entity(reference="commande",pkName="idcommande")
public class Commande extends DataEntity {
	private int idcommande;
	private int nbPersonnes;
	private int idtable;
	private Date daty;
	public int getIdcommande() {
		return idcommande;
	}
	public void setIdcommande(int idcommande) {
		this.idcommande = idcommande;
	}
	public int getNbPersonnes() {
		return nbPersonnes;
	}
	public void setNbPersonnes(int nbPersonnes) {
		this.nbPersonnes = nbPersonnes;
	}
	public int getIdtable() {
		return idtable;
	}
	public void setIdtable(int idtable) {
		this.idtable = idtable;
	}
	public Date getDaty() {
		return daty;
	}
	public void setDaty(Date daty) {
		this.daty = daty;
	}
	
}
