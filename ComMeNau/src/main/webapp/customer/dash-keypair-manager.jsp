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
    <title>Quản lý bảo mật</title>
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
                                                    <a href="<c:url value="/profile"/>">Thông tin tài
                                                        khoản</a>
                                                </li>

                                                </li>
                                                <li>
                                                    <a href="<c:url value="/invoices"/>">Đơn đặt hàng</a>
                                                </li>
                                                <li>
                                                    <a class="dash-active" href="<c:url value="/keypair-manager"/>">Quản
                                                        lý bảo mật</a>
                                                </li>

                                            </ul>
                                        </div>
                                    </div>
                                    <!--====== End - Dashboard Features ======-->
                                </div>
                                <div class="col-lg-9">
                                    <div class="dash__box dash__box--shadow dash__box--radius dash__box--bg-white">
                                        <div id="dash-content" class="dash__pad-2">
                                            <h1 class="dash__h1 u-s-m-b-14">Thay đổi mật khẩu</h1>

                                            <table id="table_id" class="table table-striped">
                                                <thead>
                                                <tr>
                                                    <th scope="col">#</th>
                                                    <th scope="col"
                                                        data-bs-toggle="tooltip" data-bs-title="Default tooltip">First</th>
                                                    <th scope="col">Last</th>
                                                    <th scope="col">Handle</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <th scope="row">1</th>
                                                    <td>Mark</td>
                                                    <td>Otto</td>
                                                    <td>@mdo</td>
                                                </tr>
                                                <tr>
                                                    <th scope="row">2</th>
                                                    <td>Jacob</td>
                                                    <td>Thornton</td>
                                                    <td>@fat</td>
                                                </tr>
                                                <tr>
                                                    <th scope="row">3</th>
                                                    <td colspan="2">Larry the Bird</td>
                                                    <td>@twitter</td>
                                                </tr>
                                                </tbody>
                                            </table>
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
    $(document).ready(function () {


        $('#table_id').DataTable();

        <%--var options = {--%>
        <%--    // set a custom rule--%>
        <%--    rules: {--%>
        <%--        confirmed: function (value) {--%>
        <%--            var passwordValue = document.querySelector('input[name="newPassword"]').value;--%>
        <%--            // Check if confirmPassword matches the password--%>
        <%--            return (value === passwordValue);--%>
        <%--        }--%>
        <%--    },--%>
        <%--    messages: {--%>
        <%--        vi: {--%>
        <%--            confirmed: {--%>
        <%--                empty: 'Vui lòng nhập trường này',--%>
        <%--                incorrect: 'Mật khẩu không khớp. Vui lòng nhập lại.'--%>
        <%--            }--%>
        <%--        }--%>
        <%--    }--%>
        <%--};--%>
        <%--// Go to validation--%>
        <%--let validate = false;--%>
        <%--new Validator(document.querySelector('#changePassForm'), function (err, res) {--%>
        <%--    validate = res;--%>
        <%--}, options);--%>

        <%--$('#changePassForm').on('submit', function (event) {--%>
        <%--    event.preventDefault(); // Ngăn chặn hành động mặc định của form--%>

        <%--    // Kiểm tra xác thực trước khi gửi AJAX request--%>
        <%--    if (validate === true) {--%>
        <%--        var newPassword = $('input[name="newPassword"]').val();--%>
        <%--        var confirmPassword = $('input[name="confirmPassword"]').val();--%>
        <%--        var currentPassword = $('input[name="currentPassword"]').val();--%>

        <%--        const encrypt = new JSEncrypt();--%>
        <%--        encrypt.setPrivateKey('')--%>

        <%--        var formData = {--%>
        <%--            newPassword: encrypt.encrypt(newPassword),--%>
        <%--            confirmPassword: encrypt.encrypt(confirmPassword),--%>
        <%--            currentPassword: encrypt.encrypt(currentPassword)--%>
        <%--        };--%>

        <%--        // Gửi AJAX request--%>
        <%--        $.ajax({--%>
        <%--            type: 'POST',--%>
        <%--            url: "<c:url value="/change-password"/>",--%>
        <%--            data: formData,--%>
        <%--            success: function () {--%>
        <%--                Swal.fire({--%>
        <%--                    icon: "success",--%>
        <%--                    title: "Thay đổi thành công",--%>
        <%--                    toast: true,--%>
        <%--                    position: "top-end",--%>
        <%--                    showConfirmButton: false,--%>
        <%--                    timer: 700,--%>
        <%--                    timerProgressBar: true,--%>
        <%--                    didOpen: (toast) => {--%>
        <%--                        toast.onmouseenter = Swal.stopTimer;--%>
        <%--                        toast.onmouseleave = Swal.resumeTimer;--%>
        <%--                    }--%>
        <%--                });--%>
        <%--                setTimeout(function () {--%>
        <%--                    window.location.href = "<c:url value="/profile"/>";--%>
        <%--                }, 700);--%>

        <%--            },--%>
        <%--            error: function () {--%>
        <%--                Swal.fire({--%>
        <%--                    icon: "warning",--%>
        <%--                    title: "Thay đổi không thành công",--%>
        <%--                    toast: true,--%>
        <%--                    position: "top-end",--%>
        <%--                    showConfirmButton: false,--%>
        <%--                    timer: 1000,--%>
        <%--                    timerProgressBar: true,--%>
        <%--                    didOpen: (toast) => {--%>
        <%--                        toast.onmouseenter = Swal.stopTimer;--%>
        <%--                        toast.onmouseleave = Swal.resumeTimer;--%>
        <%--                    }--%>
        <%--                });--%>
        <%--            }--%>
        <%--        });--%>
        <%--    } else {--%>
        <%--        // Xử lý khi form chưa được xác thực--%>
        <%--        console.log("form error")--%>
        <%--    }--%>
        <%--});--%>
    })
</script>
<%--CryptoJS--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
<%--JSEncrypt--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jsencrypt/3.0.0-beta.1/jsencrypt.min.js"></script>
<%--Datatables--%>
<script src="https://cdn.datatables.net/2.1.8/js/dataTables.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.min.css"/>
<%--Bootstrap toggle--%>
<script>
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
    const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))
</script>
</body>

</html>