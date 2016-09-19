<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new Utilisateur(),"Action?to=login-ajoutUtilisateur",request);
	builder.removeChamp(new String[]{"idutilisateur","passe","active"});
	builder.setTitle("Ajout d'un utilisateur");
	out.print(builder.getHTML(7));
%>