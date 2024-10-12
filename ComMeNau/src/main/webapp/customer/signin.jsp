<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Đăng nhập</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <script src="<c:url value="/boostrap/bootstrap-5.3.2-dist/js/bootstrap.bundle.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/signin.css"/>">
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <script src="https://www.google.com/recaptcha/enterprise.js?render=6LfanccpAAAAAGuSLG2A6Ils0v-oKPYyrTOXw8kg"></script>
    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
</head>
<body>
<div id="app">
    <c:if test="${registerSuccess != null}">
        <c:set var="alert" value="success"/>
        <c:set var="msg"
               value="<strong>Bạn đã đăng kí thành công.</strong> Vui lòng kiểm tra email để kích hoạt tài khoản."/>
    </c:if>
    <c:if test="${verifySuccess != null}">
        <c:set var="alert" value="success"/>
        <c:set var="msg"
               value="<strong>Xác minh tài khoản thành công.</strong> Tài khoản của bạn đã được xác minh, đăng nhập đã sử dụng hệ thống."/>
    </c:if>
    <c:if test="${verifyError != null}">
        <c:set var="alert" value="danger"/>
        <c:set var="msg"
               value="<strong>Xác minh tài khoản thất bại.</strong> Vui lòng kiểm tra lại email để xác minh tài khoản."/>
    </c:if>
    <c:if test="${signinError != null}">
        <c:set var="alert" value="danger"/>
        <c:if test="${signinError.contains('lần đăng nhập')}">
            <c:set var="msg" value="<strong>Bạn đã đăng nhập thất bại.</strong> Vui lòng kiểm tra username hoặc password.${signinError}"/>
        </c:if>
        <c:if test="${!signinError.contains('lần đăng nhập')}">
            <c:set var="msg" value="${signinError}"/>
        </c:if>
    </c:if>
    <c:if test="${resetPasswordSuccess != null}">
        <c:set var="alert" value="success"/>
        <c:set var="msg"
               value="<strong>Đặt lại mật khẩu thành công.</strong> Vui lòng kiểm tra email để nhận được mật khẩu mới."/>
    </c:if>
    <c:if test="${checkIp != null}">
        <c:set var="checkIp" value="${checkIp}"/>
    </c:if>
    <c:if test="${checkIp == null}">
        <c:set var="checkIp" value="0"/>
    </c:if>
    <!--====== Main Header ======-->
    <%@ include file="/customer/common/header.jsp" %>
    <!--====== End - Main Header ======-->

    <!--====== App Content ======-->
    <div class="app-content">

        <!-- <div class="u-s-p-b-60"> -->

        <!--====== Section Intro ======-->
        <div class="section__intro m-b-30 m-t-50">
            <div class="container">
                <h1 class="section__heading ">ĐĂNG NHẬP</h1>
            </div>
        </div>
        <!--====== End - Section Intro ======-->


        <!--====== Section Content ======-->
        <div class="section__content">
            <div class="container">
                <div class="row row--center">
                    <div class="col-lg-6 m-b-30">
                        <div class="l-f-o">
                            <div class="l-f-o__pad-box">
                                <c:if test="${msg != null}">
                                    <div class="alert alert-${alert} alert-dismissible fade show" role="alert">
                                            ${msg}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                    </div>
                                </c:if>
                                <h1 class="gl-h1">BẠN CHƯA CÓ TÀI KHOẢN ?</h1>
                                <div class="m-b-15">
                                    <a class="l-f-o__create-link btn--e-transparent-brand-b-2"
                                       href="<c:url value="/signup"/>">Đăng ký tài khoản</a>
                                </div>
                                <h1 class="gl-h1">ĐĂNG NHẬP</h1>

                                <span class="gl-text m-b-30">Nếu bạn đã có tài khoản, vui lòng đăng nhập.</span>
                                <form id="signin-form" class="l-f-o__form" method="post" action="<c:url value="/login"/>">
                                    <div class="gl-s-api">
                                        <div class="m-b-15">
                                            <a class="gl-s-api__btn gl-s-api__btn--fb"
                                               href="https://www.facebook.com/dialog/oauth?client_id=2847491848723378&redirect_uri=http://localhost:8080/login-facebook&scope=email,public_profile">
                                                <i class="fab fa-facebook-f"></i>
                                                <span>Đăng nhập với Facebook</span>
                                            </a>
                                        </div>
                                        <div class="m-b-15">

                                            <a class="gl-s-api__btn gl-s-api__btn--gplus" href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:8080/login-google&response_type=code
                                                                 &client_id=790048615602-63eqj1ec5eilufn3negb75ctl1mo5an0.apps.googleusercontent.com&approval_prompt=force"><i
                                                    class="fab fa-google"></i>
                                                <span>Đăng nhập với Google</span></a>
                                        </div>
                                    </div>
                                    <div class="m-b-30 m-t-30">

                                        <label class="gl-label">TÊN ĐĂNG NHẬP <span
                                                class="required-check">*</span></label>

                                        <input class="input-text input-text--primary-style" type="text"
                                               name="username" data-rule="required" value="${username}" placeholder="Nhập tên đăng nhập"
                                               >
                                    </div>
                                    <div class="m-b-30 password-container">

                                        <label class="gl-label">MẬT KHẨU <span class="required-check">*</span></label>

                                        <input class="input-text input-text--primary-style" type="password"
                                               name="password" data-rule="required" value="${password}" placeholder="Nhập mật khẩu" >
                                        <span class="toggle-password" onclick="togglePasswordVisibility()">👁️</span>
                                    </div>
                                    <div class="gl-inline">
                                        <div class="m-b-30">

                                            <button class="g-recaptcha btn btn--e-transparent-brand-b-2 "
                                                    data-sitekey="6LfanccpAAAAAGuSLG2A6Ils0v-oKPYyrTOXw8kg"
                                                    data-callback='onSubmit'>
                                            ĐĂNG NHẬP
                                            </button>
                                        </div>
                                        <div class="m-b-30">
                                            <a class="gl-link" href="<c:url value="/lost-password"/>">Quên mật khẩu?</a>
                                        </div>
                                    </div>
                                    <div class="m-b-30">

                                        <!--====== Check Box ======-->
                                        <div class="check-box">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="rememberMe"
                                                       id="flexCheckDefault"
                                                <c:if test="${rememberMe != null}"> checked</c:if> >
                                                <label class="form-check-label" for="flexCheckDefault">
                                                    Nhớ mật khẩu
                                                </label>
                                            </div>
                                        </div>
                                        <!--====== End - Check Box ======-->
                                    </div>
                                    <input type="text" hidden="hidden" name="checkIp" value="${checkIp}">
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--====== End - Section Content ======-->
        <!-- </div> -->
        <!--====== End - Section 2 ======-->
    </div>
    <!--====== End - App Content ======-->

    <!--====== Main Footer ======-->
    <%@ include file="/customer/common/footer.jsp" %>
</div>
<script src="<c:url value="/validate/validator.js"/>"></script>
<script>
    var formHandle = document.querySelector('#signin-form');
    function onSubmit(token) {
        document.getElementById("signin-form").submit();
    }

    // Go to validation
    new Validator(formHandle, function (err, res) {
        return res;
    });
    function togglePasswordVisibility() {
        const passwordInput = document.querySelector('input[name="password"]');
        const passwordIcon = document.querySelector('.toggle-password');

        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            passwordIcon.textContent = '🙈'; // Change icon to 'hide'
        } else {
            passwordInput.type = 'password';
            passwordIcon.textContent = '👁️'; // Change icon to 'show'
        }
    }
</script>
</body>

</html>
