package com.udbac.service;

public interface MonthReportService {

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
	
	String monthReportList(String userName,String cusCode,String dateStr,String monthType,String monthLocation,Integer month_media,Integer monthSummaryData);
}
