<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	TableBuilder builder=new TableBuilder(new Utilisateur(),request);
	builder.getEntity().setNomTable("utilisateur_libelle");
	builder.setLienForModif("main.jsp?cible=configuration/utilisateur-modif");
	builder.getFilterBuilder().removeChamp(new String[]{"login","passe"});
	Map<String,String> active=new HashMap<String,String>();
	active.put("1", "Active");
	active.put("2", "Desactive");
	builder.getFilterBuilder().setChampSelect("active", active);
	builder.addNotVisibleChamp(new String[]{"login","passe","idrole"});
	builder.setLibelleFor("idutilisateur", "Id");
	builder.getFieldByName("active").setMethodForChamp("findActive");
	builder.setOrdre(new String[]{"idutilisateur","nom","prenom","role","active"});
	builder.setLienForId("main.jsp?cible=configuration/utilisateur-fiche");
%>
<%=builder.getFilterBuilder().getHTML()%>
<%=HTMLBuilder.beginPanel("liste informations",12) %>
<%=builder.getHTML()%>
<%=HTMLBuilder.endPanel() %>