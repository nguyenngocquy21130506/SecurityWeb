package com.commenau.controller.customer;

import com.commenau.dto.SearchResultDTO;
import com.commenau.service.SearchService;
import com.google.gson.Gson;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/search")
public class SearchController extends HttpServlet {
    @Inject
    SearchService searchService;
    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
        if(StringUtils.isBlank(query)) return;
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.getWriter().write(gson.toJson(searchService.search(query)));
    }
}

@lombok.Data
@Builder
class Data {
    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private List<SearchResultDTO> data;
}
