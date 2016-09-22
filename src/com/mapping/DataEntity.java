package com.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.ListenerNotFoundException;
import javax.swing.text.DateFormatter;

import com.annotations.Entity;
import com.annotations.Extension;
import com.annotations.ForeignKey;
import com.annotations.NumberRestrict;
import com.annotations.Parameter;
import com.annotations.Required;
import com.annotations.StringRestrict;

import dao.DaoModele;
import utilitaire.UtileAffichage;

import com.annotations.DateRestrict;

public abstract class DataEntity {
	// ===========================================================
    // Fields
    // ===========================================================
	
	private boolean isZeroInclue=false;
	private Field[] fields;
	private Class self=this.getClass();
	private Map<Field,String> formError=null;
	private TypeComparaison typeComparison=TypeComparaison.PARTIAL;
	private boolean isIgnoreCase=true;
	private String nomTable;
	private int packSize=30;
	private String dataBaseKey="resto";
	private Map<String, String> concatString=new HashMap<String,String>();
	private String nomChampOrder;
	private String ordering;
	public static final String ASC=" asc ";
	public static final String DESC=" desc ";
	private  List<Field> summField=new ArrayList<Field>();
	private  List<Field> groupField=new ArrayList<Field>();
	private Map<Field,String> referenceForField=new HashMap<Field,String>();
	private static Map<Class,Field[]> fieldsForClasses=new HashMap<Class,Field[]>();
	private int count=0;
	private String lienForModif="";
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
	public enum TypeComparaison{
		PARTIAL,
		COMPLET
	}
	
	// ===========================================================
    // constructors
    // ===========================================================
	
	// ===========================================================
    // public method
    // ===========================================================
	

	public void setNomTable(String nomTable) {
		this.nomTable = nomTable;
	}
	
