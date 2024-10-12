package com.commenau.controller.admin;

import com.commenau.dto.RequestChartDTO;
import com.commenau.service.ExcelService;
import com.commenau.util.FormUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet("/admin/export")
public class ExportController extends HttpServlet {
    @Inject
    ExcelService excelService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

        RequestChartDTO dto = FormUtil.toModel(RequestChartDTO.class, req);
        File file = excelService.getFile(dto , getServletContext().getRealPath("/"));
        // Tạo một file tạm thời
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
}
