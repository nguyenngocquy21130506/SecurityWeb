package com.commenau.filter;

import com.commenau.constant.SystemConstant;
import com.commenau.dao.RoleDAO;
import com.commenau.model.Role;
import com.commenau.model.User;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter implements Filter {
    private static String[] urls;

    static {
        urls = new String[]{"/change-profile", "/profile", "/change-password", "/invoices", "/invoice-details", "/wishlist", "/keypair-manager"};
    }

    @Inject
    private RoleDAO roleDAO;
    private ServletContext context;

    private static boolean contain(String url) {
        for (int i = 0; i < urls.length; i++)
            if (url.contains(urls[i]))
                return true;
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String url = httpServletRequest.getRequestURI();
        // if user want access admin's url
        User user = (User) httpServletRequest.getSession().getAttribute(SystemConstant.AUTH);
        if (url.contains("/admin")) {
            //user has login success
            if (user != null) {
                Role role = roleDAO.getRoleById(user.getRoleId());
                if (role.getName().equals(SystemConstant.ADMIN))
                    chain.doFilter(request, response);
                else if (role.getName().equals(SystemConstant.USER))
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/home");
            } else
                //user hasn't login
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login");
        } else if (contain(url)) {
            // if users access urls that require login
            if (user != null)
                chain.doFilter(request, response);
            else
                //user hasn't login
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login");
        } else chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
    }

    @Override
    public void destroy() {
    }
}
