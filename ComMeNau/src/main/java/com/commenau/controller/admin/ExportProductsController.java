package com.commenau.controller.admin;

import com.commenau.service.ExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@WebServlet("/admin/export/products")
@MultipartConfig
public class ExportProductsController extends HttpServlet {
    @Inject
    ExcelService excelService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        File file = excelService.getFileProducts(getServletContext().getRealPath("/"));
        if (file == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("error");
            return;
        }

        // Trả về đường dẫn của file Excel đã tạo
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"url\": \"" + req.getContextPath() + "/" + file.getName() + "\"}");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("file"); // Lấy file từ form
        InputStream fileContent = filePart.getInputStream();
        StringBuilder fileData = new StringBuilder();
        Workbook workbook = new XSSFWorkbook(fileContent);
        excelService.updateProducts(workbook);
        resp.sendRedirect("/admin/products");
    }
}
