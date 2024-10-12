<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý bài viết</title>
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <script src="../boostrap/bootstrap-5.3.2-dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-order.css"/>">
</head>

<body class="no-skin">
<!-- navbar main-->
<%@ include file="/admin/common/header.jsp" %>
<!-- end navbar main-->
<div class="fix">
    <!-- navbar left-->
    <%@ include file="/admin/common/nav-left.jsp" %>
    <!-- end navbar left-->

    <!-- main-content -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="breadcrumbs" id="breadcrumbs">

                <ul class="breadcrumb">
                    <li>
                        <h5 href="#">Quản lý đơn hàng</h5>
                    </li>
                </ul>
                <!-- /.breadcrumb -->
            </div>

            <div class="page-content">
                <c:set var="invoice" value="${invoiceOf}"/>
                <div class="content">
                    <div class="status">
                        <div class="date">
                                <span>
                                    <i class="fa-solid fa-calendar"></i>
                                    <b>
<%--                                        Thứ Tư, Ngày 10 Tháng 10, 2023 16:00 chiều--%>
                                        <fmt:formatDate value="${invoice.updatedAt}"
                                                        pattern="'Ngày' dd 'Tháng' MM 'Năm' yyyy hh:mm"/>
                                    </b>
                                </span>
                            <p>Mã đơn hàng : ${invoice.id}</p>
                        </div>
                        <div class="checkline">
                            <div class="sub-status">
                                <div class="circle"><i class="fa-solid fa-user"
                                                       style="color: rgb(67, 142, 185)"></i></div>
                                <div class="status-content">
                                    <p><b>Khách hàng</b> <br>
                                        ${invoice.userFullName}<br>
                                        ${invoice.userEmail}
                                    </p>
                                </div>
                            </div>
                            <div class="sub-status">
                                <div class="circle"><i class="fa-solid fa-truck"
                                                       style="color: rgb(67, 142, 185);"></i></div>
                                <div class="status-content">
                                    <p><b>Thông tin đơn hàng</b> <br>
                                        Phạm vi vận chuyển : Việt Nam <br>
                                        Phương thức thanh toán : ${invoice.paymentMethod}
                                    </p>
                                </div>
                            </div>
                            <div class="sub-status">
                                <div class="circle"><i class="fa-solid fa-location-dot"
                                                       style="color: rgb(67, 142, 185);"></i>
                                </div>
                                <div class="status-content">
                                    <p><b>Vận chuyển tới</b><br>
                                        ${invoice.ward},${invoice.district},${invoice.province}
                                        <br>
                                        ${invoice.address}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="information">
                        <table style="width: 100%;">
                            <tr class="head">
                                <td style="width: 50%;">Sản phẩm</td>
                                <td style="width: 15%;">Giá</td>
                                <td style="width: 20%;">Số lượng</td>
                                <td class="f-right">Tổng</td>
                            </tr>
                            <c:forEach var="item" items="${listInvoiceDetail}">
                                <tr class="item">
                                    <td style="display: flex;">
                                        <img class="img-product" src="" alt="">
                                        <div class="product-name">
                                                ${item.name}
                                        </div>
                                    </td>
                                    <td>
                                        <fmt:formatNumber type="currency" value="${item.price}"/>
                                    </td>
                                    <td>
                                            ${item.quantity}
                                    </td>
                                    <td class="f-right">
                                        <fmt:formatNumber type="currency" value="${item.price * item.quantity}"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            <tr class="final">
                                <td></td>
                                <td></td>
                                <td>Thành tiền : <br>
                                    Phí giao hàng : <br>
                                    Tổng : <br>
                                    Trạng thái :
                                </td>
                                <td class="f-right">
                                    <fmt:formatNumber type="currency" value="${invoice.total}"/><br>
                                    <fmt:formatNumber type="currency" value="${invoice.shippingFee}"/><br>
                                    <span style="font-size: 15px;font-weight: 700;">
                                        <fmt:formatNumber type="currency"
                                        value="${invoice.total + invoice.shippingFee}"/>
                                    </span> <br>
                                    <span class="status" style="padding: 5px; background-color: antiquewhite; color: rgb(67, 142, 185);border-radius: 5px;">
                                        ${invoice.status}
                                    </span>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="operation">
                        <select name="" id="select">
                            <c:forEach var="item" items="${states}">
                                <option value="${item}" <c:if test="${item.equals(invoiceOf.status)}"> selected </c:if> >${item}</option>
                            </c:forEach>
                        </select>
                        <button class="change-state" data-id="${invoice.id}" <c:if test="${invoiceOf.status.equals('Đã hủy')}"> disabled</c:if>>Lưu thay đổi</button>
                    </div>
                </div>

                <!-- /.row -->
            </div>
            <!-- /.page-content -->
        </div>
    </div>
    <!-- /.main-content -->
</div>
<%@ include file="/admin/common/footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="<c:url value="/jquey/jquery.min.js"/>"></script>
<script>
    $('.change-state').on('click', function () {
        // var operation = document.querySelector('.operation');
        var button = $(this);
        var invoiceId = button.data('id');
        var selectedState = $('#select').val();
        $.ajax({
            type: "POST",
            url: "<c:url value="/admin/invoiceDetail"/>",
            data: JSON.stringify(invoiceId + "-" + selectedState),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (response) {
                var statusSpan = button.closest('.operation').prev('.information').find('.final .status');
                statusSpan.text(selectedState);
                Swal.fire({
                    icon: "success",
                    title : "Đổi trạng thái thành công",
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
            },
            error: function (error) {
                console.log('that bai')
                Swal.fire({
                    icon: "warning",
                    title: "Đổi trạng thái thất bại!",
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
        })
    })
</script>
</body>

</html>