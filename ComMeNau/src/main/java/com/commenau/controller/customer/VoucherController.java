package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.CartItemDTO;
import com.commenau.dto.VoucherDTO;
import com.commenau.model.Cart;
import com.commenau.model.CartItem;
import com.commenau.model.User;
import com.commenau.model.Voucher;
import com.commenau.service.CartService;
import com.commenau.service.VoucherService;
import com.commenau.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/vouchers")
public class VoucherController extends HttpServlet {
    @Inject
    VoucherService voucherService;
    @Inject
    CartService cartService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
        List<Voucher> listVoucher = voucherService.getAllVoucher();
        List<CartItemDTO> items = cartService.getCartByUserId(user.getId());
        Cart cart = cartService.getCartById(user.getId());
        double totalPriceOfCart = cartService.getTotalPrice(items);
        req.setAttribute("cart", cart);
        req.setAttribute("totalPriceOfCart", totalPriceOfCart);
        req.setAttribute("vouchers", listVoucher);
        req.getRequestDispatcher("/customer/voucher.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int voucherId = HttpUtil.of(req.getReader()).toModel(Integer.class);
        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
        int voucherId = Integer.parseInt(req.getParameter("id"));
        //nham lan status voucher
//        boolean changeStatus = voucherService.changeStatusOfVoucher(user.getId(),voucherId);
        boolean chooseVoucher = cartService.chooseVoucher(user.getId(),voucherId);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getOutputStream(), chooseVoucher);
    }
}
