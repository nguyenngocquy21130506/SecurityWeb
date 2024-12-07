//package com.commenau.filter;
//
//import com.commenau.service.WebSocketService;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import javax.websocket.Session;
//import java.io.IOException;
//
//@WebFilter("/*")
//public class SessionFilter implements Filter {
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpSession httpSession = httpRequest.getSession(false); // lấy session hiện tại
//
//        // Nếu có HttpSession, lưu vào WebSocket session
//        if (httpSession != null) {
//            // Bạn cần giữ lại WebSocket session nào đã mở
//            // Giả sử bạn có một cách để lấy tất cả các WebSocket session, ví dụ sử dụng một static list
//            for (Session websocketSession : WebSocketService.getSessions()) {
//                if (websocketSession.getUserProperties().get("userID").equals(httpRequest.getAttribute("userID"))) {
//                    // Gắn HttpSession vào WebSocket session
//                    WebSocketService.setHttpSession(websocketSession, httpSession);
//                }
//            }
//        }
//
//        chain.doFilter(request, response);
//    }
//}
public class SessionFilter {
    public static void main(String[] args) {
        String keyWeb = "MIIEuwIBADANBgkqhkiG9w0BAQEFAASCBKUwggShAgEAAoIBAQCZxLZfSn/aPRAGWRcXr7Pac4N5H84u9IHvAf+s5QZ2t7Qcg7avp6vB71C9xeu08+NdA9ZDLQUq7VyDYibf+n+/p/9Pa3wzmkglQ2eNafYV2GPAXDM5YixBnzktzodhLkjX+e7vjNY4Yh1oy1aVzlMHmzeDazO+CkYP5coAg2gQuKSQS6w2/wfMuieTizxV3uvGOhEqTWhZLwkUgB0aueEaNigOcGd+MHp7mp+3ixf+jJBnuvi3XnHbAs3SlKA393pHP9zsjzUQ70QuEfoWJqeW9jhhMFuM9RXfnOBW/zveaZF1kHp8IQkpP6EUu646hJpYLlU9vtDVFQj5IV9zY841AgMBAAECggEADKAXLUnLp5qd5zeHat11UflsERToc/iFdp9eY87GMhcxRaoW5ONKefjxUzOd/9SKlYeMHyXiJ70nfG4hrQA/XvgnQVkc8Iu5Yr3fEcBigTasMg4f8pU59wVgx38OpI8NUhZmZOrWZ/i+W21epZw0OwqmizbRH8huKj3+0P35HG48EFqwYYtMVkEP4ALsoyIvvTPCGq9SCR/4fnaf5ovjzvR05uuUEUPfIoO8Nssm4UzFGQUUaOfDTAldxiCBdqHccNa4KL2yhTZDpKnTb46tm2cub7DXa7qtDp3gnx6sAUJGjYhV+8gDRv0nVu8R6ck/Xp2Lgj3BdDPIeG9oAZfrAQKBgQDPG0ql9k0ZG7UMdbCijJ+WEQGWVT/iBbv6oSJWs+mYbUVYSUKu57rAVF6fszTZQzZxmMA+YbLaLy0nG9jzXdcd9G2Is8mOd1JvQ49g2xXLWc77H5p66zogORj5kpaLrNKzKRT0di3V41P46XdyIxT3dP75HrqT0lK7G/qewGQuwQKBgQC+Ed6wIZitAxFHZ79DDfQsNETuCY4h+DNfNkZNpgzsxLcoYlfHGwGT+tpv40FHs7GKSjy9wsBHIvpXJTBAwWtuIXqZ5Z4/FOOhQNYKQA5IkBYSikQ72mMli1pnpcG+CpCMw8IBPIS6k2Y91c1+zFr7PYiasTOKzC3vFaDnYKZwdQKBgDHSqPSf+hAuQDHNJj8UgVfCo5Uc0Q94va4fkXk2Mt3+e44voaEeEikuoszcebxClRhi3Oyk6Dfg3YXpBxoU2ylTof4e8kAZqHUn1ZhBXE7dPhPfmHQ1nngeBEoV362eV9nN6kKdI42u8IaS8687jVGyjZw0rkHdeRWWN4Z1ViSBAoGBAIiaFT7m1nC5vKhiEt7hX3SJmGSvr1wQovMx8bMKvU1JUA2RKF7rtOWiq20wmhErP1URZ7hGCKTXqa4KBzTzDf/XxeJnWaUioTqae3pgcSOLkqF+h+2wVgN8tNu7EJRByYOxiXQUB5yEJT4ZtBgGTPk2T0BGTLHbbsPe+oZwOw6lAn9ZrrEA46VXUMBv0YInuJ1elCMk5uZy7mJL0m3encPI/2/9hTZ8zKGyVzNIRkp1sM55xWV3oyVQu8wMUED022xfcmZlxMIdSeJK46KakAMSG0UDN90OJmpS/Izey5awXR1oF+PkksMURsPFiL3u0n7M3dy142JIP7muUV75z1yU";
        String keyServer = "MIIEuwIBADANBgkqhkiG9w0BAQEFAASCBKUwggShAgEAAoIBAQCZxLZfSn/aPRAGWRcXr7Pac4N5H84u9IHvAf+s5QZ2t7Qcg7avp6vB71C9xeu08+NdA9ZDLQUq7VyDYibf+n+/p/9Pa3wzmkglQ2eNafYV2GPAXDM5YixBnzktzodhLkjX+e7vjNY4Yh1oy1aVzlMHmzeDazO+CkYP5coAg2gQuKSQS6w2/wfMuieTizxV3uvGOhEqTWhZLwkUgB0aueEaNigOcGd+MHp7mp+3ixf+jJBnuvi3XnHbAs3SlKA393pHP9zsjzUQ70QuEfoWJqeW9jhhMFuM9RXfnOBW/zveaZF1kHp8IQkpP6EUu646hJpYLlU9vtDVFQj5IV9zY841AgMBAAECggEADKAXLUnLp5qd5zeHat11UflsERToc/iFdp9eY87GMhcxRaoW5ONKefjxUzOd/9SKlYeMHyXiJ70nfG4hrQA/XvgnQVkc8Iu5Yr3fEcBigTasMg4f8pU59wVgx38OpI8NUhZmZOrWZ/i+W21epZw0OwqmizbRH8huKj3+0P35HG48EFqwYYtMVkEP4ALsoyIvvTPCGq9SCR/4fnaf5ovjzvR05uuUEUPfIoO8Nssm4UzFGQUUaOfDTAldxiCBdqHccNa4KL2yhTZDpKnTb46tm2cub7DXa7qtDp3gnx6sAUJGjYhV+8gDRv0nVu8R6ck/Xp2Lgj3BdDPIeG9oAZfrAQKBgQDPG0ql9k0ZG7UMdbCijJ+WEQGWVT/iBbv6oSJWs+mYbUVYSUKu57rAVF6fszTZQzZxmMA+YbLaLy0nG9jzXdcd9G2Is8mOd1JvQ49g2xXLWc77H5p66zogORj5kpaLrNKzKRT0di3V41P46XdyIxT3dP75HrqT0lK7G/qewGQuwQKBgQC+Ed6wIZitAxFHZ79DDfQsNETuCY4h+DNfNkZNpgzsxLcoYlfHGwGT+tpv40FHs7GKSjy9wsBHIvpXJTBAwWtuIXqZ5Z4/FOOhQNYKQA5IkBYSikQ72mMli1pnpcG+CpCMw8IBPIS6k2Y91c1+zFr7PYiasTOKzC3vFaDnYKZwdQKBgDHSqPSf+hAuQDHNJj8UgVfCo5Uc0Q94va4fkXk2Mt3+e44voaEeEikuoszcebxClRhi3Oyk6Dfg3YXpBxoU2ylTof4e8kAZqHUn1ZhBXE7dPhPfmHQ1nngeBEoV362eV9nN6kKdI42u8IaS8687jVGyjZw0rkHdeRWWN4Z1ViSBAoGBAIiaFT7m1nC5vKhiEt7hX3SJmGSvr1wQovMx8bMKvU1JUA2RKF7rtOWiq20wmhErP1URZ7hGCKTXqa4KBzTzDf/XxeJnWaUioTqae3pgcSOLkqF+h+2wVgN8tNu7EJRByYOxiXQUB5yEJT4ZtBgGTPk2T0BGTLHbbsPe+oZwOw6lAn9ZrrEA46VXUMBv0YInuJ1elCMk5uZy7mJL0m3encPI/2/9hTZ8zKGyVzNIRkp1sM55xWV3oyVQu8wMUED022xfcmZlxMIdSeJK46KakAMSG0UDN90OJmpS/Izey5awXR1oF+PkksMURsPFiL3u0n7M3dy142JIP7muUV75z1yU";
        String a = "a1haYitkQUdaM1BiYWF2cGZkZjFpVTMyRXNHQXhsSktwYVhGTEw2QVNDYzRmd1I4T281cWxqZTJxeGE5dzhrODFENkZ5NThPZUZEVGhPcGJzUFVKT3o2QXJpU1lMV0VnUFB6czFDbWtac2ExcVdPZ0VqdEJrNTY2dUJOelVkSE1Qc04vd2pvS2tlcjB2cURXNC9Xb3I2TnR1aVVVc1VQR2JzUHZqblhKNWlqSENhYU0xYzR0VGh2VmVwN2o4MU5hbXNLOUhPUm9uUkRVaTZqMVVhaVFGd1VoOXMraHhyM3NQZGdUejQwclFKTStwWmxaNjd5S3BMZ2gybHdwL0c1cmxYbGdva0dIVkdqT0ZhTW4vV0dta1NTaUgrSjBXK1praVYyd2MzL0ZvRGY2RHk3Uk9QejBRdHhiUy82Y21QMWYza3h2RnBjZzdaWjVQKy9aRi9tMnBRPT0=";
        String b = "OfgaKXy8xtsY6IhEFWVnettWiZNVBoB3pJrxUtA8ozVe52HlJJtoOCFruA9o8n+3g2ArUQq1kHIn+dDkBlA5RqSrlz4GnDvS/q8E++X0LVoEVRlexkdN/sRaelU1nH55ZB793gh5oxdzEra8YZEsglMpgmsx5XR6VthcIP10ltAKQOt9DiTJJngO7DiTH7BREoUIC1yHyZReysg7xbFDjK6TXB+yQQkL8Nwf5sJizMMHzBenKQhOpxS7WMyPq5aSby6K++KYwklFft5W+L8oy4QTPmRl2GujzbpeVU4aPd5fhlK3kDHTA4Vhp8dlAZyHxLLo9ddXXj+Wt16jAQ30dw==";
        System.out.println(b.length());
    }
}