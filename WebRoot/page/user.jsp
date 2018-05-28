<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/comm/ContextPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户管理</title>
		<link rel="stylesheet" type="text/css"
			href="../js/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css"
			href="../js/themes/icon.css" />
		<link rel="stylesheet" type="text/css"
			href="../css/commonCss.css">
		<script type="text/javascript" src="../js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="../js/jquery.easyui.min.1.2.2.js"></script>
		<script type="text/javascript" src="../js/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="../js/common/comm.js"></script>
		<script type="text/javascript" src="../js/common/util.js"></script>
		<script type="text/javascript" src="../js/page/user.js"></script>
	</head>
	<body>
		<div class="query">
			<div id="querydiv" class="query-title">
				<div class="query-title-background">
					<div class="query-title-icon"></div>
					<div class="query-title-font">
						查询条件设置
					</div>
					<div id="yshowdiv" class="query-title-show">
						<a
							onclick="$('#nshowdiv').show();$('#yshowdiv').hide();$('#showdiv').hide();$('#querydiv').css({'border-bottom':'1px solid #8DB2E3'});"
							href="javascript:void(0)">隐藏</a>
					</div>
					<div id="nshowdiv" class="query-title-show" style="display: none;">
						<a
							onclick="$('#yshowdiv').show();$('#nshowdiv').hide();$('#showdiv').show();$('#querydiv').css({'border-bottom':'0px solid #8DB2E3'});"
							href="javascript:void(0)">显示</a>
					</div>
				</div>
			</div>
			<div id="showdiv" style="height: 20px;">
				<form method="post" id="queryForm" onkeydown="if(event.keyCode==13){return false;}"
					action="<%=path%>/queryList_userAction.do">
					<table style="width: 100%; height: 100%" cellspacing=1
						cellpadding=0 border=0 bgcolor="#99bbe8">
						<tr>
							<td bgcolor="#ffffff" align="center">
								用户帐号：
								<input type="text" id="queryAccount" style="width: 180px;">
							</td>
							<td bgcolor="#ffffff" align="center">
								用户类型：
								<select id="queryAccountType" style="width: 180px;">
									<option value="">--全部--</option>
									<option value="0">企业管理员</option>
									<option value="1">企业用户</option>
								</select>
							</td>
							<td bgcolor="#ffffff" align="center">
								<a class="easyui-linkbutton" href="javascript:void(0)"
									iconCls="icon-search"
									onclick="query();">查询</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<table id="datalist"></table>
		<!-- 新增和修改 -->
		<div id="addWindow" class="easyui-window" title="用户信息" closed="true"
			collapsible="false" minimizable="false" maximizable="false"
			iconCls="icon-add"
			style="width: 650px; height: 300px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<form action="<%=path%>/company_save.do" method="post"
						id="saveForm">
						<table width="100%" border="0" style="font-size: 13">
							<tr style="display: none">
								<td align="right">
									ID：
								</td>
								<td>
									<input type="text" name="user.id" id="id">
									<input type="text" name="user.modifyPwdTime" id="modifyPwdTime">
									<input type="text" name="user.loginTimes" id="loginTimes">
									<input type="text" name="user.addTime" id="addTime">
								</td>
							</tr>
							<tr>
								<td align="right" width="30%;">
									企业：
								</td>
								<td>
									<select name="user.company.id" id="company">
										<option value="">--请选择--</option>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right" width="30%;">
									用户类型：
								</td>
								<td>
									<select name="user.accountType" id="type">
										<option value="0">企业管理员</option>
										<option value="1">企业用户</option>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right">
									用户帐号：
								</td>
								<td>
									<input type="text" name="user.account" id="account"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
							<tr id="trp1" style="display:none;">
								<td align="right">
									密码：
								</td>
								<td>
									<input type="password" name="user.pwd" id="pwd">
								</td>
							</tr>
							<tr id="trp2" style="display:none;">
								<td align="right">
									确认密码：
								</td>
								<td>
									<input type="password" name="pwd1" id="pwd1"
										validType="same['pwd1']" invalidMessage="请输入密码...">
								</td>
							</tr>
							<!-- 
							<tr>
								<td align="right">
									联系人电话：
								</td>
								<td>
									<input type="text" name="account.mobilePhone"
										id="mobilePhone" class="easyui-numberbox" 
										precision="0" minlength="11" maxlength="11"
										invalidMessage="手机号长度不够...">
								</td>
							</tr>
							 -->
						</table>
					</form>
				</div>
				<div region="south" border="false"
					style="text-align: center; height: 30px; line-height: 30px;">
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="submitForm()">提交</a>
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="closeDiv('addWindow');">取消</a>
				</div>
			</div>
		</div>
		
		<!-- 角色 -->
		<div id="roleWindow" class="easyui-window" title="用户信息" closed="true"
			collapsible="false" minimizable="false" maximizable="false"
			iconCls="icon-add"
			style="width: 650px; height: 300px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<form action="<%=path%>/user_save.do" method="post"
						id="roleForm">
						<table width="100%" border="0" style="font-size: 13">
							<tr>
								<td align="right" width="30%;">
									角色：
								</td>
								<td>
									<select name="role" id="role">
										<option value="0">请选择</option>
									</select>
								</td>
							</tr>
						</table>
					</form>
				</div>
				<div region="south" border="false"
					style="text-align: center; height: 30px; line-height: 30px;">
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="saveRole()">提交</a>
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="closeDiv('roleWindow');">取消</a>
				</div>
			</div>
		</div>
	</body>
</html>