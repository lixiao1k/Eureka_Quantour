package filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by huihantao on 2017/6/6.
 */
public class MyFilter implements Filter {
    private FilterConfig filterConfig = null;
    private String defaultCharset = "UTF-8";

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String charset = filterConfig.getInitParameter("charset");
        if (charset == null) {
            charset = defaultCharset;
        }
        request.setCharacterEncoding(charset);
        response.setCharacterEncoding(charset);
        response.setContentType("text/html;charset=" + charset);

        MyCharacterEncodingRequest requestWrapper = new MyCharacterEncodingRequest(request);
        chain.doFilter(requestWrapper, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        //得到过滤器的初始化配置信息
        this.filterConfig = filterConfig;
    }

    public void destroy() {

    }

    class MyCharacterEncodingRequest extends HttpServletRequestWrapper {


        private HttpServletRequest request;

        public MyCharacterEncodingRequest(HttpServletRequest request) {
            super(request);
            this.request = request;

        }

        /* 重写getParameter方法
         * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
         */
        @Override

        public String getParameter(String name) {

            try {
                //获取参数的值
                String value = this.request.getParameter(name);
                if (value == null) {
                    return null;

                }
                //如果不是以get方式提交数据的，就直接返回获取到的值
                if (!this.request.getMethod().equalsIgnoreCase("get")) {
                    return value;

                } else {
                    //如果是以get方式提交数据的，就对获取到的值进行转码处理
                    value = new String(value.getBytes("ISO8859-1"), this.request.getCharacterEncoding());
                    return value;

                }

            } catch (Exception e) {
                throw new RuntimeException(e);

            }

        }

    }
}
