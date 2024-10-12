package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dao.UserDAO;
import com.commenau.log.LogService;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.RoleService;
import com.commenau.service.UserService;
import com.commenau.util.EncryptUtil;
import com.commenau.util.FormUtil;
import com.commenau.util.VerifyCaptcha;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Inject
    private UserService userService;
    @Inject
    private RoleService roleService;
    @Inject
    private LogService logService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = "";
        String password = "";
        String rememberMe = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cUsername"))
                    username = cookie.getValue();
                else if (cookie.getName().equals("cPassword"))
                    password = cookie.getValue();
                else if (cookie.getName().equals("cRememberMe"))
                    rememberMe = cookie.getValue();
            }
        }
        request.setAttribute("username", username);
        request.setAttribute("password", EncryptUtil.decrypt(password));
        request.setAttribute("rememberMe", rememberMe);
        request.getRequestDispatcher("/customer/signin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get user data from the form
//        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
//        if (!VerifyCaptcha.checkCaptcha(gRecaptchaResponse)) {
//            doGet(request,response);
//            return;
//        }

        User userForm = FormUtil.toModel(User.class, request);
        String password = userForm.getPassword();
        String rememberMe = request.getParameter("rememberMe");
        // Check for duplicate username and password and status
        User user = userService.signin(userForm);

        if (user != null) {
            logService.save(LogLevel.INFO, "success", user);
            HttpSession session = request.getSession();
            session.setAttribute(SystemConstant.AUTH, user);
            // remember me
            Cookie cUsername = new Cookie("cUsername", user.getUsername());
            Cookie cPassword = new Cookie("cPassword", EncryptUtil.encrypt(password));
            Cookie cRememberMe = new Cookie("cRememberMe", rememberMe);
            cUsername.setMaxAge(5 * 24 * 60 * 60); //5 days

            if (rememberMe != null) {
                cPassword.setMaxAge(5 * 24 * 60 * 60); //5 days
                cRememberMe.setMaxAge(5 * 24 * 60 * 60); //5 days
            } else {
                cPassword.setMaxAge(0);
                cRememberMe.setMaxAge(0);
            }
            response.addCookie(cUsername);
            response.addCookie(cPassword);
            response.addCookie(cRememberMe);

            String previousPage = (String) request.getSession().getAttribute(SystemConstant.PRE_PAGE);
            userService.resetNumLoginFail(user.getId());
            if (previousPage != null && !previousPage.isEmpty()) {
                if (user.getRoleId() == SystemConstant.ADMIN_ROLE_ID) {
                    response.sendRedirect(request.getContextPath() + "/admin/home");
                    return;
                }
                response.sendRedirect(previousPage);
            } else if(user.getRoleId() == SystemConstant.ADMIN_ROLE_ID){
                response.sendRedirect("/admin/home");
            }
            else if(user.getRoleId() == SystemConstant.USER_ROLE_ID){
                response.sendRedirect("/home");
            }
        } else {
            Map<String, String> data = new HashMap<>();
            data.put("username", userForm.getUsername());
            // need log plus info...
            logService.save(LogLevel.INFO, "failure", data);
            user = userService.getUserByUserName(userForm.getUsername());
            if (user != null) {
                String check = "1";
                request.setAttribute("checkIp", check);
                userService.loginFail(user); // Update user data in database
                // Lock the account if numLoginFail reaches 3
                int remain = 3 - userService.getNumLoginFail(user);
                if (remain <= 0) {
                    if (remain == 0) {
                        userService.lockOrUnlock(user.getId());
                    }
                    request.setAttribute("signinError", "<strong>Vui lòng quay lại vào ngày mai.</strong></br>Bạn đã đăng nhập sai 3 lần liên tiếp trong hôm nay");
                } else {
                    request.setAttribute("signinError", "Còn " + remain + " lần đăng nhập");
                }
            } else {
                request.setAttribute("haventAccount", "ok");
                request.setAttribute("signinError", "<strong>Tên đăng nhập không tồn tại !" +
                        "Vui lòng đăng ký tài khoản trước khi đăng nhập !</strong>");
            }

            request.getRequestDispatcher("/customer/signin.jsp").forward(request, response);
        }
    }
}
