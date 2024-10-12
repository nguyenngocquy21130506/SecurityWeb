package com.commenau.controller.admin;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.InvoiceDTO;
import com.commenau.dto.InvoiceItemDTO;
import com.commenau.log.LogService;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.InvoiceService;
import com.commenau.util.HttpUtil;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/admin/invoiceDetail")
public class InvoiceDetailController extends HttpServlet {
    @Inject
    InvoiceService invoiceService;
    @Inject
    LogService logService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int invoiceId = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("invoiceActive", "");
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceDTOById(invoiceId);
        req.setAttribute("invoiceOf", invoiceDTO);
        List<InvoiceItemDTO> list = invoiceService.getAllInvoiceItemDTOByInvoiceId(invoiceId);
        req.setAttribute("listInvoiceDetail", list);
        List<String> states = new ArrayList<>();
        states.add(SystemConstant.INVOICE_PROCESSING);
        states.add(SystemConstant.INVOICE_SHIPPING);
        states.add(SystemConstant.INVOICE_SHIPPED);
        states.add(SystemConstant.INVOICE_ACCEPTED);
        if(!invoiceDTO.getStatus().equals(SystemConstant.INVOICE_ACCEPTED)){
            states.add(SystemConstant.INVOICE_CANCEL);
        }
        req.setAttribute("states", states);
        req.getRequestDispatcher("/admin/admin-order-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String str = HttpUtil.of(req.getReader()).toModel(String.class);
        StringTokenizer reader = new StringTokenizer(str, "-");
        Integer invoiceId = Integer.valueOf(reader.nextToken()); // geted invoiceId when click button_save
        InvoiceDTO preInvoiceDTO = invoiceService.getInvoiceDTOById(invoiceId);
        String selectedStatus = reader.nextToken(); // geted selectedStatus when click button_save
        if (invoiceId != null) {
            boolean result = invoiceService.changeInvoiceStatus(invoiceId, ((User) req.getSession().getAttribute(SystemConstant.AUTH)).getRoleId(), selectedStatus);
            InvoiceDTO invoiceDTO = invoiceService.getInvoiceDTOById(invoiceId);
            if(result){
                Map<String, String> preData = new HashMap<>();
                Map<String, String> data = new HashMap<>();
                preData.put("invoiceId", preInvoiceDTO.getId()+"");
                preData.put("lastName", preInvoiceDTO.getUserFullName());
                preData.put("phoneNumber", preInvoiceDTO.getPhoneNumber());
                preData.put("status", preInvoiceDTO.getStatus());

                data.put("invoiceId", invoiceDTO.getId()+"");
                data.put("lastName", invoiceDTO.getUserFullName());
                data.put("phoneNumber", invoiceDTO.getPhoneNumber());
                data.put("newStatus", invoiceDTO.getStatus());
                logService.save(LogLevel.WARNING,"ChangeSuccess",preData,data);
                resp.getWriter().write(gson.toJson("Successfully change"));
            } else{
                resp.setStatus(HttpStatus.SC_BAD_REQUEST);
                resp.getWriter().write("Not Allow to change");
            }
        }
    }
}
