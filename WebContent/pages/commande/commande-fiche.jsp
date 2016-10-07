<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	Commande commande=new Commande();
	commande.setNomTable("ensemble_commande");
	List<Commande> cms=DaoModele.getInstance().findPageGenerique(1, commande," and idensemble="+SessionUtil.getValForAttr(request, "id"));
	if(cms.size()==0)
		throw new Exception("Pas de resultat pour votre consultation");
	PageFiche fiche=new PageFiche<Commande>(commande,request);
	fiche.setData(cms.get(0));
	fiche.setOrdre(new String[]{"idcommande","table_liste"});
	fiche.removeChamp(new String[]{"idtable"});
	MenuCommande crit=new MenuCommande();
	crit.setNomTable("menu_commande_libelle");
	List<MenuCommande> mcs=DaoModele.getInstance().findPageGenerique(1, crit," and idcommande in ("+((Commande)fiche.getData()).getIdcommande()+")");
%>
<%=HTMLBuilder.beginPanel("Liste des menus", 8)%>
<form action="commande-multipleaction" method="post">
<div class="col-lg-12 col-md-12 col-sm-12 table-responsive">
<table class="table table-striped table-advance table-hover table-bordered">
	<thead>
		<tr>
			<th></th>
			<th>Id</th>
			<th>Menu</th>
			<th>Remarque</th>
			<th>Quantité</th>
			<th>Annulé</th>
			<th>Livré</th>
			<th></th>
			<th>Operations</th>
		</tr>
	</thead>
	<tbody>
		<%
		for(MenuCommande mc:mcs){
		%>
		<tr>
			<td><input type="checkbox" name="idcommande_menu" value="<%=mc.getIdcommande_menu()%>"/></td>
			<td><%=mc.getIdcommande_menu() %></td>
			<td><%=mc.getMenu() %></td>
			<td><%=mc.getRemarque() %></td>
			<td><%=mc.getQuantite() %></td>
			<td><%=mc.getAnnuler() %></td>
			<td><%=mc.getLivrer() %></td>
			<td><input style="width:60px;" type="number" name="quantite" value="0"/></td>
			<td></td>
			</tr>
		<%
		}
		%>
	</tbody>
</table>
</div>
</form>
<%=HTMLBuilder.endPanel()%>
<%=fiche.getHTML("Fiche", 4)%>