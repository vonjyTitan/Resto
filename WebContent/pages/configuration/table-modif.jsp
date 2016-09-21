<%@page import="utilitaire.SessionUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>

<% InsertUpdateBuilder builder=new InsertUpdateBuilder(new Table(),"table-modif",request); 
	builder.setValueFromDatabase(SessionUtil.getValForAttr(request, "id"));
	builder.addNotVisibleChamp(new String[]{"idtable","positionx","positiony","etat"});%>
	<h3><a href="main.jsp?cible=configuration/table-gestion"><i class="fa fa-angle-left"></i><i class="fa fa-angle-left"></i></a> Modification d'une table</h3>
	<%
	out.print(builder.getHTML(5)); 
	%>