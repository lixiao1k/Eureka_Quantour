package servlet;

import exception.LogErrorException;
import rmi.RemoteHelper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Created by huihantao on 2017/6/6.
 */
public class Login extends HttpServlet {

    private boolean login=false;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username=req.getParameter("Name");
        String password=req.getParameter("Password");
//        try {
//            RemoteHelper.getInstance().getClientLogic().signIn(username,password.toCharArray());
//        } catch (LogErrorException e) {
//            e.printStackTrace();
//        }

        ServletContext context = this.getServletConfig().getServletContext();

        if (context.getAttribute("name")==null) {
            context.setAttribute("name", username);
        }
        else{

        }
        resp.sendRedirect("/views/welcome.jsp");


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
