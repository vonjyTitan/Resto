<%@page import="com.affichage.FormBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>

 	<% FormBuilder builder=new FormBuilder(new CategorieMenue(),"ajoutcategorie",request);
 		builder.setTitle("Saisie Categorie");
 		builder.removeChamp("idcategorie");
 	%>
 	<%=builder.beginHTMLForm()%>
 	<%=HTMLBuilder.beginPanel("Information general") %>
 	<%=builder.getHTMLBody() %>
 	<%=HTMLBuilder.endPanel() %>
 	<%=HTMLBuilder.beginPanel("autres informations") %>
 	<%=HTMLBuilder.endPanel() %>
 	<%=HTMLBuilder.beginPanel("liste informations",10) %>
 	<%=HTMLBuilder.endPanel() %>
 	<%=HTMLBuilder.beginPanel("petite informations",2) %>
 	<%=HTMLBuilder.endPanel() %>
 	<%=builder.endHTMLFormWithButton()%>