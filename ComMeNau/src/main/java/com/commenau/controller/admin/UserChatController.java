package com.commenau.controller.admin;

import com.commenau.service.ConversationService;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/chat/users")
public class UserChatController extends HttpServlet {
    Gson gson = new Gson();
    @Inject
    ConversationService conversationService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(conversationService.getUsersChat()));
    }
}
