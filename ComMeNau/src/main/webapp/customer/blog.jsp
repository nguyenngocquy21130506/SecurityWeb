<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<!DOCTYPE html>--%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Bài viết</title>

    <!--====== font-awesome - bootstrap ======-->
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/blog.css"/> ">

    <!-- for chat -->
    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">
</head>

<body>

<!--====== Main App ======-->
<div id="app">

    <!--====== Main Header ======-->
    <%@ include file="/customer/common/header.jsp" %>
    <jsp:include page="common/chat.jsp"></jsp:include>
    <!--====== End - Main Header ======-->

    <!--====== App Content ======-->
    <div class="app-content">

        <!--====== Section 1 ======-->
        <div class="u-s-p-y-90">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 col-md-4 col-sm-12">
                        <div class="blog-w-master">
                            <div class="u-s-m-b-60">
                                <div class="blog-w">

                                    <span class="blog-w__h">TÌM KIẾM</span>
                                    <form class="blog-search-form" method="get" action="<c:url value="/blogs"/> ">
                                        <label for="post-search"></label>
                                        <input class="input-text input-text--primary-style" type="text" value="${inputKey}"
                                               id="post-search" name="inputData" placeholder="Search" required>
                                        <button class="btn btn--icon" type="submit">
                                            <i class="fas fa-search"></i>
                                        </button>
                                    </form>
                                </div>
                            </div>
                            <div class="u-s-m-b-60">
                                <div class="blog-w">

                                    <span class="blog-w__h">BÀI ĐĂNG GẦN ĐÂY</span>
                                    <ul class="blog-w__b-l">
                                        <c:forEach var="blog" items="${requestScope.listBlog}">
                                            <li>
                                                <div class="b-l__block">
                                                    <div class="b-1__head">
                                                        <img src="<c:url value="/images/blogs/${blog.image}"/>" alt="">
                                                        <div class="b-1__right">
                                                            <div class="b-l__date">
                                                                    <%-- Dung tablib fmt de format Date--%>
                                                                <fmt:formatDate value="${blog.createdAt}"
                                                                                pattern="dd 'Tháng' MM yyyy"
                                                                                var="formattedDate"/>
                                                                <span>${formattedDate}</span>
                                                            </div>
                                                            <span class="b-l__h">

                                                                <a href="<c:url value="/blog-detail?id=${blog.id}"/> ">${blog.title}</a>
                                                            </span>
                                                        </div>
                                                    </div>
                                                    <span class="b-l__p">${blog.shortDescription}
                                                    </span>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-9 col-md-8 col-sm-12">
                        <c:forEach var="blogs" items="${requestScope.listBlogs}">
                            <div class="bp bp--img u-s-m-b-30">
                                <div class="bp__wrap">
                                    <div class="bp__thumbnail">

                                        <!--====== Image Code ======-->

                                        <a class="aspect aspect--bg-grey aspect--1366-768 u-d-block"
                                           href="<c:url value="/blog-detail?id=${blogs.id}"/>">

                                            <img class="aspect__img" src="<c:url value="/images/blogs/${blogs.image}"/>" alt=""></a>
                                        <!--====== End - Image Code ======-->
                                    </div>
                                    <div class="bp__info-wrap">
                                        <div class="bp__stat">

                                            <span class="bp__stat-wrap">

                                                <span class="bp__publish-date">

                                                    <a href="<c:url value="/blog-detail?id=${blogs.id}"></c:url>">
                                                        <fmt:formatDate value="${blogs.createdAt}"
                                                                        pattern="dd 'Tháng' MM yyyy"
                                                                        var="formattedDate"/>
                                                            <span>${formattedDate}</span>
                                                        </a></span></span>

                                            <span class="bp__stat-wrap">
                                                <span class="bp__comment">
                                                    <a href="<c:url value="/blog-detail?id=${blogs.id}"></c:url>"><i class="far fa-comments u-s-m-r-4"></i>
                                                        <span>${blogs.numReviews}</span></a></span></span>

                                        </div>

                                        <span class="bp__h1">

                                            <a href="<c:url value="/blog-detail?id=${blogs.id}"/>">${blogs.title}</a></span>
                                        <p class="bp__p">${blogs.shortDescription}</p>
                                        <div class="gl-l-r">
                                            <div>

                                                <span class="bp__read-more">

                                                    <a href="<c:url value="/blog-detail?id=${blogs.id}"></c:url>">ĐỌC THÊM</a></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <nav class="post-center-wrap u-s-p-y-60">

                            <!--====== Pagination ======-->
                            <ul class="blog-pg">
                                <c:set var="currentPage" value="${pageIndex}"/>
                                <c:set var="begin" value="${currentPage<=3 ? 1 : (maxPage <= 4 ? 1 : (currentPage>maxPage-3 ? maxPage-4 : currentPage-2))}"/>
                                <c:forEach var="index" begin="${begin}" end="${maxPage}"
                                           step="1">
                                    <c:if test="${index < (begin + 5)}">
                                        <li class="<c:if test="${pageIndex == index}">blog-pg--active</c:if>">
                                            <a href="<c:url value="/blogs?pageIndex=${index}"/>">${index}</a>
                                        </li>
                                    </c:if>
                                </c:forEach>
                                <%--<c:forEach var="num" begin = "1" end="${maxPage}">
                                    <li class="<c:if test="${(num)==pageIndex}">blog-pg--active</c:if>">
                                        <a href="<c:url value="/blogs?pageIndex=${num}"/>">${num}</a>
                                    </li>
                                </c:forEach>--%>
                            </ul>
                            <!--====== End - Pagination ======-->
                        </nav>
                    </div>
                </div>
            </div>
        </div>
        <!--====== End - Section 1 ======-->
    </div>
    <!--====== End - App Content ======-->


    <!--====== Main Footer ======-->
    <%@include file="/customer/common/footer.jsp" %>
</div>
<!--====== End - Main App ======-->

</body>

</html>