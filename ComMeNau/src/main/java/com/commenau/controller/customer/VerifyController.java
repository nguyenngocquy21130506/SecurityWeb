package com.commenau.controller.customer;

import com.commenau.service.EmailVerificationService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/verify-users")
public class VerifyController extends HttpServlet {
    @Inject
    private EmailVerificationService verificationService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = -1;
        try {
            userId = Integer.parseInt(request.getParameter("userId"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        String token = request.getParameter("token");
        boolean verified = verificationService.verifyUser(userId, token);
        if (verified) {
            request.setAttribute("verifySuccess", "");
            request.getRequestDispatcher("/customer/signin.jsp").forward(request, response);
        } else {
            request.setAttribute("verifyError", "");
            request.getRequestDispatcher("/customer/signin.jsp").forward(request, response);
        }
    }
}
