package action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.mapping.Utilisateur;
import com.service.LoginService;

public class LoginAction {
	private String error="";
	private String cible="stat";
	private String id="";
	private String currmenu="";
	public String testLogin(){
		HttpServletRequest request = ServletActionContext.getRequest();
		if(request.getParameter("login")==null || request.getParameter("passe")==null)
		{
			error="Mot de passe ou login obligatoire";
			return "error";
		}
		if(request.getParameter("login").isEmpty() || request.getParameter("passe").isEmpty())
		{
			error="Mot de passe et login obligatoire";
			return "error";
		}
		Utilisateur u=null;
		try {
			u=LoginService.getInstance().testLogin(request.getParameter("login"), request.getParameter("passe"));
		} catch (Exception e) {
			error=e.getMessage();
			return "error";
		}
		if(u==null){
			error="Login ou mot de passe inconnue";
			return "error";
		}
		request.getSession().setAttribute("utilisateur", u);
		return "ok";
	}
	public String getCible() {
		return cible;
	}
	public void setCible(String cible) {
		if(cible==null || cible.compareToIgnoreCase("null")==0)
			cible="stat";
		this.cible = cible;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getCurrmenu() {
		return currmenu;
	}
	public void setCurrmenu(String currmenu) {
		this.currmenu = currmenu;
	}
}
