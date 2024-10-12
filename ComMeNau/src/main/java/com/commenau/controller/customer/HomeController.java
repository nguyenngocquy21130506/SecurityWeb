package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.filter.RequestFilter;
import com.commenau.log.LogService;
import com.commenau.service.ProductService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/home")
public class HomeController extends HttpServlet {
    @Inject
    ProductService productService;
    private static final long serialVersionUID = 1L;
    @Inject
    LogService logService;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentPage = request.getRequestURL().toString();

        request.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        request.setAttribute("products", productService.getNewRelativeProductView());
        request.getRequestDispatcher("/customer/home.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
