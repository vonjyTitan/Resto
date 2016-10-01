<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	ArticleStock critArticle=new ArticleStock();
	critArticle.setNomTable("article_stock_libelle");
	PageFiche builder=new PageFiche(critArticle,request);
	builder.removeChamp(new String[]{"idarticle","idunite","idcategorie"});
	builder.getFieldByName("quantite").setMethodForChamp("findQuantite");
%>
<h3><a href="main.jsp?cible=configuration/article-liste"><i class="fa fa-angle-left"></i><i class="fa fa-angle-left"></i></a> Fiche article</h3>
<%=builder.getHTML("Imformations", 6)%>
<%=HTMLBuilder.beginPanel("Mouvement",6)%>
<%=HTMLBuilder.endPanel()%>