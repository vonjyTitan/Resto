package com.affichage;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.affichage.InsertUpdateBuilder.ERROR_SHOW;
import com.annotations.SELECT_TYPE;
import com.mapping.DataEntity;
import com.mapping.OptionObject;

import dao.Connecteur;
import dao.DaoModele;

public class FormBuilder<T extends DataEntity> extends HTMLBuilder<T> {

	protected String defauldClassForLabel="col-sm-4 col-sm-4 ";
	private String defauldClassForInput="form-control";
	private String defaultClassForSelect="form-control col-lg-6";
	protected String defaultClassForContainer="form-group col-lg-12";
	protected String classForValidation="btn btn-primary";
	private String defaultClassForError="alert alert-warning";
	protected String classForReset="btn btn-danger";
	private String defaultClassForInputContainer="col-sm-7";
	private Map<Champ,List<OptionObject>> typeSelectGenerique;
	private Map<Champ,String> classForChamp;
	public FormBuilder(T entity, HttpServletRequest request) throws Exception {
		super(entity, request);
		typeSelectGenerique=new HashMap<Champ,List<OptionObject>> ();
		classForChamp=new HashMap<Champ,String>();
	}
	public String getHTML(int taille) throws Exception{
		String reponse=beginHTMLForm();
		reponse+=HTMLBuilder.beginPanel("Formulaire general",taille);
		reponse+=getHTMLBody();
		reponse+=getHTMLButton();
		reponse+=HTMLBuilder.endPanel();
		reponse+=endHTMLForm();
		return reponse;
	}
	public String getHTML() throws Exception{
		return getHTML(12);
	}
	public String beginHTMLForm()throws Exception{
		String reponse="";	
		 /*reponse+="<div class=\"row mt\">"
		 	+"<div class=\"col-lg-12\">";*/
		String lien=request.getRequestURI()+"?cible="+request.getParameter("cible");
		reponse+= "<form action=\""+lien+"\" method=\"POST\" name=\""+getEntity().getClass().getSimpleName().toLowerCase().toLowerCase()+"form\" id=\""+getEntity().getClass().getSimpleName().toLowerCase()+"form\" class=\""+classForForm+"\">";
		return reponse;
	}
	public String getHTMLBody() throws Exception{
		Connection conn=null;
		try{
			conn=Connecteur.getConnection(entity.findDataBaseKey());
			String reponse="";
			for(Champ f:fieldsAvalaible){
				
				if(f.getField()!=null && !DaoModele.getInstance().isExisteChamp(entity.getReferenceForField(f.getField()), entity.findReference(), conn))
					continue;
				reponse+=blockFor(f,false); 
			}
			return reponse;
		}
		catch(Exception ex){
			throw ex;
		}
		finally{
			if(conn!=null)
				conn.close();
		}
	}
	
	public String blockFor(String champ)throws Exception{
		Champ field=getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		return blockFor(field,true);
	}
	public String blockFor(Champ f,boolean withDelete)throws Exception{
		String reponse="";
		if(isNotVisible(f)){
			reponse+="<input type=\"hidden\" name=\""+f.getName()+"\" id=\""+f.getName()+"\" value=\""+defaultValudeForField(f)+"\" />";
			if(withDelete)
				removeChamp(f.getName());
			return reponse;
		}
		reponse+="<div id=\""+f.getName().toLowerCase()+"container\" class=\""+defaultClassForContainer+"\">";
		reponse+=labelleFor(f,defauldClassForLabel);
		reponse+="<div class=\""+getDefaultClassForInputContainer()+"\">";
		reponse+=inputFor(f, getClassForField(f.getName()),f.getAdditionnale(),withDelete);
		reponse+="</div>";
		reponse+="</div>";
		return reponse;
	}
	public String endHTMLForm(){
		String reponse="";
		reponse+="</form>";
		/*reponse+="</div>";
		reponse+="</div>";*/
		return reponse;
	}
	public String getHTMLButton(){
		String reponse="<div class=\""+defaultClassForContainer+"\">";
		reponse+="<label class=\"control-label col-lg-4\"></label>"
              	+"<div class=\"col-lg-8\">";
		reponse+=" <input type=\"submit\"  class=\""+classForValidation+"\" value=\"Valider\"></input>";
		reponse+=" <input type=\"reset\"  class=\""+classForReset+"\" value=\"Annuler\"></input>";
		reponse+="</div>";
		reponse+="</div>";
		return reponse;
	}
	public String endHTMLFormWithButton(){
		String reponse=getHTMLButton();
		reponse+="</form>";
		reponse+="</div>";
		reponse+="</div>";
		reponse+="</div>";
		return reponse;
	}
	public String labelleFor(String champ)throws Exception{
		return labelleFor(champ,defauldClassForLabel);
	}
	public String labelleFor(String champ,String classe) throws Exception{
		Champ field=getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		return labelleFor(field,classe);
	}
	public String labelleFor(Champ f,String classe){
		if(getEntity().findFormError()!=null){
			String mess=getEntity().findFormError().get(f);
			if(mess!=null && (showErrorMode==ERROR_SHOW.COLOR_ALL || showErrorMode==ERROR_SHOW.COLOR_LABELLE))
			{
				classe+=" "+getDefaultClassForError();
			}
		}
		return "<div  class=\""+classe+"\"><label class=\"control-label\" for=\""+f.getName().toLowerCase()+"\" >"+f.getLibelle()+" "+((getEntity().isFieldRequired(f.getField())) ? "*" : "")+" : </label></div>";
	}
	public String inputFor(String champ)throws Exception{
		return inputFor(champ,defauldClassForInput);
	}
	public void renameChamp(String champ,String nom)throws Exception{
		Champ f=getFieldByName(champ);
		if(f==null)
			throw new Exception("Champ "+champ+" introuvable");
		f.setName(nom);
	}
	public void renameChamp(String []champ,String []nom)throws Exception{
		if(champ.length!=nom.length)
			throw new Exception("La longueur des de la table champ et nom doivent etre identique !");
		for(int i=0;i<champ.length;i++)
			renameChamp(champ[i],nom[i]);
	}
	
