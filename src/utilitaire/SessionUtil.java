package utilitaire;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mapping.Utilisateur;
import com.service.LoginService;

public class SessionUtil {
	
	public static void testSession(HttpServletRequest request)throws Exception{
		isExisteSession(request);
		if(!LoginService.getInstance().isAllowed((Utilisateur) request.getSession().getAttribute("utilisateur"),request.getParameter("cible")))
			throw new Exception("Vous n'avez pas acces a cette page!");
	}
	
	public static void isExisteSession(HttpServletRequest request)throws Exception{
		if(request.getSession().getAttribute("utilisateur")==null)
			throw new Exception("Veuillez vous connecter d'abord!");
	}
	
}
