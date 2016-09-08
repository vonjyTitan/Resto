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
import com.mapping.BaseModele;
import com.mapping.DataEntity;

public class FormBuilder<T extends DataEntity> {
	private T entity;
	private HttpServletRequest request;
	private String cible=null;
	private List<String> notVisibleChamp=null;
	
	private String defauldClassForLabel="col-sm-4 col-sm-4 ";
	private String defauldClassForInput="form-control";
	private String classForForm="form-horizontal style-form";
	private String defaultClassForSelect="form-control col-lg-6";
	private String defaultClassForContainer="form-group";
	private String classForValidation="btn btn-primary";
	private String defaultClassForError="alert alert-warning";
	private String classForReset="btn btn-danger";
	private String defaultClassForInputContainer="col-sm-7";
	private String title=null;
	private ERROR_SHOW showErrorMode=ERROR_SHOW.POP_UP;
	
	private Map<Field,List<OptionObject>> typeSelectGenerique;
	private Map<Field,String> classForChamp;
	private Map<Field,String> additionnalForChamp;
	private List<Field> fieldsAvalaible;
	
	public FormBuilder(T entity,String cible,HttpServletRequest request){
		this.entity=entity;
		this.request=request;
		this.cible=cible;
		notVisibleChamp=new ArrayList<String>();
		typeSelectGenerique=new HashMap<Field,List<OptionObject>> ();
		classForChamp=new HashMap<Field,String>();
		additionnalForChamp=new HashMap<Field,String>();
		fieldsAvalaible=new ArrayList<Field>();
		for(Field field:entity.getAllFields()){
			fieldsAvalaible.add(field);
		}
		try{
			if(request.getParameter("erreur")!=null){
				setEntity(getValue());
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public String getHTML() throws Exception{
		String reponse=beginHTMLForm();
		reponse+=HTMLBuilder.beginPanel("Formulaire general");
		reponse+=getHTMLBody();
		reponse+=getHTMLButton();
		reponse+=HTMLBuilder.endPanel();
		reponse+=endHTMLForm();
		return reponse;
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
			if(getEntity().getFormError()!=null){
				if(showErrorMode==ERROR_SHOW.POP_UP){
					Set<Entry<Field,String>> fil=getEntity().getFormError().entrySet();
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
	public String endHTMLForm(){
		String reponse="";
		reponse+="</form>";
		reponse+="</div>";
		reponse+="</div>";
		reponse+="</div>";
		return reponse;
	}
	public String getHTMLButton(){
		String reponse="<div class=\""+defaultClassForContainer+"\">";
		reponse+="<label class=\"control-label col-lg-4\"></label>"
              	+"<div class=\"col-lg-4\">";
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
	public String getHTMLBody() throws Exception{
		String reponse="";
		for(Field f:fieldsAvalaible){
			reponse+=blockFor(f,false); 
		}
		return reponse;
	}
	private String testSelectField(Field f,String classe) throws Exception{
		String reponse="";
		List<OptionObject> generic;
		String option="";
		if((generic=typeSelectGenerique.get(f))!=null){
			for(OptionObject objet:generic){
				option+=objet.getDOMHTML();
			}
			reponse+="<select class=\""+classe+"\" name=\""+f.getName().toLowerCase()+"\" id=\""+f.getName().toLowerCase()+"\" "+getAdditionForField(f)+">";
			reponse+=option;
			reponse+="</select>";
		}
		return reponse;
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
	
	
	public String blockFor(String champ)throws Exception{
		Field field=getEntity().getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		return blockFor(field,true);
	}
	public String blockFor(Field f,boolean withDelete)throws Exception{
		String reponse="";
		if(isNotVisible(f)){
			reponse+="<input type=\"hidden\" name=\""+f.getName().toLowerCase()+"\" id=\""+f.getName().toLowerCase()+"\" value=\""+defaultValudeForField(f)+"\" />";
			if(withDelete)
				removeChamp(f.getName());
			return reponse;
		}
		reponse+="<div id=\""+f.getName().toLowerCase()+"container\" class=\""+defaultClassForContainer+"\">";
		reponse+=labelleFor(f,defauldClassForLabel);
		reponse+="<div class=\""+getDefaultClassForInputContainer()+"\">";
		reponse+=inputFor(f, getClassForField(f),getAdditionForField(f),withDelete);
		reponse+="</div>";
		reponse+="</div>";
		return reponse;
	}
	public String labelleFor(String champ)throws Exception{
		return labelleFor(champ,defauldClassForLabel);
	}
	public String labelleFor(String champ,String classe) throws Exception{
		Field field=getEntity().getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		return labelleFor(field,classe);
	}
	public String labelleFor(Field f,String classe){
		if(getEntity().getFormError()!=null){
			String mess=getEntity().getFormError().get(f);
			if(mess!=null && (showErrorMode==ERROR_SHOW.COLOR_ALL || showErrorMode==ERROR_SHOW.COLOR_LABELLE))
			{
				classe+=" "+getDefaultClassForError();
			}
		}
		return "<div  class=\""+classe+"\"><label class=\"control-label\" for=\""+f.getName().toLowerCase()+"\" >"+getEntity().getLibelleForField(f)+" "+((getEntity().isFieldRequired(f)) ? "*" : "")+"</label></div>";
	}
	
	public String inputFor(String champ)throws Exception{
		return inputFor(champ,defauldClassForInput);
	}
	public String inputFor(String champ,String classe) throws Exception{
		Field field=getEntity().getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		return inputFor(champ,classe,getAdditionForField(field));
	}
	public String inputFor(String champ,String classe,String add) throws Exception{
		Field field=getEntity().getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		if(classe==null){
			classe=getClassForField(field);
		}
		if(classe.isEmpty()){
			classe=getClassForField(field);
		}
		return inputFor(field,classe,add,true);
	}
	public String inputFor(Field f,String classe,String add,boolean withDelete)throws Exception{
		if(withDelete)
			fieldsAvalaible.remove(f);
		if(getEntity().getFormError()!=null){
			String mess=getEntity().getFormError().get(f);
			if(mess!=null && (showErrorMode==ERROR_SHOW.COLOR_ALL || showErrorMode==ERROR_SHOW.COLOR_INPUT))
			{
				classe+=" "+getDefaultClassForError();
			}
		}
		String select=testSelectField(f,classe);
		String reponse="";
		reponse+=select;
		if(select.length()==0){
			reponse+=getTypeForField(f)+" name=\""+f.getName().toLowerCase()+"\" id=\""+f.getName().toLowerCase()+"\" value=\""+defaultValudeForField(f)+"\" class=\""+getClassForField(f)+"\" "+add+">";
		}
		return reponse;
	}
	
	public void removeChamp(String [] champ)throws Exception{
		for(String ch:champ)
			removeChamp(ch);
	}
	public void setChampSelect(String champ,Map<String,String> data) throws Exception{
		Field field=getEntity().getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		Set<Entry<String, String>> fil=data.entrySet();
		List<OptionObject> dat=new ArrayList<OptionObject>(); 
		for(Entry<String, String> entry:fil)
		{
			dat.add(new OptionObject(entry.getKey(), entry.getValue(), field));
		}
		setChampSelect(field, dat);
	}
	public void setChampSelect(String champ,List<DataEntity> data,String[] map) throws Exception{
		Field field=getEntity().getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introvable");
		List<OptionObject> datar=new ArrayList<OptionObject>();
		for(DataEntity object:data){
			String id=String.valueOf(object.getValueForField(object.getFieldByName(map[0])));
			String val=String.valueOf(object.getValueForField(object.getFieldByName(map[1])));
			datar.add(new OptionObject(id, val, field));
		}
		setChampSelect(field,datar);
	}
	public void setChampSelect(String champ,DataEntity source,String[]map) throws Exception{
		List<BaseModele> data=DaoModele.getInstance().findPageGenerique(1, (BaseModele)source);
		List<DataEntity> datar=new ArrayList<DataEntity>();
		for(BaseModele bm:data){
			datar.add(bm);
		}
		setChampSelect(champ,datar,map);
	}
	
	public void setChampSelect(Field field,List<OptionObject> data) throws Exception{
		typeSelectGenerique.put(field, data);
	}
	
	private String getTypeForField(Field f)throws Exception{
		if(getEntity().isNumberType(f.getType())){
			return "<input type=\"number\"";
		}
		else if(f.getType().equals(Date.class) || f.getType().equals(java.sql.Date.class))
		{
			return "<input type=\"text\"";
		}
		else if(f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)){
			if((boolean)entity.getValueForField(f)==true)
				return "<input type=\"checkbox\" checked=\"checked\"";
			return "<input type=\"checkbox\" checked=\"\"";
		}
		return "<input type=\"text\"";
	}
	private Object defaultValudeForField(Field f) throws Exception{
		Object val=entity.getValueForField(f);
		if(val!=null){
			return val;
		}
		return "";
	}
	public void addNotVisibleChamp(String champ){
		addNotVisibleChamp(new String[]{champ});
	}
	public String getClassForField(String champ) throws Exception
	{
		Field field=getEntity().getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introuvable");
		return getClassForField(field);
	}
	public String getClassForField(Field field){
		String val=classForChamp.get(field);
		if(val==null)
		{
			if(typeSelectGenerique.get(field)!=null){
				return defaultClassForSelect;
			}
			return defauldClassForInput;
		}
		return val;
	}
	public void addClassForChamp(String champ,String classe)throws Exception{
		String last=getClassForField(champ);
		setClassForChamp(champ, last+" "+classe);
	}
	public void setClassForChamp(String champ,String classe) throws Exception{
		Field field=getEntity().getFieldByName(champ);
		if(field==null)
			throw new Exception("Champ "+champ+" introuvable");
		classForChamp.put(field, classe);
	}
	public void addNotVisibleChamp(String[] champ){
		for(String s:champ)
		{
			notVisibleChamp.add(s);
		}
	}
	public boolean isNotVisible(Field f){
		for(String s:notVisibleChamp){
			if(f.getName().compareToIgnoreCase(s)==0)
				return true;
		}
		return false;
	}
	public void setValueFromDatabase(Object object)throws Exception{
		if(request.getParameter("erreur")==null){
			List<BaseModele> l=DaoModele.getInstance().findPageGenerique(1, (BaseModele) entity," and "+entity.getPkName()+"="+object);
			if(l.size()==0)
				throw new Exception("Objet introuvable");
			setEntity((T) l.get(0));
		}
	}
	public T getValue() throws Exception{
		T reponse=(T) getEntity().getClass().newInstance();
		Field[] fields=reponse.getAllFields();
		for(Field field:fields)
		{
			if(request.getParameter(field.getName())!=null)
			{
				if(request.getParameter(field.getName()).isEmpty())
					continue;
				if(field.getType().equals(java.util.Date.class) || field.getType().equals(java.sql.Date.class))
				{
					DateFormat form=new SimpleDateFormat("dd/MM/yyyy"); 
					reponse.setValueForField(field, form.parseObject(request.getParameter(field.getName())));
				}
				else if (field.getType().equals(int.class)){
					reponse.setValueForField(field, Integer.valueOf("0"+request.getParameter(field.getName())));
				}
				else if(field.getType().equals(double.class) || field.getType().equals(Double.class)){
					reponse.setValueForField(field, Double.valueOf("0"+request.getParameter(field.getName())));
				}
				else if(field.getType().equals(float.class) || field.getType().equals(Float.class)){
					reponse.setValueForField(field, Float.valueOf("0"+request.getParameter(field.getName())));
				}
				else reponse.setValueForField(field, request.getParameter(field.getName()));
			}
		}
		return reponse;
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
	public String getClassForForm() {
		return classForForm;
	}
	public void setClassForForm(String classForForm) {
		this.classForForm = classForForm;
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

	public ERROR_SHOW getShowErrorMode() {
		return showErrorMode;
	}
	public void setShowErrorMode(ERROR_SHOW showErrorMode) {
		this.showErrorMode = showErrorMode;
	}

	public T getEntity() {
		return entity;
	}
	private void setEntity(T entity) throws Exception {
		this.entity = entity;
		this.entity.isValide();
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDefaultClassForInputContainer() {
		return defaultClassForInputContainer;
	}
	public void setDefaultClassForInputContainer(String defaultClassForInputContainer) {
		this.defaultClassForInputContainer = defaultClassForInputContainer;
	}

	public class OptionObject extends DataEntity{
		private String id;
		private String val;
		private Field f;
		
		private OptionObject(String id,String val,Field f){
			this.id=id;
			this.val=val;
			this.f=f;
		}
		public String getDOMHTML() throws Exception{
			String selected=(String)defaultValudeForField(f);
			if(selected.equals(id))
				return "<option selected value=\""+id+"\">"+val+"</option>";
			return "<option value=\""+id+"\">"+val+"</option>";
		}
	}
	public enum ERROR_SHOW{
		POP_UP,
		COLOR_LABELLE,
		COLOR_INPUT,
		COLOR_ALL
	}
}
