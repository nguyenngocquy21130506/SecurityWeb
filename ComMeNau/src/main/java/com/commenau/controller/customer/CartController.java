package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.CartItemDTO;
import com.commenau.dto.ProductViewDTO;
import com.commenau.model.CartItem;
import com.commenau.model.User;
import com.commenau.service.CartService;
import com.commenau.service.ProductService;
import com.commenau.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/carts")
public class CartController extends HttpServlet {
    @Inject
    private CartService cartService;
    @Inject
    private ProductService productService;
    Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = ((User) request.getSession().getAttribute(SystemConstant.AUTH));
        List<CartItemDTO> items;
        if (user != null) {
            items = cartService.getCartByUserId(user.getId());
        } else {
            //get products from cookie
            items = new ArrayList<>();
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("productId")) {
                    int productId = Integer.parseInt(cookie.getName().substring("productId".length()));
                    int quantity = Integer.parseInt(cookie.getValue());
                    ProductViewDTO product = productService.getByIdWithAvatar(productId);
                    items.add(CartItemDTO.builder().product(product).quantity(quantity).build());
                }
            }
        }
        if (items.isEmpty())
            request.getRequestDispatcher("/customer/empty-cart.jsp").forward(request, response);
        else {
            List<CartItemDTO> remain = cartService.remainItems(items);
            List<CartItemDTO> remove = cartService.removeItems(items);
            cartService.deleteProduct(remove.stream().map(CartItemDTO::getId).toList());
            double totalPrice = cartService.getTotalPrice(remain);
            request.setAttribute("remove", gson.toJson(remove));
            request.setAttribute("cart", remain);
            request.setAttribute("totalPrice", totalPrice);
            request.getRequestDispatcher("/customer/cart.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the logged-in user
        User user = ((User) request.getSession().getAttribute(SystemConstant.AUTH));
        // Map incoming JSON data to CartItem
        CartItem cartItem = HttpUtil.of(request.getReader()).toModel(CartItem.class);
        if (cartItem == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        // Ensure quantity is at least 1
        if (cartItem.getQuantity() == 0) {
            cartItem.setQuantity(1);
        }

        // Handle logic based on user authentication
        if (user != null) {
            // Use database for authenticated users
            boolean result = cartService.addProductToCart(user.getId(), cartItem.getProductId(), cartItem.getQuantity());
            if (result)
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            else
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            // Use cookie for non-authenticated users
            Cookie[] cookies = request.getCookies();
            int available;

            // Loop through cookies to handle products in the cart
            for (Cookie cookie : cookies) {
                // Find the corresponding product in the cookies
                if (cookie.getName().equals("productId" + cartItem.getProductId())) {

                    // Increment quantity if found in the cookie
                    int quantity = Integer.parseInt(cookie.getValue()) + cartItem.getQuantity();
                    if (productService.checkProductValid(cartItem.getProductId(), quantity)) {
                        available = productService.checkAvailable(cartItem.getProductId(), quantity);
                        cookie.setValue(String.valueOf(available));
                        cookie.setMaxAge(5 * 24 * 60 * 60);
                        response.addCookie(cookie);
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    } else
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }
            }
            if (productService.checkProductValid(cartItem.getProductId(), cartItem.getQuantity())) {
                // Add the product to cookies if not found in the existing cart
                available = productService.checkAvailable(cartItem.getProductId(), cartItem.getQuantity());
                Cookie cookie = new Cookie("productId" + cartItem.getProductId(), String.valueOf(available));
                cookie.setMaxAge(5 * 24 * 60 * 60);
                response.addCookie(cookie);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Map incoming JSON data to a Map
        Map<String, String> map = HttpUtil.of(request.getReader()).toModel(Map.class);

        // Get the logged-in user
        User user = ((User) request.getSession().getAttribute(SystemConstant.AUTH));

        // Handle logic based on user authentication
        if (user != null) {
            // Use database for authenticated users to update the cart
            boolean result = cartService.updateCart(map, user.getId());
            if (result)
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            else
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            // Use cookie for non-authenticated users to update the cart
            Cookie[] cookies = request.getCookies();
            boolean hasError = false;

            // Loop through cookies to handle cart updates
            for (Cookie cookie : cookies) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (cookie.getName().equals("productId" + entry.getKey())) {
                        // Extract productId from cookie name
                        int productId = Integer.parseInt(entry.getKey());
                        int quantity = Integer.parseInt(entry.getValue());

                        // Check if the product is valid before updating
                        if (!productService.checkProductValid(productId, quantity)) {
                            hasError = true;
                        }
                        quantity = productService.checkAvailable(productId, quantity);
                        cookie.setValue(String.valueOf(quantity));
                        cookie.setMaxAge(5 * 24 * 60 * 60);
                        response.addCookie(cookie);

                        break;
                    }
                }
            }
            if (hasError)
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            else {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = ((User) request.getSession().getAttribute(SystemConstant.AUTH));
        Integer productId = HttpUtil.of(request.getReader()).toModel(Integer.class);
        ObjectMapper mapper = new ObjectMapper();
        if (user != null) {
            boolean result = false;
            // delete a product
            if (productId != null && productId > 0) {
                result = cartService.deleteProduct(productId, user.getId());
            } else {
                //delete all product in cart
                result = cartService.deleteAll(user.getId());
            }
            // using jackson send a result to browser
            mapper.writeValue(response.getOutputStream(), result);
        } else {
            // delete a product
            if (productId != null && productId > 0) {
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("productId" + productId)) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        mapper.writeValue(response.getOutputStream(), true);
                        break;
                    }
                }
            } else {
                //delete all product in cart
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if (cookie.getName().startsWith("productId")) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
                mapper.writeValue(response.getOutputStream(), true);
            }
        }

    }

}
