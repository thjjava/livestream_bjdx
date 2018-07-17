package com.sttri.service.impl;

import java.util.LinkedHashMap;
import java.util.List;






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sttri.bean.QueryResult;
import com.sttri.dao.CommonDao;
import com.sttri.pojo.SystemAlarm;
import com.sttri.service.ISystemAlarmService;

@Service
public class SystemAlarmServiceImpl implements ISystemAlarmService {
	@Autowired
	private CommonDao dao;
	
	@Override
	public void deletebyid(Object id) {
		dao.delete(SystemAlarm.class, id);
	}

	@Override
	public void deletebyids(Object[] array) {
		dao.delete(SystemAlarm.class, array);
	}

	@Override
	public SystemAlarm getById(Object id) {
		return dao.find(SystemAlarm.class, id);
	}

	@Override
	public List<SystemAlarm> getResultList(String wherejpql,
			LinkedHashMap<String, String> orderby, Object... queryParams) {
		return dao.getResultList(SystemAlarm.class, wherejpql, orderby, queryParams);
	}

	@Override
	public QueryResult<SystemAlarm> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return dao.getScrollData(SystemAlarm.class, firstindex, maxresult, wherejpql, queryParams, orderby);
	}

	@Override
	public void save(SystemAlarm systemAlarm) {
		dao.save(systemAlarm);
	}

	@Override
	public void update(SystemAlarm systemAlarm) {
		dao.update(systemAlarm);
	}


}
