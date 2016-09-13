package com.affichage;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.affichage.InsertUpdateBuilder.ERROR_SHOW;
import com.mapping.DataEntity;

public class FilterBuilder<T extends DataEntity> extends FormBuilder<T> {
	
	private String lien="";
	private List<Field> interval=null;
	
	public FilterBuilder(T entity, HttpServletRequest request) {
		super(entity, request);
		defaultClassForContainer="form-group col-lg-4";
		interval=new ArrayList<Field>();
	}
	
	public String beginHTMLForm()throws Exception{
		String reponse="";	
		 reponse+="<div class=\"row mt\">"
		 	+"<div class=\"col-lg-12\">"
		 	+"<div class=\"form-panel col-lg-12\">";
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
	public void setInterval(String[]champs)throws Exception
	{
		Field field=null;
		for(String champ:champs)
		{
			field=getEntity().getFieldByName(champ);
			if(field==null)
				throw new Exception("Champ "+champ+" introvable");
			interval.add(field);
		}
	}
	public String getLien() {
		return lien;
	}

	public void setLien(String lien) {
		this.lien = lien;
	}

}
