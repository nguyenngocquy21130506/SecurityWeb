package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.encryptMode.RSA;
import com.commenau.log.LogService;
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

@WebServlet("/keypair-manager")
public class KeyPairManagerController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/customer/dash-keypair-manager.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}

