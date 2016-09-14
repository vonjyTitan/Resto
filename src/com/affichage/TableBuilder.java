package com.affichage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mapping.DataEntity;
import com.mapping.ListPaginner;
import com.mapping.DataEntity;

import dao.DaoModele;
import utilitaire.UtileAffichage;

public class TableBuilder<T extends DataEntity>  extends HTMLBuilder<T>{
	private List<T> data=null;
	private String lienForId=null;
	private String apresWhere="";
	private String lien="";
	private int actualPage=1;
	private String classForTabe="table table-striped table-advance table-hover";
	private Map<Field,String> classForEntete=null;
	private Map<Field,String> lienForChamp=null;
	private Map<Field,Field> idchampForchamp=null;
	private FilterBuilder filterBuilder=null;
	
	public TableBuilder(T entity,HttpServletRequest request) throws Exception{
		super(entity,request);
		this.request=request;
		classForEntete=new HashMap<Field,String>();
		lienForChamp=new HashMap<Field,String>();
		idchampForchamp=new HashMap<Field,Field>();
		setFilterBuilder(new FilterBuilder(entity, request));
	}
	public TableBuilder(T entity,ListPaginner<T> data,HttpServletRequest request) throws Exception{
		this(entity,request);
		this.setData(data);
		this.request=request;
	}
	
