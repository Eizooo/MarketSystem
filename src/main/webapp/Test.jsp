<%--
  Created by IntelliJ IDEA.
  User: Eizooo
  Date: 2021/4/21
  Time: 18:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="${pageContext.request.contextPath }/weather" method="post">
        <div>
            city:<input name="city" id="city" type="text" value="" >
            <input type="submit" value="发送">
        </div>
    </form>
</body>
</html>
