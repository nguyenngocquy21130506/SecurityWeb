package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.log.LogService;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.util.FormUtil;
import com.commenau.util.HttpUtil;
import com.commenau.validate.Validator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/change-profile")
public class EditProfileController extends HttpServlet {
    @Inject
    UserService userService;
    @Inject
    LogService logService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/customer/dash-edit-profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = HttpUtil.of(req.getReader()).toModel(User.class);
        User preUser = userService.getUserById(user.getId());
        boolean hasError = validate(user);
        if (!hasError) {
            boolean result = userService.updateProfile(user);
            Map<String, String> preData = new HashMap<>();
            Map<String, String> data = new HashMap<>();
            preData.put("username", preUser.getUsername());
            preData.put("firstName", preUser.getFirstName());
            preData.put("lastName", preUser.getLastName());
            preData.put("phoneNumber", preUser.getPhoneNumber());
            preData.put("address", preUser.getAddress());

            data.put("username", user.getUsername());
            data.put("firstName", user.getFirstName());
            data.put("lastName", user.getLastName());
            data.put("phoneNumber", user.getPhoneNumber());
            data.put("address", user.getAddress());
            if(result){
                logService.save(LogLevel.INFO,"success",preData,data);
            }else{
                logService.save(LogLevel.WARNING,"failed",preData,data);
            }
            req.getSession().setAttribute(SystemConstant.AUTH, user);
            resp.setStatus(result ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private boolean validate(User user) {
        return Validator.isEmpty(user.getFirstName()) || Validator.isEmpty(user.getLastName()) ||
                Validator.isEmpty(user.getAddress()) || !Validator.isValidPhoneNumber(user.getPhoneNumber());

    }
}
