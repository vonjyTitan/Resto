<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	TableBuilder builder=new TableBuilder(new MenuFamille(),request);
	builder.setLienForId("main.jsp?cible=configuration/famille-fiche");
	builder.setLienForModif("main.jsp?cible=configuration/famille-modif");
	builder.setLienForDelete("famille-delete?a=1");
	%>
	<h3>Liste des familles de menu</h3>
	<%
	out.println(HTMLBuilder.beginPanel("Liste", 6));
	out.println(builder.getHTML());
	out.println(HTMLBuilder.endPanel());
%>