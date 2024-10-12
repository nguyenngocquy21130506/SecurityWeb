<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.commenau.constant.SystemConstant" %>
<style>
    ul,
    li {
        padding: 0px;
        margin: 0px;
        text-decoration: none;
    }

    .product-link {
        padding: 10px;
        display: block;
        color: grey;
        font-size: 14px;
        cursor: pointer;
    }

    .product-link:hover {
        background-color: gainsboro;
    }

    img.voucher {
        width: 300px;
        position: absolute;
        display: none;
        top: -60px;
        left: -60px;
    }

    img.voucher.show {
        display: block;
        animation: scale 2s infinite ease-in-out alternate;
    }

    @keyframes scale {
        0% {
            transform: scale(1);
        }
        50% {
            transform: scale(1.1);
        }
        100% {
            transform: scale(1);
        }
    }
</style>
<header class="header--style-1 header--box-shadow">
    <!--====== Nav 1 ======-->
    <nav class="primary-nav primary-nav-wrapper--border">
        <div class="container">
            <div class="primary-nav">
                <a class="main-logo" href="<c:url value="/home"/>">
                    <img src="<c:url value="/customer/images/logo/logo-3.jpg"/>" alt=""></a>

                <!--====== Search Form ======-->
                <div class="main-form" style="margin-right: -60px">
                    <input class="input-text input-text--border-radius input-text--style-1" type="text"
                           id="main-search" placeholder="Tìm kiếm món ăn">
                    <ul style="width: 100%;
                        position: absolute;
                        background: white;
                        /*border: 1px solid lightgray;*/
                        /*border-top: 0px;*/
                        z-index: 1000;"
                        id="searchResults">
                    </ul>
                </div>
                <!--====== End - Search Form ======-->

                <div class="" id="navigation">
                    <!--====== List ======-->
                    <ul class="search ah-list ah-list--design1 ah-list--link-color-secondary">
                        <li class="has-dropdown" data-tooltip="tooltip" data-placement="left">
                            <a><i class="fa-regular fa-circle-user"></i>
                                <c:if test="${auth!=null}">
                                    ${auth.firstName}
                                </c:if>
                            </a>
                            <ul class="sub-menu" style="width:120px">
                                <c:if test="${auth==null}">
                                    <li>
                                        <a href="<c:url value="/signup"/>"><i class="fa-solid fa-user-plus m-r-6"></i>
                                            <span>Đăng kí</span></a>
                                    </li>
                                    <li>
                                        <a href="<c:url value="/login"/>"><i class="fa-solid fa-lock m-r-6"></i>
                                            <span>Đăng nhập</span></a>
                                    </li>
                                </c:if>
                                <c:if test="${auth!=null}">
                                    <li>
                                        <a href="<c:url value="/profile"/>"><i
                                                class="fa-solid fa-circle-user m-r-6"></i>
                                            <span>Tài khoản</span></a>
                                    </li>
                                    <li>
                                        <a href="<c:url value="/logout"/>"><i
                                                class="fa-solid fa-lock-open m-r-6"></i>
                                            <span>Đăng xuất</span></a>
                                    </li>
                                </c:if>
                            </ul>
                        </li>
                        <li data-tooltip="tooltip" data-placement="left">
                            <a href="tel:+0900901904"><i class="fa-solid fa-phone-volume"></i></a>
                        </li>
                        <li data-tooltip="tooltip" data-placement="left">
                            <a href="mailto:contact@domain.com"><i class="far fa-envelope"></i></a>
                        </li>
                    </ul>
                    <!--====== End - List ======-->
                </div>
            </div>
        </div>
    </nav>
    <!--====== End - Nav 1 ======-->

    <!--====== Nav 2 ======-->
    <nav class="secondary-nav-wrapper">
        <div class="container">
            <div class="secondary-nav">
                <div style="width: 92px; height: 67px;">
                    <img class="voucher" src="<c:url value="/customer/images/logo/voucher.png"/>"></div>
                <div class="" id="navigation2">
                    <!--====== Menu ======-->
                    <ul class="ah-list ah-list--design2 ah-list--link-color-secondary ">
                        <li>
                            <a href="<c:url value="/menu"/>">THỰC ĐƠN</a>
                        </li>
                        <li>
                            <a href="<c:url value="/blogs"/>">BÀI VIẾT</a>
                        </li>
                        <li>
                            <a href="<c:url value="/contacts"/>">LIÊN LẠC</a>
                        </li>
                    </ul>

                    <!--====== End - Menu ======-->
                </div>
                <div class="" id="navigation3">
                    <ul class="ah-list ah-list--design1 ah-list--link-color-secondary">
                        <li>
                            <a href="<c:url value="/home"/>"><i class="fa-solid fa-house"></i></a>
                        </li>
                        <li class="has-dropdown">
                            <span class="numWishlistItem"
                                  style="
                                  <c:if test="${SystemConstant.AUTH != null}">'display:block;'</c:if>
                                          width:15px;height:15px; text-align: center; background: #ff4500; border-radius: 50%;position:absolute;right: 5px;top:15px; font-size: 11px;color: white;"></span>
                            <a href="<c:url value="/wishlist"/>"><i class="far fa-heart"></i></a>
                        </li>
                        <li class="has-dropdown">
                                <span class="numCartItem"
                                      style="
                                      <c:if test="${SystemConstant.AUTH != null}">'display:block;'</c:if>
                                              width:15px;height:15px; text-align: center; background: #ff4500; border-radius: 50%;position:absolute;right: 5px;top:15px; font-size: 11px;color: white;"></span>
                            <a href="<c:url value="/carts"/>"><i class="fas fa-shopping-bag"></i></a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
    <!--====== End - Nav 2 ======-->
