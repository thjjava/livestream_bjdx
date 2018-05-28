<%@page import="com.sttri.util.WorkUtil"%>
<%@page import="java.util.Map"%>
<%
String path=request.getContextPath();
boolean flag = false;
Map u = WorkUtil.getCurrUser(request);
if(u!=null)
	flag = true;
%>
<head>
	<script type="text/javascript">
		var flag = '<%=flag%>';
		if(flag!='true')
			window.parent.login();
	</script>
</head>
<input type="hidden" id="path" name="path" value="<%=path %>">