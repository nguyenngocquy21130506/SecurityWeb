package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.mail.MailService;
import com.commenau.model.EmailVerification;
import com.commenau.model.User;
import com.commenau.service.EmailVerificationService;
import com.commenau.service.UserService;
import com.commenau.util.FormUtil;
import com.commenau.validate.Validator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/signup")
public class SignupController extends HttpServlet {
    @Inject
    private UserService userService;
    @Inject
    private EmailVerificationService verificationService;
    @Inject
    private MailService mail;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/customer/signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user data from the form
        User userForm = FormUtil.toModel(User.class, request);
        //validate
        boolean hasError = validate(userForm, request);
        // If there are errors, forward to signup page with error messages
        if (hasError) {
            request.setAttribute("user", userForm);
            request.getRequestDispatcher("/customer/signup.jsp").forward(request, response);
        } else {
            // Register the user
            User user = userService.signup(userForm, SystemConstant.NOT_AUTHENTICATION);
            if (user == null) {
                // If registration fails, forward to signup page with an error message
                request.setAttribute("signupError", "Đăng ký tài khoản thất bại.");
                request.setAttribute("user", userForm);
                request.getRequestDispatcher("/customer/signup.jsp").forward(request, response);
                return;
            }

            // Create email verification
            EmailVerification verification = verificationService.insert(user.getId());
            // Send email verification
            if (verification != null) {
                mail.sendMailVerifyUser(user, verification, request);
                request.setAttribute("registerSuccess", "");
                request.getRequestDispatcher("/customer/signin.jsp").forward(request, response);
            } else {
                // If email verification fails, forward to signup page
                request.getRequestDispatcher("/customer/signup.jsp").forward(request, response);
            }
        }
    }

    private boolean validate(User user, HttpServletRequest request) {
        String usernameError = "usernameError";
        String emailError = "emailError";
        boolean hasError = false;
        if (Validator.containsWhitespace(user.getUsername()) || userService.isUsernameExists(user.getUsername())) {
            request.setAttribute(usernameError, "Tên đăng nhập đã tồn tại");
            hasError = true;
        }

        if (!Validator.isValidEmail(user.getEmail()) || userService.isEmailExists(user.getEmail())) {
            request.setAttribute(emailError, "Email đã tồn tại");
            hasError = true;
        }

        if (Validator.isEmpty(user.getFirstName()) || Validator.isEmpty(user.getLastName()) ||
                Validator.containsWhitespace(user.getPassword()) || !Validator.isValidMinLength(user.getPassword(), 6)) {
            hasError = true;
        }

        if (!Validator.isValidPhoneNumber(user.getPhoneNumber())) {
            hasError = true;
        }
        if (!Validator.isValidPassword(user.getPassword())) {
            hasError = true;
        }
        return hasError;
    }

}
