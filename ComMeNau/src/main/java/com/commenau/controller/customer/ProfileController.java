package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.util.HttpUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {

    @Inject
    UserService userService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
        req.setAttribute("auth",userService.getUserById(user.getId()));
        String currentPage = req.getRequestURL().toString();
        req.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        req.getRequestDispatcher("/customer/dash-profile.jsp").forward(req,resp);
    }
}
