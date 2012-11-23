<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>

<h3>We have found following Jobs for you..</h3>

<c:forEach items="${jobs}" var="vo">
<dl style="border: 1px solid blue;">
	<dt>Job Title:</dt>
	<dd><c:out value="${jobTitle}"/></dd>
	<dt>Company:</dt>
	<dd><c:out value="${companyName}"/></dd>
	<dt>Skills Required:</dt>
	<c:forEach items="${skills}" var="skill">
		<dd><c:out value="${skill}"/></dd>
	</c:forEach>
	
	<dt>Job Location:</dt>
	<dd><c:out value="${address}"/></dd>
	
	<dt>Distance:</dt>
	<dd><c:out value="${distance}"/></dd>
	
	<dt>Time to reach by road:</dt>
	<dd><c:out value="${duration}"/></dd>
	
</dl>

</c:forEach>

<c:url value="/connect/linkedin" var="disconnectUrl"/>
<form id="disconnect" action="${disconnectUrl}" method="post">
	<button class="btn btn-warning" type="submit">Disconnect from LinkedIn</button>	
	<input type="hidden" name="_method" value="delete" />
</form>
