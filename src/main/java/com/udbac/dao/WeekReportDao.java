package com.udbac.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.SHWeekDataExport;
import com.udbac.entity.WeekDataExport;

/**
 * 周报Dao
 * @author HAN
 *
 */
public interface WeekReportDao {

	/**
	 * 明细数据
	 * @param stratDate 周报起始时间
	 * @param endDate 周报结束时间
	 * @param actCode 活动编号
	 * @param customerName 客户名称
	 * @return
	 */
	List<WeekDataExport> listInfo(@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("actCode") String actCode,
			@Param("cusCode") String cusCode,@Param("mediaName") String mediaName);
	
	/**
	 * 明细数据
	 * @param stratDate 周报起始时间
	 * @param endDate 周报结束时间
	 * @param actCode 活动编号
	 * @param customerName 客户名称
	 * @return
	 */
	List<WeekDataExport> listWeekInfo(@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("actCode") String actCode,
			@Param("cusCode") String cusCode,@Param("mediaName") String mediaName);
	
	/**
	 * 汇总数据
	 * @param startDate
	 * @param endDate
	 * @param actCode
	 * @param customerName
	 * @return
	 */
	List<WeekDataExport> listWeekSumInfo(@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("actCode") String actCode,
			@Param("cusCode") String cusCode,@Param("mediaName") String mediaName);
	
	/**
	 * 上海周报数据
	 * @param stratDate 周报起始时间
	 * @param endDate 周报结束时间
	 * @param actCode 活动编号
	 * @param customerName 客户名称
	 * @return
	 */
	List<SHWeekDataExport> listSHWeekInfo(@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("actCode") String actCode,@Param("cusCode") String cusCode);
	
	/**
	 * 上海周报数据媒体汇总
	 * @param stratDate 周报起始时间
	 * @param endDate 周报结束时间
	 * @param actCode 活动编号
	 * @param customerName 客户名称
	 * @return
	 */
	List<SHWeekDataExport> listSHSumWeekInfo(@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("actCode") String actCode,@Param("cusCode") String cusCode);
	
	
	
	
}
