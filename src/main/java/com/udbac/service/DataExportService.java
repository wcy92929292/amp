package com.udbac.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.udbac.entity.DataExport;
import com.udbac.entity.TbAmpBasicScheduleInfo;

public interface DataExportService {

	List<DataExport> listInfo(Date date,String actName,String actCode, String customer_id, Date dte);


	List queryCustomer(String province);

	Integer sumDay(String customer_id,Date dt, String actName, String actCode, Date dte);


	Integer sumDays(String customer_id, String dt, String actName, String actCode);


	ArrayList<DataExport> listInfos(String dt, String actName, String actCode,
			String customer_id);


	Integer sumHour(String customer_id, Date dt, String actName,
			String actCode, Date dte, String mic);


	List<DataExport> listHourInfo(Date dt, String actName, String actCode,
			String customer_id, Date dte, String mic);


	Integer checkCode(String actCode);


	Integer sumPZ(String actCode, Date dt, Date dte);


	String selectProName(String actCode);


	String selectRealPer(String actCode);


	ArrayList<DataExport> listInfoPZ(String actCode, Date dt, Date dte);


	ArrayList<DataExport> listPZDay(String trim, Date dt, Date dte, Integer num);


	String saveEndPoints(Date dt, String mailbox, String mic);


	Integer checkMic(String mic);
	
	
	/**
	 * 曝光口径和点击口径更新
	 * 
	 * @param list
	 */
	void updateDataCaliber(List<TbAmpBasicScheduleInfo> list);
	

	

}
