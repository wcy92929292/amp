package com.udbac.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.dao.AutoScheduleDao;
import com.udbac.entity.AutoScheduleInfo;
import com.udbac.service.AutoScheduleService;

@Service
public class AutoScheduleServiceImpl implements AutoScheduleService {

	@Autowired
	AutoScheduleDao dao;

	@Override
	public List<AutoScheduleInfo> listAll(Date sdate,Date edate) {
		// TODO Auto-generated method stub
		return dao.listAll(sdate,edate);
	}

}
