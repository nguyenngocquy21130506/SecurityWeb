package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.encryptMode.RSA;
import com.commenau.log.LogService;
import com.commenau.model.LogLevel;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.validate.Validator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.crypto.*;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
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
        // get user from session
        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);  // with case user's not exist in session, it's return null, and i didn't throw it
        if (Objects.isNull(user)) req.setAttribute("user-id", user.getId());
        req.getRequestDispatcher("/customer/dash-edit-profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);  // with case user's not exist in session, it's return null, and i didn't throw it
//        Map<Long, String> userKeys = (Map<Long,String>)req.getSession().getAttribute(SystemConstant.USER_KEYS);
//        userKeys.get(user.getId());

        BufferedReader reader = req.getReader();
        StringBuilder jsonStringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonStringBuilder.append(line);
        }
        String jsonString = jsonStringBuilder.toString();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        // Trích xuất formDataEncrypt từ JSON
        JsonObject formDataEncrypt = jsonObject.getAsJsonObject("formDataEncrypt");
        System.out.println("Received Encrypted Data: " + formDataEncrypt);

        // Lấy public key từ JSON
        String publicKeyString = formDataEncrypt.get("publicKey").getAsString().trim();

        // Chuyển đổi public key string thành định dạng chuẩn
        publicKeyString = publicKeyString.replaceAll("\\n", "\n"); // Đảm bảo rằng các ký tự xuống dòng được giữ lại
        publicKeyString = publicKeyString.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .trim(); // Xóa các phần không cần thiết

        PublicKey publicKey;
        try {
            // Giải mã chuỗi Base64 thành mảng byte
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyString);

            // Tạo đối tượng PublicKey từ mảng byte
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert string to PublicKey", e);
        }

        if (publicKey==null) return;

        User user = User.builder()
                .id(Long.parseLong(decrypt(formDataEncrypt.get("id").getAsString(), publicKey)))
                .phoneNumber(decrypt(formDataEncrypt.get("phoneNumber").getAsString(), publicKey))
                .firstName(decrypt(formDataEncrypt.get("firstName").getAsString(), publicKey))
                .lastName(decrypt(formDataEncrypt.get("lastName").getAsString(), publicKey))
                .address(decrypt(formDataEncrypt.get("address").getAsString(), publicKey))
                .build();
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
    }

    private boolean validate(User user) {
        return Validator.isEmpty(user.getFirstName()) || Validator.isEmpty(user.getLastName()) ||
                Validator.isEmpty(user.getAddress()) || !Validator.isValidPhoneNumber(user.getPhoneNumber());

    }

    public static String decrypt(String encryptedText, PublicKey publicKey) {
        // Chuyển đổi chuỗi base64 thành byte array
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

        // Khởi tạo cipher để giải mã với RSA
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            // Giải mã
            return new String(decryptedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}