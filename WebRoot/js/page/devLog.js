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
			{field:'dev',title:'设备名称',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.dev!=null)
						return rec.dev.devName;
					else
						return '无';
				}
			},
			{field:'mediaServer',title:'服务器',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.mediaServer!=null)
						return rec.mediaServer.serverName;
					else
						return '无';
				}
			},
			{field:'clientIP',title:'客户端IP',width:120,sortable:true,align:'center'},
			{field:'operatorName',title:'网络运营商',width:120,sortable:true,align:'center'},
			{field:'logType',title:'日志类型',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.logType==0){
						return '<font color="green">开始直播</font>';
					}else if(rec.logType==1){
						return '<font color="red">停止直播</font>';
					}else if(rec.logType==2){
						return '<font>登录</font>';
					}else if(rec.logType==3){
						return '<font>直播异常</font>';
					}else if(rec.logType==4){
						return '<font>APP异常</font>';
					}else if(rec.logType==5){
						return '<font>登录远程客户端</font>';
					}
				}
			},
			{field:'logDesc',title:'日志描述',width:120,sortable:true,align:'center'},
			{field:'addTime',title:'创建时间',width:120,sortable:true,align:'center'}
			
			]];
			
/**
 * 菜单栏
 */
var tbar=[];
		 
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
	queryInit(path+'/devLog_query.do?timestamp='+ new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
	$('#'+render).datagrid('reload', {'queryLogType':$('#queryLogType').val(),'queryIsp':$('#queryIsp').val(),'addTimeStart':$('#addTimeStart').val(),"addTimeEnd":$('#addTimeEnd').val()});
}