	public String getHTML(boolean withcheckbox) throws Exception{
		testData();
		String reponse="";
		reponse+="<table class=\""+getClassForTabe()+"\">";
		
		reponse+="<thead>";
		reponse+="<tr>";
		if(withcheckbox){
			reponse+="<th></th>";
		}
		reponse+="<th>"+entity.getPkName()+"</th>";
		for(Field f:fieldsAvalaible){
			if(f.getName().compareToIgnoreCase(entity.getPkName())==0)
				continue;
			reponse+="<th>"+getSigne(f)+" "+entity.getLibelleForField(f)+"</th>";
		}
		reponse+="<th>Options</th>";
		reponse+="<tr>";
		reponse+="</thead>";
		reponse+="<tbody>";
		for(DataEntity ob:data)
		{
			reponse+="<tr>";
			if(withcheckbox){
				reponse+="<td><input type=\"checkbox\" value=\""+ob.getValueForField(entity.getFieldByName(entity.getPkName()))+"\" name=\""+entity.getPkName()+"\"/></td>";
			}
			String idval="<td>"+ob.getValueForField(entity.getFieldByName(entity.getPkName()))+"</td>";
			if(lienForId!=null){
				idval="<td><a href=\""+lienForId+"&id="+ob.getValueForField(entity.getFieldByName(entity.getPkName()))+"\">"+ob.getValueForField(entity.getFieldByName(entity.getPkName()))+"</a></td>";
			}
			
			reponse+=idval;
			for(Field f:fieldsAvalaible){
				if(f.getName().compareToIgnoreCase(entity.getPkName())==0)
					continue;
				Object value=ob.getValueForField(f);
				if(f.getType().equals(Date.class) || f.getType().equals(java.sql.Date.class)){
					value=UtileAffichage.formatAfficherDate((java.sql.Date) value);
				}
				else if(f.getType().equals(Double.class) || f.getType().equals(double.class) || f.getType().equals(Float.class) || f.getType().equals(float.class)){
					value=UtileAffichage.formatMoney((double) value);
				}
				reponse+="<td>"+getLienForField(f, value)+"</td>";
			}
			reponse+="<td>"+ob.getOption()+"</td>";
			reponse+="</tr>";
		}
		reponse+="</tbody>";
		reponse+= "</table>";
		return reponse;
	}
	private String getSigne(Field f){
		if(classForEntete.containsKey(f))
			return "<i class=\""+classForEntete.get(f)+"\"></i>";
		return "";
	}
	public String getPaginnation() throws Exception
	{
		testData();
		String reponse="<p>";
		int page=((ListPaginner<T>)data).nbPage;
		if(actualPage>1)
			reponse+="<a href=\""+getLien()+"&page="+(actualPage-1)+"\"><< precedant</a>";
		for(int i=1;i<=page;i++){
			if(i==actualPage){
				reponse+=" "+i+" ";
			}
			else
				reponse+="<a href=\""+getLien()+"&page="+i+"\"> "+i+" </a>";
		}
		if(actualPage<page){
			reponse+="<a href=\""+getLien()+"&page="+(actualPage+1)+"\"> suivant >></a>";
		}
		return reponse+="</p>";
	}
	private void testData()throws Exception{
		actualPage=Integer.valueOf("0"+((request.getParameter("page")!=null) ? request.getParameter("page") : "1"));
		if(actualPage<1){
			actualPage=1;
			data=DaoModele.getInstance().findPageGenerique(actualPage, entity,apresWhere);
		}
	}
	public String getFilterString() throws Exception{
		String reponse="";
		Field[]fields=entity.getAllFields();
		for(Field f:fields)
		{
			reponse+="&"+nameForChamp(f)+"="+UtileAffichage.getNonNullValue(entity.getValueForField(f), f);
		}
		reponse+="&nomChampOrder="+entity.findNomChampOrder()+"&ordering="+entity.findOrdering();
		return reponse;
	}
	public void setClassForEntete(String champ,String classe)throws Exception{
		Field f=entity.getFieldByName(champ);
		if(f==null)
			throw new Exception("Champ "+champ+" introuvable");
		classForEntete.put(f, classe);
	}
	public void setClassForEntete(String []champ,String []classe)throws Exception{
		for(int i=0;i<champ.length;i++){
			setClassForEntete(champ[i],classe[i]);
		}
	}
	private String getLienForField(Field f,Object value) throws Exception{
		String reponse="";
		Field fid=idchampForchamp.get(f);
		if(fid!=null){
			Object id=entity.getValueForField(fid);
			return "<a href=\""+lienForChamp.get(f)+"&id="+id+"\">"+value+"</a>";
		}
		return (String) value;
	}
	public void setLienForChamp(String champ,String lien,String nomidchamp) throws Exception{
		Field fchamp=entity.getFieldByName(champ);
		Field fid=entity.getFieldByName(nomidchamp);
		if(fchamp==null || fid==null)
			throw new Exception("Champ "+champ+" ou "+nomidchamp+" introuvable");
		lienForChamp.put(fchamp, lien);
		idchampForchamp.put(fchamp, fid);
	}
	public void setLienForChamp(String []champ,String []lien,String []nomidchamp) throws Exception{
		for(int i=0;i<champ.length;i++)
			setLienForChamp(champ[i],lien[i],nomidchamp[i]);
	}
	public String getHTML() throws Exception{
		return getHTML(false);
	}
	public String getHTMLWithCheckbox() throws Exception{
		return getHTML(true);
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String getLienForId() {
		return lienForId;
	}
	public void setLienForId(String lienForId) {
		this.lienForId = lienForId;
	}
	public String getApresWhere() {
		return apresWhere;
	}
	public void setApresWhere(String apresWhere) {
		this.apresWhere = apresWhere;
	}
	public String getLien() throws Exception {
		if(lien!="" && lien!=null)
			return lien;
		return request.getRequestURI()+"?cible="+request.getParameter("cible")+getFilterString();
	}
	public String getLienWithOrder(Field f){
		String lastChampOrder=entity.findNomChampOrder();
		String lastOrder=entity.findOrdering();
		try{
			entity.setNomChampOrder(entity.getReferenceForField(f));
			entity.setOrdering(DataEntity.ASC);
			if(lastChampOrder.compareTo(entity.getReferenceForField(f))==0 && lastOrder==DataEntity.ASC)
			{
				entity.setOrdering(DataEntity.DESC);
			}
			return getLien();
		}
		catch(Exception ex)
		{
			
		}
		finally{
			entity.setNomChampOrder(lastChampOrder);
			entity.setOrdering(lastOrder);
		}
		return "";
	}
	public void setLien(String lien) {
		this.lien = lien;
	}
	public String getClassForTabe() {
		return classForTabe;
	}
	public void setClassForTabe(String classForTabe) {
		this.classForTabe = classForTabe;
	}
	public FilterBuilder getFilterBuilder() {
		return filterBuilder;
	}
	public void setFilterBuilder(FilterBuilder filterBuilder) {
		this.filterBuilder = filterBuilder;
	}
}
