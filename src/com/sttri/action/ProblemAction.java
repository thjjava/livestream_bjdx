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
import com.sttri.pojo.TblProblem;
import com.sttri.service.IProblemService;
import com.sttri.util.JsonUtil;
import com.sttri.util.Util;

public class ProblemAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String rows;            
	private String page;
	
	private TblProblem problem;
	
	@Autowired
	private IProblemService problemService;
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String queryAccount = Util.dealNull(request.getParameter("queryDevNo"));
		PageView<TblProblem> pageView = new PageView<TblProblem>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" 1=1 ");
			if(!queryAccount.equals("")){
				jpql.append("and o.devNo like '%"+queryAccount+"%' ");
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<TblProblem> qr = problemService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"devNo\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getList(){
		response.setCharacterEncoding("UTF-8");
		List<TblProblem> ulist = null;
		try {
			PrintWriter pw = response.getWriter();
			ulist = problemService.getResultList("1=1 ", null);
			if(ulist==null || ulist.size()==0){
				ulist = new ArrayList<TblProblem>();
			}
			pw.print(new JsonView(ulist));
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
			problem.setId(Util.getUUID(6));
			problem.setAddTime(Util.dateToStr(new Date()));
			problemService.save(problem);
			JsonUtil.jsonString(response, "success");
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}
	
	public void update(){
		response.setCharacterEncoding("UTF-8");
		try {
			TblProblem p = this.problemService.getById(problem.getId());
			problem.setNetType(p.getNetType());
			problemService.update(problem);
			PrintWriter pw = response.getWriter();
			pw.print("success");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getbyid(){
		response.setCharacterEncoding("UTF-8");
		try {
			String id = Util.dealNull(request.getParameter("id"));
			TblProblem u = null;
			if(!id.equals("")){
				u = problemService.getById(id);
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
				problemService.deletebyids(ids.split("_"));
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

	public TblProblem getProblem() {
		return problem;
	}

	public void setProblem(TblProblem problem) {
		this.problem = problem;
	}

}
