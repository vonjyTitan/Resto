package com.affichage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

import dao.DaoModele;
import com.mapping.DataEntity;

public class InsertUpdateBuilder<T extends DataEntity> extends FormBuilder<T> {
	
	private String cible=null;
	private String title=null;
	
	
	public InsertUpdateBuilder(T entity,String cible,HttpServletRequest request){
		super(entity,request);
		this.request=request;
		this.cible=cible;
		try{
			if(request.getParameter("erreur")!=null){
				setEntity(getValue());
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public String beginHTMLForm()throws Exception{
		String reponse="";
		if(this.getTitle()!=null) {
			reponse+="<h3><i class=\"fa fa-angle-right\"></i> "+this.getTitle()+"</h3>";
			reponse+="<script>document.title=\""+this.getTitle()+"\";</script>";
		}		
		 reponse+="<div class=\"row mt\">"
		 	+"<div class=\"col-lg-12\">"
		 	+"<div class=\"form-panel col-lg-12\">";
		reponse+= "<form action=\""+cible+"\" method=\"POST\" name=\""+getEntity().getClass().getSimpleName().toLowerCase().toLowerCase()+"form\" id=\""+getEntity().getClass().getSimpleName().toLowerCase()+"form\" class=\""+classForForm+"\">";
		try{
			if(getEntity().findFormError()!=null){
				if(showErrorMode==ERROR_SHOW.POP_UP){
					Set<Entry<Field,String>> fil=getEntity().findFormError().entrySet();
					for(Entry<Field,String> frst:fil){
						reponse+="<script>alert('"+frst.getValue()+"')</script>";
						break;
					}
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw ex;
		}
		return reponse;
	}
	
	public void removeChamp(String [] champ)throws Exception{
		for(String ch:champ)
			removeChamp(ch);
	}
	

	
	public void setValueFromDatabase(Object object)throws Exception{
		if(request.getParameter("erreur")==null){
			List<DataEntity> l=DaoModele.getInstance().findPageGenerique(1, (DataEntity) getEntity()," and "+getEntity().getPkName()+"="+object);
			if(l.size()==0)
				throw new Exception("Objet introuvable");
			setEntity((T) l.get(0));
		}
	}
	
	public ERROR_SHOW getShowErrorMode() {
		return showErrorMode;
	}
	public void setShowErrorMode(ERROR_SHOW showErrorMode) {
		this.showErrorMode = showErrorMode;
	}
	@Override
	public void setEntity(T entity) throws Exception {
		super.setEntity(entity);
		this.getEntity().isValide();
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public enum ERROR_SHOW{
		POP_UP,
		COLOR_LABELLE,
		COLOR_INPUT,
		COLOR_ALL
	}
}
