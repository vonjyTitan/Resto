package com.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dao.DaoModele;

public class ListPaginner<E> extends ArrayList<E> {
	public int nbPage=1;
	public Object findSummeByField(String fieldName) throws Exception{
		if(this.size()==0)
			return 0;
		
		Object objet = this.get(0);
		
		Method method=objet.getClass().getMethod("get"+DaoModele.setMaj(fieldName), null);
		Field []fields = ((DataEntity)objet).getAllFields();
		Field field = null;
		for(Field f:fields)
			if(f.getName().equals(fieldName))
				field=f;
		if(field.getType().equals(double.class) || field.getType().equals(Double.class) || field.getType().equals(int.class) || field.getType().equals(float.class) || field.getType().equals(Integer.class) || field.getType().equals(Float.class) || field.getType().equals(long.class) || field.getType().equals(Long.class))
		{
			double reponse = 0.0;
			for(E b:this){
				reponse+=Double.valueOf(String.valueOf(method.invoke(b, null)));
			}
			return reponse;
		}
		return 0;
	}
}
