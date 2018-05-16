package com.udbac.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.DataExport;
import com.udbac.entity.TbAmpBasicScheduleInfo;

/**
 * 报告导出
 * 2016-05-20
 * @author han
 *
 */
public interface DataExportDao {

	/**
	 * 日数据导出
	 * @param date  选中的日期
	 * @param actName
	 * @param actCode
	 * @param customer_id //投放单位
	 * @param dt1 
	 * @param isHeBing 
	 * @return
	 */
	List<DataExport> listInfo(@Param("date") Date date,@Param("actName") String actName,@Param("actCode") String actCode,@Param("customer_id") String customer_id,@Param("dt1") Date dt1);
	/**
	 * 当前客户的省级信息
	 * @return
	 */
	List queryCustomer(@Param("province")String province);
	
	Integer sumDay(@Param("customer_id") String customer_id,@Param("date")Date date, @Param("actName")String actName, @Param("actCode")String actCode, @Param("dt1")Date dt1);
	
	List<DataExport> listInfos(@Param("date") Date date, @Param("actName") String actName,@Param("actCode") String actCode,@Param("customer_id")String customer_id, @Param("dte") Date dte);
	
	Integer sumDays(@Param("customer_id")String customer_id,@Param("date") String dt,@Param("actName")String actName,@Param("actCode") String actCode);
	
	ArrayList<DataExport> listInfos(@Param("customer_id")String customer_id,@Param("date") String dt,@Param("actName")String actName,@Param("actCode") String actCode);
	
	Integer sumHour(@Param("customer_id") String customer_id,@Param("date")Date date, @Param("actName")String actName, @Param("actCode")String actCode, @Param("dt1")Date dt1,@Param("mic") String mic);
	
	List<DataExport> listHourInfo(@Param("date") Date date,@Param("actName") String actName,@Param("actCode") String actCode,@Param("customer_id") String customer_id,@Param("dt1") Date dt1,@Param("mic") String mic);
	
	Integer checkCode(@Param("actCode") String actCode);
	Integer sumPZ(@Param("actCode") String actCode,@Param("dt") Date dt, @Param("dte")Date dte,@Param("flag") String flag);
	
	String selectProName(@Param("actCode") String actCode);
	
	String minDay(@Param("actCode") String actCode);
	
	String maxDay(@Param("actCode") String actCode);
	
	ArrayList<DataExport> listInfoPZ(@Param("actCode") String actCode,@Param("dt") Date dt, @Param("dte")Date dte,@Param("flag") String flag);
	
	ArrayList<DataExport> listPZDay(@Param("actCode") String actCode,@Param("dt") Date dt, @Param("dte")Date dte,@Param("flag") String flag, @Param("export_times")Integer export_times);
	
	void saveEndPoints(@Param("dt")Date dt,@Param("mailbox") String mailbox,@Param("mic") String mic);
	
	Integer checkMic(@Param("mic") String mic);
	
	void updateDataCaliber(@Param("list") List<TbAmpBasicScheduleInfo> list);
	
	
	
	
}
