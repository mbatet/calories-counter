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

            <c:set var="trClass" value="${interval.type==Interval.TYPE_WEIGHT_LOSS_INTERVAL?'table-success': (interval.type==Interval.TYPE_WEIGHT_GAIN_INTERVAL?'table-danger':'table-warning')  }"/>
            <c:if test="${!loop.last}">
                <tr class="${trClass}">
                    <th scope="row">
                        <c:if test="${interval.dies.size()==7}">Week ${loop.index+1}</c:if>
                        <c:if test="${interval.dies.size()!=7}">${interval.dies.size()} days period</c:if>

                    </th>
                    <td>${interval.dies.size()}</td>
                    <td><fmt:formatDate value="${interval.firstDate}" pattern="EEE dd/MM/yy"/></td>
                    <td><fmt:formatDate value="${interval.lastDate}" pattern="EEE dd/MM/yy"/></td>
                    <td>${Math.round(interval.avgConsumedCals)} cals</td>
                    <td>${Math.round(interval.avgActivityCals)} cals</td>
                    <td>${Math.round(interval.avgAdjustedCals)} cals</td>
                    <%--
                    <td>From ${interval.firstWeight} to ${interval.lastWeight} Kg</td>
                    --%>
                    <td>From ${interval.firstAdjustedWeight} to ${interval.lastAdjustedWeight} Kg</td>
                    <td>${interval.weigthDiff} Kg</td>
                </tr>
            </c:if>
            <c:if test="${loop.last}">
                <tr class="${trClass}">
                    <th scope="row">Total</th>
                    <th scope="row">${interval.dies.size()}</th>
                    <td><fmt:formatDate value="${interval.firstDate}" pattern="EEE dd/MM/yy"/></td>
                    <td><fmt:formatDate value="${interval.lastDate}" pattern="EEE dd/MM/yy"/></td>
                    <td><b>${Math.round(interval.avgConsumedCals)} cals</b></td>
                    <td><b>${Math.round(interval.avgActivityCals)} cals</b></td>
                    <td><b>${Math.round(interval.avgAdjustedCals)} cals</b></td>
                    <td>From ${interval.firstWeight} to ${interval.lastWeight} Kg</td>
                    <td><b>${interval.weigthDiff} Kg</b></td>
                </tr>
            </c:if>



    </c:forEach>
    </tbody>
</table>