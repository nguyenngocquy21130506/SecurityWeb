$(function () {

    

    $(".list-group-item").click(function () {
        $(".chat-messages").empty();
    });
    $(".chat-submit").click(function (e) {
        e.preventDefault();
        var msg = $("#chat-input").val();
        if (msg.trim() == '') {
            return false;
        }
        gennerateMessage(msg, 'self');
        $("#chat-input").val('');
    })

    function gennerateMessage(msg, type) {
        var str = `<div class="chat-message-right pb-4">
        <div>
            <img src="images/avatar-icon.jpg"
                 class="rounded-circle mr-1" alt="Chris Wood" width="40" height="40">
        </div>
        <div class="message flex-shrink-1 bg-light rounded">
           ` + msg + `
        </div>
        <div class="time">
            9:30
        </div>
    </div>`
        $(".chat-messages").append(str);
        $(".chat-messages").stop().animate({ scrollTop: $(".chat-messages")[0].scrollHeight }, 1000);
    }
})