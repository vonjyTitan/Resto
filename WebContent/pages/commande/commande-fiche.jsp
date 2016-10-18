<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>
<script src="assets/js/jquery.min.js"></script>
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
			int reste=(mc.getQuantite()-mc.getAnnuler()-mc.getLivrer());
			boolean enable=reste<=0;
		%>
		<tr>
			<td><%=mc.getIdcommande_menu() %></td>
			<td><%=mc.getMenu() %></td>
			<td><%=mc.getRemarque() %></td>
			<td><%=mc.getQuantite() %></td>
			<td><%=mc.getAnnuler() %></td>
			<td><%=mc.getLivrer() %></td>
			<td><input style="width:60px;" <%=((enable) ? "disabled" : "") %> type="number" name="quantite" value="<%=reste%>"/></td>
			<td>
			<a class="btn btn-primary btn-xs" name="livrer" <%=((enable) ? "disabled" : "") %> href="javascript:;">Livrer</a>
			<a class="btn btn-warning btn-xs"  name="annuler" <%=((enable) ? "disabled" : "") %> href="javascript:;">Annuler</a>
			<input type="hidden" name="id" value="<%=mc.getIdcommande_menu() %>"/>
			</td>
			</tr>
		<%
		}
		%>
	</tbody>
</table>
</div>
<form action="commande-annuler" id="menu-annuler" style="display: none;" method="post">
	<input name="id" id="idannulation" type="hidden"/>
	<input name="quantite" id="quantiteannulation" type="hidden"/>
	<input name="motif" id="motifannulation" type="hidden"/>
	<input name="idensemble" value="<%=SessionUtil.getValForAttr(request, "id") %>" type="hidden"/>
</form>
<form action="commande-livrer" id="menu-livrer" style="display: none;" method="post">
	<input name="id" id="idlivraison" type="hidden"/>
	<input name="quantite" id="quantitelivraison" type="hidden"/>
	<input name="idensemble" value="<%=SessionUtil.getValForAttr(request, "id") %>" type="hidden"/>
</form>
<div class="modal fade" id="motif" style="margin-top:100px;margin-left:100px;">
	<div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button class="close" style="color: white;" aria-label="Close" data-dismiss="modal" type="button">
                    <span aria-hidden="true">x</span>
                </button>
                <h4>Motif d'annulation</h4>
            </div>
            <div class="modal-body">
                <textarea id="motif-text" style="width: 100%;"></textarea>
            </div>
            
            <div class="modal-footer">
                <div class="col-lg-12">
                <a class="btn btn-primary btn-xs" name="confirme"  href="javascript:;">Confirmer</a>
			<a class="btn btn-warning btn-xs closes"  href="javascript:;">Annuler</a>
                </div>
            </div>
        </div>
    </div>
</div>
<%=HTMLBuilder.endPanel()%>
<%=fiche.beginPanel("Fiche", 4)%>
<%=fiche.getBody() %>
<div class="form-group col-lg-12" style="text-align:right;">
	<a class="btn btn-primary btn-xs"   href="javascript:;">Addition</a>
	<a class="btn btn-warning btn-xs"  href="javascript:;">Annuler tous</a>
</div>
<%=fiche.endPanel() %>
<script>
	$(document).ready(function(){
		$("a[name='livrer']").on("click",function(){
			$("#quantitelivraison").prop("value",$(this).parent().parent().find("input[name='quantite']").prop("value"));
			$("#idlivraison").prop("value",$("> input",$(this).parent()).prop("value"));
			$("#menu-livrer").submit();
		});
		$("a[name='annuler']").on("click",function(){
			$("#quantiteannulation").prop("value",$(this).parent().parent().find("input[name='quantite']").prop("value"));
			$("#idannulation").prop("value",$("> input",$(this).parent()).prop("value"));
			$("#motif").prop("class","modal show");
		});
		$(".close").on("click",function(){
			$("#motif").prop("class","modal fade");
		});
		$(".closes").on("click",function(){
			$("#motif").prop("class","modal fade");
		});
		$("a[name='confirme']").on("click",function(){
			$("#motif").prop("class","modal fade");
			$("#motifannulation").prop("value",$("#motif-text").prop("value"));
			$("#menu-annuler").submit();
		});
	});
</script>