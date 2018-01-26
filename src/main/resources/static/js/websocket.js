var websocket;
var host = window.location.hostname;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://"+host+"/answer");
}
else {
    alert('Not support websocket')
}

//连接发生错误的回调方法
websocket.onerror = function () {
    setMessageInnerHTML("error");
};

//连接成功建立的回调方法
websocket.onopen = function (event) {
   // setMessageInnerHTML("open");
}

var inited = false;
//接收到消息的回调方法
websocket.onmessage = function (event) {


    console.log(event.data);
    var json = JSON.parse(event.data);
    console.log(json,'ddd')

    if (json.method == 'init') {
        if (json.errorCode == 0) {
            inited = true;
            $('#div1').css('display', 'none');
            $('#div4').css('display', 'block');
            $('#comit').css('display', 'none');
            var time = json.message;
            if (time > new Date().getTime()) {
                $('#wait').css('display', 'block');
                run(time);
            }
        } else if(json.errorCode == 104){

            $('#div1').css('display', 'none');
            $('#div2').css('display', 'block');
            $('#comit').css('display', 'none');
            $.get("http://192.168.78.46/getQuestion", function (msg) {
                var que = msg.rows[0];
                refreshForm(que);
            });

        }else {
            // alert(json.message);
            $("#tooltip").html(json.message).css('display','block').delay(3000).hide(0);
        }
    } else if (json.method == 'question') {
        if (json.errorCode == 0) {
            isCommit = false;
            $("input[type='radio']").removeAttr('checked');
            $('#div1').css('display', 'none');
            $('#div4').css('display', 'none');
            var que = json.rows[0];
            refreshForm(que);
            if (inited) {
                $('#comit').css('display', 'block');
            } else {
                $('#comit').css('display', 'none');

            }
            $('#wait').css('display', 'none');
            $('#div2').css('display', 'block');

        } else {
           // alert(json.message);
            $("#tooltip").html(json.message).css('display','block').delay(3000).hide(0);
            rank();
        }

    } else if (json.method == 'answer') {
        var que = json.rows[0];
        $('#answer1').text($('#answer1').text() + ' ' + que['answerOne']+'个人');
        $('#answer2').text($('#answer2').text() + ' ' + que['answerTwo']+'个人');
        $('#answer3').text($('#answer3').text() + ' ' + que['answerThree']+'个人');
        $('#answer4').text($('#answer4').text() + ' ' + que['answerFour']+'个人');
        $('#answer' + que.rightAnswer).css('color', '#009688');

      //  alert(json.message);
        $("#tooltip").html(json.message).css('display','block').delay(3000).hide(0);
        if(json.errorCode!=0){
            inited=false;
        }
            // rank();
    } else if (json.method == 'updateScore') {
        $('#comit').css('display', 'none');
        if (json.errorCode == 0) {

        } else {
          //  alert(json.message);
            $("#tooltip").html(json.message).css('display','block').delay(3000).hide(0);
            inited = false;
        }

    } else {
       // alert(event.data);
        $("#tooltip").html(event.data).css('display','block').delay(3000).hide(0);
    }

}

//连接关闭的回调方法
websocket.onclose = function () {
   // setMessageInnerHTML("close");
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    websocket.close();
}

//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
    // document.getElementById('message').innerHTML += innerHTML + '<br/>';
    alert(innerHTML);
}

//关闭连接
function closeWebSocket() {
    websocket.close();
}

//发送消息
function send(message) {
    websocket.send(message);
}