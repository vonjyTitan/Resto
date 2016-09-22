package com.mapping;

import com.annotations.Entity;
import com.annotations.NumberRestrict;
import com.annotations.Parameter;
import com.annotations.Required;
import com.annotations.StringRestrict;

@Entity(pkName="idtable",reference="table_liste")
public class Table extends DataEntity {
	private int idtable;
	@Required
	@StringRestrict(minLength=2)
	private String nom;
	private int positionx;
	private int positiony;
	private int etat;
	@NumberRestrict(min=2)
	private int place;
	
	public int getIdtable() {
		return idtable;
	}
	public void setIdtable(int idtable) {
		this.idtable = idtable;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getPositionx() {
		return positionx;
	}
	public void setPositionx(int positionx) {
		this.positionx = positionx;
	}
	public int getPositiony() {
		return positiony;
	}
	public void setPositiony(int positiony) {
		this.positiony = positiony;
	}
	public int getEtat() {
		return etat;
	}
	public void setEtat(int etat) {
		this.etat = etat;
	}
	public String getEtatString() {
		if(etat==ConstantEtat.ETAT_OCCUPER_SANS_COMMANDE){
			return "<span class=\"label label-primary label-mini\">Occupé sans commande</span>";
		}
		else if(etat==ConstantEtat.ETAT_OCCUPER_AVEC_COMMANDE)
			return "<span class=\"label label-warning label-mini\">Occupé</span>";
		else if(etat==ConstantEtat.ETAT_LIBRE)
			return "<span class=\"label label-success label-mini\">Libre</span>";
		else if(etat==ConstantEtat.ETAT_RESERVER)
			return "<span class=\"label label-warning label-mini\">Reservé</span>";
		return "";
	}
	public String getOption(){
		String reponse=" <a class=\"btn btn-primary btn-xs\" href=\"main.jsp?cible=configuration/table-modif&id="+idtable+"\"><i class=\"fa fa-pencil\"></i></a> ";
		if(etat==ConstantEtat.ETAT_OCCUPER_SANS_COMMANDE){
			reponse+="<a class=\"btn btn-success btn-xs\" href=\"table-liberer\" style=\"width: 70px;\">Liberer</a>";
		}
		else if(etat==ConstantEtat.ETAT_OCCUPER_AVEC_COMMANDE)
			reponse+="<a class=\"btn btn-warning btn-xs\" href=\"table-transfert\" style=\"width: 70px;\">Transferer</a>";
		else if(etat==ConstantEtat.ETAT_LIBRE)
			reponse+="<a class=\"btn btn-primary btn-xs\" href=\"table-occuper\" style=\"width: 70px;\">Occuper</a>";
		else if(etat==ConstantEtat.ETAT_RESERVER)
			return reponse;
		return reponse;
	}
	public int getPlace() {
		return place;
	}
	public void setPlace(int place) {
		this.place = place;
	}
	
}
