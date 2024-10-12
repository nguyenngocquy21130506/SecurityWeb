package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.WishlistItemDTO;
import com.commenau.log.LogService;
import com.commenau.model.CartItem;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.model.WishlistItem;
import com.commenau.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.commenau.service.WishlistService;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/wishlist")
public class WishlistController extends HttpServlet {
    @Inject
    WishlistService wishlistService;
    @Inject
    LogService logService;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currentPage = req.getRequestURL().toString();
        req.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        try {
            User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
            List<WishlistItemDTO> wishlistDTOList = wishlistService.getAllWishlistItemById(user.getId());
            req.setAttribute("listWishlistItemDTO", wishlistDTOList);
            if (wishlistDTOList.size() == 0) {
                req.getRequestDispatcher("/customer/empty-wishlist.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/customer/wishlist.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            resp.getWriter().write("Get Your wishlist after sign in !");
            e.printStackTrace();
        }
    }

    //    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        User user = ((User) req.getSession().getAttribute(SystemConstant.AUTH));
//        ObjectMapper mapper = new ObjectMapper();
//        CartItem cartItem = HttpUtil.of(req.getReader()).toModel(CartItem.class);
//        if (user != null) {
//            //use database
//            boolean result = wishlistItemService.addProductToWishlist(user.getId(), cartItem.getProductId(), cartItem.getQuantity());
//            // using jackson send result to browser
//            mapper.writeValue(resp.getOutputStream(), result);
//        } else {
//            //use cookie
//            Cookie[] cookies = req.getCookies();
//            boolean found = false;
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("productId" + cartItem.getProductId())) {
//                    int value = Integer.parseInt(cookie.getValue());
//                    cookie.setValue(String.valueOf(value + cartItem.getQuantity()));
//                    cookie.setMaxAge(5 * 24 * 60 * 60);
//                    resp.addCookie(cookie);
//                    found = true;
//                    break;
//                }
//            }
//            if (!found) {
//                // If it isn't found in cookie, add that product
//                Cookie cookie = new Cookie("productId" + cartItem.getProductId(), "1");
//                cookie.setMaxAge(5 * 24 * 60 * 60);
//                resp.addCookie(cookie);
//                found = true;
//            }
//            mapper.writeValue(resp.getOutputStream(), found);
//        }
//    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.valueOf(req.getParameter("userId"));
        int productId = Integer.valueOf(req.getParameter("productId"));
        WishlistItem wishlistItem = WishlistItem.builder().userId(userId).productId(productId).build();
        if(wishlistItem != null){
            logService.save(LogLevel.INFO,"success",wishlistItem);
        }
        wishlistService.addItem(userId, productId);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String productID = req.getParameter("productId");
        if (productID != null) {
            wishlistService.deleteItem(Integer.valueOf(userId), Integer.valueOf(productID));

        } else {
            User user = ((User) req.getSession().getAttribute(SystemConstant.AUTH));
            Integer productId = HttpUtil.of(req.getReader()).toModel(Integer.class);
            ObjectMapper mapper = new ObjectMapper();
            boolean result = false;
            if (productId != null && productId > 0) { // delete a product
                result = wishlistService.deleteItem((int) user.getId(), productId);
            } else { //delete all product in wishlist
                result = wishlistService.resetAll(user.getId());
            }
            // using jackson send result to browser
            mapper.writeValue(resp.getOutputStream(), result);
            resp.getWriter().write("successfully");
        }
    }
}
