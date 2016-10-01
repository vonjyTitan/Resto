<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<script src="assets/js/jquery.min.js"></script>
<%
	Menu crit=new Menu();
	crit.setNomTable("menu_libelle");
	TableBuilder builder=new TableBuilder(crit,request);
	builder.addNotVisibleChamp("idfamille");
	builder.setLienForModif("main.jsp?cible=configuration/menu-modif");
	builder.setLienForDelete("menu-supprimer");
	builder.getFilterBuilder().setChampToInterval("prix");
%>
<h3>Liste des menus</h3>
<%=builder.getFilterBuilder().getHTML() %>
<%=HTMLBuilder.beginPanel("Liste",12) %>
<%=builder.getHTML()%>
<%=HTMLBuilder.endPanel()%>