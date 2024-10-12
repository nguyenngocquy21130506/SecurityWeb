package com.commenau.mail;

import com.commenau.dao.UserDAO;
import com.commenau.dto.ContactDTO;
import com.commenau.model.*;
import com.commenau.util.RoundUtil;

import javax.inject.Inject;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MailService {
    /*
     * Mỗi khi gọi phương thức sendMail, một Runnable được gửi đến ExecutorService
     * để thực thi việc gửi email trên một thread riêng biệt. ExecutorService giúp
     * tạo và quản lý các thread để thực hiện công việc gửi email một cách bất
     * đồng bộ
     */
    @Inject
    UserDAO userDAO;
    private ExecutorService executorService = Executors.newFixedThreadPool(3); // Số lượng thread tùy chọn

    private String getBaseURL(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        // convert URL to URL obj
        URL fullUrl = null;
        try {
            fullUrl = new URL(url.toString());
            // get hostname
            String hostname = fullUrl.getProtocol() + "://" + fullUrl.getHost() + (fullUrl.getPort() != -1 ? ":" + fullUrl.getPort() : "");

            // get context path
            String contextPath = request.getContextPath();

            // generate base URL
            return hostname + contextPath;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void sendMailResetPassword(Map<User, String> map, HttpServletRequest request) {
        User user = null;
        String newPassword = null;
        for (Map.Entry<User, String> entry : map.entrySet()) {
            user = entry.getKey();
            newPassword = entry.getValue();
        }
        String subject = "Thông báo: Mật khẩu đã được đổi thành công";
        StringBuilder content = new StringBuilder();
        content.append("<p>Xin ch&agrave;o <strong>").append(user.getLastName()).append(" ").append(user.getFirstName()).append("</strong>.</p>").append(
                        "<p>Ch&uacute;ng t&ocirc;i đến từ <strong>ComMeNau.com</strong>, xin th&ocirc;ng b&aacute;o rằng mật khẩu của bạn đ&atilde; được đổi th&agrave;nh c&ocirc;ng.</p>")
                .append("<p>Mật khẩu mới của bạn l&agrave;:<strong>").append(newPassword).append("</strong></p>")
                .append("<p>&nbsp;Vui l&ograve;ng đăng nhập bằng mật khẩu mới n&agrave;y để tiếp tục sử dụng t&agrave;i khoản.</p>")
                .append("<p>Vui l&ograve;ng nhấn v&agrave;o link b&ecirc;n dưới để đăng nhập bằng mật khẩu mới:</p>")
                .append("<p><a href=\"").append(getBaseURL(request)).append("/dang-nhap\"><strong>Đăng nhập</strong></a></p>")
                .append("<p>Tr&acirc;n trọng, Đội ngũ quản trị vi&ecirc;n.</p>");
        sendMail(user.getEmail(), subject, content.toString());
    }

    public void sendMailVerifyUser(User user, EmailVerification verification, HttpServletRequest request) {
        String subject = "Thông báo: Đăng kí tài khoản thành công";
        StringBuilder content = new StringBuilder();
        content.append("<p>Ch&agrave;o bạn <em><strong>").append(user.getLastName()).append(" ").append(user.getFirstName()).append("</strong></em>,</p>")
                .append("<p>Xin ch&uacute;c mừng! Bạn đ&atilde; đăng k&yacute; th&agrave;nh c&ocirc;ng t&agrave;i khoản tr&ecirc;n website <strong>ComMeNau.com</strong>. ")
                .append("Để ho&agrave;n tất qu&aacute; tr&igrave;nh đăng k&yacute; v&agrave; x&aacute;c thực t&agrave;i khoản, vui l&ograve;ng nhấp v&agrave;o đường dẫn dưới đ&acirc;y:</p>")
                .append("<p><a href=\"").append(getBaseURL(request)).append("/verify-users?userId=").append(user.getId())
                .append("&token=").append(verification.getToken()).append("\"")
                .append("><strong>Nhấn v&agrave;o đ&acirc;y để x&aacute;c thực.</strong></a></p>")
                .append("<p>Đường dẫn tr&ecirc;n sẽ đưa bạn đến trang x&aacute;c thực t&agrave;i khoản của ch&uacute;ng t&ocirc;i.&nbsp;</p>")
                .append("<p>Tr&acirc;n trọng,</p><p>Đội ngũ hỗ trợ <strong>ComMeNau.com</strong>.</p>");
        sendMail(user.getEmail(), subject, content.toString());
    }

    public void sendMailReplyContact(ReplyContact replyContact, ContactDTO contact) {
        sendMail(contact.getEmail(), replyContact.getTitle(), replyContact.getContent());
    }

    public void sendMailToAdmin(String content) {
        String subject = "Warning";
        StringBuilder send = new StringBuilder();
        send.append("<p>Cảnh báo từ hệ thống phát hiện: ").append(content).append("</p>");
        for (var x : userDAO.getAllAdminEmail()) {
            sendMail(x, subject, send.toString());
        }
    }

    public void sendNotificationToUser(User user, String content) {
        String subject = "Warning";
        StringBuilder send = new StringBuilder();
        send.append("<p>Thông báo từ hệ thống : ").append(content).append("</p>");
        sendMail(user.getEmail(), subject, send.toString());
    }

    public void sendMailInvoice(Invoice invoice, Map<Product, Integer> map) {
        double totalPrice = 0;
        int index = 1;
        StringBuilder content = new StringBuilder();
        content.append("<p>Ch&agrave;o <em><strong>").append(invoice.getFullName()).append("</strong></em>,</p>");
        content.append("<p>Ch&uacute;ng t&ocirc;i xin th&ocirc;ng b&aacute;o rằng đơn h&agrave;ng của bạn đ&atilde; ");
        content.append("được xử l&yacute; th&agrave;nh c&ocirc;ng v&agrave; đang được chuẩn bị giao đến địa chỉ nhận h&agrave;ng sau đ&acirc;y:</p>");
        content.append("<p>Địa chỉ nhận h&agrave;ng: ").append(invoice.getAddress()).append("</p>");
        content.append("<p>Số điện thoại li&ecirc;n hệ: ").append(invoice.getPhoneNumber()).append("</p>");
        content.append("<p>Dưới đ&acirc;y l&agrave; th&ocirc;ng tin chi tiết về đơn h&agrave;ng của bạn:</p>");
        content.append("<p>Danh s&aacute;ch sản phẩm đ&atilde; đặt h&agrave;ng:</p>");
        content.append("<table style=\"width: 442px;\"><tbody>");
        content.append("<tr style=\"height: 13.2px;\">");
        content.append("<td style=\"width: 45px; height: 13.2px; text-align: center;\">STT</td>");
        content.append("<td style=\"width: 191.463px; height: 13.2px;\">T&ecirc;n sản phẩm</td>");
        content.append("<td style=\"width: 111.537px; height: 13.2px; text-align: center;\">Gi&aacute;&nbsp;</td>");
        content.append("<td style=\"width: 101px; height: 13.2px; text-align: center;\">Số lượng</td>");
        content.append("</tr>");
        for (Map.Entry<Product, Integer> entry : map.entrySet()) {
            long price = RoundUtil.roundPrice(entry.getKey().getPrice() * (1 - entry.getKey().getDiscount()));
            content.append("<tr style=\"height: 13px;\">");
            content.append("<td style=\"width: 45px; height: 13px; text-align: center;\">").append(index++).append("</td>");
            content.append("<td style=\"width: 191.463px; height: 13px;\">").append(entry.getKey().getName()).append("</td>");
            content.append("<td style=\"width: 111.537px; height: 13px; text-align: center;\">").append(new DecimalFormat("#,### VNĐ").format(price)).append("</td>");
            content.append("<td style=\"width: 101px; height: 13px; text-align: center;\">").append(entry.getValue()).append("</td>");
            content.append("</tr>");
            totalPrice += price * entry.getValue();
        }
        content.append("</tbody></table>");
        content.append("<p>Tổng số tiền: <strong>").append(new DecimalFormat("#,### VNĐ").format(RoundUtil.roundPrice(totalPrice))).append("</strong></p>");
        content.append("<p>Hình thức thanh toán: ").append(invoice.getPaymentMethod()).append("</p>");
        content.append("<p>Ch&uacute;ng t&ocirc;i rất cảm ơn sự tin tưởng v&agrave; ủng hộ của bạn. ");
        content.append("Nếu bạn c&oacute; bất kỳ c&acirc;u hỏi hoặc y&ecirc;u cầu hỗ trợ n&agrave;o, vui l&ograve;ng li&ecirc;n hệ với ch&uacute;ng t&ocirc;i.</p>");
        content.append("<p>Tr&acirc;n trọng,<strong>&nbsp;ComMeNau.com</strong></p>");
        sendMail(invoice.getEmail(), "Xác nhận đặt hàng thành công", content.toString());
    }

    private void sendMail(String to, String subject, String content) {
        executorService.submit(() -> {
            // 1) get the session object
            Properties props = new Properties();
            props.put("mail.smtp.auth", MailProperties.auth);
            props.put("mail.smtp.starttls.enable", MailProperties.starttls);

            props.put("mail.smtp.host", MailProperties.host);
            props.put("mail.smtp.socketFactory.port", MailProperties.port);
            props.put("mail.smtp.port", MailProperties.port);
            Session session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(MailProperties.user, MailProperties.password);
                }
            });

            // 2) compose message
            try {
                MimeMessage message = new MimeMessage(session);
                message.addHeader("Context-type", "text/HTML; charset=UTF-8");
                message.setFrom(new InternetAddress(MailProperties.user));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject(subject, "UTF-8");
                // 3) create HTML content
                message.setContent(content, "text/HTML; charset=UTF-8");
                // 4) send message
                Transport.send(message);
                System.out.println("Message sent successfully");
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
        });
    }


}
