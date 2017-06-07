<%@ page import="rmi.RemoteHelper" %>
<%@ page import="java.rmi.Naming" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>JSP简单登录实例</title>
</head>

<body>

<%
    RemoteHelper rmic;
    boolean connected = true;
    try {
        rmic = RemoteHelper.getInstance();
        rmic.setRemote(Naming.lookup("rmi://localhost:8888/DateRemote"));

    } catch (Exception e) {
        connected = false;
    }
    if (!connected){
//        response.sendRedirect("views/netError.jsp");
    }


%>


<h2>请登录</h2>

<form action="/login" method="post">
    Login 名字: <input type="text" name="Name"><br>
    Login Password: <input type="password" name="Password"><br>
    <input type="submit" value="Send"><br>
    </form>

</body>
</html>