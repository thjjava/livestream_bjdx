package com.sttri.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.Company;
import com.sttri.pojo.TblDev;
import com.sttri.service.ICompanyService;
import com.sttri.service.IDevService;
import com.sttri.util.CreateFile;
import com.sttri.util.ExcelUtil;
import com.sttri.util.Util;
import com.sttri.util.WorkUtil;

public class DevAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String rows;            
	private String page;
	private TblDev dev;
	private File upload;
	private String uploadFileName;
	
	@Autowired
	private IDevService devService;
	@Autowired
	private ICompanyService companyService;
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String queryDevNo = Util.dealNull(request.getParameter("queryDevNo"));
		String queryCompany = Util.dealNull(request.getParameter("queryCompany"));
		String queryDevName = Util.dealNull(request.getParameter("queryDevName"));
		
		PageView<TblDev> pageView = new PageView<TblDev>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" 1=1 ");
			if(!queryDevNo.equals("")){
				jpql.append("and o.devNo like '%"+queryDevNo+"%' ");
			}
			if(Util.notEmpty(queryCompany)){
				jpql.append("and o.company.id=? ");
				param.add(queryCompany);
			}
			
			if(!"".equals(queryDevName)){
				jpql.append("and o.devName like '%"+queryDevName+"%' ");
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<TblDev> qr = devService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"devName\":\"无记录数据\"}]}";
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
		List<TblDev> dlist = null;
		try {
			PrintWriter pw = response.getWriter();
			dlist = devService.getResultList("1=1 ", null);
			if(dlist==null || dlist.size()==0){
				dlist = new ArrayList<TblDev>();
			}
			pw.print(new JsonView(dlist));
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
			PrintWriter pw = response.getWriter();
			List<TblDev> dlist = devService.getResultList("o.devNo=?", null, dev.getDevNo());
			if(dlist !=null && dlist.size()>0){
				pw.print("devNo");
				pw.flush();
				pw.close();
				return;
			}
			dlist = devService.getResultList("o.devName=?", null, dev.getDevName());
			if(dlist !=null && dlist.size()>0){
				pw.print("devName");
				pw.flush();
				pw.close();
				return;
			}
			
			Company company = companyService.getById(dev.getCompany().getId());
			dlist = devService.getResultList("o.company.id=?", null, new Object[]{dev.getCompany().getId()});
			
			int comdevs = company.getComDevNums();
			int ds = 0;
			if(dlist!=null && dlist.size()>0)
				ds = dlist.size();
			
			String rt = "fail";
			if(comdevs>ds){
				dev.setId(Util.getUUID(6));
				dev.setOnLines(1);
				dev.setIsAble(0);
				dev.setAddTime(Util.dateToStr(new Date()));
//				dev.setDevKey(WorkUtil.pwdEncrypt(dev.getDevKey()));
				dev.setFullFlag(0);
				dev.setLoginTimes(0);
				devService.save(dev);
				rt = "success";
			}else{
				rt = "devnums";
			}
			pw.print(rt);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	public void update(){
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter pw = response.getWriter();
			TblDev d = devService.getById(dev.getId());
			String devKey = dev.getDevKey();
			if(!devKey.equals(d.getDevKey())){
				dev.setDevKey(WorkUtil.pwdEncrypt(dev.getDevKey()));
			}
			String devNo = dev.getDevNo();
			if (!devNo.equals(d.getDevNo())) {
				List<TblDev> devs = this.devService.getResultList(" o.devName=?", null, new Object[]{devNo});
				if (devs != null && devs.size() >0) {
					pw.print("devNo");
					pw.flush();
					pw.close();
					return;
				}
			}
			String devName = dev.getDevName();
			if (!devName.equals(d.getDevName())) {
				List<TblDev> devs = this.devService.getResultList(" o.devName=?", null, new Object[]{devName});
				if (devs != null && devs.size() >0) {
					pw.print("devName");
					pw.flush();
					pw.close();
					return;
				}
			}
			dev.setFullFlag(d.getFullFlag());
			dev.setEditTime(Util.dateToStr(new Date()));
			devService.update(dev);
			
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
			TblDev d = null;
			if(!id.equals("")){
				d = devService.getById(id);
			}
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(d));
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
				devService.deletebyids(ids.split("_"));
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
	
	public String isAble(){
		response.setCharacterEncoding("UTF-8");
		JSONObject obj = new JSONObject();
		obj.put("key", "fail");
		String id = Util.dealNull(request.getParameter("id"));
		int isAble = Integer.parseInt(Util.dealNull(request.getParameter("isAble")));
		TblDev dev = devService.getById(id);
		if(dev!=null){
			if(dev.getOnLines()!=0){
				dev.setIsAble(isAble);
				devService.update(dev);
				obj.put("desc", "更新成功!");
			}else{
				obj.put("desc", "当前设备在线!");
			}
		}else{
			obj.put("desc", "没有找到当前设备!");
		}
		try {
			PrintWriter pw = response.getWriter();
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void saveMore(){
		response.setCharacterEncoding("UTF-8");
		String devFirstNo = Util.dealNull(request.getParameter("devFirstNo"));
		String comId = Util.dealNull(request.getParameter("comId"));
		int devNums = Integer.parseInt(Util.dealNull(request.getParameter("devNums")));//添加设备数
		int devNumsUsed = Integer.parseInt(Util.dealNull(request.getParameter("devNumsUsed")));//该公司已经拥有的设备数
		try {
			Company company = this.companyService.getById(comId);
			TblDev tblDev = null;
			for(int i=0;i<devNums;i++){
				tblDev = new TblDev();
				// 0 代表前面补充0         
			    // 4 代表长度为4         
			    // d 代表参数为正数型         
			    String formatNum = String.format("%04d", (devNumsUsed+i+1));         
//				String devNo = devFirstNo+(devNumsUsed+i+1);
			    String devNo = devFirstNo + formatNum;
			    List<TblDev> dlist = this.devService.getResultList(" o.devNo = ?", null, devNo);
				if(dlist !=null && dlist.size()>0){
					devNums++;
					continue;
				}
				tblDev.setId(Util.getUUID(6));
				tblDev.setOnLines(1);
				tblDev.setIsAble(0);
				tblDev.setAddTime(Util.dateToStr(new Date()));
				tblDev.setDevNo(devNo);
				tblDev.setDevName(devNo);
//				tblDev.setDevKey(WorkUtil.pwdEncrypt("123456"));
				tblDev.setLoginTimes(0);
				tblDev.setCompany(company);
				tblDev.setFullFlag(0);
				this.devService.save(tblDev);
			}
			PrintWriter pw = response.getWriter();
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	/**
	 * 设置设备的启用状态为启用或禁用
	 */
	public void setUnUsed(){
		response.setCharacterEncoding("UTF-8");
		String ids = Util.dealNull(request.getParameter("ids"));
		try {
			PrintWriter pw = response.getWriter();
			if(!"".equals(ids) && null!=ids){
				String[] idArray = ids.split("_");
				for (int i = 0; i < idArray.length; i++) {
					String id = idArray[i];
					TblDev dev = this.devService.getById(id);
					if (dev != null) {
						int isAble = dev.getIsAble()==0?1:0;//isAble=0 启用，=1禁用 
						dev.setIsAble(isAble);
						this.devService.update(dev);
					}
				}
				pw.print("success");
				pw.flush();
				pw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void upload(){
		response.setCharacterEncoding("UTF-8");
		String comId = Util.dealNull(request.getParameter("comId"));
		try {
			PrintWriter pw = response.getWriter();
			String saveFilePath = ServletActionContext.getServletContext().getRealPath(File.separator);
			String wjml = "",key = "";
			boolean createFolder = CreateFile.createFolder(saveFilePath+wjml);
			if(createFolder){
				if(upload!=null){
					String oldfiletype = Util.getExtendName(this.getUploadFileName()).toLowerCase();
					if(oldfiletype.equals(".xls")){
						wjml = "uploadFile"+File.separator+"excel"+File.separator;
						String newFileName = Util.getUUID(0)+"_"+this.getUploadFileName();
						File file = new File(saveFilePath+wjml, newFileName);
						if(file.exists())
							file.delete();
						FileUtils.copyFile(this.getUpload(), file);
						wjml += newFileName;
						if(File.separator.equals("\\") || File.separator.equals("/")){
							Company company = this.companyService.getById(comId);
							List<TblDev> devlist=ExcelUtil.getByExcel(saveFilePath+wjml);
							List<TblDev> hasDevList = this.devService.getResultList("o.company.id=?", null, new Object[]{comId});
							int lastDevNums = company.getComDevNums()-hasDevList.size();
							if (lastDevNums < devlist.size()) {
								key = "devNum";
							}else {
								for (TblDev dev : devlist) {
									TblDev newDev = new TblDev();
									newDev.setId(Util.getUUID(6));
									newDev.setDevNo(dev.getDevNo());
									newDev.setDevKey(WorkUtil.pwdEncrypt(dev.getDevKey()));
									newDev.setDevName(dev.getDevName());
									newDev.setOnLines(1);
									newDev.setIsAble(0);
									newDev.setAddTime(Util.dateToStr(new Date()));
									newDev.setCompany(company);
									newDev.setFullFlag(0);
									this.devService.save(newDev);
								}
								key = "success";
							}
						}else{
							key = "fail";
						}
					}else{
						key = "pictype";
					}
				}
			}
			pw.print("{'key':'"+key+"'}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
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

	public TblDev getDev() {
		return dev;
	}

	public void setDev(TblDev dev) {
		this.dev = dev;
	}
	
	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}
	
	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

}
