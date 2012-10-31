<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>

<h3>We have found following Jobs for you..</h3>

<p>Hello, <c:out value="${profile.firstName}"/>!</p>
<img src="<c:out value="${profile.profilePictureUrl}"/>"/>
<<c:forEach items="${jobs}" var="vo"></c:forEach>
<dl>
	<dt>Job Id:</dt>
	<dd>c:out value="${vo.job.id}"/></dd>
	<dt>Job Title:</dt>
	<dd><c:out value="${vo.job.jobTitle}"/></dd>
	<dt>Skills Required:</dt>
	<dd><c:out value="${vo.job.skills}"/></dd>
	<dt>Job Location:</dt>
	<dd><c:out value="${vo.job.formattedAddress}"/></dd>
	
	<dt>Distance:</dt>
	<dd><c:out value="${vo.distance.text}"/></dd>
	
	<dt>Time to reach by road:</dt>
	<dd><c:out value="${vo.duration.text}"/></dd>
	
</dl>

<c:url value="/connect/linkedin" var="disconnectUrl"/>
<form id="disconnect" action="${disconnectUrl}" method="post">
	<button type="submit">Disconnect from LinkedIn</button>	
	<input type="hidden" name="_method" value="delete" />
</form>
