package com.commenau.controller.customer;

import com.commenau.dto.ChatMessageDTO;
import com.commenau.log.LogService;
import com.commenau.model.LogLevel;
import com.commenau.model.Message;
import com.commenau.service.ConversationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@WebServlet("/message/*")
public class MessageController extends HttpServlet {
    @Inject
    ConversationService conversationService;
    @Inject
    LogService logService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] pathInfo = req.getPathInfo().split("/");
        int particapantId = Integer.valueOf(pathInfo[1]);
        int section = Integer.valueOf(req.getParameter("section"));
        List<ChatMessageDTO> chatMessageDTOS = conversationService.getMessages(particapantId , section);
        ObjectMapper json = new ObjectMapper();
        resp.setContentType("application/json");
        resp.getWriter().write(json.writeValueAsString(chatMessageDTOS));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] pathInfo = req.getPathInfo().split("/");
        int particapantId = Integer.valueOf(pathInfo[1]);
        int ownerId =  Integer.valueOf(req.getParameter("ownerId"));
        conversationService.updateViewed(particapantId , ownerId);
        Message message = conversationService.getMessage(particapantId);
        logService.save(LogLevel.INFO,"sendSuccess",message);
    }

}
