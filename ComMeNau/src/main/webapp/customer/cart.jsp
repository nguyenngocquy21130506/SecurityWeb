<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.commenau.util.RoundUtil" %>
<%@ page import="com.commenau.constant.SystemConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Giỏ hàng</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/cart.css"/>">

    <!-- for chat -->
    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">
</head>
<body>
<fmt:setLocale value="vi_VN"/>
<!--====== Main Header ======-->
<%@ include file="/customer/common/header.jsp" %>
<jsp:include page="common/chat.jsp"></jsp:include>
<!--====== End - Main Header ======-->
<!--====== App Content ======-->
<div class="app-content">
    <!--====== Section 2 ======-->
    <div class="u-s-p-b-60">

        <!--====== Section Intro ======-->
        <div class="section__intro u-s-m-b-60">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section__text-wrap">
                            <h1 class="section__heading u-c-secondary">GIỎ HÀNG</h1>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--====== End - Section Intro ======-->
        <%-- Search bar for search function in Cart --%>
        <!--====== Section Content ======-->
        <div class="section__content">
            <div class="container">
<%--                <div class="section__cartItemSearch" style="display: flex; justify-content: flex-end">--%>
<%--                    <form action="/carts" method="post">--%>
<%--                        <input class="searchBox" type="text" name="txtSearch" style="border-radius: 5px; padding: 5px; border: none; background-color: lightgrey"--%>
<%--                               placeholder="Tìm sản phẩm trong giỏ">--%>
<%--                        <input class="searchBtn" type="submit"--%>
<%--                               name="btnGo" style="border-radius: 5px; background-color: #ff4500; padding: 5px; color: white; border: none" value="Tìm kiếm">--%>
<%--                    </form>--%>
<%--                </div>--%>
                <div class="row">
                    <div class="col-lg-12 col-md-12 col-sm-12 u-s-m-b-30">
                        <div class="table-responsive">
                            <table class="table-p">
                                <c:forEach var="item" items="${cart}">
                                    <tr>
                                        <td>
                                            <div class="table-p__box">
                                                <div class="table-p__img-wrap">
                                                    <img class="u-img-fluid" src="<c:url value="/images/products/${item.product.images.get(0)}"/>" alt="">
                                                </div>
                                                <div class="table-p__info">
                                                    <span class="table-p__name"><a
                                                            href="<c:url value="/product/${item.product.id}"/>">${item.product.productName}</a></span>
                                                    <br>
                                                    <span
                                                          class="table-p__name">Loại: ${item.product.categoryName}</span>
                                                    <br>
                                                    <span
                                                            class="table-p__name">Còn Lại:  ${item.product.available}</span>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="table-p__price">
                                                <fmt:formatNumber value="${RoundUtil.roundPrice(item.product.price * (1 - item.product.discount)) * item.quantity}"
                                                                  type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                            </span>
                                        </td>
                                        <td>
                                            <div class="table-p__input-counter-wrap">
                                                <!--====== Input Counter ======-->
                                                <div class="input-counter">
                                                    <span data-input-id="${item.product.id}"
                                                          class="input-counter__minus fas fa-minus"></span>
                                                    <input id="counter-input" data-input-id="${item.product.id}"
                                                           class="input-counter__text input-counter--text-primary-style"
                                                           type="text" value="${item.quantity}" data-min="1"
                                                           data-max="${item.product.available}">
                                                    <span data-input-id="${item.product.id}"
                                                          class="input-counter__plus fas fa-plus"></span>
                                                </div>
                                                <!--====== End - Input Counter ======-->
                                            </div>
                                        </td>
                                        <td>
                                            <div class="table-p__del-wrap">
                                                <button class="far fa-trash-alt table-p__delete-link btn-delete"
                                                        data-input-id="${item.product.id}"></button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </div>
                    <div class="col-lg-12">
                        <div class="route-box">
                            <div class="route-box__g1">
                                <a class="route-box__link" href="<c:url value="${sessionScope.get(SystemConstant.PRE_PAGE)}"/>"><i
                                        class="fas fa-long-arrow-alt-left"></i>
                                    <span>TIẾP TỤC MUA SẮM</span></a>
                            </div>
                            <div class="route-box__g2">
                                <button class="route-box__link btn-delete__all">
                                    <i class="fas fa-trash"></i><span>DỌN SẠCH GIỎ HÀNG</span></button>
                                <button class="route-box__link" id="update-cart-btn"><i class="fas fa-sync"></i>
                                    <span>CẬP NHẬT GIỎ HÀNG</span></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--====== End - Section Content ======-->
    </div>
    <!--====== End - Section 2 ======-->

    <%-- Show detail of cart item when user click on it --%>
    <div class="detail_cartItem--popup">
        <div class="popup-menu">
            <div class="popup-content">
                <div class="detCartItem--img">
                    <img class="u-img-fluid" src="<c:url value="/images/products/${item.product.images.get(0)}"/>" alt="Ảnh sản phẩm">
                </div>
                <div class="detCartItem--info">

                </div>
            </div>
        </div>
    </div>

    <!--====== Section 3 ======-->
    <div class="u-s-p-b-60">

        <!--====== Section Content ======-->
        <div class="section__content">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 col-md-12 col-sm-12 u-s-m-b-30">
                        <form class="f-cart">
                            <div class="row">
                                <div class="col-lg-4 col-md-6 u-s-m-b-30">
                                    <div class="f-cart__pad-box">
                                        <div class="u-s-m-b-30">
                                            <table class="f-cart__table">
                                                <tbody>
                                                <tr>
                                                    <td>TỔNG CỘNG</td>
                                                    <td>
                                                        <fmt:formatNumber value="${RoundUtil.roundPrice(totalPrice)}"
                                                                          type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>PHÍ VẬN CHUYỂN</td>
                                                    <td><fmt:formatNumber value="0" type="currency" maxFractionDigits="0" currencyCode="VND"/></td>
                                                </tr>
                                                <tr>
                                                    <td>THÀNH TIỀN</td>
                                                    <td>
                                                        <fmt:formatNumber value="${RoundUtil.roundPrice(totalPrice)}"
                                                                          type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div>
                                            <a href="<c:url value="/payments"/>" class="btn btn--e-brand-b-2" type="submit"> Thanh toán</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!--====== End - Section Content ======-->
    </div>
    <!--====== End - Section 3 ======-->
