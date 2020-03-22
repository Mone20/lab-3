<%--
  Created by IntelliJ IDEA.
  User: Алексей
  Date: 05.03.2020
  Time: 0:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Position</title>
</head>
<body>

<table>
    <tr>
        <td>id</td>
        <td>Должность</td>
    </tr>
    <%=request.getAttribute("select")%>
</table>
<section>
    <a href="/lab_3_war_exploded">Начальная страница</a>
    <a href="position?action=createPosition">Создать</a>
</section>


</body>
</html>