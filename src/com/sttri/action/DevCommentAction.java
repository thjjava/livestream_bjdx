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
import com.sttri.pojo.DevComment;
import com.sttri.pojo.TblBlack;
import com.sttri.pojo.TblUser;
import com.sttri.service.IBlackService;
import com.sttri.service.IDevCommentService;
import com.sttri.util.Util;

public class DevCommentAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3192535830305120833L;
	
	private String rows;            
	private String page;
	
	private DevComment devComment;
	
	@Autowired
	private IDevCommentService devCommentService;
	@Autowired
	private IBlackService blackService;

	
	public void query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		String queryIsLegal = Util.dealNull(request.getParameter("queryIsLegal"));
		String addTimeStart = Util.dealNull(request.getParameter("addTimeStart"));
		String addTimeEnd = Util.dealNull(request.getParameter("addTimeEnd"));
		String queryDevNo = Util.dealNull(request.getParameter("queryDevNo"));
		PageView<DevComment> pageView = new PageView<DevComment>(row, pages);
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
			
			if(!"".equals(queryDevNo)){
				jpql.append(" and o.dev.devNo=?");
				param.add(queryDevNo);
			}
			if (!"".equals(queryIsLegal)) {
				jpql.append(" and o.isLegal =?");
				param.add(Integer.parseInt(queryIsLegal));
			}
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<DevComment> qr = this.devCommentService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"content\":\"无记录数据\"}]}";
				pw.print(json);
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	/**
	 * 审核评论
	 */
	public void check(){
		response.setCharacterEncoding("UTF-8");
		String ids = Util.dealNull(request.getParameter("ids"));
		try {
			PrintWriter pw = response.getWriter();
			if(!"".equals(ids) && null!=ids){
				String[] idArray = ids.split("_");
				for (int i = 0; i < idArray.length; i++) {
					String id = idArray[i];
					DevComment devComment = this.devCommentService.getById(id);
					if (devComment != null) {
						int isLegal = devComment.getIsLegal()==0?1:0;//isLegal=0 合法，=1违规 
						devComment.setIsLegal(isLegal);
						this.devCommentService.update(devComment);
						saveUserLog("审核评论是否合法");
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
	
	/**
	 * 拉黑
	 */
	public void addBlack(){
		response.setCharacterEncoding("UTF-8");
		String ids = Util.dealNull(request.getParameter("ids"));
		try {
			PrintWriter pw = response.getWriter();
			if(!"".equals(ids) && null!=ids){
				String[] idArray = ids.split("_");
				for (int i = 0; i < idArray.length; i++) {
					String id = idArray[i];
					DevComment devComment = this.devCommentService.getById(id);
					if (devComment != null) {
						devComment.setIsLegal(1);//isLegal=0 合法，=1违规
						this.devCommentService.update(devComment);
						TblUser user = devComment.getUser();
						List<TblBlack> blacks = this.blackService.getResultList(" o.user.id=?", null, new Object[]{user.getId()});
						if (blacks == null || blacks.size() <= 0) {
							TblBlack black = new TblBlack();
							black.setId(Util.getUUID(6));
							black.setUser(user);
							black.setFlag(1);//flag是否可以移除黑名单 0-可以 1-不可以
							black.setAddTime(Util.dateToStr(new Date()));
							this.blackService.save(black);
							saveUserLog("将违法评论的用户账号拉入黑名单："+user.getAccount());
						}
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

	public DevComment getDevComment() {
		return devComment;
	}

	public void setDevComment(DevComment devComment) {
		this.devComment = devComment;
	}
	
}
