<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<!DOCTYPE html>--%>
<html class="no-js" lang="en">

<head>
    <meta charset="UTF-8">
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"><![endif]-->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="images/favicon.png" rel="shortcut icon">
    <title>Thay đổi mật khẩu</title>
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash-manage-order.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash-edit-profile.css"/>">

    <!-- for chat -->
    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">
</head>

<body class="config">
<!--====== Main App ======-->
<div id="app">

    <!--====== Main Header ======-->
    <%@ include file="/customer/common/header.jsp" %>
    <jsp:include page="common/chat.jsp"></jsp:include>
    <!--====== End - Main Header ======-->


    <!--====== App Content ======-->
    <div class="container-content mt-5">
        <div class="app-content">
            <!--====== Section 2 ======-->
            <div class="u-s-p-b-60">

                <!--====== Section Content ======-->
                <div class="section__content">
                    <div class="dash">
                        <div class="container">
                            <div class="row">
                                <div class="col-lg-3 col-md-12">

                                    <!--====== Dashboard Features ======-->
                                    <div class="dash__box dash__box--bg-white dash__box--shadow u-s-m-b-30">
                                        <div class="dash__pad-1">

                                            <span class="dash__text u-s-m-b-16">Xin Chào, ${auth.fullName()}</span>
                                            <ul class="dash__f-list">
                                                <li>
                                                    <a class="dash-active" href="<c:url value="/profile"/>">Thông tin
                                                        tài
                                                        khoản</a>
                                                </li>

                                                </li>
                                                <li>
                                                    <a href="<c:url value="/invoices"/>">Đơn đặt
                                                        hàng</a>
                                                </li>

                                            </ul>
                                        </div>
                                    </div>
                                    <!--====== End - Dashboard Features ======-->
                                </div>
                                <div class="col-lg-6">
                                    <div class="dash__box dash__box--shadow dash__box--radius dash__box--bg-white">
                                        <div class="dash__pad-2">
                                            <h1 class="dash__h1 u-s-m-b-14">Thay đổi mật khẩu</h1>
                                            <div class="row">
                                                <div class="col-lg-12">
                                                    <form class="dash-edit-p" id="changePassForm">
                                                        <div class="gl-inline">
                                                            <div class="u-s-m-b-30">
                                                                <label class="gl-label">Mật khẩu hiện tại</label>
                                                                <input class="mb-2 input-text input-text--primary-style"
                                                                       data-rule="required|minlength-6|containsWhitespace"
                                                                       name="currentPassword" type="password"
                                                                       placeholder="Nhập mật khẩu hiện tại">
                                                            </div>
                                                        </div>
                                                        <div class="gl-inline">
                                                            <div class="u-s-m-b-30">
                                                                <label class="gl-label">Mật khẩu mới</label>
                                                                <input class="input-text input-text--primary-style"
                                                                       data-rule="required|minlength-6|containsWhitespace"
                                                                       name="newPassword" type="password"
                                                                       placeholder="Nhập mật khẩu mới">
                                                            </div>
                                                        </div>
                                                        <div class="gl-inline">
                                                            <div class="u-s-m-b-30">
                                                                <label class="gl-label">Nhập lại mật khẩu
                                                                    mới</label>
                                                                <input class="mb-2 input-text input-text--primary-style"
                                                                       data-rule="required|minlength-6|containsWhitespace|confirmed"
                                                                       name="confirmPassword" type="password"
                                                                       placeholder="Nhập lại mật khẩu mới">
                                                            </div>
                                                        </div>
                                                        <button class="btn--e-brand-b-2 btn-submit" type="submit">LƯU</button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--====== End - Section Content ======-->
            </div>
            <!--====== End - Section 2 ======-->
        </div>
    </div>
    <!--====== End - App Content ======-->


    <!--====== Main Footer ======-->
    <%@include file="/customer/common/footer.jsp" %>
</div>
<!--====== End - Main App ======-->
<script src="<c:url value="/validate/validator.js"/>"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
    var options = {
        // set a custom rule
        rules: {
            confirmed: function (value) {
                var passwordValue = document.querySelector('input[name="newPassword"]').value;
                // Check if confirmPassword matches the password
                return (value === passwordValue);
            }
        },
        messages: {
            vi: {
                confirmed: {
                    empty: 'Vui lòng nhập trường này',
                    incorrect: 'Mật khẩu không khớp. Vui lòng nhập lại.'
                }
            }
        }
    };
    // Go to validation
    let validate = false;
    new Validator(document.querySelector('#changePassForm'), function (err, res) {
        validate = res;
    }, options);

    $('#changePassForm').on('submit', function (event) {
        event.preventDefault(); // Ngăn chặn hành động mặc định của form

        // Kiểm tra xác thực trước khi gửi AJAX request
        if (validate === true) {
            var newPassword = $('input[name="newPassword"]').val();
            var confirmPassword = $('input[name="confirmPassword"]').val();
            var currentPassword = $('input[name="currentPassword"]').val();
            var formData = {
                newPassword: newPassword,
                confirmPassword: confirmPassword,
                currentPassword: currentPassword
            };

            // Gửi AJAX request
            $.ajax({
                type: 'POST',
                url: "<c:url value="/change-password"/>",
                data: formData,
                success: function () {
                    Swal.fire({
                        icon: "success",
                        title: "Thay đổi thành công",
                        toast: true,
                        position: "top-end",
                        showConfirmButton: false,
                        timer: 700,
                        timerProgressBar: true,
                        didOpen: (toast) => {
                            toast.onmouseenter = Swal.stopTimer;
                            toast.onmouseleave = Swal.resumeTimer;
                        }
                    });
                    setTimeout(function () {
                        window.location.href = "<c:url value="/profile"/>";
                    }, 700);

                },
                error: function () {
                    Swal.fire({
                        icon: "warning",
                        title: "Thay đổi không thành công",
                        toast: true,
                        position: "top-end",
                        showConfirmButton: false,
                        timer: 1000,
                        timerProgressBar: true,
                        didOpen: (toast) => {
                            toast.onmouseenter = Swal.stopTimer;
                            toast.onmouseleave = Swal.resumeTimer;
                        }
                    });
                }
            });
        } else {
            // Xử lý khi form chưa được xác thực
            console.log("form error")
        }
    });
</script>

</body>

</html>