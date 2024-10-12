package com.commenau.controller.customer;

import com.commenau.mail.MailService;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.validate.Validator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.util.Map;

@WebServlet("/lost-password")
public class LostPasswordController extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    @Inject
    private UserService userService;
    @Inject
    private MailService mail;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String currentPage = request.getRequestURL().toString();
//        request.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        request.getRequestDispatcher("/customer/lost-password.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        if (validate(email)) {
            Map<User, String> map = userService.lostPassword(email);
            if (map != null) {
                mail.sendMailResetPassword(map, request);
                request.setAttribute("resetPasswordSuccess", "");
                request.getRequestDispatcher("/customer/signin.jsp").forward(request, response);
            } else {
                request.setAttribute("userNotExists", "");
                request.getRequestDispatcher("/customer/lost-password.jsp").forward(request, response);
            }
        }else {
            response.sendRedirect("lost-password");
        }
    }

    private boolean validate(String email) {
        return Validator.isValidEmail(email);
    }

}
