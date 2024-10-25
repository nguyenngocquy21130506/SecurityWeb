
<%@ page import="java.util.Base64" %>
<%@ page import="java.security.PublicKey" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--<!DOCTYPE html>--%>
<html class="no-js" lang="en">

<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa thông tin</title>
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"><![endif]-->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
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
                                <div class="col-lg-9 col-md-12">
                                    <div class="dash__box dash__box--shadow dash__box--radius dash__box--bg-white">
                                        <div class="dash__pad-2">
                                            <h1 class="dash__h1 u-s-m-b-14">Chỉnh sửa Hồ sơ</h1>

                                            <span class="dash__text u-s-m-b-30">Có vẻ như bạn chưa cập nhật hồ sơ
                                                    của mình</span>
                                            <div class="dash__link dash__link--secondary u-s-m-b-30">

                                                <a data-modal="modal" data-modal-id="#dash-newsletter">Theo dõi bản
                                                    tin</a>
                                            </div>
                                            <div class="row">
                                                <div class="col-lg-12">
                                                    <form method="post" class="dash-edit-p" id="profileForm">
                                                        <div class="gl-inline">
                                                            <div class="u-s-m-b-30">

                                                                <label class="gl-label" for="reg-fname">Họ <span
                                                                        class="required-check">*</span></label>

                                                                <input class="input-text input-text--primary-style"
                                                                       data-rule="required"
                                                                       type="text" name="lastName" id="reg-fname"
                                                                       value="${auth.lastName}"
                                                                       placeholder="Họ">
                                                            </div>
                                                            <div class="u-s-m-b-30">

                                                                <label class="gl-label" for="reg-lname">Tên <span
                                                                        class="required-check">*</span></label>

                                                                <input class="input-text input-text--primary-style"
                                                                       data-rule="required"
                                                                       type="text" name="firstName"
                                                                       value="${auth.firstName}"
                                                                       placeholder="Tên">
                                                            </div>
                                                        </div>

                                                        <div class="gl-inline">
                                                            <div class="u-s-m-b-30">
                                                                <h2 class="dash__h2 u-s-m-b-8">Số điện thoại <span
                                                                        class="required-check">*</span></h2>

                                                                <%--                                                                <span class="dash__text">Vui lòng nhập điện thoại di--%>
                                                                <%--                                                                        động của bạn</span>--%>
                                                                <div class="u-s-m-b-30"
                                                                     style="margin-top: 10px; width: 100%;">
                                                                    <input
                                                                            class="input-text input-text--primary-style"
                                                                            data-rule="required"
                                                                            type="text" name="phoneNumber"
                                                                            value="${auth.phoneNumber}"
                                                                            placeholder="Số điện thoại">
                                                                </div>
                                                            </div>
                                                            <div class="u-s-m-b-30">
                                                                <h2 class="dash__h2 u-s-m-b-8">Địa chỉ <span
                                                                        class="required-check">*</span></h2>

                                                                <%-- <span class="dash__text">Vui lòng nhập địa chỉ của bạn của bạn</span> --%>
                                                                <div class="u-s-m-b-30"
                                                                     style="margin-top: 10px; width: 100%;">
                                                                    <input
                                                                            class="input-text input-text--primary-style"
                                                                            data-rule="required"
                                                                            type="text" name="address" id="reg-lname"
                                                                            value="${auth.address}"
                                                                            placeholder="Địa chỉ">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <button id="btn_sendform" class="btn--e-brand-b-2 btn-submit"
                                                                type="button">LƯU
                                                        </button>
                                                        <input type="hidden" name="id" value="${auth.id}">
                                                        <c:if test="${requestScope.enoughError!=null}">
                                                            <b class="text-danger">${requestScope.enoughError}</b>
                                                        </c:if>
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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="<c:url value="/validate/validator.js"/>"></script>
<%-- crypto-js cdn --%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.2.0/crypto-js.min.js"
        integrity="sha512-a+SUDuwNzXDvz4XrIcXHuCf089/iJAoN4lmrXJg18XnduKK6YlDHNRalv4yd1N40OKI80tFidF+rqTFKGPoWFQ=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script>
    $(document).ready(function () {

        let id, lastName, firstName, address, phoneNumber

        <%
        PublicKey publicKey = (PublicKey) application.getAttribute("PUBLIC_KEY");
        String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
        %>


        let validate = true;
        // Go to validation
        // new Validator(document.querySelector('#profileForm'), function (err, res) {
        //     console.log("Validation Errors:", err);
        //     console.log("Validation Result:", res);
        //     validate = res;
        // });

        $('form#profileForm').on('click', 'button#btn_sendform', function () {

            // Kiểm tra xác thực
            if (validate) {
                // Lấy dữ liệu từ form
                id = $('input[name="id"]').val();
                lastName = $('input[name="lastName"]').val();
                firstName = $('input[name="firstName"]').val();
                phoneNumber = $('input[name="phoneNumber"]').val();
                address = $('input[name="address"]').val();

                const publicKey = "<%= publicKeyString %>";
                const encrypt = new JSEncrypt();
                encrypt.setPublicKey(publicKey);

                const formData = {
                    id: id,
                    lastName: lastName,
                    firstName: firstName,
                    phoneNumber: phoneNumber,
                    address: address
                };

                // Mã hóa dữ liệu
                const formDataEncrypt = {
                    id: encrypt.encrypt(formData.id),
                    lastName: encrypt.encrypt(formData.lastName),
                    firstName: encrypt.encrypt(formData.firstName),
                    phoneNumber: encrypt.encrypt(formData.phoneNumber),
                    address: encrypt.encrypt(formData.address)
                };

                // Gửi yêu cầu AJAX
                $.ajax({
                    type: 'POST',
                    url: `<c:url value="/change-profile"/>`,
                    contentType: 'application/json',
                    data: JSON.stringify({formDataEncrypt}),
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
                            window.location.href = "<c:url value='/profile'/>";
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
                Swal.fire({
                    icon: "error",
                    title: "Thông báo",
                    text: "Vui lòng kiểm tra lại các thông tin đã nhập.",
                    confirmButtonText: "OK"
                });
                console.log("form error");
            }
        });
    })

    // function encrypt(plainText, key) {
    //     var srcs = CryptoJS.enc.Utf8.parse(plainText);
    //     var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7});
    //     return encrypted.toString();
    // }
</script>
<%--CryptoJS--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
<%--JSEncrypt--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jsencrypt/3.0.0-beta.1/jsencrypt.min.js"></script>
</body>

</html>