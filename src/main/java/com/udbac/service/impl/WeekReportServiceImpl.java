package com.udbac.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.udbac.dao.WeekReportDao;
import com.udbac.entity.SHWeekDataExport;
import com.udbac.entity.WeekDataExport;
import com.udbac.service.WeekReportService;

/**
 * 周报Service
 * 
 * @author HAN
 */
@Service
public class WeekReportServiceImpl implements WeekReportService {

	@Autowired
	WeekReportDao dao;

	@Override
	public List<WeekDataExport> listInfo(Date stratDate, Date endDate, String actCode, String cusCode,String baiduRemove) {
		// TODO Auto-generated method stub
		return dao.listInfo(stratDate, endDate, actCode, cusCode,baiduRemove);
	}
	
	@Override
	public List<WeekDataExport> listWeekInfo(Date stratDate, Date endDate, String actCode, String cusCode,String baiduRemove) {
		// TODO Auto-generated method stub
		return dao.listWeekInfo(stratDate, endDate, actCode, cusCode,baiduRemove);
	}

	@Override
	public List<WeekDataExport> listWeekSumInfo(Date stratDate, Date endDate, String actCode, String cusCode,String baiduRemove) {
		// TODO Auto-generated method stub
		return dao.listWeekSumInfo(stratDate, endDate, actCode, cusCode, baiduRemove);
	}

	@Override
	public List<SHWeekDataExport> listSHWeekInfo(Date stratDate, Date endDate, String actCode, String cusCode) {
		// TODO Auto-generated method stub
		return dao.listSHWeekInfo(stratDate, endDate, actCode, cusCode);
	}

	@Override
	public List<SHWeekDataExport> listSHSumWeekInfo(Date stratDate, Date endDate, String actCode, String cusCode) {
		// TODO Auto-generated method stub
		return dao.listSHSumWeekInfo(stratDate, endDate, actCode, cusCode);
	}

}