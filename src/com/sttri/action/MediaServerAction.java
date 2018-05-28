package com.sttri.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.et.mvc.JsonView;
import com.sttri.bean.PageView;
import com.sttri.bean.QueryJSON;
import com.sttri.bean.QueryResult;
import com.sttri.pojo.MediaServer;
import com.sttri.service.IMediaServerService;
import com.sttri.util.Util;

public class MediaServerAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	private String rows;            
	private String page;
	
	private MediaServer mediaServer;
	
	@Autowired
	private IMediaServerService mediaServerService;
	
	public String query(){
		response.setCharacterEncoding("UTF-8");
		int pages = Integer.parseInt((page == null || page == "0") ? "1":page);           
		int row = Integer.parseInt((rows == null || rows == "0") ? "10":rows); 
		
		PageView<MediaServer> pageView = new PageView<MediaServer>(row, pages);
		List<Object> param = new ArrayList<Object>();
		try {
			StringBuffer jpql = new StringBuffer(" 1=1 ");
			int firstindex = (pageView.getCurrentPage() - 1)*pageView.getMaxResult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			QueryResult<MediaServer> qr = mediaServerService.getScrollData(firstindex, pageView.getMaxResult(), jpql.toString(), param.toArray(), orderby);
			PrintWriter pw = response.getWriter();
			if(qr!=null && qr.getResultList().size()>0){
				pageView.setQueryResult(qr);
				QueryJSON qu = new QueryJSON();
				qu.setRows(pageView.getRecords());
				qu.setTotal(pageView.getTotalRecord());
				pw.print(new JsonView(qu));
			}else{
				String json = "{\"total\":1,\"rows\":[{\"serverName\":\"无记录数据\"}]}";
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
		List<MediaServer> mslist = null;
		try {
			PrintWriter pw = response.getWriter();
			mslist = mediaServerService.getResultList("1=1 ", null);
			if(mslist==null || mslist.size()==0){
				mslist = new ArrayList<MediaServer>();
			}
			pw.print(new JsonView(mslist));
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
			mediaServer.setId(Util.getUUID(6));
			mediaServer.setCpuUsed("0");
			mediaServer.setDevNum(0);
			mediaServer.setMemoryUsed("0");
			mediaServer.setPlayNum(0);
			mediaServer.setOnLine(0);
			mediaServerService.save(mediaServer);
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
			MediaServer ms = mediaServerService.getById(mediaServer.getId());
			mediaServer.setCpuUsed(ms.getCpuUsed());
			mediaServer.setMemoryUsed(ms.getMemoryUsed());
			mediaServer.setDevNum(ms.getDevNum());
			mediaServer.setPlayNum(ms.getPlayNum());
			mediaServer.setOnLine(ms.getOnLine());
			mediaServerService.update(mediaServer);
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
			MediaServer ms = null;
			if(!id.equals("")){
				ms = mediaServerService.getById(id);
			}
			PrintWriter pw = response.getWriter();
			pw.print(new JsonView(ms));
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
				mediaServerService.deletebyids(ids.split("_"));
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
	 * 设置服务器的状态为在线或离线
	 */
	public void setOnLine(){
		response.setCharacterEncoding("UTF-8");
		String ids = Util.dealNull(request.getParameter("ids"));
		try {
			PrintWriter pw = response.getWriter();
			if(!"".equals(ids) && null!=ids){
				String[] idArray = ids.split("_");
				for (int i = 0; i < idArray.length; i++) {
					String id = idArray[i];
					MediaServer server = this.mediaServerService.getById(id);
					if (server != null) {
						int onLine = server.getOnLine()==0?1:0;//onLine=0 离线，=1在线 
						server.setOnLine(onLine);
						this.mediaServerService.update(server);
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

	public MediaServer getMediaServer() {
		return mediaServer;
	}

	public void setMediaServer(MediaServer mediaServer) {
		this.mediaServer = mediaServer;
	}

}
