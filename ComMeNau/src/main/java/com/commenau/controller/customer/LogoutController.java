package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute(SystemConstant.AUTH);
        String previousPage = (String) request.getSession().getAttribute(SystemConstant.PRE_PAGE);
        if (previousPage != null && !previousPage.isEmpty()) {
            response.sendRedirect(previousPage);
        } else {
            response.sendRedirect("home");
        }
    }

}
