<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/spring-social/social/tags" prefix="social" %>

<h4><a href="<c:url value="/connect"/>">Connections</a></h4>


<h4><a href="<c:url value="/linkedin"/>">LinkedIn</a></h4>
<social:connected provider="linkedin">
<ul class="menu">
	<li><a href="<c:url value="/linkedin"/>">User Profile</a></li>
</ul>
<h4><a href="<c:url value="/jobsforme"/>">Jobs For Me</a></h4>
</social:connected>