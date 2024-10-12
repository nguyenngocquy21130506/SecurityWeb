<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product.css"/>">
    <style>
        #simple-table .img-product {
            width: 170px;
            height: 100px;
        }
    </style>
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
                        <h5 href="#">Quản lý sản phẩm</h5>
                    </li>
                </ul>
                <!-- /.breadcrumb -->

                <div class="nav-search" id="nav-search">
                    <form class="form-search" method="get" action="<c:url value="/admin/products"/>">
                            <span class="input-icon">
                                <input type="text" placeholder="Search ..." value="${keyWord}" class="nav-search-input" name="keyWord"/>
                                 <input type="hidden" value="" name="sortBy"/>
                                <i class="ace-icon fa fa-search nav-search-icon"></i>
                            </span>
                    </form>
                </div>
                <!-- /.nav-search -->
            </div>

            <div class="page-content">

                <div class="col-xs-12 d-flex flex-column align-items-center">
                    <div style="width: 100%; display: flex; justify-content: space-between;padding: 20px 12px;">
                        <div>
                            <select class="form-select" name="sortBy">
                                <c:forEach var="entry" items="${sort}">
                                    <option value="${entry.key}" <c:if test="${sortBy != null && entry.key eq sortBy}">selected</c:if>>${entry.value}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div style="display: flex">
                            <div style="width: 100px;height: 50px;margin: auto;margin-right: 5px;border-radius: 5px" id="exportBtn" class="btn btn-primary mr-2">Export</div>
                            <form style="display: flex" class="upload-form"  action="/admin/export/products" method="post" enctype="multipart/form-data">
                                <input type="file" name="file" accept=".xlsx, .xls" style="margin:10px;">
                                <div class="btn btn-success" type="submit">Upload</div>
                            </form>
                        </div>
                        <div>
                            <ul class="d-flex">
                                <li><a class="btn btn-white btn-primary btn-bold" data-toggle="tooltip"
                                       title='Thêm sản phẩm' href="<c:url value="/admin/product-detail"/>"> <span>
                                            <i class="fa-solid fa-circle-plus bigger-110 purple"></i>
                                        </span>
                                </a>
                                </li>
                                <li>
                                    <button id="btnDelete" type="button"
                                            class="btn btn-white btn-primary btn-bold" data-toggle="tooltip"
                                            title='Xóa sản phẩm'>
                                        <span> <i class="fa-regular fa-trash-can bigger-110"></i></span>
                                    </button>
                                </li>
                                <li>
                                    <button id="resetQuantity" type="button"
                                            class="btn btn-white btn-primary btn-bold" data-toggle="tooltip"
                                            title='Thiết lập số lượng mặc định'>
                                        <span> <i class="fa-solid fa-arrows-rotate bigger-110"></i></span>
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <!-- PAGE CONTENT BEGINS -->
                    <div class="w-100 px-2" style="<c:if test="${products.size() <= 4}">height: 510px;</c:if>">
                        <div class="col-xs-12 p-r-0">
                            <table id="simple-table" class="table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th class="text-center" style="width: 80px; padding-bottom: 20px;padding-top: 20px;">
                                        <label class="pos-rel">
                                            <input type="checkbox" class="form-check-input" id="checkAll"
                                                   style="border: 1px solid #ccc"/>
                                            <span class="lbl"></span>
                                        </label>
                                    </th>
                                    <th class="text-center" style="padding-bottom: 20px; padding-top: 20px;">Tên</th>
                                    <th class="text-center" style=" padding-bottom: 20px; padding-top: 20px;">Danh mục</th>
                                    <th class="text-center" style="padding-bottom: 20px; padding-top: 20px;">Số lượng</th>
                                    <th class="text-center" style=" padding-bottom: 20px; padding-top: 20px;">Hình ảnh</th>
                                    <th class="text-center" style=" padding-bottom: 20px; padding-top: 20px;">Giá</th>
                                    <th class="text-center" style=" padding-bottom: 20px; padding-top: 20px;">Đánh giá</th>
                                    <th class="text-center" style=" padding-bottom: 20px; padding-top: 20px;">Trạng thái</th>
                                    <th class="text-center" style="padding-bottom: 20px; padding-top: 20px;"></th>
                                </tr>
                                </thead>

                                <tbody>
                                    <c:forEach items="${products}" var="item">
                                        <tr>
                                            <td class="text-center">
                                                <label class="pos-rel">
                                                    <input type="checkbox" class="form-check-input checkbox" value="${item.id}"/>
                                                    <span class="lbl"></span>
                                                </label>
                                            </td>
                                            <td>${item.productName}</td>
                                            <td>${item.categoryName}</td>
                                            <td class="text-center">${item.available}</td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${item.images != null && !item.images.isEmpty()}">
                                                        <img class="img-product" src="<c:url value="/images/products/${item.images.get(0)}"/>">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img class="img-product" src="">
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <fmt:formatNumber value="${item.price}" type="currency" pattern="###,### VNĐ"/>
                                            </td>
                                            <td class="text-center">
                                                <div class="gl-rate-style">
                                                    <c:forEach begin="1" end="${item.rating}">
                                                        <i class="fa-solid fa-star" style="color: #ff9600;"></i>
                                                    </c:forEach>
                                                    <c:forEach begin="1" end="${5 - item.rating}">
                                                        <i class="fa-regular fa-star" style="color: #ff9600;"></i>
                                                    </c:forEach>
                                                </div>
                                            </td>
                                            <td>
                                                <c:if test="${item.status}">
                                                    <span class="badge badge-sm bg-success"> Đang kinh doanh</span>
                                                </c:if>
                                                <c:if test="${item.status == false}">
                                                    <span class="badge badge-sm bg-warning">Ngừng kinh doanh</span>
                                                </c:if>
                                            </td>
                                            <td class="text-center" >
                                                <div class="btn-group">
                                                    <a href="<c:url value="/admin/product-detail?productId=${item.id}"/>"
                                                            class="d-flex align-items-center justify-content-center btn btn-xs btn-info">
                                                        <i class="ace-icon fa fa-pencil bigger-120"></i>
                                                    </a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                    </div>
                    <!-- paginatation -->
                    <form id="formPaging" action="<c:url value="/admin/products"/>" method="get">
                        <input type="hidden" value="" id="page" name="page"/>
                        <input type="hidden" value="${keyWord}" id="keyWord" name="keyWord"/>
                        <input type="hidden" value="${sortBy}" id="sortBy" name="sortBy"/>
                    </form>
                    <nav aria-label="Page navigation">
                        <ul class="pagination" id="pagination"></ul>
                    </nav>
                </div>
            </div>
            <!-- /.page-content -->
        </div>
    </div>
