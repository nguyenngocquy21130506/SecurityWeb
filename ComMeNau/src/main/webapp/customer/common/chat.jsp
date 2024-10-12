<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="body">

    <div id="chat-circle" class="btn btn-raised">
        <div id="chat-overlay"></div>
        <i style="font-size: 40px;" class="material-icons">message</i>
        <span id="noti"></span>
    </div>

    <div class="chat-box">
        <div class="chat-box-header">
            ChatBot
            <span class="chat-box-toggle"><i class="material-icons">close</i></span>
        </div>
        <div class="chat-box-body">
            <div class="chat-box-overlay">
            </div>
            <div class="chat-logs">

            </div><!--chat-log -->
        </div>
        <div class="chat-input">
            <form class="d-flex align-items-center justify-content-center">
                <input type="text" id="chat-input" placeholder="Send a message..."/>
                <button type="submit" class="chat-submit" id="chat-submit"><i
                        class="material-icons">send</i></button>
            </form>
        </div>
    </div>

</div>
<script>
    $(document).ready(function () {


        var userId = ${sessionScope.auth.id};


        const socket = new WebSocket("ws://localhost:8080/chat/" + userId);

        socket.onopen = function (event) {

        };

        socket.onmessage = function (event) {
            var chatmessage = JSON.parse(event.data);
            var viewed = false;
            if ($('.chat-box').css('display') === 'block') {
                viewed = true;
            }
            else{
                if (!$('#noti').hasClass("notification-badge")) {
                    $('#noti').addClass("notification-badge");
                    $('#noti').text("!")
                }
            }
            if (chatmessage.senderId == userId) {
                generate_message(chatmessage.senderId, chatmessage.content, 'self', chatmessage.time, viewed, false);
            } else {
                generate_message(chatmessage.senderId, chatmessage.content, 'user', chatmessage.time, viewed, false);
            }
            updateViewed();

        };
        socket.onclose = function (event) {
        };
        var section = 1;
        let messages = [];

        function updateViewed() {
            if ($('.chat-logs').css('display') === 'block') {
                $.ajax({
                    type: "PUT",
                    url: "http://localhost:8080/message/" + userId +"?ownerId="+userId,
                    contentType: "application/x-www-form-urlencoded; charset=UTF-16",
                    // Serialize dữ liệu biểu mẫu
                    success: function (response) {

                    }
                });
            }
        }

        function loadMessages() {
            $.ajax({
                type: "GET",
                url: "http://localhost:8080/message/" + userId + "?section=" + section,
                contentType: "application/x-www-form-urlencoded; charset=UTF-16",
                // Serialize dữ liệu biểu mẫu
                success: function (response) {
                    section++;
                    response.forEach(n => {
                        if (n.senderId == userId) {
                            generate_message(n.senderId, n.content, 'self', n.time, n.viewed, true);
                        } else {
                            generate_message(n.senderId, n.content, 'user', n.time, n.viewed, true);
                        }
                    })

                    if (!response[0].viewed && response[0].senderId != userId) {
                        if (!$('#noti').hasClass("notification-badge")) {
                            $('#noti').addClass("notification-badge");
                            $('#noti').text("!")
                        }
                    }
                    $(".chat-logs").scrollTop($(".chat-logs")[0].scrollHeight);
                }
            });
        }

        loadMessages();

        function createDiv(msg, type, sendtime) {
            var chatmessage = $("<div>").attr({
                class: "chat-msg " + type,
            });
            var avatar = $("<span>").attr({
                class: "msg-avatar",
            });
            var img = $("<img>").attr({
                src: "/customer/images/logo/user.png",
            });
            avatar.append(img);
            var message = $("<div>").attr({
                class: "cm-msg-text d-flex flex-column",
            });
            var text = $("<div>").attr({
                class: "chat-text",
            })
            text.append(msg)
            message.append(text);
            var time = $("<div>").attr({
                class: "chat-time",
            })

            var date = new Date(sendtime);
            var y = formatTime(date,"hh:mm");
            time.append(y)
            message.append(time);
            chatmessage.append(avatar);
            chatmessage.append(message);

            return chatmessage;
        }

        $(".chat-logs").scroll(function () {
            if ($(this).scrollTop() === 0) {
                $.ajax({
                    type: "GET",
                    url: "http://localhost:8080/message/" + userId + "?section=" + section,
                    contentType: "application/x-www-form-urlencoded; charset=UTF-16",
                    // Serialize dữ liệu biểu mẫu
                    success: function (response) {

                        section++;
                        response.forEach(n => {
                            if (n.senderId == userId) {
                                generate_message(n.senderId, n.content, 'self', n.time, n.viewed, true);
                            } else {
                                generate_message(n.senderId, n.content, 'user', n.time, n.viewed, true);
                            }
                        })
                        var positionToScroll = $(".chat-logs").find(".chat-msg").eq(response.length).position();
                        $(".chat-logs").scrollTop(positionToScroll.top);

                    }

                });
            } else if ($(this).scrollTop() > 0) {

            }
        });


        var INDEX = 0;

        $("#chat-submit").click(function (e) {
            e.preventDefault();
            var msg = $("#chat-input").val();
            if (msg.trim() == '') {
                return false;
            }
            $("#chat-input").val('');

            var message = {msg: msg}

            socket.send(JSON.stringify(message));

            generate_message(userId, msg, 'self', new Date().getTime(), false);

        })
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

        function generate_message(sender, msg, type, time, viewed, pre) {
            if (pre) {
                $(".chat-logs").prepend(createDiv(msg, type, time));
                messages.unshift({senderId: sender, time: time, content: msg, viewed: viewed});
            } else {
                $(".chat-logs").append(createDiv(msg, type, time));
                messages.push({senderId: sender, time: time, content: msg, viewed: viewed});
                $(".chat-logs").stop().animate({scrollTop: $(".chat-logs")[0].scrollHeight}, 1000);
            }
        }


        $("#chat-circle").click(function () {
            $("#chat-circle").toggle('scale');
            $(".chat-box").toggle('scale');
            $(".chat-logs").scrollTop($(".chat-logs")[0].scrollHeight);
            updateViewed();
            if ($('#noti').hasClass("notification-badge")) {
                $('#noti').removeClass("notification-badge");
                $('#noti').text("")
            }

        })

        $(".chat-box-toggle").click(function () {
            $("#chat-circle").toggle('scale');
            $(".chat-box").toggle('scale');
        })

    });

</script>


