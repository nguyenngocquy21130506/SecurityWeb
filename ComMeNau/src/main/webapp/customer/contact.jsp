<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Liên hệ</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/contact.css"/>">

    <!-- for chat -->
    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">
</head>
<body>
<div id="app">
    <!--====== Main Header ======-->
    <%@ include file="/customer/common/header.jsp" %>
    <jsp:include page="common/chat.jsp"></jsp:include>
    <!--====== End - Main Header ======-->
    <!--====== App Content ======-->
    <div class="app-content">
        <div class="p-b-60 p-t-60">
            <div class="section__content">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="ratio ratio-16x9">
                                <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3918.3138198427973!2d106.74325807486993!3d10.863718657577879!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x317527f2cf756d67%3A0xb8f7205d29a75004!2zUXXDoW4gQ8ahbSBOZ29uIDI0Nw!5e0!3m2!1sen!2s!4v1697548022888!5m2!1sen!2s"
                                        width="1140" height="600" style="border:0;" allowfullscreen="" loading="lazy"
                                        referrerpolicy="no-referrer-when-downgrade"></iframe>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="p-b-60">
            <div class="section__content">
                <div class="container">
                    <div class="row row--space-between">
                        <div class="col-4">
                            <div class="contact-o u-h-100">
                                <div class="contact-o__wrap">
                                    <div class="contact-o__icon"><i class="fas fa-phone-volume"></i></div>

                                    <span class="contact-o__info-text-1">SỐ ĐIỆN THOẠI</span>

                                    <span class="contact-o__info-text-2">(+0) 900 901 904</span>
                                </div>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="contact-o u-h-100">
                                <div class="contact-o__wrap">
                                    <div class="contact-o__icon"><i class="fas fa-map-marker-alt"></i></div>

                                    <span class="contact-o__info-text-1">ĐỊA CHỈ</span>

                                    <span class="contact-o__info-text-2">
                                        110 Đường Sương Nguyệt Anh, Phường Phạm Ngũ Lão, Quận 1 </span>
                                    <span class="contact-o__info-text-2">Thành phố Hồ Chí Minh</span>
                                </div>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="contact-o u-h-100">
                                <div class="contact-o__wrap">
                                    <div class="contact-o__icon"><i class="far fa-clock"></i></div>
                                    <span class="contact-o__info-text-1">GIỜ MỞ CỬA</span>
                                    <span class="contact-o__info-text-2">Thứ Hai - Chủ Nhật</span>
                                    <span class="contact-o__info-text-2">Từ 9 AM đến 20 PM</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="p-b-60">
            <div class="section__content">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="contact-area u-h-100">
                                <div class="contact-area__heading">
                                    <h2>Liên hệ với chúng tôi</h2>
                                </div>
                                <form id="contactForm" class="contact-f" method="post"
                                      action="<c:url value="/contacts"/>">
                                    <div class="row">
                                        <div class="col-lg-6 u-h-100 pd-1">
                                            <div class="m-b-30">
                                                <p class="form-label">Nhập họ tên<span class="required-check">*</span></p>
                                                <input class="input-text input-text--border-radius input-text--primary-style"
                                                       type="text" name="fullName"
                                                       data-rule="required|containsAllWhitespace"></div>
                                            <div class="m-b-30">
                                                <p class="form-label">Nhập email<span class="required-check">*</span></p>
                                                <input class="input-text input-text--border-radius input-text--primary-style"
                                                       type="email" name="email" data-rule="required|email"></div>
                                        </div>
                                        <div class="col-lg-6 u-h-100  pd-1">
                                            <div class="m-b-30">
                                                <label for="c-message"></label>
                                                <textarea
                                                        class="text-area text-area--border-radius text-area--primary-style"
                                                        id="c-message" placeholder="Nhập nội dung" name="message"
                                                        style="max-width: 538px; max-height: 185px;"
                                                        data-rule="required"></textarea>
                                            </div>
                                        </div>
                                        <div class="col-lg-12">
                                            <button class="btn--e-brand-b-2 btn-submit" type="submit">GỬI</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--====== End - App Content ======-->

    <!--====== Main Footer ======-->
    <%@ include file="/customer/common/footer.jsp" %>
</div>
<script src="<c:url value="/validate/validator.js"/>"></script>
<%--<script src="<c:url value="/jquey/jquery.min.js"/>"></script>--%>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    $(document).ready(function () {
        let validate = false; // Khởi tạo biến validate

        // Go to validation
        new Validator(document.querySelector('#contactForm'), function (err, res) {
            validate = res; // Gán giá trị xác thực vào biến validate
        });

        // Intercept form submission
        $('#contactForm').on('submit', function (event) {
            event.preventDefault(); // Ngăn chặn hành động mặc định của form

            // Kiểm tra xác thực trước khi gửi AJAX request
            if (validate === true) {
                var fullName = $('input[name="fullName"]').val();
                var email = $('input[name="email"]').val();
                var message = $('textarea[name="message"]').val();
                var formData = {
                    fullName: fullName,
                    email: email,
                    message: message
                };

                // Gửi AJAX request
                $.ajax({
                    type: 'POST',
                    url: $(this).attr('action'),
                    contentType: 'application/json',
                    data: JSON.stringify(formData),
                    success: function (response) {
                        // console.log("success")
                        //reset value
                        $('input[name="fullName"]').val("");
                        $('input[name="email"]').val("");
                        $('textarea[name="message"]').val("");

                        Swal.fire({
                            icon: "success",
                            title: "Đã gửi thành công",
                            toast: true,
                            position: "top-end",
                            showConfirmButton: false,
                            timer: 800,
                            timerProgressBar: true,
                            didOpen: (toast) => {
                                toast.onmouseenter = Swal.stopTimer;
                                toast.onmouseleave = Swal.resumeTimer;
                            }
                        });

                    },
                    error: function (xhr, status, error) {
                        // console.log("eror")
                        Swal.fire({
                            icon: "warning",
                            title: "Gửi thất bại!",
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
    });
</script>
</body>

</html>
