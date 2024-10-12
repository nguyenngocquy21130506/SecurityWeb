package com.commenau.controller.admin;

import com.commenau.dto.ContactDTO;
import com.commenau.mail.MailService;
import com.commenau.model.ReplyContact;
import com.commenau.paging.PageRequest;
import com.commenau.paging.Sorter;
import com.commenau.service.ContactService;
import com.commenau.util.FormUtil;
import com.commenau.util.HttpUtil;
import com.commenau.util.PagingUtil;
import com.commenau.validate.Validator;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/contacts")
public class ContactController extends HttpServlet {
    @Inject
    private ContactService contactService;
    @Inject
    private MailService mailService;
    private static final int maxPageItem = 6;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contactIdStr = request.getParameter("contactId");
        if (contactIdStr != null) {
            displayReply(contactIdStr, request, response);
        } else {
            displayListContacts(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReplyContact replyContact = FormUtil.toModel(ReplyContact.class, request);
        boolean hasError = validate(replyContact);
        if (!hasError) {
            boolean result = contactService.saveReply(replyContact);
            ContactDTO contact = contactService.getById(replyContact.getContactId());
            mailService.sendMailReplyContact(replyContact, contact);
            response.setStatus(result ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }else response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer[] ids = HttpUtil.of(request.getReader()).toModel(Integer[].class);
        boolean isSuccess = contactService.deleteByIds(ids);
        response.setStatus(isSuccess ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private void displayReply(String contactIdStr, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int contactId = 0;
        try {
            contactId = Integer.parseInt(contactIdStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        ContactDTO contact = contactService.getById(contactId);
        request.setAttribute("contactActive", "");
        request.setAttribute("contact", contact);
        request.getRequestDispatcher("/admin/admin-reply-contact.jsp").forward(request, response);
    }

    private void displayListContacts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageStr = request.getParameter("page");
        String keyWord = request.getParameter("keyWord");

        //paging
        int totalItem = contactService.countByKeyWord(keyWord);
        int maxPage = PagingUtil.maxPage(totalItem, maxPageItem);
        int page = PagingUtil.convertToPageNumber(pageStr, maxPage);
        //sort
        List<Sorter> sorters = List.of(new Sorter("replyId", "ASC"),
                new Sorter("createdAt", "DESC"));
        PageRequest pageRequest = PageRequest.builder().page(page).maxPageItem(maxPageItem)
                .sorters(sorters).build();

        request.setAttribute("maxPage", maxPage);
        request.setAttribute("page", page);
        request.setAttribute("contactActive", "");
        request.setAttribute("keyWord", keyWord);
        request.setAttribute("contacts", contactService.getByKeyWord(pageRequest, keyWord));
        request.getRequestDispatcher("/admin/admin-contact.jsp").forward(request, response);
    }

    private boolean validate(ReplyContact reply) {
        return Validator.isEmpty(reply.getContent()) || Validator.isEmpty(reply.getTitle());
    }
}
