package com.udbac.service;

import java.util.Date;
import java.util.List;

import com.udbac.entity.RegionDataExport;
import com.udbac.entity.RegionSum;
import com.udbac.entity.RegionSum2;
public interface RegionReportService {

	/**
	 * 周
	 * @param stratDate 地域及频次起始时间
	 * @param endDate 地域及频次结束时间
	 * @param actCode 活动编号
	 * @param customerName 客户名称
	 * @return
	 * throws DataAccessException 捕捉数据库连接异常
	 */
	List<RegionDataExport> listRegionInfo(Date stratDate,Date endDate,String actCode,String cusName,String customerName);
	/**
	 * 月
	 * @param monthStartDate
	 * @param endDate
	 * @param actCode
	 * @param customerName
	 * @return
	 */
	List<RegionDataExport> listRegionMonthInfo(Date monthStartDate,Date monthEndDate,String actCode,String cusName,String customerName);
	
	/**
	 * 周曝光点击总数
	 * @param startDate
	 * @param endDate
	 * @param actCode
	 * @param customerName
	 * @return
	 */
	List<RegionSum> listSumRegionInfo(Date startDate,Date endDate,String actCode,String cusName,String customerName);
	/**
	 * 月曝光点击总数
	 * @param startDate
	 * @param endDate
	 * @param actCode
	 * @param customerName
	 * @return
	 */
	List<RegionSum> listSumMonthRegionInfo(Date monthStartDate,Date monthEndDate,String actCode,String cusName,String customerName);
	
	/**
	 * 得到所有的省份
	 * @return 
	 */
	public List<RegionDataExport> getAllProvice();
	/**
	 * 累计
	 * @param eDate 截止时间
	 * @param actCode 活动编号
	 * @param customerName 客户名称
	 * @return 
	 */
	List<RegionDataExport> listRegionInfo2(Date eDate,String actCode,String cusName,String customerName);
	/**
	 * 累计总数
	 * @param eDate 截止时间
	 * @param actCode 活动编号
	 * @param customerName 客户名称
	 * @return 
	 */
	List<RegionSum2> listSumRegionInfo2(Date eDate,String actCode,String cusName,String customerName);
	/**
	 * 不累计地市的
	 * @param startDate
	 * @param endDateCity
	 * @param actCode
	 * @param cusName
	 * @param customerName
	 * @return
	 */
	List<RegionDataExport> listRegionInfoCity(Date startDate, Date endDateCity, String actCode, String cusName,
			String customerName);
	/**
	 * 累计地市的
	 * @param eDate
	 * @param actCode
	 * @param cusName
	 * @param customerName
	 * @return
	 */
	List<RegionDataExport> listRegionInfo2City(Date eDate, String actCode, String cusName, String customerName);
	
}
