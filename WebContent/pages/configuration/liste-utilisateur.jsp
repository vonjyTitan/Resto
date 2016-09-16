<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	TableBuilder builder=new TableBuilder(new Utilisateur(),request);
	builder.setLienForModif("main.jsp?cible=configuration/utilisateur-modif");
	builder.getFilterBuilder().removeChamp(new String[]{"login","passe","idutilisateur"});
	Map<String,String> active=new HashMap<String,String>();
	active.put("1", "Active");
	active.put("2", "Desactive");
	builder.getFilterBuilder().setChampSelect("active", active);
	builder.getFilterBuilder().setChampSelect("idrole", new Role(), new String[]{"idrole","libelle"});
	builder.addNotVisibleChamp(new String[]{"login","passe"});
	builder.setLibelleFor("idutilisateur", "Id");
	builder.getFieldByName("active").setMethodForChamp("findActive");
%>
<%=builder.getFilterBuilder().getHTML()%>
<%=HTMLBuilder.beginPanel("liste informations",12) %>
<%=builder.getHTML()%>
<%=HTMLBuilder.endPanel() %>