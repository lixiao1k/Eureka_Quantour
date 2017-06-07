<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>

<html>
<body>
<h2>欢迎页面(测试session)</h2>

<%

    String  username = (String)config.getServletContext().getAttribute("name");
    System.out.println(username);

    if (username!= null) {
        {
            out.println(username);
        }
%>
<input type=button value="退出" onclick="javascript:location.href='/logout'"
actio>
<%
}
else {
%>
<jsp:forward page="login.jsp"/>
<%
    }
%>

</body>
</html>