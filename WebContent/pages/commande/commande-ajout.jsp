<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
Table crit=new Table();
List<Table> data=DaoModele.getInstance().findPageGenerique(1, crit);
Menu critM=new Menu();
critM.setPackSize(100);
List<Menu> menus=DaoModele.getInstance().findPageGenerique(1, critM);
%>
<form action="commande-ajout" class="form-horizontal style-form" method="post">
<div class="col-lg-5">
<%=HTMLBuilder.beginPanel("Informations", 12)%>
	<div id="libellecontainer" class="form-group col-lg-12">
	<div class="col-sm-4 col-sm-4 "><label class="control-label" for="idtable">Table sans commande : </label></div>
	<div class="col-sm-5">
	<select name="idtable" id="idtable" class="form-control">
		<option value="0">--</option>
		<%
			for(Table table:data){
				if(table.getEtat()!=ConstantEtat.ETAT_OCCUPER_SANS_COMMANDE)
					continue;
				%>
				<option value="<%=table.getIdtable()%>"><%=table.getNom() %></option>
				<%
			}
		%>
	</select>
	</div>
	<div class="col-sm-3">
	<a class="btn btn-success" data-toggle="modal" data-target="plan" id="popuptable">Plan</a>
	</div>
	</div>
	<div id="libellecontainer" class="form-group col-lg-12">
	<div class="col-sm-4 col-sm-4 "><label class="control-label" for="client">Nombre de client : </label></div>
	<div class="col-sm-7">
	<input name="client" id="client" class="form-control" value="" type="number">
	</div>
	</div>
<%=HTMLBuilder.endPanel()%>
<div class="form-group col-lg-12"><label class="control-label col-lg-1" style="margin-left:50px;"></label><div class="col-lg-8"> <input class="btn btn-primary" value="Valider" type="submit"> <input class="btn btn-danger" value="Annuler" type="reset"></div></div>
</div>
<%=HTMLBuilder.beginPanel("Menus", 7)%>
<table class="table table-striped table-advance table-hover table-bordered">
	<thead>
		<tr>
			<th>Menu</th>
			<th>Quantite</th>
			<th>Remarque</th>
			<th></th>
		</tr>
	</thead>
	<tbody id="menus"></tbody>
</table>
<div class="col-lg-12" style="text-align:center;">
<a href="javascript:;" id="adddmenu" class="btn btn-primary btn-xs" style="width:150px;"><i class="fa fa-plus"></i></a>
</div>
<%=HTMLBuilder.endPanel()%>
</form>
<div class="modal fade" id="plan" style="margin-top:100px;margin-left:100px;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button class="close" style="color: white;" aria-label="Close" data-dismiss="modal" type="button">
                    <span aria-hidden="true">x</span>
                </button>
                <h4>Plan des tables</h4>
            </div>
            <div class="modal-body">
                <div class="row" style="text-align:center;">
                <div class="cl-lg-12" id="tableEmpl">
                 </div>
            </div>
            
            <div class="modal-footer">
                <p style="color:red;text-align:center;" id="erreur_table"></p>
            </div>
        </div>
    </div>
</div>  
<script src="assets/js/gestionnaires-table.js"></script>
	<script src="assets/js/jquery.min.js"></script>
	<script>
	var taille=<%=menus.size()%>;
	var menus=[];
	var etat={
			occ_sans_comm : <%=ConstantEtat.ETAT_OCCUPER_SANS_COMMANDE%>,
			occ_avec_comm : <%=ConstantEtat.ETAT_OCCUPER_AVEC_COMMANDE%>,
			libre : <%=ConstantEtat.ETAT_LIBRE%>,
			reserver : <%=ConstantEtat.ETAT_RESERVER%>
	};
	$(document).ready(function () {
		$("#popuptable").on("click",function(){
			$("#plan").prop("class","modal show");
			$("#erreur_table").html("");
		});
		$(".close").on("click",function(){
			$("#plan").prop("class","modal fade");
		});
		<%int ii=0;for(Menu menu:menus){%>menus[<%=ii%>]=[];menus[<%=ii%>]["id"]=<%=menu.getIdmenu()%>;menus[<%=ii%>]["lib"]='<%=menu.getLibelle()%>';<%ii++;}%>
		var tables = [];
		<%
		int taille=data.size();
		for(int i=0;i<taille;i++){
			%>
			tables[<%=i %>]=new Table(<%=data.get(i).getPositionx() %>, <%=data.get(i).getPositiony() %>, 40,'<%=data.get(i).getNom() %>',<%=data.get(i).getIdtable() %>,false,<%=data.get(i).getEtat() %>);
		<%}%>
		
		initGestionTable("tableEmpl",tables,false,true,true,function(selected,xval,yval){
			if(selected.etat!=2){
				var err="";
				if(selected.etat==etat.libre){
					err="La table est encore libre, veuillez l'occuper d'abord";
				}
				if(selected.etat==etat.occ_avec_comm){
					err="La table a deja envoyer une commande avant";
				}
				if(selected.etat==etat.reserver){
					err="La table est reserver";
				}
				$("#erreur_table").html(err);
				return;
			}
			$("#idtable").prop("value",selected.id);
			$("#plan").prop("class","modal fade");
		});
		$("#adddmenu").on("click",function(){
			addChild();
		});
		addChild();
		addChild();
		addChild();
	});
	function addChild(){
		var node = "<tr><td><select name=\"menu\"><option value=0 >--</option>";
		for(var ii=0;ii<taille;ii++){
				node+="<option value=\""+menus[ii]["id"]+"\">"+menus[ii]["lib"]+"</option>";
		}
		node+="</select></td>";
		node+="<td><input name=\"quantite\" type=\"number\"/></td>"
		+"<td style=\"width: 100px;\"><textarea style=\"height: 25px;\" name=\"remarque\"></textarea></td><td><a href=\"javascript:;\" name=\"suppr\" class=\"suppr btn btn-danger btn-xs\"><i class=\"fa fa-trash-o\"></i></a></td></tr>";
		
		$("#menus").append(node);
		$("[name='suppr']").on("click",function(){$(this).parent("td").parent("tr").remove();});
	}
	</script>