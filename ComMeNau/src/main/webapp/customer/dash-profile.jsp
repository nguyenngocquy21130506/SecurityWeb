<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông Tin Tài Khoản</title>
    <!--====== font-awesome ======-->
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

<body>
<%@ include file="/customer/common/header.jsp" %>
<jsp:include page="common/chat.jsp"></jsp:include>
<!--========= Main-Content ===========-->
<div class="container-content mt-5">
    <!--====== Section 1 ======-->
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
                                            <a class="dash-active" href="<c:url value="/profile"/>">Thông tin tài
                                                khoản</a>
                                        </li>

                                        </li>
                                        <li>
                                            <a href="<c:url value="/invoices"/>">Đơn đặt hàng</a>
                                        </li>

                                    </ul>
                                </div>
                            </div>
                            <!--====== End - Dashboard Features ======-->
                        </div>
                        <div class="col-lg-9 col-md-12">
                            <div
                                    class="dash__box dash__box--shadow dash__box--radius dash__box--bg-white u-s-m-b-30">
                                <div
                                        class="dash__box dash__box--shadow dash__box--radius dash__box--bg-white u-s-m-b-30">
                                    <div class="dash__pad-2">
                                        <h1 class="dash__h1 u-s-m-b-14">Tài Khoản của tôi</h1>
                                        <span class="dash__text u-s-m-b-30">Xem tất cả thông tin của bạn, bạn có thể
                                            chỉnh sửa lại chúng.</span>
                                        <div class="row">
                                            <div class="col-lg-4 u-s-m-b-30">
                                                <h2 class="dash__h2 u-s-m-b-8">Tên</h2>

                                                <span class="dash__text">${auth.fullName()}</span>
                                            </div>
                                            <div class="col-lg-4 u-s-m-b-30">
                                                <h2 class="dash__h2 u-s-m-b-8">E-mail</h2>
                                                <div class="editable-container">
                                                    <span class="editable-text dash__text">${auth.email}</span>
                                                </div>
                                            </div>
                                            <div class="col-lg-4 u-s-m-b-30">
                                                <h2 class="dash__h2 u-s-m-b-8">Tên đăng nhập</h2>
                                                <div class="editable-container">
                                                    <span class="editable-text dash__text">${auth.username}</span>
                                                </div>
                                            </div>
                                            <div class="col-lg-4 u-s-m-b-30">
                                                <h2 class="dash__h2 u-s-m-b-8">Địa chỉ</h2>
                                                <div class="editable-container">
                                                    <span class="editable-text dash__text">${auth.address}</span>
                                                </div>
                                            </div>
                                            <div class="col-lg-4 u-s-m-b-30">
                                                <h2 class="dash__h2 u-s-m-b-8">SĐT</h2>
                                                <div class="editable-container">
                                                    <span class="editable-text dash__text">${auth.phoneNumber}</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-lg-12">

                                                <div class="u-s-m-b-16">

                                                    <a class="dash__custom-link btn--e-transparent-brand-b-2 btn--e-brand-b-2"
                                                       href="<c:url value="/change-profile"/>">Thay đổi thông tin</a>
                                                </div>
                                                <div>

                                                    <a class="dash__custom-link btn--e-brand-b-2"
                                                       href="<c:url value="/change-password"/>">Thay đổi mật
                                                        khẩu</a>
                                                </div>
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
        <!--====== End - Section 1 ======-->
    </div>
</div>
<!--========= End-Main-Content ===========-->
<!--====== Main Footer ======-->
<%@include file="/customer/common/footer.jsp" %>
</body>
<script>
    $(document).ready(function () {
        $(".edit-button").on("click", function () {
            // Lấy container chứa
            var container = $(this).closest(".editable-container");

            // Gọi hàm chuyển đổi với container tương ứng
            toggleEdit(container);
        });

        function toggleEdit(container) {
            // Lấy giá trị hiện tại của văn bản
            var currentEmail = container.find(".editable-text").text();

            // Tạo một trường nhập liệu và thiết lập giá trị từ văn bản hiện tại
            var input = $("<input>").attr({
                type: "text",
                class: "editable-input",
                value: currentEmail
            });

            // Thay thế văn bản bằng trường nhập liệu
            container.find(".editable-text").replaceWith(input);

            // Tạo nút lưu
            var saveButton = $("<button>").text("Lưu").addClass("save-button btn btn-success mt-3");

            // Thêm trường nhập liệu và nút lưu vào container
            container.append(saveButton);

            // Ẩn nút "Thay đổi"
            container.find(".edit-button").hide();

            // Xử lý sự kiện cho nút lưu
            container.find(".save-button").on("click", function () {
                // Lấy giá trị từ trường nhập liệu và hiển thị lại dưới dạng văn bản
                var newEmail = container.find(".editable-input").val();
                $("<span>").addClass("editable-text  dash__text").text(newEmail).replaceAll(".editable-input");

                // Ẩn nút lưu và hiển thị lại nút "Thay đổi"
                container.find(".save-button").remove();
                container.find(".edit-button").show();
            });
        }
    });
</script>

</html>