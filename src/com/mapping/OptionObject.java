package com.mapping;

public class OptionObject extends DataEntity{
	private String id;
	private String val;
	
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
}