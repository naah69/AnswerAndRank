var websocket;
var websocketChart = null;
var host = window.location.hostname;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://" + host + "/answer");
}
else {
    alert('该设备不支持答题！')
}

//连接发生错误的回调方法
websocket.onerror = function () {
    showMessage("连接服务器出现错误！");
};

//连接成功建立的回调方法
websocket.onopen = function (event) {
    showMessage("连接服务器成功！");
}

var inited = null;
//接收到消息的回调方法
websocket.onmessage = function (event) {
    console.log(event.data);
    var json = JSON.parse(event.data);
    if (json.method == 'init') {


        if (json.errorCode == 0) {
            inited = true;
            var time = json.message;
            if (time > new Date().getTime()) {
                $('#wait').css('display', 'block');
                beginTimeCountDown(time);
            }
            $('#initDiv').css('display', 'none');
            $('#waitDiv').css('display', 'block');
            $('#commitBtn').css('display', 'none');
            initChat();

        } else if (json.errorCode == 104) {
             inited = false;
            $.get("http://"+host+"/getQuestion", function (msg) {
                var que = msg.rows[0];
                refreshForm(que);
            });
            $('#initDiv').css('display', 'none');
            $('#questionDiv').css('display', 'block');
            $('#commitBtn').css('display', 'none');
            initChat();
        } else {
             inited = false;
            showMessage(json.message);
        }
    } else if (json.method == 'question') {
        if (inited != null) {
            if (json.errorCode == 0) {
                isCommit = false;
                $("input[type='radio']").removeAttr('checked');
                var que = json.rows[0];
                refreshForm(que);
                $('#questionDiv').css('display', 'block');
                if (inited != null && inited) {
                    $('#commitBtn').css('display', 'block');
                } else {
                    $('#commitBtn').css('display', 'none');

                }
                $('#initDiv').css('display', 'none');
                $('#waitDiv').css('display', 'none');
                $('#wait').css('display', 'none');
            } else {
                // alert(json.message);
                showMessage(json.message);
                $('#rankBtn').click();
                $('#rankDiv').css('display', 'block');
            }
        }

    } else if (json.method == 'answer') {
        clearTime();
        var que = json.rows[0];
        $('#answer1').text($('#answer1').text() + '    ' + que['answerOne'] + '个人');
        $('#answer2').text($('#answer2').text() + '    ' + que['answerTwo'] + '个人');
        $('#answer3').text($('#answer3').text() + '    ' + que['answerThree'] + '个人');
        $('#answer4').text($('#answer4').text() + '    ' + que['answerFour'] + '个人');
        $('#answer' + que.rightAnswer).css('color', '#009688');
        showMessage(json.message);
        if (json.errorCode != 0) {
            inited = false;
        }
    } else if (json.method == 'updateScore') {
        $('#commitBtn').css('display', 'none');
        if (json.errorCode == 0) {
        } else {
            showMessage(json.message);
            inited = false;
        }
    } else {
        showMessage(event.data);
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


//发送消息
function send(message) {
    websocket.send(message);
}

function showMessage(message) {
    $("#tooltip").html(message).css('display', 'block').delay(3000).hide(0);
}


function beginTimeCountDown(enddate) {
    setInterval(function () {
        var date1 = new Date();
        var date3 = enddate - date1.getTime();
        var days = Math.floor(date3 / (24 * 3600 * 1000));
        var leave1 = date3 % (24 * 3600 * 1000);
        var hours = Math.floor(leave1 / (3600 * 1000));
        var leave2 = leave1 % (3600 * 1000);
        var minutes = Math.floor(leave2 / (60 * 1000));
        var leave3 = leave2 % (60 * 1000);
        var seconds = Math.round(leave3 / 1000);
        $('#timeEnd').html(days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒");
    }, 1000);
}

var table = null;

$('#initBtn').on('click', function () {
    if ($('#username').val() == "" || $('#department').val() == "" || $('#tel').val() == "") {
        alert("请填写本人姓名部门和电话！");
        return false; //如果验证不通过，则不执行后面
    } else {
        var message = {
            "method": "init",
            "user": {"username": $('#username').val(), "department": $('#department').val(), "tel": $('#tel').val()}
        };
        $('#rankBtn').css('display', 'block');
        send(JSON.stringify(message));
    }
});
var isCommit = false;
$('#commitBtn').on('click', function () {

    var time = intervalSecond - timesRun;
    var message = {
        "method": "updateScore",
        "id": $('#id').val(),
        "answer": $('input:radio:checked').val(),
        "times": time
    };
    isCommit = true;
    //clearTime();
    $('#commitBtn').css('display', 'none');
    $('#userTime').text("本次用时：" + time);
    send(JSON.stringify(message));
    // $("input[type='radio']").removeAttr('checked');
});


$('#rankBtn').on('click', function () {
    $('#rankDiv').toggle();
    $.get("http://"+host+"/rank", function (msg) {
        console.log(msg.rows)
//            table.reload('annualMeeting_question_table', {
//                data: data.rows
//            })
        var str = "";
        var data = msg.rows;
        for (i in data) {
            str += "<tr style=\"font-size: 18px;color: #333;\">" +
                "<td>" + data[i].username + "</td>" +
                "<td>" + data[i].department + "</td>" +
                "<td>" + data[i].timesSecond + "</td>" +
                "<td>" + data[i].score + "</td>" +
                "</tr>";
        }
        tbody.innerHTML = str;
    });
});

var tbody = window.document.getElementById("tbody-result");


var timesRun = 0;
var interval;
var intervalSecond = 12;
$.get("http://"+host+"/intervalSecond",
    function (data) {
        intervalSecond = data;
    });

function time() {
    timesRun = intervalSecond;
    interval = setInterval(function () {
        $('#timeViewer').text(timesRun);
        if (timesRun === 0) {
            clearTime();
            interval = null;
            if (!isCommit && inited != null && inited) {
                $('#commitBtn').click();
            }
        } else {

            timesRun--;
        }


    }, 1000);
}

function clearTime() {
    if (interval != null && interval != undefined) {
        clearInterval(interval);
    }
}


function refreshForm(que) {
    clearTime();
    time();
    $('#userTime').text("");
    $('#id').val(que['id']);
    console.log($('#id').val())
    $('#question').html('第' + que['id'] + '题：<br>' + que['question']);
    $('#answer1').text(que['answerOne']);
    $('#answer1').css({"font-size": "25px", "color": "#333"});
    $('#answer2').text(que['answerTwo']);
    $('#answer2').css({"font-size": "25px", "color": "#333"});
    $('#answer3').text(que['answerThree']);
    $('#answer3').css({"font-size": "25px", "color": "#333"});
    $('#answer4').text(que['answerFour']);
    $('#answer4').css({"font-size": "25px", "color": "#333333"});

}

var isChat = false;

function chat() {
    if (!isChat) {
        if ($('#commentValue').val() != '' && $('#commentValue').val() != null && $('#commentValue').val() != undefined) {
            websocketChart.send($('#commentValue').val());
            $('#commentValue').val('');
            isChat = true;
            setTimeout(function () {
                isChat = false;
            }, 2000);
        } else {
            showMessage("说点什么吧！")
        }

    } else {
        showMessage("歇一会吧！2秒后再说！")
    }
}

function initChat() {
    //判断当前浏览器是否支持WebSocket
    if (websocketChart == null) {
        if ('websocketChart' in window) {
            websocketChart = new WebSocket("ws://"+host+":8080/chat");
            $('#chatDiv').css('display', 'block');
        }
        else {
            alert('Not support websocketChart')
        }

        //接收到消息的回调方法
        websocketChart.onmessage = function (event) {
            console.log(event.data);
            var i = Math.round(Math.random() * 7);
            var item = {
                info: event.data, //文字
                href: '', //链接
                close: true, //显示关闭按钮
                speed: 5, //延迟,单位秒,默认6
                top: i, //距离底部高度,单位px,默认随机
                color: '#ffffff', //颜色,默认白色
                old_ie_color: '#ffffff' //ie低版兼容色,不能与网页背景相同,默认黑色
            }
            $('body').barrager(item);
        }

        websocketChart.onerror = function () {
            websocketChart = new WebSocket("ws://"+host+":8080/chat");
        };

    }
}


(function () {


	'use strict';

	// Placeholder
	var placeholderFunction = function() {
		$('input, textarea').placeholder({ customClass: 'my-placeholder' });
	}

	// Placeholder
	var contentWayPoint = function() {
		var i = 0;
		$('.animate-box').waypoint( function( direction ) {

			if( direction === 'down' && !$(this.element).hasClass('animated-fast') ) {

				i++;

				$(this.element).addClass('item-animate');
				setTimeout(function(){

					$('body .animate-box.item-animate').each(function(k){
						var el = $(this);
						setTimeout( function () {
							var effect = el.data('animate-effect');
							if ( effect === 'fadeIn') {
								el.addClass('fadeIn animated-fast');
							} else if ( effect === 'fadeInLeft') {
								el.addClass('fadeInLeft animated-fast');
							} else if ( effect === 'fadeInRight') {
								el.addClass('fadeInRight animated-fast');
							} else {
								el.addClass('fadeInUp animated-fast');
							}

							el.removeClass('item-animate');
						},  k * 200, 'easeInOutExpo' );
					});

				}, 100);

			}

		} , { offset: '85%' } );
	};
	// On load
	$(function(){
		placeholderFunction();
		contentWayPoint();

	});

}());