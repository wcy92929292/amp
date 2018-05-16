package com.udbac.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.util.DateUtil;
import com.udbac.controller.TotalController;
import com.udbac.dao.TotalDao;
import com.udbac.entity.WeekDataExport;
import com.udbac.model.MonthReportModel;
import com.udbac.service.TotalService;
import com.udbac.util.FilePathManager;
import com.udbac.util.XlsxUtil;
@Service("totalService")
public class TotalServiceImpl implements TotalService {
	@Autowired
	TotalDao totalDao;
	
    protected XlsxUtil xlsxUtil = null;
	
	@Autowired
	private FilePathManager fpm;
	
	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

	private Class<MonthReportModel> modelClazz = MonthReportModel.class; 
	//用于存储Excel中用到的XSSFCellStyle
    private Map<String,XSSFCellStyle> cellStyles = new HashMap<>();
    
		//用于存储Excel中用到的XSSFCellStyle
	private Map<String,XSSFCellStyle> sumCellStyles = new HashMap<>();
	
	
	private static String[] lastSheetTitleTop = {"项目名称","媒体名称","广告位","广告曝光监测","活动网站监测","完成率情况","URL","备注"}; 
	private static String[] lastSheetTitle = {"营销识别码","日期","曝光预估","曝光次数","曝光人数","点击预估","点击次数","点击人数","点击率CTR","访问次数","访问人数","到站点击","浏览量","跳出次数","跳出率","平均访问时长(s)","曝光完成率","点击完成率"};
	/**
	 * 通用月数据最后sheet汇总
	 * @param monthList
	 */
	private static String[] lastSheetMethods ={"getActivityName","getMediaName","getPointLocation","getMic","getStartToEndDate","getExposureAvg",
		"getExposurePV","getExposureUV","getClickAvg","getClickPV","getClickUV","getClickRate","getVisit","getVisitor",
		"getClick","getBounceTimes","getPageView","getBounceRate","getAvgViewTime","getExposureFinishRate","getClickFinishRate","getUrl"};
	private static String[] lastSheetMediaMethods ={"getMediaName","","","","","getExposureAvg",
		"getExposurePV","getExposureUV","getClickAvg","getClickPV","getClickUV","getClickRate","getVisit","getVisitor",
		"getClick","getBounceTimes","getPageView","getBounceRate","getAvgViewTime","getExposureFinishRate","getClickFinishRate"};
	private static String[] lastSheetActMethods ={"getActivityName","","","","","getExposureAvg",
		"getExposurePV","getExposureUV","getClickAvg","getClickPV","getClickUV","getClickRate","getVisit","getVisitor",
		"getClick","getBounceTimes","getPageView","getBounceRate","getAvgViewTime","getExposureFinishRate","getClickFinishRate"};
	
	/**
	 * 初始化最后一个sheet
	 */
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
	@Override
	public List<WeekDataExport> listTotalInfo(Date dt, String customer_id,
			String actCode) {
		Date dt1=new Date();
		if(dt!=null){
			dt1=getNextDay(dt);
		}
		return totalDao.listTotalInfo(dt,dt1,customer_id,actCode);
	}
	@Override
	public List<WeekDataExport> listHuiZongInfo(Date dt, String customer_id,
			String actCode) {
		Date dt1=new Date();
		if(dt!=null){
			dt1=getNextDay(dt);
		}
		return totalDao.listHuiZongInfo(dt,dt1,customer_id,actCode);
	}
	@Override
	public Integer sumDay(Date dt,  String customer_id,String actCode) {
		Date dt1=new Date();
		if(dt!=null){
			dt1=getNextDay(dt);
		}
		return totalDao.sumDay(dt,dt1,customer_id,actCode);
	}
	@Override
	public String monthReportList(String cusCode,Date dateStr,String realName,String actCode,Integer dayIf,Integer isRegion) {
		
		List<MonthReportModel> monthList = null;
		String month = "";
		month = sd.format(dateStr);
		String path = "";
			//普通月结报告
		monthList = totalDao.listMonth1(cusCode,dateStr,actCode,dayIf);
//	    monthList.addAll(totalDao.listMonthBaidu1(cusCode,dateStr,actCode,dayIf));
			
			if(monthList.size() == 0){
				return "0";
			}
			path = getMonthReportPath(monthList, month);
			newFile(path);
			initCellStyle();
			//将数据写入Excel文件
			int sheetNum = writeMonthDataToXlsx(monthList);
		    	//最后一页汇总信息
		    	monthList = totalDao.listSumMic(cusCode,dateStr,actCode);
		    	if(monthList.size()>0){
					initLastSheet(sheetNum, monthList.get(0).getCustomerName(), month,realName);
		    	}
				sumLastSheetData(monthList);
		    	xlsxUtil.close(null);	
			
			
		
		return path;
		
	}
	
