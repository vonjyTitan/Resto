package com.mapping;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;

import com.annotations.Entity;
import com.annotations.ForeignKey;
import com.annotations.Parameter;

@Entity(reference="commande",pkName="idcommande")
public class Commande extends DataEntity {
	@Parameter(libelle="Numero de commande")
	private int idcommande;
	@Parameter(reference="nombre_client",libelle="Nombre de personne")
	private int nbPersonnes;
	@Parameter(libelle="Date")
	private Date daty;
	private int idensemble;
	private int lastensemble;
	private int etat;
	public String getTable_liste() {
		return table_liste;
	}
	public void setTable_liste(String table_liste) {
		this.table_liste = table_liste;
	}
	public String getIdtable() {
		return idtable;
	}
	public void setIdtable(String idtable) {
		this.idtable = idtable;
	}
	@Parameter(libelle="Table")
	private String table_liste;
	private String idtable;
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
	public String getOption() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String rr=super.getOption();
		if(rr=="-")
			rr="";
		rr+="<a href=\"main.jsp?cible=commande/commande-fiche&id="+idensemble+"\" class=\"btn btn-primary btn-xs\">voir</a>";
		return rr;
	}
	public int getEtat() {
		return etat;
	}
	public void setEtat(int etat) {
		this.etat = etat;
	}
	public String etatString(){
		if(etat==1)
			return "Active";
		return "Annulée";
	}
	
}
