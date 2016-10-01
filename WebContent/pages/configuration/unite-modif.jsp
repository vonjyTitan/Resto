<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>

<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new Unite(),"crud-update?classenom=com.mapping.Unite&cible=configuration/unite-liste&refereur=main.jsp?cible=configuration/unite-modif",request);
	builder.addNotVisibleChamp("idunite");
	builder.setChampTextarea("description");
	builder.setValueFromDatabase(SessionUtil.getValForAttr(request, "id"));
	%>
	<h3><a href="main.jsp?cible=configuration/unite-liste"><i class="fa fa-angle-left"></i><i class="fa fa-angle-left"></i></a> Modification unite d'article</h3>
	<%
	out.println(builder.getHTML(6));
%>