<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/comm/ContextPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>设备管理</title>
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
		<script type="text/javascript" src="../js/page/dev.js"></script>
		<script type="text/javascript" src="../js/qrcode.js"></script>
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
								设备号：
								<input type="text" id="queryDevNo" style="width: 180px;">
							</td>
							<td bgcolor="#ffffff" align="center">
								设备名称：
								<input type="text" id="queryDevName" style="width: 180px;">
							</td>
							<td bgcolor="#ffffff" align="center">
								企业:
								<select id="queryCompany">
									<option value="">--请选择--</option>
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
		<div id="addWindow" class="easyui-window" title="设备信息" closed="true"
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
									<input type="text" name="dev.id" id="id">
									<input type="text" name="dev.imsi" id="imsi">
									<input type="text" name="dev.phone" id="phone">
									<input type="text" name="dev.audioRtpPort" id="audioRtpPort">
									<input type="text" name="dev.audioRtcpPort" id="audioRtcpPort">
									<input type="text" name="dev.videoRtpPort" id="videoRtpPort">
									<input type="text" name="dev.videoRtcpPort" id="videoRtcpPort">
									<input type="text" name="dev.serverId" id="serverId">
									<input type="text" name="dev.drId" id="drId">
									<input type="text" name="dev.onLines" id="onLines">
									<input type="text" name="dev.isAble" id="isAble">
									<input type="text" name="dev.lastLoginTime" id="lastLoginTime">
									<input type="text" name="dev.addTime" id="addTime">
								</td>
							</tr>
							<tr>
								<td align="right" width="30%;">
									企业：
								</td>
								<td>
									<select name="dev.company.id" id="company" onchange="getDevNums()">
										<option value="">--请选择--</option>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right">
									设备名称：
								</td>
								<td>
									<input type="text" name="dev.devName" id="devName"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
							<tr>
								<td align="right">
									设备号：
								</td>
								<td>
									<input type="text" name="dev.devNo" id="devNo"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
							<tr id="trp1" style="display:none;">
								<td align="right">
									密码：
								</td>
								<td>
									<input type="password" name="dev.devKey" id="devKey"
										validType="length[1,50]" invalidMessage="请输入密码...">
								</td>
							</tr>
							<tr id="trp2" style="display:none;">
								<td align="right">
									确认密码：
								</td>
								<td>
									<input type="password" name="pwd1" id="pwd1"
										validType="same['devKey']" invalidMessage="请输入密码...">
								</td>
							</tr>
							<tr>
								<td align="right">
									是否转码：
								</td>
								<td>
									<input type="radio" name="dev.isTransCode" value="0" style="width:15px;" checked="checked">否
									<input type="radio" name="dev.isTransCode" value="1" style="width:15px;" >是
								</td>
							</tr>
							<tr style="display: none;">
								<td align="right">
									视频发布地址：
								</td>
								<td>
									<input type="text" name="dev.publishUrl"
										id="publishUrl" class="easyui-validatebox" 
										validType="length[1,300]"
										invalidMessage="请输入地址...">
								</td>
							</tr>
							<tr>
								<td align="right">
									&nbsp;
								</td>
								<td>
									<span id="devNumsInfo"></span>
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
		<div id="qrcodeWindow" class="easyui-window" title="二维码" closed="true"
			collapsible="false" minimizable="false" maximizable="false"
			iconCls="icon-add"
			style="width: 220px; height: 220px; display: none;" resizable="false">
			<div style="margin-left:30px;margin-top:10px;" id="preview"></div>
			<span style="margin-left:30px;">使用手机扫描观看</span>
		</div>
		<!-- 新增和修改 -->
		<div id="addMoreWindow" class="easyui-window" title="设备信息" closed="true"
			collapsible="false" minimizable="false" maximizable="false"
			iconCls="icon-add"
			style="width: 650px; height: 300px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<form action="<%=path%>/company_saveMore.do" method="post"
						id="saveMoreForm">
						<table width="100%" border="0" style="font-size: 13">
							<tr style="display: none">
								<td align="right">
									ID：
								</td>
								<td>
									<input type="text" name="dev.id" id="id">
									<input type="text" name="dev.imsi" id="imsi">
									<input type="text" name="dev.phone" id="phone">
									<input type="text" name="dev.audioRtpPort" id="audioRtpPort">
									<input type="text" name="dev.audioRtcpPort" id="audioRtcpPort">
									<input type="text" name="dev.videoRtpPort" id="videoRtpPort">
									<input type="text" name="dev.videoRtcpPort" id="videoRtcpPort">
									<input type="text" name="dev.serverId" id="serverId">
									<input type="text" name="dev.drId" id="drId">
									<input type="text" name="dev.onLines" id="onLines">
									<input type="text" name="dev.isAble" id="isAble">
									<input type="text" name="dev.lastLoginTime" id="lastLoginTime">
									<input type="text" name="dev.addTime" id="addTime">
								</td>
							</tr>
							<tr>
								<td align="right" width="30%;">
									企业：
								</td>
								<td>
									<select id="companyMore" onchange="getDevNumsMore()">
										<option value="">--请选择--</option>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right">
									设备编号：
								</td>
								<td>
									<input type="text" id="devFirstNo"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
							<tr>
								<td align="right">
									设备总数：
								</td>
								<td>
									<input type="text" id="devNumsTotal"
										class="easyui-validatebox" required="true" disabled="disabled">
								</td>
							</tr>
							<tr>
								<td align="right">
									已有设备数：
								</td>
								<td>
									<input type="text" id="devNumsUsed"
										class="easyui-validatebox" required="true" disabled="disabled">
								</td>
							</tr>
							<tr>
								<td align="right">
									设备新增数：
								</td>
								<td>
									<input type="text" id="devNums"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
							<tr>
								<td align="right">
									&nbsp;
								</td>
								<td>
									<span id="devNumsInfoMore"></span>
								</td>
							</tr>
						</table>
					</form>
				</div>
				<div region="south" border="false"
					style="text-align: center; height: 30px; line-height: 30px;">
					<a id="subBtn" class="easyui-linkbutton" href="javascript:void(0)"
						onclick="submitFormMore()">提交</a>
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="closeDiv('addMoreWindow');">取消</a>
				</div>
			</div>
		</div>
		
		<!-- 批量导入开始 -->
		<div id="importWindow" class="easyui-window" title="设备信息" closed="true"
			collapsible="false" minimizable="false" maximizable="false"
			iconCls="icon-add"
			style="width: 500px; height: 300px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<table width="100%" border="0" style="font-size: 13">
						<tr>
							<td align="right" width="30%;">
								企业：
							</td>
							<td>
								<select id="companyImport">
									<option value="">--请选择--</option>
								</select>
							</td>
						</tr>
						<tr>
							<td align="right">文件：</td>
			    			<td>
			    				<form method="post" id="uploadForm" action="<%=path%>/dev_upload.do" enctype="multipart/form-data">
			    					<input type="file" id="upload" name="upload" value="浏览">
			    				</form>
			    			</td>
						</tr>
					</table>
				</div>
				<div region="south" border="false"
					style="text-align: center; height: 30px; line-height: 30px;">
					<a id="downBtn" class="easyui-linkbutton" href="javascript:void(0)"
						onclick="javascript:location.href='<%=path%>/excel/dev.xls'">下载模版</a>
					<a id="subBtn" class="easyui-linkbutton" href="javascript:void(0)"
						onclick="importExcel();">导入</a>
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="closeDiv('importWindow');">取消</a>
				</div>
			</div>
		</div>
		<!-- 批量导入结束 -->
	</body>
</html>