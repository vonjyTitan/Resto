package com.affichage;

public class HTMLBuilder {
	public static String beginPanel(String title,int taille){
		return "<div class=\"col-lg-"+taille+" col-md-"+taille+" col-sm-"+taille+" mb\">"
				+"<div class=\"white-panel pn box-solid\">"
 		+"<div class=\"blue-header\"><h5>"+title+"</h5></div>";
	}
	public static String beginPanel(String title){
		return beginPanel(title,6);
	}
	public static String endPanel(){
		return "</div>"
				+ "</div>";
	}
}
