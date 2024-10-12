package com.commenau.controller.admin;

import com.commenau.dto.LogDTO;
import com.commenau.log.LogService;
import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

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

@WebServlet("/admin/logs")
public class LogController extends HttpServlet {
    @Inject
    LogService logService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("logActive", "");
        req.getRequestDispatcher("/admin/admin-log.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        BufferedReader reader = request.getReader();
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

        String level = jsonObject.get("level").getAsString();

        int draw = jsonObject.get("draw").getAsInt();
        int start = jsonObject.get("start").getAsInt();
        int length = jsonObject.get("length").getAsInt();
        String search = jsonObject.getAsJsonObject("search").get("value").getAsString();
        // Lấy order từ JsonObject
        JsonArray order = jsonObject.getAsJsonArray("order");
        int columnIndex = order.get(0).getAsJsonObject().get("column").getAsInt();
        String direction = order.get(0).getAsJsonObject().get("dir").getAsString();
//        String columnIndex = request.getParameter("order[i][column]") != null ? request.getParameter("order[i][column]") : "0";
//        String direction = request.getParameter("order[i][dir]") != null ? request.getParameter("order[i][dir]") : "0";
        List<LogDTO> listLog;
        if(!level.equals("")){
            listLog = logService.getLogForLevel(Integer.parseInt(level));
        }else{
            listLog = logService.getAllLog();
        }
        List<LogDTO> filters = listLog
                .stream()
                .filter(log -> log.getStatus().contains(search))
                .skip(start)
                .limit(length)
                .sorted(getComparator(String.valueOf(columnIndex), direction))
                .toList();
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.addProperty("draw", draw);
        json.addProperty("recordsTotal", listLog.size());
        json.addProperty("recordsFiltered", listLog.size());
        json.add("data", gson.toJsonTree(filters));
        response.getWriter().write(gson.toJson(json));
    }

    private Comparator<? super LogDTO> getComparator(String columnIndex, String direction) {
        return switch (columnIndex) {
            case "1" ->
                    direction.equals("asc") ? Comparator.comparing(LogDTO::getEndpoint) : Comparator.comparing(LogDTO::getEndpoint).reversed();
            case "2" ->
                    direction.equals("asc") ? Comparator.comparing(LogDTO::getIpAddress) : Comparator.comparing(LogDTO::getIpAddress).reversed();
            case "3" ->
                    direction.equals("asc") ? Comparator.comparing(LogDTO::getStatus) : Comparator.comparing(LogDTO::getStatus).reversed();
            case "4" ->
                    direction.equals("asc") ? Comparator.comparing(LogDTO::getLevel) : Comparator.comparing(LogDTO::getLevel).reversed();
            case "5" ->
                    direction.equals("asc") ? Comparator.comparing(LogDTO::getPreValue) : Comparator.comparing(LogDTO::getPreValue).reversed();
            default ->
                    direction.equals("asc") ? Comparator.comparing(LogDTO::getValue) : Comparator.comparing(LogDTO::getValue).reversed();
        };
    }
}
