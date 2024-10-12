package com.commenau.controller.admin;

import com.commenau.model.Product;
import com.commenau.service.InvoiceService;
import com.commenau.service.UserService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/home")
public class HomeController extends HttpServlet {
    @Inject
    private UserService userService;
    @Inject
    private InvoiceService invoiceService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalUser = userService.countAll();
        int sellingOfDay = invoiceService.sellingOfDay();
        int sellingOfMonth = invoiceService.sellingOfMonth();
        double revenueOfMonth = invoiceService.revenueOfMonth();
        double revenueOfDay = invoiceService.revenueOfDay();
        Map<String, Integer> bestSale = invoiceService.bestSaleProduct();

        request.setAttribute("totalUser",totalUser);
        request.setAttribute("sellingOfDay",sellingOfDay);
        request.setAttribute("sellingOfMonth",sellingOfMonth);
        request.setAttribute("revenueOfMonth",revenueOfMonth);
        request.setAttribute("revenueOfDay",revenueOfDay);
        request.setAttribute("bestSale",bestSale);
        request.getRequestDispatcher("/admin/admin-home.jsp").forward(request, response);
    }
}
