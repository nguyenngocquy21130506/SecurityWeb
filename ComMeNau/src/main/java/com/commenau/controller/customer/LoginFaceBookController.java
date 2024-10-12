package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.mapper.UserMapper;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.util.FaceBookUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login-facebook")
public class LoginFaceBookController extends HttpServlet {
    @Inject
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            showSigninErrorPage(request, response);
            return;
        }
        handleFaceBookSignin(request, response, code);
    }

    private void showSigninErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("signinError", "");
        request.getRequestDispatcher("/customer/signin.jsp").forward(request, response);
    }

    private void handleFaceBookSignin(HttpServletRequest request, HttpServletResponse response, String code) throws ServletException, IOException {
        String accessToken = FaceBookUtil.getToken(code);
        com.restfb.types.User userFaceBook = FaceBookUtil.getUserInfo(accessToken);
        if (userFaceBook.getId() == null) {
            showSigninErrorPage(request, response);
            return;
        }
        User user = userService.signinSocial(UserMapper.toModel(userFaceBook));
        if (user == null) {
            showSigninErrorPage(request, response);
            return;
        }
        request.getSession().setAttribute(SystemConstant.AUTH, user);
        String previousPage = (String) request.getSession().getAttribute(SystemConstant.PRE_PAGE);
        if (previousPage != null && !previousPage.isEmpty()) {
            response.sendRedirect(previousPage);
        } else {
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
