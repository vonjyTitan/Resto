package com.rooteur;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mapping.Utilisateur;
import com.service.LoginService;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import utilitaire.ConfigUtil;
import utilitaire.SessionUtil;

public class Serveur {
	private static final long serialVersionUID = 1L;
       
    private static Map<String,Action> allAction=new HashMap<String,Action>();
	
    public Serveur() {
        super();
    }

    private static void initAction() throws Exception{
    	if(allAction.size()==0)
    	{
    		ScanResult scanResult = new FastClasspathScanner("com.action").scan();
    		List<String> ss=scanResult.getNamesOfAllClasses();
    		if(ss!=null){
    			for(String s:ss)
    			{
    				try {
						Class classe=Class.forName(s);
						if(!classe.getSuperclass().equals(Action.class))
						{
							System.out.println("Une class d'action doit imperativement herite de la classe Action");
							continue;
						}
						if(!s.contains("Action"))
						{
							System.out.println("La class action doit se terminer par le nom Action");
						}
						try {
							allAction.put(classe.getSimpleName().toLowerCase(),(Action) classe.newInstance());
						} catch(Exception ex){
							throw ex;
						}
					} catch (ClassNotFoundException e) {
						System.out.println("La class "+s+" est introuvable");
					}
    			}
    		}
    	}
    }
	
	public static void run(HttpServletRequest request, HttpServletResponse response,String action) throws ServletException, IOException {
		try {
			initAction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(action.compareToIgnoreCase("login-testLogin")!=0)
		{
			try {
				SessionUtil.isExisteSession(request);
			} catch (Exception e) {
					response.sendRedirect("login.jsp?erreur=Veuillez vous connecter d'abord!");
					return;
			}
			try {
				if(!LoginService.getInstance().isAllowed((Utilisateur) request.getSession().getAttribute("utilisateur"),action))
					throw new Exception("Vous n'avez pas acces a cette page!");
			} catch (Exception e) {
				back(request,response,e.getMessage());
				return;
			}
		}
		String cible=action;
		if(cible==null || cible.isEmpty() || cible.split("-").length<=1)
		{
			back(request,response,"Action introuvable");
			return;
		}
		String classe=cible.split("-")[0]+"Action";
		String method=cible.split("-")[1];
		if(!allAction.containsKey(classe.toLowerCase()))
		{
			back(request,response,"Class d'action introuvable");
			return;
		}
		Action act=allAction.get(classe.toLowerCase());
		try {
			act.run(method, request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
			back(request,response,ex.getMessage());
		}
		
	}
	public static void back(HttpServletRequest request, HttpServletResponse response,String errer) throws IOException{
		response.getWriter().write("<html><body><script>alert('"+errer+"');history.back();</script></body></html>");
	}

}
