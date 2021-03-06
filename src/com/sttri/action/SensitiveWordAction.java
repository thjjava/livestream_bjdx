package com.sttri.action;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.SensitiveWord;
import com.sttri.service.ISensitiveWordService;
import com.sttri.util.CreateFile;
import com.sttri.util.ExcelUtil;
import com.sttri.util.Util;

public class SensitiveWordAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3192535830305120833L;
	
	private String rows;            
	private String page;
	
	private SensitiveWord sensitiveWord;
	
	private File upload;
	private String uploadFileName;
	
	@Autowired
	private ISensitiveWordService sensitiveWordService;

	
	public void query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String addTimeStart = Util.dealNull(request.getParameter("addTimeStart"));
		String addTimeEnd = Util.dealNull(request.getParameter("addTimeEnd"));
		String sensitiveWord = Util.dealNull(request.getParameter("querySensitive"));
		PageView<SensitiveWord> pageView = new PageView<SensitiveWord>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" 1=1 ");
			if(!addTimeStart.equals("")){
				jpql.append(" and o.addTime >=?");
				param.add(addTimeStart);
				if(!addTimeEnd.equals("")){
					jpql.append(" and o.addTime<=?");
					param.add(addTimeEnd);
				}else{
					jpql.append(" and o.addTime<=?");
					param.add(Util.dateToStr(new Date()));
				}
			}
			
			if(!"".equals(sensitiveWord)){
				jpql.append(" and o.sensitiveWord like ?");
				param.add("%"+sensitiveWord+"%");
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<SensitiveWord> qr = this.sensitiveWordService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"sensitive\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	public void getList(){
		response.setCharacterEncoding("UTF-8");
		List<SensitiveWord> sList = null;
		try {
			PrintWriter pw = response.getWriter();
			sList = this.sensitiveWordService.getResultList("1=1 ", null);
			if(sList==null || sList.size()==0){
				sList = new ArrayList<SensitiveWord>();
			}
			pw.print(new JsonView(sList));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save(){
		response.setCharacterEncoding("UTF-8");
		try {
			String result = "fail";
			List<SensitiveWord> list = this.sensitiveWordService.getResultList("o.sensitiveWord = ?", null, new Object[]{sensitiveWord.getSensitiveWord()});
			if(list!=null && list.size()>0){
				result = "hased";
			}else{
				sensitiveWord.setId(Util.getUUID(6));
				sensitiveWord.setAddTime(Util.dateToStr(new Date()));
				this.sensitiveWordService.save(sensitiveWord);
				result = "success";
				saveUserLog("新建敏感词："+sensitiveWord.getSensitiveWord());
			}
			
			PrintWriter pw = response.getWriter();
			pw.print(result);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update(){
		response.setCharacterEncoding("UTF-8");
		try {
			this.sensitiveWordService.update(sensitiveWord);
			saveUserLog("修改敏感词信息："+sensitiveWord.getSensitiveWord());
			PrintWriter pw = response.getWriter();
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getbyid(){
		response.setCharacterEncoding("UTF-8");
		try {
			String id = Util.dealNull(request.getParameter("id"));
			SensitiveWord u = null;
			if(!"".equals(id)){
				u = this.sensitiveWordService.getById(id);
			}
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(u));
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deletebyids(){
		response.setCharacterEncoding("UTF-8");
		try {
			String ids = Util.dealNull(request.getParameter("ids"));
			if(!"".equals(ids) && null!=ids){
				String[] array = ids.split("_");
				this.sensitiveWordService.deletebyids(array);
				PrintWriter pw = response.getWriter();
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
							List<Map<Object, Object>> list = ExcelUtil.readExcel(saveFilePath+wjml);
							for (int i = 0; i < list.size(); i++) {
								Map<Object, Object> map = list.get(i);
								String sensitiveWord = (String) map.get("cell"+i);
								List<SensitiveWord> sWords = this.sensitiveWordService.getResultList(" o.sensitiveWord=? ", null, sensitiveWord);
								if (sWords == null || sWords.size() == 0) {
									SensitiveWord sensitive = new SensitiveWord();
									sensitive.setId(Util.getUUID(6));
									sensitive.setSensitiveWord(sensitiveWord);
									sensitive.setAddTime(Util.dateToStr(new Date()));
									this.sensitiveWordService.save(sensitive);
									saveUserLog("批量导入敏感词："+sensitiveWord);
								}
							}
							key = "success";
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

	public SensitiveWord getSensitiveWord() {
		return sensitiveWord;
	}

	public void setSensitiveWord(SensitiveWord sensitiveWord) {
		this.sensitiveWord = sensitiveWord;
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
