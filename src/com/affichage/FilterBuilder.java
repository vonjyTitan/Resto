package com.affichage;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.affichage.InsertUpdateBuilder.ERROR_SHOW;
import com.mapping.DataEntity;

import utilitaire.UtileAffichage;

public class FilterBuilder<T extends DataEntity> extends FormBuilder<T> {
	
	private String lien="";
	private TableBuilder<DataEntity> tableau=null;
	
	public FilterBuilder(T entity, HttpServletRequest request,TableBuilder t) throws Exception {
		super(entity, request);
		this.tableau=t;
		defaultClassForContainer="form-group col-lg-4";
		setDefaultClassForInputContainer("col-sm-6 col-lg-6 col-md-6");
		defauldClassForLabel="col-sm-6 col-sm-6";
		
	}
	
	public String getHTML() throws Exception{
		tableau.setWithFilter(true);
		String reponse=beginHTMLForm();
		reponse+=HTMLBuilder.beginPanel("Filtre",12,"");
		reponse+=getHTMLBody();
		reponse+=getHTMLButton();
		reponse+=HTMLBuilder.endPanel();
		reponse+=endHTMLForm();
		return reponse;
	}
	private void setApresWhereForTable(){
		String awhere="";
		int cp=0;
		for(Champ f:fieldsAvalaible){
			if(f.getField()==null)
			{
				if(f.getValue()!=null){
					Object value=null;
					if(f.getType().equals(java.util.Date.class) || f.getType().equals(java.sql.Date.class))
					{
						value="'"+UtileAffichage.getNonNullValue(f.getValue(), f.getType())+"'";
					}
					else
						value=f.getValue();
					if(cp==0){
						awhere+=" and "+f.getName().subSequence(0, f.getName().length()-1)+" >= "+value;
					}
					else if(cp==1)
						awhere+=" and "+f.getName().subSequence(0, f.getName().length()-1)+" <= "+value;
				}
				cp++;
				if(cp==2)
					cp=0;
			}
		}
		tableau.setApresWhere(tableau.getApresWhere()+" "+awhere);
	}
	public String beginHTMLForm()throws Exception{
		setEntity(getValue());
		setApresWhereForTable();
		String reponse="";	
		 reponse+="<div class=\"row mt\">"
		 	+"<div class=\"col-lg-12\">"
		 	+"<div class=\"col-lg-12\">";
		 if(lien==""){
			 lien=request.getRequestURI()+"?cible="+request.getParameter("cible");
		 }
		reponse+= "<form action=\""+lien+"\" method=\"POST\" name=\""+getEntity().getClass().getSimpleName().toLowerCase().toLowerCase()+"form\" id=\""+getEntity().getClass().getSimpleName().toLowerCase()+"form\" class=\""+classForForm+"\">";
		return reponse;
	}
	public String getHTMLButton(){
		String reponse="<div class=\"col-lg-12\">";
		reponse+="<label class=\"control-label col-lg-4\"></label>"
              	+"<div class=\"col-lg-8\">";
		reponse+=" <input type=\"submit\"  class=\""+classForValidation+"\" value=\"Chercher\"></input>";
		reponse+=" <input type=\"reset\"  class=\""+classForReset+"\" value=\"Reinitialiser\"></input>";
		reponse+="</div>";
		reponse+="</div>";
		return reponse;
	}
	public String labelleFor(Champ f,String classe){
		if(getEntity().findFormError()!=null){
			String mess=getEntity().findFormError().get(f);
			if(mess!=null && (showErrorMode==ERROR_SHOW.COLOR_ALL || showErrorMode==ERROR_SHOW.COLOR_LABELLE))
			{
				classe+=" "+getDefaultClassForError();
			}
		}
		return "<div  class=\""+classe+"\"><label class=\"control-label\" for=\""+f.getName().toLowerCase()+"\" >"+f.getLibelle()+" : </label></div>";
	}
	public void setChampToInterval(String champ)throws Exception{
		Champ field=getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introuvable");
		if(!DataEntity.isNumberType(field.getType()) && !DataEntity.isDateType(field.getType()))
			throw new Exception("Champ interval integer ou date seulement");
		int index=fieldsAvalaible.indexOf(field);
		String name=field.getName();
		field.setName(field.getName()+"1");
		field.setField(null);
		Champ second=new Champ(null,name+"2",entity,null);
		second.setLibelle(field.getLibelle()+" max");
		field.setLibelle(field.getLibelle()+" min");
		second.setType(field.getType());
		fieldsAvalaible.add(index+1, second);
	}
	public void setChampToInterval(String []champ)throws Exception{
		for(String cham:champ)
			setChampToInterval(cham);
	}
	public String getLien() {
		return lien;
	}

	public void setLien(String lien) {
		this.lien = lien;
	}
	public void removeChamp(String champ)throws Exception{
		Champ field=getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		tableau.getEntity().setValueForField(field.getField(), entity.getClass().newInstance().getValueForField(field.getField()));
		fieldsAvalaible.remove(field);
	}

}
