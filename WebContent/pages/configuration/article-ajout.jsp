<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new ArticleStock(),"crud-insert?classenom=com.mapping.ArticleStock&cible=configuration/article-liste&refereur=main.jsp?cible=configuration/article-ajout",request);
	builder.setChampTextarea("description");
	builder.removeChamp(new String[]{"idarticle","quantite"});
	builder.setTitle("Ajout article");
%>
<%=builder.getHTML(6)%>