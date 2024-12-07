package com.commenau.controller.customer;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class DownloadController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đường dẫn đến tệp trên máy chủ
        String filePath = getServletContext().getRealPath("/files/Signature_Tool_Group2.jar");

        // Thiết lập các header cho việc tải xuống
        File file = new File(filePath);
        if (file.exists()) {
            // Cài đặt kiểu MIME cho tệp .exe (tùy theo loại tệp bạn có thể thay đổi)
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            // Đọc tệp và ghi vào response output stream
            try (InputStream in = new FileInputStream(file);
                 OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } else {
            // Nếu tệp không tồn tại, trả về lỗi
            response.getWriter().write("Tệp không tìm thấy.");
        }
    }
}
