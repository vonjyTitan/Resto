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
	String disable=(cms.get(0).getEtat()==ConstantEtat.ETAT_COMMANDE_ANNULER) ? "disabled=true" : "";
	fiche.setOrdre(new String[]{"idcommande","table_liste"});
	fiche.removeChamp(new String[]{"idtable"});
	MenuCommande crit=new MenuCommande();
	crit.setNomTable("menu_commande_libelle");
	List<MenuCommande> mcs=DaoModele.getInstance().findPageGenerique(1, crit," and idcommande in ("+((Commande)fiche.getData()).getIdcommande()+")");
	Menu critM=new Menu();
	critM.setPackSize(100);
	List<Menu> menus=DaoModele.getInstance().findPageGenerique(1, critM);

	%>
		<h4><a href="main.jsp?cible=commande/commande-liste"><i class="fa fa-angle-left"></i><i class="fa fa-angle-left"></i></a> Fiche commande ensemble numero <%if(cms.get(0).getEtat()==ConstantEtat.ETAT_COMMANDE_ANNULER){
			%><strong style="color : red !important;"> , Commande annulée </strong><%
	}%></h4>

<%=HTMLBuilder.beginPanel("Liste des menus", 8)%>
<div class="col-lg-12 col-md-12 col-sm-12 table-responsive">
<table class="table table-striped table-advance table-hover table-bordered">
	<thead>
		<tr>
			<th>Id</th>
			<th>Menu</th>
			<th>Remarque</th>
			<th>Qté</th>
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
			<td><input <%=disable %> style="width:60px;" <%=((enable) ? "disabled" : "") %> type="number" name="quantite" max="<%=reste%>" value="<%=reste%>"/></td>
			<td style="width: 200px;">
			<a class="btn btn-success btn-xs" <%=disable %> name="livrer" <%=((enable) ? "disabled" : "") %> href="javascript:;">Livrer</a>
			<a class="btn btn-warning btn-xs" <%=disable %>  name="annuler" <%=((enable) ? "disabled" : "") %> href="javascript:;">Annuler</a>
			<a class="btn btn-primary btn-xs" <%=disable %>  name="rajouter" href="javascript:;">Rajouter</a>
			<input type="hidden" name="id" value="<%=mc.getIdcommande_menu() %>"/>
			</td>
			</tr>
		<%
		}
		%>
	</tbody>
</table>
</div>
<div class="col-lg-12" style="border-top: 1px solid #eff2f7;">
	<h5 style="text-align: center;">Ajouter d'autres menus</h5>
	<form action="commande-ajoutMenus" method="post">
	<input name="idensemble" value="<%=SessionUtil.getValForAttr(request, "id") %>" type="hidden"/>
		<table class="table table-striped table-advance table-hover table-bordered">
	<thead>
		<tr>
			<th>Menu</th>
			<th>Quantite</th>
			<th>Remarque</th>
			<th></th>
		</tr>
	</thead>
	<tbody id="ajout-menu"></tbody>
</table>
<div class="col-lg-12" style="text-align:center;">
<a href="javascript:;" id="adddmenu" class="btn btn-primary btn-xs" <%=disable %> style="width:150px;"><i class="fa fa-plus"></i></a>
<br>
<input type="submit" class="btn btn-success btn-xs" <%=disable %> style="width: 150px;margin-top: 10px;" value="Envoyer"/>
</div>
	</form>
</div>
<form action="commande-livrer" id="menu-livrer" style="display: none;" method="post">
	<input name="id" id="idlivraison" type="hidden"/>
	<input name="quantite" id="quantitelivraison" type="hidden"/>
	<input name="idensemble" value="<%=SessionUtil.getValForAttr(request, "id") %>" type="hidden"/>
</form>
<div class="modal fade" id="motif" style="margin-top:100px;margin-left:100px;">
	<div class="modal-dialog">
	<form action="" id="form-annulation" method="post">
        <div class="modal-content">
            <div class="modal-header">
                <button class="close" style="color: white;" aria-label="Close" data-dismiss="modal" type="button">
                    <span aria-hidden="true">x</span>
                </button>
                <h4>Motif d'annulation</h4>
            </div>
            <div class="modal-body">
                <textarea id="motif-text" name="motif" style="width: 100%;"></textarea>
                <input name="id" id="idannulation" type="hidden"/>
				<input name="quantite" id="quantiteannulation" type="hidden"/>
				<input name="idensemble" value="<%=SessionUtil.getValForAttr(request, "id") %>" type="hidden"/>
            </div>
            
            <div class="modal-footer">
                <div class="col-lg-12">
                <input type="submit" class="btn btn-primary btn-xs" name="confirme"  value="Confirmer"/>
			<a class="btn btn-warning btn-xs closes"  href="javascript:;">Annuler</a>
                </div>
            </div>
        </div>
        </form>
    </div>
</div>
<div class="modal fade" id="rajout" style="margin-top:100px;margin-left:100px;">
	<div class="modal-dialog">
	<form action="commande-rajoutMenu" method="post">
        <div class="modal-content">
            <div class="modal-header">
                <button class="close" style="color: white;" aria-label="Close" data-dismiss="modal" type="button">
                    <span aria-hidden="true">x</span>
                </button>
                <h4>Quantité</h4>
            </div>
            <div class="modal-body">
            <input name="idensemble" value="<%=SessionUtil.getValForAttr(request, "id") %>" type="hidden"/>
                <input type="number" name="quantite" value="1"/>
                <input name="idcommande_menu" type="hidden" id="idmc_rajout"/>
            </div>
            
            <div class="modal-footer">
                <div class="col-lg-12">
                <input type="submit" class="btn btn-primary btn-xs" name="confirme-rajout"  value="Confirmer"/>
				<a class="btn btn-warning btn-xs closes"  href="javascript:;">Annuler</a>
                </div>
            </div>
        </div>
        </form>
    </div>
