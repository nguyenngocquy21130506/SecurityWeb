package com.commenau.dao;

import com.commenau.dto.ContactDTO;
import com.commenau.model.Contact;
import com.commenau.model.ReplyContact;
import com.commenau.paging.PageRequest;
import com.commenau.util.PagingUtil;
import org.jdbi.v3.core.statement.Update;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.commenau.connectionPool.ConnectionPool;
public class ContactDAO {
    public int countAll() {
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery("SELECT COUNT(id) FROM contacts")
                        .mapTo(Integer.class).one()
        );
    }

    public int countByKeyWord(String keyWord) {
        String sql = "SELECT COUNT(id) FROM contacts WHERE fullName LIKE :keyWord " +
                "OR email LIKE :keyWord OR message LIKE :keyWord OR CONVERT(createdAt, CHAR) LIKE :keyWord";
        return ConnectionPool.getConnection().withHandle(handle ->
                        handle.createQuery(sql))
                .bind("keyWord", "%" + keyWord + "%")
                .mapTo(Integer.class).stream().findFirst().orElse(0);
    }

    public ContactDTO findOneById(int contactId) {
        String sql = "SELECT c.id AS contactId, fullName, email, message, r.id AS replyId,content,title " +
                "FROM contacts c LEFT JOIN reply_contacts r ON r.contactId = c.id WHERE c.id=:id";
        Optional<ContactDTO> contact = ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql).bind("id", contactId)
                        .map((rs, ctx) -> {
                            ReplyContact reply = null;
                            int replyId = rs.getInt("replyId");
                            if (replyId != 0)
                                reply = ReplyContact.builder().id(replyId)
                                        .title(rs.getString("title"))
                                        .content(rs.getString("content"))
                                        .build();

                            return ContactDTO.builder()
                                    .id(rs.getInt("contactId"))
                                    .fullName(rs.getString("fullName"))
                                    .email(rs.getString("email"))
                                    .message(rs.getString("message"))
                                    .reply(reply).build();
                        }).stream().findFirst());
        return contact.orElse(new ContactDTO());
    }

    public List<ContactDTO> findAll(PageRequest pageRequest) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT c.id AS contactId, fullName, email, message, createdAt, r.id AS replyId ");
        builder.append("FROM contacts c LEFT JOIN reply_contacts r ON r.contactId = c.id ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql).map((rs, ctx) -> {
                    int replyId = rs.getInt("replyId");
                    ReplyContact reply = (replyId == 0) ? null : ReplyContact.builder().id(replyId).build();
                    return ContactDTO.builder()
                            .id(rs.getInt("contactId"))
                            .fullName(rs.getString("fullName"))
                            .email(rs.getString("email"))
                            .message(rs.getString("message"))
                            .createdAt(rs.getTimestamp("createdAt"))
                            .reply(reply).build();
                }).stream().toList());
    }

    public List<ContactDTO> findByKeyWord(PageRequest pageRequest, String keyWord) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT c.id AS contactId, fullName, email, message,createdAt, r.id AS replyId ");
        builder.append("FROM contacts c LEFT JOIN reply_contacts r ON r.contactId = c.id ");
        builder.append("WHERE fullName LIKE :keyWord OR email LIKE :keyWord OR message LIKE :keyWord ");
        builder.append("OR CONVERT(createdAt, CHAR) LIKE :keyWord ");
        String sql = PagingUtil.appendSortersAndLimit(builder, pageRequest);
        return ConnectionPool.getConnection().withHandle(handle ->
                handle.createQuery(sql).bind("keyWord", "%" + keyWord + "%")
                        .map((rs, ctx) -> {
                            int replyId = rs.getInt("replyId");
                            ReplyContact reply = (replyId == 0) ? null : ReplyContact.builder().id(replyId).build();
                            return ContactDTO.builder()
                                    .id(rs.getInt("contactId"))
                                    .fullName(rs.getString("fullName"))
                                    .email(rs.getString("email"))
                                    .message(rs.getString("message"))
                                    .createdAt(rs.getTimestamp("createdAt"))
                                    .reply(reply).build();
                        }).stream().toList());
    }

    public boolean save(Contact contact) {
        String sql = "INSERT INTO contacts(fullName, email, message, userId) VALUES (:fullName, :email, :message, :userId)";
        int result = ConnectionPool.getConnection().inTransaction(handle -> {
            Update update = handle.createUpdate(sql)
                    .bind("fullName", contact.getFullName())
                    .bind("email", contact.getEmail())
                    .bind("message", contact.getMessage());
            if (contact.getUserId() != 0) {
                update.bind("userId", contact.getUserId());
            } else {
                update.bindNull("userId", Types.INTEGER);
            }
            return update.execute();
        });
        return result > 0;
    }

    public boolean saveReply(ReplyContact replyContact) {
        String sql = "INSERT INTO reply_contacts(contactId, title, content) VALUES (:contactId,:title,:content)";
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate(sql)
                        .bind("contactId", replyContact.getContactId())
                        .bind("title", replyContact.getTitle())
                        .bind("content", replyContact.getContent()).execute()
        );
        return result > 0;
    }

    public boolean deleteByIds(Integer[] ids) {
        int result = ConnectionPool.getConnection().inTransaction(handle -> {
            return handle.createUpdate("DELETE FROM contacts WHERE id IN (<ids>)")
                    .bindList("ids", Arrays.asList(ids))
                    .execute();

        });
        return result > 0;
    }


    public boolean deleteReplyByContactId(Integer contactId) {
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate("DELETE FROM reply_contacts WHERE contactId =:contactId")
                        .bind("contactId", contactId)
                        .execute()

        );
        return result > 0;
    }

    public boolean deleteById(Integer id) {
        int result = ConnectionPool.getConnection().inTransaction(handle ->
                handle.createUpdate("DELETE FROM contacts WHERE id =:id")
                        .bind("id", id)
                        .execute()

        );
        return result > 0;
    }
}
