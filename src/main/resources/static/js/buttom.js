var layer = null,$ = null;
	layui.use('layer', function(){
	  layer = layui.layer;
	  $ = layui.$;
	  $("#testListAction").click(function(){
		layer.confirm("确定数据无误？保存后将无法修改！", {icon: 3, title:'提示'}, function(index){
			 $.post('/savaAnnualMeetingGameQuestion',function(data){
				if(data == 'ok'){
					layer.msg("插入成功")
				}else{
					layer.msg("插入失败")
				}
			}); 
			
			table.reload('annualMeeting_question_table_temporaryData',{
				data:null
			});
			table.reload('annualMeeting_question_table');
			$('#td_waitFor').html('上传成功！');
			layer.close(index);
		})
	  });
	  $("#dumpData").click(function(){
	  	layer.confirm("确定要清除数据？清除后将无法恢复！", {icon: 3, title:'提示'}, function(index){
			 $.post('/clearAllData',function(data){
				if(data == 'ok'){
					layer.msg("清除成功")
				}else{
					layer.msg("清除失败")
				}
			}); 
			table.reload('annualMeeting_question_table');
			layer.close(index);
		})
	  });
	  
	});   