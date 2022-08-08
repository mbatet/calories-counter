<%@ attribute name="maxWeight" required="false" type="java.lang.Float" description="maxWeight"  %>
<%@ attribute name="minWeight" required="false" type="java.lang.Float" description="minWeight"  %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>

<!--

return "dies:" + dies.size() + ".first:"+getFirstDate() + ".last:"+getLastDate() + ".avgCalories:" + getAverageCalories() + ".weightDiff:" + getWeigthDiff();

-->




<table class="table" border="0">


    <tr class="table-warning">
        <th scope="row">Max weight</th>
        <td>${maxWeight}</td>
    </tr>
    <tr class="table-warning">
        <th scope="row">Min weight</th>
        <td>${minWeight}</td>
    </tr>

    </tbody>
</table>