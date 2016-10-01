<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	TableBuilder builder=new TableBuilder(new CategorieArticle(),request);
	builder.setLienForModif("main.jsp?cible=configuration/categorie-modif");
	builder.setLienForDelete("crud-delete?classenom=com.mapping.CategorieArticle&cible=configuration/categorie-liste");
%>
<h3>Liste des categories d'article</h3>
<%=HTMLBuilder.beginPanel("Liste", 6) %>
<%=builder.getHTML()%>
<%=HTMLBuilder.endPanel()%>