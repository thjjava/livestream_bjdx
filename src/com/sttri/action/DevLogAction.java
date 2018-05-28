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
import com.sttri.pojo.DevLog;
import com.sttri.service.IDevLogService;
import com.sttri.util.Util;


public class DevLogAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rows;            
	private String page;
	
	private DevLog devLog;
	@Autowired
	private IDevLogService devLogService;
	
	
	public void query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String queryLogType = Util.dealNull(request.getParameter("queryLogType"));
		String queryIsp = Util.dealNull(request.getParameter("queryIsp"));
		String addTimeStart = Util.dealNull(request.getParameter("addTimeStart"));
		String addTimeEnd = Util.dealNull(request.getParameter("addTimeEnd"));
		PageView<DevLog> pageView = new PageView<DevLog>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer("1=1 ");
			if (!"".equals(queryLogType)) {
				jpql.append(" and o.logType=?");
				param.add(Integer.parseInt(queryLogType));
			}
			if (!"".equals(queryIsp)) {
				String isp=null;
				switch (Integer.parseInt(queryIsp)) {
				case 0:
					isp = "电信";
					break;
				case 1:
					isp = "移动";
					break;
				case 2:
					isp = "联通";
					break;
				case 3:
					isp = "铁通";
					break;
				default:
					isp = null;
					break;
				}
				jpql.append(" and o.operatorName=?");
				param.add(isp);
			}
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
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("addTime", "desc");
			QueryResult<DevLog> qr = this.devLogService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"logDesc\":\"无记录数据\"}]}";
				pw.print(json);
			}
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

	public DevLog getDevLog() {
		return devLog;
	}

	public void setDevLog(DevLog devLog) {
		this.devLog = devLog;
	}

}
