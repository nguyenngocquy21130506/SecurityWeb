<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý liên lạc</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product.css"/>">
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
                        <h6>Quản lý liên lạc</h6>
                    </li>
                </ul>
                <!-- /.breadcrumb -->

                <div class="nav-search" id="nav-search">
                    <form class="form-search" method="get" action="<c:url value="/admin/contacts"/>">
                            <span class="input-icon">
                                <input type="text" placeholder="Search ..." value="${keyWord}" class="nav-search-input" name="keyWord"/>
                                <i class="ace-icon fa fa-search nav-search-icon"></i>
                            </span>
                    </form>
                </div>
                <!-- /.nav-search -->
            </div>

            <div class="page-content">

                <div class="col-xs-12  d-flex flex-column align-items-center">
                    <div class="table-btn-controls d-flex flex flex-row-reverse text-right  p-t-10">
                        <ul>
                            <li>
                                <button id="btnDelete" type="button"
                                        class="btn btn-white btn-primary btn-bold" data-toggle="tooltip"
                                        title='Xóa phản hồi'>
                                        <span> <i class="fa-regular fa-trash-can bigger-110"></i>
                                        </span>
                                </button>
                            </li>
                        </ul>
                    </div>
                    <!-- PAGE CONTENT BEGINS -->
<%--                    <div class="w-100 px-2" >--%>
                    <div class="w-100 px-2" style="<c:if test="${contacts.size() <= 6}">height: 575px;</c:if>">
                        <div class="col-xs-12 p-r-0 ">
                            <table id="simple-table" class="table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th class="text-center"
                                        style="width: 80px; padding-bottom: 20px;padding-top: 20px;">
                                        <label class="pos-rel">
                                            <input type="checkbox" class="form-check-input" id="checkAll"
                                                   style="border: 1px solid #ccc"/>
                                            <span class="lbl"></span>
                                        </label>
                                    </th>
                                    <th class="text-center"
                                        style="width: 200px; padding-bottom: 20px; padding-top: 20px;">
                                        Tên khách hàng
                                    </th>
                                    <th class="text-center"
                                        style="width: 250px;padding-bottom: 20px; padding-top: 20px;">Email
                                    </th>
                                    <th class="text-center"
                                        style="padding-bottom: 20px; padding-top: 20px; ">Tin nhắn
                                    </th>
                                    <th class="text-center"
                                        style="padding-bottom: 20px; padding-top: 20px; width: 200px;">Thời gian
                                    </th>
                                    <th style="width: 70px;"></th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="item" items="${contacts}">
                                    <tr style="height: 70px">
                                        <td class="text-center">
                                            <label class="pos-rel">
                                                <input type="checkbox" class="form-check-input checkbox"
                                                       value="${item.id}"/>
                                                <span class="lbl"></span>
                                            </label>
                                        </td>

                                        <td>${item.fullName}</td>
                                        <td>${item.email}</td>
                                        <td>${item.message}</td>
                                        <td><fmt:formatDate type="both" pattern="dd/MM/yyyy hh:mm:ssa"
                                                            value="${item.createdAt}"/></td>
                                        <td class="text-center">
                                            <div class="btn-group">
                                                <a href="<c:url value="/admin/contacts?contactId=${item.id}"/>"
                                                        <c:if test="${item.reply == null}">
                                                            class="d-flex align-items-center justify-content-center btn btn-xs btn-info">
                                                            <i class="fa-regular fa-message"></i>
                                                        </c:if>
                                                        <c:if test="${item.reply != null}">
                                                            class="d-flex align-items-center justify-content-center btn btn-xs btn-success">
                                                            <i class="fa-solid fa-circle-check"></i>
                                                        </c:if>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- /.row -->
                    <!-- paginatation -->
                    <form id="formPaging" action="<c:url value="/admin/contacts"/>" method="get">
                        <input type="hidden" value="" id="page" name="page"/>
                        <input type="hidden" value="${keyWord}" id="keyWord" name="keyWord"/>
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
<script>
    $(document).ready(function () {
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

        //check all
        $("#checkAll").change(function () {
            $(".checkbox").prop('checked', $(this).prop("checked"));
        });

        $('#btnDelete').on('click', function () {
            Swal.fire({
                title: "Xóa phản hồi?",
                text: "Bạn có chắc muốn xóa phản hồi không!",
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
                        url: '<c:url value="/admin/contacts"/>',
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
                                timer: 700,
                                timerProgressBar: true,
                                didOpen: (toast) => {
                                    toast.onmouseenter = Swal.stopTimer;
                                    toast.onmouseleave = Swal.resumeTimer;
                                }
                            });
                            setTimeout(function () {
                                window.location.href = "<c:url value="/admin/contacts"/>";
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