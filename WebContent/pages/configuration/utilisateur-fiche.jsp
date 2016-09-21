<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	PageFiche builder=new PageFiche(new Utilisateur(),request);
	builder.getEntity().setNomTable("utilisateur_libelle");
	builder.addNotVisibleChamp(new String[]{"login","passe","idrole"});
	builder.getFieldByName("active").setMethodForChamp("findActive");
%>
<h3><a href="main.jsp?cible=configuration/liste-utilisateur"><i class="fa fa-angle-left"></i><i class="fa fa-angle-left"></i></a> Detail utilisateur</h3>
<%=builder.beginPanel("Informations general", 6) %>
<%=builder.getBody()%>
<div class="form-group col-lg-12" style="text-align: right;">
<%if(((Utilisateur)builder.getEntity()).getActive()==2){ %>
	<a class="btn btn-success btn-xs" href="login-active?id=<%=((Utilisateur)builder.getEntity()).getIdutilisateur()%>"><i class="fa fa-check"></i>Activer</a>
		<%
		}
		else 
		{
		%><a class="btn btn-danger btn-xs" href="login-desactive?id=<%=((Utilisateur)builder.getEntity()).getIdutilisateur()%>"><i class="fa fa-trash-o "></i> Desactiver</a>
		<%} %>
		<a class="btn btn-primary btn-xs" href="main.jsp?cible=configuration/utilisateur-modif&id=<%=((Utilisateur)builder.getEntity()).getIdutilisateur()%>"><i class="fa fa-pencil "></i> Modifier</a>
</div>
<%=builder.endPanel() %>
<%=HTMLBuilder.beginPanel("Activites recent", 6)%>
<%=HTMLBuilder.endPanel()%>