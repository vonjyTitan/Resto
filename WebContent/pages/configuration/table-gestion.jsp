<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	TableBuilder builder=new TableBuilder(new Table(),request);
	builder.removeChamp(new String[]{"positionx","positiony"});
	builder.getFieldByName("etat").setMethodForChamp("getEtatString");
%>
<h3>Gestion des tables</h3>
<%
	out.print(HTMLBuilder.beginPanel("Liste des tables", 6));
%>
<form id="action-multiple" action="table-actionmultiple" method="post">
<%
	out.print(builder.getHTMLWithCheckbox("id"));
%>
<div class="form-group col-lg-12" style="text-align:right;">
	<input type="hidden" name="iddesination" id="iddesination-multiple"/>
	<input type="submit" value="Occuper" data="table-occuper" id="occuperbouton" class="btn btn-primary btn-xs"/>
	<input type="submit" value="Liberer" data="table-liberer" class="btn btn-success btn-xs" id="libererbouton"/>
	<a href="javascript:;" data="table-transferer" class="btn btn-warning btn-xs" id="transfererbouton">Transferer</a>
</div>
</form>
<%
	out.print(HTMLBuilder.endPanel());
	List<Table> data=builder.getData();
%>
<div class="col-lg-6 col-md-6 col-sm-6 mt">
<div class="panel panel-primary box-solid">
<div class="blue-header">
<h5>Emplacement</h5>
</div>
<div class="panel-body form-horizontal style-form" >
<div class="cl-lg-12" id="tableEmpl">
</div>
<div class="modal fade" id="transfert" style="margin-top:100px;margin-left:100px;">
	<div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button class="close" style="color: white;" aria-label="Close" data-dismiss="modal" type="button">
                    <span aria-hidden="true">x</span>
                </button>
                <h4>Transferer vers la table : </h4>
            </div>
            <div class="modal-body">
            <%
            for(Table t:data){
            %>
                <p><input type="radio" name="selected-table"> <%=t.getNom() %></p>
            <%
            }
            %>
            </div>
            
            <div class="modal-footer">
                <div class="col-lg-12">
                <input type="submit" class="btn btn-primary btn-xs" name="confirme"  value="Confirmer"/>
			<a class="btn btn-warning btn-xs closes"  href="javascript:;">Annuler</a>
                </div>
            </div>
        </div>
    </div>
</div>
<style>
.selected td{
	background-color:#b1b1ff !important;
}

</style>
<div class="cl-lg-12">
	<form action="table-modifplace" method="POST">
		<%for(Table table:data){
			%>
			<input type="hidden" name="id" value="<%=table.getIdtable()%>"/>
			<input type="hidden" id="<%=table.getIdtable()%>x" name="posx" value="<%=table.getPositionx()%>"/>
			<input type="hidden" id="<%=table.getIdtable()%>y" name="posy" value="<%=table.getPositiony()%>"/>
		<%}%>
		<input type="submit" value="Valider les places"/>
	</form>
</div>
<form id="form-transfert" action="table-transferer" method="post" style="display: none;">
	<input name="iddesination" id="iddesination-simple" type="hidden"/>
	<input name="id" id="idtable-simple" type="hidden"/>
</form>
	<script src="assets/js/gestionnaires-table.js"></script>
	<script src="assets/js/jquery.min.js"></script>
	<script>
	var afterSelectSimple=function(idtable)
	{
		$("#iddesination-simple").prop("value",idtable);
		$("#form-transfert").submit();
	}
	var afterSelectMultiple=function(idtable)
	{
		$("#iddesination-multiple").prop("value",idtable);
		$("#action-multiple").submit();
	}
	var selectedFunct=null;
	$(document).ready(function () {
		var tables = [];
		<%for(int i=0;i<data.size();i++){
			%>
			tables[<%=i %>]=new Table(<%=data.get(i).getPositionx() %>, <%=data.get(i).getPositiony() %>, 40,'<%=data.get(i).getNom() %>',<%=data.get(i).getIdtable() %>,true,<%=data.get(i).getEtat() %>);
		<%}%>
		initGestionTable("tableEmpl",tables,false,true,true,function(selected,xval,yval){
			$("#"+selected.id+"x").val(xval);
			$("#"+selected.id+"y").val(yval);
			$(".selected").removeClass("selected");
			$("#tr"+selected.id).addClass("selected");
		});
		$("input.btn").on("click",function(){
			$("#action-multiple").attr("action",$(this).attr("data"));
		});
		$(".close").on("click",function(){
			$(this).parents(".modal").prop("class","modal fade");
		});
		$(".closes").on("click",function(){
			$(this).parents(".modal").prop("class","modal fade");
		});
		$("#transfererbouton").on("click",function(){
			selectedFunct=afterSelectMultiple;
			$("#action-multiple").attr("action",$(this).attr("data"));
			$("#transfert").prop("class","modal show");
		});
		$("[name='transfertsimple']").on("click",function(){
			selectedFunct=afterSelectSimple;
			$("#idtable-simple").prop("value",$(this).attr("data"));
			$("#action-multiple").attr("action",$("#transfererbouton").attr("data"));
			$("#transfert").prop("class","modal show");
		});
		$("[name='confirme']").on("click",function(){
			$("#transfert").prop("class","modal fade");
			selectedFunct(2);
		});
	});
	
	</script>
</div>
</div>
</div>

