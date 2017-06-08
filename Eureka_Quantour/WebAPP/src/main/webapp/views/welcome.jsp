<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*" %>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>jQuery UI Datepicker - Default functionality</title>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script>
        $( function() {
            $( "#datepicker" ).datepicker();
        } );
    </script>
    <style>
        div{
            float: left;
        }
    </style>



</head>

<body>
<%--check log in--%>
<%
    String username = (String) config.getServletContext().getAttribute("name");

    if (username != null) {
        {
            out.println("<div>");
            out.println("<p1>");
            out.println(username);
            out.println("</p1>");
            out.println("</div>");
        }


} else {
        response.sendRedirect("/views/login.jsp");
    }
%>


<p2>个人主页</p2>


<div>
<input type=button value="退出" onclick="location.href='/logout'">
</div>
<div>
<p>Date: <input type="text" id="datepicker"></p>
</div>
<br>

<input type ="button"  value="单个股票查询" onclick="location.href='/views/stock.jsp'">
<br>




</body>
</html>