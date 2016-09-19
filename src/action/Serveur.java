package action;

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

import utilitaire.ConfigUtil;
import utilitaire.SessionUtil;

/**
 * Servlet implementation class Action
 */
@WebServlet("/Action")
public class Serveur extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private static Map<String,Action> allAction=new HashMap<String,Action>();
	
    public Serveur() {
        super();
    }

    private static void initAction() throws Exception{
    	if(allAction.size()==0)
    	{
    		ResourceBundle bundle=ConfigUtil.getBundleByName("domaine.properties.action");
    		String[] classStr=bundle.getString("action").split(";");
    		if(classStr!=null){
    			for(String s:classStr)
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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		run(request,response);		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		run(request,response);
		
	}
	protected void run(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			initAction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(new String(request.getParameter("to")).compareToIgnoreCase("login-testLogin")!=0)
		{
			try {
				SessionUtil.isExisteSession(request);
			} catch (Exception e) {
					response.sendRedirect("login.jsp?erreur=Veuillez vous connecter d'abord!");
					return;
			}
			try {
				if(!LoginService.getInstance().isAllowed((Utilisateur) request.getSession().getAttribute("utilisateur"),request.getParameter("to")))
					throw new Exception("Vous n'avez pas acces a cette page!");
			} catch (Exception e) {
				back(request,response);
				return;
			}
		}
		String cible=new String(request.getParameter("to"));
		if(cible==null || cible.isEmpty() || cible.split("-").length<=1)
		{
			back(request,response);
			return;
		}
		String classe=cible.split("-")[0]+"Action";
		String method=cible.split("-")[1];
		if(!allAction.containsKey(classe.toLowerCase()))
		{
			back(request,response);
			return;
		}
		Action action=allAction.get(classe.toLowerCase());
		try {
			action.run(method, request, response, this);
		} catch (Exception ex) {
			ex.printStackTrace();
			back(request,response);
		}
		
	}
	public static void back(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.getWriter().write("<html><body><script>history.back();</script></body></html>");
	}

}
