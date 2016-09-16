package com.affichage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.annotations.Required;
import com.mapping.DataEntity;

public class Champ {
	private Field field=null;
	private String nameField="";
	private String libelle="";
	private Object value=null;
	private String additionnale="";
	private Class type=null;
	private Method methodForChamp=null;
	private DataEntity entity=null;
	
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public String getName() {
		return nameField;
	}
	public void setName(String name) {
		this.nameField = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Champ(Field field,String name,DataEntity entity,Object value){
		this.entity=entity;
		this.field=field;
		this.nameField=name;
		this.value=value;
		this.libelle=(field!=null) ? entity.getLibelleForField(field) : name;
		this.type=(field!=null) ? field.getType() : null;
		if(field!=null){
			try{
				methodForChamp=entity.getClass().getMethod("get"+DataEntity.getToUp(field.getName()), null);
			}
			catch(Exception ex){
				try {
					methodForChamp=entity.getClass().getMethod("find"+DataEntity.getToUp(field.getName()), null);
				} catch (NoSuchMethodException e) 
				{
					
				}
			}
			
		}
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getAdditionnale() {
		return additionnale;
	}
	public void setAdditionnale(String additionnale) {
		this.additionnale = additionnale;
	}
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}
	public Method getMethodForChamp() {
		return methodForChamp;
	}
	public void setMethodForChamp(Method methodForChamp) {
		this.methodForChamp = methodForChamp;
	}
	public void setMethodForChamp(String methodForChamp) throws NoSuchMethodException, SecurityException {
		this.methodForChamp = entity.getClass().getMethod(methodForChamp, null);
	}
}
