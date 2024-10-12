package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.model.User;
import com.commenau.service.CategoryService;
import com.commenau.service.ProductService;
import com.commenau.service.WishlistService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/menu")
public class MenuController extends HttpServlet {
    @Inject
    ProductService productService;
    @Inject
    CategoryService categoryService;
    @Inject
    WishlistService wishlistService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currentPage = req.getRequestURL().toString();
        req.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        int categoryId ;
        if(req.getParameter("categoryId") != null){
            categoryId = Integer.valueOf(req.getParameter("categoryId"));
        }
        else{
            categoryId = 1;
        }

        req.setAttribute("total" , productService.countProductsInCategory(categoryId));
        req.setAttribute("categories" , categoryService.getAllCategoryInfo());
        req.getRequestDispatcher("/customer/filter.jsp").forward(req,resp);
    }
}
