<%@page import="com.affichage.InsertUpdateBuilder.ERROR_SHOW"%>
<%@page import="com.affichage.*"%>
<%@page import="com.mapping.*"%>

<jsp:include page='../verificateur.jsp'/>

 	<% InsertUpdateBuilder builder=new InsertUpdateBuilder(new CategorieMenue(),"ajoutcategorie",request);
 		builder.setTitle("Saisie Categorie");
 		builder.removeChamp("idcategorie");
 	%>
 	<%=builder.beginHTMLForm()%>
 	<%=HTMLBuilder.beginPanel("Information general") %>
 	<%=builder.getHTMLBody() %>
 	<%=HTMLBuilder.endPanel() %>
 	<%=HTMLBuilder.beginPanel("autres informations") %>
	 <div id="myCanvas" class="col-lg-12">
 </div>
 <script type="text/javascript" src="assets/js/jquery.min.js">
 </script>
<script type="text/javascript" src="assets/js/gestionnaires-table.js">
 </script>
 <script>
	$(function(){
	 var table =  new Table(150,125,50,"TA1",1);
	 var array = [];
		array[0]=table;
	 initGestionTable("myCanvas",array,false,false,true);});
 </script>
 	<%=HTMLBuilder.endPanel() %>
 	<%=HTMLBuilder.beginPanel("liste informations",10) %>
 	<%=HTMLBuilder.endPanel() %>
 	<%=HTMLBuilder.beginPanel("petite informations",2) %>
 	<%=HTMLBuilder.endPanel() %>
 	<%=builder.endHTMLFormWithButton()%>