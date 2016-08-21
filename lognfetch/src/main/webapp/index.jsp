<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII" import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<h2>Hello World!</h2>
<% 	List<String> urls = new ArrayList<String>();
	urls.add("http://localhost:8080/lognfetch/jaxrslog/lognfetch/222");
	pageContext.setAttribute("urls", urls);
%>
dbUrl: <%= pageContext.getServletContext().getInitParameter("dbURL") %><br>
dbUser: ${ initParam["dbUser"] }
<br><br>
<strong>Urls to use in app:</strong>
<c:forEach var="i" begin="0" end="1" step="1">
	<c:out value="${urls[i]}"></c:out><br>
</c:forEach>
</body>
</html> 
