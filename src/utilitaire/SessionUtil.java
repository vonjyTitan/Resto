package utilitaire;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtil {
	
	public static int ListeCategorieAction=1;
	public static int formCategorieAction=2;
	public static int ficheCategorieAction=3;
	public static int sousCategorieCategorieAction=4;
	public static int ListePublicationAction=5;
	public static int fichePublicationAction=6;
	public static int listeStoreAction=7;
	public static int formStoreAction=8;
	public static int formCategorie=9;
	public static int formPublicationAction=10;
	public static int listeMotCle=11;
	public static int formNumero=12;
	public static int ficheStore=13;
	public static int valideSousctagorie=14;
	public static int validePublication = 15;
	public static int deleteMotCle=16;
	public static int ajoutMotCle=17;
	public static int ajoutUtilisateur=18;
	public static int listeUtilisateur=19;
	public static int deletecarrousel=20;
	public static int itunesliste=21;
	public static int ajoutIncitation=22;
	
	static int[] actionSuperAdmin={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22};
	static int[] actionAdmin={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22};
	static int[] actionEditeur={5,6,10,11,12,13,17};
	
	
	
	
	public static void testSession(HttpServletRequest request)throws Exception{
		isExisteSession(request);
		isAccesEnable(request,request.getParameter("cible"));
	}
	
	public static void isExisteSession(HttpServletRequest request)throws Exception{
		if(request.getSession().getAttribute("utilisateur")==null)
			throw new Exception("Veuillez vous connecter d'abord!");
	}
	public static void isAccesEnable(HttpServletRequest request,String cible)throws Exception{
		/*HttpSession session=request.getSession();
		Utilisateur user=(Utilisateur) session.getAttribute("utilisateur");
		if(user.getType()==1){
			if(chercheDansTable(actionSuperAdmin,action)==false)
				throw new ControllerException("acces.refuser",ExceptionType.NOTACCES);
		}
		else if(user.getType()==2){
			if(chercheDansTable(actionAdmin,action)==false)
				throw new ControllerException("acces.refuser",ExceptionType.NOTACCES);
		}
		else if(user.getType()==3){
			if(chercheDansTable(actionEditeur,action)==false)
				throw new ControllerException("acces.refuser",ExceptionType.NOTACCES);
		}
		*/
	}
	static boolean chercheDansTable(int[] table,int chiffre){
		for(int i:table)
			if(i==chiffre)
				return true;
		return false;
	}
	
}
