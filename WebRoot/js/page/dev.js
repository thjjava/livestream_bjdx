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
			{field:'com',title:'企业',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.company!=null)
						return rec.company.comName;
					else
						return '无';
				}
			},
			{field:'devName',title:'设备名称',width:120,sortable:true,align:'center'},
			{field:'devNo',title:'设备号',width:120,sortable:true,align:'center'},
			//{field:'imsi',title:'imsi',width:120,sortable:true,align:'center'},
			//{field:'phone',title:'手机号',width:120,sortable:true,align:'center'},
			{field:'publishUrl',title:'发布地址',width:120,sortable:true,align:'center'},
			{field:'onLines',title:'是否在线',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.onLines==0)
						return '在线';
					else
						return '离线';
				}
			},
			{field:'isAble',title:'启用状态',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.isAble==0)
						return '<font color="green">启用</font>';
					else
						return '<font color="red">禁用</font>';
				}
			},
			{field:'addTime',title:'创建时间',width:120,sortable:true,align:'center'}
			,
			{field:'oper',title:'操作',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.onLines==0){
						if(rec.hlsUrl !='' && rec.hlsUrl !=null){
							return '<a href="javascript:void(0);" style="text-decoration: none;" onclick="makeQrcode(\''+rec.hlsUrl+'\');">使用手机观看</a>|'+
									'<a href="javascript:void(0);" style="text-decoration: none;" onclick="wxPost(\''+rec.id+'\',\''+rec.hlsUrl+'\');">观看</a>';
						}else{
							return '无操作';
						}
					}else{
						return '无操作';
					}
				}
			}
			/*,
			{field:'oper',title:'操作',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.onLines==1)
						if(rec.isAble==0)
							return '<a href="javascript:void(0);" style="text-decoration: none;" onclick="isAble(\''+rec.id+'\','+rec.onLines+',1);">禁用</a>';
						else
							return '<a href="javascript:void(0);" style="text-decoration: none;" onclick="isAble(\''+rec.id+'\','+rec.onLines+',0);">启用</a>';
					else
						return '无操作';
				}
			}
			*/
			]];
			
/**
 * 菜单栏
 */
var tbar=[{ 
			id:'btnadd',
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				$("#devNumsInfo").html("");
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
			text:'批量增加',
			iconCls:'icon-add',
			handler:function(){
				$("#devNumsInfo").html("");
				resetForm(wform);
				openDiv('addMoreWindow');
			}
		 },'-',{ 
			id:'btnedit',
			text:'更改启用状态',
			iconCls:'icon-edit',
			handler:setUnUsed
		 },'-',{ 
			id:'btnadd',
			text:'批量导入',
			iconCls:'icon-add',
			handler:function(){
//				resetForm(wform);
				openDiv('importWindow');
			}
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
	$('#queryCompany').html(html);
	$('#companyMore').html(html);
	$('#companyImport').html(html);
}); 

/**
 * 刷新列表
 */
function init(){
	queryInit(path+'/dev_query.do?timestamp='+ new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
    var queryDevNo=$('#queryDevNo').val();
	$('#'+render).datagrid('reload', {"queryDevNo":queryDevNo,'queryCompany':$('#queryCompany').val(),'queryDevName':$('#queryDevName').val()});
}


/**
 * 增加和修改操作
 */
function submitForm(){
	if($('#'+wform).form('validate')){
		var url="";
		var devKey = $("#devKey").val();
		var pwd = $("#pwd1").val();
		if(devKey != pwd){
			$.messager.alert('提示',"确认密码跟密码不相同,请重新输入!");
			return;
		}
		var id=$('#id').val();
		if(id==''){
			url=path+"/dev_save.do";
		}else{
			url=path+"/dev_update.do";
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
		     	}else if("devnums"==data){
		     		$.messager.alert('提示',"不能添加过多设备!");
		     	}else if("devNo"==data){
		     		$.messager.alert('提示',"当前设备编号已经存在!");
		     	}else if("devName"==data){
		     		$.messager.alert('提示',"当前设备名称已经存在!");
		     	}else{
		     		$.messager.alert('提示',"更新数据失败!");
		     	}
		    }
		})
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
				$('input[name="dev.id"]').val(arry.id);
				$('input[name="dev.devName"]').val(arry.devName);
				$('input[name="dev.devNo"]').val(arry.devNo);
				if(arry.company!=null)
					$('select[name="dev.company.id"]').val(arry.company.id);
				else
					$('select[name="dev.company.id"]').val("");
				
				$('input[name="dev.devKey"]').val(arry.devKey);
				$('input[name="pwd1"]').val(arry.devKey);
				/*$('#trp1').hide();
				$('#trp2').hide();*/
				$('input[name="dev.imsi"]').val(arry.imsi);
				$('input[name="dev.phone"]').val(arry.phone);
				$('input[name="dev.publishUrl"]').val(arry.publishUrl);
				$('input[name="dev.audioRtpPort"]').val(arry.audioRtpPort);
				$('input[name="dev.audioRtcpPort"]').val(arry.audioRtcpPort);
				$('input[name="dev.videoRtpPort"]').val(arry.videoRtpPort);
				$('input[name="dev.videoRtcpPort"]').val(arry.videoRtcpPort);
				$('input[name="dev.onLines"]').val(arry.onLines);
				$('input[name="dev.serverId"]').val(arry.serverId);
				$('input[name="dev.isAble"]').val(arry.isAble);
				$('input[name="dev.drId"]').val(arry.drId);
				$('input[name="dev.lastLoginTime"]').val(arry.lastLoginTime);
				$('input[name="dev.addTime"]').val(arry.addTime);
				$("input[type='radio'][name='dev.isTransCode'][value='"+arry.isTransCode+"']").attr("checked", "checked");
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
	$("#devNumsInfo").html("");
	var rows = $('#'+render).datagrid('getSelections');
	if(rows.length==0){
		alert("请选择一条记录！");
		return;
	}
	var queryUrl=path+'/dev_getbyid.do?id='+rows[0].id;
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
				$.post(path+"/dev_deletebyids.do",{"ids":ids},function(data){ 
					if("success"==data){
						$('#'+render).datagrid('clearSelections');
			     		$.messager.alert('提示',"更新数据成功!");
			     		init();
			     	}
				});
			}
        }
    });
}

