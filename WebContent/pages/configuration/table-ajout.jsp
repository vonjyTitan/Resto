<%@page import="utilitaire.SessionUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new Table(),"table-ajout",request);
	builder.addNotVisibleChamp(new String[]{"idtable","positionx","positiony"});
	builder.removeChamp("etat");
	%>
	<h3>Ajout nouvelle table</h3>
	<%
	out.print(builder.getHTML(6));
%>