<%--
  Created by IntelliJ IDEA.
  User: Acer - PC
  Date: 27.03.2020
  Time: 16:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>updateDegree</title>
</head>
<body>
<section>
    <jsp:useBean id="degree" scope="request" class="model.Degree"/>
    <form method="post" action="degreeD?action=submit&id=${degree.id}">
        <dl>
            <dt>Degree: </dt>
            <dd><input type="text" name="degree" value="${degree}" placeholder="${degree}" /></dd>
        </dl>
        <button type="submit">Save</button>
    </form>
</section>
</body>
</html>
