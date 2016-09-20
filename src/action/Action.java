package action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Action {
	public void run(String nomMethod,HttpServletRequest request,HttpServletResponse response,HttpServlet servlet) throws IOException{
		try {
			Method met=this.getClass().getMethod(nomMethod, new Class[]{HttpServletRequest.class,HttpServletResponse.class});
			met.invoke(this, new Object[]{request,response});
		} catch (Exception e) 
		{
			e.printStackTrace();
			Serveur.back(request, response,e.getMessage());
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
				request.getRequestDispatcher(url).forward(request, response);
				return;
			}
			response.sendRedirect(url);
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
	}
}
