<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	TableBuilder builder=new TableBuilder(new Utilisateur(),request);
	//builder.getFilterBuilder().addNotVisibleChamp(new String[]{"login","passe"});
	builder.addNotVisibleChamp(new String[]{"login","passe"});
	builder.setLibelleFor("idutilisateur", "Id");
	builder.getFilterBuilder().setChampToInterval("idutilisateur");
%>
<%=builder.getFilterBuilder().getHTML()%>
<%=HTMLBuilder.beginPanel("liste informations",12) %>
<%=builder.getHTML()%>
<%=HTMLBuilder.endPanel() %>