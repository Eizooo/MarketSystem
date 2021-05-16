<%--
  Created by IntelliJ IDEA.
  User: Eizooo
  Date: 2021/5/13
  Time: 17:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <script src="${pageContext.request.contextPath}/statics/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript">
        var result = ${weather};
        alert(result);
        alert(result.date);
        $(document).ready(function() {
            var s = "";

            s = "<tr><td>" + result.date + "</td><td>" + result.templow + "</td><td>" +
                result.temp + "</td><td>" + result.week + "</td><td>" + result.weather + "</td></tr>";
            $("#tab").append(s);

        });
    </script>
</head>

<body>
<table border="1px" id="tab">
    <tr>
        <td>时间</td>
        <td>湿度</td>
        <td>温度</td>
        <td>日期</td>
        <td>天气</td>
    </tr>
</table>
</body>

</html>
