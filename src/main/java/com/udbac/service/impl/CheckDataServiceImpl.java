package com.udbac.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.dao.CheckDataDao;
import com.udbac.exception.ScheduleException;
import com.udbac.model.CheckDataModel;
import com.udbac.service.CheckDataService;

@Service
public class CheckDataServiceImpl implements CheckDataService{

	@Autowired
	private CheckDataDao checkDataDao;
	
	@Override
	public List<CheckDataModel> checkHourAndDay(String dateStr) throws ScheduleException {
		StringBuffer sb = new StringBuffer(10).append(dateStr).append(" 235959");
		
		Integer count = checkDataDao.countHour(dateStr,sb.toString());
		if(count == 0){
			throw new ScheduleException(" hour data is none!");
		}
		
		count = checkDataDao.countDay(dateStr);
		if(count == 0){
			throw new ScheduleException(" day data is none!");
		}
		
		return checkDataDao.hourAndDay(dateStr, sb.toString());
	}

}