</header>
<script>
    $(document).ready(function () {
        var $searchInput = $("#main-search");
        var $searchResults = $("#searchResults");

        // Event listener for the input field
        $(document).on("mousedown", function (event) {
            // Kiểm tra xem phần tử được click có là con của #yourDiv hay không
            if ((!$searchResults.is(event.target) && !$searchResults.has(event.target).length) || ($searchInput.is(event.target) && !$searchInput.has(event.target).length)) {
                $searchInput.val("");
                $searchResults.empty();
            }
        });

        $searchInput.on("input", function () {
            // Clear previous results
            // Get the input value
            var query = $(this).val().toLowerCase();
            if (query !== '') {
                $.ajax({
                    type: "GET",
                    url: "http://localhost:8080/search?query=" + query,
                    contentType: "application/x-www-form-urlencoded; charset=UTF-16",
                    // Serialize dữ liệu biểu mẫu
                    success: function (response) {
                        $searchResults.empty();
                        var data = JSON.parse(response);
                        console.log(data)
                        data.forEach(element => {
                            var link = "/product/" + element.productId;
                            $searchResults.append("<a href='${pageContext.request.contextPath}" + link + "' class='product-link'>" + element.productName + "</a>");
                            $('#searchResults li:gt(7)').remove();
                        });
                    }
                });
            }
        });
        const holidays = [
            '01-01', // New Year's Day
            '14-02', // Lễ Tình nhân
            '08-03', // Ngày Quốc tế Phụ nữ
            '14-03', // Lễ Tình nhân Trắng
            '30-04', // Ngày Giải phóng miền Nam
            '01-05', // Ngày Quốc tế Lao động
            '02-09', // Ngày Quốc khánh
            '20-10', // Ngày Phụ nữ Việt Nam
            '20-11', // Ngày Nhà giáo Việt Nam
            '24-12', // Noel
            '02-02',
            '03-03',
            '04-04',
            '05-05',
            '06-06',
            '07-07',
            '08-08',
            '09-09',
            '10-10',
            '11-11',
            '12-12'
        ];

        const today = new Date();
        const month = String(today.getMonth() + 1).padStart(2, '0'); // Lấy tháng hiện tại (bắt đầu từ 0)
        const day = String(today.getDate()).padStart(2, '0'); // Lấy ngày hiện tại
        const todayStr = month + '-' + day; // Định dạng mm-dd
        const userIcon = document.querySelector('img.voucher');
        if (holidays.includes(todayStr)) {
            userIcon.classList.add("show");
        }

        let numCartItem = $('span.numCartItem')[0];
        let numWishlistItem = $('span.numWishlistItem')[0];
        $.ajax({
            type: "POST",
            url: "/checkNumItem",
            contentType: "application/x-www-form-urlencoded; charset=UTF-16",
            // Serialize dữ liệu biểu mẫu
            success: function (response) {
                let respNumCartItem = response.toString().substring(1,response.toString().indexOf("-"));
                let respNumWishlistItem = response.toString().substring(response.toString().indexOf("-")+1,response.toString().length-1);
                console.log(respNumCartItem + "/" + respNumWishlistItem)
                numCartItem.style.display = 'block';
                numWishlistItem.style.display = 'block';
                numCartItem.style.width = '15px';
                numWishlistItem.style.width = '15px';
                numCartItem.textContent = respNumCartItem;
                numWishlistItem.textContent = respNumWishlistItem;
            },
            error: function (){
                numCartItem.style.display = 'none';
                numWishlistItem.style.display = 'none';
            }
        });
    });

</script>
