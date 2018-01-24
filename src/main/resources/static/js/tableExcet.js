var table = null;
layui.use('table', function(){
	table = layui.table;
	var $ = layui.$;
	table.render({
	 	elem: '#annualMeeting_question_table'
	 	,url: '/admin/userData'
	 	,cols: [[
	 	   {field:'id', title: 'id', width:'5%',sort: true}
	 	   ,{field:'topic', title: '题目', width:'70%'}
	 	   ,{field:'answerOne', title: '答案一', width:'5%'}
	 	   ,{field:'answerTwo', title: '答案二', width:'5%'}
	 	   ,{field:'answerThree', title: '答案三', width:'5%'}
	 	   ,{field:'answerFour', title: '答案四', width:'5%'}
	 	   ,{field:'rightAnswer', title: '正确答案', width:'5%'}
	 	]]
		,initSort:{
			field: 'id'
			,type: 'asc'
		}
	 	,id:'annualMeeting_question_table'
	 	,loading:true
	 	,response:{
	 		  statusName: 'state' //数据状态的字段名称
			  ,statusCode: 1 //成功的状态码
			  ,msgName: 'stateInfo' //状态信息的字段名称
			  ,countName: 'count' //数据总数的字段名称
			  ,dataName: 'allQuestions' //数据列表的字段名称
	 	}
	 })
	 
	 table.render({
			 	elem: '#annualMeeting_question_table_temporaryData'
			 	,cols: [[
		 		 	   {field:'id', title: 'id', width:'5%',sort: true}
		 		 	   ,{field:'topic', title: '题目', width:'70%' ,edit:'text'}
		 		 	   ,{field:'answerOne', title: '答案一', width:'5%' ,edit:'text'}
		 		 	   ,{field:'answerTwo', title: '答案二', width:'5%' ,edit:'text'}
		 		 	   ,{field:'answerThree', title: '答案三', width:'5%' ,edit:'text'}
		 		 	   ,{field:'answerFour', title: '答案四', width:'5%' ,edit:'text'}
		 		 	   ,{field:'rightAnswer', title: '正确答案', width:'5%' ,edit:'text'}
		 		 ]
			 	]
			 	,id:'annualMeeting_question_table_temporaryData'
			 	,loading:true
			 	,response:{
			 		  statusName: 'state' //数据状态的字段名称
					  ,statusCode: 1 //成功的状态码
					  ,msgName: 'stateInfo' //状态信息的字段名称
					  ,countName: 'count' //数据总数的字段名称
					  ,dataName: 'allQuestions' //数据列表的字段名称
			 	}
			 })
	 table.on('edit(annualMeeting_question_table_temporaryData)', function(obj){
	    var value = obj.value //得到修改后的值
	    ,data = obj.data //得到所在行所有键值
	    ,field = obj.field; //得到字段
	    $.post('/admin/updataQuestionData',{id:data.id,fieldName:field,fieldValue:value})
	  });
})