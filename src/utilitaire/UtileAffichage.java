package utilitaire;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtileAffichage {
	public static Object getNonNullValue(Object value,Field f){
		if(f.getType().equals(String.class)){
			if(value==null)
				return "";
			return value;
		}
		if(f.getType().equals(java.util.Date.class) || f.getType().equals(java.sql.Date.class)){
			if(value==null){
				return "";
			}
			return value;
		}
		return value;
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
