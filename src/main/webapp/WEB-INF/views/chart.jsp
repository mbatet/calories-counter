<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="org.mbatet.calories.model.Constants"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ca">
    <%@include file="shared/head.jsp" %>
    <body>
        <div class="container">

            <h1>${title}</h1>

          <tags:message/>

            <c:if test="${hasErrors}">
                <%--
                <hr/>
                    <%@include file="shared/format.jsp" %>
                <hr/>
                  <%@include file="updates/errors.jsp" %>
                --%>

            </c:if>
            <c:if test="${!hasErrors}">
                <c:if test="${type==Constants.WHEIGHT_TRACKING_CHART_TWO_SERIES_RAW_AND_ADJUSTED}">
                    <tags:multiSeriesChart titleX="${title}" titleY="Pes" dies="${dies}" type="${type}"></tags:multiSeriesChart>
                </c:if>
                <c:if test="${type==Constants.WHEIGHT_TRACKING_CHART_ADJUSTED_WEIGHTS || type==Constants.WHEIGHT_TRACKING_CHART_RAW_WEIGHTS}">
                    <tags:splinechart titleX="${title}" titleY="Pes" dies="${dies}" type="${type}"></tags:splinechart>
                </c:if>

                <tags:intervals intervals="${intervals}"></tags:intervals>
                <tags:stats stats="${stats}"></tags:stats>

            </c:if>
            <%@include file="shared/inici_link.jsp" %>


        </div>

    </body>
</html>

