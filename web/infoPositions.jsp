<%--
  Created by IntelliJ IDEA.
  User: Алексей
  Date: 27.02.2020
  Time: 17:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf8" language="java" %>
<html>
<head>
    <title>Positions</title>
</head>
<body>
<section>
    <h3>Position info</h3>
    <jsp:useBean id="position" scope="request" class="model.Position"/>
    <tr>

        <td>ID: ${position.id} | Position: ${position.position} |</td>
        <td><a href="time?action=update&id=${position.id}">Update</a></td>
        <td><a href="time?action=delete&id=${position.id}">Delete</a></td>
    </tr>

</section>
<section>
    <td><a href="time">Главная</a></td>
</section>
</body>
</html>