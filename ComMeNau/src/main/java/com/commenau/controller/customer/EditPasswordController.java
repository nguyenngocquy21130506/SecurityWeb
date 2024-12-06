package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.encryptMode.RSA;
import com.commenau.log.LogService;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.validate.Validator;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/change-password")
public class EditPasswordController extends HttpServlet {
    @Inject
    UserService userService;
    @Inject
    LogService logService;
    private RSA rsa;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            rsa = new RSA();
            String privateKey = Base64.getEncoder().encodeToString(rsa.getPrivateKey().getEncoded());

            req.setAttribute("privateKey", privateKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        req.getRequestDispatcher("/customer/dash-change-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
        String currentPassword = req.getParameter("currentPassword");   // password is encrypted
        String newPassword = req.getParameter("newPassword");           // new password is encrypted too
        String confirmPassword = req.getParameter("confirmPassword");   // confirm password is encrypted too

        // decrypts
        try {
            currentPassword = rsa.decrypt(currentPassword);
            newPassword = rsa.decrypt(newPassword);
            confirmPassword = rsa.decrypt(confirmPassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean hasError = validate(newPassword, confirmPassword, currentPassword, user);
        if (!hasError) {
            user = userService.changePassword(newPassword, user);
            resp.setStatus(user != null ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private boolean validate(String newPassword, String confirmPassword, String currentPassword, User user) {
        boolean checkPass = BCrypt.checkpw(currentPassword, user.getPassword());
        return Validator.isEmpty(newPassword) || Validator.isEmpty(currentPassword) ||
                Validator.isEmpty(confirmPassword) || !newPassword.equals(confirmPassword) ||
                !checkPass;
    }
}

