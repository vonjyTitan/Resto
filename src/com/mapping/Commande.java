package com.mapping;

import java.sql.Date;

import com.annotations.Entity;
import com.annotations.Parameter;

@Entity(reference="commande",pkName="idcommande")
public class Commande extends DataEntity {
	private int idcommande;
	@Parameter(reference="nombre_client",libelle="Nombre de personne")
	private int nbPersonnes;
	private Date daty;
	private int idensemble;
	private int lastensemble;
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
	public Date getDaty() {
		return daty;
	}
	public void setDaty(Date daty) {
		this.daty = daty;
	}
	public int getIdensemble() {
		return idensemble;
	}
	public void setIdensemble(int idensemble) {
		this.idensemble = idensemble;
	}
	public int getLastensemble() {
		return lastensemble;
	}
	public void setLastensemble(int lastensemble) {
		this.lastensemble = lastensemble;
	}
	
}
