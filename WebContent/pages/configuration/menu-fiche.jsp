<%@page import="utilitaire.UtileAffichage"%>
<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	Menu crit=new Menu();
	crit.setNomTable("menu_libelle");
	PageFiche builder=new PageFiche(crit,request);
	builder.addNotVisibleChamp(new String[]{"idfamille"});
	MenuArticle critma=new MenuArticle();
	critma.setNomTable("menu_article_libelle");
	List<MenuArticle> ma=DaoModele.getInstance().findPageGenerique(1, critma," and idmenu="+SessionUtil.getValForAttr(request, "id"));
%>
<h3>Fiche menu</h3>
<%=HTMLBuilder.beginPanel("Informations", 6) %>
<%=builder.getBody()%>
<div class="form-controlle col-lg-12" style="text-align: right;">
	<a class="btn btn-success btn-xs" href="main.jsp?cible=configuration/menu-modif&id=<%=((Menu)builder.getEntity()).getIdmenu()%>">Modifier</a>
	<a class="btn btn-danger btn-xs" href="menu-supprimer&id=<%=((Menu)builder.getEntity()).getIdmenu()%>">Supprimer</a>
</div>
<%=HTMLBuilder.endPanel() %>
<%=HTMLBuilder.beginPanel("Articles en besoin", 6)%>
<table class="table table-striped table-advance table-hover table-bordered">
	<thead>
		<tr>
			<th>Article</th>
			<th>Quantite</th>
		</tr>
	</thead>
	<tbody>
		<%
			for(MenuArticle mia:ma){
				%>
					<tr>
						<td><a href="main.jsp?cible=configuration/article-fiche&id=<%=mia.getIdarticle()%>"><%=mia.getArticle() %></a></td>
						<td><%=mia.getQuantite()+" "+mia.getUnite() %></td>
					</tr>
				<%
			}
		%>
	</tbody>
</table>
<%=HTMLBuilder.endPanel()%>