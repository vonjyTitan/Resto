package com.affichage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.annotations.Required;

public class Champ {
	private Field field=null;
	private String nameField=null;
	private Object value=null;
	
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public String getNameField() {
		return nameField;
	}
	public void setNameField(String name) {
		this.nameField = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Champ(Field field,String name,Object value){
		this.field=field;
		this.nameField=name;
		this.value=value;
	}
}
