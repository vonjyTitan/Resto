<%@page import="utilitaire.SessionUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>

<% InsertUpdateBuilder builder=new InsertUpdateBuilder(new Utilisateur(),"login-modif",request); 
	builder.setValueFromDatabase(SessionUtil.getValForAttr(request, "id"));
	builder.removeChamp("passe");
	builder.addNotVisibleChamp("idutilisateur");%>
	<h3><a href="main.jsp?cible=configuration/liste-utilisateur"><i class="fa fa-angle-left"></i><i class="fa fa-angle-left"></i></a> Modification utilisateur</h3>
	<%
	out.print(builder.getHTML(5)); 
	%>


