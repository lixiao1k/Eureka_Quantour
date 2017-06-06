<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*"%>

<html>
<body>
<h2>欢迎页面(测试session)</h2>

<%

    String  Login = (String)session.getAttribute("Login");
    int     nCount=0;


    if (Login != null && Login.equals("OK")) {
        Integer myCount = (Integer)session.getAttribute("myCount");
        if(myCount!=null)
        {
            nCount = myCount.intValue();
            nCount = nCount + 1;
            session.setAttribute("myCount",new Integer(nCount));
        }
%>
登录成功,myCount=<%=nCount%></br>
<input type=button value="退出" onclick="javascript:location.href='logout.jsp'">
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