	public String findReference(){
		if(this.nomTable!=null && this.nomTable!="")
			return this.nomTable;
		Class classe=this.getClass();
		Entity entity=(Entity) classe.getAnnotation(Entity.class);
		if(entity==null)
			return classe.getSimpleName().toLowerCase();
		if(entity.reference()=="" || entity.reference()==null)
			return classe.getSimpleName().toLowerCase();
		return entity.reference();
	}
	public String getReferenceForField(Field field){
		if(referenceForField.containsKey(field))
			return referenceForField.get(field);
		Parameter annotation=field.getAnnotation(Parameter.class);
		String fname=field.getName();
		if(annotation==null)
			return fname;
		return (annotation.reference().isEmpty()) ? fname : annotation.reference();
	}
	public void setReferenceForField(String champ,String reference) throws Exception
	{
		Field f=getFieldByName(champ);
		if(f==null)
			throw new Exception("Champ "+champ+" introuvable");
		setReferenceForField(f,reference);
	}
	public void setReferenceForField(Field champ,String reference)
	{
		referenceForField.put(champ, reference);
	}
	public String getLibelleForField(Field field){
		Parameter annotation=field.getAnnotation(Parameter.class);
		String fname=getToUp(field.getName());
		if(annotation==null)
			return fname;
		return (annotation.libelle()=="" || annotation.libelle()==null) ? fname : getToUp(annotation.libelle());
	}
	public boolean isFieldRequired(Field field){
			return (field.getAnnotation(Required.class)!=null);
	}
	public boolean isValide() throws Exception{
		Field[]fields=this.getAllFields();
		setFormError(new HashMap<Field,String>());
		
		Method met=null;
		Class inter=null;
		Annotation annotation=null;
		String errorMessage="";
		Object value=null;
		
		for(Field f:fields){
			inter=f.getType();
			try {
				met=self.getMethod("get"+getToUp(f.getName()), null);
			} catch (NoSuchMethodException | SecurityException e) {
				try{
					met=self.getMethod("find"+getToUp(f.getName()), null);
				}
				catch(Exception ex){
					
				}
			}
			try {
				value=met.invoke(this, null);
				if((annotation=f.getAnnotation(Required.class))!=null){
					boolean req=false;
					req=value==null ;
					if((f.getType().equals(String.class))){
						req=req || value=="" ;
					}
					if(isNumberType(f.getType()))
					{
						req=req || Double.valueOf(String.valueOf(value))==0;
					}
					if(req){
						errorMessage=((Required)annotation).messageOnAny().length()!=0 ? ((Required)annotation).messageOnAny() : "Le champ "+getLibelleForField(f)+" est obligatoire";
						formError.put(f, errorMessage);
					}
				}
				if(value!=null){
					
					if((annotation=f.getAnnotation(StringRestrict.class))!=null){
						valideChampString(f,(StringRestrict) annotation,value);
					}
					else if((annotation=f.getAnnotation(DateRestrict.class))!=null){
						valideChampDate(f, (DateRestrict) annotation, value);
					}
					
					else if((annotation=f.getAnnotation(NumberRestrict.class))!=null){
						valideChampNumber(f, (NumberRestrict) annotation, value);
					}
					ForeignKey fk=null;
					if((fk=f.getAnnotation(ForeignKey.class))!=null && isNumberType(f.getType())){
						if((int)value==0 && fk.nullable())
							continue;
						valideChampFK(f,fk,value);
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return formError.size()==0;
	}
	private void valideChampFK(Field f,ForeignKey fk,Object value){
		if(fk.toclasse()!=null){
			try{
				DataEntity entity=(DataEntity) fk.toclasse().newInstance();
				if(fk.totable()!=null && !fk.totable().isEmpty())
					entity.setNomTable(fk.totable());
				entity=DaoModele.getInstance().findById(entity, (int)entity.getPkValue());
				if(entity==null){
					formError.put(f, (fk.messageOnNotExiste()!=null && !fk.messageOnNotExiste().isEmpty())
							? fk.messageOnNotExiste() : "La valeur du champ "+this.getReferenceForField(f)+" doit etre dans "+entity.findReference());
				}
				return;
			}
			catch(Exception ex){
				ex.printStackTrace();
				return;
			}
		}
		try{
			OptionObject option=new OptionObject();
			option.setNomTable(fk.totable());
			List<OptionObject> ress=DaoModele.getInstance().findPageGenerique(1, option," and "+fk.pktable()+"="+value);
			if(ress.size()==0){
				formError.put(f, (fk.messageOnNotExiste()!=null && !fk.messageOnNotExiste().isEmpty())
						? fk.messageOnNotExiste() : "La valeur du champ "+this.getReferenceForField(f)+" doit etre dans "+fk.totable());
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	private void valideChampString(Field f,StringRestrict restriction,Object value){
		if(!(value instanceof String))
			return;
		String val=(String)value;
		int l=val.length();
		String message=restriction.messageOnNoInvalide();
		if(l>restriction.maxLength() && restriction.maxLength()>0 ){
			formError.put(f, (message.isEmpty()) ? "La longueur du champ "+getLibelleForField(f)
			+" doit etre inferieur a "+restriction.maxLength() :
				message
					);
		}
		else if( val.length()<restriction.minLength()){
			formError.put(f, (message.isEmpty()) ? "La longueur du champ "+getLibelleForField(f)
			+" doit etre superieur a "+restriction.minLength() :
				message
					);
		}
	}
	private void valideChampNumber(Field f,NumberRestrict restriction,Object value){
		if(!isNumberType(f.getType()))
			return;
		double val=Double.valueOf(String.valueOf(value));
		boolean minValide=false;
		boolean maxValide=false;
		boolean zeroValide=false;
		Object minVal=UtileAffichage.numberVal(restriction.min(),f.getType());
		Object maxVal=UtileAffichage.numberVal(restriction.max(),f.getType());
		Object zeroVal=UtileAffichage.numberVal(0.0,f.getType());
		String message=restriction.messageOnNoInvalide();
		if(maxValide=(val>restriction.max() && restriction.max()!=0)){
			message=message.length()!=0 ? message
					: "La valeur du champ "+getLibelleForField(f)+" doit etre inferieur a "+maxVal;
		}
		if(minValide=(val<restriction.min() && restriction.min()!=0)){
			message=message.length()!=0 ? message
					: "La valeur du champ "+getLibelleForField(f)+" doit etre superieur a "+minVal;
		}
		if(zeroValide=(val==0 && !restriction.zeroInclue())){
			message=message.length()!=0 ? message
					: "La valeur du champ "+getLibelleForField(f)+" ne doit pas etre egal a "+zeroVal+" ";
		}
		if(maxValide || minValide){
			message=message.length()!=0 ? message
					: "La valeur du champ "+getLibelleForField(f)+" doit etre compris entre "+minVal+" et "+maxVal;
		}
		if(maxValide || minValide || zeroValide)
		{
			formError.put(f, message);
		}
	}
	public Field getFieldByName(String name){
		Field[]fields=this.getAllFields();
		for(Field f:fields){
			if(f.getName().compareToIgnoreCase(name)==0){
				return f;
			}
		}
		return null;
	}
	public Object getValueForField(Field f) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Method met=null;
		try{
			met=this.getClass().getMethod("get"+getToUp(f.getName()), null);
		}
		catch(Exception ex){
			try{
				met=this.getClass().getMethod("find"+getToUp(f.getName()), null);
			}
			catch(Exception exp){
				
			}
		}
		return met.invoke(this, null);
	}
	public void setValueForField(Field f,Object value)throws Exception{
		Method met=this.getClass().getMethod("set"+getToUp(f.getName()), new Class[]{f.getType()});
		if(f.getType().equals(boolean.class) || f.getType().equals(Boolean.class)){
			value=new Boolean((String)value);
		}
		met.invoke(this, value);
	}
	private void valideChampDate(Field f,DateRestrict restriction,Object value){
		if(!(value instanceof Date))
			return;
		Date val=(Date)value;
		Date before=null;
		Date after=null;
		boolean beforeValide=true;
		boolean afterValide=true;
		String message="";
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
		try {
			after=df.parse(restriction.after());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			before = df.parse(restriction.before());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(after!=null){
			if(afterValide=after.before(val)){
				message= restriction.messageOnNoInvalide().length()!=0 ?
						restriction.messageOnNoInvalide():
							"La valeur du champ "+getLibelleForField(f)+" doit pas depasse le "+df.format(after);
			}
		}
		if(before!=null){
			if(beforeValide=before.after(val)){
				message= restriction.messageOnNoInvalide().length()!=0 ?
						restriction.messageOnNoInvalide():
							"La valeur du champ "+getLibelleForField(f)+" doit pas etre avant le "+df.format(before);
			}
		}
		if((beforeValide || afterValide) && after!=null && before!=null){
			message= restriction.messageOnNoInvalide().length()!=0 ?
					restriction.messageOnNoInvalide():
						"La valeur du champ "+getLibelleForField(f)+" doit etre compris entre "+df.format(after)+" et "+df.format(before);
		}
		if(beforeValide || afterValide)
		{
			formError.put(f, message);
		}
	}
	public Field[] getAllFields(){
		if(fields!=null)
			return fields;
		if(fieldsForClasses.containsKey(self))
			return fields=fieldsForClasses.get(self);
		Class classe=self;
		List<Field> lc=new ArrayList<Field>(); 
		Field[]inter=null;
		while(classe!=DataEntity.class && classe.getAnnotation(Extension.class)==null){
			inter=classe.getDeclaredFields();
			for(Field f:inter){
				if(isBaseType(f.getType()))
					lc.add(f);
			}
			classe=classe.getSuperclass();
		}
		fieldsForClasses.put(self, fields=lc.toArray(new Field[]{}));
		return fields;
	}
	public boolean isBaseType(Class classe){
    	Class[] liste={int.class,Double.class,String.class,double.class,Number.class,java.util.Date.class,java.sql.Date.class,Boolean.class,boolean.class};
    	for(int i=0;i<liste.length;i++){
    		if(classe.getName()==liste[i].getName())
    			return true;
    	}
    	return false;
    }
	public Map<Field,Object> getFieldsFilter(Connection conn) throws Exception{
		Map<Field,Object> reponse=new HashMap<Field,Object>();
		Field[]fields=this.getAllFields();
		Method met=null;
		Class cl=self;
		Object value=null;
		
		for(Field f:getAllFields()){
			if(!isBaseType(f.getType()))
				continue;
			if(!DaoModele.getInstance().isExisteChamp(this.getReferenceForField(f), findReference(), conn))
				continue;
			try{
				met=cl.getMethod("get"+getToUp(f.getName()), null);
			}
			catch(Exception ex){
				try{
					met=cl.getMethod("find"+getToUp(f.getName()), null);
				}
				catch(Exception exp){
					
				}
			}
			try {
				value=met.invoke(this, null);
				if(value==null)continue;
				if(f.getType()==String.class){
					if(((String)value).length()==0)continue;
					if(this.typeComparison==TypeComparaison.PARTIAL){
						value="%"+value+"%";
					}
				}
				else if(isNumberType(f.getType())){
					if((int)value==0 && !isZeroInclue){
						continue;
					}
				}
				reponse.put(f,value);
			} catch (Exception ex) {
				
			}
		}
		return reponse;
	}
	public String getPkName(){
		Class classe=this.getClass();
		Entity entity=(Entity) classe.getAnnotation(Entity.class);
		if(entity==null)
		{
			return "id";
		}
		if(entity.pkName()=="" || entity.pkName()==null)
			return "id";
		return entity.pkName();
	}
	public Object getPkValue() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return  this.getValueForField(this.getFieldByName(this.getPkName()));
	}
	public String getOption() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(lienForModif!=null && !lienForModif.isEmpty()){
			return " <a href=\""+lienForModif+"&id="+getValueForField(getFieldByName(getPkName()))+"\" class=\"btn btn-primary btn-xs\"><i class=\"fa fa-pencil\"></i></a> ";
		}
		return "";
	}
	public void addCountChamp(String champ)throws Exception{
		Field f=this.getFieldByName(champ);
		if(f!=null)
			throw new Exception("Champ "+champ+" introuvable");
		this.summField.add(f);
	}
	public void addCountChamp(String[]champs)throws Exception{
		for(String s:champs)
			addCountChamp(s);
	}
	public void addGroupChamp(String champ)throws Exception{
		Field f=this.getFieldByName(champ);
		if(f!=null)
			throw new Exception("Champ "+champ+" introuvable");
		this.groupField.add(f);
	}
	public void addGroupChamp(String[]champs)throws Exception{
		for(String s:champs)
			addGroupChamp(s);
	}
	// ===========================================================
    // private method
    // ===========================================================
	public static boolean isNumberType(Class f){
		Class[] numClass={int.class,Integer.class,double.class,Double.class,float.class,Float.class,long.class
				,Long.class};
		for(Class cl:numClass)
			if(cl==f)
				return true;
		return false;
	}
	public static String getToUp(String str){
		return  str.substring(0, 1).toUpperCase()+str.substring(1);
	}
	
	// ===========================================================
    // methods for interfaces
    // ===========================================================
	
	// ===========================================================
    // getters and setters
    // ===========================================================
	
	

	public boolean isZeroInclue() {
		return isZeroInclue;
	}

	public void setZeroInclue(boolean isZeroInclue) {
		this.isZeroInclue = isZeroInclue;
	}
	public Map<Field,String> findFormError() {
		return formError;
	}
	public void setFormError(Map<Field,String> formError) {
		this.formError = formError;
	}
	public TypeComparaison findTypeComparison() {
		return typeComparison;
	}
	public void setTypeComparison(TypeComparaison typeComparison) {
		this.typeComparison = typeComparison;
	}
	public boolean isIgnoreCase() {
		return isIgnoreCase;
	}
	public void setIgnoreCase(boolean isIgnoreCase) {
		this.isIgnoreCase = isIgnoreCase;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Field> findSummField() {
		return summField;
	}

	public void setCountField(List<Field> countField) {
		this.summField = countField;
	}

	public List<Field> findGroupField() {
		return groupField;
	}

	public void setGroupField(List<Field> entetField) {
		this.groupField = entetField;
	}

	public static boolean isDateType(Class type) {
		return type.equals(java.util.Date.class) || type.equals(java.sql.Date.class);
	}

	public String getLienForModif() {
		return lienForModif;
	}

	public void setLienForModif(String lienForModif) {
		this.lienForModif = lienForModif;
	}
	
}
