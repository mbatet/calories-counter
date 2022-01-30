<%@ attribute name="stats" required="true" type="org.mbatet.calories.model.stats.Stats" description="Les estadistiques"  %>

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
            <tags:stats_line stats="${stats.weightLossStats}"></tags:stats_line>
        </td>


    </tr>
    <tr>
        <th scope="row">Estimated maintenance calories</th>
        <td>
            <tags:stats_line stats="${stats.maintenanceStats}"></tags:stats_line>
        </td>
    </tr>
    <tr>
        <th scope="row">Estimated cals for weight gain</th>
        <td>
            <tags:stats_line stats="${stats.weightGainStats}"></tags:stats_line>
        </td>
    </tr>

    </tbody>
</table>