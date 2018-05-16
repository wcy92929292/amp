package com.udbac.service.impl;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.dao.IndexDao;
import com.udbac.service.IndexService;

/****
 * 概览画面计算用问题数用
 * 
 * @author lp
 *
 */
@Service
public class IndexServiceImpl implements IndexService {
	@Autowired
	private IndexDao dao;

	@Override
	public List<Object> QueryList(Map<String, String> map) {
		return dao.QueryList(map);
	}

}
