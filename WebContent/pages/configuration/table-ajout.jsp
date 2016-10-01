<%@page import="dao.DaoModele"%>
<%@page import="utilitaire.SessionUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new Table(),"table-ajout",request);
	builder.addNotVisibleChamp(new String[]{"idtable","positionx","positiony"});
	builder.removeChamp("etat");
	%>
	<h3>Ajout nouvelle table</h3>
	<%
	out.print(builder.getHTML(6));
	out.println(HTMLBuilder.beginPanel("Emplacement", 6));
	Table crit=new Table();
	List<Table> data=DaoModele.getInstance().findPageGenerique(1, crit);
	%>
	<div class="cl-lg-12" id="tableEmpl">
</div>
	<script src="assets/js/gestionnaires-table.js"></script>
	<script src="assets/js/jquery.min.js"></script>
	<script>
	$(document).ready(function () {
		var tables = [];
		
		<%
		int taille=data.size();
		for(int i=0;i<taille;i++){
			%>
			tables[<%=i %>]=new Table(<%=data.get(i).getPositionx() %>, <%=data.get(i).getPositiony() %>, 40,'<%=data.get(i).getNom() %>',<%=data.get(i).getIdtable() %>,false,<%=data.get(i).getEtat() %>);
		<%}%>
		var tableNouveau=new Table(250, 75, 40,'NV',-1,true,0);
		tables[<%=taille%>]=tableNouveau;
		initGestionTable("tableEmpl",tables,false,true,true,function(selected,xval,yval){
			if(selected.id==-1){
				$("#positionx").val(xval);
				$("#positiony").val(yval);
			}
		});
		
		$("#positionx").val(250);
		$("#positiony").val(75);
		
	});
	</script>
	<%
	out.print(HTMLBuilder.endPanel());
%>