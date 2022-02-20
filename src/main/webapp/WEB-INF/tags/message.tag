<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${!empty message}">
    <div class="alert alert-primary" role="alert">
        ${message}
    </div>
</c:if>