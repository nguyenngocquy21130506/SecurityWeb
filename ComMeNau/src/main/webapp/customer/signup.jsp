<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Đăng ký</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <script src="<c:url value="/boostrap/bootstrap-5.3.2-dist/js/bootstrap.bundle.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/signup.css"/>">
    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
</head>
<body>
<!--====== Main Header ======-->
<%@ include file="/customer/common/header.jsp" %>
<!--====== End - Main Header ======-->
<!--====== App Content ======-->
<div class="app-content">
    <!--====== Section Intro ======-->

    <div class="section__intro m-b-60 m-t-50">
        <div class="container">
            <h1 class="section__heading ">ĐĂNG KÝ TÀI KHOẢN</h1>
        </div>
    </div>
    <!--====== End - Section Intro ======-->


    <!--====== Section Content ======-->
    <div class="section__content">
        <div class="container">
            <div class="row row--center">
                <div class="col-lg-6">
                    <div class="l-f-o m-b-60">
                        <div class="l-f-o__pad-box">
                            <h1 class="gl-h1 l-s">THÔNG TIN CÁ NHÂN</h1>
                            <form id="signup-form" class="l-f-o__form" action="<c:url value="/signup"/>" method="post">
                                <div class="form-inline">
                                    <div class="m-b-30 form-input m-r-8">
                                        <label class="gl-label">Họ <span class="required-check">*</span></label>
                                        <input name="lastName" data-rule="required|containsAllWhitespace"
                                               value="${user.lastName}" class="input-text input-text--primary-style"
                                               type="text" placeholder="Nhập họ của bạn">
                                        <lable class="error-input"></lable>
                                    </div>
                                    <div class="m-b-30 form-input">
                                        <label class="gl-label">Tên <span class="required-check">*</span></label>
                                        <input name="firstName" data-rule="required|containsAllWhitespace"
                                               value="${user.firstName}" class="input-text input-text--primary-style"
                                               type="text" placeholder="Nhập tên của bạn">
                                        <lable class="error-input"></lable>
                                    </div>
                                </div>
                                <div class="m-b-30 form-input">
                                    <label class="gl-label">Email <span class="required-check">*</span></label>
                                    <input name="email" data-rule="required|email" value="${user.email}"
                                           class="input-text input-text--primary-style" type="email"
                                           placeholder="Nhập địa chỉ email">
                                    <lable class="error-input">${emailError}</lable>
                                </div>

                                <div class="m-b-30 form-input">
                                    <label class="gl-label">Số điện thoại</label>
                                    <input name="phoneNumber" data-rule="phone" value="${user.phoneNumber}"
                                           class="input-text input-text--primary-style" type="text"
                                           placeholder="Nhập số điện thoại">
                                    <lable class="error-input"></lable>
                                </div>
                                <div class="m-b-30 form-input">
                                    <label class="gl-label">Địa chỉ</label>
                                    <input name="address" value="${user.address}"
                                           class="input-text input-text--primary-style" type="text"
                                           placeholder="Nhập địa chỉ">
                                    <lable class="error-input"></lable>
                                </div>
                                <div class="m-b-30 form-input">
                                    <label class="gl-label">Tên đăng nhập <span class="required-check">*</span></label>
                                    <input name="username" data-rule="required|containsWhitespace"
                                           value="${user.username}" class="input-text input-text--primary-style"
                                           type="text" placeholder="Nhập tên đăng nhập">
                                    <lable class="error-input">${usernameError}</lable>
                                </div>
                                <div class="m-b-30 form-input password-container">
                                    <label class="gl-label">Mật khẩu <span class="required-check">*</span></label>
                                    <input id="password" data-rule="required|minlength-8|containsWhitespace|formatPass"
                                           name="password" class="input-text input-text--primary-style" type="password"
                                           placeholder="Nhập mật khẩu">
                                    <span class="toggle-password" onclick="togglePasswordVisibility()">👁️</span>
                                    <lable class="error-input"></lable>
                                </div>
                                <div class="m-b-30 form-input">
                                    <label class="gl-label">Nhập lại mật khẩu <span
                                            class="required-check">*</span></label>
                                    <input name="confirmPassword"
                                           data-rule="required|minlength-8|containsWhitespace|formatPass|confirmed"
                                           class="input-text input-text--primary-style" type="password"
                                           placeholder="Nhập lại mật khẩu">
                                    <lable class="error-input"></lable>
                                </div>
                                <div class="m-b-15">
                                    <input class="btn btn-submit btn--e-transparent-brand-b-2" type="submit"
                                           value="ĐĂNG KÝ"/>
                                    <lable class="error-total"></lable>
                                </div>
                            </form>
                        </div>
                        <a class="gl-link" href="<c:url value="/home"/>">Trở lại trang chủ</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--====== End - Section Content ======-->

<!--====== End - Section 2 ======-->
</div>
<!--====== End - App Content ======-->
<!--====== Main Footer ======-->
<%@ include file="/customer/common/footer.jsp" %>

