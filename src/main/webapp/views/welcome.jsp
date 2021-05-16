<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> <!--输出,条件,迭代标签库-->

<html>
<head>
    <title>Eizooo魔法世界</title>
</head>
<body>
    <h2>Hello 欢迎来到SpringWorld!</h2>
    <h3>欢迎${username}用户的到来</h3>
    <h4>KEY(String)---->>${string}</h4>
    <table border="1px yellow solid">
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.userName}</td>
                <td>${user.userCode}</td>
                <td>${user.phone}</td>
                <td>${user.address}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
