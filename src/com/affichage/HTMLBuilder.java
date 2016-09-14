package com.affichage;

import java.lang.reflect.Field;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.affichage.InsertUpdateBuilder.ERROR_SHOW;
import com.mapping.DataEntity;

import dao.DaoModele;

public class HTMLBuilder<T extends DataEntity> {
	protected HttpServletRequest request;
	protected List<Field> fieldsAvalaible;
	protected T entity;
	private Map<Field,String> nameForChamp;
	private List<String> notVisibleChamp=null;
	protected ERROR_SHOW showErrorMode=ERROR_SHOW.POP_UP;
	private Map<Field,String> additionnalForChamp;
	
	protected String classForForm="form-horizontal style-form";
	
	public HTMLBuilder(T entity,HttpServletRequest request) throws Exception{
		this.entity=entity;
		this.setEntity(getValue());
		this.request=request;
		notVisibleChamp=new ArrayList<String>();
		fieldsAvalaible=new ArrayList<Field>();
		nameForChamp=new HashMap<Field,String>();
		additionnalForChamp=new HashMap<Field,String>();
		for(Field field:entity.getAllFields()){
			fieldsAvalaible.add(field);
		}
	}
	public void renameChamp(String champ,String nom)throws Exception{
		Field f=getEntity().getFieldByName(champ);
		if(f==null)
			throw new Exception("Champ "+champ+" introvable");
		nameForChamp.put(f, nom);
	}
	public void renameChamp(String []champ,String []nom)throws Exception{
		if(champ.length!=nom.length)
			throw new Exception("La longueur des de la table champ et nom doivent etre identique !");
		for(int i=0;i<champ.length;i++)
			renameChamp(champ[i],nom[i]);
	}
	protected String nameForChamp(Field f){
		String add=nameForChamp.get(f);
		if(add!=null)
			return add;
		return f.getName().toLowerCase();
	}
	public static String beginPanel(String title,int taille){
		return "<div class=\"col-lg-"+taille+" col-md-"+taille+" col-sm-"+taille+" mb\">"
				+"<div class=\"white-panel pn box-solid\">"
 		+"<div class=\"blue-header\"><h5>"+title+"</h5></div>";
	}
	public static String beginPanel(String title){
		return beginPanel(title,6);
	}
	public static String endPanel(){
		return "</div>"
				+ "</div>";
	}
	
	public boolean isNotVisible(Field f){
		for(String s:notVisibleChamp){
			if(f.getName().compareToIgnoreCase(s)==0)
				return true;
		}
		return false;
	}
	public void additionForChamp(String champ,String addition)throws Exception{
		Field field=getEntity().getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		additionnalForChamp.put(field, addition);
	}
	public String getAdditionForField(Field field){
		String val=additionnalForChamp.get(field);
		if(val==null)
			return "";
		return val;
	}
	
	
	public void removeChamp(String champ)throws Exception{
		Field field=getEntity().getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		fieldsAvalaible.remove(field);
	}
	public void removeChamp(String []champ)throws Exception{
		for(String s:champ){
			removeChamp(s);
		}
	}
	public T getEntity() {
		return entity;
	}
	public void setEntity(T entity)throws Exception  {
		this.entity = entity;
	}
	public T getValue() throws Exception{
		T reponse=(T) getEntity().getClass().newInstance();
		Field[] fields=reponse.getAllFields();
		for(Field field:fields)
		{
			if(request.getParameter(nameForChamp(field))!=null)
			{
				if(request.getParameter(nameForChamp(field)).isEmpty())
					continue;
				if(field.getType().equals(java.util.Date.class) || field.getType().equals(java.sql.Date.class))
				{
					DateFormat form=new SimpleDateFormat("dd/MM/yyyy"); 
					reponse.setValueForField(field, form.parseObject(request.getParameter(nameForChamp(field))));
				}
				else if (field.getType().equals(int.class)){
					reponse.setValueForField(field, Integer.valueOf("0"+request.getParameter(nameForChamp(field))));
				}
				else if(field.getType().equals(double.class) || field.getType().equals(Double.class)){
					reponse.setValueForField(field, Double.valueOf("0"+request.getParameter(nameForChamp(field))));
				}
				else if(field.getType().equals(float.class) || field.getType().equals(Float.class)){
					reponse.setValueForField(field, Float.valueOf("0"+request.getParameter(nameForChamp(field))));
				}
				else reponse.setValueForField(field, request.getParameter(nameForChamp(field)));
			}
		}
		if(request.getParameter("nomChampOrder")!=null)
			if(!request.getParameter("nomChampOrder").isEmpty())
				reponse.setNomChampOrder(request.getParameter("nomChampOrder"));
		if(request.getParameter("ordering")!=null)
			if(!request.getParameter("ordering").isEmpty())
				reponse.setOrdering(request.getParameter("ordering"));
		return reponse;
	}
	public void addNotVisibleChamp(String champ){
		addNotVisibleChamp(new String[]{champ});
	}
	
	public void addNotVisibleChamp(String[] champ){
		for(String s:champ)
		{
			notVisibleChamp.add(s);
		}
	}
	
	
	
}
