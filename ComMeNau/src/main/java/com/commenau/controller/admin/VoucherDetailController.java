package com.commenau.controller.admin;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.VoucherDTO;
import com.commenau.log.LogService;
import com.commenau.model.Blog;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.model.Voucher;
import com.commenau.service.BlogService;
import com.commenau.service.VoucherService;
import com.commenau.util.FormUtil;
import com.commenau.util.WriteImage;
import com.commenau.validate.Validator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;


@MultipartConfig(
        maxFileSize = 1024 * 1024,// 1MB
        maxRequestSize = 1024 * 1024 * 10//10MB
)
@WebServlet("/admin/voucher-detail")
public class VoucherDetailController extends HttpServlet {
    @Inject
    private VoucherService voucherService;
    @Inject
    private LogService logService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String voucherId = request.getParameter("voucherId");
        VoucherDTO voucher = new VoucherDTO();
        // display data of blog
        if (voucherId != null) {
            int newVoucherId = 0;
            try {
                newVoucherId = Integer.parseInt(voucherId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            voucher = voucherService.getVoucherById(newVoucherId);

            // Chuyển đổi khoảng thời gian từ mili giây sang ngày
            int diffDays = (int) ((voucher.getExpriedAt().getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24));
            request.setAttribute("voucherId", Integer.parseInt(voucherId));
            request.setAttribute("expiry", diffDays);
            request.setAttribute("voucher", voucher);
        }

        request.setAttribute("voucherActive", "");
        request.getRequestDispatcher("/admin/admin-voucher-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
        int expiredAt = Integer.parseInt(request.getParameter("expiry"));
        Voucher voucher = FormUtil.toModel(Voucher.class, request);
        // Tạo đối tượng Timestamp từ timestamp tính bằng mili giây
        Timestamp newExpiredAt = new Timestamp(System.currentTimeMillis() + expiredAt * 24L * 60 * 60 * 1000);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        voucher.setExpriedAt(newExpiredAt);
        //validate
        boolean hasError = validate(voucher);
        if (!hasError) {
            VoucherDTO newVoucher = voucherService.getVoucherById(voucher.getId());
            boolean isOperationSuccessful = voucher.getId() != 0 ? voucherService.update(voucher) : voucherService.save(voucher);
            if (isOperationSuccessful) {
                if (voucher.getId() != 0) { //update
                    VoucherDTO preVoucher = voucherService.getVoucherById(voucher.getId());
                    logService.save(LogLevel.INFO, "updateSuccess", preVoucher, newVoucher);
                } else { // create
                    logService.save(LogLevel.INFO, "addSuccess", newVoucher);
                }
            }
            int statusCode = isOperationSuccessful ? HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            response.setStatus(statusCode);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    private boolean validate(Voucher voucher) {
        if (Validator.isEmpty(voucher.getName()) ||
                Validator.isEmpty(String.valueOf(voucher.getApplyTo())) ||
                Validator.isEmpty(String.valueOf(voucher.getDiscount())))
            return true;
        return false;
    }
}