<%--<script src="<c:url value="/validate/validator.js"/>"></script>--%>

<script>
    // var formHandle = document.querySelector('#signup-form');
    // var options = {
    //     // set a custom rule
    //     rules: {
    //         confirmed: function (value) {
    //             var passwordValue = document.querySelector('input[name="password"]').value;
    //             // Check if confirmPassword matches the password
    //             return (value === passwordValue);
    //         }
    //     },
    //     messages: {
    //         vi: {
    //             confirmed: {
    //                 empty: 'Vui lòng nhập trường này',
    //                 incorrect: 'Mật khẩu không khớp. Vui lòng nhập lại.'
    //             }
    //         }
    //     }
    // };
    // // Go to validation
    // new Validator(formHandle, function (err, res) {
    //     return res;
    // }, options);

    // new validate sign up
    // Lấy tất cả các trường input trong form
    let inputs = document.querySelectorAll('#signup-form input');
    let form = document.querySelector('#signup-form')

    // Lặp qua từng trường input
    inputs.forEach(function (input) {
        // Gắn sự kiện input hoặc change cho từng trường input
        input.addEventListener('blur', function (event) {
            // Lấy giá trị và tên của trường input
            let value = input.value;
            let name = input.getAttribute('name');

            // Lấy đối tượng label error tương ứng với trường input
            let errorLabel = input.parentElement.querySelector('.error-input');

            // Xóa thông báo lỗi hiện tại
            errorLabel.textContent = '';

            // Kiểm tra nếu giá trị trống
            if (value === '' && name !== 'gender') {
                errorLabel.textContent = 'Vui lòng nhập trường này';
            } else {
                switch (name) {
                    case 'firstName' :
                        if (/\d/.test(value)) {
                            // /\d/ + /\s+/
                            errorLabel.textContent = 'Họ không hợp lệ';
                        }
                        break;
                    case 'lastName' :
                        if (/\d/.test(value) || (/\s+/).test(value)) {
                            errorLabel.textContent = 'Tên không hợp lệ';
                        }
                        break;
                    case 'email' :
                        let emailPattern = /^(?=.*\w)(?=.*\d)[\w\d]+@\w+\.\w+$/;
                        // /^(?=.*[a-z])(?=.*\d)[\w\d]+@\w+\.\w+$/
                        if (!emailPattern.test(value)) {
                            errorLabel.textContent = 'Email không hợp lệ';
                        }
                        break;
                    case 'phoneNumber' :
                        let phonePattern = /^0\d{9}$/;
                        // /^0\d{9}$/
                        if (!phonePattern.test(value)) {
                            errorLabel.textContent = 'Số điện thoại không hợp lệ';
                        }
                        break;
                    case 'username' :
                        let usernamePattern = /^(?=.*[A-Z]).{6,}$/;// /^(?=.*[A-Z]).{6,}$/
                        if (!usernamePattern.test(value)) {
                            errorLabel.textContent = 'Username phải có ký tự in hoa và có ít nhất 6 ký tự';
                        }else if ((/\s+/).test(value)) {
                            errorLabel.textContent = 'Username không hợp lệ';
                        }
                        break;
                    case 'password' :
                        if (!(/^.{8,}$/).test(value)) {
                            errorLabel.textContent = 'Password phải có tối thiểu 8 ký tự';
                        } else if (!(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[\W_]).+$/).test(value)) {
                            errorLabel.textContent = 'Password phải có ký tự hoa, ký tự thường, ký tự đặc biệt và số';
                        }
                        break;
                    case 'confirmPassword' :
                        let pass = document.querySelector('input[name=password]').value;
                        if (pass === '') {
                            errorLabel.textContent = 'Vui lòng nhập mật khẩu như trên';
                        } else if (pass !== value) {
                            errorLabel.textContent = 'Xác nhận mật khẩu không chính xác';
                        }
                        break;
                }
            }
        });
    });
    form.addEventListener('submit', function (event) {
        let errors = document.querySelectorAll('#signup-form .error-input');
        let errorTotal = document.querySelector('#signup-form .error-total');
        let hasError = false;
        let hasBlank = false;
        errors.forEach(error => {
            if (error.textContent.trim() !== '') {
                hasError = true;
            }
        })
        inputs.forEach(input => {
            if (input.value === '') {
                hasBlank = true;
            }
        })
        if (hasError || hasBlank) {
            event.preventDefault();
            errorTotal.textContent = 'Vui lòng điền đúng và đủ thông tin!'
        }
    })

    function togglePasswordVisibility() {
        const passwordInput = document.getElementById('password');
        const passwordIcon = document.querySelector('.toggle-password');
        const confirmPasswordInput = document.querySelector('input[name="confirmPassword"]');

        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            confirmPasswordInput.type = 'text';
            passwordIcon.textContent = '🙈'; // Change icon to 'hide'
        } else {
            passwordInput.type = 'password';
            confirmPasswordInput.type = 'password';
            passwordIcon.textContent = '👁️'; // Change icon to 'show'
        }
    }
</script>
</body>
</html>