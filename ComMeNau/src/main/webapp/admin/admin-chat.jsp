<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Huy11
  Date: 13/12/2023
  Time: 11:10 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Chat</title>
    <link rel="stylesheet" href="../boostrap/bootstrap-5.3.2-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="../font-awesome/fontawesome-free-6.4.2-web/css/all.min.css">
    <script src="../boostrap/bootstrap-5.3.2-dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="<c:url value="/admin/css/common.css " />">
    <link rel="stylesheet" href=" <c:url value="/admin/css/chat.css" />">

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
    </style>
</head>

<body class="no-skin">
<!-- Main Header-->
<%@ include file="/admin/common/header.jsp" %>
<!-- end  Main Header-->
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
                        <h5>Quản lý khách hàng</h5>
                    </li>

                    <!-- <li><a href="#">Tables</a></li>
                <li class="active">Simple &amp; Dynamic</li> -->
                </ul>
                <!-- /.breadcrumb -->


                <!-- /.nav-search -->
            </div>

            <div class="page-content p-t-20" style="height: 566px;">

                <div class="d-flex flex-column align-items-center w-100 ">

                    <!-- PAGE CONTENT BEGINS -->
                    <main class="d-flex w-100" style="height: 550px">

                        <div style="max-height: 550px;min-height:550px;overflow-y: auto" class="col-4 border-right">

                            <div class="px-4 d-none d-md-block">
                                <div class="d-flex align-items-center">
                                    <div class="flex-grow-1 " style="position: relative">
                                        <input id="main-search" type="text" class="form-control mt-3"
                                               placeholder="Search...">
                                        <ul style="width: 100%;
                        position: absolute;
                        background: white;
                        /*border: 1px solid lightgray;*/
                        /*border-top: 0px;*/
                        z-index: 1000;"
                                            id="searchResults">
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div id="users" class="mt-3">

                            </div>
                            <hr class="d-block d-lg-none mt-1 mb-0">
                        </div>
                        <div class="chat-container col-8  border border-left"
                             style="display: none ; min-height: 540px; max-height: 550px">
                            <div class="py-2 px-4 border-bottom d-none d-lg-block">
                                <div class="d-flex align-items-center py-1">
                                    <div class="position-relative">
                                        <img src="images/avatar-icon.jpg" class="rounded-circle mr-1"
                                             alt="Sharon Lessman"
                                             width="40" height="40">
                                    </div>
                                    <div class="flex-grow-1 pl-3 ms-2">
                                        <strong id="username">None</strong>
                                    </div>

                                </div>
                            </div>

                            <div class="position-relative" style="min-height: 400px">
                                <div class="chat-messages p-4">

                                </div>
                            </div>

                            <div class="flex-grow-0 py-3 px-4 border-top">
                                <div class="d-flex align-items-center justify-content-center">
                                    <input type="text" id="chat-input" placeholder="Send a message..."/>
                                    <button class="chat-submit" id="chat-submit"><i
                                            class="material-icons">send</i></button>
                                </div>
                            </div>
                        </div>

                    </main>
                    <!-- /.row -->


                    <!--hết-->

                    <!-- PAGE CONTENT ENDS -->
                </div>
                <!-- /.col -->


                <!-- /.row -->
            </div>
            <!-- /.page-content -->
        </div>
    </div>
    <!-- /.main-content -->
</div>
<%@ include file="/admin/common/footer.jsp" %>


