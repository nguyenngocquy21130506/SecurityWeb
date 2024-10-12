package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.log.LogService;
import com.commenau.model.Contact;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.ContactService;
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

@WebServlet("/contacts")
public class ContactController extends HttpServlet {
    @Inject
    private ContactService service;
    @Inject
    private LogService logService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentPage = request.getRequestURL().toString();
        request.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        request.getRequestDispatcher("/customer/contact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Contact contact = HttpUtil.of(request.getReader()).toModel(Contact.class);
        boolean hasError = validate(contact);
        if (!hasError) {
            User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
            user = (user == null) ? new User() : user;
            contact.setUserId(user.getId());
            if (service.insert(contact)) {
                logService.save(LogLevel.INFO, "sendSuccess", contact);
                response.setStatus(HttpServletResponse.SC_OK);
            }else
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    private boolean validate(Contact contact) {
        if (!Validator.isValidEmail(contact.getEmail()) || Validator.isEmpty(contact.getFullName())
                || Validator.isEmpty(contact.getMessage()))
            return true;
        return false;
    }
}
