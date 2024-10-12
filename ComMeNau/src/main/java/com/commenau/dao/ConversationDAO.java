package com.commenau.dao;

import com.commenau.model.ChatMessage;
import com.commenau.model.Conversation;
import com.commenau.model.Message;

import java.util.List;
import com.commenau.connectionPool.ConnectionPool;
public class ConversationDAO {
    public int getId(int participantId) {
        return ConnectionPool.getConnection().withHandle(n -> {

            boolean exists = n.createQuery("select count(*) from conversations where participantId = ?").bind(0, participantId).mapTo(Integer.class).one() > 0;
            if (!exists) {
                n.createUpdate("insert into conversations(participantId) values (?)").bind(0, participantId).execute();
            }
            return n.createQuery("select id from conversations where participantId = ?").bind(0, participantId).mapTo(Integer.class).stream().findFirst().get();
        });
    }

    public int saveMessage(int senderId, int receiverId, int conversationId, String msg) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createUpdate("insert into messages (conversationId , senderId , recipientId , content) values (? , ? ,? ,?) ").bind(0, conversationId).bind(1, senderId).bind(2, receiverId).bind(3, msg).execute();
        });
    }

    public List<Message> getMessages(int participantId, int section) {
        return ConnectionPool.getConnection().withHandle(n -> {
            String sql = "select senderId , viewed , content , sendTime from messages join conversations on messages.conversationId = conversations.id where participantId = ? order by sendTime desc limit 8 offset ?";
            return n.createQuery(sql).bind(0, participantId).bind(1, (section - 1) * 8).mapToBean(Message.class).stream().toList();
        });
    }

    public Message getLastMessage(int participantId) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createQuery("select senderId , viewed , content , sendTime from messages join conversations on messages.conversationId = conversations.id where participantId = ? order by sendTime desc limit 1")
                    .bind(0, participantId).mapToBean(Message.class).stream().findFirst().orElse(null);
        });
    }

    public void updateViewed(int participantId, int ownerId) {
        ConnectionPool.getConnection().withHandle(n -> {

            int conversationId = n.createQuery("select id from conversations where participantId = ?").bind(0, participantId).mapTo(Integer.class).stream().findFirst().orElse(0);

            Message message = n.createQuery("select id , senderId from messages where conversationId = ? order by sendTime desc limit 1").bind(0, conversationId).mapToBean(Message.class).stream().findFirst().orElse(new Message());
//            Message message = n.createQuery("select id , senderId from messages where conversationId = ? order by sendTime desc limit 1").bind(0, conversationId).mapToBean(Message.class).stream().findFirst().get();
            if (Math.toIntExact(message.getSenderId()) != ownerId) {
                return n.createUpdate("update messages set viewed = 1 where id = ?").bind(0, message.getId()).execute();
            }
            return null;
        });
    }

    public List<Conversation> getAllConversation() {
        return ConnectionPool.getConnection().withHandle((n -> {
            return n.createQuery("select * from conversations").mapToBean(Conversation.class).stream().toList();
        }));
    }


}
