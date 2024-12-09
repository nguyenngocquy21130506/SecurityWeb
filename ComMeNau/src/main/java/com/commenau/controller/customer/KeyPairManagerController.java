package com.commenau.controller.customer;

import com.commenau.constant.SystemConstant;
import com.commenau.dao.PublicKeyDao;
import com.commenau.encryptMode.RSA;
import com.commenau.log.LogService;
import com.commenau.model.PublicKeyEntity;
import com.commenau.model.User;
import com.commenau.service.UserService;
import com.commenau.validate.Validator;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/keypair-manager")
public class KeyPairManagerController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = (String) req.getParameter("action");
        if (action == null)
            req.getRequestDispatcher("/customer/dash-keypair-manager.jsp").forward(req, resp);
        else {
            try {
                switch (action) {
                    case "create-keypair":
                        createKeyPair(req, resp);
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void createKeyPair(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        RSA rsa = new RSA();

        String fileName = UUID.randomUUID().toString();

        // Lưu vào cơ sở dữ liệu
        PublicKeyDao publicKeyDao = new PublicKeyDao();
        boolean isInserted = publicKeyDao.save(
                PublicKeyEntity.builder()
                        .publicKey(Base64.getEncoder().encodeToString(rsa.getPublicKey().getEncoded()))
                        .expired(LocalDateTime.now().plusHours(1)) // Đặt hạn 1 tiếng
                        .status((byte) 1) // 1 có thể là trạng thái "active"
                        .userId(getCurrentUserId(req)) // Phương thức để lấy user_id hiện tại
                        .name(fileName)
                        .build());

        // Khởi tạo đối tượng Map để chứa thông tin trả về
        Map<String, Object> responseData = new HashMap<>();

        if (isInserted) {
            // Nếu lưu thành công, thêm các thông tin vào Map
            responseData.put("status", "success");
            responseData.put("message", "Khóa công khai đã được tạo và lưu thành công.");
            responseData.put("privateKey", Base64.getEncoder().encodeToString(rsa.getPrivateKey().getEncoded()));
//            responseData.put("publicKey", Base64.getEncoder().encodeToString(rsa.getPublicKey().getEncoded()));
            responseData.put("filename", fileName);
            responseData.put("expired", LocalDateTime.now().plusHours(1).toString());

        } else {
            // Nếu không thành công, thông báo lỗi
            responseData.put("status", "error");
            responseData.put("message", "Lỗi khi chèn khóa công khai vào cơ sở dữ liệu. Vui lòng kiểm tra kết nối cơ sở dữ liệu và thử lại.");
        }

        // Chuyển Map thành JSON và gửi lại cho client
        String jsonResponse = new Gson().toJson(responseData);

        // Cấu hình kiểu trả về là JSON
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.write(jsonResponse);
        out.close();
    }

    private long getCurrentUserId(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute(SystemConstant.AUTH);
        return user == null ? -1 : user.getId();
    }


}

