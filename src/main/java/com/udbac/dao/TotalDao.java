package com.udbac.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.udbac.entity.AddMonitorPlanBean;
import com.udbac.entity.WeekDataExport;
import com.udbac.model.MonthReportModel;

public interface TotalDao {
	/**
	 * 
	 * @param dt:为截止日期
	 * @param dt2：为截止日期往前推7天
	 * @param customer_id：为投放单位
	 * @param actCode：为活动编号
	 * @return
	 */
	List<WeekDataExport> listTotalInfo(@Param("dt") Date dt,@Param("dt1") Date dt1, @Param("customer_id") String customer_id,
			@Param("actCode") String actCode);

	List<WeekDataExport> listHuiZongInfo(@Param("dt") Date dt,@Param("dt1")  Date dt1, @Param("customer_id") String customer_id,
			@Param("actCode") String actCode);

	Integer sumDay(@Param("dt") Date dt,@Param("dt1") Date dt1, @Param("customer_id") String customer_id,
			@Param("actCode") String actCode);
	//查询的是月报的累计
    List<MonthReportModel> listMonth1(@Param("customer")String customer,@Param("month") Date dateStr, @Param("actCode") String actCode,@Param("dayIf")Integer dayIf); 
//	List<MonthReportModel> listMonthBaidu1(@Param("customer")String customer,@Param("month") Date dateStr, @Param("actCode") String actCode,@Param("dayIf")Integer dayIf);

	List<MonthReportModel> listSumMic(@Param("customer")String customer,@Param("month") Date dateStr, @Param("actCode") String actCode);

	List<WeekDataExport> listMediaInfo(@Param("dt") Date dt, @Param("customer_id") String customer_id,
			@Param("actCode") String actCode);
	
	
	Double getGroupId(@Param("customer")String customer,@Param("actCode") String actCode, @Param("dateStr")Date dateStr);

	List<MonthReportModel> listMonthNotMic(@Param("customer")String customer,@Param("month") Date dateStr, @Param("actCode") String actCode);

	List<MonthReportModel> listRegionSum(@Param("mic")String pointNames,@Param("month") Date dateStr);

//	/**
//	 * 查询月报数据
//	 * @return
//	 */
//	List<MonthReportModel> listMonth(@Param("customer")String customer,@Param("smonth") Date smonth,
//			@Param("emonth") Date emonth,@Param("monthType") String monthType,@Param("monthFile") Integer monthFile);
////	List<MonthReportModel> listMonth(@Param("customer")String customer,@Param("month") String month);
//	
////	List<MonthReportModel> listMonthBaidu(@Param("customer")String customer,@Param("month") String month);
//	
//	
//	/**
//	 * 查询最后的汇总sheet
//	 * @param customer
//	 * @param month
//	 * @return
//	 */
////	List<MonthReportModel> listSumMic(@Param("customer")String customer,@Param("month") String month);
//	List<MonthReportModel> listSumMic(@Param("customer")String customer,@Param("smonth") Date smonth,
//			@Param("emonth") Date emonth,@Param("monthType") String monthType);
//	
//	/**
//	 * 查询上海月报汇总数据
//	 * @param month
//	 * @return
//	 */
//	List<MonthReportModel> listShanghai(@Param("month") String month);

}
