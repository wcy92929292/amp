package com.udbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.TbAmpBasicScheduleInfo;

/**
 * 排期Dao
 * @author LFQ
 * @DATE 2016-04-14
 */
public interface ScheduleDao {

	/**
	 * 根据活动编号查找排期
	 * @param actCode
	 * @return
	 */
	List<TbAmpBasicScheduleInfo> findScheduleByActCode(@Param("actCode") String actCode);
	
	/**
	 * 根据活动编号以及短代码查找排期信息
	 * @param actCode
	 * @param mics
	 * @return
	 */
	List<TbAmpBasicScheduleInfo> findByMicAndActCode(@Param("actCode") String actCode,@Param("mics") String[] mics);
	
	/**
	 * 批量插入排期
	 * @param schedules
	 */
	void insertSchedules(@Param("schedules") List<TbAmpBasicScheduleInfo> schedules);
	
	/**
	 * 批量导入排期扩展信息
	 * @param scheduleExtens
	 */
	void insertScheduleExtens(@Param("scheduleExtens") List<TbAmpBasicScheduleInfo> schedules);
	
	/**
	 * 删除排期扩展信息
	 * @param schedules
	 */
	void deleteExtendByActCode(@Param("actCode") String actCode);
	
	/**
	 * 根据活动编号删除排期
	 * @param schedules
	 */
	void deleteScheduleByActCode(@Param("actCode") String actCode);
	
	/**
	 * 同步跳转表
	 * 		mic,url,supportClick,supportExposure
	 * @return
	 */
	List<TbAmpBasicScheduleInfo> micAndUrl();
	
	/**
	 * 更改排期点击预估，曝光预估
	 * @param schedules
	 */
	void updateClickAndExposureAvg(@Param("schedules") List<TbAmpBasicScheduleInfo> schedules);
	
	/**
	 * 更改排期顺序编号
	 * @param schedules
	 */
	void updateMicNo(@Param("schedules") List<TbAmpBasicScheduleInfo> schedules);;
	
}
