<%@ attribute name="stats" required="true" type="org.mbatet.calories.model.Stats" description="Les estadistiques"  %>
<%@ tag import="org.mbatet.calories.model.Constants"%>
<%@ tag import="org.mbatet.calories.model.Stats"%>
<%@ tag import="java.lang.Math"%>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>

<!--

return "dies:" + dies.size() + ".first:"+getFirstDate() + ".last:"+getLastDate() + ".avgCalories:" + getAverageCalories() + ".weightDiff:" + getWeigthDiff();

-->

<table class="table">
    <thead>
    <tr>
        <th scope="col" colspan="2"></th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <th scope="row"><b>Estimated cals for weight loss</b></th>
        <td>
            <c:if test="${stats.weightLossAvgCals!=null}">
                <b>${Math.round(stats.weightLossAvgCals)}  (with an avrg of ${Math.round(stats.weightLossActivityAvgCals)}  consumed in activity) or ${Math.round(stats.weightLossAvgAdjustedCals)} without</b>
            </c:if>
            <c:if test="${stats.weightLossAvgCals==null}">N/A</c:if>
        </td>


    </tr>
    <tr>
        <th scope="row">Estimated maintenance calories</th>
        <td>
            <c:if test="${stats.maintenanceAvgCals!=null}">
                ${Math.round(stats.maintenanceAvgCals)}  (with an avrg of ${Math.round(stats.maintenanceActivityAvgCals)}  consumed in activity) or ${Math.round(stats.maintenanceAvgAdjustedCals)} without</b>
            </c:if>
            <c:if test="${stats.maintenanceAvgCals==null}">N/A</c:if>
        </td>
    </tr>
    <tr>
        <th scope="row">Estimated cals for weight gain</th>
        <td>
            <c:if test="${stats.weightGainAvgCals!=null}">
                ${Math.round(stats.weightGainAvgCals)} (with an avrg of ${Math.round(stats.weightGainActivityAvgCals)}  consumed in activity) or ${Math.round(stats.weightGainAvgAdjustedCals)} without
            </c:if>
            <c:if test="${stats.weightGainAvgCals==null}">N/A</c:if>
        </td>
    </tr>

    </tbody>
</table>