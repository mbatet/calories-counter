<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="org.mbatet.calories.model.Constants"%>

<!DOCTYPE html>

<html lang="ca">

    <%@include file="shared/head.jsp" %>
    <body>

        <div class="container">

            <h1>Pujar dades o copiar un text en format CSV</h1>
            ${missatge}<br/>


            <%@include file="shared/format.jsp" %>
            <br/><br/>


            <form:form method="POST" action="parse" modelAttribute="form" enctype="multipart/form-data">

                <!-- PUJDAR DESDE FITXER CVS -->

                <!--
                <div class="col-xs-4"></div>
                -->
                <!-- TODO: Aqui fer un loop sobre el hashmap-->
                <form:select path="type" name="type" id="type" >
                    <form:option  value="${Constants.WHEIGHT_TRACKING_CHART_RAW_WEIGHTS}">${Constants.CHART_TITTLE.get(Constants.WHEIGHT_TRACKING_CHART_RAW_WEIGHTS)}</form:option>
                    <form:option  value="${Constants.WHEIGHT_TRACKING_CHART_ADJUSTED_WEIGHTS}">${Constants.CHART_TITTLE.get(Constants.WHEIGHT_TRACKING_CHART_ADJUSTED_WEIGHTS)}</form:option>
                    <form:option  value="${Constants.WHEIGHT_TRACKING_CHART_TWO_SERIES_RAW_AND_ADJUSTED}">${Constants.CHART_TITTLE.get(Constants.WHEIGHT_TRACKING_CHART_TWO_SERIES_RAW_AND_ADJUSTED)}</form:option>

                </form:select>
                <br/><br/>


                <input type = "file" name = "file" name="file"/>
                <input type="submit" value="Veure gràfic"/><br/>

                <br/>

                <!-- PUJDAR FENT COPY&PASTE A UN CAMP TEXTAREA -->
                O alternativament, potser fer directament un copy&paste d'un text CSV aquí...<br/>
                <form:textarea path="textArea" cols="100" rows="20" id="textArea"/>
                <br/> <input type="submit" value="Veure gràfic"/>
                <br/>

            </form:form>

        </div>

    </body>
</html>
