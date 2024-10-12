package com.commenau.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebFilter("/login")
public class IPBlockFilter implements Filter {
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private final Map<String, Integer> loginAttempts = new HashMap<>(); // số lần login fail của mỗi IP
    private ScheduledExecutorService scheduler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            synchronized (loginAttempts) {
                loginAttempts.clear(); // Đặt lại bộ đếm mỗi ngày
            }
        }, 0, 1, TimeUnit.DAYS); // Thực hiện mỗi ngày
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        loginAttempts.clear();
        String check = request.getParameter("checkIp");
        if (check != null && check.equals("0")) {
            String ipAddress = httpRequest.getRemoteAddr(); // lay địa chỉ IP của user
            String requestURI = httpRequest.getRequestURI(); // Kiểm tra trang yêu cầu là trang "login"
            if (requestURI.endsWith("login")) {
                // Kiểm tra xem người dùng đã đăng nhập sai quá 3 lần chưa
                if (loginAttempts.containsKey(ipAddress) && loginAttempts.get(ipAddress) >= MAX_FAILED_ATTEMPTS + 1) {
                    // Chuyển hướng về trang home
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
                    return;
                } else {
                    recordFailedAttempt(ipAddress);
                }
            }
            // Tiếp tục với filter chain
        }else{
            loginAttempts.clear();
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Hủy bỏ filter khi kết thúc ứng dụng
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    // Ghi lại số lần đăng nhập sai của mỗi địa chỉ IP
    private void recordFailedAttempt(String ipAddress) {
        loginAttempts.put(ipAddress, loginAttempts.getOrDefault(ipAddress, 0) + 1);
    }
}