</div>
<div class="modal fade" id="addition" style="margin-top:100px;margin-left:100px;">
	<div class="modal-dialog">
	<form action="commande-addition" method="post">
        <div class="modal-content">
            <div class="modal-header">
                <button class="close" style="color: white;" aria-label="Close" data-dismiss="modal" type="button">
                    <span aria-hidden="true">x</span>
                </button>
                <h4>Addition</h4>
            </div>
            <div class="modal-body">
            <input name="idensemble" value="<%=SessionUtil.getValForAttr(request, "id") %>" type="hidden"/>
                <div class="">
<table class="table table-striped table-advance table-hover table-bordered">
	<thead>
		<tr>
			<th>Id</th>
			<th>Menu</th>
			<th>Remarque</th>
			<th>Qté</th>
			<th>Payé</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<%
		for(MenuCommande mc:mcs){
			if(mc.getQuantite()-mc.getAnnuler()<=0)
				continue;
			int reste=(mc.getQuantite()-mc.getAnnuler()-mc.getPayer());
			boolean enable=reste<=0;
		%>
		<tr>
			<td><%=mc.getIdcommande_menu() %></td>
			<td><%=mc.getMenu() %></td>
			<td><%=mc.getRemarque() %></td>
			<td><%=(mc.getQuantite()-mc.getAnnuler()) %></td>
			<td><%=mc.getPayer() %></td>
			<td><input style="width:60px;" <%=((enable) ? "disabled" : "") %> type="number" name="quantite" max="<%=reste%>" value="<%=reste%>"/></td>
			</tr>
		<%
		}
		%>
	</tbody>
</table>
</div>
            </div>
            
            <div class="modal-footer">
                <div class="col-lg-12">
                <input type="submit" class="btn btn-primary btn-xs" name="confirme-rajout"  value="Confirmer"/>
				<a class="btn btn-warning btn-xs closes"  href="javascript:;">Annuler</a>
                </div>
            </div>
        </div>
        </form>
    </div>
</div>
<%=HTMLBuilder.endPanel()%>
<%=fiche.beginPanel("Fiche", 4)%>
<%=fiche.getBody() %>
<div class="form-group col-lg-12" style="text-align:right;">
	<a class="btn btn-primary btn-xs " name="addition"  href="javascript:;">Addition</a>
	<a class="btn btn-warning btn-xs" <%=disable %> name="annuler-tous"  href="javascript:;">Annuler tous</a>
</div>
<%=fiche.endPanel() %>
<script>
var taille=<%=menus.size()%>;
var menus=[];
	$(document).ready(function(){
		<%int ii=0;for(Menu menu:menus){%>menus[<%=ii%>]=[];menus[<%=ii%>]["id"]=<%=menu.getIdmenu()%>;menus[<%=ii%>]["lib"]='<%=menu.getLibelle()%>';<%ii++;}%>
		
		$("a[name='livrer']").on("click",function(){
			$("#quantitelivraison").prop("value",$(this).parent().parent().find("input[name='quantite']").prop("value"));
			$("#idlivraison").prop("value",$("> input",$(this).parent()).prop("value"));
			$("#menu-livrer").submit();
		});
		$("a[name='annuler']").on("click",function(){
			$("#quantiteannulation").prop("value",$(this).parent().parent().find("input[name='quantite']").prop("value"));
			$("#idannulation").prop("value",$("> input",$(this).parent()).prop("value"));
			$("#form-annulation").prop("action","commande-annuler");
			$("#motif").prop("class","modal show");
		});
		$("a[name='rajouter']").on("click",function(){
			$("#idmc_rajout").prop("value",$("input",$(this).parent()).prop("value"));
			$("#rajout").prop("class","modal show");
		});
		$(".close").on("click",function(){
			$(this).parents(".modal").prop("class","modal fade");
		});
		$(".closes").on("click",function(){
			$(this).parents(".modal").prop("class","modal fade");
		});
		$("a[name='annuler-tous']").on("click",function(){
			$("#form-annulation").prop("action","commande-annulerTous");
			$("#motif").prop("class","modal show");
		});
		$("a[name='addition']").on("click",function(){
			$("#addition").prop("class","modal show");
		});
		$("#adddmenu").on("click",function(){
			addChild();
		});
		addChild();
		addChild();
	});
	function addChild(){
		var disable="<%=disable %>";
		var node = "<tr><td><select "+disable+" name=\"menu\"><option value=0 >--</option>";
		for(var ii=0;ii<taille;ii++){
				node+="<option value=\""+menus[ii]["id"]+"\">"+menus[ii]["lib"]+"</option>";
		}
		node+="</select></td>";
		node+="<td><input "+disable+" name=\"quantite\" type=\"number\"/></td>"
		+"<td style=\"width: 100px;\"><textarea "+disable+" style=\"height: 25px;\" name=\"remarque\"></textarea></td><td><a href=\"javascript:;\" name=\"suppr\" class=\"suppr btn btn-danger btn-xs\" "+disable+"><i class=\"fa fa-trash-o\"></i></a></td></tr>";
		
		$("#ajout-menu").append(node);
		$("[name='suppr']").on("click",function(){$(this).parent("td").parent("tr").remove();});
	}
	</script>