package com.sttri.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class WorkUtil {
	public static String pwdEncrypt(String pwd){
		return MD5Util.MD5Code(pwd);
	}
	
	public static Map getCurrUser(HttpServletRequest request){
		return (Map)request.getSession().getAttribute("user");
	}
}
