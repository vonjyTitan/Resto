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
	out.print(builder.getHTML());
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
<div class="cl-lg-12">
	<form action="Action?to=table-modifplace" method="POST">
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
		});
		
	});
	</script>
</div>
</div>
</div>