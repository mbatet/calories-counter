<%@ attribute name="interval" required="true" type="org.mbatet.calories.model.Interval" description="Un interval"  %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>

<c:if test="${lastWeek.dies.size()>0}">

    <th scope="row">Last ${lastWeek.dies.size()} days since monday...</th>
    <td>

        <b>${Math.round(lastWeek.avgConsumedCals)}</b> cals (with an avg of ${Math.round(lastWeek.avgActivityCals)} activity cals)  or <b>${Math.round(lastWeek.avgAdjustedCals)}</b> without
        <%--  ${Math.round(stats.recomendedCals)} --%>
    </td>
</c:if>