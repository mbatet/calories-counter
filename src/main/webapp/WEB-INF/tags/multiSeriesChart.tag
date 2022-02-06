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
https://canvasjs.com/javascript-charts/multi-series-chart/



-->


<script type="text/javascript">

    window.onload = function () {

        var chart = new CanvasJS.Chart("chartContainer", {
            animationEnabled: true,
            title:{
                text: "${titleX}"
            },
            axisX: {
                valueFormatString: "DD MMM,YYYY"
            },
            axisY: {
                title: "${titleY}",
                suffix: " Kg"
            },
            legend:{
                cursor: "pointer",
                fontSize: 16,
                itemclick: toggleDataSeries
            },
            toolTip:{
                shared: true
            },
            data: [{
                name: "Raw weight",
                type: "spline",
                //yValueFormatString: "#0.## °C",
                yValueFormatString: "#0.# Kg",
                showInLegend: true,
                dataPoints: [
                    <c:forEach items="${dies}" var="dia"  varStatus="loop">
                        {x: new Date(${dia.date.year+1900}, ${dia.date.month}, ${dia.date.date}), y: ${Utils.round(dia.weight)}}
                        <c:if test="${!loop.last}">,</c:if>
                    </c:forEach>
                ]
            },
                {
                    name: "Adjusted weight",
                    type: "spline",
                    yValueFormatString: "#0.# Kg",
                    showInLegend: true,
                    dataPoints: [
                        <c:forEach items="${dies}" var="dia"  varStatus="loop">
                            {x: new Date(${dia.date.year+1900}, ${dia.date.month}, ${dia.date.date}), y: ${Utils.round(dia.adjustedWeight)}}
                            <c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    ]
                }
                /*,
                {
                    name: "Nantucket",
                    type: "spline",
                    yValueFormatString: "#0.## °C",
                    showInLegend: true,
                    dataPoints: [
                        { x: new Date(2017,6,24), y: 22 },
                        { x: new Date(2017,6,25), y: 19 },
                        { x: new Date(2017,6,26), y: 23 },
                        { x: new Date(2017,6,27), y: 24 },
                        { x: new Date(2017,6,28), y: 24 },
                        { x: new Date(2017,6,29), y: 23 },
                        { x: new Date(2017,6,30), y: 23 }
                    ]
                }*/
                ]
        });
        chart.render();

        function toggleDataSeries(e){
            if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
                e.dataSeries.visible = false;
            }
            else{
                e.dataSeries.visible = true;
            }
            chart.render();
        }

    }

</script>


<div id="chartContainer" style="width: 100%; height: 312px"></div>