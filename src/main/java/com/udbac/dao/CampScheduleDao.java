package com.udbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.CampBasicActivityInfo;
import com.udbac.entity.CampBasicSchedulCalendarInfo;
import com.udbac.entity.CampScheduleInfo;
import com.udbac.entity.TbAmpBasicMediaInfo;
import com.udbac.entity.TbAmpBasicScheduleInfoBackup;

/**
 * 活动排期信息
 * 
 * @author han
 *
 */
public interface CampScheduleDao {

	/**
	 * 根据活动排期概览页的编号查找排期信息
	 * 
	 * @param actCode
	 * @return
	 */
	public List<CampScheduleInfo> listAll(@Param("actCode") String actCode);

	List<CampScheduleInfo> findScheduleByActCode(@Param("actCode") String actCode);

	CampBasicActivityInfo findByCode(@Param("actCode") String actCode);

	TbAmpBasicMediaInfo findMediaById(@Param("mediaId") Integer mediaId);

	CampBasicSchedulCalendarInfo findScheduleCalendarBymic(@Param("mic") String mic);

	void scheduleBackup(@Param("scheduleBackups") List<TbAmpBasicScheduleInfoBackup> scheduleBackups);
	
	void updateClickAvg(@Param("clickAvg") String clickAvg,@Param("mic") String mic);
	
	void updateExposureAvg(@Param("exposureAvg") String exposureAvg,@Param("mic") String mic);
}
