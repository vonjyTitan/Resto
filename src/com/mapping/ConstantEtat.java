package com.mapping;

public class ConstantEtat {
	/*Etat for table*/
	public static final int ETAT_TABLE_LIBRE=1;
	public static final int ETAT_TABLE_OCCUPER_SANS_COMMANDE=2;
	public static final int ETAT_TABLE_OCCUPER_AVEC_COMMANDE=3;
	public static final int ETAT_TABLE_RESERVER=4;
	/*Etat for table*/
	
	/*Etat for commande*/
	public static final int ETAT_COMMANDE_EN_COUR=1;
	public static final int ETAT_COMMANDE_ANNULER=2;
	/*Etat for commande*/
}
