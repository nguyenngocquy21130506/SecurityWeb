package com.commenau.service;

import com.commenau.dao.UserDAO;
import com.commenau.dto.ChatMessageDTO;
import com.commenau.encryptMode.RSA;
import com.commenau.model.ChatMessage;
import com.google.gson.Gson;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/chat/{userID}")
@Data
@Getter
@Setter
public class WebSocketService {
    Gson gson = new Gson();

    ConversationService conversationService = new ConversationService();

    UserDAO userDAO = new UserDAO();

    RSA rsa;

//    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
//
//    public static Set<Session> getSessions() {
//        return sessions;
//    }
//
//    // Thiết lập HttpSession vào WebSocket session
//    public static void setHttpSession(Session session, HttpSession httpSession) {
//        session.getUserProperties().put("httpSession", httpSession);
//    }
    @OnOpen
    public void onOpen(@PathParam("userID") final int userID, Session session) throws IOException {
//        sessions.add(session);
//        HttpSession httpSession = (HttpSession) session.getUserProperties().get("httpSession");
//
//        if (httpSession == null) {
//            System.err.println("HttpSession is null for userID: " + userID);
//            session.close();
//            return;
//        }
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

//        HttpSession httpSession = (HttpSession) session.getUserProperties().get("httpSession");
        try {
            System.out.println("enMessage: "+chatMessage.getMsg());
            rsa = new RSA();
            String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4WyW32YEXI+P7WG9WN9Y54EL1nh1emkJkT5DLoUPVMYEiZGo0ZFT0i5aTMYGenffqZbLCxUtI9eRgt9cEvo03w2zrw06RPy4HZFUBN42s1hNGBTrxYVL47g35nrlqTabGrjADRI86OusXWyy1HUudrsBTmBFwOrRTTxuG1tJTuYDJ6hN+gxmA+qidbnmxguJwimB/Rg8auUyAdZuUa8PLD0IP8hOFa+NPcBeKaGW9MQVsRaZzyG3uVWiBeBlZVJiBJNvZzF2R98xWsJyhAM+OhecmLF05NJQS+eZjsQrG8yTezsmfBKwBxcJ9OdNuO1zAU+81wrVy/lD/NrplLCIRQIDAQAB";
            rsa.setPublicKey(Base64.getDecoder().decode(publicKey.getBytes(StandardCharsets.UTF_8)));

            chatMessage.setMsg(new String(rsa.decrypt(chatMessage.getMsg())));
            System.out.println("message: "+chatMessage.getMsg());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
    }
}
