<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	TableBuilder builder=new TableBuilder(new Unite(),request);
	builder.setLienForModif("main.jsp?cible=configuration/unite-modif");
	builder.setLienForDelete("crud-delete?classenom=com.mapping.Unite&cible=configuration/unite-liste");
%>
<h3>Liste des unites</h3>
<%=HTMLBuilder.beginPanel("Liste", 6) %>
<%=builder.getHTML()%>
<%=HTMLBuilder.endPanel()%>