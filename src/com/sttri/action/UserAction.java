package com.sttri.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.TblRole;
import com.sttri.pojo.TblUser;
import com.sttri.pojo.UserRole;
import com.sttri.service.IRoleService;
import com.sttri.service.IUserRoleService;
import com.sttri.service.IUserService;
import com.sttri.util.JsonUtil;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;

public class UserAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String rows;            
	private String page;
	
	private TblUser user;
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IUserRoleService userRoleService;
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String queryAccount = Util.dealNull(request.getParameter("queryAccount"));
		String accountType = Util.dealNull(request.getParameter("accountType"));
		PageView<TblUser> pageView = new PageView<TblUser>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" 1=1 ");
			if(!queryAccount.equals("")){
				jpql.append("and o.account like '%"+queryAccount+"%' ");
			}
			if (!accountType .equals("")) {
				jpql.append("and o.accountType = '"+Integer.parseInt(accountType)+"' ");
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<TblUser> qr = userService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"account\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}
	
	public String getList(){
		response.setCharacterEncoding("UTF-8");
		List<TblUser> ulist = null;
		try {
			PrintWriter pw = response.getWriter();
			ulist = userService.getResultList("1=1 ", null);
			if(ulist==null || ulist.size()==0){
				ulist = new ArrayList<TblUser>();
			}
			pw.print(new JsonView(ulist));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}
	
	public void save(){
		response.setCharacterEncoding("UTF-8");
		try {
			String result = "";
			List<TblUser> list = userService.getResultList("o.account=?", null, user.getAccount());
			if(list!=null && list.size()>0){
				result = "account";
				JsonUtil.jsonString(response, result);
				return ;
			}
			if(user.getAccountType()==0){
				list = userService.getResultList("o.company.id=? and o.accountType=0", null, user.getCompany().getId());
				if(list!=null && list.size()>0){
					JsonUtil.jsonString(response, "manager");
					return ;
				}
				String pwd = user.getPwd();
				if (!Util.isNormalPwd(pwd)) {
					JsonUtil.jsonString(response, "pwdFalse");
					return ;
				}
				user.setPwd(WorkUtil.pwdEncrypt(pwd));
			}
			user.setId(Util.getUUID(6));
			user.setAddTime(Util.dateToStr(new Date()));
			user.setLoginTimes(0);
			userService.save(user);
			JsonUtil.jsonString(response, "success");
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	public void update(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			TblUser u = userService.getById(user.getId());
			if(!user.getPwd().equals(u.getPwd())){
				String pwd = user.getPwd();
				if (!Util.isNormalPwd(pwd)) {
					pw.print("pwdFalse");
					pw.flush();
					pw.close();
					return ;
				}
				user.setPwd(WorkUtil.pwdEncrypt(pwd));
			}
			user.setEditTime(Util.dateToStr(new Date()));
			userService.update(user);
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	public String getbyid(){
		response.setCharacterEncoding("UTF-8");
		try {
			String id = Util.dealNull(request.getParameter("id"));
			TblUser u = null;
			if(!id.equals("")){
				u = userService.getById(id);
			}
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(u));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String deletebyids(){
		response.setCharacterEncoding("UTF-8");
		try {
			String ids = Util.dealNull(request.getParameter("ids"));
			if(!"".equals(ids) && null!=ids){
				userService.deletebyids(ids.split("_"));
				PrintWriter pw = response.getWriter();
				pw.print("success");
				pw.flush();
				pw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 设置角色
	 * @return
	 */
	public void saveRole(){
		response.setCharacterEncoding("UTF-8");
		String userId = Util.dealNull(request.getParameter("userId"));
		String roleId = Util.dealNull(request.getParameter("roleId"));
		try {
			TblRole role = this.roleService.getById(roleId);
			List<UserRole> uRoles = this.userRoleService.getResultList("o.user.id=? ", null, new Object[]{userId});
			if (uRoles == null || uRoles.size() <= 0) {
				TblUser user = this.userService.getById(userId);
				UserRole userRole = new UserRole();
				userRole.setId(Util.getUUID(6));
				userRole.setUser(user);
				userRole.setRole(role);
				this.userRoleService.save(userRole);
			}else {
				UserRole userRole = uRoles.get(0);
				if (userRole.getRole() == null) {
					userRole.setRole(role);
					this.userRoleService.update(userRole);
				}else {
					if(!roleId.equals(userRole.getRole().getId())){
						userRole.setRole(role);
						this.userRoleService.update(userRole);
					}
				}
			}
			PrintWriter pw = response.getWriter();
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public TblUser getUser() {
		return user;
	}

	public void setUser(TblUser user) {
		this.user = user;
	}

}
