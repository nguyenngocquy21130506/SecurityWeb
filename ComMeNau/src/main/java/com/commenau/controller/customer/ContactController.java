package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.encryptMode.DESEncryption;
import com.commenau.log.LogService;
import com.commenau.model.Contact;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.ContactService;
import com.commenau.util.FormUtil;
import com.commenau.util.HttpUtil;
import com.commenau.validate.Validator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@WebServlet("/contacts")
public class ContactController extends HttpServlet {
    @Inject
    private ContactService service;
    @Inject
    private LogService logService;

    @Inject
    private DESEncryption desEncryption;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currentPage = request.getRequestURL().toString();
        try {
            desEncryption = new DESEncryption();
            request.setAttribute("secretKey", desEncryption.getSecretKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        request.getSession().setAttribute(SystemConstant.PRE_PAGE, currentPage);
        request.getRequestDispatcher("/customer/contact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(request.getReader());

        JsonNode enFormData = jsonNode.get("enFormData");
        String secretKey = desEncryption.getSecretKey();

        String enFullname = enFormData.get("enFullname") != null ? enFormData.get("enFullname").asText() : null;
        String enEmail = enFormData.get("enEmail") != null ? enFormData.get("enEmail").asText() : null;
        String enMessage = enFormData.get("enMessage") != null ? enFormData.get("enMessage").asText() : null;

        String enFullname1 = enFullname.substring(10);
        String enEmail1 = enEmail.substring(10);
        String enMessage1 = enMessage.substring(10);

        DESEncryption desEncryption;
        try {
            desEncryption = new DESEncryption();
            // Giải mã secretKey từ Base64
            byte[] decodedKey = Base64.getDecoder().decode(secretKey);
            desEncryption.setSecretKey(decodedKey); // Truyền mảng byte cho phương thức setSecretKey
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return; // Trả về lỗi nếu có vấn đề với secretKey
        }

        String fullname = null;
        String email = null;
        String message = null;
        try {
            fullname = desEncryption.decrypt(enFullname);
            email = desEncryption.decrypt(enEmail);
            message = desEncryption.decrypt(enMessage);
        } catch (Exception e) {
            // Xử lý ngoại lệ có thể xảy ra
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Kiểm tra và lưu contact
        Contact contact = Contact.builder()
                .fullName(fullname)
                .email(email)
                .message(message)
                .build();
        boolean hasError = validate(contact);
        if (!hasError) {
            User user = (User) request.getSession().getAttribute(SystemConstant.AUTH);
            user = (user == null) ? new User() : user;
            contact.setUserId(user.getId());
            if (service.insert(contact)) {
                logService.save(LogLevel.INFO, "sendSuccess", contact);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
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
