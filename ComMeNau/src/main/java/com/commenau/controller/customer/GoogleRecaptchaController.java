package com.commenau.controller.customer;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/recaptcha")
public class GoogleRecaptchaController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
//
//        boolean verify = VerifyUtils.verify(gRecaptchaResponse);
//        if (verify) {
//            // reCAPTCHA đã được xác nhận, xử lý logic tiếp theo
//            // Điều này có thể là việc xác thực và xử lý biểu mẫu
//            response.getWriter().write("reCAPTCHA verified!");
//        } else {
//            // reCAPTCHA không được xác nhận, xử lý lỗi hoặc yêu cầu người dùng thử lại
//            response.getWriter().write("reCAPTCHA verification failed!");
//        }
    }
}