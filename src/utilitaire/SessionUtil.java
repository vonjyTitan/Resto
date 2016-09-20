package utilitaire;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mapping.Utilisateur;
import com.service.LoginService;

public class SessionUtil {
	
	public static void testAcces(HttpServletRequest request)throws Exception{
		String cible=getValForAttr(request,"cible");
		if(!LoginService.getInstance().isAllowed((Utilisateur) request.getSession().getAttribute("utilisateur"),cible))
			throw new Exception("Vous n'avez pas acces a cette page!");
	}
	
	public static void isExisteSession(HttpServletRequest request)throws Exception{
		if(request.getSession().getAttribute("utilisateur")==null)
			throw new Exception("Veuillez vous connecter d'abord!");
	}
	public static String getValForAttr(HttpServletRequest request,String attr){
		String reponse=request.getParameter(attr);
		if(reponse==null)
		{
			return (String) request.getAttribute(attr);
		}
		return reponse;
	}
	
	
}
