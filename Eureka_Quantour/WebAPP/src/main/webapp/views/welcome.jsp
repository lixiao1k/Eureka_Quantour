<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*" %>

<html>
<head>
    <script type='text/javascript' src='/scripts/calendar.js'></script>
</head>
<body>
<h2>欢迎页面(测试session)</h2>
<%
    String username = (String) config.getServletContext().getAttribute("name");

    if (username != null) {
        {
            out.println(username);
        }
%>
<input type=button value="退出" onclick="location.href='/logout'">


<div class='calendar' id='canlendar'></div>




<%
} else {
%>
<jsp:forward page="login.jsp"/>
<%
    }
%>



<input type ="button"  value="单个股票查询" onclick="location.href='/views/stock.jsp'">




</body>
</html>