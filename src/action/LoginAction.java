package action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

public class LoginAction {
	private String cible="stat";
	private String id="";
	public String testLogin(){
		HttpServletRequest request = ServletActionContext.getRequest();
		request.getSession().setAttribute("utilisateur", "test");
		return "ok";
	}
	public String getCible() {
		return cible;
	}
	public void setCible(String cible) {
		this.cible = cible;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
