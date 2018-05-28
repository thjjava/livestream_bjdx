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
            {field:'devNo',title:'账号',width:120,sortable:true,align:'center'},
            {field:'mobileModel',title:'手机型号',width:120,sortable:true,align:'center'},
            {field:'systemOS',title:'系统版本',width:120,sortable:true,align:'center'},
			{field:'netType',title:'网络类型',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					switch (rec.netType) {
					case 0:
						val="wifi";
						break;
					case 1:
						val="联通3G";
						break;
					case 2:
						val="联通4G";
						break;
					case 3:
						val="电信3G";
						break;
					case 4:
						val="电信4G";
						break;
					case 5:
						val="移动3G";
						break;
					case 6:
						val="移动4G";
						break;
					default:
						val="wifi";
						break;
					}
					return val;
					/*if(rec.netType==0)
						return 'wifi';
					else
						return '企业用户';*/
				}
			},
			{field:'content',title:'问题描述',width:120,sortable:true,align:'center'},
            {field:'answer',title:'问题解答',width:120,sortable:true,align:'center'},
			{field:'addTime',title:'添加时间',width:120,sortable:true,align:'center'}
			]];
			
/**
 * 菜单栏
 */
var tbar=[/*{ 
			id:'btnadd',
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				$('#trp1').show();
				$('#trp2').show();
				resetForm(wform);
				openDiv(win);
			}
		 },'-',*/{ 
			id:'btnedit',
			text:'解答',
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
}); 

/**
 * 刷新列表
 */
function init(){
	queryInit(path+'/problem_query.do?timestamp='+ new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
	$('#'+render).datagrid('reload', {"queryDevNo":$('#queryDevNo').val()});
}


/**
 * 增加和修改操作
 */
function submitForm(){
	if($('#'+wform).form('validate')){
		var url="";
		var id=$('#id').val();
		if(id==''){
			url=path+"/problem_save.do";
		}else{
			url=path+"/problem_update.do";
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
				$('input[name="problem.id"]').val(arry.id);
				$('select[name="problem.netType"]').val(arry.netType);
				$('input[name="problem.devNo"]').val(arry.devNo);
				$('input[name="problem.mobileModel"]').val(arry.mobileModel);
				$('input[name="problem.systemOS"]').val(arry.systemOS);
				$('input[name="problem.content"]').val(arry.content);
				$('input[name="problem.answer"]').val(arry.answer);
				$('input[name="problem.addTime"]').val(arry.addTime);
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
	var queryUrl=path+'/problem_getbyid.do?id='+rows[0].id;
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
				$.post(path+"/problem_deletebyids.do",{"ids":ids},function(data){
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