package com.udbac.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.dao.AfterDataDao;
import com.udbac.entity.AfterData;
import com.udbac.service.AfterDataService;

@Service
public class AfterDataServiceImpl implements AfterDataService {

	@Autowired
	AfterDataDao dao;
	
	@Override
	public List<AfterData> list(Date startDate,Date endDate, List<String> list) {
		// TODO Auto-generated method stub
		return dao.list(startDate,endDate, list);
	}

}
