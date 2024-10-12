<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Huy11
  Date: 20/12/2023
  Time: 12:33 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Thực Đơn</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/filter.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/pagination.css"/>">

    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick-theme.css"/>
    <script src="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/fancybox/3.5.7/jquery.fancybox.min.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/fancybox/3.5.7/jquery.fancybox.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/paginationjs/2.1.5/pagination.js"></script>

    <!-- Toastr CSS -->
    <link rel="stylesheet" type="text/css"
          href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css">

    <!-- Toastr JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>

    <!-- for chat -->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">
</head>
<body class="config">

<!--====== Main App ======-->
<div id="app">
    <c:set var="product" value="${ requestScope.product}"/>
    <fmt:setLocale value="vi_VN"/>
    <!--====== Main Header ======-->
    <jsp:include page="common/header.jsp"></jsp:include>
    <jsp:include page="common/chat.jsp"></jsp:include>
    <!--====== End - Main Header ======-->


    <!--====== App Content ======-->
    <div class="app-content">

        <!--====== Section 1 ======-->
        <div class="u-s-p-y-90">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 col-md-12">
                        <div class="shop-w-master">
                            <h1 class="shop-w-master__heading u-s-m-b-30"><i class="fas fa-filter u-s-m-r-8"></i>

                                <span>LỌC</span>
                            </h1>
                            <div class="shop-w-master__sidebar sidebar--bg-snow">
                                <div class="u-s-m-b-30">
                                    <div class="shop-w">
                                        <div class="shop-w__intro-wrap">
                                            <h1 class="shop-w__h">PHÂN LOẠI</h1>

                                            <!-- <span class="fas fa-minus shop-w__toggle" data-target="#s-category" data-toggle="collapse"></span> -->
                                        </div>
                                        <div class="shop-w__wrap collapse show" id="s-category">
                                            <ul class="shop-w__category-list gl-scroll">
                                                <li class="">
                                                    <a class="has-list"  href="<c:url value='/menu'/>">TẤT CẢ</a>
                                                </li>
                                                <c:forEach var="category" items="${requestScope.categories}">
                                                    <li class="">
                                                        <a class="has-list"  href="<c:url value='/menu?categoryId=${category.id}'/>">${category.name}</a>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-9 col-md-12">
                        <div class="shop-p">
                            <div class="shop-p__toolbar u-s-m-b-30">
                                <div class="shop-p__tool-style">
                                    <div class="tool-style__group u-s-m-b-8">

                                    </div>
                                    <form>
                                        <div class="tool-style__form-wrap">

                                            <div class="u-s-m-b-8"><select id="sortingOptions"
                                                                           class="select-box select-box--transparent-b-2">
                                                <option sortBy="createdAt" sort="desc" selected>Sắp xếp: Mặt hàng mới
                                                    nhất
                                                </option>
                                                <option sortBy="rate" sort="desc">Sắp xếp: Đánh giá tốt nhất</option>
                                                <option sortBy="price" sort="asc">Sắp xếp: Giá thấp nhất</option>
                                                <option sortBy="price" sort="desc">Sắp xếp: Giá cao nhất</option>
                                            </select></div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <div class="shop-p__collection">
                                <div class="products row is-grid-active">


                                </div>
                            </div>
                            <div class="u-s-p-y-60">
                                <!--====== Pagination ======-->
                                <div id="pagination-container">

                                </div>
                                <!--====== End - Pagination ======-->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--====== End - Section 1 ======-->
    </div>
    <!--====== End - App Content ======-->


    <!--====== Main Footer ======-->
    <jsp:include page="common/footer.jsp"></jsp:include>

