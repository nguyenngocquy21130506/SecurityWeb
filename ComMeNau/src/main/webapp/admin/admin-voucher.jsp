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
    <script src="<c:url value="/boostrap/bootstrap-5.3.2-dist/js/bootstrap.min.js"/>"></script>
    <link href="https://cdn.datatables.net/v/bs5/jq-3.7.0/dt-2.0.3/datatables.min.css" rel="stylesheet">
    <script src="https://cdn.datatables.net/v/bs5/jq-3.7.0/dt-2.0.3/datatables.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-category.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-user.css"/>">
    <style>
        table i:hover {
            cursor: pointer;
            opacity: 0.7;
        }
    </style>
</head>
<body class="no-skin">
<!--====== Main Header ======-->
<%@ include file="/admin/common/header.jsp" %>
<!--====== End - Main Header ======-->

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
                        <h5>Quản lý khuyến mãi</h5>
                    </li>
                </ul>
            </div>


            <div class="page-content">
                <div class="table-btn-controls  d-flex flex-row-reverse text-right p-t-10">
                    <a class="btn btn-white btn-primary btn-bold" data-toggle="tooltip"
                       title='Thêm khuyến mãi' href="<c:url value="/admin/voucher-detail"/>"> <span>
                                            <i class="fa-solid fa-circle-plus bigger-110 purple"></i>
                                        </span>
                    </a>
                </div>
                <table id="voucher" class="display table table-border table-striped" style="width:100%; margin: 0 30px">
                    <thead>
                    <tr>
                        <th>Tên khuyến mãi</th>
                        <th>Giá trị giảm</th>
                        <th>Áp dụng từ</th>
                        <th>Trạng thái</th>
                        <th>Ngày tạo</th>
                        <th>Hiệu lực</th>
                        <th>Tác vụ</th>
                    </tr>
                    </thead>
                </table>
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
        new DataTable('#voucher', {
            ajax: {
                url: "/admin/vouchers",
                type: "POST"
            },
            columns: [
                {data: 'name'},
                {data: 'discount',
                    render: function (data) {
                        return (data) + "%"
                    }
                },
                {data: 'applyTo',
                    render: function (data) {
                        return data + ".000 đ"
                    }
                },
                {data: 'status',
                    render: function (data) {
                        if (data == 1) {
                            return '<i class="fa-solid text-success fa-circle-check" style="font-size: 20px"></i>'
                        } else {
                            return '<i class="fa-solid text-danger fa-circle-xmark" style="font-size: 20px"></i>'
                        }
                    }
                },
                {data: 'createdAt'},
                {data: 'expriedAt',
                    render: function (data) {
                        // Tính số ngày giữa expriedAt và hiện tại
                        var today = new Date();
                        var expriedAt = new Date(data);

                        // Tính số ngày
                        var timeDiff = expriedAt - today;
                        var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
                        if (timeDiff < 0) {
                            return '<span class="p-1 px-2 bg-danger rounded-pill text-white">Hết</span>'
                        } else {
                            return diffDays + ' ngày';
                        }
                    }
                },
                {data: 'id',
                    render: function (data) {
                        // return '<i data-target="' + data + '" class="fa-solid fa-pen-to-square text-info"></i>'
                        //     + ' <i data-target="' + data + '" class="fa-solid fa-trash text-danger"></i>'
                        return '<a href="<c:url value="/admin/voucher-detail?voucherId=' + data + '"/>"><i class="fa-solid fa-pen-to-square text-info"></i></a>'
                            + ' <i data-target="' + data + '" class="fa-solid fa-trash text-danger"></i>'
                    }

                },
            ],
            processing: true,
            serverSide: true,
        });

        // xóa khuyến mãi
        $(document).on("click", ".fa-trash", (event) => {
            Swal.fire({
                title: "Xóa khuyến mãi?",
                text: "Bạn có muốn xóa khỏi danh sách khuyến mãi?",
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                cancelButtonText: "Không",
                confirmButtonText: "Có"
            }).then((result) => {
                if (result.isConfirmed) {
                    let idVoucher = $(event.target).data("target");
                    $.ajax({
                        url: '<c:url value="/admin/vouchers"/>',
                        type: "DELETE",
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify({idVoucher: idVoucher}),
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
                                window.location.href = "<c:url value="/admin/vouchers"/>";
                            }, 600);
                        }
                    });
                }
            });
        });

    });
</script>
</body>
</html>
<%--Swal.fire({&ndash;%&gt;--%>
<%--        title: "Xóa thể loại?",--%>
<%--        text: "Bạn có chắc muốn xóa thể loại này không!",--%>
<%--        icon: "warning",--%>
<%--        showCancelButton: true,--%>
<%--        confirmButtonColor: "#3085d6",--%>
<%--        cancelButtonColor: "#d33",--%>
<%--        cancelButtonText: "Không",--%>
<%--        confirmButtonText: "Có"--%>
<%--    })
<%--Swal.fire({--%>
<%--    title : "xóa nha",--%>
<%--    text: "chắc chưa?",--%>
<%--    icon: "warning".--%>
<%--    showCancelButton: true,--%>
<%--    cancelButtonText: "không"--%>
<%--    confirmButtonText: "có"--%>
<%--}).then(result => {--%>
<%--    if(reslt.isConfirm){--%>
<%--        $.ajax({--%>
<%--            data:{},--%>
<%--            type:--%>
<%--            url:--%>
<%--            type--%>
<%--        })--%>
<%--    }--%>
<%--})--%>