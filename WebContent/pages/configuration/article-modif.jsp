<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new ArticleStock(),"crud-update?classenom=com.mapping.ArticleStock&cible=configuration/article-liste&refereur=main.jsp?cible=configuration/article-modif",request);
	builder.setChampTextarea("description");
	builder.setValueFromDatabase(SessionUtil.getValForAttr(request, "id"));
	builder.addNotVisibleChamp(new String[]{"idarticle","quantite"});
%>
	<h3><a href="main.jsp?cible=configuration/article-liste"><i class="fa fa-angle-left"></i><i class="fa fa-angle-left"></i></a> Modification article</h3>
<%=builder.getHTML(6)%>