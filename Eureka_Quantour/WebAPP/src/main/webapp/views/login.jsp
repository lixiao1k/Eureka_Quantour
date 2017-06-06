<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
    <title>JSP简单登录实例</title>
</head>

<body>
<h2>请登录</h2>

<form action="${pageContext.request.contextPath}/servlet/a" method="post">
    Login 名字: <input type="text" name="Name"><br>
    Login Password: <input type="password" name="Password" ><br>
    <input type="submit" value="Send"><br>
    <form>

            <%
      if (request.getParameter("Name") != null
              && request.getParameter("Password") != null) {
          String Name = request.getParameter("Name");
          String Password = request.getParameter("Password");
          if (Name.equals("a") && Password.equals("a")) {
              session.setAttribute("Login", "OK");
              session.setAttribute("myCount", new Integer(1));
              response.sendRedirect("welcome.jsp");
          }
          else {
              %>
        登录失败:用户名或密码不正确～
            <%
          }
      }
%>
</body>
</html>