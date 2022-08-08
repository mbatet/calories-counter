<%@ attribute name="maxWeight" required="false" type="java.lang.Float" description="maxWeight"  %>
<%@ attribute name="minWeight" required="false" type="java.lang.Float" description="minWeight"  %>
<%@ attribute name="currentWeight" required="false" type="java.lang.Float" description="currentWeight"  %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>

<!--

return "dies:" + dies.size() + ".first:"+getFirstDate() + ".last:"+getLastDate() + ".avgCalories:" + getAverageCalories() + ".weightDiff:" + getWeigthDiff();

-->




<table class="table">
    <tbody>

    <tr class="table-danger">
        <th scope="row">Max weight</th>
        <td>${maxWeight}</td>
    </tr>
    <tr class="table-warning">
        <th scope="row">Current weight</th>
        <td>${currentWeight}</td>
    </tr>
    <tr class="table-success">
        <th scope="row">Min weight</th>
        <td>${minWeight}</td>
    </tr>

    </tbody>
</table>