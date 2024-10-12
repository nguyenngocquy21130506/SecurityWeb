package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dto.CartItemDTO;
import com.commenau.dto.InvoiceDTO;
import com.commenau.dto.ProductViewDTO;
import com.commenau.log.LogService;
import com.commenau.model.Cart;
import com.commenau.model.Invoice;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.CartService;
import com.commenau.service.InvoiceService;
import com.commenau.service.ProductService;
import com.commenau.service.VoucherService;
import com.commenau.util.FormUtil;
import com.commenau.validate.Validator;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/payments")
public class CheckoutController extends HttpServlet {
    @Inject
    private CartService cartService;
    @Inject
    private InvoiceService invoiceService;
    @Inject
    private ProductService productService;
    @Inject
    private VoucherService voucherService;
    @Inject
    private LogService logService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
        String paymentMethod = request.getParameter("paymentMethod");
        String key = request.getParameter("key");

        if (!StringUtils.isBlank(paymentMethod) && paymentMethod.equals("VNPAY")) {
            if (!request.getParameter("vnp_ResponseCode").equals("00")) {
                response.sendRedirect("carts");
                return;
            }
            Invoice invoice = SystemConstant.waitingPayments.get(key);
            SystemConstant.waitingPayments.remove(key);
            invoice.setUserId(user.getId());
            boolean isSuccess = invoiceService.saveInvoice(invoice);
            if (isSuccess) {
                logService.save(LogLevel.INFO, "success", invoice);
            }
            response.sendRedirect("carts");
            return;
        }

        if (user != null) {
            //using db
            List<CartItemDTO> items = cartService.getCartByUserId(user.getId());
            if (items.isEmpty()) {
                response.sendRedirect("carts");
                return;
            } else {
                long totalPrice = cartService.getTotalPrice(items);

                // add voucher
                boolean opened = false;
                for (String date : dateOpenVoucher()) {
                    int day = Integer.parseInt(date.substring(0, date.indexOf("/")));
                    int month = Integer.parseInt(date.substring(date.indexOf("/") + 1, date.length()));
                    if (day == LocalDate.now().getDayOfMonth() && month == LocalDate.now().getMonthValue()) {
                        opened = true;
                    }
                }
//                String idVoucher = request.getParameter("idVoucher");
                String idVoucher = String.valueOf(cartService.getCartById(user.getId()).getVoucherId());
                long lastTotalPrice = voucherService.applyVoucher(totalPrice, idVoucher);
                String nameVoucher = (idVoucher.equals("0")) ? "" : voucherService.getVoucherById(Integer.parseInt(idVoucher)).getName();
                Cart cart = cartService.getCartById(user.getId());

                request.setAttribute("cartItems", items); // list cartItem haizz
                request.setAttribute("cart", cart);
                request.setAttribute("opened", opened);
                request.setAttribute("totalPrice", lastTotalPrice);
                request.setAttribute("nameVoucher", nameVoucher);
            }
            request.setAttribute("fullName", user.getLastName() + " " + user.getFirstName());
            request.setAttribute("phoneNumber", user.getPhoneNumber());
            request.setAttribute("address", user.getAddress());
            request.setAttribute("email", user.getEmail());
        } else {
            //get products from cookie
            List<CartItemDTO> items = new ArrayList<>();
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("productId")) {
                    int productId = Integer.parseInt(cookie.getName().substring("productId".length()));
                    int quantity = Integer.parseInt(cookie.getValue());
                    ProductViewDTO product = productService.getByIdWithAvatar(productId);
                    items.add(CartItemDTO.builder().product(product).quantity(quantity).build());
                }
            }
            if (items.isEmpty()) {
                response.sendRedirect("carts");
                return;
            } else {
                long totalPrice = cartService.getTotalPrice(items);
                request.setAttribute("cart", items);
                request.setAttribute("totalPrice", totalPrice);
            }
        }
//        String currentPage = request.getRequestURL().toString();
//        request.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        request.getRequestDispatcher("/customer/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");

        User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
        Invoice invoice = FormUtil.toModel(Invoice.class, request);
        boolean hasError = validate(invoice);

        boolean isVnPayAction = !StringUtils.isBlank(invoice.getPaymentMethod()) && invoice.getPaymentMethod().equals("VNPAY");

        boolean isSuccess = false;

        if (!hasError) {
            if (user != null) {
                invoice.setUserId(user.getId());
                isSuccess = invoiceService.saveInvoice(invoice);
                if(isSuccess){
                    InvoiceDTO invoiceDTO = invoiceService.getInvoiceDTOById(invoice.getId());
                    logService.save(LogLevel.INFO, "success", invoiceDTO);
                }
            } else {
                // Using cookie
                isSuccess = invoiceService.saveInvoiceWithCookie(invoice, request.getCookies());
                if (isSuccess) {
                    // Delete all cart items in cookie
                    Cookie[] cookies = request.getCookies();
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().startsWith("productId")) {
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        }
                    }
                }
            }
        }

        if (isVnPayAction) {
            response.sendRedirect("carts");
            return;
        }

        if (!hasError && isSuccess) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else if (!hasError && !isSuccess) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private boolean validate(Invoice invoice) {
        if (Validator.isEmpty(invoice.getFullName()) || Validator.isEmpty(invoice.getAddress()) ||
                !Validator.isValidEmail(invoice.getEmail()) || !Validator.isValidPhoneNumber(invoice.getPhoneNumber()))
            return true;
        return false;
    }

    private List<String> dateOpenVoucher() {
        List<String> re = new ArrayList<>();
        re.add("1/1"); // Tết Dương Lịch
        re.add("14/2"); // Lễ Tình nhân
        re.add("8/3"); // Ngày Quốc tế Phụ nữ
        re.add("14/3"); // Lễ Tình nhân Trắng
        re.add("30/4"); // Ngày Giải phóng miền Nam
        re.add("1/5"); // Ngày Quốc tế Lao động
        re.add("2/9"); // Ngày Quốc khánh
        re.add("20/10"); // Ngày Phụ nữ Việt Nam
        re.add("20/11"); // Ngày Nhà giáo Việt Nam
        re.add("24/12"); // Ngày Noel
        re.add("2/2");
        re.add("3/3");
        re.add("4/4");
        re.add("5/5");
        re.add("6/6");
        re.add("7/7");
        re.add("8/8");
        re.add("9/9");
        re.add("10/10");
        re.add("11/11");
        re.add("12/12");
        re.add("11/7"); //test
        return re;
    }
}
