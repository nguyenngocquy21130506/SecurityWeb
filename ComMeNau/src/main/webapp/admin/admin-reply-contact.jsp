<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.commenau.mail.MailProperties" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Phản hồi khách hàng</title>
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
                        <h6 >Phản hồi khách hàng</h6>
                    </li>
                </ul>
                <!-- /.breadcrumb -->
            </div>

            <div class="page-content">

                <div class="col-xs-12">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="p-t-30 p-b-30" style="height: 632px;">
                        <div class="col-xs-12 ps-2">
                            <div style="width: 80%;">
                                <form id="replyForm">
                                    <div class="form-group row">
                                        <label class="col-sm-2 col-form-label">Tên khách hàng<span class="required-check">*</span></label>
                                        <div class="col-sm-10">
                                            <input type="text" class="form-control" value="${contact.fullName}" data-rule="required|containsAllWhitespace|length-1-100">
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-2 col-form-label">Email người nhận<span class="required-check">*</span></label>
                                        <div class="col-sm-10">
                                            <input type="email" class="form-control" value="${contact.email}"  data-rule="required|email">
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label class="col-sm-2 col-form-label">Tiêu đề<span class="required-check">*</span></label>
                                        <div class="col-sm-10">
                                            <input type="text" class="form-control" name="title" value="${contact.reply.title}" data-rule="required|containsAllWhitespace|length-1-200">
                                        </div>
                                    </div>

                                    <div class="form-group row mt-3">
                                        <div class="d-flex flex-row">
                                            <label class="col-2 col-form-label">Nội dung<span class="required-check">*</span></label>
                                            <div class="col-10">
                                                <textarea class="form-control" id="response" rows="8">${contact.reply.content}</textarea>
                                                <lable class="error-input" id="contentError"></lable>
                                            </div>
                                        </div>
                                    </div>
                                    <c:if test="${contact.reply == null}" >
                                        <div class="form-group row mt-3">
                                            <div class="d-flex flex-row">
                                                <label class="col-2"></label>
                                                <div class="col-10">
                                                    <button class="btn btn-success" type="submit">Gửi</button>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                    <input type="hidden" name="contactId" value="${contact.id}">
                                </form>
                            </div>
                        </div>

                    </div>
                </div>

                <!-- /.span -->
            </div>
            <!-- /.row -->

            <!-- PAGE CONTENT ENDS -->
        </div>
        <!-- /.col -->
    </div>
    <!-- /.page-content -->


    <!-- /.main-content -->
</div>

<!--====== Main Footer ======-->
<%@ include file="/admin/common/footer.jsp" %>
<script src="<c:url value="/jquey/jquery.min.js"/>"></script>
<script src="<c:url value="/admin/ckeditor4/ckeditor.js"/>"></script>
<script src="<c:url value="/validate/validator.js"/>"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    $(document).ready(function () {
        var editor1 = '';
        $(document).ready(function () {
            editor1 = CKEDITOR.replace('response');
        });

        // Go to validation
        let validate = false;
        new Validator(document.querySelector('#replyForm'), function (err, res) {
            validate = res;
        });
        $('#replyForm').submit(function (event) {
            event.preventDefault(); // Ngăn chặn hành động mặc định của form
            // Lấy giá trị từ CKEditor và đặt vào FormData
            var contentValue = CKEDITOR.instances.response.getData();
            var isValidContent = true;

            // Kiểm tra nếu content trống
            if (contentValue.length === 0) {
                $('#contentError').text('Nội dung không được để trống.');
                isValidContent = false;
            }
            if (validate === true && isValidContent === true) {
                var title = $('input[name="title"]').val();
                var contactId = $('input[name="contactId"]').val();

                // Create an object with the collected values
                var formData = {
                    title: title,
                    content: contentValue,
                    contactId: contactId
                };
                // console.log(formData);

                $.ajax({
                    type: 'POST',
                    url: '<c:url value="/admin/contacts"/>',
                    data: formData,
                    success: function (response) {
                        Swal.fire({
                            icon: "success",
                            title: "Đã lưu thành công",
                            toast: true,
                            position: "top-end",
                            showConfirmButton: false,
                            timer: 600,
                            timerProgressBar: true,
                            didOpen: (toast) => {
                                toast.onmouseenter = Swal.stopTimer;
                                toast.onmouseleave = Swal.resumeTimer;
                            }
                        });
                        setTimeout(function () {
                            window.location.href = "<c:url value="/admin/contacts"/>";
                        }, 700);
                    },
                    error: function (xhr, status, error) {
                        Swal.fire({
                            icon: "warning",
                            title: "Lưu thất bại!",
                            toast: true,
                            position: "top-end",
                            showConfirmButton: false,
                            timer: 600,
                            timerProgressBar: true,
                            didOpen: (toast) => {
                                toast.onmouseenter = Swal.stopTimer;
                                toast.onmouseleave = Swal.resumeTimer;
                            }
                        });
                    }
                });
            }else {
                // Xử lý khi form chưa được xác thực
                console.log("form error")
            }
        });
    });
</script>
</body>
</html>