<%@page import="utilitaire.SessionUtil"%>
<%@page import="dao.DaoModele"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>
<%
	InsertUpdateBuilder builder=new InsertUpdateBuilder(new MenuFamille(),"crud-insert?classenom=com.mapping.MenuFamille&cible=configuration/famille-liste&refereur=main.jsp?cible=configuration/famille-ajout",request);
	builder.setChampTextarea("description");
	builder.removeChamp("idfamille");
	builder.setTitle("Ajout famille menu");
%>
<%=builder.getHTML(6)%>