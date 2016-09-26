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
	<input type="submit" value="Occuper" data="table-occuper" id="occuperbouton" class="btn btn-primary btn-xs"/>
	<input type="submit" value="Liberer" data="table-liberer" class="btn btn-success btn-xs" id="libererbouton"/>
	<input type="submit" value="Transferer" data="table-transferer" class="btn btn-warning btn-xs" id="transfererbouton"/>
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
	<script src="assets/js/gestionnaires-table.js"></script>
	<script src="assets/js/jquery.min.js"></script>
	<script>
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
		
	});
	</script>
</div>
</div>
</div>
