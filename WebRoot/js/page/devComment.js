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
			{field:'user',title:'用户',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.user!=null)
						return rec.user.account;
					else
						return '无';
				}
			},
			{field:'dev',title:'直播设备',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.dev!=null)
						return rec.dev.devName;
					else
						return '无';
				}
			},
			{field:'clientIP',title:'用户IP',width:120,sortable:true,align:'center'},
			{field:'content',title:'内容',width:120,sortable:true,align:'center'},
			{field:'isLegal',title:'审核',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.isLegal==0){
						return '<font color="green">通过</font>';
					}else if(rec.isLegal==1){
						return '<font color="red">未通过</font>';
					}
				}
			},
			{field:'commentTime',title:'评论时间',width:120,sortable:true,align:'center'}
			
			]];
			
/**
 * 菜单栏
 */
var tbar=[{ 
	id:'btnadd',
	text:'审核',
	iconCls:'icon-add',
	handler:check
	},'-',{ 
		id:'btnremove',
		text:'拉黑',
		iconCls:'icon-remove',
		handler:addBlack
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
	queryInit(path+'/devComment_query.do',tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
	$('#'+render).datagrid('reload', {'queryIsLegal':$('#queryIsLegal').val(),'addTimeStart':$('#addTimeStart').val(),"addTimeEnd":$('#addTimeEnd').val(),"queryDevNo":$('#queryDevNo').val()});
}

/**
 * 审核评论
 */
function check(){
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
		$.post(path+"/devComment_check.do",{"ids":ids},function(data){
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

function createGroupTree(){
	$.ajax({
		type:'POST',
		url:path+'/companyGroup_getTree.do',
		success:function(data){
			if(data !=''){
				var arry = eval('('+data+')');
				$.fn.zTree.init($("#grouptree"), groupsetting, arry);
				openDiv('groupWindow');
			}
		}
  	});
}

var groupsetting = {
	check: {
		enable: true,
		chkStyle: "radio",
		radioType: "all"
	},
	view: {
		dblClickExpand: false
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onClick: onGroupClick,
		onCheck: onGroupCheck
	}
};

function onGroupClick(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("grouptree");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onGroupCheck(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("grouptree"),
	nodes = zTree.getCheckedNodes(true),
	names = '',ids='';
	for (var i=0, l=nodes.length; i<l; i++) {
		names += nodes[i].name + ',';
		ids += nodes[i].id + ',';
	}
	if (names.length > 0 ) names = names.substring(0, names.length-1);
	if (ids.length > 0 ) ids = ids.substring(0, ids.length-1);
	$("#groupName").val(names);
	$('#groupId').val(ids);
}

/**
 * 拉黑用户
 */
function addBlack(){
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
		$.post(path+"/devComment_addBlack.do",{"ids":ids},function(data){
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

