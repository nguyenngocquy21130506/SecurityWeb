<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<%@ page import="com.commenau.constant.SystemConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý nhật ký web</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <script src="<c:url value="/boostrap/bootstrap-5.3.2-dist/js/bootstrap.min.js"/>"></script>
    <link href="https://cdn.datatables.net/v/bs5/jq-3.7.0/dt-2.0.3/datatables.min.css" rel="stylesheet">
    <script src="https://cdn.datatables.net/v/bs5/jq-3.7.0/dt-2.0.3/datatables.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product.css"/>">
    <script src="https://unpkg.com/json-viewer-js@latest"></script>

</head>
<style>
    #logs td {
        word-wrap: break-word;
        overflow-wrap: break-word;
    }
</style>
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
                        <h5>Quản lí nhật ký hoạt động web</h5>
                    </li>
                </ul>
            </div>
            <%-- Search-bar --%>
            <div class="page-content">
                <div class="table-btn-controls d-flex flex-row text-right p-t-10" style="padding-left: 20px">
                    <form id="filter-form" action="<c:url value="/admin/logs"/>" method="post">
                        <div class="btn-group" role="group">
                            <button name="info" value="0" type="button" class="btn btn-info rounded-pill">An toàn
                            </button>
                            <button name="warning" value="1" type="button" class="btn btn-warning rounded-pill">Cảnh
                                báo
                            </button>
                            <button name="danger" value="2" type="button" class="btn btn-danger rounded-pill">Nguy
                                hiểm
                            </button>
                        </div>
                        <input type="hidden" id="levelInput" name="level" value=""/>
                    </form>
                </div>
                <div class="table-container" style="padding: 10px 20px">
                    <table id="logs" class="display table table-border table-striped"
                           style="width:100%;table-layout: fixed;overflow: hidden;">
                        <thead>
                        <tr>
                            <th>Địa chỉ IP</th>
                            <th>Endpoint</th>
                            <th>Mức độ</th>
                            <th>Trạng thái</th>
                            <th>Giá trị trước</th>
                            <th>Giá trị hiện tại</th>
                            <th>Ngày tạo</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                    <!-- /.col -->
                    <!-- /.row -->
                </div>
            </div>
            <!-- /.page-content -->
        </div>
    </div>
    <!-- /.main-content -->
</div>
<%@ include file="/admin/common/footer.jsp" %>
<script src="<c:url value="/jquey/jquery.min.js"/>"></script>
<script>
    $(document).ready(function () {
        // const jsonViewer = new JSONViewer();
        // document.getElementById("json-container").appendChild(jsonViewer.getContainer());
        const table = new DataTable('#logs', {
            processing: true,
            serverSide: true,
            ajax: {
                url: "/admin/logs",
                type: "POST",
                contentType: "application/json",
                data: function (data) {
                    // Add custom data to the DataTables data object
                    const additionalData = {
                        level: $('#levelInput').val()
                    };
                    // Merge additionalData into the existing data object
                    const requestData = $.extend({}, data, additionalData);

                    // Log the request data for debugging
                    console.log("Request Data:", requestData);

                    return JSON.stringify(requestData);
                },
            },
            columns: [
                {
                    data: 'ipAddress',
                    render: function (data) {
                        if (data == null) {
                            return '127.0.0.1';
                        } else {
                            // jsonViewer.showJSON(data, null, 4);
                            // return jsonViewer.getContainer();
                            return data;
                        }
                    }
                },
                {data: 'endpoint'},
                {
                    data: 'level',
                    render: function (data) {
                        if (data == 0) {
                            return '<span class="p-1 px-2 bg-info rounded-pill text-white">an toàn</span>';
                        } else if (data == 1) {
                            return '<span class="p-1 px-2 bg-warning rounded-pill text-white">cảnh báo</span>';
                        } else if (data == 2) {
                            return '<span class="p-1 px-2 bg-danger rounded-pill text-white">nguy hiểm</span>';
                        }
                    }
                },
                {data: 'status'},
                {
                    data: 'preValue',
                    render: function (data) {
                        if (data == null) {
                            return '';
                        } else {
                            // jsonViewer.showJSON(data, null, 4);
                            // return jsonViewer.getContainer();
                            return data;
                        }
                    }
                },
                {data: 'value',
                    render: function (data) {
                        if (data == null) {
                            return '';
                        } else {
                            // jsonViewer.showJSON(data, null, 4);
                            // return jsonViewer.getContainer();
                            return data;
                        }
                    }},
                {data: 'createAt'},
            ],
            columnDefs: [
                {"width": "100px", "targets": 0},  // Địa chỉ IP
                {"width": "100px", "targets": 1}, // Endpoint
                {"width": "80px", "targets": 2},  // Mức độ
                {"width": "80px", "targets": 3},  // Trạng thái
                {"width": "200px", "targets": 4}, // Giá trị trước
                {"width": "200px", "targets": 5}, // Giá trị hiện tại
                {"width": "100px", "targets": 6}   // Ngày tạo
            ]
        });

        // Xử lý sự kiện click trên các nút trong form
        $('.btn-group button').on('click', function () {
            const value = $(this).val();
            $('#levelInput').val(value);
            console.log("btn-group button : " + value)
            table.ajax.reload(); // Reload DataTable với dữ liệu mới
        });

    });
</script>

</body>
</html>
