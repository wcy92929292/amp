package com.udbac.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.RegionDataExport;
import com.udbac.entity.RegionSum;
import com.udbac.entity.RegionSum2;

/**
 * 地域及频次Dao
 * 
 * @author HAN
 *
 */
public interface RegionReportDao {

	/**
	 * 周
	 * 
	 * @param stratDate  地域及频次起始时间
	 * @param endDate  地域及频次结束时间
	 * @param actCode  活动编号
	 * @param customerName  客户名称
	 * @return
	 */
	List<RegionDataExport> listRegionInfo(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("actCode") String actCode, @Param("cusName") String cusName,@Param("customerName") String customerName);

	/**
	 * 月
	 * 
	 * @param monthStartDate
	 * @param monthEndDate
	 * @param actCode
	 * @param customerName
	 * @return
	 */
	List<RegionDataExport> listRegionMonthInfo(@Param("monthStartDate") Date monthStartDate,
			@Param("monthEndDate") Date monthEndDate, @Param("actCode") String actCode,
			@Param("cusName") String cusName,@Param("customerName") String customerName);

	/**
	 * 周总曝光点击数
	 * @param startDate
	 * @param endDate
	 * @param actCode
	 * @param customerName
	 * @return
	 */
	List<RegionSum> listSumRegionInfo(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("actCode") String actCode, @Param("cusName") String cusName,@Param("customerName") String customerName);

	/**
	 * 月周总曝光点击数
	 * @param startDate
	 * @param endDate
	 * @param actCode
	 * @param customerName
	 * @return
	 */
	List<RegionSum> listSumMonthRegionInfo(@Param("monthStartDate") Date monthStartDate,
			@Param("monthEndDate") Date monthEndDate, @Param("actCode") String actCode,
			@Param("cusName") String cusName,@Param("customerName") String customerName);

	/**
	 * 得到所有的省份
	 */
	public List<RegionDataExport> getAllProvice();

	/**
	 * 累计
	 */
	List<RegionDataExport> listRegionInfo2(@Param("eDate") Date eDate, 
			@Param("actCode") String actCode, @Param("cusName") String cusName,@Param("customerName") String customerName);
	/**
	 * 累计总
	 */
	List<RegionSum2> listSumRegionInfo2(@Param("eDate") Date eDate, 
			@Param("actCode") String actCode, @Param("cusName") String cusName,@Param("customerName") String customerName);
/**
 * 不累计地市
 * @param startDate
 * @param endDateCity
 * @param actCode
 * @param cusName
 * @param customerName
 * @return
 */
	List<RegionDataExport> listRegionInfoCity(@Param("startDate")Date startDate, @Param("endDateCity")Date endDateCity, 
			@Param("actCode")String actCode, @Param("cusName")String cusName,@Param("customerName")String customerName);
/**
 * 累计地市的
 * @param eDate
 * @param actCode
 * @param cusName
 * @param customerName
 * @return
 */
List<RegionDataExport> listRegionInfo2City(@Param("eDate")Date eDate, @Param("actCode")String actCode, 
		@Param("cusName")String cusName, @Param("customerName")String customerName);
}
