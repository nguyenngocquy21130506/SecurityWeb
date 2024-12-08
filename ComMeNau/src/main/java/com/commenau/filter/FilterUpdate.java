package com.commenau.filter;

import com.commenau.service.SearchService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

@WebFilter("/*")
public class FilterUpdate implements Filter {
    @Inject
    SearchService searchService;

    private static String paths[] = {};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        for (var x : paths) {
            if (Pattern.matches(x, ((HttpServletRequest) request).getServletPath())) {
                searchService.updateInformationSearching();
            }
        }
        Iterator<String> it = ((HttpServletRequest) request).getParameterNames().asIterator();
        chain.doFilter(request, response);
    }
}
