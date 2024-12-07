<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.commenau.util.RoundUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Thanh Toán</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/input.css"/>">
    <link rel="stylesheet" href="<c:url value="/customer/css/checkout.css"/>">

    <!-- for chat -->
    <script src="<c:url value="/jquey/jquery.min.js"/>"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="<c:url value="/customer/css/chat.css"/>">
</head>
<body>
<fmt:setLocale value="vi_VN"/>
<!--====== Main Header ======-->
<%@ include file="/customer/common/header.jsp" %>
<jsp:include page="common/chat.jsp"></jsp:include>
<div class="container-content mt-5">
    <form class="checkout-f__delivery" id="checkout-form">
        <div class="checkout-f d-flex flex-row">
            <div class="col-6 me-5">
                <h1 class="checkout-f__h1">THÔNG TIN VẬN CHUYỂN</h1>

                <div class="flex-fill">
                    <div class="u-s-m-b-15">
                        <label class="gl-label">Họ tên <span class="required-check">*</span></label>
                        <input data-rule="required|containsAllWhitespace" class="input-text input-text--primary-style"
                               type="text" name="fullName" value="${fullName}">
                    </div>
                    <div class="u-s-m-b-15">
                        <label class="gl-label">Số điện thoại <span class="required-check">*</span></label>
                        <input data-rule="required|phone" class="input-text input-text--primary-style" type="text"
                               name="phoneNumber" value="${phoneNumber}">
                    </div>
                    <div class="u-s-m-b-15">
                        <label class="gl-label">Email<span class="required-check">*</span></label>
                        <input data-rule="required|email" class="input-text input-text--primary-style" type="email"
                               name="email" value="${email}">
                    </div>
                    <div class="u-s-m-b-15">
                        <label class="gl-label">Tỉnh/Thành Phố <span class="required-check">*</span></label>
                        <select data-rule="required" id="provinceSelected" class="input-text input-text--primary-style"
                                name="province">
                            <option value="">Chọn tỉnh/thành phố</option>
                        </select>
                    </div>
                    <div class="u-s-m-b-15">
                        <label class="gl-label">Quận/Huyện <span class="required-check">*</span></label>
                        <select data-rule="required" id="districtSelected" class="input-text input-text--primary-style"
                                name="district">
                            <option value="">Quận/Huyện</option>
                        </select>
                    </div>
                    <div class="u-s-m-b-15">
                        <label class="gl-label">Phường/Xã <span class="required-check">*</span></label>
                        <select data-rule="required" id="wardSelected" class="input-text input-text--primary-style"
                                name="ward">
                            <option value="">Chọn phường/xã</option>
                        </select>
                    </div>
                    <div class="u-s-m-b-15">
                        <label class="gl-label">Địa chỉ <span class="required-check">*</span></label>
                        <input data-rule="required|containsAllWhitespace" class="input-text input-text--primary-style"
                               type="text" name="address" value="${address}">
                    </div>
                    <div class="u-s-m-b-10">
                        <label class="gl-label">Ghi chú</label><textarea
                            class="text-area text-area--primary-style" name="note"></textarea>
                    </div>
                </div>

            </div>
            <div class="col-6">
                <h1 class="checkout-f__h1">SƠ LƯỢC ĐƠN HÀNG</h1>

                <div class="o-summary">
                    <div class="o-summary__section u-s-m-b-10">
                        <div class="o-summary__item-wrap gl-scroll">
                            <c:forEach var="item" items="${cartItems}">
                                <div class="o-card">
                                    <div class="o-card__flex">
                                        <div class="o-card__img-wrap">
                                            <img class="u-img-fluid"
                                                 src="<c:url value="/images/products/${item.product.images.get(0)}"/>"
                                                 alt="">
                                        </div>
                                        <div class="o-card__info-wrap">
                                            <span class="o-card__name"><a
                                                    href="#">${item.product.productName}</a></span>
                                            <span class="o-card__quantity">Số lượng x ${item.quantity}</span>
                                            <span class="o-card__price">
                                                <fmt:formatNumber
                                                        value="${RoundUtil.roundPrice(item.product.price * (1 - item.product.discount))}"
                                                        type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                        </span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="o-summary__section u-s-m-b-10">
                        <div class="o-summary__box">
                            <table class="o-summary__table">
                                <tbody>
                                <tr>
                                    <td>Phí vận chuyển</td>
                                    <td id="shippingFee"><fmt:formatNumber value="0"
                                                                           type="currency" maxFractionDigits="0"
                                                                           currencyCode="VND"/></td>
                                </tr>
                                <tr>
                                    <td>Tổng giá thành</td>
                                    <td id="price" data="${totalPrice}">
                                        <fmt:formatNumber value="${totalPrice}"
                                                          type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Tổng đơn hàng</td>
                                    <td id="total">
                                        <fmt:formatNumber value="${totalPrice}"
                                                          type="currency" maxFractionDigits="0" currencyCode="VND"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="u-s-m-b-5">
                        <div class="o-summary__box">
                            <i class="fa-solid fa-tags"></i> <i>Chương trình khuyến mãi được áp dụng cho những <b>ngày
                            lễ</b> hoặc các <b>ngày trùng với tháng</b></i>
                        </div>
                    </div>
                    <div class="o-summary__section u-s-m-b-10">
                        <div class="o-summary__box" style="padding: 10px 20px">
                            <a href="<c:url value="/vouchers"/>" style="
                                color: #ff4500;
                                font-weight: 700;
                                display: flex;
                                justify-content: space-between;"
                               class="<c:if test="${opened == false}">disabled</c:if>"
                            >Áp dụng khuyến mãi
                                <c:if test="${!nameVoucher.equals('')}">
                                    <span style="
                                    color: green;
                                    margin-top: -10px;
                                    margin-left: -200px;
                                    padding-top: -5px;
                                    font-size: 15px;
                                    background: yellow;
                                    border-radius: 50%;
                                    padding: 10px;">${nameVoucher}</span>
                                    <input type="text" value="${cart.getVoucherId()}" id="voucherId" hidden="hidden">
                                </c:if>
                                <i class='fa-solid fa-angle-right'></i></a>
                        </div>
                    </div>
                    <div class="o-summary__section u-s-m-b-10">
                        <div class="o-summary__box">
                            <h1 class="checkout-f__h1">XÁC THỰC</h1>
                            <div class="u-s-m-b-10" style="margin-top: 20px;">
                                <!--====== Signature ======-->
                                <span>Chọn file dữ liệu chữ ký của đơn hàng</span>
                                <div class="o-summary__box d-flex flex">
                                    <input type="file" id="file-signature" name="file-signature" style="display: none;">
                                    <label for="file-signature" class="file-signature">
                                        Tải lên</label>
                                    <span class="file-content" id="file-content">
                                        <!-- Nội dung tệp sẽ được hiển thị tại đây -->
                                    </span>
                                    <span class="view-content-signature">
                                    👁️
                                    </span>
                                </div>
                            </div>
                            <div class="u-s-m-b-10" style="margin-top: 10px;">
                                <!--====== Public key ======-->
                                <span>Chọn khóa công khai dùng xác thực đơn hàng</span>
                                <div class="o-summary__box d-flex flex-column">
                                    <select id="select"
                                            style="padding: 5px 10px;height: 40px; border-radius: 5px;
                                            color: orangered; border-color: orangered">
                                        <option value="0"> Chọn khóa công khai phù hợp với chữ ký!</option>
                                        <c:forEach var="item" items="${publicKeys}">
                                            <option value="${item}">${item}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="o-summary__section u-s-m-b-10">
                        <div class="o-summary__box">
                            <h1 class="checkout-f__h1">THÔNG TIN THANH TOÁN</h1>
                            <div class="u-s-m-b-10 d-flex flex-column" style="margin-top: 20px;">
                                <!--====== Radio Box ======-->
                                <div class="radio-box">
                                    <input type="radio" id="cod" name="paymentMethod" value="COD" checked>
                                    <div class="radio-box__state radio-box__state--primary">
                                        <label class="radio-box__label" for="cod">Thanh toán bằng tiền
                                            mặt
                                        </label>
                                    </div>
                                </div>
                                <!--====== End - Radio Box ======-->
                            </div>
                            <div class="u-s-m-b-10 d-flex flex-column" style="margin-top: 20px;">

                                <!--====== Radio Box ======-->
                                <div class="radio-box">
                                    <input type="radio" id="vnpay" name="paymentMethod" value="VNPAY">
                                    <div class="radio-box__state radio-box__state--primary">
                                        <label class="radio-box__label" for="vnpay">Thanh toán bằng
                                            VNPAY</label>
                                    </div>
                                </div>
                                <!--====== End - Radio Box ======-->
                            </div>
                            <div>
                                <button class="btn btn--e-brand-b-2" id="btn-checkout" type="submit">THANH TOÁN</button>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" name="amount" value="${totalPrice}">
                </div>
                <!--====== End - Order Summary ======-->
            </div>
        </div>
    </form>
