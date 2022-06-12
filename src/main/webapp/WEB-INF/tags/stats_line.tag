<%@ attribute name="stats" required="true" type="org.mbatet.calories.model.stats.WeightStats" description="Les estadistiques"  %>
<%@ tag import="org.mbatet.calories.model.Constants"%>
<%@ tag import="java.lang.Math"%>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>


<th scope="row">${stats.title}</th>
<td>
    <c:if test="${stats.notEnoughData}">N/A (more data needed)</c:if>
    <c:if test="${!stats.notEnoughData}">
        <b>${Math.round(stats.recomendedCals)}</b>
        (with an avg of ${Math.round(stats.activityCals)}  consumed in activity)
        or <b>${Math.round(stats.adjustedCals)}</b> without
    </c:if>

</td>


