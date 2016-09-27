<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new Unite(),"crud-insert?classenom=com.mapping.Unite&cible=configuration/unite-liste&refereur=main.jsp?cible=configuration/unite-ajout",request);
	builder.removeChamp("idunite");
	builder.setChampTextarea("description");
%>
	<h3>Ajout unite d'article</h3>
	<%
	out.println(builder.getHTML(6));
%>