<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<%@ page import="com.commenau.util.RoundUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<!DOCTYPE html>--%>
<html class="no-js" lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Danh sách yêu thích</title>
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/wishlist.css"/> ">
    <!--====== App ======-->
    <!-- for chat -->
    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">
</head>

<body>

<!--====== Main App ======-->
<div id="app">

    <!--====== Main Header ======-->
    <%@ include file="/customer/common/header.jsp" %>
    <jsp:include page="common/chat.jsp"></jsp:include>
    <!--====== End - Main Header ======-->


    <!--====== App Content ======-->
    <div class="app-content">

        <!--====== Section ======-->
        <div class="u-s-p-b-60">

            <!--====== Section Intro ======-->
            <div class="section__intro u-s-m-b-60">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="section__heading u-c-secondary">Danh sách yêu thích</h1>
                        </div>
                    </div>
                </div>
            </div>
            <!--====== End - Section Intro ======-->


            <!--====== Section Content ======-->
            <div class="section__content">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-12 col-md-12 col-sm-12">

                            <!--====== Wishlist Product ======-->
                            <c:forEach var="wishlistItemDTO" items="${requestScope.listWishlistItemDTO}">
                                <div class="w-r u-s-m-b-30">
                                    <div class="w-r__container">
                                        <div class="w-r__wrap-1">
                                            <div class="w-r__img-wrap">
                                                <img class="u-img-fluid"
                                                     src="<c:url value="/images/products/${wishlistItemDTO.image}"/>"
                                                     alt="">
                                            </div>
                                            <div class="w-r__info">
                                                <span class="w-r__name">
                                                    <a href="<c:url value="/product/${wishlistItemDTO.product.id}"/>">${wishlistItemDTO.product.name}</a></span>
                                                <span class="w-r__category">
                                                    <a>${wishlistItemDTO.categoryName}</a></span>
                                                <fmt:formatNumber value="${RoundUtil.roundPrice(wishlistItemDTO.product.price * (1 - wishlistItemDTO.product.discount))}"
                                                                  type="currency"
                                                                  var="formattedPrice"/>
                                                <span class="w-r__price">${formattedPrice}
                                                    <span class="w-r__discount">
                                                        <fmt:formatNumber
                                                                value="${wishlistItemDTO.product.price}"
                                                                type="currency" pattern="###,### đ"/>
                                                    </span></span>
                                            </div>
                                        </div>
                                        <div class="w-r__wrap-2">
                                            <button class="w-r__link btn--e-brand-b-2 btn-add-cart" data-input-id="${wishlistItemDTO.product.id}">THÊM VÀO GIỎ HÀNG</button>
<%--                                            <a class="w-r__link btn--e-transparent-platinum-b-2" href="#">XÓA</a>--%>
                                            <button class="w-r__link btn-delete"
                                                    style="background: none; color: #ff4500;border: 2px solid #ff4500;"
                                                    data-input-id="${wishlistItemDTO.product.id}" data="input-id">XÓA
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            <!--====== End - Wishlist Product ======-->

                            <!--====== End - Wishlist Product ======-->
                        </div>
                        <div class="col-lg-12">
                            <div class="route-box">
                                <div class="route-box__g">
                                    <a class="route-box__link" href="<c:url value="/home"/>"><i
                                            class="fas fa-long-arrow-alt-left"></i>
                                        <span>TIẾP TỤC MUA SẮM</span></a>
                                </div>
                                <div class="route-box__g">
                                    <button class="route-box__link btn-delete__all">
                                        <i class="fas fa-trash"></i><span>ĐẶT LẠI DANH SÁCH</span></button>
                                    <%--                                    <a class="route-box__link" href="wishlist.html"><i class="fas fa-trash"></i>--%>
                                    <%--                                        <span>ĐẶT LẠI DANH SÁCH</span></a>--%>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--====== End - Section Content ======-->
        </div>
        <!--====== End - Section ======-->
    </div>
    <!--====== End - App Content ======-->


    <!--====== Main Footer ======-->
    <%@include file="/customer/common/footer.jsp" %>

    <!--====== Modal Section ======-->
    <!--====== End - Modal Section ======-->
</div>
<!--====== End - Main App ======-->
<%--<script src="<c:url value="/jquey/jquery.min.js"/>"></script>--%>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>

    $('.btn-add-cart').on('click', function () {
        var inputData = {};
        inputData['productId'] = $(this).data('input-id');
        console.log(inputData);
        // using Ajax to send data to server
        $.ajax({
            type: "POST",
            url: "<c:url value="/carts"/>",
            data: JSON.stringify(inputData),
            contentType: "application/json; charset=utf-8",
            success: function () {
                Swal.fire({
                    icon: "success",
                    title: "Đã thêm vào giỏ hàng",
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
            },
            error: function (error) {
                console.log("Đã xảy ra lỗi trong quá trình gửi dữ liệu: " + error);
                Swal.fire({
                    icon: "warning",
                    title: "Thêm vào giỏ hàng thất bại",
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
            }
        });
    });


    $('.btn-delete').on('click', function () {
        Swal.fire({
            title: "Xóa khỏi danh sách yêu thích?",
            text: "Bạn có muốn xóa sản phẩm này khỏi danh sách yêu thích ?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            cancelButtonText: "Không",
            confirmButtonText: "Có"
        }).then((result) => {
            if (result.isConfirmed) {
                var inputData = $(this).data('input-id');
                // using Ajax to send data to server
                $.ajax({
                    type: "DELETE",
                    url: "<c:url value="/wishlist"/>",
                    data: JSON.stringify(inputData),
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
                            timer: 1500,
                            timerProgressBar: true,
                            didOpen: (toast) => {
                                toast.onmouseenter = Swal.stopTimer;
                                toast.onmouseleave = Swal.resumeTimer;
                            }
                        });
                        setTimeout(function () {
                            window.location.href = "<c:url value="/wishlist"/>";
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
                            timer: 1000,
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
            title: "Xóa tất cả ?",
            text: "Bạn có chắc muốn đặt lại danh sách yêu thích?",
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
                    url: "<c:url value="/wishlist"/>",
                    data: JSON.stringify("-1"),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (response) {
                        <%--window.location.href = "<c:url value="/wishlist"/>";--%>
                        Swal.fire({
                            icon: "success",
                            title: "Đã xóa tất cả sản phẩm",
                            toast: true,
                            position: "top-end",
                            showConfirmButton: false,
                            timer: 1500,
                            timerProgressBar: true,
                            didOpen: (toast) => {
                                toast.onmouseenter = Swal.stopTimer;
                                toast.onmouseleave = Swal.resumeTimer;
                            }
                        });
                        setTimeout(function () {
                            window.location.href = "<c:url value="/wishlist"/>";
                        }, 600);
                    },
                    error: function (error) {
                        console.log("Đã xảy ra lỗi trong quá trình gửi dữ liệu: " + error);
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
</script>
</body>

</html>