package com.udbac.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.controller.MonthReportController;
import com.udbac.dao.MonthReportDao;
import com.udbac.model.MonthReportModel;
import com.udbac.service.MonthReportService;
import com.udbac.util.DateUtil;
import com.udbac.util.FilePathManager;
import com.udbac.util.XlsxUtil;

/**
 * 月报Service
 * @author LFQ
 */
public class MonthReportServiceImpl implements MonthReportService{

	protected XlsxUtil xlsxUtil = null;
	
	@Autowired
	protected FilePathManager fpm;
	
	@Autowired
	protected MonthReportDao monthReportDao;
	
	protected Class<MonthReportModel> modelClazz = MonthReportModel.class; 
	
	//用于存储Excel中用到的XSSFCellStyle
	protected Map<String,XSSFCellStyle> cellStyles = new HashMap<>();
	//用于存储Excel中用到的XSSFCellStyle
	protected Map<String,XSSFCellStyle> sumCellStyles = new HashMap<>();
	
	public void newFile(String path){
		try {
			//删除以前的文件
			File file = new File(path);
			if(file.exists()){
				file.delete();
			}
			
			this.xlsxUtil = new XlsxUtil(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//end newFile
	
	/**
	 * 初始化单元格样式
	 */
	public void initCellStyle(){
		
		//项目名称	 媒体名称	广告位 投放形式
		XSSFCellStyle songti = xlsxUtil.createCellStyle(null, null, XlsxUtil.FONT_NAME_SONGTI, (short)9, null, null, 
				XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
				XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
		
		cellStyles.put("songti", songti);
		
		//营销识别码	日期   URL
		XSSFCellStyle calibri = xlsxUtil.createCellStyle(null, null, XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
				XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
				XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
		
		cellStyles.put("calibri", calibri);
		
		//曝光预估	曝光次数	曝光人数	点击预估	点击次数	点击人数
		//访问次数	访问人数	到站点击	浏览量	跳出次数
		XSSFCellStyle number = xlsxUtil.createCellStyle(null, XlsxUtil.NUMBER_FORMAT_THOUSANDS, 
				XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
				XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
				XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
		
		cellStyles.put("number", number);
		
		//点击率CTR 跳出率   曝光完成率	点击完成率
		XSSFCellStyle percent = xlsxUtil.createCellStyle(null, XlsxUtil.NUMBER_FORMAT_PERCENT, 
				XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
				XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
				XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
		
		cellStyles.put("percent", percent);
		
		//平均访问时长(s)
		XSSFCellStyle decimails = xlsxUtil.createCellStyle(null, XlsxUtil.NUMBER_FORMAT_TWO_DECIMALS, 
				XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
				XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
				XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
		
		cellStyles.put("decimails", decimails);
		
		int blue[] = new int[]{197,217,241};
		//媒体汇总  ，项目汇总
		songti = xlsxUtil.createCellStyle(blue, null, XlsxUtil.FONT_NAME_SONGTI, (short)9, null, null, 
				XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
				XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
		
		sumCellStyles.put("songti", songti);
		sumCellStyles.put("calibri", songti);
		
		//汇总
		//曝光预估	曝光次数	曝光人数	点击预估	点击次数	点击人数
		//访问次数	访问人数	到站点击	浏览量	跳出次数
		number = xlsxUtil.createCellStyle(blue, XlsxUtil.NUMBER_FORMAT_THOUSANDS, 
				XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
				XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
				XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
				
		sumCellStyles.put("number", number);
		
		//点击率CTR 跳出率   曝光完成率	点击完成率
		percent = xlsxUtil.createCellStyle(blue, XlsxUtil.NUMBER_FORMAT_PERCENT, 
			XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
			XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
			XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
		
		sumCellStyles.put("percent", percent);
		
		//平均访问时长(s)
		decimails = xlsxUtil.createCellStyle(blue, XlsxUtil.NUMBER_FORMAT_TWO_DECIMALS, 
			XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
			XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
			XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
		
		sumCellStyles.put("decimails", decimails);
		
	}//end initCellStyle
	
	
	/**
	 * 报告处理
	 * @param cusCode
	 * @param dateStr
	 */
	@Override
	public String monthReportList(String userName,String cusCode,String dateStr,String monthType,String monthLocation,Integer monthFile,Integer monthSummaryData){
		
		List<MonthReportModel> monthList = new LinkedList<>();
		String[] sdateAndEdate = getSdateAndEdate(dateStr);
		Date sdate,edate;
		sdate = DateUtil.getDate(sdateAndEdate[0], null);
		edate = DateUtil.getDate(sdateAndEdate[1], null);
		String month = dateStr.substring(0,dateStr.lastIndexOf("-"));
		String path = "";
		
		//上海月报
		if("SH".equals(cusCode)){
			monthList = monthReportDao.listShanghai(month);
			path = getMonthReportPath(monthList, dateStr);
			
			newFile(path);
			initCellStyle();
			shanghaiMonthReport(monthList);
		}
		else{
			//普通月结报告
			monthList = monthReportDao.listMonth(cusCode,sdate,edate,monthType,monthFile);
//			monthList.addAll(monthReportDao.listMonthBaidu(cusCode,month));
			if(monthList == null || monthList.size() == 0){
				return "0";
			}
			
			path = getMonthReportPath(monthList, dateStr);
			newFile(path);
			initCellStyle();
			
			//将数据写入Excel文件
			int sheetNum = writeMonthDataToXlsx(monthList);
			
			//最后一页汇总信息
			monthList = monthReportDao.listSumMic(cusCode,sdate,edate,monthType);
			initLastSheet(sheetNum, monthList.get(0).getCustomerName(), month);
			sumLastSheetData(monthList);
		}
		
		xlsxUtil.close(null);
		
		return path;
		
	}//end monthReportList();
	
	/**
	 * 获取某天所在月的最大以及最小日期
	 * @param dateStr
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected String[] getSdateAndEdate(String dateStr){
		String[] sdateAndEdate = {"",""};
		Date date = DateUtil.getDate(dateStr, null);
		int month = date.getMonth() + 1;
		int day = DateUtil.getMonthLastDate(date);
		String monthStr = date.getYear() + 1900 + "-" + (month < 10 ? "0"+ month : month);
		
		sdateAndEdate[0] = monthStr + "-01";
		sdateAndEdate[1] = monthStr + "-" + (day < 10 ? "0"+ day : day);
		return sdateAndEdate;
	}//end getSdateAndEdate
	
	/**
	 * 将监测数据写入报告文件
	 */
	//活动名称	投放媒体	投放位置	投放形式	日期	营销识别码	监测曝光PV	监测曝光UV	监测点击PV	监测点击UV	访问次数	访问人数	浏览量	跳出次数	跳出率	平均访问时长(s)
	//点位方法
	private static final String listMonthMethod[] = {"getActivityName","getMediaName","getPointLocation","getPutFunction","getPutDate",
		"getMic","getExposurePV","getExposureUV","getClickPV","getClickUV","getVisit","getVisitor","getPageView","getBounceTimes","getBounceRate","getViewTime"};
	//媒体sheet汇总
	private static final String sumMediaMethods[] = {"getMediaName","","","","","","getSumExposurePV","getSumExposureUV","getSumClickPV","getSumClickUV","getVisit","getVisitor","getPageView","getBounceTimes","getBounceRate","getViewTime"};
	private static final String sumPointMethods[] = {"getPointLocation","","","","","","getSumExposurePV","getSumExposureUV","getSumClickPV","getSumClickUV","getVisit","getVisitor","getPageView","getBounceTimes","getBounceRate","getViewTime"};
	
	public int writeMonthDataToXlsx(List<MonthReportModel> monthList){
		
		int startXy[] = {8,1};		//开始写入数据的位置
		MonthReportModel monthReportModel = null;
		
		int i;
		String mediaName = "";
		String pointName = "";
		String cusName = monthList.get(0).getCustomerName();
		Date startDate = null;
		Date endDate = null;	
		int sheetNum = 0;	//用于记录sheet的编号
		int startRow = 0;	//用于初始化数据插入的位置
		int[] rangeCell = new int[4];
		MonthReportModel sumModel = null;
		List<MonthReportModel> pointReportList = new LinkedList<>();	//用于记录相同投放位置的点位，便于对投放位置的汇总。
		List<MonthReportModel> sheetReportList = new LinkedList<>();	//用于记录相同媒体的点位，便于对其进行汇总
		startRow = startXy[0];
		
		try {
			for (i = 0; i < monthList.size(); i++) {
				
				monthReportModel = monthList.get(i);
				
				//初始化工作簿设置
				if(!mediaName.equals(monthReportModel.getMediaName())){
					
//					//对上一个sheet页进行汇总
					if(sheetNum > 0){
						//将汇总点位点位数据放入媒体汇总List
						sheetReportList.addAll(pointReportList);
						//汇总点位数据
						sumModel = sumPointList(pointReportList, startRow, pointName, rangeCell);
						startRow ++;
						//汇总媒体sheet信息
						sumMediaList(sheetReportList, startRow, mediaName, rangeCell);
						//写上标题日期信息
						writeTitleData(cusName,mediaName,startDate,endDate);
					}
					//初始化媒体名称、点位名称
					mediaName = monthReportModel.getMediaName();
					pointName = monthReportModel.getPutFunction();
					//初始化日期
					startDate = monthReportModel.getPutDate();
					endDate = startDate;
					//初始化工作簿
					xlsxUtil.setSheet(sheetNum ++,mediaName);
					initMonthReport();
					startRow = startXy[0];
				}//end if 初始化工作簿
				
				//分投放位置汇总
				if(!pointName.equals(monthReportModel.getPutFunction())){
					if(pointReportList.size() != 0){
						
						//将汇总点位点位数据放入媒体汇总List
						sheetReportList.addAll(pointReportList);
						//汇总点位数据
						sumModel = sumPointList(pointReportList, startRow, pointName, rangeCell);
						
						startRow++;
					}//end if(pointReportList.size() != 0)
					
					//初始化点位信息
					pointName = monthReportModel.getPutFunction();
				}//end if(!pointName.equals(monthReportModel.getPutFunction()))
				
				//将点位数据汇总到点位汇总List
				pointReportList.add(monthReportModel);
				
				//写入点位明细数据
				writeLineData(startRow, 1,1, monthReportModel, listMonthMethod);
				mediaSheetStyle(startRow, false);
				
				//判断结束日期
				if(endDate.compareTo(monthReportModel.getPutDate()) < 0 ){
					endDate = monthReportModel.getPutDate();
				}
				
				startRow ++;
			}//end for( i < monthList.size())
			
			//将汇总点位点位数据放入媒体汇总List
			sheetReportList.addAll(pointReportList);
			//汇总点位数据
			sumModel = sumPointList(pointReportList, startRow, pointName, rangeCell);
			startRow ++;
			//汇总媒体信息
			sumMediaList(sheetReportList, startRow, mediaName, rangeCell);
			//写上标题日期信息
			writeTitleData(cusName,mediaName,startDate,endDate);
			
		} catch (Exception e) {
			e.printStackTrace();
		}//end try - catch
		
		return sheetNum;
	}//end writeMonthDataToXlsx
	
	/**
	 * 汇总Sheet媒体信息
	 * @param sheetReportList
	 * @param startRow
	 * @param mediaName
	 * @param rangeCell
	 * @return
	 * @throws Exception
	 */
	private MonthReportModel sumMediaList(List<MonthReportModel> sheetReportList,int startRow,String mediaName,int[] rangeCell) throws Exception{
		MonthReportModel sumModel = null;
		//汇总媒体信息
		sumModel = sumPoint(sheetReportList);
		sumModel.setMediaName(mediaName + "总计");
		//写入一行汇总数据
		writeLineData(startRow, 1,sheetReportList.size(), sumModel, sumMediaMethods,setRangeCell(rangeCell,startRow,1,startRow,6));
		mediaSheetStyle(startRow, true);
		
		sheetReportList.clear();
		return sumModel;
	}//end sumMediaList
	
	/**
	 * 汇总点位List
	 * @param pointReportList	目标list
	 * @param startRow			写入的行
	 * @param pointName			点位 名称
	 * @param rangeCell			合并的单元格
	 * @return
	 * @throws Exception
	 */
	private MonthReportModel sumPointList(List<MonthReportModel> pointReportList,int startRow,String pointName,int[] rangeCell) throws Exception{
		MonthReportModel sumModel = null;
		//汇总点位数据
		sumModel = sumPoint(pointReportList);
		sumModel.setPointLocation(pointName + "汇总");
		
		//写入汇总 点位数据
		writeLineData(startRow, 1,pointReportList.size(), sumModel, sumPointMethods,setRangeCell(rangeCell,startRow,1,startRow,6));
		mediaSheetStyle(startRow, true);
//		System.out.println(pointName + "===" + pointReportList.size()+"===" + sumModel.getExposurePV());
		//清空list
		pointReportList.clear();
		return sumModel;
	}//end sumPointList
	
	/**
	 * 通用月数据最后sheet汇总
	 * @param monthList
	 */
	private static String[] lastSheetMethods ={"getActivityName","getMediaName","getPointLocation","getMic","getStartToEndDate","getExposureAvg",
		"getExposurePV","getExposureUV","getClickAvg","getClickPV","getClickUV","getClickRate","getVisit","getVisitor",
		"getClick","getPageView","getBounceTimes","getBounceRate","getViewTime","getExposureFinishRate","getClickFinishRate","getUrl"};
	private static String[] lastSheetMediaMethods ={"getMediaName","","","","","getExposureAvg",
		"getExposurePV","getExposureUV","getClickAvg","getClickPV","getClickUV","getClickRate","getVisit","getVisitor",
		"getClick","getPageView","getBounceTimes","getBounceRate","getViewTime","getExposureFinishRate","getClickFinishRate"};
	private static String[] lastSheetActMethods ={"getActivityName","","","","","getExposureAvg",
		"getExposurePV","getExposureUV","getClickAvg","getClickPV","getClickUV","getClickRate","getVisit","getVisitor",
		"getClick","getPageView","getBounceTimes","getBounceRate","getViewTime","getExposureFinishRate","getClickFinishRate"};
	public void sumLastSheetData(List<MonthReportModel> monthList){
		
		//活动汇总
		List<MonthReportModel> actSumList = new LinkedList<>();
		//媒体汇总
		List<MonthReportModel> mediaSumList = new LinkedList<>();
		
		MonthReportModel monthReportModel = null;
		MonthReportModel sumMediaModel = null;
		MonthReportModel sumActModel = null;
		String mediaName = "";
		String actName = "";
		int[] rangeCell = new int[4];
		
		int i,row = 9,startRow = 9;
		try {
			for (i = 0; i < monthList.size(); i++) {
			
				monthReportModel = monthList.get(i);
				row = i + startRow;

				//初始化媒体汇总信息
				if(!actName.equals(monthReportModel.getActivityName()) || !mediaName.equals(monthReportModel.getMediaName())){
					
					//汇总同一活动，同一媒体的监测数据
					if(i != 0){
						//媒体汇总给活动汇总
						actSumList.addAll(mediaSumList);
						
						sumMediaModel = sumListData(mediaSumList,false);
						sumMediaModel.setMediaName(mediaName + "汇总");
						
						writeLineData(row, 1,mediaSumList.size(), sumMediaModel, lastSheetMediaMethods,setRangeCell(rangeCell,row,1,row,5));
						lastSumSheetStyle(row,true);
						++row;
						startRow ++;
						
						//清空
						mediaSumList.clear();
					}
					
					mediaName = monthReportModel.getMediaName();
					
					//汇总同一活动的监测数据
					if(!actName.equals(monthReportModel.getActivityName()) && i != 0){
						sumActModel = sumListData(actSumList,false);
						sumActModel.setActivityName(actName + "汇总");
						
						writeLineData(row, 1,actSumList.size(), sumActModel, lastSheetActMethods,setRangeCell(rangeCell,row,1,row,5));
						lastSumSheetStyle(row,true);
						++row;
						startRow ++;
						
						//清空
						actSumList.clear();
					}
					actName = monthReportModel.getActivityName();
				}
				
				//写入mic数据
				writeLineData(row, 1,1, monthReportModel, lastSheetMethods);
				lastSumSheetStyle(row,false);
				
				//将点位信息放入媒体汇总List中
				mediaSumList.add(monthReportModel);
			}//end for
			
			//汇总媒体数据
			row ++;
			sumMediaModel = sumListData(mediaSumList,false);
			actSumList.addAll(mediaSumList);
			sumMediaModel.setMediaName(mediaName + "汇总");		
			writeLineData(row, 1,mediaSumList.size(), sumMediaModel, lastSheetMediaMethods,setRangeCell(rangeCell,row,1,row,5));
			lastSumSheetStyle(row,true);
			//汇总活动数据
			row ++;
			sumActModel = sumListData(actSumList,false);
			sumActModel.setActivityName(actName + "汇总");
			writeLineData(row, 1,actSumList.size(), sumActModel, lastSheetActMethods,setRangeCell(rangeCell,row,1,row,5));
			lastSumSheetStyle(row,true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}//end sumLastSheetData
	
	
	private int[] setRangeCell(int[] rangeCell,int firstRow,int firstColumn,int lastRow,int lastColumn){
		if(rangeCell != null && rangeCell.length == 4 ){
			rangeCell[0] = firstRow;
			rangeCell[1] = firstColumn;
			rangeCell[2] = lastRow;
			rangeCell[3] = lastColumn;
		}
		return rangeCell;
	}
	
	/**
	 *  写入一行监测数据 
	 * @param row				在哪行写入
	 * @param startColumn		开始写入的列
	 * @param monthReportModel	存储数据的
	 * @param modelLen 			数据的长度，用于平均  平均访问时长
	 * @param beanMethod
	 * @param rangCell
	 * @throws Exception
	 */
	public void writeLineData(int row,int startColumn,int modelLen,
			MonthReportModel monthReportModel,String beanMethod[],int[] ... rangeCell) throws Exception{
		int column = 0;
		Object result = 0;
		int j = 0;
		String methodName = "";
		
		for (j = 0; j < beanMethod.length; j++) {
			column = startColumn + j;
			methodName = beanMethod[j];
			
			//内蒙古移动不需要后端数据
			if("内蒙古移动".equals(monthReportModel.getActivityName()) && (
						methodName.contains("Visit") || 
						methodName.endsWith("Click") ||
						methodName.contains("Visit") ||
						methodName.contains("BounceTimes") ||
						methodName.contains("PageView") ||
						methodName.contains("BounceRate") ||
						methodName.contains("ViewTime")
					)){
				result = NA;
			}
			else{
				result = setData(monthReportModel, beanMethod[j],modelLen);
			}
			xlsxUtil.writeCellData(result, row, column);
		}
		
		//合并单元格
		int[] rs = null;
		if(rangeCell != null){
			for (j = 0; j < rangeCell.length; j++) {
				rs = rangeCell[j];
				xlsxUtil.setRangeCell(rs[0], rs[1],rs[2],rs[3]);
			}
		}
	}//end writeLineData
	
	/**
	 * 媒体列表页的样式
	 * @param row	当前写入数据的行
	 * @param isSum	是否汇总数据
	 */
	public void mediaSheetStyle(int row,boolean isSum){
		
		Map<String,XSSFCellStyle> cs = isSum ? sumCellStyles : cellStyles; 
		XSSFCellStyle cellStyle = cs.get("decimails");
		
		for (int i = 1; i <= 16; i++) {
			
			if(i <= 4){
				//合并单元格中，不是首个单元格，不用设置样式
//				if( i != 1 && xlsxUtil.isMergedRegion(row, i)){
//					continue;
//				}
				cellStyle = cs.get("songti");
			}
			else if(i == 6 || i == 5){
//				//合并单元格中，不是首个单元格，不用设置样式
//				if(xlsxUtil.isMergedRegion(row, i)){
//					continue;
//				}
				cellStyle = cs.get("calibri");
			}
			else if(7 <= i && i <= 14){
				cellStyle = cs.get("number");
			}
			else if(i==15){
				cellStyle = cs.get("percent");
			}
			else{
				cellStyle = cs.get("decimails");
			}
			
			xlsxUtil.cloneCellStyle(row, i, cellStyle);
		}
	}//end mediaSheetStyle()
	
	/**
	 * 上海列表页的样式
	 * @param row
	 * @param startColumn
	 * @param endColumn
	 * @param isSum
	 */
	private void shangHaiSheetStyle(int row,boolean isSum){
		
		Map<String,XSSFCellStyle> cs = isSum ? sumCellStyles : cellStyles; 
		XSSFCellStyle cellStyle = null;
		
		for (int i = 1; i <= 35; i++) {
			
			if(i == 5 || i == 6 || i == 7 || i == 29 || i == 35){
				cellStyle = cs.get("songti");
			}
			else if(i <= 4 || i == 8 || i == 14 || (20 <= i && i <= 25) || i == 30){
				cellStyle = cs.get("calibri");
			}
			else if((9 <= i && i <= 13) || (26 <= i && i<= 28) || i == 31 || i == 32){
				cellStyle = cs.get("number");
			}
			else if((i >= 15 && i <= 18) || i == 33 || i == 34){
				cellStyle = cs.get("percent");
			}
			else{
				cellStyle = cs.get("decimails");
			}
			
			xlsxUtil.cloneCellStyle(row, i, cellStyle);
		}
	}//end shangHaiSheetStyle()
	
	
	/**
	 * 最后一个汇总页的样式
	 * @param row
	 * @param isSum 时候汇总
	 */
	public void lastSumSheetStyle(int row,boolean isSum){
		
		Map<String,XSSFCellStyle> cs = isSum ? sumCellStyles : cellStyles; 
		XSSFCellStyle cellStyle = null;
		
		for (int i = 1; i <= 23; i++) {
			
			if(i < 4){
				//合并单元格中，不是首个单元格，不用设置样式
//				if( i != 1 && xlsxUtil.isMergedRegion(row, i)){
//					continue;
//				}
				cellStyle = cs.get("songti");
			}
			else if(i == 4 || i == 5 || i == 22 || i == 23){
				//合并单元，不用设置样式
//				if(xlsxUtil.isMergedRegion(row, i)){
//					continue;
//				}
				cellStyle = cs.get("calibri");
			}
			else if((6 <= i && i <= 11) || (13 <= i && i <= 17)){
				cellStyle = cs.get("number");
			}
			else if(i==12 || i == 18 || i == 20 || i == 21){
				cellStyle = cs.get("percent");
			}
			else{
				cellStyle = cs.get("decimails");
			}
			
			xlsxUtil.cloneCellStyle(row, i, cellStyle);
		}
	}//end lastSumSheetStyle()
	
	/**
	 * Sheet中写入如下数据
	 * 	客户:
		投放时期:
		日期:
	 * @param cusName
	 * @param mediaName
	 * @param startDate
	 * @param endDate
	 */
	private void writeTitleData(String cusName,String mediaName,Date startDate,Date endDate){
		
		String datas[] = new String[3];
		datas[0] =  cusName;
		datas[1] =  mediaName;
		datas[2] = DateUtil.getDateStr(startDate,"yyyy年MM月dd日") +"-"+ DateUtil.getDateStr(endDate,"yyyy年MM月dd日");
		
		for(int i=0;i<datas.length;i++){
			xlsxUtil.writeCellData(datas[i],4+i,2);
			xlsxUtil.setAlign(4+i,2,XlsxUtil.ALIGN_CENTER,true);
			xlsxUtil.setAlign(4+i,2,XlsxUtil.ALIGN_VERTICAL_CENTER);
			xlsxUtil.setMergedRegionBorder(4+i,2,XlsxUtil.BORDER_THIN,XlsxUtil.BORDER_ALL);
			xlsxUtil.setFont(4+i,2, SONG_TI, (short)9);
		}
		
	}//end writeTitleData()
	
	/**
	 * 汇总普通排期的
	 * @param sumList
	 * @param isDayMic
	 */
	public MonthReportModel sumPoint(List<MonthReportModel> sumList){
		MonthReportModel sumModel = new MonthReportModel();
		MonthReportModel report = null;
		String mic = "";
		
		TreeMap<String,MonthReportModel> map = new TreeMap<>();
		
		for (int j = 0; j < sumList.size(); j++) {
			report = sumList.get(j);
			map.put(report.getMic(), report);
			
			//曝光预估	曝光次数	曝光人数	点击预估	点击次数	点击人数	访问次数	访问人数	到站点击	浏览量	跳出次数
			sumData("exposureAvg",sumModel,report.getExposureAvg(), 0);
			sumData("ImpPV",sumModel,report.getExposurePV(), 0);
			sumData("ImpUV",sumModel,report.getExposureUV(), 0);
			sumData("clickAvg",sumModel,report.getClickAvg(),0);
			sumData("ClkPV",sumModel,report.getClickPV(),0);
			sumData("ClkUV",sumModel,report.getClickUV(),0);
			
			sumData("visit",sumModel,report.getVisit(),0);
			sumData("visitor",sumModel,report.getVisitor(),0);
			sumData("click",sumModel,report.getClick(),0);
			sumData("pageView",sumModel,report.getPageView(),0);
			sumData("bounceTimes",sumModel,report.getBounceTimes(),0);
			
			sumData("viewTime",sumModel,report.getViewTime(),1);
			
//			//将月汇总数据装入Model中
//			if(!mic.equals(report.getMic())){
//				sumModel.setSumDirtyClkPV(null2Integer(sumModel.getSumDirtyClkPV()) + null2Integer(report.getSumClickPV()));
//				sumModel.setSumDirtyClkUV(null2Integer(sumModel.getSumDirtyClkUV()) + null2Integer(report.getSumClickUV()));
//				sumModel.setSumDirtyImpPV(null2Integer(sumModel.getSumDirtyImpPV()) + null2Integer(report.getSumExposurePV()));
//				sumModel.setSumDirtyImpUV(null2Integer(sumModel.getSumDirtyImpUV()) + null2Integer(report.getSumExposureUV()));
//				mic = report.getMic();
//			}// if
		}//end for
		
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		
		while (iterator.hasNext()) {
			report = map.get(iterator.next());
//			if(!mic.equals(report.getMic())){
			sumModel.setSumDirtyClkPV(null2Integer(sumModel.getSumDirtyClkPV()) + null2Integer(report.getSumClickPV()));
			sumModel.setSumDirtyClkUV(null2Integer(sumModel.getSumDirtyClkUV()) + null2Integer(report.getSumClickUV()));
			sumModel.setSumDirtyImpPV(null2Integer(sumModel.getSumDirtyImpPV()) + null2Integer(report.getSumExposurePV()));
			sumModel.setSumDirtyImpUV(null2Integer(sumModel.getSumDirtyImpUV()) + null2Integer(report.getSumExposureUV()));
			mic = report.getMic();
//			}// if
		}
		
		return sumModel;
	}
	
	/**
	 * 汇总最后一个页面的数据
	 * @param mediaSumList
	 * @param isDayMic 		是否是天的mic
	 */
	private MonthReportModel sumListData(List<MonthReportModel> sumList,boolean isDayMic){
		
		MonthReportModel sumModel = new MonthReportModel();
		MonthReportModel report = null;
		
		for (int j = 0; j < sumList.size(); j++) {
			report = sumList.get(j);
			//曝光预估	曝光次数	曝光人数	点击预估	点击次数	点击人数	访问次数	访问人数	到站点击	浏览量	跳出次数
			sumData("exposureAvg", sumModel, report.getExposureAvg(), 0);
			sumData("ImpPV", sumModel, report.getExposurePV(), 0);
			sumData("ImpUV", sumModel, report.getExposureUV(), 0);
			sumData("clickAvg",sumModel,report.getClickAvg(),0);
			sumData("ClkPV",sumModel,report.getClickPV(),0);
			sumData("ClkUV",sumModel,report.getClickUV(),0);
			
			sumData("visit",sumModel,report.getVisit(),0);
			sumData("visitor",sumModel,report.getVisitor(),0);
			sumData("click",sumModel,report.getClick(),0);
			sumData("pageView",sumModel,report.getPageView(),0);
			sumData("bounceTimes",sumModel,report.getBounceTimes(),0);
			
			sumData("viewTime",sumModel,report.getViewTime(),isDayMic ? 
					(report.getVisit() == null ? 1 : report.getVisit()) : 1);
		}
		
		return sumModel;
	}//end sumLastMediaSheet
	
	/**
	 * 将null数据转换成 Integer
	 * @param data
	 * @return
	 */
	public Integer null2Integer(Integer data){
		return data == null ? 0 : data;
	}
	/**
	 * 对数据进行汇总
	 * @param fieldName		数据的名称
	 * @param reportModel	保存汇总数据的对象
	 * @param data			数据
	 */
	private void sumData(String fieldName,MonthReportModel reportModel,Object dataObj,Integer visit){
		
		if(dataObj == null){
			return ;
		}
		Integer data = 0;
		Double douData = 0.0;
		
		if(dataObj instanceof Integer){
			data = (Integer)dataObj;
		}else if(dataObj instanceof Double){
			douData = (Double)dataObj;
		}else{
			return;
		}
		if(fieldName.endsWith("ImpPV")){
			reportModel.setDirtyImpPV((reportModel.getDirtyImpPV() == null ? 0 : reportModel.getDirtyImpPV()) + data);
		}
		else if(fieldName.endsWith("ImpUV")){
			reportModel.setDirtyImpUV((reportModel.getDirtyImpUV() == null ? 0 : reportModel.getDirtyImpUV()) + data);
		}
		else if(fieldName.endsWith("ClkPV")){
			reportModel.setDirtyClkPV((reportModel.getDirtyClkPV() == null ? 0 : reportModel.getDirtyClkPV()) + data);
		}
		else if(fieldName.endsWith("ClkUV")){
			reportModel.setDirtyClkUV((reportModel.getDirtyClkUV() == null ? 0 : reportModel.getDirtyClkUV()) + data);
		}//"visit","visitor","pageView","bounceTimes","bounceRate","viewTime"
		else if("visit".equals(fieldName)){
			reportModel.setVisit((reportModel.getVisit() == null ? 0 : reportModel.getVisit()) + data);
		}
		else if("visitor".equals(fieldName)){
			reportModel.setVisitor((reportModel.getVisitor() == null ? 0 : reportModel.getVisitor()) + data);
		}
		else if("pageView".equals(fieldName)){
			reportModel.setPageView((reportModel.getPageView() == null ? 0 : reportModel.getPageView()) + data);
		}
		else if("bounceTimes".equals(fieldName)){
			reportModel.setBounceTimes((reportModel.getBounceTimes() == null ? 0 : reportModel.getBounceTimes()) + data);
		}
//		else if("bounceRate".equals(fieldName)){
//			//跳出率 =总跳出次数/总到站访次
//			reportModel.setBounceRate((double)reportModel.getBounceTimes() / (double)reportModel.getVisit());
//		}
		else if("click".equals(fieldName)){
			reportModel.setClick((reportModel.getClick() == null ? 0 : reportModel.getClick()) + data);
		}
		else if("viewTime".equals(fieldName)){
			reportModel.setViewTime((reportModel.getViewTime() == null ? 0 : reportModel.getViewTime()) + douData * visit);
		}
		else if("exposureAvg".equals(fieldName)){
			reportModel.setExposureAvg((reportModel.getExposureAvg() == null ? 0 : reportModel.getExposureAvg()) + data + "");
		}
		else if("exposureAvg".equals(fieldName)){
			reportModel.setExposureAvg((reportModel.getExposureAvg() == null ? 0 : reportModel.getExposureAvg()) + data + "");
		}
		else if("clickAvg".equals(fieldName)){
			reportModel.setClickAvg((reportModel.getClickAvg() == null ? 0 : reportModel.getClickAvg()) + data + "");
		}
		
	}//end sumData()
	
	/**
	 * 保存月报文件路径
	 * @param cusCode
	 * @param dateStr
	 * @return
	 */
	public String getMonthReportPath(List<MonthReportModel> monthList,String dateStr){
		
		if(monthList == null || monthList.size() == 0){
			return "0";
		}
		
		StringBuffer filePathSB = new StringBuffer(50);
		
		filePathSB.append(fpm.getTmpExcel());
		filePathSB.append("/month/");
		filePathSB.append(monthList.get(0).getCustomerCode());
		
		File file = new File(filePathSB.toString());
		if(!file.exists()){
			file.mkdirs();
		}
		
		filePathSB.append("/项目月报_");
		filePathSB.append(monthList.get(0).getCustomerName());
		filePathSB.append("互联网推广监测_");
		filePathSB.append(dateStr);
		filePathSB.append(".xlsx");
		
		return filePathSB.toString();
	}//end saveMonthReport
	
	
	
	private static List<int[]> rangCellList = new LinkedList<>();
	static{
		//小标题合并单元格
		rangCellList.add(new int[]{7,1,8,1});
		rangCellList.add(new int[]{7,2,8,2});
		rangCellList.add(new int[]{7,3,8,3});
		rangCellList.add(new int[]{7,XlsxUtil.getColumnNum("E"),7,XlsxUtil.getColumnNum("M")});
		rangCellList.add(new int[]{7,XlsxUtil.getColumnNum("N"),7,XlsxUtil.getColumnNum("T")});
		rangCellList.add(new int[]{7,XlsxUtil.getColumnNum("U"),7,XlsxUtil.getColumnNum("V")});
		rangCellList.add(new int[]{7,XlsxUtil.getColumnNum("W"),8,XlsxUtil.getColumnNum("W")});
		rangCellList.add(new int[]{7,XlsxUtil.getColumnNum("X"),8,XlsxUtil.getColumnNum("X")});
	}
	private static String[] lastSheetTitleTop = {"项目名称","媒体名称","广告位","广告曝光监测","活动网站监测","完成率情况","URL","备注"}; 
	private static String[] lastSheetTitle = {"营销识别码","日期","曝光预估","曝光次数","曝光人数","点击预估","点击次数","点击人数","点击率CTR","访问次数","访问人数","到站点击","浏览量","跳出次数","跳出率","平均访问时长(s)","曝光完成率","点击完成率"};
	
	/**
	 * 初始化最后一个sheet
	 * @param sheetNum	第几个sheet
	 * @param campaign	客户
	 * @param period	月份
	 */
	public void initLastSheet(int sheetNum,String campaign,String period){
//		xlsxUtil.
		xlsxUtil.setSheet(sheetNum, campaign+"汇总");
		xlsxUtil.setDisplayGridlines(false);
		
		//左上角信息
		XSSFCellStyle cellStyle = writeNewData("Campaign:",2,1, null, CALIBRI, (short)9, null, 
				XlsxUtil.ALIGN_LEFT, null, XlsxUtil.FONT_BOLD, null, null, null);
		
		xlsxUtil.writeCellData("Period:", 3, 1,cellStyle);
		xlsxUtil.writeCellData("Preparedby:", 4, 1,cellStyle);
		xlsxUtil.writeCellData("UDBAC",4, 2,cellStyle);
		
		writeNewData(campaign,2,2, null, SONG_TI, (short)9, null, 
				XlsxUtil.ALIGN_LEFT, null, XlsxUtil.FONT_BOLD, XlsxUtil.BORDER_THIN, null, XlsxUtil.BORDER_BOTTOM);
		
		writeNewData(period,3,2, null, CALIBRI, (short)9, null, 
				XlsxUtil.ALIGN_LEFT, null, XlsxUtil.FONT_BOLD, null, null, null);
		
		//小标题
		int i=0 ;
		cellStyle = xlsxUtil.createCellStyle(BLUE, null, 
				XlsxUtil.FONT_NAME_SONGTI, (short)9, WHITE, XlsxUtil.FONT_BOLD, 
				XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
				XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
		
		for(;i<rangCellList.size();i++){
			int[] index = rangCellList.get(i);
			xlsxUtil.setRangeCell(index[0],index[1],index[2],index[3]);
			xlsxUtil.writeCellData(lastSheetTitleTop[i],index[0], index[1], cellStyle);
		}
		
		int column = 4;
		for (i = 0; i < lastSheetTitle.length; i++) {
				xlsxUtil.writeCellData(lastSheetTitle[i],8,column, cellStyle);
			++column;
		}
	}//end init
	
	/**
	 * 初始化月报模板
	 * @param filePath
	 */
	protected void initMonthReport(){
		
		try {
			
			xlsxUtil.setDisplayGridlines(false);
			
			xlsxUtil.insertImage(1,XlsxUtil.getColumnNum("O"),3,XlsxUtil.getColumnNum("Q"), MonthReportController.MonthLogo);
			
			//互联网广告投放结案报告
			int[] titleXy = {1,1};
			xlsxUtil.setRangeCell(1, 1, 2, XlsxUtil.getColumnNum("Q"));
			xlsxUtil.writeCellData("互联网广告投放结案报告",titleXy[0],titleXy[1]);
			xlsxUtil.setCellBgColor(titleXy[0],titleXy[1],new int[]{31,73,125},true);
			xlsxUtil.setAlign(titleXy[0],titleXy[1],XlsxUtil.ALIGN_CENTER);
			xlsxUtil.setAlign(titleXy[0],titleXy[1],XlsxUtil.ALIGN_VERTICAL_CENTER);
			xlsxUtil.setFont(titleXy[0],titleXy[1], SONG_TI, (short)9);
			xlsxUtil.setFontColor(titleXy[0],titleXy[1], new int[]{255,255,255});
			xlsxUtil.setFontBold(titleXy[0],titleXy[1], XlsxUtil.FONT_BOLD);
			xlsxUtil.setMergedRegionBorder(titleXy[0],titleXy[1],XlsxUtil.BORDER_MEDIUM,XlsxUtil.BORDER_ALL);
			xlsxUtil.setRowHeight(titleXy[0],(short) 25);
			xlsxUtil.setRowHeight(titleXy[0] + 1,(short) 20);
			
			//经过UDBAC互联网广告监测认证
			int [] secondTitleXy = {3,1};
			xlsxUtil.setRangeCell(3, 1, 3, XlsxUtil.getColumnNum("Q"));
			xlsxUtil.writeCellData("经过UDBAC互联网广告监测认证",secondTitleXy[0],secondTitleXy[1]);
			xlsxUtil.setCellBgColor(secondTitleXy[0],secondTitleXy[1],new int[]{31,73,125},true);
			xlsxUtil.setAlign(secondTitleXy[0],secondTitleXy[1],XlsxUtil.ALIGN_CENTER);
			xlsxUtil.setAlign(secondTitleXy[0],secondTitleXy[1],XlsxUtil.ALIGN_VERTICAL_CENTER);
			xlsxUtil.setFont(secondTitleXy[0],secondTitleXy[1], SONG_TI, (short)9);
			xlsxUtil.setFontColor(secondTitleXy[0],secondTitleXy[1], new int[]{255,255,255});
			xlsxUtil.setFontBold(secondTitleXy[0],secondTitleXy[1], XlsxUtil.FONT_BOLD);
			xlsxUtil.setMergedRegionBorder(secondTitleXy[0],secondTitleXy[1],XlsxUtil.BORDER_MEDIUM,XlsxUtil.BORDER_ALL);
			
			xlsxUtil.setRowHeight(secondTitleXy[0],(short) 20);
			//客户
			int[] cusXy = {4,1};
			xlsxUtil.writeCellData("客户:",cusXy[0],cusXy[1]);
			xlsxUtil.setFont(cusXy[0], cusXy[1],SONG_TI, (short)9,true);
			xlsxUtil.setFontColor(cusXy[0],cusXy[1], new int[]{0,0,0});
			xlsxUtil.setAlign(cusXy[0],cusXy[1], XlsxUtil.ALIGN_LEFT);
			xlsxUtil.setMergedRegionBorder(cusXy[0],cusXy[1], XlsxUtil.BORDER_THIN,XlsxUtil.BORDER_ALL);
			
			xlsxUtil.setRangeCell(cusXy[0],cusXy[1] + 1,cusXy[0], XlsxUtil.getColumnNum("Q"));
			xlsxUtil.setFont(cusXy[0], cusXy[1] + 1,SONG_TI, (short)9,true);
			xlsxUtil.setFontColor(cusXy[0],cusXy[1] + 1, new int[]{0,0,0});
			xlsxUtil.setAlign(cusXy[0],cusXy[1] + 1, XlsxUtil.ALIGN_CENTER);
			xlsxUtil.setMergedRegionBorder(cusXy[0],cusXy[1] + 1,XlsxUtil.BORDER_THIN,XlsxUtil.BORDER_ALL);
			
			//投放媒体
			int[] mediaXy = {5,1};
			xlsxUtil.writeCellData("投放媒体:",mediaXy[0],mediaXy[1]);
			xlsxUtil.setFont(mediaXy[0], mediaXy[1],SONG_TI, (short)9,true);
			xlsxUtil.setFontColor(mediaXy[0],mediaXy[1], new int[]{0,0,0});
			xlsxUtil.setAlign(mediaXy[0],mediaXy[1], XlsxUtil.ALIGN_LEFT);
			xlsxUtil.setMergedRegionBorder(mediaXy[0],mediaXy[1], XlsxUtil.BORDER_THIN,XlsxUtil.BORDER_ALL);
			
			xlsxUtil.setRangeCell(mediaXy[0],mediaXy[1] + 1,mediaXy[0], XlsxUtil.getColumnNum("Q"));
			xlsxUtil.setFont(mediaXy[0], mediaXy[1] + 1,SONG_TI, (short)9,true);
			xlsxUtil.setFontColor(mediaXy[0],mediaXy[1] + 1, new int[]{0,0,0});
			xlsxUtil.setAlign(mediaXy[0],mediaXy[1] + 1, XlsxUtil.ALIGN_CENTER);
			xlsxUtil.setMergedRegionBorder(mediaXy[0], mediaXy[1] + 1,XlsxUtil.BORDER_THIN,XlsxUtil.BORDER_ALL);
			
			//日期
			int[] dateXy = {6,1};
			xlsxUtil.writeCellData("日期:",dateXy[0],dateXy[1]);
			xlsxUtil.setFont(dateXy[0], dateXy[1],SONG_TI, (short)9,true);
			xlsxUtil.setFontColor(dateXy[0],dateXy[1], new int[]{0,0,0});
			xlsxUtil.setAlign(dateXy[0],dateXy[1], XlsxUtil.ALIGN_LEFT);
			xlsxUtil.setMergedRegionBorder(dateXy[0],dateXy[1], XlsxUtil.BORDER_THIN,XlsxUtil.BORDER_ALL);
			
			xlsxUtil.setRangeCell(dateXy[0],dateXy[1] + 1,dateXy[0], XlsxUtil.getColumnNum("Q"));
			xlsxUtil.setFont(dateXy[0], dateXy[1] + 1,SONG_TI, (short)9,true);
			xlsxUtil.setFontColor(dateXy[0],dateXy[1] + 1, new int[]{0,0,0});
			xlsxUtil.setAlign(dateXy[0],dateXy[1] + 1, XlsxUtil.ALIGN_CENTER);
			xlsxUtil.setMergedRegionBorder(dateXy[0],dateXy[1] + 1,XlsxUtil.BORDER_THIN,XlsxUtil.BORDER_ALL);
			
			//smallTitile
			String smallTitile[] = {"活动名称","投放媒体","投放位置","投放形式","日期","营销识别码","监测曝光PV","监测曝光UV","监测点击PV","监测点击UV","访问次数","访问人数","浏览量","跳出次数","跳出率","平均访问时长(s)"};
			int startXy[] = {7,1} ;
			for (int i = 0; i < smallTitile.length; i++) {
				
				xlsxUtil.writeCellData(smallTitile[i],startXy[0],startXy[1] + i);
				xlsxUtil.setCellBgColor(startXy[0],startXy[1] + i,new int[]{31,73,125},true);
				xlsxUtil.setAlign(startXy[0],startXy[1] + i, XlsxUtil.ALIGN_CENTER);
				xlsxUtil.setAlign(startXy[0],startXy[1],XlsxUtil.ALIGN_VERTICAL_CENTER);
				xlsxUtil.setFont(startXy[0],startXy[1] + i,SONG_TI, (short)9);
				xlsxUtil.setFontColor(startXy[0],startXy[1] + i, new int[]{255,255,255});
				xlsxUtil.setFontBold(startXy[0],startXy[1] + i, XlsxUtil.FONT_BOLD);
				xlsxUtil.setMergedRegionBorder(startXy[0],startXy[1] + i, XlsxUtil.BORDER_THIN,XlsxUtil.BORDER_ALL);
				
			}
			
			xlsxUtil.setColumnWidth(XlsxUtil.getColumnNum("Q"),20);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//end initMonthReport()
	
	/**
	 * 初始化上海移动报告模板
	 */
	private static String smallTitile[] = {"开始日期","结束日期","活动类型","活动子类","活动名称","媒体类别","媒体名称","投放终端","广告曝光量","独立曝光量","广告点击量","独立点击量","广告到站量","广告成本量",
		"点击率","独立点击率","到达率","跳出率","平均访问时长","平均访问深度","业务办理量","活动转化率","千次曝光成本","点击成本","到站成本","进站PV","访问人数","跳出访次","投放点位","短代码","预估曝光量","预估点击量","曝光完成率","点击完成率",""};
	
	private static String monthMethod[] = {"getStartDate","getEndDate","","","getActivityName","getMediaType","getMediaName","getTerminalType","getExposurePV","getExposureUV","getClickPV","getClickUV","getVisit","",
		"getClickRate","getOloneClickRate","getArriveRate","getBounceRate","getViewTime","","","","","","","getPageView","getVisitor","getBounceTimes","getPointLocation","getMic","getExposureAvg","getClickAvg","getExposureFinishRate","getClickFinishRate"};
	
	/**
	 * 将上海月报的数据写入文件。
	 * @param monthList
	 */
	public void shanghaiMonthReport(List<MonthReportModel> monthList){

		int i = 0,j = 0;
		int[] white = {255,255,255};
		int[] black = {0,0,0};
		int[] smallTitleBg = {118,147,60};
		int[] blue = {141,180,226};
		int row = smallTitile.length;
		int column = 0;
		//列号
		for (i=1; i < row; i++) {
			writeNewData(i,0 ,i,null, CALIBRI,(short)9,black,
					XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, XlsxUtil.FONT_BOLD, 
					XlsxUtil.BORDER_THIN,null, XlsxUtil.BORDER_ALL);
		}
		
		writeNewData("备注",0 ,i,null, CALIBRI,(short)9,black,
				XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, XlsxUtil.FONT_BOLD, 
				XlsxUtil.BORDER_THIN,null, XlsxUtil.BORDER_ALL);
		
		//小标题样式
		for (i=1; i < row + 1; i++) {
			int[] color = null;
			if(i == 13|| (i >= 17 && i <= 28)){
				color = blue;
			}else{
				color = smallTitleBg;
			}
			writeNewData(smallTitile[i - 1],1,i, color, SONG_TI,(short)9,white,
					XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, XlsxUtil.FONT_BOLD, 
					XlsxUtil.BORDER_THIN,null, XlsxUtil.BORDER_ALL);
		}
		xlsxUtil.setRowHeight(1,(short)50);

		MonthReportModel reportModel = null;
		Object data = null;
		//遍历月报数据
		if (monthList != null) {
			for(i=1;i < monthList.size();i++){
				row = i + 1;
				reportModel = monthList.get(i);
				
				for(j=0;j<monthMethod.length;j++){
					column = j+1;
					try {
						data = setData(reportModel, monthMethod[j],0);
						if(j < 2){
							data = DateUtil.getDateStr((Date)data, "yyyy-MM-dd");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					xlsxUtil.writeCellData(data, row, column);
				
				}//end for(j=0;j<monthMethod.length;j++)
				
				shangHaiSheetStyle(row, false);
			}//end for(i=0;i < monthList.size();i++)		
		}
	}
	
	
	/**
	 * 反射MethodName设置一些通用的值
	 * @param monthReportModel
	 * @param methodName
	 * @param modelLen 			用于计算平均访问时长
	 */
	public Object setData(MonthReportModel monthReportModel,String methodName,int modelLen
			) throws  Exception{
		if("".equals(methodName)){
			return NA;
		}
		
		Object data = null;
		Object tmpData = null;
		Method method = modelClazz.getDeclaredMethod(methodName);
		tmpData = method.invoke(monthReportModel);
		
		//判断是否支持点击
		if(methodName.endsWith("ClkUV") || methodName.endsWith("ClkPV")){
			data = "1".equals(monthReportModel.getSupportClick())  ?
						(tmpData == null ? NA : tmpData) : NA;
		}
		//判断是否支持曝光
		else if(methodName.endsWith("ImpUV") || methodName.endsWith("ImpPV")){
			data = "1".equals(monthReportModel.getSupportExposure())  ?
					(tmpData == null ? NA : tmpData) : NA;
		}
		//跳出率计算,以及访问时长
		else if(methodName.endsWith("Rate") || "getAvgViewTime".equals(methodName)){
			if(tmpData == null){
				data = NA;
			}else if(new Double(-1.0).equals(tmpData)){
				data = DIV0;
			}else{
				data = tmpData;
			}
		}
		//平均访问时长
		else if(methodName.equals("getViewTime")){
			if(tmpData == null){
				data = NA;
			}else if(modelLen == 0){
				data = DIV0;
			}else{
				data = (double)tmpData / modelLen;
			}
		}
		//投放日期
		else if("getPutDate".equals(methodName)){
			data = DateUtil.getDateStr((Date)tmpData, "yyyy/MM/dd");
		}
		else {
			data = tmpData == null ? NA : tmpData;
		}
		return data;
	}//end 
	
	
	/**
	 * 写入新数据
	 * @param data
	 * @param x
	 * @param y
	 * @param bgColor
	 * @param fontName
	 * @param fontSize
	 * @param fontColor
	 * @param align
	 * @param valign
	 * @param fontBold
	 * @param borderStyle
	 * @param border
	 */
	public XSSFCellStyle writeNewData(Object data,int x,int y,
			int[] bgColor,
			String fontName,Short fontSize,int[] fontColor,
			Short align,Short valign,Short fontBold,
			Short borderStyle,String format,short... border){
		
		XSSFCellStyle cellStyle = null;
		xlsxUtil.writeCellData(data,x,y);
		
		if(fontName == null){
			fontName = SONG_TI;
		}
		cellStyle = xlsxUtil.setFont(x,y,fontName, fontSize,true);
		
		if(align != null){
			xlsxUtil.setAlign(x,y,align);
		}
		
		if(bgColor != null){
			xlsxUtil.setCellBgColor(x,y,bgColor);
		}
		
		if(fontColor != null){
			xlsxUtil.setFontColor(x,y,fontColor);
		}
		
		if(borderStyle != null && border != null){
			xlsxUtil.setMergedRegionBorder(x,y,borderStyle,border);
		}
		
		if(format != null &&  "".equals(format)){
			xlsxUtil.format(x, y,format);
		}
		if(fontBold != null){
			xlsxUtil.setFontBold(x,y, fontBold);
		}
		if(valign != null){
			xlsxUtil.setAlign(x,y,valign);
		}
		return cellStyle;
	}
	
	
	/**
	 * 合并同一天相同的投放形式
	 */
	public void RangeSamePutFunction(){
		                            
		
		
	}
}