package com.sttri.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sttri.pojo.TblUser;
import com.sttri.util.Constant;
import com.sttri.util.CookiesUtil;
import com.sttri.util.Util;

/**
 * 用户登陆
 *
 */

@Component
public class LoginAction extends BaseAction{
	private static final long serialVersionUID = -7848098284608038314L;
	private TblUser user;
	private static final int ERRORTIMES = 5;//错误登录次数
	
	public String login(){
		String account = Util.dealNull(request.getParameter("account"));
		String password = Util.dealNull(request.getParameter("pwd"));
		String code = Util.dealNull(request.getParameter("code"));
		String validateNumber=CookiesUtil.getString("ValidateNumber", null, request);
		if("".equals(account) || null==account){
			request.setAttribute("errorinfo", "请输入用户名！");
			return "login";
		}
		if("".equals(password) || null==password){
			request.setAttribute("errorinfo", "请输入密码！");
			return "login";
		}
		if("".equals(code) || null==code){
			request.setAttribute("errorinfo", "请输入验证码！");
			return "login";
		}
		if(!code.toUpperCase().equals(validateNumber)){
			request.setAttribute("errorinfo", "验证码输入不正确！");
			return "login";
		}
		/*
		String hql="o.account=?";
		List<TblUser> list=userService.getResultList(hql, null, account);
		if(list!=null && list.size()>0){
			TblUser att=list.get(0);
			if(att.getAccountType()!=0){
				request.setAttribute("errorinfo", "请使用管理员帐号！");
				return "login";
			}
			if(WorkUtil.pwdEncrypt(password).equals(att.getPwd())){
				request.getSession().setAttribute("user", att);
			}else{
				request.setAttribute("errorinfo", "密码不正确！");
				return "login";
			}
		}else{
			request.setAttribute("errorinfo", "用户名不存在！");
			return "login";
		}
		*/
		
		/**
		 * 设置密码错误登录次数5次，达到5次后，需要10分钟之后才能再次登录
		 */
		if(account.equals(Constant.readKey("admin"))){
			Object hasErrorLoginSession = request.getSession().getAttribute("hasErrorLogin");
			int hasErrorLogin = 0;
			if(hasErrorLoginSession != null){
				hasErrorLogin = (Integer)hasErrorLoginSession;
			}
			String hasErrorLoginTime = (String)request.getSession().getAttribute("hasErrorLoginTime");
			String now = Util.dateToStr(new Date());
			if (!"".equals(hasErrorLoginTime) && hasErrorLoginTime != null) {
				int dateDiff = (int)Util.datediff(hasErrorLoginTime, now, "yyyy-MM-dd HH:mm:ss");
				if (dateDiff <= 10*60*1000 && hasErrorLogin >= ERRORTIMES) {
					request.getSession().setAttribute("errorinfo", "登录失败的次数已达上限,请10分钟后再登录！");
					saveUserLog(account+"登录失败的次数已达上限,请10分钟后再登录！");
					return "login";
				}
			}
			if (hasErrorLogin <= ERRORTIMES) {
				if(password.equals(Constant.readKey("password"))){
					Map<String, String> map = new HashMap<String, String>();
					map.put("account", account);
					request.getSession().setAttribute("user", map);
					request.getSession().setAttribute("Account", account);
					request.getSession().setAttribute("hasErrorLogin", 0);
					request.getSession().setAttribute("hasErrorLoginTime", "");
				}else{
					request.getSession().setMaxInactiveInterval(10*60);//有效时间10分钟
					if (hasErrorLogin == 0) {
						request.getSession().setAttribute("hasErrorLoginTime", Util.dateToStr(new Date()));
					}
					hasErrorLogin +=1;
					int lastLoginTimes = ERRORTIMES-hasErrorLogin;
					request.getSession().setAttribute("hasErrorLogin", hasErrorLogin);
					request.getSession().setAttribute("errorinfo", "密码不正确,剩余登录次数还有"+lastLoginTimes+"次！");
					saveUserLog(account+"密码不正确,剩余登录次数还有"+lastLoginTimes+"次！");
					return "login";
				}
			}else {
				hasErrorLogin +=1;
				request.getSession().setAttribute("hasErrorLogin", hasErrorLogin);
				request.getSession().setAttribute("errorinfo", "登录失败的次数已达上限,请10分钟后再登录！");
				saveUserLog(account+"登录失败的次数已达上限,请10分钟后再登录！");
				return "login";
			}
		}else{
			request.setAttribute("errorinfo", "用户名不存在！");
			return "login";
		}
		
		saveUserLog(account+"登录成功！");
		return "index";
	}
	
	
	public String logout(){
		request.getSession().removeAttribute("Account");
		saveUserLog(Constant.readKey("admin")+"退出登录");
		return "logout";
	}

	public TblUser getUser() {
		return user;
	}

	public void setUser(TblUser user) {
		this.user = user;
	}
}
