package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.InvoiceDTO;
import com.commenau.model.User;
import com.commenau.service.InvoiceService;
import com.commenau.service.WishlistService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/invoices")
public class InvoiceController extends HttpServlet {
    @Inject
    InvoiceService invoiceService;
    @Inject
    WishlistService wishlistService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
//        if (sortOption == null) {
//            invoicedtos = invoiceService.get10InvoiceDTOById(user.getId());
//        }
        List<InvoiceDTO> invoicedtos = null;
        String sortOption = req.getParameter("sortOption");
        if ("option10".equals(sortOption)) {
            invoicedtos = invoiceService.get10InvoiceDTOById(user.getId());
        } else if ("optionAll".equals(sortOption)) {
            invoicedtos = invoiceService.getAllInvoiceDTOById(user.getId());
        } else {
            invoicedtos = invoiceService.get10InvoiceDTOById(user.getId());
        }
        int count = 0;
        for(InvoiceDTO invoiceDTO : invoicedtos){
            if (invoiceDTO.getStatus().equals(SystemConstant.INVOICE_CANCEL)) count++;
        }
        //nav-left
        req.setAttribute("numWishlistItems", wishlistService.getAllWishlistItemById(user.getId()).size());
        req.setAttribute("numInvoiceCanceled", count);
        req.setAttribute("sizeListInvoiceDTO", invoicedtos.size());
        //main
        req.setAttribute("option", sortOption);
        req.setAttribute("listInvoiceDTO", invoicedtos);
        req.getRequestDispatcher("/customer/dash-my-order.jsp").forward(req, resp);
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
//        List<InvoiceDTO> invoicedtos = null;
//        String sortOption = req.getParameter("sortOption");
//        if ("10 đơn đặt hàng cuối cùng".equals(sortOption)) {
//            invoicedtos = invoiceService.get10InvoiceDTOById(user.getId());
//        } else if ("Tất cả các đơn đặt hàng".equals(sortOption)) {
//            invoicedtos = invoiceService.getAllInvoiceDTOById(user.getId());
//        }
//        req.setAttribute("SizelistInvoiceDTO", invoicedtos.size());
//        req.setAttribute("listInvoiceDTO", invoicedtos);
////        resp.sendRedirect("/dash-my-order.jsp?");
//        req.getRequestDispatcher("/customer/dash-my-order.jsp").forward(req, resp);
//    }
}
