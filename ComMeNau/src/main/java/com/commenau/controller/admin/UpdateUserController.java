package com.commenau.controller.admin;

import com.commenau.constant.SystemConstant;
import com.commenau.log.LogService;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/updateUser")
public class UpdateUserController extends HttpServlet {
    @Inject
    UserService userService;
    @Inject
    LogService logService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        Long userId = HttpUtil.of(req.getReader()).toModel(Long.class); // geted userId when click button
        int numCurrentPage = req.getParameter("nextPage") == null ? 1 : Integer.parseInt(req.getParameter("nextPage"));
        User preUser = userService.getUserById(userId);
        if (userId != null) {
            boolean result = userService.lockOrUnlock(userId); //
            User user = userService.getUserById(userId);
            Map<String, String> preData = new HashMap<>();
            Map<String, String> data = new HashMap<>();
            preData.put("username", preUser.getUsername());
            preData.put("phoneNumber", preUser.getPhoneNumber());
            preData.put("status", preUser.getStatus());

            data.put("username", user.getUsername());
            data.put("phoneNumber", user.getPhoneNumber());
            data.put("newStatus", user.getStatus());
            if(result){
                logService.save(LogLevel.WARNING,"success",preData,data);
            }
            String currentPage = req.getRequestURL().toString();
            resp.getWriter().write(gson.toJson(""));
        }
    }
}
