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
import com.sttri.pojo.TblBlack;
import com.sttri.service.IBlackService;
import com.sttri.util.Util;

public class BlackAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3192535830305120833L;
	
	private String rows;            
	private String page;
	
	private TblBlack black;
	
	@Autowired
	private IBlackService blackService;

	
	public void query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String addTimeStart = Util.dealNull(request.getParameter("addTimeStart"));
		String addTimeEnd = Util.dealNull(request.getParameter("addTimeEnd"));
		String queryAccount = Util.dealNull(request.getParameter("queryAccount"));
		PageView<TblBlack> pageView = new PageView<TblBlack>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" 1=1 ");
			if(!addTimeStart.equals("")){
				jpql.append(" and o.commentTime >=?");
				param.add(addTimeStart);
				if(!addTimeEnd.equals("")){
					jpql.append(" and o.commentTime<=?");
					param.add(addTimeEnd);
				}else{
					jpql.append(" and o.commentTime<=?");
					param.add(Util.dateToStr(new Date()));
				}
			}
			
			if(!"".equals(queryAccount)){
				jpql.append(" and o.user.account=?");
				param.add(queryAccount);
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<TblBlack> qr = this.blackService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"addTime\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	public void deletebyids(){
		response.setCharacterEncoding("UTF-8");
		try {
			String ids = Util.dealNull(request.getParameter("ids"));
			if(!"".equals(ids) && null!=ids){
				String[] array = ids.split("_");
				for (int i = 0; i < array.length; i++) {
					TblBlack black = this.blackService.getById(array[i]);
					saveUserLog("将用户【"+black.getUser().getAccount()+"】移除黑名单");
				}
				this.blackService.deletebyids(array);
				PrintWriter pw = response.getWriter();
				pw.print("success");
				pw.flush();
				pw.close();
			}
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

	public TblBlack getTblBlack() {
		return black;
	}

	public void setTblBlack(TblBlack black) {
		this.black = black;
	}
	
}
