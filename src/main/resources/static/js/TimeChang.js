layui.use(['util','laydate','layer','table'], function(){
	  var laydate = layui.laydate,util = layui.util,layer = layui.layer,table = layui.table;
	  var times = null;
	 
	  //执行一个laydate实例
	  laydate.render({
	    elem: '#gameStarTime' //指定元素
	    ,type: 'datetime'
	    ,done: function(value, date){
	      times = date;
	    }
	  });
	  
	  
	  
	  $('#dumpData').click(function(data){
		var time = $('#gameStarTime').val();
		if(time.trim()==""){
			layer.alert("请选择时间！")
		}else{
			$.get('/admin/time?time='+time,function(data){
		  		if(data == "ok"){
			  		setCountdown(times.year, times.month  - 1, times.date, times.hours, times.minutes, times.seconds);
			  		$.post("/admin/getTimeParam", function(data) {
				  		var demoList = $('#demoList');
				  		demoList.empty();
				  		var tr = $(['<tr>'
					          ,'<td id="beginTimeStr">'+ data.beginTimeStr +'</td>'
					          ,'<td id="intervalTime">'+ data.intervalTime +'秒</td>'
					        ,'</tr>'].join(''));
						        
						    demoList.append(tr);
					  })
			  		}
		  		})
		  	}
	  });
	  
	  $('#btnGameMiao').click(function(data){
		var gametime = $('#gameMiao').val();
		if(!isNaN(gametime)){
			$.get('/admin/setTimeInterval?gametime='+gametime,function(data) {
		  		if(data == "ok"){
			  		$.post("/admin/getTimeParam", function(data) {
				  		var demoList = $('#demoList');
				  		demoList.empty();
				  		var tr = $(['<tr>'
					          ,'<td id="beginTimeStr>'+ data.beginTimeStr +'</td>'
					          ,'<td id="intervalTime">'+ data.intervalTime +'秒</td>'
					          ,'</tr>'].join(''));
					        
					    demoList.append(tr);
				  })
		  		}
			})
		}else{
			layer.alert("请输入一个数字");
		  	}
	  	
	  })
	  	
	  var thisTimer, setCountdown = function(y, M, d, H, m, s){
		    var endTime = new Date(y, M||0, d||1, H||0, m||0, s||0) //结束日期
		,serverTime = new Date(); //假设为当前服务器时间，这里采用的是本地时间，实际使用一般是取服务端的
		
		clearTimeout(thisTimer);
		util.countdown(endTime, serverTime, function(date, serverTime, timer){
		  var str = date[0] + '天' + date[1] + '时' +  date[2] + '分' + date[3] + '秒';
		  lay('#CountDown').html(str);
		      thisTimer = timer;
		    });
	  };

	$.post("/admin/getTimeParam", function(data) {
	  		var demoList = $('#demoList');
	  		demoList.empty();
	  		var tr = $(['<tr>'
		          ,'<td id="beginTimeStr">'+ data.beginTimeStr +'</td>'
		          ,'<td id="intervalTime">'+ data.intervalTime +'秒</td>'
		        ,'</tr>'].join('')); 
		    demoList.append(tr);
			clearTimeout(thisTimer);
			util.countdown(data.beginTime, new Date().getTime(), function(date, serverTime, timer){
			    var str = date[0] + '天' + date[1] + '时' +  date[2] + '分' + date[3] + '秒';
			    lay('#CountDown').html(str);
			    thisTimer = timer;
			});
	})
	$('#clearData').click(function(data) {
	  layer.confirm('是否清除开始时间！', function(index){
		  $('#gameStarTime').val("");
		  $.get('/admin/clearData',function(data){
			  if(data=='ok'){
				  layer.alert('清除成功！')
				  $('#beginTimeStr').html('未设置');
				  clearTimeout(thisTimer);
				  lay('#CountDown').html('0天0时0分0秒');
			  }
		  })
		  layer.close(index);
		});  
	})
 })
