<%@ page import="com.commenau.encryptMode.RSA" %>
<%@ page import="java.util.Base64" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<!DOCTYPE html>--%>
<html class="no-js" lang="en">

<head>
    <meta charset="UTF-8">
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"><![endif]-->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="images/favicon.png" rel="shortcut icon">
    <title>Quản lý bảo mật</title>
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <%--    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">--%>
    <%--    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/js/bootstrap.bundle.js"/>">--%>
    <%--    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/js/bootstrap.min.js"/>">--%>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
            integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
            crossorigin="anonymous"></script>

    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash-manage-order.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/dash-edit-profile.css"/>">

    <!-- for chat -->
    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">
</head>

<body class="config">

<!--====== Main App ======-->
<div id="app">

    <!--====== Main Header ======-->
    <%@ include file="/customer/common/header.jsp" %>
    <jsp:include page="common/chat.jsp"></jsp:include>
    <!--====== End - Main Header ======-->

    <!--====== App Content ======-->
    <div class="container-content mt-5">
        <div class="app-content">
            <!--====== Section 2 ======-->
            <div class="u-s-p-b-60">

                <!--====== Section Content ======-->
                <div class="section__content">
                    <div class="dash">
                        <div class="container">
                            <div class="row">
                                <div class="col-lg-3 col-md-12">

                                    <!--====== Dashboard Features ======-->
                                    <div class="dash__box dash__box--bg-white dash__box--shadow u-s-m-b-30">
                                        <div class="dash__pad-1">

                                            <span class="dash__text u-s-m-b-16">Xin Chào, ${auth.fullName()}</span>
                                            <ul class="dash__f-list">
                                                <li><a href="<c:url value="/profile"/>">Thông tin tài khoản</a></li>
                                                <li><a href="<c:url value="/invoices"/>">Đơn đặt hàng</a></li>
                                                <li><a class="dash-active" href="<c:url value="/keypair-manager"/>">Quản
                                                    lý khoá bảo mật</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                    <!--====== End - Dashboard Features ======-->
                                </div>
                                <div class="col-lg-9">
                                    <div class="dash__box dash__box--shadow dash__box--radius dash__box--bg-white">
                                        <div id="dash-content" class="dash__pad-2">
                                            <h1 class="dash__h1 u-s-m-b-14">Quản lý khoá bảo mật</h1>

                                            <%-- Button groups --%>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <div>
                                                    <button id="btn_keypair_create" class="btn btn-primary"
                                                            type="button" data-bs-toggle="modal"
                                                            data-bs-target="#modal_keypair_create">Tạo keypair
                                                    </button>
                                                    <button id="btn_keypair_load" class="btn btn-outline-primary"
                                                            type="button" data-bs-toggle="tooltip"
                                                            data-bs-placement="top" data-bs-title="Tooltip on top">Tải
                                                        key
                                                    </button>
                                                </div>
                                                <button id="btn_keypair_report" class="btn btn-danger" type="button"
                                                        data-bs-toggle="tooltip" data-bs-placement="top"
                                                        data-bs-title="Tooltip on top">Báo cáo
                                                </button>
                                            </div>

                                            <table id="table_id" class="table table-striped">
                                                <thead>
                                                <tr>
                                                    <th scope="col">Tên</th>
                                                    <th scope="col">Hạn sử dụng</th>
                                                    <th scope="col">Thời gian tạo</th>
                                                    <th scope="col">Trạng thái</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <th scope="row">1</th>
                                                    <td>Mark</td>
                                                    <td>Otto</td>
                                                    <td>@mdo</td>
                                                </tr>
                                                <tr>
                                                    <th scope="row">2</th>
                                                    <td>Jacob</td>
                                                    <td>Thornton</td>
                                                    <td>@fat</td>
                                                </tr>
                                                <tr>
                                                    <th scope="row">3</th>
                                                    <td>Larry the Bird</td>
                                                    <td>Larry the Bird</td>
                                                    <td>@twitter</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--====== End - Section Content ======-->
            </div>
            <!--====== End - Section 2 ======-->
        </div>
    </div>
    <!--====== End - App Content ======-->

    <!--====== Main Footer ======-->
    <%@include file="/customer/common/footer.jsp" %>
</div>
<!--====== End - Main App ======-->
<script src="<c:url value="/validate/validator.js"/>"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
    $(document).ready(function () {

        // Them Datatable vao table#table_id
        $('#table_id').DataTable();

        $('#btn_keypair_create').on('click', async function () {
            console.log(`Button Create KeyPair is clicked`);
            const userConfirm = confirm("Xác nhận tạo keypair?");
            if (userConfirm) {
                createKeyPair();
            }
        });

        $('#btn_keypair_load').on('click', function () {
            getPrivateKeyBinary()
        })

        $('#btn_keypair_report').on('click', function () {
        })
    })
</script>
<%-- Load Javascript for this file--%>
<script src="<c:url value="/customer/js/keypair-manager.js"/>"></script>
<%--CryptoJS--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
<%--JSEncrypt--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jsencrypt/3.0.0-beta.1/jsencrypt.min.js"></script>
<%--Datatables--%>
<script src="https://cdn.datatables.net/2.1.8/js/dataTables.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.min.css"/>
<%--Bootstrap toggle--%>
<script>
    const myModal = document.getElementById('myModal')
    const myInput = document.getElementById('myInput')

    myModal.addEventListener('shown.bs.modal', () => {
        myInput.focus()
    })
</script>
</body>

</html>