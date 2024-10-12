package com.commenau.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

@WebFilter(value = {"/login", "/login-facebook", "/login-google"})
public class InitValueFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        ((HttpServletRequest) request).getSession().setAttribute("listViewProduct", new ArrayList<Integer>());

    }
}
