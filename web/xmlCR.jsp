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
<a href="xmlCR?action=submitSaveXML">Save ON xmL</a>
</section>

<section>
Download DB with xml
</section>
<form enctype="multipart/form-data" method="post" >
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