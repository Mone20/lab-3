<%--
  Created by IntelliJ IDEA.
  User: Rodion
  Date: 25.02.2020
  Time: 20:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>
<h1>Write/Read xml</h1>
<section>
    <form method="post" action="xmlCR?action=exportXML">
        Научная степень
        <p><select name="degreeId_filter" size="1">
            <option selected value="empty">Не выбрано</option>
            <%=request.getAttribute("htmlDeg")%>
        </select></p>
        Должность
        <p><select name="positionId_filter" size="1">
            <option selected value="empty">Не выбрано</option>
            <%=request.getAttribute("htmlPos")%>
        </select></p>
        <input type="submit" value="Показать"></p>
    </form>
</section>

<section>
Download DB with xml
</section>
<form enctype="multipart/form-data" method="post" action="xmlCR?action=importXML" >
    <p><input type="file" name="file">
        <section>
        <input type="submit" value="Отправить">
        </section>
    </p>
</form>
<section>
    <a href="/lab_3_war_exploded">Начальная страница</a>
</section>
</body>
</html>