function isAble(id,online,isAble){
	$.post(path+"/dev_isAble.do",{"id":id,"online":online,"isAble":isAble},function(data){
		if(data==null || data==''){
			$.messager.alert('提示',"操作错误!");
			return;
		}
		var d = eval("("+data+")");
		if(d==null){
			$.messager.alert('提示',"操作错误!");
			return;
		}
		if(d.key=='success'){
			$.messager.alert('提示',"更新数据成功!");
			init();
		}
	});
}

//获取该企业下设备数信息
function getDevNums(){
	var comId = $("#company").val();
	if(comId !=""){
		$.post(path+"/company_getDevNums.do",{"id":comId},function(data){
			var obj = eval("("+data+")");
			var total = obj.total;
			var used = obj.used;
			var html = "该企业设备总数：<font color='green'>"+total+"</font>, 已拥有设备数：<font color='red'>"+used+"</font>";
			if(used >= total){
				//$("#subBtn").removeAttr("onclick");
				html += ",【<font color='red'>不能再添加设备!</font>】";
			}
			$("#devNumsInfo").html(html);
		});
	}
}

function makeQrcode(hlsUrl){
	$("#preview").html("");
	closeDiv("qrcodeWindow")
    var qrcode = new QRCode("preview",hlsUrl,"150","150");
    qrcode.makeCode(hlsUrl);
    openDiv("qrcodeWindow")
}

function wxPost(id,hlsUrl){
	window.open(path+"/mobile.html?id="+id+"&hlsUrl="+encodeURIComponent(hlsUrl));
}

//获取该企业下设备数信息
function getDevNumsMore(){
	var comId = $("#companyMore").val();
	if(comId !=""){
		$.post(path+"/company_getDevNums.do",{"id":comId},function(data){
			var obj = eval("("+data+")");
			var total = obj.total;
			var used = obj.used;
			$("#devNumsTotal").val(total);
			$("#devNumsUsed").val(used);
		});
	}
}

function submitFormMore(){
	if($('#saveMoreForm').form('validate')){
		var url="";
		var devNumsTotal = $("#devNumsTotal").val();
		var devNumsUsed = $("#devNumsUsed").val();
		var devNums = $("#devNums").val();
		if(devNums > (devNumsTotal-devNumsUsed)){
			$.messager.alert('提示',"批量添加数不能超过设备总数");
			return;
		}
		var comId = $("#companyMore").val();
		var devFirstNo = $("#devFirstNo").val();
		var url = path+"/dev_saveMore.do?devFirstNo="+devFirstNo+"&comId="+comId+"&devNums="+devNums+"&devNumsUsed="+devNumsUsed;
		$('#saveMoreForm').form('submit', {
		    url:url,
		    onSubmit: function(){
		    },   
			success:function(data){
		     	if("success"==data){
		     		$.messager.alert('提示',"更新数据成功!");
			     	resetForm('saveMoreForm');
					closeDiv('addMoreWindow');
					init();
		     	}else if("devnums"==data){
		     		$.messager.alert('提示',"不能添加过多设备!");
		     	}else if("dev"==data){
		     		$.messager.alert('提示',"当前设备已经存在!");
		     	}else{
		     		$.messager.alert('提示',"更新数据失败!");
		     	}
		    }
		})
	}
}

/**
 * 设置设备的启用状态为启用或禁用
 */
function setUnUsed(){
	$.messager.confirm('系统提示', '您确定要操作吗?', function(r) {
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
				$.post(path+"/dev_setUnUsed.do",{"ids":ids},function(data){
					if("success"==data){
						$('#'+render).datagrid('clearSelections');
			     		$.messager.alert('提示',"更新数据成功!");
			     		init();
			     	}else{
			     		$.messager.alert('提示',"更新数据失败!");
			     	}
				});
			}
        }
    });
}

/**
 * 将excel数据导入到数据库中
 */
function importExcel(){
	var comId=$("#companyImport").val();
	var file=$("#upload").val();
	if(comId==''){
		alert("请选择企业!");
		return;
	}
	if(file==''){
		alert("请选择上传的文件!");
		return;
	}
	var url=path+'/dev_upload.do?comId='+comId;
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
	    			}else if(d.key=='devNum'){
	    				$.messager.alert('提示',"批量导入设备数大于企业剩余设备数!");
	    			}else{
	    				$.messager.alert('提示',"导入失败!");
	    			}
	    		}
	    	}
	    }
	})
}