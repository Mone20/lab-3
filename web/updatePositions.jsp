<%--
  Created by IntelliJ IDEA.
  User: Алексей
  Date: 27.02.2020
  Time: 17:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf8" language="java" %>
<html>
<head>
    <title>Update</title>
</head>
<body>
<section>
    <jsp:useBean id="position" scope="request" class="model.UniversityPosition"/>
    <form method="post" action="time?action=submit&id=${position.id}">
        <dl>
            <dt>Position:</dt>
            <dd><input type="text" name="position" value="${position.position}" placeholder="${position.position}"/>
            </dd>
        </dl>


        <button type="submit">Save</button>
    </form>
</section>
</body>
</html>