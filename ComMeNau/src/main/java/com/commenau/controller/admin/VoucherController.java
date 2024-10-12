package com.commenau.controller.admin;

import com.commenau.dto.VoucherDTO;
import com.commenau.log.LogService;
import com.commenau.model.LogLevel;
import com.commenau.model.Voucher;
import com.commenau.service.UserService;
import com.commenau.service.VoucherService;
import com.commenau.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/vouchers")
public class VoucherController extends HttpServlet {
    @Inject
    VoucherService voucherService;
    @Inject
    LogService logService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("voucherActive", "");
        request.getRequestDispatcher("/admin/admin-voucher.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");
        String search = request.getParameter("search[value]").toLowerCase();
        String columnIndex = request.getParameter("order[i][column]")!=null ? request.getParameter("order[i][column]"):"0";
        String direction  = request.getParameter("order[i][dir]")!=null ? request.getParameter("order[i][dir]"):"0";;
        var listVoucher = voucherService.getAllVoucher();
        var filters = listVoucher
                .stream()
                .filter(voucher -> voucher.getName().toLowerCase().contains(search))
                .skip(Long.parseLong(start))
                .limit(Long.parseLong(length))
                .sorted(getComparator(columnIndex, direction))
                .toList();
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.addProperty("draw", Integer.parseInt(draw));
        json.addProperty("recordsTotal", listVoucher.size());
        json.addProperty("recordsFiltered", listVoucher.size());
        json.add("data", gson.toJsonTree(filters));
        response.getWriter().write(gson.toJson(json));
    }

    private Comparator<? super Voucher> getComparator(String columnIndex, String direction) {
        return switch (columnIndex){
            case "1" -> direction.equals("asc")?Comparator.comparing(Voucher::getName):Comparator.comparing(Voucher::getName).reversed();
            case "2" -> direction.equals("asc")?Comparator.comparing(Voucher::getDiscount):Comparator.comparing(Voucher::getDiscount).reversed();
            case "3" -> direction.equals("asc")?Comparator.comparing(Voucher::getStatus):Comparator.comparing(Voucher::getStatus).reversed();
            case "4" -> direction.equals("asc")?Comparator.comparing(Voucher::getCreatedAt):Comparator.comparing(Voucher::getCreatedAt).reversed();
            case "5" -> direction.equals("asc")?Comparator.comparing(Voucher::getExpriedAt):Comparator.comparing(Voucher::getExpriedAt).reversed();
            default -> direction.equals("asc")?Comparator.comparing(Voucher::getId):Comparator.comparing(Voucher::getId).reversed();
        };
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Đọc dữ liệu từ body của yêu cầu
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String body = sb.toString();
        // Chuyển đổi chuỗi JSON thành đối tượng Java
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(body, Map.class);
        int idVoucher = ((Double) map.get("idVoucher")).intValue(); // Chuyển đổi số thành int
        VoucherDTO voucherDTO = voucherService.getVoucherById(idVoucher);
        boolean delete = voucherService.deleteVoucher(idVoucher);
        if(delete){
            logService.save(LogLevel.WARNING,"deleteSuccess",voucherDTO);
        }
        // Trả về kết quả của phương thức xóa
        resp.getWriter().write(String.valueOf(delete));
    }
}