</div>
<!--====== End - App Content 0347987462======-->
<!--====== Main Footer ======-->
<%@ include file="/customer/common/footer.jsp" %>
<script src="<c:url value="/jquey/jquery.min.js"/>"></script>
<script src="<c:url value="/validate/validator.js"/>"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    $(document).ready(function () {

        var shippingFee = 0;


        let validate = false;
        // Go to validation
        new Validator(document.querySelector('#checkout-form'), function (err, res) {
            validate = res;
        });


        $('#checkout-form').on('submit', async function (event) {
            event.preventDefault();
            if (validate === true) {
                var formData = $('#checkout-form').serializeArray();
                var isVnPaySelected = $('#vnpay').is(':checked');
                var districtCode = $('#districtSelected option:selected').data("info");
                var wardId = $('#wardSelected option:selected').data("info");
                var voucherId = $('#voucherId').val();
                var paymentUrl = isVnPaySelected ? '<c:url value="/vnpay-payment"/>' : '<c:url value="/payments"/>';
                formData.push({name: "shippingFee", value: shippingFee})
                formData.push({name: "wardCode", value: wardId})
                formData.push({name: "districtCode", value: districtCode})
                formData.push({name: "voucherId", value: voucherId})
                var ajaxConfig = {
                    type: 'POST',
                    url: paymentUrl,
                    dataType: 'json',
                    data: formData
                };

                try {
                    var response = await $.ajax(ajaxConfig);
                    if (isVnPaySelected) {
                        handlePaymentResponse(response);
                    } else {
                        handlePaymentMessage("success", "Thanh toán thành công", '<c:url value="/carts"/>');
                    }
                } catch (error) {
                    handlePaymentMessage("warning", "Thanh toán thất bại!", null);
                }
            } else {
                console.log("form error")
            }
        });

        function handlePaymentResponse(response) {
            if (response.code === "00") {
                window.location.href = response.data;
            } else {
                handlePaymentMessage("warning", "Thanh toán thất bại!", null);
            }
        }

        function handlePaymentMessage(icon, message, url) {
            Swal.fire({
                icon: icon,
                title: message,
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

            if (url !== null && icon === "success") {
                setTimeout(function () {
                    window.location.href = url;
                }, 700);
            }
        }


        <%-- call api handle address --%>
        $.ajax({
            url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province',
            headers: {
                'token': "b5c8bf1a-ea8c-11ee-8bfa-8a2dda8ec551" // Chèn token vào header của yêu cầu
            },
            method: 'GET',
            success: function (response) {
                // Xử lý dữ liệu nhận được từ API
                var provinces = response.data; // Dữ liệu về các quận/huyện


                // Cập nhật danh sách chọn quận/huyện
                var provinceSelect = $('#provinceSelected');
                $.each(provinces, function (index, province) {
                    provinceSelect.append('<option data-info="' + province.ProvinceID + '" value="' + province.ProvinceName + '">' + province.ProvinceName + '</option>');
                });


            },
            error: function (error) {
                console.error('Error loading data: ' + error);
            }
        });
        // Sử dụng jQuery để gửi yêu cầu API và nhận dữ liệu
        $('#provinceSelected').change(function () {
            var provinceId = $('#provinceSelected option:selected').data("info");
            $.ajax({
                url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=' + provinceId,
                headers: {
                    'token': "b5c8bf1a-ea8c-11ee-8bfa-8a2dda8ec551" // Chèn token vào header của yêu cầu
                },
                method: 'GET',
                success: function (response) {
                    // Xử lý dữ liệu nhận được từ API
                    var districts = response.data; // Dữ liệu về các quận/huyện


                    // Cập nhật danh sách chọn quận/huyện
                    var districtSelect = $('#districtSelected');
                    $.each(districts, function (index, district) {
                        districtSelect.append('<option data-info="' + district.DistrictID + '" value="' + district.DistrictName + '">' + district.DistrictName + '</option>');
                    });
                },
                error: function (error) {
                    console.error('Error loading data: ' + error);
                }
            });
        });

        // Sự kiện khi chọn quận/huyện
        $('#districtSelected').change(function () {
            var districtId = $('#districtSelected option:selected').data("info");// Lấy giá trị ID của quận/huyện đã chọn

            // Gửi yêu cầu API để lấy danh sách các phường/xã của quận/huyện đã chọn
            $.ajax({
                url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=' + districtId,
                method: 'GET',
                headers: {
                    'token': "b5c8bf1a-ea8c-11ee-8bfa-8a2dda8ec551" // Chèn token vào header của yêu cầu
                },
                success: function (response) {
                    // Xử lý dữ liệu nhận được từ API
                    var wards = response.data; // Dữ liệu về các phường/xã

                    // Cập nhật danh sách chọn phường/xã
                    var wardSelect = $('#wardSelected');
                    wardSelect.empty(); // Xóa các phần tử hiện có
                    wardSelect.append('<option value="">Chọn phường/xã</option>'); // Thêm tùy chọn mặc định
                    $.each(wards, function (index, ward) {
                        wardSelect.append('<option  data-info="' + ward.WardCode + '" value="' + ward.WardName + '">' + ward.WardName + '</option>');
                    });

                },
                error: function (error) {
                    console.error('Error loading wards: ' + error);
                }
            });
        });
        $('#wardSelected').change(function () {
            var wardId = $('#wardSelected option:selected').data("info");// Lấy giá trị ID của quận/huyện đã chọn
            var districtId = $('#districtSelected option:selected').data("info");
            console.log(wardId + "  " + districtId);
            var jsonData = {
                "from_district_id": 1458,
                "from_ward_code": "21608",
                "service_id": 53320,
                "service_type_id": null,
                "to_district_id": parseInt(districtId),
                "to_ward_code": wardId + "",
                "height": 10,
                "length": 40,
                "weight": 1000,
                "width": 20,
                "insurance_value": 2000,
                "cod_failed_amount": 2000,
                "coupon": null
            };

            // Gửi yêu cầu API để lấy danh sách các phường/xã của quận/huyện đã chọn
            $.ajax({
                url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee',
                method: 'POST',
                headers: {
                    'Token': "b5c8bf1a-ea8c-11ee-8bfa-8a2dda8ec551" // Chèn token vào header của yêu cầu
                },
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(jsonData),
                success: function (response) {
                    shippingFee = parseInt(response.data.total)
                    $("#shippingFee").empty();
                    $("#shippingFee").text(formatCurrencyVND(response.data.total))
                    var price = $("#price").attr("data");
                    $("#total").text(formatCurrencyVND(parseInt(response.data.total) + parseInt(price)))
                },
                error: function (error) {
                    console.error('Error loading wards: ' + error);
                }
            });
        });

        function formatCurrencyVND(amount) {
            // Chuyển đổi số thành chuỗi và thêm dấu phẩy sau mỗi 3 chữ số
            var parts = amount.toString().split(".");
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");

            // Thêm đồng VND vào sau chuỗi đã được định dạng
            return parts.join(".") + " đ";
        }
    });
</script>
<%-- script for load file--%>
<script>
    document.getElementById('load-signature').addEventListener('click', () => {
        document.getElementById('file-signature').click(); // Mở hộp chọn file
    });

    document.getElementById('file-signature').addEventListener('change', (event) => {
        const file = event.target.files[0]; // Lấy file đã chọn
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                const fileContent = e.target.result; // Nội dung file dưới dạng chuỗi
                console.log('Nội dung file:', fileContent);
            };
            reader.readAsText(file); // Đọc file dưới dạng text
        }
    });
</script>
<script>
    document.getElementById("file-signature").addEventListener("change", function (event) {
        const file = event.target.files[0]; // Lấy tệp được tải lên
        const fileContentDiv = document.getElementById("file-content");
        const view = document.querySelector(".view-content-signature");

        if (file) {
            const reader = new FileReader();

            // Khi đọc xong tệp
            reader.onload = function (e) {
                const content = e.target.result; // Nội dung tệp
                fileContentDiv.textContent = content; // Hiển thị nội dung tệp
                view.title = content; // Hiển thị toàn bộ nội dung khi hover
                view.style.display = 'block';
                view.style.cursor = 'pointer';
                view.style.opacity = 0.8;
            };

            // Đọc tệp dưới dạng văn bản
            reader.readAsText(file);
        } else {
            fileContentDiv.textContent = "Không có tệp nào được chọn.";
        }
    });
</script>
</body>
</html>