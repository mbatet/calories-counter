<%@ attribute name="stats" required="true" type="org.mbatet.calories.model.stats.GeneralStats" description="Les estadistiques"  %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>

<!--

return "dies:" + dies.size() + ".first:"+getFirstDate() + ".last:"+getLastDate() + ".avgCalories:" + getAverageCalories() + ".weightDiff:" + getWeigthDiff();

-->




<table class="table" border="0">
    <thead>
    <tr>
        <th scope="col" colspan="2"></th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <tags:stats_line stats="${stats.weightLossStats}"></tags:stats_line>
    </tr>

    <tr>
        <c:if test="${!stats.weightMaintenanceStats.notEnoughData}">
            <tags:stats_line stats="${stats.weightMaintenanceStats}"></tags:stats_line>
        </c:if>
        <c:if test="${stats.weightMaintenanceStats.notEnoughData}">
            <th scope="row">Estimated/recommended cals for maintenance</th>
            <td><b>${Math.round(stats.maintenanceCals)}</b></td>
        </c:if>
    </tr>
    <tr>
        <tags:stats_line stats="${stats.weightGainStats}"></tags:stats_line>
    </tr>

    <tr class="table-warning">
        <th scope="row">Daily calorie intake to optimize calorie deficit</th>
        <td><b>${Math.round(stats.caloriesBelowMaintenance)}</b></td>
    </tr>

    <tr class="table-warning">
        <tags:lastWeek interval="${lastWeek}"></tags:lastWeek>
    </tr>

    </tbody>
</table>