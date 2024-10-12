package com.commenau.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
