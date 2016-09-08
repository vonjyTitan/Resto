
<%
	if(!request.getRequestURI().equals("/Resto/main.jsp")){
		%><script>document.location.replace("/Resto/main.jsp?cible=stat");</script><%
		return;
	}
%>