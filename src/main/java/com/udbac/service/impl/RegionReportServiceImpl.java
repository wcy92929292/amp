package com.udbac.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.dao.RegionReportDao;
import com.udbac.entity.RegionDataExport;
import com.udbac.entity.RegionSum;
import com.udbac.entity.RegionSum2;
import com.udbac.service.RegionReportService;

/**
 * 地域频次Service
 * 
 * @author HAN
 */
@Service
public class RegionReportServiceImpl implements RegionReportService {

	@Autowired
	RegionReportDao dao;

	/**
	 * 周
	 */
	@Override
	public List<RegionDataExport> listRegionInfo(Date stratDate, Date endDate, String actCode, String cusName,String customerName) {
		List<RegionDataExport> info = dao.listRegionInfo(stratDate, endDate, actCode,cusName, customerName);
		return info;
	}

	/**
	 * 月
	 */
	@Override
	public List<RegionDataExport> listRegionMonthInfo(Date monthStartDate, Date monthEndDate, String actCode,String cusName,String customerName) {
		List<RegionDataExport> monthInfo = dao.listRegionMonthInfo(monthStartDate, monthEndDate, actCode,cusName,customerName);
		return monthInfo;
	}

	/**
	 * 所有省份
	 */
	@Override
	public List<RegionDataExport> getAllProvice() {
		return dao.getAllProvice();
	}

	/**
	 * 周曝光点击总
	 */
	@Override
	public List<RegionSum> listSumRegionInfo(Date startDate, Date endDate, String actCode, String cusName,String customerName) {
		// TODO Auto-generated method stub
		return dao.listSumRegionInfo(startDate, endDate, actCode,cusName, customerName);
	}

	/**
	 * 月曝光点击总
	 */
	@Override
	public List<RegionSum> listSumMonthRegionInfo(Date monthStartDate, Date monthEndDate, String actCode, String cusName,String customerName) {
		// TODO Auto-generated method stub
		return dao.listSumMonthRegionInfo(monthStartDate, monthEndDate, actCode, cusName,customerName);
	}


	/**
	 * 累计
	 */
	@Override
	public List<RegionDataExport> listRegionInfo2(Date eDate, String actCode, String cusName,String customerName) {

		return dao.listRegionInfo2(eDate, actCode,cusName, customerName);
	}

	/**
	 * 累计总数
	 */
	@Override
	public List<RegionSum2> listSumRegionInfo2(Date eDate, String actCode, String cusName,String customerName) {

		return dao.listSumRegionInfo2(eDate, actCode, cusName,customerName);
	}

	@Override
	public List<RegionDataExport> listRegionInfoCity(Date startDate, Date endDateCity, String actCode, String cusName,
			String customerName) {
		List<RegionDataExport> info = dao.listRegionInfoCity(startDate, endDateCity, actCode,cusName, customerName);
		return info;
	}

	@Override
	public List<RegionDataExport> listRegionInfo2City(Date eDate, String actCode, String cusName, String customerName) {
		// TODO Auto-generated method stub
		List<RegionDataExport> info = dao.listRegionInfo2City(eDate, actCode,cusName, customerName);
		return info;
	}



}