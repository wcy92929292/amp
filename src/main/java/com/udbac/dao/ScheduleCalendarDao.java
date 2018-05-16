package com.udbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.TbAmpBasicSchedulCalendarInfo;
import com.udbac.entity.TbAmpBasicScheduleInfo;
import com.udbac.model.RefreshCalendarModel;

/**
 * 排期日历Dao
 * @author LFQ
 *
 */
public interface ScheduleCalendarDao {
	
	/**
	 * 插码某个短代码的所有投放时期安排
	 * @param mic
	 * @return
	 */
	TbAmpBasicSchedulCalendarInfo findScheduleCalendarBymic(@Param("mic") String mic);
	
	/**
	 * 查找某个活动下面所有的活动日历
	 * @param actCode
	 * @return
	 */
	List<RefreshCalendarModel> findByActCode(@Param("actCode") String actCode);
	
	/**
	 * 批量插入排期日历
	 * @param calendars
	 */
	void insertCalendar(@Param("calendars") List<TbAmpBasicSchedulCalendarInfo> calendars);
	
	/**
	 * 删除排期日历
	 * @param schedules
	 */
	void deleteCalendars(@Param("schedules") List<TbAmpBasicScheduleInfo> schedules);
	
	/**
	 * 更新排期点位更新时间，以同步到旧平台中
	 * @param schedules
	 */
	void updateTime(@Param("schedules") List<TbAmpBasicScheduleInfo> schedules);

}
