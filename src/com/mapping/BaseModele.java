package com.mapping;

import java.util.HashMap;
import java.util.Map;

import dao.Connecteur.DatabaseType;

public class BaseModele extends DataEntity {
	
	private String nomChampOrder;
	private String ordering;
	private boolean ZeroComparaisonInclue=false;
	public static final String ASC=" asc ";
	public static final String DESC=" desc ";
	private int packSize=30;
	private String dataBaseKey="boky";
	private Map<String, String> concatString=new HashMap<String,String>();
	
	private String typeComparaison="%";
	
	

	public String findNomChampOrder() {
		return nomChampOrder;
	}

	public void setNomChampOrder(String nomChampOrder) {
		this.nomChampOrder = nomChampOrder;
	}

	public String findOrdering() {
		return ordering;
	}

	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}

	public String findTypeComparaison() {
		return typeComparaison;
	}

	public void setTypeComparaison(String typeComparaison) {
		this.typeComparaison = typeComparaison;
	}

	public boolean findZeroComparaisonInclue() {
		return ZeroComparaisonInclue;
	}

	public void setZeroComparaisonInclue(boolean zeroComparaisonInclue) {
		ZeroComparaisonInclue = zeroComparaisonInclue;
	}

	public int findPackSize() {
		return packSize;
	}

	public void setPackSize(int packSize) {
		this.packSize = packSize;
	}

	public String findDataBaseKey() {
		return dataBaseKey;
	}

	public void setDataBaseKey(String dataBaseKey) {
		this.dataBaseKey = dataBaseKey;
	}

	public Map<String, String> findConcatString() {
		return concatString;
	}

	public void setConcatString(Map<String, String> concatString) {
		this.concatString = concatString;
	}	
}
