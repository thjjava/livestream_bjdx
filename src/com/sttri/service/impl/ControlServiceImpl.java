package com.sttri.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sttri.bean.QueryResult;
import com.sttri.dao.CommonDao;
import com.sttri.pojo.TblControl;
import com.sttri.service.IControlService;

@Service
public class ControlServiceImpl implements IControlService {
	@Autowired
	private CommonDao dao;
	@Override
	public void deletebyid(Object id) {
		dao.delete(TblControl.class, id);
	}

	@Override
	public void deletebyids(Object[] array) {
		dao.delete(TblControl.class, array);
	}

	@Override
	public TblControl getById(Object id) {
		return dao.find(TblControl.class, id);
	}

	@Override
	public List<TblControl> getResultList(String wherejpql,
			LinkedHashMap<String, String> orderby, Object... queryParams) {
		return dao.getResultList(TblControl.class, wherejpql, orderby, queryParams);
	}

	@Override
	public QueryResult<TblControl> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return dao.getScrollData(TblControl.class, firstindex, maxresult, wherejpql, queryParams, orderby);
	}

	@Override
	public void save(TblControl control) {
		dao.save(control);
	}

	@Override
	public void update(TblControl control) {
		dao.update(control);
	}

}
