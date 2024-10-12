<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý sản phẩm</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product-detail.css"/>">

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
                        <h5>Quản lý sản phẩm</h5>
                    </li>
                </ul>
                <!-- /.breadcrumb -->
            </div>

            <div class="page-content">

                <div class="col-xs-12 d-flex flex-column align-items-center">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="p-t-30  p-b-30">
                        <div class="col-xs-12">
                            <div style="width: 80%;">
                                <form id="productForm">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="col-form-label">Thể loại <span
                                                        class="required-check">*</span></label>
<%--                                                <select class="form-control" name="categoryId" data-rule="required">--%>
<%--                                                    <c:if test="${product.categoryId == 0}">--%>
<%--                                                        <option value="0" selected></option>--%>
<%--                                                        <c:forEach var="category" items="${categories}">--%>
<%--                                                            <option value="${category.id}">${category.name}</option>--%>
<%--                                                        </c:forEach>--%>
<%--                                                    </c:if>--%>

<%--                                                    <c:if test="${product.categoryId > 0}">--%>
<%--                                                        <c:forEach var="category" items="${categories}">--%>
<%--                                                            <option value="${category.id}"--%>
<%--                                                                    <c:if test="${category.id == product.categoryId}">selected</c:if>--%>
<%--                                                            >${category.name}</option>--%>
<%--                                                        </c:forEach>--%>
<%--                                                    </c:if>--%>
<%--                                                </select>--%>
                                                <select class="form-control" name="categoryId" data-rule="required">
                                                    <c:if test="${product.categoryId == 0}"><option value="0" selected></option></c:if>
                                                    <c:forEach var="category" items="${categories}">
                                                        <option value="${category.id}"
                                                                <c:if test="${product.categoryId == category.id}">selected</c:if>
                                                        >${category.name}</option>
                                                    </c:forEach>
                                                </select>

                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="col-form-label">Trạng thái <span
                                                        class="required-check">*</span></label>
                                                <select class="form-control" name="status" data-rule="required">
                                                    <option value="true" <c:if test="${product.status}">selected</c:if>>
                                                        Đang kinh doanh
                                                    </option>
                                                    <option value="false"
                                                            <c:if test="${product.status==false}">selected</c:if>>Ngừng
                                                        kinh doanh
                                                    </option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row mt-3">
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label class="col-form-label">Tên sản phẩm <span class="required-check">*</span></label>
                                                <input type="text" name="name" value="${product.productName}"
                                                       class="form-control" data-rule="required">
                                            </div>

                                        </div>
                                    </div>
                                    <div class="row mt-3">
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label class="col-form-label">Hình ảnh đại diện</label>
                                                <input type="file" id="mainImg" name="mainImg"
                                                       onchange="uploadMainImg()">
                                                <div id="mainImgDiv">
                                                    <c:if test="${product.images != null && !product.images.isEmpty()}">
                                                        <img src="<c:url value="/images/products/${product.images.get(0)}"/>">
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row mt-3">
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label class="col-form-label">Hình ảnh phụ</label>
                                                <input type="file" id="secondImg" name="secondImg"
                                                       onchange="uploadSecondImg()" multiple>
                                                <div id="secondImgDiv">
                                                    <c:if test="${product.images != null && !product.images.isEmpty()}">
                                                        <c:forEach var="image" items="${product.images}"
                                                                   varStatus="index">
                                                            <c:if test="${index.index != 0}">
                                                                <img src="<c:url value="/images/products/${product.images.get(index.index)}"/>">
                                                            </c:if>
                                                        </c:forEach>
                                                    </c:if>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                    <div class="row mt-3">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="col-form-label">Giá bán (VNĐ)<span
                                                        class="required-check">*</span></label>
                                                <input type="number" name="price" value="${product.price}"
                                                       class="form-control"
                                                       min="0" max="10000000" data-rule="required|between-0-10000000">
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="col-form-label">Giảm giá (%)</label>
                                                <input type="number" name="discount"
                                                       value="${ Float.parseFloat(product.discount * 100)}"
                                                       class="form-control"
                                                       min="0" max="100" data-rule="between-0-100">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row mt-3">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label class="col-form-label">Số lượng <span
                                                        class="required-check">*</span></label>
                                                <input type="number" name="available" value="${product.available}"
                                                       class="form-control"
                                                       min="0" max="500" data-rule="required|between-0-500">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row mt-3">
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label class="col-form-label">Mô tả</label>
                                                <textarea class="form-control" id="description"
                                                          rows="8">${product.description}</textarea>
                                            </div>

                                        </div>
                                    </div>
                                    <div class="col-md-4" style="padding: 0;margin: 30px auto;">
                                        <input type="submit" class="btn btn-success btn-send pt-2 btn-block"
                                               value="Lưu">
                                    </div>
                                    <input type="hidden" name="id" value="${product.id}">
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<!--====== Main Footer ======-->
<%@ include file="/admin/common/footer.jsp" %>
<script src="<c:url value="/jquey/jquery.min.js"/>"></script>
<script src="<c:url value="/admin/ckeditor4/ckeditor.js"/>"></script>
<script src="<c:url value="/validate/validator.js"/>"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    //integrate ckeditor
    var description = '';
    $(document).ready(function () {
        description = CKEDITOR.replace('description');
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

    // open image selected
    function uploadSecondImg() {
        $('#secondImgDiv').text('');
        var fileSelected = document.getElementById('secondImg').files;
        if (fileSelected.length > 0) {
            for (var i = 0; i < fileSelected.length; i++) {
                var fileToLoad = fileSelected[i];
                var fileReader = new FileReader();
                fileReader.onload = function (fileLoaderEvent) {
                    var srcData = fileLoaderEvent.target.result;
                    var newImage = document.createElement('img');
                    newImage.src = srcData;
                    document.getElementById('secondImgDiv').innerHTML += newImage.outerHTML;
                }
                fileReader.readAsDataURL(fileToLoad);
            }

        }
    }

    // Go to validation
    let validate = false;
    new Validator(document.querySelector('#productForm'), function (err, res) {
        validate = res;
    });
    $('#productForm').submit(function (event) {
        event.preventDefault(); // Ngăn chặn hành động mặc định của form
        if (validate === true) {
            // Lấy giá trị từ CKEditor và đặt vào FormData
            var descriptionValue = CKEDITOR.instances.description.getData();

            var formData = new FormData(this);// Tạo đối tượng FormData từ form
            formData.set('description', descriptionValue);

            $.ajax({
                type: 'POST',
                url: '<c:url value="/admin/product-detail"/>', // Lấy URL action của form
                dataType: "json",
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
                        timer: 600,
                        timerProgressBar: true,
                        didOpen: (toast) => {
                            toast.onmouseenter = Swal.stopTimer;
                            toast.onmouseleave = Swal.resumeTimer;
                        }
                    });
                    setTimeout(function () {
                        window.location.href = "<c:url value="/admin/products"/>";
                    }, 700);
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
</script>
</body>
</html>