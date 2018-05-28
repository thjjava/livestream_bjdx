package com.sttri.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.Company;
import com.sttri.pojo.TblDev;
import com.sttri.service.ICompanyService;
import com.sttri.service.IDevService;
import com.sttri.util.AesUtil;
import com.sttri.util.Constant;
import com.sttri.util.Escape;
import com.sttri.util.Util;

public class CompanyAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String rows;            
	private String page;
	
	private Company company;
	
	@Autowired
	private ICompanyService companyService;
	@Autowired
	private IDevService devService;
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String queryComName = Util.dealNull(Escape.unescape(Escape.unescape(request.getParameter("queryComName"))));
		
		PageView<Company> pageView = new PageView<Company>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" 1=1 ");
			if(!queryComName.equals("")){
				jpql.append("and o.comName like '%"+queryComName+"%' ");
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<Company> qr = companyService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"comName\":\"无记录数据\"}]}";
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
		List<Company> clist = null;
		try {
			PrintWriter pw = response.getWriter();
			clist = companyService.getResultList("1=1 ", null);
			if(clist==null || clist.size()==0){
				clist = new ArrayList<Company>();
			}
			pw.print(new JsonView(clist));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}
	
	public String save(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			//企业名唯一
			String comName = company.getComName();
			List<Company> cList = this.companyService.getResultList(" o.comName = ?", null, comName);
			if(cList!=null && cList.size() >0){
				pw.print("Hased");
				pw.flush();
				pw.close();
				return null;
			}
			String license = company.getLicense();
			boolean flag = isLicense(license, comName);
			if(!flag){
				pw.print("false");
				pw.flush();
				pw.close();
				return null;
			}
			String content = parseLicense(license, comName);
			if(content !=null && !"".equals(content)){
				String[] con = content.split(",");
				if(con[0].equals(con[1])){
					company.setComDevNums(Integer.parseInt(con[0]));
				}
			}
			company.setId(Util.getUUID(6));
			company.setAddTime(Util.dateToStr(new Date()));
			companyService.save(company);
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}
	
	public String update(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			String comName = company.getComName();
			String license = company.getLicense();
			boolean flag = isLicense(license, comName);
			if(!flag){
				pw.print("false");
				pw.flush();
				pw.close();
				return null;
			}
			String content = parseLicense(license, comName);
			if(content !=null && !"".equals(content)){
				String[] con = content.split(",");
				if(con[0].equals(con[1])){
					company.setComDevNums(Integer.parseInt(con[0]));
				}
			}
			company.setEditTime(Util.dateToStr(new Date()));
			companyService.update(company);
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}
	
	public String getbyid(){
		response.setCharacterEncoding("UTF-8");
		try {
			String id = Util.dealNull(request.getParameter("id"));
			Company c = null;
			if(!id.equals("")){
				c = companyService.getById(id);
			}
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(c));
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
				companyService.deletebyids(ids.split("_"));
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
	 * 验证企业license和企业名称是否匹配
	 * @param license 企业license
	 * @param comName 企业名称
	 * @return
	 */
	public boolean isLicense(String license,String comName){
		boolean flag = false;
		try {
			String content = parseLicense(license, comName);
			if(content !=null && !"".equals(content)){
				flag = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 根据企业名称解密企业license
	 * @param license 企业license
	 * @param comName 企业名称
	 * @return
	 */
	public String parseLicense(String license,String comName){
		try {
			String mac = Constant.readKey("mac");
			String key = AesUtil.md5Encrypt(mac+comName);
			System.out.println("key=:"+key);
			String content = AesUtil.aesDecrypt(license, key);
			System.out.println("content:"+content);
			return content;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取企业设备数信息
	 */
	public void getDevNums(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			String id = Util.dealNull(request.getParameter("id"));
			int total = 0,nums=0;
			if(!"".equals(id) && null!=id){
				Company com = this.companyService.getById(id);
				if(com != null){
					String comName = com.getComName();
					String license = com.getLicense();
					total = license==null?com.getComDevNums():0;
					String content = parseLicense(license, comName);
					if(content !=null && !"".equals(content)){
						String[] con = content.split(",");
						if(con[0].equals(con[1])){
							if(checkStr(con[0])){
								total = Integer.parseInt(con[0]);
							}
						}
					}
					List<TblDev> dList = devService.getResultList("o.company.id=?", null, new Object[]{id});
					if(dList != null && dList.size() > 0){
						nums = dList.size();
					}
				}
			}
			pw.print("{\"total\":"+total+",\"used\":"+nums+"}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//判断字符串是否都为数字 返回true则都为数字
	public boolean checkStr(String str){
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence)str);
		boolean result = matcher.matches();
		return result;
	}
	
	/**
	 * 根据企业名称创建License
	 * 使用ASE加密算法
	 * 创建license方法： 项目部署服务器的mac+企业名称 生成经过base64转化的md5加密密钥作为key,设备数+','+设备数作为加密内容  进行aes加密
	 */
	public void getLicense(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			String comName = Util.dealNull(request.getParameter("comName"));
			String devNum = Util.dealNull(request.getParameter("devNum"));
			String license = "";
			if(comName != null && !"".equals(comName) && devNum != null && !"".equals(devNum)){
				String mac = Constant.readKey("mac");
				devNum +=","+devNum;
				String key = AesUtil.md5Encrypt(mac+comName);
				System.out.println("key=:"+key);
				license = AesUtil.aesEncrypt(devNum, key);
				System.out.println("license:"+license);
			}
			pw.print(license);
			pw.flush();
			pw.close();
		} catch (Exception e) {
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
