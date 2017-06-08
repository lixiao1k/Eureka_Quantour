package servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by huihantao on 2017/6/5.
 */
public class UserloginServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String message="awesgrd";
        request.setAttribute("message", message);
        request.getRequestDispatcher("/userlogin.jsp").forward(request,
                response);


    }
}
