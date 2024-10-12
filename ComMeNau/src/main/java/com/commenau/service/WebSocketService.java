package com.commenau.service;

import com.commenau.dao.UserDAO;
import com.commenau.dto.ChatMessageDTO;
import com.commenau.model.ChatMessage;
import com.google.gson.Gson;

import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Timestamp;

@ServerEndpoint("/chat/{userID}")
public class WebSocketService {
    Gson gson = new Gson();

    ConversationService conversationService = new ConversationService();

    UserDAO userDAO = new UserDAO();

    @OnOpen
    public void onOpen(@PathParam("userID") final int userID, Session session) {
        if (!userDAO.isAdmin(userID)) {
            session.getUserProperties().put("role", "customer");
            session.getUserProperties().put("userID", userID);
        } else {
            session.getUserProperties().put("role", "admin");
            session.getUserProperties().put("userID", userID);
        }
    }

    @OnMessage

    public void onMessage(String message, Session session) throws IOException {
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        String role = session.getUserProperties().get("role").toString();
        if (role.equalsIgnoreCase("customer")) {
            int senderId = (int) session.getUserProperties().get("userID");
            int conversationId = conversationService.getId(senderId);
            int executable = conversationService.saveMessage(senderId, 0, conversationId, chatMessage.getMsg());
            if (executable > 0) {
                for (var x : session.getOpenSessions()) {
                    if (x.getUserProperties().get("role").toString().equalsIgnoreCase("admin")) {
                        ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder().content(chatMessage.getMsg()).time(new Timestamp(System.currentTimeMillis())).viewed(false).senderId(senderId).build();
                        x.getBasicRemote().sendText(gson.toJson(chatMessageDTO));
                    }
                }
            }
        } else {
            int conversationId = conversationService.getId(chatMessage.getReceiverId());
            int executable = conversationService.saveMessage(0, chatMessage.getReceiverId(), conversationId, chatMessage.getMsg());
            for (var x : session.getOpenSessions()) {
                if (x.getUserProperties().get("userID").toString().equalsIgnoreCase(chatMessage.getReceiverId() + "")) {
                    if (executable > 0) {
                        ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder().content(chatMessage.getMsg()).time(new Timestamp(System.currentTimeMillis())).viewed(false).senderId(0).build();
                        x.getBasicRemote().sendText(gson.toJson(chatMessageDTO));
                    }
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        // Được gọi khi kết nối WebSocket bị đóng
    }
}
