<%@ attribute name="stats" required="true" type="org.mbatet.calories.model.stats.TrackingChart" description="Les estadistiques"  %>

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
            <tags:stats_line stats="${stats.weightLossStats}"></tags:stats_line>
    </tr>
    <tr>
            <tags:stats_line stats="${stats.weightGainStats}"></tags:stats_line>
    </tr>
    <tr>
        <th scope="row">Estimated/recommended cals for maintenance</th>
        <td><b>${Math.round(stats.maintenanceCals)}</b></td>

    </tr>
    <tr>
        <tags:lastWeek interval="${lastWeek}"></tags:lastWeek>
    </tr>

    </tbody>
</table>