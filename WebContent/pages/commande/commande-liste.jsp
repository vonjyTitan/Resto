<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	Commande crit=new Commande();
	crit.setNomTable("ensemble_commande");
	TableBuilder builder=new TableBuilder(crit,request);
	builder.removeChamp(new String[]{"lastensemble","idtable"});
	builder.getFieldByName("etat").setMethodForChamp("etatString");
	Map<String,String> etats=new HashMap<String,String>();
	etats.put("1","Active");
	etats.put("2","Annulée");
	builder.getFilterBuilder().setChampSelect("etat", etats);
	builder.setOrdre(new String[]{"idensemble"});
	builder.getFilterBuilder().removeChamp(new String[]{"lastensemble","idtable"});
	builder.getFilterBuilder().setChampToInterval(new String[]{"daty","nbpersonnes"});
%>
<h3>Liste de tous les commandes</h3>
<%=builder.getFilterBuilder().getHTML(12) %>
<%=HTMLBuilder.beginPanel("Liste des commandes", 12,"") %>
<form action="commande-jumellage" method="post">
<%=builder.getHTMLWithCheckbox("id")%>
<div class="form-group col-lg-12" style="text-align:right;">
	<input class="btn btn-warning btn-xs" type="submit" value="Jumeller les commandes"/>
</div>
</form>
<%=HTMLBuilder.endPanel()%>