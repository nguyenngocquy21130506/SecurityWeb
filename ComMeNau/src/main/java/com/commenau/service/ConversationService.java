package com.commenau.service;

import com.commenau.dao.ConversationDAO;
import com.commenau.dao.UserDAO;
import com.commenau.dto.ChatMessageDTO;
import com.commenau.model.Conversation;
import com.commenau.model.Message;
import com.commenau.model.User;
import com.commenau.model.UserChatDTO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ConversationService {

    ConversationDAO conversationDAO = new ConversationDAO();

    UserDAO userDAO = new UserDAO();

    public int getId(int participantId) {
        return conversationDAO.getId(participantId);
    }

    public int saveMessage(int senderId, int receiverId, int conversationId, String msg) {
        return conversationDAO.saveMessage(senderId, receiverId, conversationId, msg);
    }

    public List<UserChatDTO> getUsersChatByRelativeName(String query) {
        List<Conversation> conversations = conversationDAO.getAllConversation();
        List<UserChatDTO> userChatDTOS = new ArrayList<>();
        for (var x : conversations) {
            UserChatDTO userChatDTO = UserChatDTO.builder().build();
            User user = userDAO.getFirstNameAndLastName((long) x.getParticipantId());
            userChatDTO.setId(x.getParticipantId());
            userChatDTO.setName(user.getLastName() + " " + user.getFirstName());
            if (userChatDTO.getName().toLowerCase().contains(query.toLowerCase())) {
                userChatDTOS.add(userChatDTO);
            }
        }
        return userChatDTOS;
    }

    public List<UserChatDTO> getUsersChat() {
        List<Conversation> conversations = conversationDAO.getAllConversation();
        List<UserChatDTO> userChatDTOS = new ArrayList<>();

        for (var x : conversations) {
            UserChatDTO userChatDTO = UserChatDTO.builder().build();
            Message lastMessage = conversationDAO.getLastMessage(x.getParticipantId());
            if (lastMessage == null) continue;
            User user = userDAO.getFirstNameAndLastName((long) x.getParticipantId());

            ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder().build();
            chatMessageDTO.setSenderId(lastMessage.getSenderId());
            chatMessageDTO.setTime(lastMessage.getSendTime());
            chatMessageDTO.setContent(lastMessage.getContent());
            chatMessageDTO.setViewed(lastMessage.isViewed());


            userChatDTO.setId(x.getParticipantId());
            userChatDTO.setName(user.getLastName() + " " + user.getFirstName());
            userChatDTO.setMessage(chatMessageDTO);
            userChatDTOS.add(userChatDTO);
        }


        return userChatDTOS.stream().sorted(Comparator.comparing(n -> n.getMessage().getTime())).toList();
    }

    public List<ChatMessageDTO> getMessages(int participantId, int section) {
        List<ChatMessageDTO> chatMessageDTOS = new ArrayList<>();

        List<Message> messages = conversationDAO.getMessages(participantId, section);


        for (var x : messages) {
            ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder().build();
            chatMessageDTO.setSenderId(x.getSenderId());
            chatMessageDTO.setTime(x.getSendTime());
            chatMessageDTO.setContent(x.getContent());
            chatMessageDTO.setViewed(x.isViewed());
            chatMessageDTOS.add(chatMessageDTO);
        }

        return chatMessageDTOS;
    }

    public void updateViewed(int particapantId, int ownerId) {
        conversationDAO.updateViewed(particapantId, ownerId);
    }
    public Message getMessage(int particapantId){
        return conversationDAO.getLastMessage(particapantId);
    }
}
