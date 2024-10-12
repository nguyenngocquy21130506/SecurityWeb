package com.commenau.controller.admin;

import com.commenau.dto.RequestChartDTO;
import com.commenau.service.ChartService;
import com.commenau.service.ProductService;
import com.commenau.util.FormUtil;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/chartData")
public class ChartDataController extends HttpServlet {
    @Inject
    ProductService productService;
    @Inject
    ChartService chartService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setContentType("application/json");

        RequestChartDTO r = FormUtil.toModel(RequestChartDTO.class , req);
        Gson gson = new Gson();
        resp.getWriter().write(gson.toJson(chartService.getData(r.getSearch(),r.getCondition(),r.getDay())));
    }
}
