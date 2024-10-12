package com.commenau.controller.admin;

import com.commenau.constant.SystemConstant;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/findUser")
public class FindUserController extends HttpServlet {
    @Inject
    UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nextPage = req.getParameter("nextPage") == null ? "1" : req.getParameter("nextPage");
        String findInput = req.getParameter("inputData") == null ? "" : req.getParameter("inputData");
        req.setAttribute("userActive", "");
        if (!findInput.equals("")) {
            List<User> list = userService.findUserByInput(findInput);
            req.setAttribute("inputKey", findInput);
            req.setAttribute("listCustomer", list);
            req.setAttribute("maxPage", 0);
            req.setAttribute("nextPage", 1);
            req.getRequestDispatcher("/admin/admin-user.jsp").forward(req, resp);
        }else{
            int pageSize = 10;
            int allCustomer = userService.getNumCustomer();
            int maxPage = (allCustomer % pageSize == 0)
                    ? allCustomer / pageSize : (allCustomer / pageSize) + 1;
            try{
                if(Integer.parseInt(nextPage) > maxPage || Integer.parseInt(nextPage) <= 0){
                    nextPage = "1";
                }
            }catch(Exception e){
                nextPage = "1";
                e.printStackTrace();
            }
            req.setAttribute("maxPage", maxPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("listCustomer", userService.getAllCustomerPaged(Integer.parseInt(nextPage), pageSize));
            req.getRequestDispatcher("/admin/admin-user.jsp?nextPage=" + nextPage).forward(req, resp);
        }
    }
}
