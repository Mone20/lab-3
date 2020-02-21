<%--
  Created by IntelliJ IDEA.
  User: Rodion
  Date: 11.02.2020
  Time: 23:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello</title>
</head>
<body>
<form method="post" action="time?action=show">
    Научная степень
    <p><select name="selectDeg" size="1">
        <option selected value="empty">Не выбрано</option>
        <%=request.getAttribute("htmlDeg")%>
    </select></p>
    Должность
    <p><select name="selectPos" size="1">
        <option selected value="empty">Не выбрано</option>
        <%=request.getAttribute("htmlPos")%>
    </select></p>
        <input type="submit" value="Показать"></p>
</form>
<table>
    <tr>
        <td>id</td>
        <td>Фамилия</td>
        <td>Имя</td>
        <td>Отчество</td>

    </tr>
        <%=request.getAttribute("select")%>
    </table>
<section>
    <a href="index.jsp">Начальная страница</a>
    <a href="time?action=create">Создать</a>
</section>
</body>
</html>
