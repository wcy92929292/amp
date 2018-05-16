package com.udbac.service;

/**
 * 自然月结报告，日报的维度报告
 */
public interface MonthDayReportService extends MonthReportService{
	
//	public String monthReportList(String cusCode, String dateStr,String monthType,String monthLocation,Integer );
	
	String CLICK_COLUMN = "M";
	String EXP_COLUMN = "J";
	String CLICK_UV_COLUMN = "N";
	String EXP_UV_COLUMN = "K";
	String CLICK_CTR_COLUMN = "O";
	String EXP_FINISH_CTR_COLUMN = "W";
	String CLICK_FINISH_CTR_COLUMN = "X";
	String BOUNCE_TIMES_COLUMN = "T";
	String VISIT_COLUMN = "P";
	String BOUNCE_RATE_COLUMN = "U";
	String VIEW_TIME_COLUMN = "V";
	String CLICK_AVG_COLUMN = "L";
	String EXP_AVG_COLUMN = "I";


}
