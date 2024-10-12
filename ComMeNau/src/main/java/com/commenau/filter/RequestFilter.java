package com.commenau.filter;

import com.commenau.dao.LogDao;
import com.commenau.log.Log;
import com.commenau.log.LogService;
import com.commenau.mail.MailService;
import com.commenau.model.LogLevel;
import com.commenau.model.Rule;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@WebFilter(value = {"/login", "/payments", "/invoice-details", "/change-profile",
        "/change-password", "/wishlist", "/blog-detail", "/admin/updateUser",
        "/admin/invoiceDetail", "/admin/blog-detail", "/admin/voucher-detail",
        "/admin/products", "/admin/product-detail","/message/*","/contacts","/admin/categories"
        })
public class RequestFilter implements Filter {

    private static ThreadLocal<HttpServletRequest> currentRequest = new ThreadLocal<>();
    private static ThreadLocal<List<Log>> currentLogs = new ThreadLocal<>();
    @Inject
    LogDao logDao;
    @Inject
    MailService mailService;
    @Inject
    LogService logService;
    private Map<String, Rule> rules = new HashMap<>();

    {
        Map<String, String> dataLoginRule = new HashMap<>();
        dataLoginRule.put("endpoint", "/login");
        Rule loginRule = Rule.builder().sql("select 1 from logs where endpoint like :endpoint and address like :address group by Date(createAt) having count(*) >= 3")
                .alter("Spam login")
                .data(dataLoginRule)
                .build();
        rules.put("/login", loginRule);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            currentRequest.set(httpRequest);
            currentLogs.set(new ArrayList<>());
        }
//
//        // Tiếp tục chuỗi filter
        chain.doFilter(request, response);

        for (var x : getCurrentLogs()) {
            if (x.getStatus().equalsIgnoreCase("failure")) {
                for (var y : rules.entrySet()) {
                    if (Pattern.matches(y.getKey(), getCurrentRequest().getServletPath())) {
                        if (getCurrentRequest().getHeader("X-FORWARDED-FOR") != null && logDao.checkWarning(y.getValue(), getCurrentRequest().getHeader("X-FORWARDED-FOR"))) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mailService.sendMailToAdmin(y.getValue().getAlter());
                                    Map<String, String> alter = new HashMap<>();
                                    alter.put("Noti", y.getValue().getAlter());
                                    logService.save(LogLevel.WARNING, "failure", alter);
                                }
                            }).start();
                        }
                    }
                }
            }
            break;
        }


        currentRequest.remove();
        currentLogs.remove();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần thực hiện gì trong phương thức này
    }

    @Override
    public void destroy() {
        // Không cần thực hiện gì trong phương thức này
    }

    public static HttpServletRequest getCurrentRequest() {
        return currentRequest.get();
    }

    public static List<Log> getCurrentLogs() {
        return currentLogs.get();
    }

    public static void addLog(Log log) {
        getCurrentLogs().add(log);
    }

}