</div>

<!--====== Main Footer ======-->
<%@ include file="/admin/common/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="<c:url value="/jquey/jquery.min.js"/>"></script>
<script src="<c:url value="/jquey/jquery.twbsPagination.js"/>"></script>
<script >
    $(document).ready(function () {
        $('#exportBtn').on('click',()=>{
            $.ajax({
                url: 'http://localhost:8080/admin/export/products',
                method: 'GET',
                success: function (response) {
                    window.location.href = response.url;
                },
                error: function (error) {
                    Swal.fire({
                        icon: "failure",
                        title: "Export thất bại",
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
        })
        //paging
        $(function () {
            var totalPages = ${maxPage};
            var currentPage = ${page};
            window.pagObj = $('#pagination').twbsPagination({
                totalPages: totalPages,
                visiblePages: 10,
                startPage: currentPage,
                onPageClick: function (event, page) {
                    // console.info(page + ' (from options)');
                    if (currentPage !== page) {
                        $('#page').val(page);
                        $('#formPaging').submit();
                    }
                }
            });
        });

        //sort
        $('select[name="sortBy"]').change(function() {
            var selectedValue = $(this).val(); // Lấy giá trị được chọn trong select
            $('input[name="sortBy"]').val(selectedValue); // Gán giá trị vào thẻ input hidden
            $('.form-search').submit(); // Submit form
        });

        //check all
        $("#checkAll").change(function() {
            $(".checkbox").prop('checked', $(this).prop("checked"));
        });

        $('#btnDelete').on('click', function () {
            Swal.fire({
                title: "Xóa sản phẩm?",
                text: "Bạn có chắc muốn xóa sản phẩm này không!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Không",
                confirmButtonText: "Có"
            }).then((result) => {
                if (result.isConfirmed) {
                    var ids = [];
                    $('input[type="checkbox"]:checked').each(function() {
                        if ($(this).attr('id') !== 'checkAll') {
                            ids.push($(this).val()); // Thêm giá trị của checkbox được chọn vào mảng
                        }
                    });
                    console.log(ids)
                    $.ajax({
                        url: '<c:url value="/admin/products"/>',
                        method: 'DELETE',
                        contentType: "application/json; charset=utf-8",
                        // dataType: "json",
                        data: JSON.stringify(ids),
                        success: function (response) {
                            Swal.fire({
                                icon: "success",
                                title: "Đã xóa thành công",
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
                        error: function (error) {
                            console.log('that bai')
                            Swal.fire({
                                icon: "warning",
                                title: "Xóa thất bại!",
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
                        }
                    })

                }
            });
        });

        $('#resetQuantity').on('click', function () {
            Swal.fire({
                title: "Thiết lập số lượng?",
                text: "Bạn có muốn thiết lập số lượng mặc định không!",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Không",
                confirmButtonText: "Có"
            }).then((result) => {
                if (result.isConfirmed) {
                    var ids = [];
                    $('input[type="checkbox"]:checked').each(function() {
                        if ($(this).attr('id') !== 'checkAll') {
                            ids.push($(this).val()); // Thêm giá trị của checkbox được chọn vào mảng
                        }
                    });
                    $.ajax({
                        url: '<c:url value="/admin/products"/>',
                        method: 'PUT',
                        contentType: "application/json; charset=utf-8",
                        // dataType: "json",
                        data: JSON.stringify(ids),
                        success: function (response) {
                            Swal.fire({
                                icon: "success",
                                title: "Thiết lập thành công",
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
                        error: function (error) {
                            console.log('that bai')
                            Swal.fire({
                                icon: "warning",
                                title: "Thiết lập thất bại!",
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
                    })

                }
            });
        });

    });
</script>
</body>
</html>