	/**
	 * 保存月报文件路径
	 * @param cusCode
	 * @param month
	 * @return
	 */
	public String getMonthReportPath(List<MonthReportModel> monthList,String month){
		
		if(monthList.size() == 0){
			return "0";
		}
		
		StringBuffer filePathSB = new StringBuffer(50);
		
		filePathSB.append(fpm.getTmpExcel());
		filePathSB.append("/total/");
		filePathSB.append(monthList.get(0).getCustomerCode());
		
		File file = new File(filePathSB.toString());
		if(!file.exists()){
			file.mkdirs();
		}
		
		
		filePathSB.append("/"+monthList.get(0).getCustomerName());
		filePathSB.append("累计数据报表_按月_");
		filePathSB.append(month);
		filePathSB.append(".xlsx");
		
		return filePathSB.toString();
	}//end saveMonthReport
	/**
	 * 将监测数据写入报告文件
	 */
	//活动名称	投放媒体	投放位置	投放形式	日期	营销识别码	监测曝光PV	监测曝光UV	监测点击PV	监测点击UV	访问次数	访问人数	浏览量	跳出次数	跳出率	平均访问时长(s)
	//点位方法
	private static final String listMonthMethod[] = {"getActivityName","getMediaName","getPointLocation","getPutFunction","getPutDate",
		"getMic","getExposurePV","getExposureUV","getClickPV","getClickUV","getVisit","getVisitor","getPageView","getBounceTimes","getBounceRate","getViewTime"};
	//媒体sheet汇总
	private static final String sumMediaMethods[] = {"getMediaName","","","","","","getExposurePV","getExposureUV","getClickPV","getClickUV","getVisit","getVisitor","getPageView","getBounceTimes","getBounceRate","getAvgViewTime"};
	private static final String sumPointMethods[] = {"getPointLocation","","","","","","getExposurePV","getExposureUV","getClickPV","getClickUV","getVisit","getVisitor","getPageView","getBounceTimes","getBounceRate","getAvgViewTime"};
	
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
		List<MonthReportModel> pointReportList = new LinkedList<>();			//用于记录相同投放位置的点位，便于对投放位置的汇总。
		List<MonthReportModel> sheetReportList = new LinkedList<>();	//用于记录相同媒体的点位，便于对其进行汇总
		startRow = startXy[0];
		
