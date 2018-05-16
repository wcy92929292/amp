package com.udbac.service;

import java.util.Date;
import java.util.List;

import com.udbac.entity.WeekDataExport;

public interface TotalService {
	String NA = "N/A";
	String DIV0 = "#Div/0!";
	String THOUNT = "#,##0";	//千分位
	String HUN_POINT = "0.00%";	//两位百分比
	String TWO_POINT = "0.00";	//两位小数
	String SONG_TI = "宋体";
	String CALIBRI = "Calibri"; 
	
	int[] BLUE = {55,96,145};	//蓝色
	int[] WHITE = {255,255,255};	//白色
	
	short FONT_9 = 9;
	List<WeekDataExport> listTotalInfo(Date dt, String customer_id,
			String actCode);

	List<WeekDataExport> listHuiZongInfo(Date dt, String customer_id,
			String actCode);

	Integer sumDay(Date dt,  String customer_id,String actCode);

	String monthReportList(String month_customer, Date dt, String realName, String actCode,Integer dayIf,Integer isRegion);

	List<WeekDataExport> listMediaInfo(Date dt, String customer_id,
			String actCode);


}
