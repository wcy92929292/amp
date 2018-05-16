package com.udbac.service;

import java.util.Date;
import java.util.List;

import com.udbac.entity.SHWeekDataExport;
import com.udbac.entity.WeekDataExport;

public interface WeekReportService {

	/**
	 * 明细数据
	 * @param stratDate 周起始时间
	 * @param endDate 周结束时间
	 * @param actCode 活动编号
	 * @param customerName 客户名称
	 * @return
	 */
	List<WeekDataExport> listInfo(Date stratDate,Date endDate,String actCode,String cusCode,String baiduRemove);
	
	/**
	 * 明细数据
	 * @param stratDate 周起始时间
	 * @param endDate 周结束时间
	 * @param actCode 活动编号
	 * @param customerName 客户名称
	 * @return
	 */
	List<WeekDataExport> listWeekInfo(Date stratDate,Date endDate,String actCode,String cusCode,String baiduRemove);
	
	/**
	 * 汇总数据
	 * @param stratDate
	 * @param endDate
	 * @param actCode
	 * @param customerName
	 * @return
	 */
	List<WeekDataExport> listWeekSumInfo(Date stratDate,Date endDate,String actCode,String cusCode,String baiduRemove);
	
	/**
	 * 上海周报数据
	 * @param stratDate 周起始时间
	 * @param endDate 周结束时间
	 * @param actCode 活动编号
	 * @param customerName 客户名称
	 * @return
	 */
	List<SHWeekDataExport> listSHWeekInfo(Date stratDate,Date endDate,String actCode,String cusCode);
	
	/**
	 * 上海周报数据媒体汇总
	 * @param stratDate 周起始时间
	 * @param endDate 周结束时间
	 * @param actCode 活动编号
	 * @param customerName 客户名称
	 * @return
	 */
	List<SHWeekDataExport> listSHSumWeekInfo(Date stratDate,Date endDate,String actCode,String cusCode);
	
	
	
}
