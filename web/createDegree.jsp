<%--
  Created by IntelliJ IDEA.
  User: Acer - PC
  Date: 04.03.2020
  Time: 14:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>CreateDegree</title>
</head>
<body>
<section>
    <form method="post" action="degree?action=createDegree">
        <dl>
            <dt>Degree: </dt>
            <dd><input type="text" name="degree" /></dd>
        </dl>
        <button type="submit">Create</button>
    </form>
</section>
</body>
</html>
