<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="navbar" class="navbar navbar-default">
    <div class="navbar-container d-flex flex-row justify-content-between w-100" id="navbar-container ">
        <div>
            <a href="<c:url value="/admin/home"/>" class="navbar-brand"> <small> <i class="fa fa-leaf"></i>Admin
            </small>
            </a>
        </div>

        <div>
            <ul class="nav ace-nav">
                <li class="light-blue">
                    <span class="user-info"> Xin chào, ${sessionScope.auth.fullName()}</span>
                </li>
                <li class="">
                    <a href="<c:url value="/logout"/>" class="btn-signout"><span class="user-info">Đăng xuất</span> </a>
                </li>
            </ul>
        </div>
    </div>
    <!-- /.navbar-container -->
</div>