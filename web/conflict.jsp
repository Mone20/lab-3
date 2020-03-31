<%--
  Created by IntelliJ IDEA.
  User: Rodion
  Date: 26.03.2020
  Time: 21:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Conflicts</title>
    <style>
        p {
            margin-top: 0.5em; /* Отступ сверху */
            margin-bottom: 1em; /* Отступ снизу */
        }
    </style>
</head>
<body>
<h1>Подтвердите изменения:</h1>

<form method="post" action="xmlCR?action=submitConflict">
    <p><input type="checkbox" onclick="checkAll(this)"  /><label>Выбрать все</label></p>
<%=request.getAttribute("htmlConflict")%>
<section>

        <button type="submit">Подтвердить</button>

</section>
</form>

</body>
<script type="text/javascript">
    function checkAll(obj) {
        'use strict';
        var items = obj.form.getElementsByTagName("input"),
            len, i;
        for (i = 0, len = items.length; i < len; i += 1) {
            if (items.item(i).type && items.item(i).type === "checkbox") {
                if (obj.checked) {
                    items.item(i).checked = true;
                } else {
                    items.item(i).checked = false;
                }
            }
        }
    }
</script>
</html>
