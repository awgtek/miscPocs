<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Maven + Spring MVC + @JavaConfig</title>

<spring:url value="/resources/css/core.css" var="coreCss" />
<spring:url
	value="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	var="bootstrapCdnCss" />
<link href="${bootstrapCdnCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />
</head>

<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">Item States</a>
		</div>
	</div>
</nav>

<div class="jumbotron">
	<div class="container">
		<h1>${title}</h1>
		<p>
			<c:if test="${not empty name}">
			Hello ${name}
		</c:if>

			<c:if test="${empty name}">
			Welcome Welcome!
		</c:if>
		</p>
		<p>
			<a class="btn btn-primary btn-lg" href="#" role="button">Learn
				more</a>
		</p>
	</div>
</div>

<div class="container">
	<h1>New/Edit Contact</h1>
	<form:form action="saveItemState.do" method="post"
		modelAttribute="itemstate">
		<div class="row">
			<div class="col-md-6">
				<p>Name:</p>
				<p>
					<form:input path="name" />
				</p>
			</div>
			<div class="col-md-6">
				<p>Description</p>
				<p>
					<form:input path="description" />
				</p>
			</div>
			<div class="col-md-6">
				<p>
					<input type="submit" value="Save">
				</p>
			</div>
		</div>
	</form:form>
	<h2>All States in System</h2>

	<table border="1">
		<tr>
			<th>No</th>
			<th>Name</th>
			<th>Description</th>
			<th>Action</th>
		</tr>
		<c:forEach items="${itemstates}" var="itemstate" varStatus="status">
			<tr>
				<td>${status.index + 1}</td>
				<td>${itemstate.name}</td>
				<td>${itemstate.description}</td>
				<td><a href="/editItemState.do?name=${itemstate.name}">Edit</a>
					&nbsp;&nbsp;&nbsp;&nbsp; <a
					href="/deleteItemState.do?name=${itemstate.name}">Delete</a></td>
			</tr>
		</c:forEach>
	</table>

	<hr>
	<footer>
		<p>© awgtek.com 2015</p>
	</footer>
</div>

<spring:url value="/resources/core/css/hello.js" var="coreJs" />
<spring:url value="/resources/core/css/bootstrap.min.js"
	var="bootstrapJs" />

<script src="${coreJs}"></script>
<script src="${bootstrapJs}"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>

</body>
</html>