<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new Menu(),"menu-ajout",request);
	builder.removeChamp("idmenu");
	List<ArticleStock> articles=DaoModele.getInstance().findPageGenerique(1, new ArticleStock());
%>

<h3>Ajout nouveau menu</h3>
<%=builder.beginHTMLForm()%>
<%=HTMLBuilder.beginPanel("Informations", 6) %>
<%=builder.getHTMLBody() %>
<%=HTMLBuilder.endPanel() %>
<%=HTMLBuilder.beginPanel("Article en besoin", 6) %>
<table class="table table-striped table-advance table-hover table-bordered">
	<thead>
		<tr>
			<th>Article</th>
			<th>Quantite</th>
			<th></th>
		</tr>
	</thead>
	<tbody id="articles">
		<tr>
			<td>
			<select name="article">
				<option>hjbjhbklknl</option>
				</select>
			</td>
			<td>
				<input type="number" name="quantite"/>
			</td>
			<td>
			<a href="javascript:;" class="btn btn-danger btn-xs"><i class="fa fa-trash-o"></i></a>
			</td>
		</tr>
	</tbody>
</table>
<div class="col-lg-12" style="text-align:center;">
<a href="javascript:;" class="btn btn-primary btn-xs" style="width:150px;"><i class="fa fa-plus"></i></a>
</div>
<%=HTMLBuilder.endPanel() %>
<%=builder.endHTMLFormWithButton()%>
<script>
	var node="<tr><td></td><td></td><td><a href=\"javascript:;\" class=\"btn btn-danger btn-xs\"><i class=\"fa fa-trash-o\"></i></a></td></tr>";
		$(document).ready(function(){
		$("#articles").appendChild(node);
	});
</script>