<%@page import="java.util.Calendar"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.sttri.pojo.TblUser"%>
<%@page import="com.sttri.util.WorkUtil"%>
<%@page import="java.util.Map"%>
<% 
	String path=request.getContextPath();
	boolean flag = false;
	Map u = WorkUtil.getCurrUser(request);
	if(u!=null)
		flag = true;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>即时传送管理系统</title>
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/default.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=path %>/js/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/js/themes/icon.css" />
		<script type="text/javascript" src="<%=path %>/js/jquery-1.6.min.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jQuery.easyui.js"></script>
		<script type="text/javascript" src="<%=path %>/js/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src='<%=path %>/js/outlook2.js'></script>
		<script type="text/javascript">
			var flag = '<%=flag%>';
			if(flag!='true')
				window.location.href = window.location.protocol+'//'+window.location.host+'<%=path%>';
			var _menus = {"menus":[
							{"menuid":"1","icon":"icon-sys","menuname":"系统管理",
							  		"menus":[
								  		{"menuname":"角色管理","icon":"icon-users","url":"<%=path%>/page/role.jsp"},
								  		{"menuname":"菜单管理","icon":"icon-users","url":"<%=path%>/page/menus.jsp"},
							  			{"menuname":"企业管理","icon":"icon-users","url":"<%=path%>/page/company.jsp"},
							  			{"menuname":"用户管理","icon":"icon-users","url":"<%=path%>/page/user.jsp"},
							  			{"menuname":"设备管理","icon":"icon-dev","url":"<%=path%>/page/dev.jsp"},
							  			{"menuname":"设备日志","icon":"icon-users","url":"<%=path%>/page/devLog.jsp"},
							  			{"menuname":"服务器","icon":"icon-alg","url":"<%=path%>/page/mediaServer.jsp"},
							  			{"menuname":"版本控制","icon":"icon-alg","url":"<%=path%>/page/control.jsp"},
							  			<%-- {"menuname":"问题反馈","icon":"icon-alg","url":"<%=path%>/page/problem.jsp"}, --%>
							  			{"menuname":"评论管理","icon":"icon-alg","url":"<%=path%>/page/devComment.jsp"},
							  			{"menuname":"黑名单管理","icon":"icon-alg","url":"<%=path%>/page/black.jsp"},
							  			{"menuname":"创建License","icon":"icon-alg","url":"<%=path%>/license.jsp"}
									  ]
							 }
					     ]};
		</script>
	</head>
	<body class="easyui-layout">
		<div region="north" border="false" split="true"
			style="height: 80px; background: #E0ECFF; background: url(<%=path %>/images/banner_03.jpg); width: 100%;">
			<div style="float: left;height: 100%;width: 50%;">
				<span style="color: white;font-size: 25px;padding-top: 20px;float: left;padding-left: 25px;font-style: 楷体;">
					即时传送管理系统
				</span>
			</div>
			<div style="float: right;display: ;">
				<span style="valign: middle; float: right; padding-right: 10px; margin-top: 10px; color: white;"
					class="head">
					<a id="loginOut" href="<%=path %>/login_logout.do" style='color: white;cursor:pointer;text-decoration: none;'>安全退出</a>
				</span>
			</div>
		</div>
		<div region="west" split="true" title="菜单" style="width: 180px;">
			<div class="easyui-accordion" fit="true" border="false">
				导航内容
			</div>
		</div>
		<!-- 
		<div region="east" split="true" title="East" style="width:100px;padding:10px;"></div>
		 -->
		<div region="south" split="true"
			style="height: 35px; background: url(<%=path %>/images/south.jpg); text-align: center;">
			<font style="font-size: 20px;color: white;">©2015-<%=Calendar.getInstance().get(Calendar.YEAR) %> 视频服务产品（上海）运营中心 版权所有</font>
		</div>
		<div region="center" title="" style="overflow-y: hidden;">
			<div id="tabs" class="easyui-tabs" fit="true" border="false">
				<!-- <div title="欢迎使用" style="overflow: hidden;" id="home">
					<img src="<%=path %>/images/ehome.jpg" style="width: 100%;height: 100%;"/>
				</div> -->
			</div>
		</div>
		<!--加载内容页面结束-->
		<div id="mm" class="easyui-menu" style="width: 150px;">
			<div id="mm-tabclose">
				关闭
			</div>
			<div id="mm-tabcloseall">
				全部关闭
			</div>
			<div id="mm-tabcloseother">
				除此之外全部关闭
			</div>
			<div class="menu-sep"></div>
			<div id="mm-tabcloseright">
				当前页右侧全部关闭
			</div>
			<div id="mm-tabcloseleft">
				当前页左侧全部关闭
			</div>
			<div class="menu-sep"></div>
			<div id="mm-exit">
				退出
			</div>
		</div>
	</body>
</html>