<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/comm/ContextPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>问题反馈</title>
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
		<script type="text/javascript" src="../js/page/problem.js"></script>
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
								用户名：
								<input type="text" id="queryDevNo" style="width: 180px;">
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
		<div id="addWindow" class="easyui-window" title="设备信息" closed="true"
			collapsible="false" minimizable="false" maximizable="false"
			iconCls="icon-add"
			style="width: 650px; height: 300px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<form action="<%=path%>/problem_save.do" method="post"
						id="saveForm">
						<table width="100%" border="0" style="font-size: 13">
							<tr style="display: none">
								<td align="right">
									ID：
								</td>
								<td>
									<input type="text" name="problem.id" id="id">
									<input type="text" name="problem.addTime" id="addTime">
								</td>
							</tr>
							<tr>
								<td align="right">
									手机型号：
								</td>
								<td>
									<input type="text" name="problem.mobileModel" id="mobileModel"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
							<tr>
								<td align="right">
									系统版本：
								</td>
								<td>
									<input type="text" name="problem.systemOS" id="systemOS"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
							<tr>
								<td align="right">
									用户名：
								</td>
								<td>
									<input type="text" name="problem.devNo" id="devNo"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
							<tr>
								<td align="right">
									网络类型：
								</td>
								<td>
									<select name="problem.netType" id="netType">
										<option value="">--全部--</option>
										<option value="0">wifi</option>
										<option value="1">联通3G</option>
										<option value="2">联通4G</option>
										<option value="3">电信3G</option>
										<option value="4">电信4G</option>
										<option value="5">移动3G</option>
										<option value="6">移动4G</option>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right">
									问题描述：
								</td>
								<td>
									<input type="text" name="problem.content" id="content"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
							<tr>
								<td align="right">
									问题解答：
								</td>
								<td>
									<input type="text" name="problem.answer" id="answer"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
						</table>
					</form>
				</div>
				<div region="south" border="false"
					style="text-align: center; height: 30px; line-height: 30px;">
					<a id="subBtn" class="easyui-linkbutton" href="javascript:void(0)"
						onclick="submitForm()">提交</a>
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="closeDiv('addWindow');">取消</a>
				</div>
			</div>
		</div>
		
	</body>
</html>