	public String inputFor(String champ,String classe) throws Exception{
		Champ field=getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		return inputFor(champ,classe,field.getAdditionnale());
	}
	public String inputFor(String champ,String classe,String add) throws Exception{
		Champ field=getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		return inputFor(field,classe,add,true);
	}
	public String inputFor(Champ f,String classe,String add,boolean withDelete)throws Exception{
		Champ field=f;
		if(field==null)
			throw new Exception("Champ "+f+" introvable");
		if(withDelete)
			fieldsAvalaible.remove(f);
		if(getEntity().findFormError()!=null){
			String mess=getEntity().findFormError().get(f);
			if(mess!=null && (showErrorMode==ERROR_SHOW.COLOR_ALL || showErrorMode==ERROR_SHOW.COLOR_INPUT))
			{
				classe+=" "+getDefaultClassForError();
			}
		}
		String select=testSelectField(field,classe);
		String reponse="";
		reponse+=select;
		if(select.length()==0){
			reponse+=getTypeForField(field)+" name=\""+field.getName()+"\" id=\""+field.getName()+"\"  class=\""+classe+"\" "+add+" "+getEndInput(field,defaultValudeForField(field));
		}
		return reponse;
	}
	private String getEndInput(Champ field,Object value){
		if(field.isTextarea())
			return ">"+value+"</textarea>";
		return "value=\""+value+"\" />";
	}
	private String getTypeForField(Champ f)throws Exception{
		if(getEntity().isNumberType(f.getType())){
			return "<input type=\"number\"";
		}
		else if(f.getType().equals(Date.class) || f.getType().equals(java.sql.Date.class))
		{
			return "<input type=\"text\"";
		}
		else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)){
			if((boolean)defaultValudeForField(f)==true)
				return "<input type=\"checkbox\" checked=\"checked\"";
			return "<input type=\"checkbox\" checked=\"\"";
		}
		if(f.isTextarea()){
			return "<textarea ";
		}
		return "<input type=\"text\"";
	}
	
	private String testSelectField(Champ f,String classe) throws Exception{
		List<OptionObject> data;
		
		if((data=typeSelectGenerique.get(f))!=null){
			return buildSelect(f,data,classe,false);
		}
		if(f.isForeignKey() && f.getFk().selecttype()==SELECT_TYPE.OPTION)
		{
			return buildSelect(f,f.getForeignKeyData(),classe,f.getFk().nullable());
		}
		return "";
	}
	private String buildSelect(Champ f,List<OptionObject> data,String classe,boolean nullable) throws Exception{
		String reponse="";
		String option="";
		if(this.getClass().equals(FilterBuilder.class))
			option+="<option value=\"\">Tous</option>";
		else if(nullable)
		{
			option+="<option value=\"\"> -- </option>";
		}
		for(OptionObject objet:data){
			option+=objet.getOptionHTML(defaultValudeForField(f));
		}
		reponse+="<select class=\""+classe+"\" name=\""+f.getName()+"\" id=\""+f.getName()+"\" "+f.getAdditionnale()+">";
		reponse+=option;
		reponse+="</select>";
		return reponse;
	}
	public void setChampSelect(Champ field,List<OptionObject> data) throws Exception{
		typeSelectGenerique.put(field, data);
	}
	public void setChampSelect(String champ,Map<String,String> data) throws Exception{
		Champ field=getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		Set<Entry<String, String>> fil=data.entrySet();
		List<OptionObject> dat=new ArrayList<OptionObject>(); 
		for(Entry<String, String> entry:fil)
		{
			dat.add(new OptionObject(entry.getKey(), entry.getValue()));
		}
		setChampSelect(field, dat);
	}
	public void setChampSelect(String champ,List<DataEntity> data,String[] map) throws Exception{
		Champ field=getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		List<OptionObject> datar=new ArrayList<OptionObject>();
		for(DataEntity object:data){
			String id=String.valueOf(object.getValueForField(object.getFieldByName(map[0])));
			String val=String.valueOf(object.getValueForField(object.getFieldByName(map[1])));
			datar.add(new OptionObject(id, val));
		}
		setChampSelect(field,datar);
	}
	public void setChampSelect(String champ,DataEntity source,String[]map) throws Exception{
		List<DataEntity> data=DaoModele.getInstance().findPageGenerique(1, (DataEntity)source);
		List<DataEntity> datar=new ArrayList<DataEntity>();
		for(DataEntity bm:data){
			datar.add(bm);
		}
		setChampSelect(champ,datar,map);
	}
	public String getClassForField(Field field){
		String val=classForChamp.get(field);
		if(val==null)
		{
			if(typeSelectGenerique.containsKey(field)){
				return defaultClassForSelect;
			}
			return defauldClassForInput;
		}
		return val;
	}
	
	public String getDefaultClassForInputContainer() {
		return defaultClassForInputContainer;
	}
	public void setDefaultClassForInputContainer(String defaultClassForInputContainer) {
		this.defaultClassForInputContainer = defaultClassForInputContainer;
	}
	public String getClassForField(String champ) throws Exception
	{
		Field field=getEntity().getFieldByName(champ);
		if(field==null)
		{
			if(typeSelectGenerique.get(field)!=null){
				return defaultClassForSelect;
			}
			return defauldClassForInput;
		}
		return getClassForField(field);
	}
	public void addClassForChamp(String champ,String classe)throws Exception{
		String last=getClassForField(champ);
		setClassForChamp(champ, last+" "+classe);
	}
	public void setClassForChamp(String champ,String classe) throws Exception{
		Champ field=getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introuvable");
		classForChamp.put(field, classe);
	}
	public void setChampTextarea(String[] champs)throws Exception
	{
		for(String champ:champs)
			setChampTextarea(champ);
	}
	public void setChampTextarea(String champ)throws Exception{
		Champ f=getFieldByName(champ);
		if(f==null)
			throw new Exception("Champ "+champ+" introuvable");
		f.setTextarea(true);
	}
	public String getDefauldClassForLabel() {
		return defauldClassForLabel;
	}
	public void setDefauldClassForLabel(String defauldClassForLabel) {
		this.defauldClassForLabel = defauldClassForLabel;
	}
	public String getDefauldClassForInput() {
		return defauldClassForInput;
	}
	public void setDefauldClassForInput(String defauldClassForInput) {
		this.defauldClassForInput = defauldClassForInput;
	}
	
	public String getDefaultClassForSelect() {
		return defaultClassForSelect;
	}
	public void setDefaultClassForSelect(String defaultClassForSelect) {
		this.defaultClassForSelect = defaultClassForSelect;
	}

	
	
	public String getDefaultClassForContainer() {
		return defaultClassForContainer;
	}
	public void setDefaultClassForContainer(String defaultClassForContainer) {
		this.defaultClassForContainer = defaultClassForContainer;
	}

	public String getClassForValidation() {
		return classForValidation;
	}
	public void setClassForValidation(String classForValidation) {
		this.classForValidation = classForValidation;
	}

	public String getClassForReset() {
		return classForReset;
	}
	public void setClassForReset(String classForReset) {
		this.classForReset = classForReset;
	}

	public String getDefaultClassForError() {
		return defaultClassForError;
	}
	public void setDefaultClassForError(String defaultClassForError) {
		this.defaultClassForError = defaultClassForError;
	}
	public String getClassForForm() {
		return classForForm;
	}
	public void setClassForForm(String classForForm) {
		this.classForForm = classForForm;
	}
	
}
