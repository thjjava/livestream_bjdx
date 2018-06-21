package com.sttri.service;

import java.util.LinkedHashMap;
import java.util.List;

import com.sttri.bean.QueryResult;
import com.sttri.pojo.UserLog;

public interface IUserLogService {
	/**
     * 根据条件来进行查询
     * @param wherejpql
     * @param orderby
     * @param queryParams
     * @return
     */
	public List<UserLog> getResultList(String wherejpql,LinkedHashMap<String, String> orderby, Object... queryParams);
	
	
	/**
	 * 分页信息查询
	 * */
	public QueryResult<UserLog> getScrollData(int firstindex,int maxresult, String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby);

	public void save(UserLog userLog);

	public void update(UserLog userLog);
	
	public UserLog getById(Object id);

	public void deletebyids(Object[] array);

	public void deletebyid(Object id);
	
}
