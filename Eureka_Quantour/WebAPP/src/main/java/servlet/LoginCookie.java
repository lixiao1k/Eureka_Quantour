package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Created by huihantao on 2017/6/6.
 */
public class LoginCookie extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
                //设置服务器端以UTF-8编码进行输出
                 response.setCharacterEncoding("UTF-8");
                 //设置浏览器以UTF-8编码进行接收,解决中文乱码问题
                 response.setContentType("text/html;charset=UTF-8");

                 PrintWriter out = response.getWriter();
                 //获取浏览器访问访问服务器时传递过来的cookie数组
                 Cookie[] cookies = request.getCookies();
                 //如果用户是第一次访问，那么得到的cookies将是null
                 if (cookies!=null) {
                         out.write("您上次访问的时间是：");
                         for (int i = 0; i < cookies.length; i++) {
                                 Cookie cookie = cookies[i];
                                 if (cookie.getName().equals("lastAccessTime")) {
                                         String lastAccessTime =cookie.getValue();
                                         LocalDateTime date = LocalDateTime.parse(lastAccessTime);
                                         out.write(date.toString());
                                     }
                             }
                     }else {
                         out.write("这是您第一次访问本站！");
                     }

                 //用户访问过之后重新设置用户的访问时间，存储到cookie中，然后发送到客户端浏览器
                 Cookie cookie = new Cookie("lastAccessTime", LocalDateTime.now().toString());//创建一个cookie，cookie的名字是lastAccessTime
                 //将cookie对象添加到response对象中，这样服务器在输出response对象中的内容时就会把cookie也输出到客户端浏览器
                 response.addCookie(cookie);
             }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
