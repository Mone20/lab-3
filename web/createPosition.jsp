<%--
  Created by IntelliJ IDEA.
  User: Алексей
  Date: 27.02.2020
  Time: 17:28
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=utf8" language="java" %>
<html>
<head>
    <title>CreatePosition</title>
</head>
<body>
<section>
    <form method="post" action="position?action=createPosition">
        <dl>
            <dt>Position: </dt>
            <dd><input type="text" name="position" /></dd>
        </dl>

        <button type="submit">Create</button>
    </form>
</section>
</body>
</html>