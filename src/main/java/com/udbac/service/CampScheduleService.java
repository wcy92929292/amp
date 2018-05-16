package com.udbac.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.CampBasicActivityInfo;
import com.udbac.entity.CampScheduleInfo;
import com.udbac.entity.TbAmpBasicActivityInfo;
import com.udbac.entity.TbAmpBasicMediaInfo;
import com.udbac.entity.TbAmpBasicScheduleInfo;

public interface CampScheduleService {

	/**
	 * 根据活动排期概览页的编号查找排期信息
	 * 
	 * @param actCode
	 * @return
	 */
	public List<CampScheduleInfo> listAll(String actCode);

	public List<CampScheduleInfo> listSche(String actCode);

	/**
	 * 根据活动编号下载排期
	 * 
	 * @param activityCode
	 * @return
	 */
	CampBasicActivityInfo downloadSchedule(String activityCode);
	
	void updateClickAvg( String clickAvg,String mic);
	
	void updateExposureAvg( String exposureAvg,String mic);

}