</div>
<!--====== End - Main App ======-->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>

    var categoryId = window.location.search.substring(1).split("=")[1];
    const buttons = document.querySelectorAll('.has-list');
    var reviewSize = ${requestScope.total};
    var sortBy = undefined;
    var sort = undefined;
    if (!categoryId) {
        categoryId = 0;
    }

    function markLink(categoryId){
        var links = $(".has-list")
        for(let i = 0 ; i < links.length ; i++){
            if(categoryId ==0){
                links.eq(0).addClass("selected")
                break;
            }
            else if(i == categoryId){
                links.eq(i).addClass("selected")
                break;
            }
        }
    }
    markLink(categoryId)

    $('#sortingOptions').on('change', function() {
        sortBy = $(this).children(":selected").attr("sortBy")
        sort = $(this).children(":selected").attr("sort")
        loadProducts(1);
    });

    function loadProducts(pagination) {
        var url = "";
        if (sortBy != undefined) {
            url = "http://localhost:8080/menu/filter?id=" + categoryId + "&size=9&page=" + pagination + "&sortBy=" + sortBy + "&sort=" + sort
        } else {
            url = "http://localhost:8080/menu/filter?id=" + categoryId + "&size=9&page=" + pagination;
        }
        $.ajax({
            type: "GET",
            url: url,
            contentType: "application/x-www-form-urlencoded; charset=UTF-16",
            // Serialize dữ liệu biểu mẫu
            success: function (response) {
                var re = JSON.parse(response);
                $(".products").empty();
                re.forEach(n => {
                    var star = ``;
                    for (let i = 0; i < n.rating; i++) {
                        star += ` <i class="fas fa-star"></i>`
                    }
                    for (let i = 0; i < 5 - n.rating; i++) {
                        star += ` <i class="far fa-star"></i>`
                    }
                    var discount = formatCurrency(n.price * (1 - n.discount));
                    var price = formatCurrency(n.price);

                    var wishlist;
                    if (n.wishlist) {
                        wishlist = "fas";
                    } else {
                        wishlist = "far";
                    }
                    var buttonContent = `<button class="btn--e-brand-b-2 btn-add-cart" data-input-id="` + n.id + `">THÊM VÀO GIỎ HÀNG</button>`;
                    if (n.available <= 0) {
                        buttonContent = `<button class="btn btn--e-brand-b-2" disabled>HẾT HÀNG</button>`;
                    }
                    var data = `<div class="col-lg-4 col-md-6 col-sm-6">
                                            <div class="product-m">
                                                <div class="product-m__thumb">

                                                    <a class="aspect aspect--bg-grey aspect--square u-d-block"
                                                       href="/product/` + n.id + `">

                                                        <img class="aspect__img" src="images/products/` + n.avatar + `"
                                                             alt=""></a>

                                                    <div class="product-m__add-cart">` + buttonContent + `
                                                    </div>
                                                </div>
                                                <div class="product-m__content">
                                                    <div class="product-m__category">

                                                        <a href="shop-side-version-2.html">` + n.categoryName + `</a>
                                                    </div>
                                                    <div class="product-m__name">

                                                        <a href="product-detail.html">` + n.productName + `</a>
                                                    </div>
                                                     <div class="product-m__category">

                                                        <a href="product-detail.html"> lượt xem: ` + n.view + `</a>
                                                    </div>
                                                    <div class="product-m__rating gl-rating-style">
                                                       ` + star + `

                                                        <span class="product-m__review">(` + n.amountOfReview + `)</span>
                                                    </div>
                                                    <span class="product-o__price">`+ discount +`
                                                        <span class="product-o__discount">`+ price +`</span>
                                                     </span>
                                                    <div class="product-m__hover">
                                                        <div class="product-m__preview-description">

                                                            <span>` + n.description + `</span>
                                                        </div>
                                                        <div class="product-m__wishlist">
                                                            <i  productId=` + n.id + ` class="wishlist ` + wishlist + ` fa-heart" href="#" data-tooltip="tooltip"
                                                               data-placement="top" title="Add to Wishlist"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>`
                    $(".products").append(data);
                })


                var userId = ${sessionScope.auth.id}+"";
                $('.wishlist').click(function () {
                    var edit = $(this);
                    var method = "";
                    if (edit.hasClass("far")) {
                        method = "POST"
                    } else {
                        method = "DELETE"
                    }

                    $.ajax({
                        type: method,
                        url: "http://localhost:8080/wishlist?productId=" + edit.attr('productId') + "&userId=" + userId,
                        contentType: "application/x-www-form-urlencoded; charset=UTF-16",
                        // Serialize dữ liệu biểu mẫu
                        success: function (response) {
                            if (edit.hasClass("far")) {
                                edit.removeClass("far")
                                edit.addClass("fas")
                            } else {
                                edit.removeClass("fas")
                                edit.addClass("far")
                            }

                        },
                        error: function (error) {

                        }

                    });
                });
            },
            error: function (error) {

            }

        });
    }


    loadProducts(1);

    function roundPrice(price) {
        return Math.round(price / 1000) * 1000;
    }

    function formatCurrency(amount) {
        const roundedAmount = roundPrice(amount);

        const formatter = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        });

        return formatter.format(roundedAmount);
    }

    $(document).ready(function () {
        const data = Array.from({length: reviewSize}, (_, index) => `Item ${index + 1}`);

        // Cấu hình và kích hoạt Pagination.js
        $('#pagination-container').pagination({
            dataSource: data,
            pageSize: 9,  // Số lượng mục trên mỗi trang
            callback: function (data, pagination) {
                loadProducts(pagination.pageNumber);
            },
        });


        // Sử dụng hàm thông thường thay vì lambda function
        buttons.forEach(function (button) {
            button.addEventListener('click', function () {
                $(".shop-w__category-list li a").removeClass("selected");
                $(this).addClass("selected");
            });
        });


    });

    $(document).on('click', '.btn-add-cart', function () {
        var inputData = {};
        inputData['productId'] = $(this).data('input-id');
        console.log(inputData);
        // using Ajax to send data to server
        $.ajax({
            type: "POST",
            url: "<c:url value="/carts"/>",
            data: JSON.stringify(inputData),
            contentType: "application/json; charset=utf-8",
            success: function () {
                Swal.fire({
                    icon: "success",
                    title: "Đã thêm vào giỏ hàng",
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
            },
            error: function (error) {
                console.log("Đã xảy ra lỗi trong quá trình gửi dữ liệu: " + error);
                Swal.fire({
                    icon: "success",
                    title: "Thêm vào giỏ hàng thất bại",
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


</script>
</body>
</html>
