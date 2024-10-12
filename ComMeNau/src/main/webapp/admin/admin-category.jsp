<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý thể loại</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <script src="<c:url value="/boostrap/bootstrap-5.3.2-dist/js/bootstrap.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-category.css"/>">
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
                        <h5>Quản lý thể loại</h5>
                    </li>
                </ul>
            </div>

            <div class="page-content">

                <div class="col-xs-12 d-flex flex-column align-items-center">
                    <div class="table-btn-controls  d-flex flex-row-reverse text-right p-t-10">
                        <ul>
                            <li>
                                <button class="btn btn-white btn-primary btn-bold" data-toggle="tooltip"
                                        title='Thêm thể loại' id="addRow"> <span>
                                            <i class="fa-solid fa-circle-plus bigger-110 purple"></i>
                                        </span>
                                </button>
                            </li>
                            <li>
                                <button id="btnDelete" type="button"
                                        class="btn btn-white btn-primary btn-bold" data-toggle="tooltip"
                                        title='Xóa thể loại'>
                                        <span> <i class="fa-regular fa-trash-can bigger-110"></i>
                                        </span>
                                </button>
                            </li>
                        </ul>
                    </div>

                    <!-- PAGE CONTENT BEGINS -->
                    <div class="w-100 px-2" style="<c:if test="${categories.size() <= 10}">height: 575px;</c:if>">
                        <div class="col-xs-12 p-r-0 ">
                            <table id="simple-table" class="table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th class="text-center"
                                        style="width: 100px; padding-bottom: 20px;padding-top: 20px;">
                                        <label class="pos-rel">
                                            <input type="checkbox" class="form-check-input" id="checkAll"
                                                   style="border: 1px solid #ccc"/>
                                            <span class="lbl"></span>
                                        </label>
                                    </th>
                                    <th class="text-center" style="padding-bottom: 20px;padding-top: 20px;">Mã thể
                                        loại
                                    </th>
                                    <th class="text-center" style="padding-bottom: 20px;padding-top: 20px;">Tên thể
                                        loại
                                    </th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach var="item" items="${categories}">
                                    <tr>
                                        <td class="text-center">
                                            <label class="pos-rel">
                                                <input type="checkbox" class="form-check-input checkbox"
                                                       value="${item.id}"/>
                                                <span class="lbl"></span>
                                            </label>
                                        </td>
                                        <input type="hidden" name="id" id="category-id" value="${item.id}">

                                        <td class="category-col">
                                            <input type="text" name="code" value="${item.code}" disabled>
                                        </td>
                                        <td class="category-col">
                                            <input type="text" name="name" value="${item.name}" disabled>
                                        </td>
                                        <td>
                                            <div class="btn-group">
                                                <button class="d-flex align-items-center justify-content-center btn btn-xs btn-info"
                                                        title='Sửa thể loại'>
                                                    <i class="ace-icon fa fa-pencil bigger-120"></i>
                                                </button>
                                                <button type="submit"
                                                        class="d-flex align-items-center justify-content-center btn btn-xs btn-success"
                                                        title='Lưu thể loại'>
                                                    <i class="ace-icon fa fa-check bigger-120"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <!-- /.span -->
                    </div>
                    <!-- /.row -->
                    <!-- paginatation -->
                    <c:if test="${maxPage != 1}">
                        <nav aria-label="Page navigation example" style="text-align: center;">
                            <ul class="pagination">
                                <c:forEach var="index" begin="1" end="${maxPage}">
                                    <li class="page-item">
                                        <a class="page-link <c:if test="${page == index}">active</c:if>"
                                           href='<c:url value="/admin/categories?page=${index}"/>'>${index}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </nav>
                    </c:if>
                    <!--hết-->

                    <!-- PAGE CONTENT ENDS -->
                </div>
                <!-- /.col -->


                <!-- /.row -->
            </div>
            <!-- /.page-content -->
        </div>
    </div>
    <!-- /.main-content -->
</div>

