<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	Menu crit=new Menu();
	crit.setNomTable("menu_libelle");
	PageFiche builder=new PageFiche(crit,request);
	builder.addNotVisibleChamp(new String[]{"idfamille"});
%>
<h3>Fiche menu</h3>
<%=HTMLBuilder.beginPanel("Informations", 6) %>
<%=builder.getBody()%>
<div class="form-controlle">
	
</div>
<%=HTMLBuilder.endPanel() %>
<%=HTMLBuilder.beginPanel("Articles en besoin", 6)%>
<%=HTMLBuilder.endPanel()%>