		try {
			for (i = 0; i < monthList.size(); i++) {
				
				monthReportModel = monthList.get(i);
				
				//初始化工作簿设置
				if(!mediaName.equals(monthReportModel.getMediaName())){
					
//					//对上一个sheet页进行汇总
					if(sheetNum > 0){
						//汇总点位数据
						sumModel = sumPointList(pointReportList, startRow, pointName, rangeCell);
						//将汇总点位点位数据放入媒体汇总List
						sheetReportList.add(sumModel);
						startRow ++;
						
						//汇总媒体sheet信息
						sumMediaList(sheetReportList, startRow, mediaName, rangeCell);
						//写上标题日期信息
						writeTitleData(cusName,mediaName,startDate,endDate);
					}
					//初始化媒体名称、点位名称
					mediaName = monthReportModel.getMediaName();
					pointName = monthReportModel.getPointLocation();
					//初始化日期
					startDate = monthReportModel.getPutDate();
					endDate = startDate;
					//初始化工作簿
					xlsxUtil.setSheet(sheetNum ++,mediaName);
					initMonthReport();
					startRow = startXy[0];
				}//end if 初始化工作簿
				
				//分投放位置汇总
				if(!pointName.equals(monthReportModel.getPointLocation())){
					if(pointReportList.size() != 0){
						
						//汇总点位数据
						sumModel = sumPointList(pointReportList, startRow, pointName, rangeCell);
						//将汇总点位点位数据放入媒体汇总List
						sheetReportList.add(sumModel);
						
						startRow++;
					}//end if(pointReportList.size() != 0){
					
					//初始化点位信息
					pointName = monthReportModel.getPointLocation();
				}//end if(!pointName.equals(monthReportModel.getPointLocation()))
				
				//将点位数据汇总到点位汇总List
				pointReportList.add(monthReportModel);
				
				//写入点位明细数据
				writeLineData(startRow, 1, monthReportModel, listMonthMethod);
				mediaSheetStyle(startRow, false);
				
				//判断结束日期
				if(endDate.compareTo(monthReportModel.getPutDate()) < 0 ){
					endDate = monthReportModel.getPutDate();
				}
				
				startRow ++;
			}//end for( i < monthList.size())
			
			
			//汇总点位数据
			sumModel = sumPointList(pointReportList, startRow, pointName, rangeCell);
			//将汇总点位点位数据放入媒体汇总List
			sheetReportList.add(sumModel);
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
	
	/* 初始化月报模板
	 * @param filePath
	 */
	protected void initMonthReport(){
		
		try {
			
			xlsxUtil.setDisplayGridlines(false);
			
			xlsxUtil.insertImage(1,XlsxUtil.getColumnNum("O"),3,XlsxUtil.getColumnNum("Q"), TotalController.MonthLogo);
			
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
			if(XlsxUtil.getColumnNum("D")==3){
				xlsxUtil.setColumnWidth(1,30);//活动名称
				xlsxUtil.setColumnWidth(3,30);//广告位置
				xlsxUtil.setColumnWidth(4,30);//投放形式
				xlsxUtil.setColumnWidth(6,20);//短代码
			}else{
				xlsxUtil.setColumnWidth(XlsxUtil.getColumnNum("Q"),20);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//end initMonthReport()
	
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
		sumModel = sumListData(pointReportList,true);
		sumModel.setPointLocation(pointName + "汇总");
		
		//写入汇总 点位数据
		writeLineData(startRow, 1, sumModel, sumPointMethods,setRangeCell(rangeCell,startRow,1,startRow,6));
		mediaSheetStyle(startRow, true);
		
		//清空list
		pointReportList.clear();
		
		return sumModel;
	}//end sumPointList
	
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
		sumModel = sumListData(sheetReportList,false);
		sumModel.setMediaName(mediaName + "总计");
		
		//写入一行汇总数据
		writeLineData(startRow, 1, sumModel, sumMediaMethods,setRangeCell(rangeCell,startRow,1,startRow,6));
		mediaSheetStyle(startRow, true);
		
		sheetReportList.clear();
		return sumModel;
	}//end sumMediaList
	
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
	
	private int[] setRangeCell(int[] rangeCell,int firstRow,int firstColumn,int lastRow,int lastColumn){
		if(rangeCell != null && rangeCell.length == 4 ){
			rangeCell[0] = 	firstRow;
			rangeCell[1] = 	firstColumn;
			rangeCell[2] = 	lastRow;
			rangeCell[3] = 	lastColumn;
		}
		return rangeCell;
	}
	
	
	/**
	 *  写入一行监测数据 
	 * @param row				在哪行写入
	 * @param startColumn		开始写入的列
	 * @param monthReportModel	存储数据的
	 * @param beanMethod
	 * @param rangCell
	 * @throws Exception
	 */
	public void writeLineData(int row,int startColumn,
			MonthReportModel monthReportModel,String beanMethod[],int[] ... rangeCell) throws Exception{
		int column = 0;
		Object result = 0;
		int j = 0;
		String methodName = "";
		
		for (j = 0; j < beanMethod.length; j++) {
			column = startColumn + j;
			methodName = beanMethod[j];
			
			if("内蒙古移动".equals(monthReportModel.getActivityName()) && (
						methodName.contains("Visit") || 
						methodName.contains("Click") ||
						methodName.contains("Visit") ||
						methodName.contains("BounceTimes") ||
						methodName.contains("PageView") ||
						methodName.contains("BounceRate") ||
						methodName.contains("ViewTime")
					)){
				
				result = NA;
				
			}else{
				result = setData(monthReportModel, beanMethod[j]);
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
	
	/* 反射MethodName设置一些通用的值
	 * @param monthReportModel
	 * @param methodName
	 */
	public Object setData(MonthReportModel monthReportModel,String methodName) throws  Exception{
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
		//投放日期
		else if("getPutDate".equals(methodName)){
			data = DateUtil.getDateStr((Date)tmpData, "yyyy/MM/dd");
		}
		else {
			data = tmpData == null ? NA : tmpData;
		}
		return data;
	}//end 
	

		public void initLastSheet(int sheetNum,String campaign,String month, String realName){
//		xlsxUtil.
		xlsxUtil.setSheet(sheetNum, campaign+"汇总");
		xlsxUtil.setDisplayGridlines(false);
		//设置特殊宽度
		if(XlsxUtil.getColumnNum("D")==3){
			xlsxUtil.setColumnWidth(1,30);//项目名称
			xlsxUtil.setColumnWidth(3,30);//广告位
			xlsxUtil.setColumnWidth(4,20);//短代码
			xlsxUtil.setColumnWidth(5,20);//开始截止日期
			xlsxUtil.setColumnWidth(22,20);//url
		}else{
			xlsxUtil.setColumnWidth(XlsxUtil.getColumnNum("Q"),20);

		}
		
		//左上角信息
		XSSFCellStyle cellStyle = writeNewData("项目名称:",2,1, null, CALIBRI, (short)9, null, 
				XlsxUtil.ALIGN_LEFT, null, XlsxUtil.FONT_BOLD, null, null, null);
		
		xlsxUtil.writeCellData("报表周期:", 3, 1,cellStyle);
		xlsxUtil.writeCellData("交付日期:", 4, 1,cellStyle);
		xlsxUtil.writeCellData(sd.format(new Date()),4, 2,cellStyle);
		xlsxUtil.writeCellData("交付人",5, 1,cellStyle);
		xlsxUtil.writeCellData(realName,5, 2,cellStyle);
		
		writeNewData(campaign,2,2, null, SONG_TI, (short)9, null, 
				XlsxUtil.ALIGN_LEFT, null, XlsxUtil.FONT_BOLD, XlsxUtil.BORDER_THIN, null, XlsxUtil.BORDER_BOTTOM);
		
		writeNewData(month,3,2, null, CALIBRI, (short)9, null, 
				XlsxUtil.ALIGN_LEFT, null, XlsxUtil.FONT_BOLD, null, null, null);
		
		//小标题
		
		int[] blue = {55,96,145};
		int[] white = {255,255,255};
		int i=0 ;
		for(;i<rangCellList.size();i++){
			int[] index = rangCellList.get(i);
			xlsxUtil.setRangeCell(index[0],index[1],index[2],index[3]);
			if(i==0){
				cellStyle = writeNewData(lastSheetTitleTop[0],index[0], index[1],
						blue,SONG_TI, (short)9, white, 
						XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
						XlsxUtil.FONT_BOLD, XlsxUtil.BORDER_THIN, null,XlsxUtil.BORDER_ALL);
			}else{
				xlsxUtil.writeCellData(lastSheetTitleTop[i],index[0], index[1], cellStyle);
			}
		}
		
		int column = 4;
		for (i = 0; i < lastSheetTitle.length; i++) {
			if(i==0){
				cellStyle = writeNewData(lastSheetTitle[0],8,column,
						blue,SONG_TI, (short)9, white, 
						XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, 
						XlsxUtil.FONT_BOLD, XlsxUtil.BORDER_THIN, null,XlsxUtil.BORDER_ALL);
			}else{
				xlsxUtil.writeCellData(lastSheetTitle[i],8,column, cellStyle);
			}
			++column;
		}
	}//end init
	
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
						sumMediaModel = sumListData(mediaSumList,false);
						sumMediaModel.setMediaName(mediaName + "汇总");
						
						writeLineData(row, 1, sumMediaModel, lastSheetMediaMethods,setRangeCell(rangeCell,row,1,row,5));
						lastSumSheetStyle(row,true);
						//媒体汇总给活动汇总
						actSumList.add(sumMediaModel);
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
						
						writeLineData(row, 1, sumActModel, lastSheetActMethods,setRangeCell(rangeCell,row,1,row,5));
						lastSumSheetStyle(row,true);
						++row;
						startRow ++;
						
						//清空
						actSumList.clear();
					}
					actName = monthReportModel.getActivityName();
					
				}
				
				//写入mic数据
				writeLineData(row, 1, monthReportModel, lastSheetMethods);
				lastSumSheetStyle(row,false);
				
				//将点位信息放入媒体汇总List中
				mediaSumList.add(monthReportModel);
			}//end for
			
			row ++;
			sumMediaModel = sumListData(mediaSumList,false);
			actSumList.add(sumMediaModel);
			sumMediaModel.setMediaName(mediaName + "汇总");		
			writeLineData(row, 1, sumMediaModel, lastSheetMediaMethods,setRangeCell(rangeCell,row,1,row,5));
			lastSumSheetStyle(row,true);
			row ++;
			sumActModel = sumListData(actSumList,false);
			sumActModel.setActivityName(actName + "汇总");
			writeLineData(row, 1, sumActModel, lastSheetActMethods,setRangeCell(rangeCell,row,1,row,5));
			lastSumSheetStyle(row,true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}//end sumLastSheetData

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
 * @param format
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

/* 最后一个汇总页的样式
 * @param row
 * @param startColumn
 * @param endColumn
 * @param isSum
 */
public void lastSumSheetStyle(int row,boolean isSum){
	Map<String,XSSFCellStyle> cs = isSum ? sumCellStyles : cellStyles; 
	XSSFCellStyle cellStyle = null;
	
	for (int i = 1; i <= 23; i++) {
		
		if(i < 4){
			cellStyle = cs.get("songti");
		}
		else if(i == 4 || i == 5 || i == 22 || i == 23){
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
 * 媒体列表页的样式
 * @param row
 * @param startColumn
 * @param endColumn
 * @param isSum
 */
public void mediaSheetStyle(int row,boolean isSum){
	//项目名称	 媒体名称	广告位 投放形式
	
	Map<String,XSSFCellStyle> cs = isSum ? sumCellStyles : cellStyles; 
	XSSFCellStyle cellStyle = null;
	
	for (int i = 1; i <= 16; i++) {
		if(i <= 4){
			
			cellStyle = cs.get("songti");
		
		}
		else if(i == 6 || i == 5){
			cellStyle =cs.get("calibri");
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
 * 全模板媒体列表页的样式
 * @param row
 * @param startColumn
 * @param endColumn
 * @param isSum
 */
public void mediaSheetStyle2(int row,boolean isSum){
	//项目名称	 媒体名称	广告位 投放形式
	
	Map<String,XSSFCellStyle> cs = isSum ? sumCellStyles : cellStyles; 
	XSSFCellStyle cellStyle = null;
	if(xlsxUtil.isMergedRegion(row, 1)){
		for (int i = 1; i <= XlsxUtil.getColumnNum("Z"); i++) {
			
			if(i <= 7){
				
				cellStyle = cs.get("songti");
			
			}
			else if(i==24){
				cellStyle =cs.get("calibri");
			}
			else if(8 <= i && i <= 13 || (15<=i && i<=19)){
				cellStyle = cs.get("number");
			}
			else if(i==14||i==22||i==23 ||i==20){
				cellStyle = cs.get("percent");
			}
			else{
				cellStyle = cs.get("decimails");
			}
			
			xlsxUtil.cloneCellStyle(row, i, cellStyle);
		}
	}else{
	
	for (int i = 1; i <= XlsxUtil.getColumnNum("Z"); i++) {
		
		if(i <= 4){
			
			cellStyle = cs.get("songti");
		
		}
		else if(i == 6 || i == 5 || i==7 ||i==24){
			cellStyle =cs.get("calibri");
		}
		else if(8 <= i && i <= 13 || (15<i && i<19)){
			cellStyle = cs.get("number");
		}
		else if(i==14||i==22||i==23){
			cellStyle = cs.get("percent");
		}
		else{
			cellStyle = cs.get("decimails");
		}
		
		xlsxUtil.cloneCellStyle(row, i, cellStyle);
	}
	}
}//end mediaSheetStyle()

/**
 * 初始化单元格样式
 */
protected void initCellStyle(){
	
	//项目名称	 媒体名称	广告位 投放形式
	XSSFCellStyle songti = xlsxUtil.createCellStyle(null, null, XlsxUtil.FONT_NAME_SONGTI, (short)9, null, null, 
			XlsxUtil.ALIGN_LEFT, XlsxUtil.ALIGN_VERTICAL_CENTER, 
			XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
	
	cellStyles.put("songti", songti);
	
	//营销识别码	日期   URL
	XSSFCellStyle calibri = xlsxUtil.createCellStyle(null, null, XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
			XlsxUtil.ALIGN_LEFT, XlsxUtil.ALIGN_VERTICAL_CENTER, 
			XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
	
	cellStyles.put("calibri", calibri);
	
	//曝光预估	曝光次数	曝光人数	点击预估	点击次数	点击人数
	//访问次数	访问人数	到站点击	浏览量	跳出次数
	XSSFCellStyle number = xlsxUtil.createCellStyle(null, XlsxUtil.NUMBER_FORMAT_THOUSANDS, 
			XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
			XlsxUtil.ALIGN_RIGHT, XlsxUtil.ALIGN_VERTICAL_CENTER, 
			XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
	
	cellStyles.put("number", number);
	
	//点击率CTR 跳出率   曝光完成率	点击完成率
	XSSFCellStyle percent = xlsxUtil.createCellStyle(null, XlsxUtil.NUMBER_FORMAT_PERCENT, 
			XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
			XlsxUtil.ALIGN_RIGHT, XlsxUtil.ALIGN_VERTICAL_CENTER, 
			XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
	
	cellStyles.put("percent", percent);
	
	//平均访问时长(s)
	XSSFCellStyle decimails = xlsxUtil.createCellStyle(null, XlsxUtil.NUMBER_FORMAT_TWO_DECIMALS, 
			XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
			XlsxUtil.ALIGN_RIGHT, XlsxUtil.ALIGN_VERTICAL_CENTER, 
			XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
	
	cellStyles.put("decimails", decimails);
	
	
	int blue[] = new int[]{197,217,241};
	//媒体汇总  ，项目汇总
	songti = xlsxUtil.createCellStyle(blue, null, XlsxUtil.FONT_NAME_SONGTI, (short)9, null, null, 
			XlsxUtil.ALIGN_LEFT, XlsxUtil.ALIGN_VERTICAL_CENTER, 
			XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
	
	sumCellStyles.put("songti", songti);
	sumCellStyles.put("calibri", songti);
	
	//汇总
	//曝光预估	曝光次数	曝光人数	点击预估	点击次数	点击人数
	//访问次数	访问人数	到站点击	浏览量	跳出次数
	number = xlsxUtil.createCellStyle(blue, XlsxUtil.NUMBER_FORMAT_THOUSANDS, 
			XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
			XlsxUtil.ALIGN_RIGHT, XlsxUtil.ALIGN_VERTICAL_CENTER, 
			XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
			
	sumCellStyles.put("number", number);
	
	//点击率CTR 跳出率   曝光完成率	点击完成率
	percent = xlsxUtil.createCellStyle(blue, XlsxUtil.NUMBER_FORMAT_PERCENT, 
		XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
		XlsxUtil.ALIGN_RIGHT, XlsxUtil.ALIGN_VERTICAL_CENTER, 
		XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
	
	sumCellStyles.put("percent", percent);
	
	//平均访问时长(s)
	decimails = xlsxUtil.createCellStyle(blue, XlsxUtil.NUMBER_FORMAT_TWO_DECIMALS, 
		XlsxUtil.FONT_NAME_CALIBRI, (short)9, null, null, 
		XlsxUtil.ALIGN_RIGHT, XlsxUtil.ALIGN_VERTICAL_CENTER, 
		XlsxUtil.BORDER_THIN, XlsxUtil.BORDER_ALL);
	
	sumCellStyles.put("decimails", decimails);
	
	
}//end initCellStyle

	 
	//获得截止日期的前七天,
		 public static Date getNextDay(Date date) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DAY_OF_MONTH, -7);
				date = calendar.getTime();
				return date;
			}
		@Override
		public List<WeekDataExport> listMediaInfo(Date dt, String customer_id,
				String actCode) {
			
			return totalDao.listMediaInfo(dt,customer_id,actCode);
		}
	
}