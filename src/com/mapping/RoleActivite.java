package com.mapping;

import com.annotations.Entity;

@Entity(reference="roleactivite")
public class RoleActivite extends DataEntity {
	private int idrole;
	private String activite;
	public int getIdrole() {
		return idrole;
	}
	public void setIdrole(int idrole) {
		this.idrole = idrole;
	}
	public String getActivite() {
		return activite;
	}
	public void setActivite(String activite) {
		this.activite = activite;
	}
	
}
