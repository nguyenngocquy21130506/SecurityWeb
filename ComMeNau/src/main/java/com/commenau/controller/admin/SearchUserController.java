package com.commenau.controller.admin;

import com.commenau.service.ConversationService;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/chat/users/search")
public class SearchUserController extends HttpServlet {
    Gson gson = new Gson();
    @Inject
    ConversationService conversationService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("query");
        if(StringUtils.isBlank(query)) return;
        resp.getWriter().write(gson.toJson(conversationService.getUsersChatByRelativeName(query)));
    }
}
