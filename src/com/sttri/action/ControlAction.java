package com.sttri.action;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.TblControl;
import com.sttri.service.IControlService;
import com.sttri.util.CreateFile;
import com.sttri.util.Util;

public class ControlAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private File upload;
	private String uploadFileName;
	private String uploadContentType;
	
	private String rows;
	private String page;
	
	private TblControl control;
	
	@Autowired
	private IControlService controlService;
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		
		PageView<TblControl> pageView = new PageView<TblControl>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" 1=1 ");
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<TblControl> qr = controlService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"conName\":\"无记录数据\"}]}";
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
		List<TblControl> clist = null;
		try {
			PrintWriter pw = response.getWriter();
			clist = controlService.getResultList("1=1 ", null);
			if(clist==null || clist.size()==0){
				clist = new ArrayList<TblControl>();
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
			control.setId(Util.getUUID(6));
			control.setAddTime(Util.dateToStr(new Date()));
			controlService.save(control);
			PrintWriter pw = response.getWriter();
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
			control.setEditTime(Util.dateToStr(new Date()));
			controlService.update(control);
			PrintWriter pw = response.getWriter();
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
			TblControl c = null;
			if(!id.equals("")){
				c = controlService.getById(id);
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
				controlService.deletebyids(ids.split("_"));
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
	
	public String upload(){
		response.setCharacterEncoding("UTF-8");
		try {
			String saveFilePath = ServletActionContext.getServletContext().getRealPath(File.separator);
			String key = "";
			String value = "";
			String wjml = "";
			boolean createFolder = CreateFile.createFolder(saveFilePath+wjml);
			if(createFolder){
				if(upload!=null){
					String oldfiletype = Util.getExtendName(this.getUploadFileName()).toLowerCase();
					if(oldfiletype.equals(".apk")||oldfiletype.equals(".exe")||oldfiletype.equals(".msi")||oldfiletype.equals(".ipa")){
//						String newfilename = Util.getUUID(8)+oldfiletype;
						String newfilename = this.getUploadFileName();
						wjml = "uploadFile"+File.separator+"app"+File.separator;
						File file = new File(saveFilePath+wjml, newfilename);
						if(file.exists())
							file.delete();
						FileUtils.copyFile(this.getUpload(), file);
						wjml += newfilename;
						
						if(File.separator.equals("\\")){
							key = "success";
							value = wjml.replaceAll("\\\\", "/");
						}else if(File.separator.equals("/")){
							key = "success";
							value = wjml;
						}else{
							key = "fail";
							value = "";
						}
					}else{
						key = "pictype";
					}
				}
			}
			PrintWriter pw = response.getWriter();
			pw.print("{'key':'"+key+"','value':'"+value+"','fileName':'"+this.getUploadFileName()+"'}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
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

	public TblControl getControl() {
		return control;
	}

	public void setControl(TblControl control) {
		this.control = control;
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

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

}
