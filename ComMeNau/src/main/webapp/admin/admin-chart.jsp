<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý thể loại</title>
    <link href="<c:url value="/customer/images/favicon.png"/>" rel="shortcut icon">
    <link rel="stylesheet" href="<c:url value="/font-awesome/fontawesome-free-6.4.2-web/css/all.min.css"/>">
    <link rel="stylesheet" href="<c:url value="/boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css"/>">
    <script src="<c:url value="/boostrap/bootstrap-5.3.2-dist/js/bootstrap.min.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/admin/css/common.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-product.css"/>">
    <link rel="stylesheet" href="<c:url value="/admin/css/admin-category.css"/>">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            background-color: #f4f4f4;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        h1 {
            text-align: center;
            margin-bottom: 20px;
        }

        .select-container {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }

        select {
            width: 150px;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            background-color: #fff;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            appearance: none;
            -webkit-appearance: none;
            -moz-appearance: none;
        }

        select:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
        }

        canvas {
            display: block;
            max-width: 100%;
            height: auto;
        }

        #searchInput {
            width: 300px;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-bottom: 20px;
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

        <select id="select1">
            <option value="mostSell">Bán nhiều nhất</option>
            <option value="lessSell">Bán ít nhất</option>
            <option value="cancelSell">Hủy nhiều nhất</option>
            <option value="stockSell">Tồn kho nhiều nhất</option>
            <option value="neverSell">Sản phẩm chưa bao giờ được mua</option>
            <option value="againSell">Sản phẩm chưa được mua lại</option>
            <option value="suggest">Đề nghị nhập khoa</option>
            <!-- Add more options as needed -->
        </select>

        <select id="select2">
            <option value="1 day">1 ngày</option>
            <option value="5 day">5 ngày</option>
            <option value="15 day">15 ngày</option>
            <option value="1 month">1 tháng</option>
            <option value="1 year">1 năm</option>
            <!-- Add more options as needed -->
        </select>
        <input type="text" id="searchInput" placeholder="Tên sản phẩm">
        <button id="exportBtn" class="btn btn-secondary text-black">Export</button>
        <canvas id="salesChart"></canvas>

    </div>
    <!-- /.main-content -->
</div>

<!--====== Main Footer ======-->
<%@ include file="/admin/common/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="<c:url value="/jquey/jquery.min.js"/>"></script>
<script>
    var ctx = document.getElementById('salesChart').getContext('2d');
    var salesChart;

    function updateChart(data) {
        var labels = data.map(function (item) {
            return item.name;
        });
        var revenueData = data.map(function (item) {
            return item.amount;
        });

        var colors = [
            'rgba(75, 192, 192, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)',
            'rgba(255, 99, 132, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)',
            'rgba(205, 133, 63, 0.2)',
            'rgba(72, 209, 204, 0.2)',
            'rgba(245, 74, 85, 0.2)',
            'rgba(186, 85, 211, 0.2)'
        ];

        var borderColors = [
            'rgba(75, 192, 192, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(255, 99, 132, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)',
            'rgba(205, 133, 63, 1)',
            'rgba(72, 209, 204, 1)',
            'rgba(245, 74, 85, 1)',
            'rgba(186, 85, 211, 1)'
        ];

        if (salesChart) {
            salesChart.destroy();
        }

        if ($('#select1').val() == 'neverSell' || $('#select1').val() == 'againSell') {
            // Tạo dữ liệu cho biểu đồ "word cloud"
            var data = {
                labels: labels,
                datasets: [{
                    data: revenueData,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)', // Màu nền của biểu đồ
                    borderColor: 'rgba(54, 162, 235, 1)', // Màu viền của biểu đồ
                    borderWidth: 1
                }]
            };

            // Cấu hình các tùy chọn cho biểu đồ
            var options = {
                indexAxis: 'y', // Hiển thị trục y là trục chính
                responsive: true,
                plugins: {
                    legend: {
                        display: false // Ẩn hiển thị chú thích
                    }
                }
            };
            ctx.canvas.height = labels.length * 10; // Thiết lập chiều cao của canvas
            salesChart = new Chart(ctx, {
                type: 'bar',
                data: data,
                options: options
            });
        } else {
            var maxRevenue = revenueData.reduce(function (max, current) {
                return Math.max(max, current);
            }, -Infinity);
            console.log("Giá trị lớn nhất là: " + maxRevenue);
            ctx.canvas.height = (window.innerHeight * 0.1) ;
            salesChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Sales',
                        data: revenueData,
                        backgroundColor: colors,
                        borderColor: borderColors,
                        borderWidth: 2
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        }
    }
    $('#exportBtn').on('click',()=>{
        var select1Value = $('#select1').val();
        var select2Value = $('#select2').val();
        var searchInput = $('#searchInput').val();

        $.ajax({
            url: 'http://localhost:8080/admin/export',
            method: 'GET',
            data: {
                condition: select1Value,
                day: select2Value,
                search: searchInput
            },
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
    function fetchData() {
        var select1Value = $('#select1').val();
        var select2Value = $('#select2').val();
        var searchInput = $('#searchInput').val();

        $.ajax({
            url: 'http://localhost:8080/admin/chartData',
            method: 'GET',
            data: {
                condition: select1Value,
                day: select2Value,
                search: searchInput
            },
            success: function (data) {
                updateChart(data);
            },
            error: function (error) {
                console.error('Error fetching sales data:', error);
            }
        });
    }

    $(document).ready(function () {
        $('#select1, #select2').change(function () {
            fetchData();
        });

        function toggleSelectBasedOnInput() {
            if ($('#searchInput').val() == "") {
                $('#select1').prop('disabled', false);
            } else {
                $('#select1').prop('disabled', true);
            }
        }

        // Check the input value on page load
        toggleSelectBasedOnInput();

        // Check the input value whenever it changes
        $('#searchInput').on('input', function () {
            toggleSelectBasedOnInput();
        });
        $('#searchInput').keypress(function (e) {

            if (e.which === 13) { // Enter key pressed
                fetchData(); // Refresh chart data
            }
        });
        fetchData();
    });
</script>
</body>
</html>