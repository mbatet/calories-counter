<%@ attribute name="date" required="true" type="java.util.Date" description="Date"  %>
<%@ attribute name="weight" required="true" type="java.lang.Float" description="Weight"  %>
<%@ attribute name="last" required="true" type="java.lang.Boolean" description="Is last item"  %>
<%@ tag import="org.mbatet.calories.service.Utils"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>


{x: new Date(${date.year+1900}, ${date.month}, ${date.date}), y: ${Utils.round(weight)}}
<c:if test="${!last}">,</c:if>

