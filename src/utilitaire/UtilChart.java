package utilitaire;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dao.DaoModele;

public class UtilChart {
	public static String getChartValue(List<Object> objetlist,String[]col,String[]lib)throws Exception{
		String reponse="[[";
		boolean first=true;
		for(String in:lib){
			reponse+=((first==false) ? "," : "") + "'"+in+"'";
			first=false;
		}
		reponse+="]";
		if(objetlist.size()==0)
			return reponse+"]";
		Method[]methlist=new Method[col.length];
		for(int i=0;i<col.length;i++){
			methlist[i]=objetlist.get(0).getClass().getMethod("get"+DaoModele.setMaj(col[i]), null);
		}
		for(Object in:objetlist){
			first=true;
			reponse+=",[";
			for(Method inmeth:methlist){
				reponse+=((first==true) ? "'" : ",")+(String.valueOf(inmeth.invoke(in, null))).replaceAll("'", " ") +((first==true) ? "'" : "");

				first=false;
			}
			reponse+="]";
		}
		
		reponse+="]";
		return reponse;
	}
	public static Object[][] getChartArray(List<Object> objetlist,String[][]col,String[]lib)throws Exception{
		Object[][]rep=new Object[objetlist.size()+1][lib.length];
		rep[0]=lib;
		if(objetlist.size()==0)
			return rep;
		
		Method[][]methlist=new Method[col.length][];
		for(int i=0;i<col.length;i++){
			methlist[i]=new Method[col[i].length];
			for(int ii=0;ii<col[i].length;ii++)
				methlist[i][ii]=objetlist.get(0).getClass().getMethod("get"+DaoModele.setMaj(col[i][ii]), null);
		}
		int irep=1;
		for(Object in:objetlist){
			int iirep=0;
			for(Method[] inmeth:methlist){
				if(inmeth.length == 1){
					rep[irep][iirep]=inmeth[0].invoke(in, null);
					if(rep[irep][iirep].getClass().equals(String.class))
						rep[irep][iirep]=((String)rep[irep][iirep]).replaceAll("'", " ");
				}
				else{
					rep[irep][iirep]="";
					for(Method method:inmeth)
						rep[irep][iirep]+=" "+String.valueOf(method.invoke(in, null)).replaceAll("'", " ");
				}
				iirep++;
			}
			irep++;
		}
		return rep;
	}
}
