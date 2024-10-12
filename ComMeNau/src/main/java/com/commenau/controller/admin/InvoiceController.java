package com.commenau.controller.admin;

import com.commenau.service.InvoiceService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/invoices")
public class InvoiceController extends HttpServlet {
    @Inject
    InvoiceService invoiceService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nextPage = req.getParameter("nextPage") == null ? "1" : req.getParameter("nextPage");
        int pageSize = 10;
        int allInvoice = invoiceService.getAllInvoice().size();
        int maxPage = (allInvoice % pageSize == 0) ? allInvoice / pageSize : (allInvoice / pageSize) + 1;
        try{
            if(Integer.parseInt(nextPage) > maxPage || Integer.parseInt(nextPage) <= 0){
                nextPage = "1";
            }
        }catch(Exception e){
            nextPage = "1";
            e.printStackTrace();
        }
        req.setAttribute("invoiceActive","");
        req.setAttribute("maxPage", maxPage);
        req.setAttribute("nextPage", nextPage);
        req.setAttribute("listInvoice", invoiceService.getAllInvoicePaged(Integer.parseInt(nextPage), pageSize));
        req.getRequestDispatcher("/admin/admin-order.jsp?nextPage=" + nextPage).forward(req, resp);
    }
}
