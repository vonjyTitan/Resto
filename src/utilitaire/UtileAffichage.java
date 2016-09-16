package utilitaire;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtileAffichage {
	public static Object getNonNullValue(Object value,Class f){
		if(f.equals(String.class)){
			if(value==null)
				return "";
			return value;
		}
		if(f.equals(java.util.Date.class) || f.equals(java.sql.Date.class)){
			if(value==null){
				return "";
			}
			return formatAfficherDate((java.util.Date) value);
		}
		return value;
	}
	public static Object parseFromRequest(Object value,Class type) throws ParseException{
		if(type.equals(java.util.Date.class) || type.equals(java.sql.Date.class))
		{
			DateFormat form=new SimpleDateFormat("dd/MM/yyyy"); 
			return form.parseObject((String) value);
		}
		else if (type.equals(int.class)){
			return Integer.valueOf("0"+((String)value));
		}
		else if(type.equals(double.class) || type.equals(Double.class)){
			return Double.valueOf("0"+((String)value));
		}
		else if(type.equals(float.class) || type.equals(Float.class)){
			return Float.valueOf("0"+((String)value));
		}
		else return value;
	}
	public static String formatMoney(double nombre){
		DecimalFormat myNumberFormat = new DecimalFormat("###,###.###");
		return myNumberFormat.format(nombre);
	}
	public static String formatDate(java.sql.Date date){
		if(date==null)
			return "";
		Date date_var = new java.sql.Date(date.getTime());
		DateFormat myDateFormat=new SimpleDateFormat("dd-MM-yyyy");
		return myDateFormat.format(date_var);
		
	}
	public static String formatDate(java.util.Date date){
		if(date==null)
			return "";
		DateFormat myDateFormat=new SimpleDateFormat("dd-MM-yyyy");
		return myDateFormat.format(date);
		
	}
	public static String formatAfficherDate(java.sql.Date date){
		if(date==null)
			return "";
		Date date_var = new java.sql.Date(date.getTime());
		DateFormat myDateFormat=new SimpleDateFormat("dd/MM/yyyy");
		return myDateFormat.format(date_var);
	}
	public static String formatAfficherDate(java.util.Date date){
		if(date==null)
			return "";
		DateFormat myDateFormat=new SimpleDateFormat("dd/MM/yyyy");
		return myDateFormat.format(date);
	}
	public static Date getDateNow(){
		return new java.util.Date();
	}
}
