<%@page import="utilitaire.SessionUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>

<% InsertUpdateBuilder builder=new InsertUpdateBuilder(new Utilisateur(),"modifUser",request); 
	builder.setValueFromDatabase(SessionUtil.getValForAttr(request, "id"));
	builder.setTitle("Modification du role d'un utilisateur");
	builder.removeChamp(new String[]{"passe","nom","prenom","login","active"});
	builder.setChampSelect("idrole",new Role(),new String[]{"idrole","libelle"});
	builder.addNotVisibleChamp("idutilisateur");
	out.print(builder.getHTML(5));
%>