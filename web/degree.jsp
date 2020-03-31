<%--
  Created by IntelliJ IDEA.
  User: Acer - PC
  Date: 02.03.2020
  Time: 17:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Degree</title>
</head>
<body>

<table>
    <tr>
        <td>id</td>
        <td>Научная степень</td>
    </tr>
    <%=request.getAttribute("select")%>
</table>
<section>
    <a href="/lab_3_war_exploded">Начальная страница</a>
<%--    <a href="/lab_3_war_exploded">Начальная страница</a>--%>
    <a href="degree?action=createDegree">Создать</a>
</section>


</body>
</html>
