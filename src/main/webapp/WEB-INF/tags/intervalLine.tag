<%@ attribute name="interval" required="true" type="org.mbatet.calories.model.Interval" description="Un interval"  %>
<%@ attribute name="index" required="false" type="java.lang.Integer" description="Index"  %>

<%@ tag import="org.mbatet.calories.model.Constants"%>
<%@ tag import="java.lang.Math"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag pageEncoding="UTF-8" %>


<c:set var="trClass" value="${interval.type==Constants.TYPE_WEIGHT_LOSS?'table-success': (interval.type==Constants.TYPE_WEIGHT_GAIN?'table-danger':'table-warning')  }"/>
<c:if test="${index!=null}">
    <tr class="${trClass}">
        <th scope="row">
            <c:if test="${interval.dies.size()==7}">Week ${index}</c:if>
            <c:if test="${interval.dies.size()==14}">Fortnight ${index}</c:if>
            <c:if test="${interval.dies.size()==30 || interval.dies.size()==31}">Month ${index}</c:if>
            <c:if test="${interval.dies.size()!=7 && interval.dies.size()!=14 && interval.dies.size()!=30 && interval.dies.size()!=30 }">${interval.dies.size()} days period</c:if>

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
<c:if test="${index==null}">
    <tr class="table-warning">
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