</div>
<!--====== End - App Content ======-->
<!--====== Main Footer ======-->
<%@ include file="/customer/common/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    $(document).ready(function () {
        // Show Detail Cart Item
        $('.popup-menu').hide();
        $('.table-p__box').on('click', function() {
            var productName = $(this).find('.table-p__name').text(); // Tên sản phẩm
            var productPrice = $(this).find('.table-p__price').text(); // Giá sản phẩm
            var productQuantity = $(this).find('.input-counter__text').val(); // Số lượng sản phẩm
            var imageUrl = $(this).find('.table-p__img-wrap img').attr('src');
            $('.detCartItem--img').html('<img src="' + imageUrl + '" alt="Ảnh sản phẩm">');
            $('.detCartItem--info').html('<p>Tên sản phẩm: ' + productName + '</p>' +
                '<p>Giá: ' + productPrice + '</p>' +
                '<p>Số lượng: ' + productQuantity + '</p>');
        });

        $('.table-p__info').on('click', function() {
            var productName = $(this).find('.table-p__name').text();
            $('.popup-content').text('Bạn đã chọn sản phẩm: ' + productName);
            $('.popup-menu').show();
        });
        $(document).on('click', function(event) {
            if (!$(event.target).closest('.popup-menu').length) {
                $('.popup-menu').hide();
            }
        });

        var min = parseInt($('.input-counter__text').attr('data-min'));
        var max = parseInt($('.input-counter__text').attr('data-max'));
        $('.input-counter__text').on('input', function () {
            var value = parseInt($(this).val());

            if (isNaN(value) || value < min) {
                value = min;
            } else if (value > max) {
                value = max;
            }

            $(this).val(value);
        });

        //Handle the click event of the increase button
        $('.input-counter__plus').on('click', function () {
            var inputId = $(this).attr('data-input-id');
            var inputElement = $('.input-counter__text[data-input-id="' + inputId + '"]');

            if (inputElement.length > 0) {
                var newValue = parseInt(inputElement.val()) + 1;
                if (newValue <= max)
                    inputElement.val(newValue);
            }
        });

        // Handle the click event of the decrease button
        $('.input-counter__minus').on('click', function () {
            var inputId = $(this).attr('data-input-id');
            var inputElement = $('.input-counter__text[data-input-id="' + inputId + '"]');
            if (inputElement.length > 0) {
                var newValue = parseInt(inputElement.val()) - 1;
                if (newValue >= min)
                    inputElement.val(newValue);
            }
        });

        // Handle the click event of the update cart button
        $('#update-cart-btn').on('click', function () {
            var inputData = {}; // key-value

            // loop thought all the input tags in table and save value, data-input-id to the obj
            $('.table-p input').each(function () {
                var inputValue = $(this).val();
                var inputId = $(this).data('input-id');
                inputData[inputId] = inputValue; // save with key is data-input-id
            });

            console.log(inputData);
            // using Ajax to send data to server
            $.ajax({
                type: "PUT",
                url: "<c:url value="/carts"/>",
                data: JSON.stringify(inputData),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (response) {
                    <%--window.location.href = "<c:url value="/gio-hang"/>";--%>
                    Swal.fire({
                        icon: "success",
                        title: "Cập nhật thành công",
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
                        window.location.href = "<c:url value="/carts"/>";
                    }, 750);
                },
                error: function (error) {
                    <%--console.log("Đã xảy ra lỗi trong quá trình gửi dữ liệu: " + error);--%>
                    <%--window.location.href = "<c:url value="/gio-hang"/>";--%>
                    Swal.fire({
                        icon: "warning",
                        title: "Cập nhật giỏ hàng thất bại",
                        toast: true,
                        position: "top-end",
                        showConfirmButton: false,
                        timer: 500,
                        timerProgressBar: true,
                        didOpen: (toast) => {
                            toast.onmouseenter = Swal.stopTimer;
                            toast.onmouseleave = Swal.resumeTimer;
                        }
                    });
                    setTimeout(function () {
                        window.location.href = "<c:url value="/carts"/>";
                    }, 600);
                }
            });
        });

        $('.btn-delete').on('click', function () {
            Swal.fire({
                title: "Xóa khỏi giỏ hàng?",
                text: "Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Không",
                confirmButtonText: "Có"
            }).then((result) => {
                if (result.isConfirmed) {
                    var cartItemId = $(this).data('input-id');
                    console.log(cartItemId);
                    // using Ajax to send data to server
                    $.ajax({
                        type: "DELETE",
                        url: "<c:url value="/carts"/>",
                        data: JSON.stringify(cartItemId),
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        success: function (response) {
                            <%--window.location.href = "<c:url value="/gio-hang"/>";--%>
                            <%--console.log("xoa thanh cong");--%>
                            Swal.fire({
                                icon: "success",
                                title: "Xóa sản phẩm thành công",
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 500,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                            setTimeout(function () {
                                window.location.href = "<c:url value="/carts"/>";
                            }, 600);
                        },
                        error: function (error) {
                            <%--console.log("Đã xảy ra lỗi trong quá trình gửi dữ liệu: " + error);--%>
                            <%--window.location.href = "<c:url value="/gio-hang"/>";--%>
                            Swal.fire({
                                icon: "warning",
                                title: "Xóa sản phẩm thất bại",
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 500,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                        }
                    });

                }
            });
        });

        $('.btn-delete__all').on('click', function () {
            Swal.fire({
                title: "Xóa tất cả?",
                text: "Bạn có chắc muốn xóa tất cả sản phẩm khỏi giỏ hàng!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Không",
                confirmButtonText: "Có"
            }).then((result) => {
                if (result.isConfirmed) {
                    // using Ajax to send data to server
                    $.ajax({
                        type: "DELETE",
                        url: "<c:url value="/carts"/>",
                        data: JSON.stringify("-1"),
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        success: function (response) {
                            <%--window.location.href = "<c:url value="/gio-hang"/>";--%>
                            Swal.fire({
                                icon: "success",
                                title: "Đã xóa tất cả sản phẩm",
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 500,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                            setTimeout(function () {
                                window.location.href = "<c:url value="/carts"/>";
                            }, 600);
                        },
                        error: function (error) {
                            <%--console.log("Đã xảy ra lỗi trong quá trình gửi dữ liệu: " + error);--%>
                            <%--window.location.href = "<c:url value="/gio-hang"/>";--%>
                            Swal.fire({
                                icon: "warning",
                                title: "Xóa sản phẩm thất bại",
                                toast: true,
                                position: "top-end",
                                showConfirmButton: false,
                                timer: 500,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                        }
                    });
                }
            });
        });
        <%--$('.btn-add-cart').on('click', function () {--%>
        <%--    var inputData = {};--%>
        <%--    inputData['productId'] = $(this).data('input-id');--%>
        <%--    console.log(inputData);--%>
        <%--    // using Ajax to send data to server--%>
        <%--    $.ajax({--%>
        <%--        type: "POST",--%>
        <%--        url: "<c:url value="/gio-hang"/>",--%>
        <%--        data: JSON.stringify(inputData),--%>
        <%--        contentType: "application/json; charset=utf-8",--%>
        <%--        dataType: "json",--%>
        <%--        success: function (response) {--%>
        <%--            &lt;%&ndash;window.location.href = "<c:url value="/gio-hang"/>";&ndash;%&gt;--%>
        <%--            Swal.fire({--%>
        <%--                icon: "success",--%>
        <%--                title: "Đã thêm thành công",--%>
        <%--                toast: true,--%>
        <%--                position: "top-end",--%>
        <%--                showConfirmButton: false,--%>
        <%--                timer: 1000,--%>
        <%--                timerProgressBar: true,--%>
        <%--                didOpen: (toast) => {--%>
        <%--                    toast.onmouseenter = Swal.stopTimer;--%>
        <%--                    toast.onmouseleave = Swal.resumeTimer;--%>
        <%--                }--%>
        <%--            });--%>

        <%--        },--%>
        <%--        error: function (error) {--%>
        <%--            console.log("Đã xảy ra lỗi trong quá trình gửi dữ liệu: " + error);--%>
        <%--            &lt;%&ndash;window.location.href = "<c:url value="/gio-hang"/>";&ndash;%&gt;--%>
        <%--        }--%>
        <%--    });--%>
        <%--});--%>
        for (let i = 0; i < ${requestScope.remove}.length; i++) {
            Swal.fire({
                icon: "warning",
                title: "Xóa sản phẩm  " + ${requestScope.remove}[i].product.productName + " ra khỏi giỏ hàng vì hết hàng",
                toast: true,
                position: "top-end",
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                didOpen: (toast) => {
                    toast.onmouseenter = Swal.stopTimer;
                    toast.onmouseleave = Swal.resumeTimer;
                }
            });
        }
        // let infBoard = document.querySelector(".opacity_bgk0");
        // let infBtn = document.querySelector(".u-img-fluid"); // picture and name product
        // infBtn.onclick = function () {
        //     infBoard.style.display = "block";
        // };
    });

</script>
</body>
</html>