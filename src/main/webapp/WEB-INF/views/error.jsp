<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="ca">
    <%@include file="shared/head.jsp" %>
    <body>
        <div class="container">
            <h1>Ooops.... something has gone wrong</h1>
            <hr/>
            Missatge: ${missatge}
            <hr/>
            <%@include file="shared/inici_link.jsp" %>
        </div>


    </body>
</html>
