var render="datalist";
var win="addWindow";
var wform="saveForm";
var qform="queryForm";
var path = $('#path').val();

/**
 * 数据列表
 * @type 
 */
var tcolumn=[[
			{field:'sensitiveWord',title:'敏感词',width:120,sortable:true,align:'center'},
			{field:'addTime',title:'创建时间',width:120,sortable:true,align:'center'}
			]];
			
/**
 * 菜单栏
 */
var tbar=[{ 
			id:'btnadd',
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				$('#comtr').hide();
				resetForm(wform);
				openDiv(win);
			}
		 },'-',{ 
			id:'btnedit',
			text:'修改',
			iconCls:'icon-edit',
			handler:edit
		 },'-',{ 
			id:'btnremove',
			text:'删除',
			iconCls:'icon-remove',
			handler:deleteobj
		 },'-',{ 
				id:'btnadd',
				text:'批量导入',
				iconCls:'icon-add',
				handler:function(){
//					resetForm(wform);
					openDiv('importWindow');
				}
		 }];
		 
/**
 * 加载初始化
 */
$(function(){
	init();
}); 

/**
 * 刷新列表
 */
function init(){
	queryInit(path+'/sensitive_query.do?timestamp='+ new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
	var querySensitive=$('#querySensitive').val();
	$('#'+render).datagrid('reload', {"querySensitive":querySensitive});
}


/**
 * 增加和修改操作
 */
function submitForm(){
	if($('#'+wform).form('validate')){
		var url="";
		var id=$('#id').val();
		if(id==''){
			url=path+"/sensitive_save.do";
		}else{
			url=path+"/sensitive_update.do";
		}
		$('#'+wform).form('submit', {
		    url:url,
		    onSubmit: function(){
		    },   
			success:function(data){
		     	if("success"==data){
		     		$.messager.alert('提示',"更新数据成功!");
			     	resetForm(wform);
					closeDiv(win);
					init();
		     	}else if('hased'==data){
		     		$.messager.alert('提示',"敏感词已经存在!");
		     	}else{
		     		$.messager.alert('提示',"更新数据失败!");
		     	}
		    }
		});
	}
}

/**
 * 获取详细信息
 * @param {} url
 */
function queryObjectbyID(url){
	$.ajax({
		type:'POST',
		url:url,
		success:function(msg){
			if(msg !=''){
				var arry = eval("("+msg+")");
				$('input[name="sensitiveWord.id"]').val(arry.id);
				$('input[name="sensitiveWord.sensitiveWord"]').val(arry.sensitiveWord);
				$('input[name="sensitiveWord.addTime"]').val(arry.addTime);
				openDiv(win);
			}else{
				$.messager.alert('提示','信息不存在！');
			}
		}
  	});
}

/**
 * 修改
 */
function edit(){
	resetForm(wform);
	var rows = $('#'+render).datagrid('getSelections');
	if(rows.length==0){
		alert("请选择一条记录！");
		return;
	}
	var queryUrl=path+'/sensitive_getbyid.do?id='+rows[0].id;
	$('#'+render).datagrid('clearSelections');
	queryObjectbyID(queryUrl);
}

/**
 * 删除
 */
function deleteobj(){
	$.messager.confirm('系统提示', '您确定要删除吗?', function(r) {
        if (r) {
            var rows = $('#'+render).datagrid('getSelections');
            var ids="";
			if(rows.length>0){
				for(var i=0;i<rows.length;i+=1){
					if(i==0){
						ids=rows[i].id;
					}else{
						ids+="_"+rows[i].id;
					}
				}
				$.post(path+"/sensitive_deletebyids.do",{"ids":ids},function(data){
					if("success"==data){
						$('#'+render).datagrid('clearSelections');
			     		$.messager.alert('提示',"更新数据成功!");
			     		init();
			     	}
				});
			}else{
				$.messager.alert('提示',"请选择要删除数据!");
			}
        }
    });
}



/**
 * 将excel数据导入到数据库中
 */
function importExcel(){
	var file=$("#upload").val();
	if(file==''){
		alert("请选择上传的文件!");
		return;
	}
	var url=path+'/sensitive_upload.do';
	$('#uploadForm').form('submit', {
	    url:url,
	    onSubmit: function(){
	    },   
		success:function(data){
	    	if(data!=''){
	    		var d = eval('('+data+')');
	    		if(d!=null){
	    			if(d.key=='success'){
	    				$.messager.alert('提示',"导入成功!");
	    				closeDiv('importWindow');
	    				init();
	    			}else if(d.key=='pictype'){
	    				$.messager.alert('提示',"文件格式不支持,请导入xls格式文件!");
	    			}else{
	    				$.messager.alert('提示',"导入失败!");
	    			}
	    		}
	    	}
	    }
	})
}

