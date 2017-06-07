package servlet;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by huihantao on 2017/6/7.
 */
public class SingleStock extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code=req.getParameter("stockcode");
        resp.getWriter().println(code);
        resp.getWriter().println( "<script>");
        resp.getWriter().println("      alert('My First JavaScript')");
        resp.getWriter().println("</script>");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
