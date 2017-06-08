<%--
  Created by IntelliJ IDEA.
  User: huihantao
  Date: 2017/6/7
  Time: 15:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>单个股票查询</title>
</head>
<body>
<input type=button value="返回" onclick="location.href='welcome.jsp'">
<br>
<form action="/singleStock" method="post">
    First name: <input type="text" name="stockcode" />
    <input type="submit" value="查询" />
</form>
<br>



</body>
</html>
