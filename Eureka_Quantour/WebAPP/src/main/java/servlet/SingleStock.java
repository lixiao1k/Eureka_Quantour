package servlet;


import rmi.RemoteHelper;

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
//        RemoteHelper.getInstance().getStockLogic().getStockBasicInfo(code,);

        resp.getWriter().println("<script language='JavaScript' src='scripts/test.js'></script>");
        resp.getWriter().println("<button type=\"button\" onclick=\"test()\">Try it</button>");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
