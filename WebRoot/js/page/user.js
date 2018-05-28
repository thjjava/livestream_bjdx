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
			{field:'accountType',title:'用户类型',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.accountType==0)
						return '企业管理员';
					else
						return '企业用户';
				}
			},
			{field:'com',title:'企业',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.company!=null)
						return rec.company.comName;
					else
						return '无';
				}
			},
			{field:'account',title:'帐号',width:120,sortable:true,align:'center'},
			{field:'addTime',title:'创建时间',width:120,sortable:true,align:'center'},
			{field:'oper',title:'操作',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.accountType==0){
						return '<a href="javascript:void(0);" style="text-decoration: none;" onclick="setRole(\''+rec.id+'\');">设置角色</a>';
					}else{
						return '无';
					}
				}
			}
			]];
			
/**
 * 菜单栏
 */
var tbar=[{ 
			id:'btnadd',
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
//				$('#trp1').show();
//				$('#trp2').show();
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
		 }];
		 
/**
 * 加载初始化
 */
$(function(){
	init();
	var data = getObject(path+'/company_getList.do');
	if(data==null || data==''){
		return;
	}
	var d = eval('('+data+')');
	if(d==null || d.length==0)
		return;
	
	var html = '<option value="">--请选择--</option>';
	for(var i=0;i<d.length;i++){
		html += '<option value="'+d[i].id+'">'+d[i].comName+'</option>';
	}
	$('#company').html(html);
}); 

/**
 * 刷新列表
 */
function init(){
	queryInit(path+'/user_query.do?timestamp='+ new Date().getTime(),tcolumn,tbar,render);
	$.post(path+'/role_getList.do?timestamp='+ new Date().getTime(),function(result){
		var data = eval('('+result+')');
		if(data !=null){
			var html='';
			$("#role").html('<option value="0">请选择</option>');
			for(var i=0;i<data.length;i++){
				html+='<option value="'+data[i].id+'">'+data[i].roleName+'</option>';
			}
			$("#role").append(html);
		}
	});
}

/**
 * 查询
 */
function query(){
    var queryAccount=$('#queryAccount').val();
    var accountType = $('#queryAccountType').val();
	$('#'+render).datagrid('reload', {"queryAccount":queryAccount,"accountType":accountType});
}


/**
 * 增加和修改操作
 */
function submitForm(){
	if($('#'+wform).form('validate')){
		var url="";
		var id=$('#id').val();
		if(id==''){
			url=path+"/user_save.do";
		}else{
			url=path+"/user_update.do";
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
		     	}else if('account'==data){
		     		$.messager.alert('提示',"当前用户已经存在!");
		     	}else if('manager'==data){
		     		$.messager.alert('提示',"当前企业只能有一个管理员!");
		     	}else if('pwdFalse'==data){
		     		$.messager.alert('提示',"请输入位数是8到20位的正确密码,格式必须含有大小写字母，数字和特殊字符!");
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
				$('input[name="user.id"]').val(arry.id);
				$('select[name="user.accountType"]').val(arry.accountType);
				$('input[name="user.account"]').val(arry.account);
				/*
				if(arry.accountType==0){
					$('select[name="user.company.id"]').val("");
					$('company').hide();
				}else{
					$('select[name="user.company.id"]').val(arry.company.id);
					$('company').show();
				}
				*/
				if(arry.company!=null)
					$('select[name="user.company.id"]').val(arry.company.id);
				$('input[name="user.pwd"]').val(arry.pwd);
				$('input[name="pwd1"]').val(arry.pwd);
//				$('#trp1').hide();
//				$('#trp2').hide();
				$('input[name="user.addTime"]').val(arry.addTime);
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
	var queryUrl=path+'/user_getbyid.do?id='+rows[0].id;
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
				$.post(path+"/user_deletebyids.do",{"ids":ids},function(data){
					if("success"==data){
						$('#'+render).datagrid('clearSelections');
			     		$.messager.alert('提示',"更新数据成功!");
			     		init();
			     	}
				}) 
			}
        }
    });
}

function getOnChangeType(obj){
	if($(obj).val()==0){
		$('#comtr').hide();
	}else{
		$('#comtr').show();
	}
}

/**
 * 设置角色
 */
var userId ='';
function setRole(obj){
	resetForm('roleForm');
	userId = obj;
	$.post(path+"/role_getUserRole.do",{'userId':userId},function(data){
		var array = eval("("+data+")");
		$('select[name="role"]').val(array.id);
	});
	openDiv('roleWindow');
}

function saveRole(){
	var roleId = $("#role").val();
	if(roleId == '0'){
		$.messager.alert('提示',"请选择角色!");
		return;
	}
	$.post(path+'/user_saveRole.do',{'userId':userId,'roleId':roleId},function(data){
		if("success"==data){
     		$.messager.alert('提示',"更新数据成功!");
			closeDiv('roleWindow');
			init();
     	}else{
     		$.messager.alert('提示',"更新数据失败!");
     	}
	});
}

function showPwd(){
	var accountType = $("#type").val();
	if(accountType == 0){
		$("#trp1").css("display","");
		$("#trp2").css("display","");
	}else{
		$("#trp1").css('display','none');
		$("#trp2").css('display','none');
	}
}
