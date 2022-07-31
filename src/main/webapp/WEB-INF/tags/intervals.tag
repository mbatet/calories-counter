<%@ attribute name="intervals" required="true" type="java.util.ArrayList<org.mbatet.calories.model.Interval>" description="El llistat de intervals"  %>
<%@ tag import="org.mbatet.calories.model.Constants"%>
<%@ tag import="org.mbatet.calories.model.Interval"%>
<%@ tag import="java.util.Date"%>
<%@ tag import="java.lang.Math"%>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag pageEncoding="UTF-8" %>

<!--

return "dies:" + dies.size() + ".first:"+getFirstDate() + ".last:"+getLastDate() + ".avgCalories:" + getAverageCalories() + ".weightDiff:" + getWeigthDiff();

-->





<table class="table" width="100%&">
    <thead>
    <tr>
        <th scope="col" width="8%"></th>
        <th scope="col" width="8%"># days</th>
        <th scope="col" width="12%">First</th>
        <th scope="col" width="12%">Last</th>
        <th scope="col" width="10%">Avg consumed cals</th>
        <th scope="col" width="10%">Avg activity cals</th>
        <th scope="col" width="10%">Consumed - expended</th>
        <th scope="col" width="15%">Adjusted weight (*)</th>
        <th scope="col" width="15%">Adjusted weight variation (*)</th>
    </tr>
    </thead>
    <tbody>

    <c:forEach items="${intervals}" var="interval"  varStatus="loop">
            <tags:intervalLine interval="${interval}" index="${loop.index+1}"></tags:intervalLine>
    </c:forEach>
    <tags:intervalLine interval="${intervalGeneral}"></tags:intervalLine>
    </tbody>
</table>