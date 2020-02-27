<%--
  Created by IntelliJ IDEA.
  User: Rodion
  Date: 20.02.2020
  Time: 12:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=utf8" language="java" %>
<html>
<head>
    <title>Update</title>
</head>
<body>
<section>
    <jsp:useBean id="worker" scope="request" class="model.Worker"/>
    <form method="post" action="time?action=submit&id=${worker.id}">
        <dl>
            <dt>LastName: </dt>
            <dd><input type="text" name="lastName" value="${worker.lastName}" placeholder="${worker.lastName}" /></dd>
        </dl>
        <dl>
            <dt>FirstName: </dt>
            <dd><input type="text" name="firstName" value="${worker.firstName}" placeholder="${worker.firstName}" /></dd>
        </dl>
        <dl>
            <dt>MiddleName: </dt>
            <dd><input type="text" name="middleName" value="${worker.middleName}" placeholder="${worker.middleName}" /></dd>
        </dl>
        <dl>
            <dt>BirthDate: </dt>
            <dd><input type="text" name="birthDate" value="${worker.birthDate}" placeholder="${worker.birthDate}" /></dd>
        </dl>
        <dl>
            <dt>Degree: </dt>

                <p><select name="selectDeg" size="1">
                    <%=request.getAttribute("htmlDeg")%>
                </select></p>


        </dl>
        <dl>
            <dt>Position: </dt>

                <p><select name="selectPos" size="1">
                    <%=request.getAttribute("htmlPos")%>
                </select></p>


        </dl>

        <dl>
            <dt>Boss(id): </dt>

            <p><select name="selectParent" size="1">
                <option selected value="-2">Empty(no boss)</option>
                <%=request.getAttribute("htmlParent")%>
            </select></p>


        </dl>
        <button type="submit">Save</button>
    </form>
</section>
</body>
</html>