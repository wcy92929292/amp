package com.udbac.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.model.MonthReportModel;

/**
 * 月报Dao
 * @author LFQ
 *
 */
public interface MonthReportDao {

	/**
	 * 查询月报数据
	 * @return
	 */
	List<MonthReportModel> listMonth(@Param("customer")String customer,@Param("smonth") Date smonth,
			@Param("emonth") Date emonth,@Param("monthType") String monthType,@Param("monthFile") Integer monthFile);
//	List<MonthReportModel> listMonth(@Param("customer")String customer,@Param("month") String month);
	
//	List<MonthReportModel> listMonthBaidu(@Param("customer")String customer,@Param("month") String month);
	
	
	/**
	 * 查询最后的汇总sheet
	 * @param customer
	 * @param month
	 * @return
	 */
//	List<MonthReportModel> listSumMic(@Param("customer")String customer,@Param("month") String month);
	List<MonthReportModel> listSumMic(@Param("customer")String customer,@Param("smonth") Date smonth,
			@Param("emonth") Date emonth,@Param("monthType") String monthType);
	
	/**
	 * 查询上海月报汇总数据
	 * @param month
	 * @return
	 */
	List<MonthReportModel> listShanghai(@Param("month") String month);
	
}
