<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="stylesheet" href="<c:url value="/customer/css/empty-cart.css"/>">

    <!-- for chat -->
    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">
</head>
<body>
<!--====== Main Header ======-->
<%@ include file="/customer/common/header.jsp" %>
<jsp:include page="common/chat.jsp"></jsp:include>
<!--====== End - Main Header ======-->
<!--====== App Content ======-->
<div class="container-content my-5">

    <div class="empty py-5 d-flex flex-column align-items-center">
        <div class="d-flex flex-column align-items-center">
            <span class="empty__big-text">Trống</span>
            <span class="empty__text-1">Không có sản phẩm trong giỏ hàng của bạn.</span>
            <a class="empty__redirect-link btn--e-brand" href="<c:url value="/home"/>">Tiếp Tục Mua Hàng</a>
        </div>
    </div>
</div>
<!--====== End - App Content ======-->
<!--====== Main Footer ======-->
<%@ include file="/customer/common/footer.jsp" %>
</body>
</html>