<%--
  Created by IntelliJ IDEA.
  User: Rodion
  Date: 20.02.2020
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf8" language="java" %>
<html>
<head>
    <title>Workers</title>
</head>
<body>
<section>
    <h3>Worker info</h3>
    <jsp:useBean id="worker" scope="request" class="model.Worker"/>
    <tr>

        <td>ID: ${worker.id} | LastName: ${worker.lastName} | FirstName: ${worker.firstName}| MiddleName: ${worker.middleName}|BirthDate: ${worker.birthDate}|UniversityPosition: <%=request.getAttribute("position")%>|Degree: <%=request.getAttribute("degree")%>|</td>
        <td><a href="time?action=update&id=${worker.id}">Update</a></td>
        <td><a href="time?action=delete&id=${worker.id}">Delete</a></td>
        <td><a href="time?action=info&id=${worker.parentId}">Boss</a></td>
    </tr>

</section>
<section>
    <td><a href="time">Главная</a></td>
</section>
</body>
</html>