<!--====== Main Footer ======-->
<%@ include file="/admin/common/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="<c:url value="/jquey/jquery.min.js"/>"></script>
<script>
    $(document).ready(function () {
        // Bắt sự kiện khi nút "Edit" được nhấn
        $(document).on("click", ".btn-info", function (e) {
            e.preventDefault(); // Ngăn chặn việc submit form
            console.log('btn-info')
            // Tìm thẻ <input> bên cạnh nút "Edit" trong cùng một hàng
            var $inputElements = $(this).closest("tr").find("input[type='text']").not("#category-id");


            // Gỡ bỏ thuộc tính "disabled" trên các thẻ <input>
            $inputElements.prop("disabled", false);
        });
        // Bắt sự kiện khi nút "Submit" được nhấn
        $(document).on("click", ".btn-success", function () {
            // Tìm ra các phần tử input trong hàng được nhấn nút
            var $rowInputs = $(this).closest("tr").find("input[type='text'], input[type='hidden']");
            var dataToSend = {};

            $rowInputs.each(function () {
                dataToSend[$(this).attr("name")] = $(this).val();
            });
            // Gửi dữ liệu đến server bằng Ajax
            $.ajax({
                url: '<c:url value="/admin/categories"/>',
                method: 'POST',
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify(dataToSend),
                success: function (response) {
                    Swal.fire({
                        icon: "success",
                        title: "Thêm mới thành công",
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
                        window.location.href = "<c:url value="/admin/categories"/>";
                    }, 800);
                },
                error: function (error) {
                    console.log('that bai')
                    Swal.fire({
                        icon: "warning",
                        title: "Thêm mới thất bại!",
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


        // Bắt sự kiện khi nút "Thêm thể loại" được nhấn
        $("#addRow").on("click", function () {
            var tableBody = $("#simple-table tbody");

            // Tạo một dòng mặc định
            var defaultRowHTML =
                `
                    <tr>
                        <td class="text-center"><label class="pos-rel"> <input type="checkbox" class="form-check-input checkbox"/>
                            <span class="lbl"></span>
                        </label></td>
                            <input type="hidden" name="id" id="category-id" value="">
                            <td class="category-col"><input name="code" type="text" value="" ></td>
                            <td class="category-col"><input name="name" type="text" value="" ></td>
                            <td>
                                <div class="btn-group">
                                    <button type="button" class="d-flex align-items-center justify-content-center btn btn-xs btn-info" title='Sửa thể loại'>
                                        <i class="ace-icon fa fa-pencil bigger-120"></i>
                                    </button>
                                    <button type="button" class="d-flex align-items-center justify-content-center btn btn-xs btn-success" title='Lưu thể loại'>
                                        <i class="ace-icon fa fa-check bigger-120"></i>
                                    </button>
                                </div>
                            </td>
                    </tr>`;
            // Append the new row to the table
            tableBody.append(defaultRowHTML);
        });
        //check all
        $("#checkAll").change(function () {
            $(".checkbox").prop('checked', $(this).prop("checked"));
        });

        $("#btnDelete").click(function () {
            Swal.fire({
                title: "Xóa thể loại?",
                text: "Bạn có chắc muốn xóa thể loại này không!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Không",
                confirmButtonText: "Có"
            }).then((result) => {
                if (result.isConfirmed) {
                    var ids = [];
                    $('input[type="checkbox"]:checked').each(function () {
                        if ($(this).attr('id') !== 'checkAll') {
                            ids.push($(this).val()); // Thêm giá trị của checkbox được chọn vào mảng
                        }
                    });
                    console.log(ids)
                    $.ajax({
                        url: '<c:url value="/admin/categories"/>',
                        method: 'DELETE',
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify(ids),
                        success: function (response) {
                            Swal.fire({
                                icon: "success",
                                title: "Xóa thành công",
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
                                window.location.href = "<c:url value="/admin/categories"/>";
                            }, 800);
                        },
                        error: function (error) {
                            console.log('that bai')
                            Swal.fire({
                                icon: "warning",
                                title: "Xóa thất bại!",
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
                    })
                }
            });
        });
    });
</script>
</body>
</html>