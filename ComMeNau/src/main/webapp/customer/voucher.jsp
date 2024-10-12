<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.commenau.util.RoundUtil" %>
<%@ page import="com.commenau.constant.SystemConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Khuyến mãi</title>
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
                            <h1 class="section__heading u-c-secondary">DANH SÁCH KHUYẾN MÃI</h1>
                            <b class="u-c-secondary">(Chỉ áp dụng 1 khuyến mãi cho 1 đơn hàng)</b>
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
                <div class="row">
                    <div class="col-lg-12 col-md-12 col-sm-12 u-s-m-b-30">
                        <div class="table-responsive">
                            <table class="table-p">
                                <tbody>
                                <c:forEach var="item" items="${vouchers}">
                                    <tr>
                                        <td>
                                            <div class="table-p__box">
                                                <div class="table-p__info">
                                                    <span class="table-p__name fs-5 fw-bold">${item.name}</span>
                                                    <span class="table-p__name fs-5 fw-bold">Chiết khấu : ${item.discount} %</span>
                                                        <%--                                                    Multiply by the price of the order to show the reduced price for the order--%>

                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="table-p__price">
                                                <span class="table-p__name">Áp dụng cho đơn từ ${item.applyTo} VNĐ</span>
                                                <c:if test="${item.checkDay()}">
                                                    <c:out value="Hết hạn trong ${item.remainTime()} ngày"/>
                                                </c:if>
                                                <c:if test="${!item.checkDay()}">
                                                    <c:if test="${!item.inHour()}">
                                                        <c:out value="Hết hạn trong ${item.remainTime()} giờ"/>
                                                    </c:if>
                                                    <c:if test="${item.inHour()}">
                                                        <c:out value="Hết hiệu lực"/>
                                                    </c:if>
                                                </c:if>
                                            </span>
                                        </td>
                                        <td>
                                            <div class="table-p__del-wrap">
                                                <input class="checkVoucher"
                                                       <c:if test="${!item.checkDay() && item.inHour()}">disabled</c:if>
                                                       <c:if test="${item.getApplyTo()*1000 > requestScope.totalPriceOfCart}">disabled</c:if>
                                                       <c:if test="${item.getId() == requestScope.cart.getVoucherId()}"> checked </c:if>
                                                       type="checkbox"
                                                       data-input-id="${item.id}"
                                                       value="${item.id}">
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="col-lg-12">
                        <div class="route-box">
                            <div class="route-box__g1">
<%--                                <form id="cofirmVoucher" method="get" action="<c:url value="/payments"/>" style="margin: 0">--%>
                                    <input type="hidden" name="idVoucher" value="0">
                                    <button type="submit" style="border: none; ">
                                        <a class="route-box__link" href="<c:url value="/payments"/>">
                                        <i class="fas fa-long-arrow-alt-left"></i>
                                        <span>TIẾP TỤC THANH TOÁN</span></a></button>
<%--                                </form>--%>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--====== End - Section Content ======-->
    </div>
    <!--====== End - Section 2 ======-->

</div>
<!--====== End - App Content ======-->
<!--====== Main Footer ======-->
<%@ include file="/customer/common/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>

    $("#cofirmVoucher").on("submit", function(event){
        event.preventDefault(); // Ngăn chặn gửi form ngay lập tức

        $("input.checkVoucher").each(function(index, check) {
            if (check.checked) {
                $("input[name='idVoucher']").val(check.getAttribute("data-input-id"));
            }
        });
        this.submit();
    })


    $(document).ready(function () {
        var min = parseInt($('.input-counter__text').attr('data-min'));
        $('.checkVoucher').change(function () {
            let id = $(this).attr('data-input-id');
            if (this.checked) {
                // Nếu checkbox này được chọn, disable các checkbox khác
                $('.checkVoucher').not(this).prop('disabled', true);
            } else {
                // Nếu checkbox này không được chọn, enable lại các checkbox khác
                $('.checkVoucher').prop('disabled', false);
            }
            $.ajax({
                type: "POST",
                url: "<c:url value="/vouchers"/>", // Điều chỉnh đường dẫn tùy theo đường dẫn của controller
                data: { id: id }, // Dữ liệu gửi đi (nếu cần)
                success: function (response) {
                    // Xử lý kết quả thành công (nếu cần)
                    Swal.fire({
                        icon: "success",
                        title: "Đã chọn thành công",
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
                        window.location.href = "<c:url value="/vouchers"/>";
                    }, 600);
                },
                error: function (error) {
                    // Xử lý lỗi (nếu có)
                    Swal.fire({
                        icon: "warning",
                        title: "Áp dụng thất bại",
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
                }
            });
        });
        $('.checkVoucher').each(function () {
            if (this.checked) {
                // Nếu checkbox này được chọn, disable các checkbox khác
                $('.checkVoucher').not(this).prop('disabled', true);
            }
        });
    });

</script>
</body>
</html>