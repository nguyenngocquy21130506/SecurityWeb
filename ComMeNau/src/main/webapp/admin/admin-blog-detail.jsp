<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý bài viết</title>
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
                        <h5>Quản lý bài viết</h5>
                    </li>
                </ul>
            </div>

            <div class="page-content">

                <div class="col-xs-12 d-flex flex-column align-items-center">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="p-t-30  p-b-30">
                        <div class="col-xs-12">
                            <div style="width: 80%;">
                                <form id="blogForm" action="<c:url value="/admin/blog-detail"/>" method="post"
                                      enctype="multipart/form-data">
                                    <div class="form-group row ">
                                        <label class="col-sm-2 col-form-label">Tiêu đề<span
                                                class="required-check">*</span></label>
                                        <div class="col-sm-10">
                                            <input type="text" data-rule="required|containsAllWhitespace"
                                                   class="form-control" name="title" value="${blog.title}">
                                        </div>
                                    </div>
                                    <div class="form-group row mt-3 ">
                                        <label class="col-sm-2 col-form-label">Hình ảnh<span
                                                class="required-check">*</span></label>
                                        <div class="col-sm-10 d-flex flex-column">
                                            <input type="file" id="mainImg" accept=".jpg, .jpeg, .png, .gif, .svg"
                                                   title="Chọn hình ảnh" name="image"
                                                   data-rule="fileextension-jpg-jpeg-png-gif-svg|maxfilesize-1-MB"/>
                                            <div id="mainImgDiv">
                                                <c:if test="${blog.image != null }">
                                                    <img src="<c:url value="/images/blogs/${blog.image}"/>" >
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group row mt-3">
                                        <label class="col-sm-2 col-form-label">Mô tả ngắn<span
                                                class="required-check">*</span></label>
                                        <div class="col-sm-10">
                                                <textarea class="form-control" id="shortDescription"
                                                          name="shortDescription"
                                                          rows="8">${blog.shortDescription}</textarea>
                                            <lable class="error-input" id="shortDescError"></lable>
                                        </div>
                                    </div>
                                    <div class="form-group row mt-3">
                                        <label class="col-sm-2 col-form-label">Nội dung<span
                                                class="required-check">*</span></label>
                                        <div class="col-sm-10">
                                            <textarea class="form-control" id="content" rows="8" name="content">${blog.content} </textarea>
                                            <lable class="error-input" id="contentError"></lable>
                                        </div>
                                    </div>
                                    <div class="form-group row mt-3">
                                        <label class="col-sm-2 col-form-label"></label>
                                        <div class="col-sm-10">
                                            <button class="btn btn-success" type="submit">Lưu</button>
                                        </div>
                                    </div>
                                    <input type="hidden" name="id" value="${blog.id}">
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
        //integrate ckeditor
        var editor1 = '';
        $(document).ready(function () {
            editor1 = CKEDITOR.replace('content');
        });
        var editor2 = '';
        $(document).ready(function () {
            editor2 = CKEDITOR.replace('shortDescription');
        });

        // open image selected
        $('#mainImg').change(function (event) {
            var imageBase64 = '';
            var imageName = '';
            var reader = new FileReader();
            var file = $(this)[0].files[0];
            reader.onload = function (e) {
                imageBase64 = e.target.result;
                imageName = file.name;
            };
            reader.readAsDataURL(file);
            openImage(this);
        });
        function openImage(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    reader.result
                    var srcData = reader.result;
                    var newImage = document.createElement('img');
                    newImage.src = srcData;
                    document.getElementById('mainImgDiv').innerHTML = newImage.outerHTML;
                }
                reader.readAsDataURL(input.files[0]);
            }
        }

        // Go to validation
        let validate = false;
        new Validator(document.querySelector('#blogForm'), function (err, res) {
            validate = res;
        });
        $('#blogForm').submit(function (event) {
            event.preventDefault(); // Ngăn chặn hành động mặc định của form
            var contentValue = CKEDITOR.instances.content.getData();
            var shortDescriptionValue = CKEDITOR.instances.shortDescription.getData();
            var isValid = true;

            // Kiểm tra nếu content trống
            if (contentValue.length === 0) {
                $('#contentError').text('Nội dung không được để trống.');
                isValid = false;
            }
            // Kiểm tra nếu short description trống
            if (shortDescriptionValue.length === 0) {
                $('#shortDescError').text('Mô tả ngắn không được để trống.');
                isValid = false;
            }
            if (validate === true && isValid === true) {
                // Lấy giá trị từ CKEditor và đặt vào FormData
                var formData = new FormData(this);// Tạo đối tượng FormData từ form
                formData.set('content', contentValue);
                formData.set('shortDescription', shortDescriptionValue);

                $.ajax({
                    type: 'POST',
                    url: $(this).attr('action'), // Lấy URL action của form
                    // dataType: "json",
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        Swal.fire({
                            icon: "success",
                            title: "Đã lưu thành công",
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
                            window.location.href = "<c:url value="/admin/blogs"/>";
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