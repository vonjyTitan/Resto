<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>

<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new CategorieArticle(),"crud-insert?classenom=com.mapping.CategorieArticle&cible=configuration/categorie-liste&refereur=main.jsp?cible=configuration/categorie-ajout",request);
	builder.removeChamp("idcategorie");
	builder.setChampTextarea("description");
	%>
	<h3>Ajout categorie d'article</h3>
	<%
	out.println(builder.getHTML(6));
%>