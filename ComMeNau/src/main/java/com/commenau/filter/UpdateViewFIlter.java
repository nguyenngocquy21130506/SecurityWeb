package com.commenau.filter;

import com.commenau.constant.SystemConstant;
import com.commenau.service.ProductService;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@WebFilter("/product/*")
public class UpdateViewFIlter implements Filter {
    @Inject
    ProductService productService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        chain.doFilter(request, response);
        if (req.getMethod().equals("GET") && req.getSession().getAttribute(SystemConstant.AUTH) != null) {
            String[] pathInfo = req.getPathInfo().split("/");
            int productId = Integer.valueOf(pathInfo[1]);
            boolean update = true;
            List<Integer> productIds = (List<Integer>) req.getSession().getAttribute("listViewProduct");
            for (var id : productIds) {
                if (productId == id) {
                    update = false;
                }
            }
            if (update) {
                productService.updateAmountOfView(productId);
                productIds.add(productId);
                req.getSession().setAttribute("listViewProduct", productIds);
            }
        }
    }
}
