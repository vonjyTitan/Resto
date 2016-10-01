package com.rooteur;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.el.MethodNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mapping.Notification;

public class Action {
	public void run(String nomMethod,HttpServletRequest request,HttpServletResponse response) throws IOException{
		try {
			if(nomMethod.isEmpty())
				throw new Exception("Action introuvable");
			Method met=this.getClass().getMethod(nomMethod, new Class[]{HttpServletRequest.class,HttpServletResponse.class});
			met.invoke(this, new Object[]{request,response});
		}
		catch(MethodNotFoundException ex){
			Serveur.back(request, response,"Action introuvable");
			return;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Serveur.back(request, response,e.getCause().getMessage());
			return;
		} 
	}
	protected void back(HttpServletRequest request,HttpServletResponse response,String error){
		try {
			Serveur.back(request, response,error);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected void goTo(HttpServletRequest request,HttpServletResponse response,String url){
		try {
			if(request.getMethod().compareToIgnoreCase("post")==0){
				goTo(request,response,"post",url);
				return;
			}
			goTo(request,response,"get",url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void goTo(HttpServletRequest request,HttpServletResponse response,String method,String url){
		try {
			if(method.compareToIgnoreCase("post")==0){
				request.getRequestDispatcher(url).forward(request, response);
				return;
			}
			response.sendRedirect(url);
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
	}
}
