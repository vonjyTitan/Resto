package com.affichage;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mapping.OptionObject;
import com.annotations.ForeignKey;
import com.annotations.Required;
import com.mapping.DataEntity;
import com.mapping.ListPaginner;

import dao.DaoModele;

public class Champ {
	private Field field=null;
	private String nameField="";
	private String libelle="";
	private Object value=null;
	private String additionnale="";
	private Class type=null;
	private Method methodForChamp=null;
	private DataEntity entity=null;
	private ForeignKey fk=null;
	private boolean isTextarea=false;
	
	public ForeignKey getFk() {
		return fk;
	}
	public void setFk(ForeignKey fk) {
		this.fk = fk;
	}
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
	public boolean isForeignKey(){
		if(fk!=null)
			return true;
		if(field!=null){
			return (fk=field.getAnnotation(ForeignKey.class))!=null;
		}
		return false;
	}
	public List<OptionObject> getForeignKeyData()throws Exception
	{
		if(fk==null)throw new Exception("Pas de foreign key pour le champ "+nameField);
		if(fk.toclasse()!=null){
			DataEntity entity=(DataEntity)fk.toclasse().newInstance();
			if(fk.totable()!=null && !fk.totable().isEmpty())
				entity.setNomTable(fk.totable());
			entity.setPackSize(100);
			List<DataEntity> rep=DaoModele.getInstance().findPageGenerique(1, entity);
			ListPaginner<OptionObject> options=new ListPaginner<OptionObject>();
			options.nbPage=((ListPaginner<DataEntity>)rep).nbPage;
			for(DataEntity item:rep)
			{
				options.add(new OptionObject(String.valueOf(item.getValueForField(item.getFieldByName(fk.pktable()))), String.valueOf(item.getValueForField(item.getFieldByName(fk.libtable())))));
			}
			return options;
		}
		OptionObject option=new OptionObject();
		option.setNomTable(fk.totable());
		option.setReferenceForField("id", fk.pktable());
		option.setReferenceForField("val", fk.libtable());
		option.setPackSize(100);
		return DaoModele.getInstance().findPageGenerique(1, option);
	}
	public boolean isTextarea() {
		return isTextarea;
	}
	public void setTextarea(boolean isTextarea) {
		this.isTextarea = isTextarea;
	}
}
