<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new MenuFamille(),"crud-update?classenom=com.mapping.MenuFamille&cible=configuration/famille-liste&refereur=main.jsp?cible=configuration/famille-modif",request);
	builder.setChampTextarea("description");
	builder.addNotVisibleChamp("idfamille");
	builder.setValueFromDatabase(SessionUtil.getValForAttr(request, "id"));
%>
<h3><a href="main.jsp?cible=configuration/famille-liste"><i class="fa fa-angle-left"></i><i class="fa fa-angle-left"></i></a>Modification famille menu</h3>
<%=builder.getHTML(6)%>