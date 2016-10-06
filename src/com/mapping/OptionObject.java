package com.mapping;

public class OptionObject extends DataEntity{
	private String id;
	private String val;
	private int idint;
	private int valint;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public OptionObject(String id,String val){
		this.id=id;
		this.val=val;
	}
	public OptionObject() {
	}
	public String getOptionHTML(Object defaultVal) throws Exception{
		String selected=String.valueOf(defaultVal);
		if(selected.equals(id))
			return "<option selected value=\""+id+"\">"+val+"</option>";
		return "<option value=\""+id+"\">"+val+"</option>";
	}
	public int getIdint() {
		return idint;
	}
	public void setIdint(int idint) {
		this.idint = idint;
	}
	public int getValint() {
		return valint;
	}
	public void setValint(int valint) {
		this.valint = valint;
	}
}