<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<script src="assets/js/jquery.min.js"></script>
<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new Menu(),"menu-ajout",request);
	builder.removeChamp("idmenu");
	List<ArticleStock> articles=DaoModele.getInstance().findPageGenerique(1, new ArticleStock());
	String[] articlesRet=request.getParameterValues("article");
	String[] quantes=request.getParameterValues("quantite");
%>
<div class="col-lg-12" ng-app="myApp" ng-controller="myCtrl">
<h3>Ajout nouveau menu</h3>
<%=builder.beginHTMLForm()%>
<%=HTMLBuilder.beginPanel("Informations", 6) %>
<%=builder.getHTMLBody() %>
<%=builder.getHTMLButton() %>
<%=HTMLBuilder.endPanel() %>
<%=HTMLBuilder.beginPanel("Article en besoin", 6) %>
<table class="table table-striped table-advance table-hover table-bordered">
	<thead>
		<tr>
			<th>Article</th>
			<th>Quantite</th>
			<th></th>
			<th></th>
		</tr>
	</thead>
	<tbody id="articles">
	</tbody>
</table>
<div class="col-lg-12" style="text-align:center;">
<a href="javascript:;" id="addarticle" class="btn btn-primary btn-xs" style="width:150px;"><i class="fa fa-plus"></i></a>
</div>
<%=HTMLBuilder.endPanel() %>
<%=builder.endHTMLForm()%>
<script>
	var unite=[];
	unite[0]='--';
	
		$(document).ready(function(){
			<%if(articlesRet!=null && articlesRet.length>0){
				for(int i=0;i<articlesRet.length;i++){
					%>
						addChild("<%=articlesRet[i]%>",<%=quantes[i]%>,unite[<%=articlesRet[i]%>]);
					<%
				}
			}
			else{%>
				addChild(null,0,"--");
				addChild(null,0,"--");
				addChild(null,0,"--");
			<%}%>
	});
		$("#addarticle").on("click",function(){
			addChild(null,0,"--");
		});
		$("select[name='article']").on("change",function(){
			alert($(this).value);
		});
		function addChild(articlee,quantite,unitee){
			var node = "<tr><td><select onChange=\"changeUnite(this);\" name=\"article\"><option value=0 >--</option>";
			<%for(ArticleStock article:articles){%>unite[<%=article.getIdarticle()%>]='<%=article.getUnite()%>';node+="<option value=<%=article.getIdarticle()%>><%=article.getLibelle()%></option>";<%}%>
			node+="</select></td>";
			node+="<td><input name=\"quantite\" value=\""+quantite+"\" type=\"number\"/></td>"
			+"<td style=\"width: 100px;\"><span>"+unitee+"</span></td><td><a href=\"javascript:;\" name=\"suppr\" class=\"suppr btn btn-danger btn-xs\"><i class=\"fa fa-trash-o\"></i></a></td></tr>";
			if(articlee){
				//TODO set selected index
				//$("select",$(node));
			}
			$("#articles").append(node);
			$("[name='suppr']").on("click",function(){$(this).parent("td").parent("tr").remove();});
		}
		function changeUnite(select){
			$("span",$(select).parent("td").parent("tr")).html(" "+unite[select.value]);
		}
		
</script>