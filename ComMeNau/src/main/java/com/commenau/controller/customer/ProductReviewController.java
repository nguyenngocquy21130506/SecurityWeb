package com.commenau.controller.customer;

import com.commenau.dao.ProductReviewDAO;
import com.commenau.model.User;
import com.commenau.service.ProductReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.xdevapi.JsonParser;
import com.mysql.cj.xdevapi.JsonValue;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/review")
public class ProductReviewController extends HttpServlet {
    @Inject
    ProductReviewService productReviewService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.valueOf(req.getParameter("id"));
        int size = Integer.valueOf(req.getParameter("size"));
        int page = Integer.valueOf(req.getParameter("page"));
        String sortBy = req.getParameter("sortBy");
        String sort = req.getParameter("sort");
        ObjectMapper json = new ObjectMapper();
        resp.setContentType("application/json");
        resp.getWriter().write(json.writeValueAsString(productReviewService.getPage(id, size, page, sortBy, sort)));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        if(req.getSession(false) != null && req.getSession(false).getAttribute("auth") != null ){
            int userId = Math.toIntExact(((User) req.getSession(false).getAttribute("auth")).getId());
            int productId = Integer.valueOf(req.getParameter("productId"));
            int rating = Integer.valueOf(req.getParameter("rating"));
            String content = req.getParameter("reviewer-text");

            productReviewService.save(productId,userId,rating,content);
            resp.setStatus(HttpServletResponse.SC_OK);

        }
        else {

            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
