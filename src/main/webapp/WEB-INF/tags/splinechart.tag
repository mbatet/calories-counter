<%@ attribute name="titleX" required="true" type="java.lang.String" description="Titol a mostrar al grafic al eix de les X"  %>
<%@ attribute name="titleY" required="true" type="java.lang.String" description="Titol a mostrar al grafic al eix de les Y"  %>
<%@ attribute name="dies" required="true" type="java.util.ArrayList<org.mbatet.calories.model.Dia>" description="El llistat de dies amb les dates"  %>
<%@ attribute name="type" required="true" type="java.lang.String" description="Tipus de grafic (ponderat / noponderat)"  %>
<%@ tag import="org.mbatet.calories.model.Constants"%>
<%@ tag import="org.mbatet.calories.service.Utils"%>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>

<!--
https://canvasjs.com/docs/charts/integration/jquery/chart-types/jquery-spline-chart/

const d = new Date("2015-03-25");

-->


<script type="text/javascript">
    window.onload = function() {
        $("#chartContainer").CanvasJSChart({
            title: {
                text: "${titleX}",
                fontSize: 22
            },
            axisY: {
                title: "${titleY}",
                includeZero: false,
                Prefix: "Kg"
            },
            axisX: {
                interval: 2,
                intervalType: "week",
                valueFormatString: "DD-MMM",
                labelAngle: -45
            },
            data: [
                {
                    type: "spline",
                    toolTipContent: "{x}: {y} Kg",
                    dataPoints: [
                        <c:forEach items="${dies}" var="dia"  varStatus="loop">
                            <c:if test="${type==Constants.WHEIGHT_TRACKING_CHART}">
                                {x: new Date(${dia.date.year+1900}, ${dia.date.month}, ${dia.date.date}), y: ${Utils.round(dia.pes)}}
                            </c:if>
                            <c:if test="${type==Constants.WHEIGHT_TRACKING_CHART_PONDERAT}">
                                {x: new Date(${dia.date.year+1900}, ${dia.date.month}, ${dia.date.date}), y: ${Utils.round(dia.adjustedWeight)}}
                            </c:if>
                            <c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    ]
                }
            ]
        });
    }
</script>


<div id="chartContainer" style="width: 112%; height: 312px"></div>