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
import utilitaire.SessionUtil;
import utilitaire.UtileAffichage;

public class HTMLBuilder<T extends DataEntity> {
	protected HttpServletRequest request;
	protected List<Champ> fieldsAvalaible;
	protected T entity;
	private List<String> notVisibleChamp=null;
	protected ERROR_SHOW showErrorMode=ERROR_SHOW.POP_UP;
	
	protected String classForForm="form-horizontal style-form";
	
	public HTMLBuilder(T entity,HttpServletRequest request) throws Exception{
		this.request=request;
		this.entity=entity;
		fieldsAvalaible=new ArrayList<Champ>();
		for(Field field:entity.getAllFields()){
			fieldsAvalaible.add(new Champ(field,field.getName(),entity,null));
		}
		this.setEntity(getValue());
		notVisibleChamp=new ArrayList<String>();
		
	}
	public Champ getFieldByName(String name){
		for(Champ champ:fieldsAvalaible){
			if(champ.getName().compareToIgnoreCase(name)==0)
				return champ;
		}
		return null;
	}
	public void setLibelleFor(String champ,String nom)throws Exception{
		Champ f=getFieldByName(champ);
		if(f==null)
			throw new Exception("Champ "+champ+" introuvable");
		f.setLibelle(nom);
	}
	public void setLibelleFor(String []champ,String []nom)throws Exception{
		if(champ.length!=nom.length)
			throw new Exception("La longueur des de la table champ et libelle doivent etre identique !");
		for(int i=0;i<champ.length;i++)
			setLibelleFor(champ[i],nom[i]);
	}

	public static String beginPanel(String title,int taille,String mg){
		return "<div class=\"col-lg-"+taille+" col-md-"+taille+" col-sm-"+taille+" "+mg+"\">"
				+"<div class=\"panel panel-primary box-solid\">"
 		+"<div class=\"blue-header\"><h5>"+title+"</h5></div><div class=\"panel-body form-horizontal style-form\">";
	}
	public static String beginPanel(String title,int taille){
		return beginPanel(title,taille,"mt");
	}
	public static String beginPanel(String title){
		return beginPanel(title,6);
	}
	public static String endPanel(){
		return "</div>"
				+ "</div>"
				+ "</div>";
	}
	
	public boolean isNotVisible(Champ f){
		for(String s:notVisibleChamp){
			if(f.getName().compareToIgnoreCase(s)==0)
				return true;
		}
		return false;
	}
	public void additionForChamp(String champ,String addition)throws Exception{
		Champ field=getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		field.setAdditionnale(addition);
	}
	public void removeChamp(String champ)throws Exception{
		Champ field=getFieldByName(champ);
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
		entity.setNomTable(this.entity.findReference());
		entity.setLienForModif(this.entity.getLienForModif());
		entity.setLienForDelete(this.entity.findLienForDelete());
		this.entity = entity;
	}
	protected Object defaultValudeForField(Champ f) throws Exception{
		Object val=null;
		if(f.getField()==null){
			val = f.getValue();
		}
		else
			val=getEntity().getValueForField(f.getField());
		if(val!=null){
			return val;
		}
		
		return "";
	}
	public T getValue() throws Exception{
		T reponse=(T) getEntity().getClass().newInstance();
		//Field[] fields=reponse.getAllFields();
		for(Champ field:fieldsAvalaible)
		{
			if(SessionUtil.getValForAttr(request,field.getName())!=null)
			{
				if(SessionUtil.getValForAttr(request,field.getName()).isEmpty())
					continue;
				//test 
				if(field.getField()==null){
					field.setValue(UtileAffichage.parseFromRequest(SessionUtil.getValForAttr(request,field.getName()),field.getType()));
					continue;
				}
				reponse.setValueForField(field.getField(), UtileAffichage.parseFromRequest(SessionUtil.getValForAttr(request,field.getName()),field.getType()));
			}
		}
		if(request.getParameter("nomChampOrder")!=null)
			if(!request.getParameter("nomChampOrder").isEmpty() && request.getParameter("nomChampOrder").compareToIgnoreCase("null")!=0)
				reponse.setNomChampOrder(request.getParameter("nomChampOrder"));
		if(request.getParameter("ordering")!=null)
			if(!request.getParameter("ordering").isEmpty() && request.getParameter("ordering").compareToIgnoreCase("null")!=0)
				reponse.setOrdering(request.getParameter("ordering"));
		return reponse;
	}
	public void setOrdre(String[] ordre){
		int rang=0;
		for(String s:ordre)
		{
			Champ champ=getFieldByName(s);
			if(champ!=null)
			{
				fieldsAvalaible.remove(champ);
				fieldsAvalaible.add(rang, champ);
				rang++;
			}
		}
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
