<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	TableBuilder builder=new TableBuilder(new ArticleStock(),request);
	builder.getFilterBuilder().setChampToInterval("quantite");
	builder.getFilterBuilder().removeChamp("idunite");
	builder.getFieldByName("quantite").setMethodForChamp("findQuantite");
	builder.setLienForId("main.jsp?cible=configuration/article-fiche");
	builder.setLienForModif("main.jsp?cible=configuration/article-modif");
	builder.setLienForDelete("crud-delete?classenom=com.mapping.ArticleStock&cible=configuration/article-liste");
%>
<%=builder.getFilterBuilder().getHTML() %>
<%=HTMLBuilder.beginPanel("Liste", 12,"")%>
<%=builder.getHTML() %>
<%=HTMLBuilder.endPanel() %>