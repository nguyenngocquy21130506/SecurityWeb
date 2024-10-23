package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.log.LogService;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.util.AESKeyUtil;
import com.commenau.util.FormUtil;
import com.commenau.util.HttpUtil;
import com.commenau.validate.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
import java.util.Objects;

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

        String secretKeyStr = (String) getServletContext().getAttribute("SECRET_KEY");
        SecretKey secretKey = AESKeyUtil.getAESKeyFromString(secretKeyStr);

        // Lấy dữ liệu đã mã hóa từ form
//        String encryptedText = req.getParameter("encryptedData");
        String encryptedText = HttpUtil.of(req.getReader()).toModel(Map.class).get("encryptedData").toString();
        if (!Objects.isNull(encryptedText) && !encryptedText.isBlank()) {
            System.out.println("Get Encrypted Text from Ajax:\n\t" + encryptedText);

            String decryptedData = decryptAES(encryptedText, secretKey);

            User user = new ObjectMapper().readValue(decryptedData, User.class);

//            User user = HttpUtil.of(req.getReader()).toModel(User.class);
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
                if (result) {
                    logService.save(LogLevel.INFO, "success", preData, data);
                } else {
                    logService.save(LogLevel.WARNING, "failed", preData, data);
                }
                req.getSession().setAttribute(SystemConstant.AUTH, user);
                resp.setStatus(result ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private boolean validate(User user) {
        return Validator.isEmpty(user.getFirstName()) || Validator.isEmpty(user.getLastName()) ||
                Validator.isEmpty(user.getAddress()) || !Validator.isValidPhoneNumber(user.getPhoneNumber());

    }

    private String decryptAES(String encryptedData, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedValue = cipher.doFinal(decodedValue);
            return new String(decryptedValue);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi giải mã", e);
        }
    }
}
