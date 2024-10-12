<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý khuyến mãi</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
</head>
<body class="no-skin">
<!--====== Main Header ======-->
<%@ include file="/admin/common/header.jsp" %>
<!--====== End - Main Header ======-->

<div class="fix">
    <!-- navbar left-->

    <!-- end navbar left-->
    <%@ include file="/admin/common/nav-left.jsp" %>
    <!-- main-content -->
    <div class="main-content">
        <div class="main-content-inner">
            <div class="breadcrumbs" id="breadcrumbs">

                <ul class="breadcrumb">
                    <li>
                        <h5>Quản lý khuyến mãi</h5>
                    </li>
                </ul>
            </div>

            <div class="page-content">

                <div class="col-xs-12 d-flex flex-column align-items-center">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="p-t-30  p-b-30">
                        <div class="col-xs-12">
                            <div style="width: 80%;">
                                <form id="voucherForm" action="<c:url value="/admin/voucher-detail"/>" method="post"
                                      enctype="multipart/form-data">
                                    <div class="form-group">
                                        <div class="form-group row">
                                            <label class="">Tên khuyến mãi<span
                                                    class="required-check">*</span></label>
                                            <input type="text" data-rule="required|containsAllWhitespace"
                                                   class="form-control" name="nameVoucher" value="${voucher.name}">
                                        </div>
                                        <div class="form-group">
                                            <label class="col-form-label">Chiết khấu (%)<span
                                                    class="required-check">*</span></label>
                                            <input type="number" name="discount" value="${voucher.discount}"
                                                   class="form-control"
                                                   min="0" max="100" data-rule="required|between-10-100">
                                        </div>
                                        <div class="form-group">
                                            <label class="col-form-label">Giá trị đơn hàng áp dụng (VNĐ)<span
                                                    class="required-check">*</span></label>
                                            <input type="number" name="applyTo" value="${voucher.applyTo}"
                                                   class="form-control"
                                                   min="0" max="10000000" data-rule="required|between-0-10000000">
                                        </div>
                                        <div class="form-group">
                                            <label class="col-form-label">Thời hạn áp dụng (Ngày) <span
                                                    class="required-check">*</span></label>
                                            <input type="number" name="expiry" value="${expiry}"
                                                   class="form-control"
                                                   data-rule="required">
                                        </div>
                                        <div class="form-group">
                                            <label class="col-form-label">Trạng thái hoạt động<span
                                                    class="required-check">*</span></label>
                                            <select name="voucherStatus" id="voucherStatus">
                                                <option <c:if test="${voucher.status == 1}">selected</c:if> value="1">Hoạt động</option>
                                                <option <c:if test="${voucher.status == 0}">selected</c:if> value="0">Ngưng hoạt động</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group row mt-3">
                                        <label class="col-sm-2 col-form-label"></label>
                                        <div class="col-sm-10">
                                            <button class="btn btn-success" type="submit">Lưu</button>
                                        </div>
                                    </div>
                                    <input type="hidden" name="voucherId" value="${voucherId}">
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- PAGE CONTENT ENDS -->
            </div>
        </div>
    </div>

</div>
<!-- /.main-content -->


<!--====== Main Footer ======-->
<%@ include file="/admin/common/footer.jsp" %>
<script src="<c:url value="/validate/validator.js"/>"></script>
<script src="<c:url value="/jquey/jquery.min.js"/>"></script>
<script src="<c:url value="/admin/ckeditor4/ckeditor.js"/>"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    $(document).ready(function () {

        // CKEDITOR.replace('nameVoucher');
        // CKEDITOR.replace('discount');
        // CKEDITOR.replace('applyTo');
        // CKEDITOR.replace('voucherStatus');
        // Go to validation
        let validate = false;
        new Validator(document.querySelector('#voucherForm'), function (err, res) {
            validate = res;
        });
        $('#voucherForm').submit(function (event) {
            event.preventDefault(); // Ngăn chặn hành động mặc định của form
            let formData = new FormData(this);// Tạo đối tượng FormData từ form
            let voucherId = $('input[name="voucherId"]').val();
            let nameVoucher = $('input[name="nameVoucher"]').val();
            let discount = $('input[name="discount"]').val();
            let applyTo = $('input[name="applyTo"]').val();
            let status = $('#voucherStatus').val();
            // let expriedAt = $('input[name="expriedAt"]').val();
            let isValid = true;
            // Kiểm tra nếu content trống
            if (nameVoucher.length === 0) {
                $('#contentError').text('Nội dung không được để trống.');
                isValid = false;
            }

            if (validate === true && isValid === true) {
                formData.set('id', voucherId);
                formData.set('name', nameVoucher);
                formData.set('discount', discount);
                formData.set('applyTo', applyTo);
                formData.set('status', status);
                // formData.set('expriedAt', expriedAt);
                console.log(formData)
                $.ajax({
                    type: 'POST',
                    url: '<c:url value="/admin/voucher-detail"/>', // Lấy URL action của form
                    data: formData,
                    processData: false, // Để ngăn jQuery xử lý dữ liệu
                    contentType: false, // Để ngăn jQuery thiết lập loại nội dung
                    success: function (response) {
                        Swal.fire({
                            icon: "success",
                            title: "Đã thêm thành công",
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
                            window.location.href = "<c:url value="/admin/vouchers"/>";
                        }, 600);
                    },
                    error: function (xhr, status, error) {
                        Swal.fire({
                            icon: "warning",
                            title: "Lưu thất bại!",
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