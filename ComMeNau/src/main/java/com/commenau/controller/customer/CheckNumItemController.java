package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.CartItemDTO;
import com.commenau.model.Cart;
import com.commenau.model.User;
import com.commenau.model.Voucher;
import com.commenau.service.CartService;
import com.commenau.service.VoucherService;
import com.commenau.service.WishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/checkNumItem")
public class CheckNumItemController extends HttpServlet {
    @Inject
    CartService cartService;
    @Inject
    WishlistService wishlistService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
        if(user!=null){
            int numCartItem = cartService.getNumCartItem(user.getId());
            int numWishlistItem = wishlistService.getAllWishlistItemById(user.getId()).size();
            String result = numCartItem+"-"+numWishlistItem;
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getOutputStream(), result);
        }else{
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