</body>
<script>
    $(document).ready(function () {
        var userId = ${sessionScope.auth.id};

        var userIdShow = window.location.search.substring(1).split("&")[0].split("=")[1];

        var section = 1;

        const socket = new WebSocket("ws://localhost:8080/chat/" + userId);


        socket.onopen = function (event) {

        };

        function formatTime(date, format) {
            var hours = date.getHours();
            var minutes = date.getMinutes();

            // Thêm số 0 đằng trước nếu giờ, phút hoặc giây chỉ có một chữ số
            hours = hours < 10 ? "0" + hours : hours;
            minutes = minutes < 10 ? "0" + minutes : minutes;


            // Thay thế các phần tử định dạng
            format = format.replace("hh", hours);
            format = format.replace("mm", minutes);

            return format;
        }

        socket.onmessage = function (event) {
            var chatmessage = JSON.parse(event.data);

            // decrypt message at real time

            if (userIdShow == chatmessage.senderId) {
                gennerateMessage(chatmessage.content, 'user', chatmessage.time, false);
                loadUsers(false, false);
            } else {
                loadUsers(false, true);
            }
        };

        socket.onclose = function (event) {

        };

        function updateView(userId) {
            $.ajax({
                type: "PUT",
                url: "http://localhost:8080/message/" + userId + "?ownerId=0",
                contentType: "application/x-www-form-urlencoded; charset=UTF-16",
                // Serialize dữ liệu biểu mẫu
                success: function (response) {

                }
            });
        }

        if (userIdShow != null) {
            loadUsers(false, true)
            updateView(userIdShow)
        } else {
            loadUsers(true, true);
        }

        function loadMessages(userId) {
            return $.ajax({
                type: "GET",
                url: "http://localhost:8080/message/" + userId + "?section=" + section,
                contentType: "application/x-www-form-urlencoded; charset=UTF-16",
                // Serialize dữ liệu biểu mẫu
                success: function (response) {
                    section++;
                    response.forEach(n => {
                        if (n.senderId == 0)
                            gennerateMessage(n.content, 'self', n.time, true)
                        else gennerateMessage(n.content, 'user', n.time, true)
                    })
                    return true;
                }
                , error: function (error) {
                    return false;
                }
            });
        }

        $(".chat-messages").scroll(function () {
            if ($(this).scrollTop() === 0) {
                if (loadMessages(userIdShow)) {
                    updateView();
                    var positionToScroll = $(".chat-messages").find(".chat-message").eq(0).position();
                    $(".chat-messages").scrollTop(positionToScroll.top);
                }
            }
        });

        function loadUsers(loadMessage, mark) {
            $.ajax({
                type: "GET",
                url: "http://localhost:8080/admin/chat/users",
                contentType: "application/x-www-form-urlencoded; charset=UTF-16",
                // Serialize dữ liệu biểu mẫu
                success: function (response) {
                    $("#users").empty();
                    for (let i = response.length - 1; i >= 0; i--) {
                        var time = new Date(response[i].message.time);

                        var noti = !response[i].message.viewed && response[i].message.senderId != 0;

                        var bold = '';
                        var badge = ``;

                        if (noti && mark) {
                            bold = "fw-bold";
                            badge = `<div class="badge bg-success ">!</div>`;
                        }

                        var content = response[i].message.content.length < 30 ? response[i].message.content : response[i].message.content.toString().substr(0, 30).concat("...");

                        var user = ` <a href="/admin/chat?user=` + response[i].id + `" class="user-chat d-flex flex-row justify-content-between px-5 pb-3 border-0">
        <div class="d-flex align-items-start">
        <img src="images/avatar-icon.jpg" class="rounded-circle me-3" alt="Vanessa Tucker"
        width="40" height="40">
        <div class="flex-grow-1 ml-3 ">`
                            + response[i].name +
                            `<div class="` + bold + `">` + content + `</div>
        </div>
        </div>
        <div class="float-right">
        ` + badge + `
        <div class="fw-bold">` + formatTime(time, "hh:mm") + `</div>
        </div>
        </a>`

                        $("#users").append(user);
                        if (userIdShow == response[i].id) {
                            $(".chat-container").css("display", "block")
                            $("#username").text(response[i].name)
                            updateView(userIdShow);
                            loadMessages(userIdShow);
                            $(".chat-messages").stop().animate({scrollTop: $(".chat-messages")[0].scrollHeight}, 1000);
                        }
                    }

                }
            });
        }


        $(".list-group-item").click(function () {
            $(".chat-messages").empty();
        });
        $(".chat-submit").click(function (e) {
            e.preventDefault();
            var msg = $("#chat-input").val();
            if (msg.trim() == '') {
                return false;
            }
            gennerateMessage(msg, 'self', new Date().getTime(), false);
            loadUsers(false, false);
            var message = {receiverId: userIdShow, msg: msg}

            socket.send(JSON.stringify(message));

            $("#chat-input").val('');
            $(".chat-messages").stop().animate({scrollTop: $(".chat-messages")[0].scrollHeight}, 1000);
        })

        function gennerateMessage(msg, type, time, pre) {

            var sendTime = new Date(time);
            var str = `<div class="chat-message chat-message-` + type + ` pb-4">
        <div>
            <img src="images/avatar-icon.jpg"
                 class="rounded-circle mr-1" alt="Chris Wood" width="40" height="40">
        </div>
        <div class="message flex-shrink-1 bg-light rounded">
           ` + msg + `
        </div>
        <div class="time">
            ` + formatTime(sendTime, "hh:mm") + `
        </div>
    </div>`
            if (pre) {
                $(".chat-messages").prepend(str);
            } else {
                $(".chat-messages").append(str);
            }
        }

        // search
        $(document).on("mousedown", function (event) {
            // Kiểm tra xem phần tử được click có là con của #yourDiv hay không
            if ((!$('#searchResults').is(event.target) && !$('#searchResults').has(event.target).length) || ($('#main-search').is(event.target) && !$('#main-search').has(event.target).length)) {
                $('#main-search').val("");
                $('#searchResults').empty();
            }
        });
        $('#main-search').on("input", function () {
            $('#searchResults').empty();
            var query = $(this).val().toLowerCase();
            if (query !== '') {
                $.ajax({
                    type: "GET",
                    url: "/admin/chat/users/search?query=" + query,
                    contentType: "application/x-www-form-urlencoded; charset=UTF-16",
                    // Serialize dữ liệu biểu mẫu
                    success: function (response) {

                        var data = JSON.parse(response);
                        data.forEach(element => {
                            $('#searchResults').append("<a href='/admin/chat?user=" + element.id + "' class='product-link'>" + element.name + "</a>");
                            $('#searchResults li:gt(7)').remove();
                        });
                    }
                });
            }
        });
    });
</script>
</html>
