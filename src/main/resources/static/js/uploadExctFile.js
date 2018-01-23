layui.use('upload', function(){
	var $ = layui.jquery,upload = layui.upload;
	var demoListView = $('#demoList');
	//拖拽上传
	upload.render({
	   elem: '#exctFileLoad'
	   ,url: '/admin/loadExctFile'
	   ,method:'post'
	   ,accept:'file'
	   ,exts:'xls|xlsx|xlsm|xlsb'
	   ,field:'exctFile'
	   ,before: function(obj){ 
	   	  demoListView.empty(); 
	      //var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
	      
	    /*  if(files.length>0){
	      	delete files[0];
	      }*/
	      
	      //读取本地文件
	      obj.preview(function(index, file, result){
	        var tr = $(['<tr id="upload-'+ index +'">'
	          ,'<td>'+ file.name +'</td>'
	          ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
	          ,'<td id="td_waitFor">等待上传</td>'
	        ,'</tr>'].join(''));
	        
	        
	        demoListView.append(tr);
	      });
	    }
	   ,done: function(res,index,upload){
		   table.reload('annualMeeting_question_table_temporaryData',{
			   data:res.allQuestions
		   })
	   }
	});
})