layui.use('upload', function(){
	var $ = layui.jquery,upload = layui.upload;
	var demoListView = $('#demoList');
	//拖拽上传
	upload.render({
	   elem: '#exctFileLoad'
	   ,url: '/loadExctFile'
	   ,method:'post'
	   ,accept:'file'
	   ,exts:'xls|xlsx|xlsm|xlsb'
	   ,field:'exctFile'
	   ,before: function(obj){ 
	   	  demoListView.empty(); 
	      var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
	      
	      if(files.length>0){
	      	delete files[0];
	      }
	      
	      //读取本地文件
	      obj.preview(function(index, file, result){
	        var tr = $(['<tr id="upload-'+ index +'">'
	          ,'<td>'+ file.name +'</td>'
	          ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
	          ,'<td>等待上传</td>'
	          ,'<td>'
	            ,'<button class="layui-btn layui-btn-mini layui-btn-danger demo-delete">删除</button>'
	          ,'</td>'
	        ,'</tr>'].join(''));
	        
	        //删除
	        tr.find('.demo-delete').on('click', function(){
	          delete files[index]; //删除对应的文件
	          tr.remove();
	          uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
	          //删除以及上传的数据
	          
	        });
	        
	        demoListView.append(tr);
	      });
	    }
	   ,done: function(res,index,upload){
	     console.log(res)
	   }
	});
})