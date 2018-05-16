package com.udbac.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.stereotype.Service;

import com.udbac.model.MonthMediaSumList;
import com.udbac.model.Mics;
import com.udbac.model.MonthReportModel;
import com.udbac.model.ReportCellTitle;
import com.udbac.service.MonthDayReportService;
import com.udbac.util.DateUtil;
import com.udbac.util.XlsxUtil;

/**
 *	自然月结报告，日报的维度报告
 */
@Service
public class MonthDayReportServiceImpl extends MonthReportServiceImpl implements MonthDayReportService{
	
	@Override
	public String monthReportList(String userName,String cusCode, String dateStr,String monthType,String monthLocation,Integer monthFile,
			Integer monthSummaryData) {
		
		String[] sdateAndEdate = getSdateAndEdate(dateStr);
		Date smonth = DateUtil.getDate(sdateAndEdate[0], null);
		Date emonth = DateUtil.getDate(sdateAndEdate[1], null);
		
		List<MonthReportModel> monthList = monthReportDao.listMonth(cusCode, smonth, emonth, monthType,monthFile);
		
		String reportPath = getMonthReportPath(monthList, dateStr);
		newFile(reportPath);
		initCellStyle();
		
		Map<String, MonthMediaSumList> groupMedia = groupMedia(monthList);
		writeMonthDataToXlsx(groupMedia, monthSummaryData,userName,dateStr);
		
		xlsxUtil.close(null);
		
		return reportPath;
	}
	

	/**
	 * 初始化报告信息
	 */
	private static List<ReportCellTitle> titles = new LinkedList<>();
	static {
		titles.add(new ReportCellTitle("项目名称：", 1,1, null));
		titles.add(new ReportCellTitle("报表周期：", 2, 1, null));
		titles.add(new ReportCellTitle("交付日期：", 3, 1, null));
		titles.add(new ReportCellTitle("交付人：", 4, 1, null));
		titles.add(new ReportCellTitle("注：N/A表示不可用/不适用，一般表示此处值未获取或不可使用。", 4, 4, null));
		
		titles.add(new ReportCellTitle("项目名称", 6, 1, new int[]{6,1,7,1}));
		titles.add(new ReportCellTitle("媒体名称", 6, 2, new int[]{6,2,7,2}));
		titles.add(new ReportCellTitle("广告位", 6,3, new int[]{6,3,7,3}));
		titles.add(new ReportCellTitle("投放形式", 6, 4, new int[]{6,4,7,4}));
		titles.add(new ReportCellTitle("投放日期", 6, 5,new int[]{6,5,7,5}));
		titles.add(new ReportCellTitle("营销代码", 6, 6,new int[]{6,6,7,6}));
		titles.add(new ReportCellTitle("投放类型", 6, 7, new int[]{6,7,7,7}));
		titles.add(new ReportCellTitle("广告前端曝光点击监测", 6, 8, new int[]{6,8,6,14}));
		titles.add(new ReportCellTitle("曝光预估", 7, 8, null));
		titles.add(new ReportCellTitle("曝光次数", 7, 9, null));
		titles.add(new ReportCellTitle("曝光人数", 7, 10, null));
		titles.add(new ReportCellTitle("点击预估", 7,11, null));
		titles.add(new ReportCellTitle("点击次数", 7,12, null));
		titles.add(new ReportCellTitle("点击人数", 7,13, null));
		titles.add(new ReportCellTitle("点击率CTR", 7,14, null));
		titles.add(new ReportCellTitle("活动网站监测", 6, 15, new int[]{6,15,6,21}));
		titles.add(new ReportCellTitle("访问次数", 7, 15, null));
		titles.add(new ReportCellTitle("访问人数", 7,16, null));
		titles.add(new ReportCellTitle("到站点击", 7,17, null));
		titles.add(new ReportCellTitle("浏览量", 7,18, null));
		titles.add(new ReportCellTitle("跳出次数", 7,19, null));
		titles.add(new ReportCellTitle("跳出率", 7,20, null));
		titles.add(new ReportCellTitle("平均访问时长(s)", 7,21, null));
		titles.add(new ReportCellTitle("完成率情况", 6, 22, new int[]{6,22,6,23}));
		titles.add(new ReportCellTitle("曝光完成率", 7,22, null));
		titles.add(new ReportCellTitle("点击完成率", 7, 23, null));
		titles.add(new ReportCellTitle("URL", 6, 24, new int[]{6,24,7,24}));
		titles.add(new ReportCellTitle("备注", 6, 25, new int[]{6,25,7,25}));
	}
	
	/**
	 * 初始化报告模板
	 */
	@Override
	public void initMonthReport(){
		//左上角信息
		XSSFCellStyle leftTitle = xlsxUtil.createCellStyle(WHITE, null, SONG_TI, FONT_9, null, XlsxUtil.FONT_BOLD, XlsxUtil.ALIGN_LEFT, XlsxUtil.ALIGN_VERTICAL_CENTER, XlsxUtil.BORDER_THIN,null);
		
		//小标题
		XSSFCellStyle topTitle = xlsxUtil.createCellStyle(BLUE, null, SONG_TI, FONT_9, WHITE, XlsxUtil.FONT_BOLD, XlsxUtil.ALIGN_CENTER, XlsxUtil.ALIGN_VERTICAL_CENTER, XlsxUtil.BORDER_THIN,XlsxUtil.BORDER_ALL);
		
		xlsxUtil.setDisplayGridlines(false);
		
		ReportCellTitle reportCellTitle = null;
		int[] range = null;
		
		for (int i = 0; i < titles.size(); i++) {
			reportCellTitle = titles.get(i);
			
			//写入数据
			if(i < 5){
				xlsxUtil.writeCellData(reportCellTitle.getTitle(), reportCellTitle.getRow(),reportCellTitle.getColumn(), leftTitle);
			}else{
				xlsxUtil.writeCellData(reportCellTitle.getTitle(), reportCellTitle.getRow(),reportCellTitle.getColumn(), topTitle);
			}
			
			//设置合并单元格
			range = reportCellTitle.getRange();
			if(range != null){
				xlsxUtil.setRangeCell(range[0], range[1], range[2], range[3]);
			}
		}//end for
	}//end initMonthReport
	
	/**
	 * 对点位进行按媒体分组
	 * @param monthList
	 */
	public Map<String, MonthMediaSumList> groupMedia(List<MonthReportModel> monthList){
		
		MonthReportModel monthReport;
		MonthMediaSumList mediaReport;
		
		//媒体集合
		Map<String, MonthMediaSumList> mediaMap = new HashMap<>();
		
		try{
			for (int i = 0; i < monthList.size(); i++) {
				monthReport = monthList.get(i);
				mediaReport = mediaMap.get(monthReport.getMediaName());
			
				//如果媒体集合中没有找到媒体，则进行新建媒体
				if(mediaReport == null){
					mediaReport = new MonthMediaSumList(monthReport.getMediaName());
					mediaMap.put(monthReport.getMediaName(), mediaReport);
				}
				mediaReport.addReport2Mics(monthReport);
			}//end for
		}catch(Exception e){
			e.printStackTrace();
		}
		return mediaMap;
	}//end monthList
	

	private static String[] detailMethod = {"getActivityName","getMediaName","getPointLocation","getPutFunction","getPutDate","getMic","getTerminalType"
			,"getExposureAvg","getExposurePV","getExposureUV","getClickAvg","getClickPV","getClickUV",""
			,"getVisit","getVisitor","getClick","getPageView","getBounceTimes","","getViewTime","","","getUrl"};

	/**
	 * 将媒体分组之后的数据写入文件
	 * @param groupMedia
	 * @return
	 */
	public int writeMonthDataToXlsx(Map<String, MonthMediaSumList> groupMedia,Integer monthSummaryData,String userName,String dateStr){
		
		int sheetNum = 0;
		int startColumn = 1;
		int startRow = 8;
		int row = startRow;
		
		try {
			Set<String> keySet = groupMedia.keySet();
			Iterator<String> iterator = keySet.iterator();
			String mediaName;
			MonthMediaSumList sumList;
			
			//遍历媒体数据
			while (iterator.hasNext()) {
				mediaName = iterator.next();
				createSheet(sheetNum++,mediaName);
				sumList = groupMedia.get(mediaName);
				List<Mics> micsList = sumList.getMicsList();
				
				row = startRow;
				Mics mics;
				
				int groupid = 0;
				Mics summics = null;
				
				//遍历点位数据
				for (int i = 0; i < micsList.size(); i++) {
					mics = micsList.get(i);
					
					//如果是不同的分组，则将点位汇总写回文件。
					if(groupid != 0 && groupid != mics.getGroupId()){
						writeGroupMicsSumData(summics, row, startColumn, monthSummaryData);
						row++;
						groupid = 0;
					}
					
					//将分组点位放到一块
					if(mics.getGroupId() != 0){
						//判断分组点位是否已经初始化。
						if(groupid == 0){
							groupid = mics.getGroupId();
							summics = new Mics(null, groupid, null);
							summics.setStartWriteRow(row);
//							summics.getMicList().addAll(mics.getMicList());
						}
						
						//否则，是相同的分组。
						summics.getMicList().addAll(mics.getMicList());
					}//end 点位汇总
					
					//写入mic数据
					mics.setStartWriteRow(row);
					row = writeMicsData(mics.getMicList(), row,startColumn, detailMethod);
					row++;
					
					//媒体中需要合并的点位预估
					if(sumList.isMergeByGroupId()){
						mergeGroupMicsData(mics);
						countGroupMicsData(mics);
					}
					
					//没有分组的点位汇总
					if(mics.getGroupId() == 0)
					{
						writeMicsSumData(mics, row, startColumn,monthSummaryData);
						row++;
					}
					
					//点位不需要合并的完成率计算
					if(mics.getGroupId() == 0){
						eachNotGroupMicCTR(mics);
					}
					//分点位汇总
				}//end for
				
				//媒体中没有合并的点位预估
				if(!sumList.isMergeByGroupId()){
					writeNotGroupMeidaSumData(sumList, row, startColumn);
				}else{
					if(groupid != 0){
						writeGroupMicsSumData(summics, row, startColumn, monthSummaryData);
						row++;
					}
					writeGroupMeidaSumData(sumList, row, startColumn,monthSummaryData);
				}
				
				//写入样式
				writeLeftTop(sumList, userName, dateStr);
				setCellStyle(startRow);
			}//end while
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sheetNum;
	}//writeMonthDataToXlsx

	
	/**
	 * 写入左上角报表交付信息
	 * 			咪咕善跑9月结案数据
				2016/9/22-9/30
				2016/10/21
				陈小米
	 */
	public void writeLeftTop(MonthMediaSumList sumList,String userName,String dateStr){
		
		List<Mics> micsList = sumList.getMicsList();
		MonthReportModel reportModel = micsList.get(0).getMicList().get(0);
		String projectName = reportModel.getCustomerName() +
				DateUtil.getDateStr(DateUtil.getDate(dateStr,null) ,"mm") + "月结案数据";
		String period = "";
		String putDate = DateUtil.getDateStr(new Date(),"yyyy/MM/dd");
		
		Date minDate = reportModel.getPutDate() ,maxDate = reportModel.getPutDate();
		
		//查找最大以及最小的日期
		for (int i = 0; i < micsList.size(); i++) {
			Mics mics = micsList.get(i);
			List<MonthReportModel> micList = mics.getMicList();
			for (int j = 0; j < micList.size(); j++) {
				reportModel = micList.get(j);
				
				minDate =  minDate.compareTo(reportModel.getPutDate()) > 0 ? reportModel.getPutDate() : minDate;
				maxDate =  maxDate.compareTo(reportModel.getPutDate()) < 0 ? reportModel.getPutDate() : maxDate;
			}
		}//end for 
		
		period = DateUtil.getDateStr(minDate, "yyyy/MM/dd") + "-" + DateUtil.getDateStr(maxDate, "yyyy/MM/dd");
		
		XSSFCellStyle songti = xlsxUtil.createCellStyle(null, null, SONG_TI, FONT_9, null, null, null, null, null, null);
		XSSFCellStyle calibri = xlsxUtil.createCellStyle(null, null, CALIBRI, FONT_9, null, null, null, null, null, null);
		xlsxUtil.writeCellData(projectName, 1, 2,songti);
		xlsxUtil.writeCellData(period, 2, 2,calibri);
		xlsxUtil.writeCellData(putDate, 3, 2,calibri);
		xlsxUtil.writeCellData(userName, 4, 2,songti);
	}//end writeLeftTop
	
	
	/**
	 * 设置报告样式
	 * @param startRow
	 */
	public void setCellStyle(int startRow){
		int j,i;
		int lastCol = XlsxUtil.getColumnNum("Z");
		
		Map<String,XSSFCellStyle> cs = cellStyles;
		
		for (i = startRow; i <= xlsxUtil.getLastRow(); i++) {
			
			boolean isMerge = xlsxUtil.isMergedRegion(i, 1);
			//如果第一列是合并单元格，则为汇总行
			cs = isMerge ? sumCellStyles : cellStyles;
			
			String cellName = "songti";
			
			for (j = 1; j <= lastCol; j++) {
				
				if(j <= 4 || j == 25){
					cellName = "songti";
				}
				else if((5 <= j && j <= 7 && !isMerge) || j == 24){
					cellName = "calibri";
				}
				else if((8 <= j && j<= 13) || (15 <= j && j <= 19)){
					cellName = "number";
				}
				else if(j == 21){
					cellName = "decimails";
				}
				else{
					cellName = "percent";
				}
//				System.out.println(cellName + "===" + cs.get(cellName));
				xlsxUtil.cloneCellStyle(i, j, cs.get(cellName));
			}//end for
		}
	}
	
		
	
	private static final int[] MERGE_COLUMN = {XlsxUtil.getColumnNum("I"),XlsxUtil.getColumnNum("L"),
		XlsxUtil.getColumnNum("O"),XlsxUtil.getColumnNum("W"),XlsxUtil.getColumnNum("X")};
	/**
	 * 合并相同分组的点位
	 * @param mics
	 */
	public void mergeGroupMicsData(Mics mics){
		if(mics.getGroupId() == 0){
			return;
		}
		
		int startWriteRow = mics.getStartWriteRow();
		int endRow = mics.getStartWriteRow() + mics.getMicList().size() - 1;
		
		for (int i = 0; i < MERGE_COLUMN.length; i++) {
			xlsxUtil.setRangeCell(startWriteRow, MERGE_COLUMN[i], endRow, MERGE_COLUMN[i]);
		}
	}//end mergeGroupMicsData
	
	/**
	 * =(J9+J10)/I9	//曝光完成率
	 * =(M9+M10)/L9 //点击完成率
	 * =(M9+M10)/(J10+J9) //点击率
	 */
	public void countGroupMicsData(Mics mics){
		int startRow = mics.getStartWriteRow();
		List<MonthReportModel> micList = mics.getMicList();
		
		StringBuffer clickSB = new StringBuffer();
		StringBuffer expSB = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		
		for (int i = 1; i <= micList.size();i++) {
			append(clickSB,CLICK_COLUMN,startRow + i,"+");
			append(expSB,EXP_COLUMN,startRow + i,"+");
		}
		clickSB.replace(clickSB.length() - 1,clickSB.length(), "");	//M9+M10
		expSB.replace(expSB.length() - 1,expSB.length(), "");	//J9+J10
		
		//点击率
		writeFormulaData(sb, startRow, XlsxUtil.getColumnNum(CLICK_CTR_COLUMN), 
				"(",clickSB,")/(",expSB,")");
		//点击完成率
		writeFormulaData(sb, startRow, XlsxUtil.getColumnNum(CLICK_FINISH_CTR_COLUMN), 
				"(",clickSB,")/",CLICK_AVG_COLUMN,startRow + 1);
		//曝光完成率
		writeFormulaData(sb, startRow, XlsxUtil.getColumnNum(EXP_FINISH_CTR_COLUMN), 
				"(",expSB,")/",EXP_AVG_COLUMN,startRow + 1);
		//跳出率 //=IFERROR(T18/P18,"N/A")
		writeFormulaData(sb, startRow, XlsxUtil.getColumnNum(BOUNCE_RATE_COLUMN), 
				"IFERROR(",BOUNCE_TIMES_COLUMN,startRow + 1,"/",VISIT_COLUMN,startRow + 1,",\"N/A\")");
	}//end countGroupMicsData
	
	/**
	 * 在指定单元格中写入公式
	 * @param sb
	 * @param writeRow
	 * @param writeColumn
	 * @param formulaDatas
	 */
	private void writeFormulaData(StringBuffer sb,int writeRow,int writeColumn,Object ... formulaDatas){
		append(sb, formulaDatas);
		xlsxUtil.writeFormulaData(sb.toString(),writeRow, writeColumn);
		sb.delete(0, sb.length());
	}
	
	/**
	 * 追加数据
	 * @param sb
	 * @param datas
	 */
	private void append(StringBuffer sb,Object... datas){
		for (int i = 0; i < datas.length; i++) {
			sb.append(datas[i]);
		}
	}
	
	private void createSheet(int sheetNum,String sheetName){
		xlsxUtil.setSheet(sheetNum, sheetName);
		initMonthReport();
	}
	
	/**
	 * 给每个短代码写上  点击完成率，跳出率，曝光完成率，点击完成率
	 * @param mics Mic集合
	 */
	private void eachNotGroupMicCTR(Mics mics){
		int startWriteRow = mics.getStartWriteRow();
		List<MonthReportModel> micList = mics.getMicList();
		
		StringBuffer sb = new StringBuffer(20);
		
		for (int i = 0; i < micList.size(); i++) {
			iferror(CTR_LIST,startWriteRow + i, sb);
		}
	}//end eachMicCTR
	
	/**
	 * 写入点位数据
	 * @param monthList
	 * @param startRow
	 * @param methods
	 * @return
	 */
	public int writeMicsData(List<MonthReportModel> monthList,int startRow,int startColumn,String[] methods){
		
		int row = startRow;
		MonthReportModel model = null;
		System.out.println(monthList.size()+"=====");
		try {
			for (int i = 0; i < monthList.size(); i++) {
				row = startRow + i;
				
				model = monthList.get(i);
				writeLineData(row, startColumn, 1, model, methods);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return row;
	}//end writeMicsData
	
	
	//完成率相关列
	private static final List<String[]> CTR_LIST = new LinkedList<>(); 
	static {
		CTR_LIST.add(new String[]{"M","J","O"});	//点击率
		CTR_LIST.add(new String[]{"T","P","U"});	//跳出率
		CTR_LIST.add(new String[]{"J","I","W"}); //曝光完成率
		CTR_LIST.add(new String[]{"M","L","X"}); //点击完成率
	}
	//汇总相关列
	private static char[] TOTAL_COLUMNS = {'I','J','K','L','M','N','P','Q','R','S','T'};
	/**
	 * 短代码汇总
	 * @param monthList
	 * @param startRow
	 * @param startColumn
	 * @param methods
	 * @param monthSummaryData 是否按照频控汇总
	 * @return
	 */
	public void writeMicsSumData(Mics mics,int startRow,int startColumn,int monthSummaryData){
//		=SUBTOTAL(9,J81:J116)
//		=IFERROR(N117/K117,"N/A")
//		=AVERAGE(V9:V26)
		int startWriteRow = mics.getStartWriteRow() + 1;
		List<MonthReportModel> micList = mics.getMicList();
		
		StringBuffer startRowSB = new StringBuffer();
		subtotal(TOTAL_COLUMNS, startWriteRow,(startWriteRow + micList.size() - 1), startRow, startRowSB);
		
		//取频控汇总数据
		if(monthSummaryData == 1){
			MonthReportModel reportModel = micList.get(0);
			xlsxUtil.writeCellData(reportModel.getSumClickPV(), startRow, XlsxUtil.getColumnNum(CLICK_COLUMN));
			xlsxUtil.writeCellData(reportModel.getSumClickUV(), startRow, XlsxUtil.getColumnNum(CLICK_UV_COLUMN));
			xlsxUtil.writeCellData(reportModel.getSumExposurePV(), startRow, XlsxUtil.getColumnNum(EXP_COLUMN));
			xlsxUtil.writeCellData(reportModel.getSumExposureUV(), startRow, XlsxUtil.getColumnNum(EXP_UV_COLUMN));
		}
		
		//短代码
		writeRangMessage(mics.getMicList().get(0).getMic() + " 汇总", startRow);
		
		iferror(CTR_LIST, startRow, startRowSB);
		
//		AVERAGE(V9:V26)  平均访问时长
		writeFormulaData(startRowSB, startRow, XlsxUtil.getColumnNum(VIEW_TIME_COLUMN), 
				"AVERAGE(",VIEW_TIME_COLUMN,startWriteRow,":",VIEW_TIME_COLUMN,startWriteRow + micList.size() - 1,")");
	}//end writeMicsSumData
	
	
	/**
	 * 分组点位汇总
	 * @param monthList
	 * @param startRow
	 * @param startColumn
	 * @param methods
	 * @param monthSummaryData 是否按照频控汇总
	 * @return
	 */
	public void writeGroupMicsSumData(Mics mics,int startRow,int startColumn,int monthSummaryData){
//		=SUBTOTAL(9,J81:J116)
//		=IFERROR(N117/K117,"N/A")
//		=AVERAGE(V9:V26)
		int startWriteRow = mics.getStartWriteRow() + 1;
		List<MonthReportModel> micList = mics.getMicList();
		
		StringBuffer startRowSB = new StringBuffer();
		subtotal(TOTAL_COLUMNS, startWriteRow,(startWriteRow + micList.size() - 1), startRow, startRowSB);
		
		//取频控汇总数据
		if(monthSummaryData == 1){
			String mic = "";
			int i = 0 ;
			
			MonthReportModel reportModel = micList.get(i);
			int[] sumDatas = new int[4];
			
			//汇总
			while(!mic.equals(reportModel.getMic())){
				mic = reportModel.getMic();
				
				sumDatas[0] += reportModel.getSumClickPV();
				sumDatas[1] += reportModel.getSumClickUV();
				sumDatas[2] += reportModel.getSumExposurePV();
				sumDatas[3] += reportModel.getSumExposureUV();
				
				i++;
				micList.get(i);
			}
			
			xlsxUtil.writeCellData(sumDatas[0], startRow, XlsxUtil.getColumnNum(CLICK_COLUMN));
			xlsxUtil.writeCellData(sumDatas[1], startRow, XlsxUtil.getColumnNum(CLICK_UV_COLUMN));
			xlsxUtil.writeCellData(sumDatas[2], startRow, XlsxUtil.getColumnNum(EXP_COLUMN));
			xlsxUtil.writeCellData(sumDatas[3], startRow, XlsxUtil.getColumnNum(EXP_UV_COLUMN));
		}
		
		//短代码
		writeRangMessage("         汇总", startRow);
		
		iferror(CTR_LIST, startRow, startRowSB);
		
//		AVERAGE(V9:V26)  平均访问时长
		writeFormulaData(startRowSB, startRow, XlsxUtil.getColumnNum(VIEW_TIME_COLUMN), 
				"AVERAGE(",VIEW_TIME_COLUMN,startWriteRow,":",VIEW_TIME_COLUMN,startWriteRow + micList.size() - 1,")");
	}//end writeMicsSumData
	

	/**
	 * 汇总需要合并预估的点位的媒体
	 * @param sumList
	 * @param startRow
	 * @param startColumn
	 * @param monthSummaryData
	 */
	public void writeGroupMeidaSumData(MonthMediaSumList sumList,int startRow,int startColumn,int monthSummaryData){
		
		List<Mics> micsList = sumList.getMicsList();
		int firstRow = micsList.get(0).getStartWriteRow() + 1;
		int lastRow = micsList.get(micsList.size() - 1).getStartWriteRow() + micsList.get(micsList.size() - 1).getMicList().size();
		
		writeRangMessage(sumList.getMediaName() + " 汇总", startRow);
		
		//汇总数据 =SUM(J9:J26)
		StringBuffer rowSb = new StringBuffer(100);
		Mics mics;
		List<MonthReportModel> reports;
		int i,j,k;
		
		int startWriteRow ;
		mics = micsList.get(0);
		
		for ( k = 0; k < TOTAL_COLUMNS.length; k++) {
//			for (j = 0; j < micsList.size(); j++) {
//				mics = micsList.get(j);
//				reports = mics.getMicList();
//				startWriteRow = mics.getStartWriteRow();
//				
//				if(mics.getGroupId() == 0){
//					append(rowSb,TOTAL_COLUMNS[k], startWriteRow + reports.size() + 1,"+");
//				}else{
//					for (i = 0; i < reports.size(); i++) {
//						append(rowSb,TOTAL_COLUMNS[k], startWriteRow + i + 1,"+");
//					}
//				}
//			}//end for
			startWriteRow = mics.getStartWriteRow();
			
			for (; startWriteRow < xlsxUtil.getLastRow(); startWriteRow++) {
				if(xlsxUtil.isMergedRegion(startWriteRow, 1)){
					append(rowSb,TOTAL_COLUMNS[k], startWriteRow + 1,"+");
				}
			}
			rowSb.replace(rowSb.length() - 1, rowSb.length(), "");
			
			writeFormulaData(rowSb, startRow, XlsxUtil.getColumnNum(String.valueOf(TOTAL_COLUMNS[k])));
		}
		
		//需要频控汇总数据
		if(monthSummaryData == 1){
			writeGroupMediaSumDataPCPVUVPC(micsList, startRow);
		}//if(monthSummaryData == 1)
		
		//完成率
		iferror(CTR_LIST, startRow, rowSb);
		
		//平均访问时长  =AVERAGE(V9:V26)
		
		startWriteRow = mics.getStartWriteRow();
		rowSb.append("AVERAGE(");
		
		for (; startWriteRow < xlsxUtil.getLastRow(); startWriteRow++) {
			if(xlsxUtil.isMergedRegion(startWriteRow, 1)){
				append(rowSb,VIEW_TIME_COLUMN, startWriteRow + 1,"+");
			}
		}
		
		rowSb.replace(rowSb.length() - 1, rowSb.length(), "");
		writeFormulaData(rowSb, startRow,XlsxUtil.getColumnNum(VIEW_TIME_COLUMN),")");
	}
	
	
	/**
	 * 合并点位媒体 需要频控的媒体数据汇总
	 * @param micsList
	 * @param startRow
	 */
	public void writeGroupMediaSumDataPCPVUVPC(List<Mics> micsList,int startRow){
		//Map<短代码,{PC,UC,PV,UV}>
		Map<String,int[]> map = new HashMap<>();
		MonthReportModel model;
		Mics mics;
		List<MonthReportModel> micList;
		
		//提取所有点位的频控汇总数据
		for (int i = 0; i < micsList.size() ;i++) {
			mics = micsList.get(i);
			micList = mics.getMicList();
			
			for (int j = 0; j < micList.size(); j++) {
				model = micList.get(j);
				map.put(model.getMic(), new int[]{model.getSumClickPV(),model.getSumClickUV(),model.getSumExposurePV(),model.getSumExposureUV()});
			}
		}//end for
		
		//汇总点位数据
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		
		int[] sumData = new int[]{0,0,0,0};
		while (iterator.hasNext()) {
			int[] is = map.get(iterator.next());
			sumData[0] += is[0];
			sumData[1] += is[1];
			sumData[2] += is[2];
			sumData[3] += is[3];
		}
		xlsxUtil.writeCellData(sumData[0], startRow, XlsxUtil.getColumnNum(CLICK_COLUMN));
		xlsxUtil.writeCellData(sumData[1], startRow, XlsxUtil.getColumnNum(CLICK_UV_COLUMN));
		xlsxUtil.writeCellData(sumData[2], startRow, XlsxUtil.getColumnNum(EXP_COLUMN));
		xlsxUtil.writeCellData(sumData[3], startRow, XlsxUtil.getColumnNum(EXP_UV_COLUMN));
	}//end  writeGroupMediaSumDataPCPVUVPC 
	
	
	/**
	 * 没有合并单元格的媒体汇总
	 * @param sumList
	 * @param startRow
	 * @param startColumn
	 */
	public void writeNotGroupMeidaSumData(MonthMediaSumList sumList,int startRow,int startColumn){
		
		writeRangMessage(sumList.getMediaName() + " 汇总", startRow);
		
		List<Mics> micsList = sumList.getMicsList();
		List<Integer> sumRow = new LinkedList<>();
		StringBuffer sb =  new StringBuffer(30);
		int i,j;
		Mics mics = null;
		
		//哪些行是需要合并的
		for (i = 0; i < micsList.size(); i++) {
			mics = micsList.get(i);
			sumRow.add(Integer.valueOf(mics.getStartWriteRow() + mics.getMicList().size() + 1));
		}
		
//		=L25+L23+L21+L19+L15+L17+L13+L11+L9
		for (i = 0; i < TOTAL_COLUMNS.length; i++) {
			for (j = 0; j < sumRow.size(); j++) {
				append(sb, TOTAL_COLUMNS[i],sumRow.get(j),"+");
			}
			sb.replace(sb.length() - 1, sb.length(),"");

			writeFormulaData(sb, startRow, XlsxUtil.getColumnNum(String.valueOf(TOTAL_COLUMNS[i])));
		}//end for
		
		//完成率
		iferror(CTR_LIST, startRow, sb);
		
		//平均访问时长
		sb.append("AVERAGE(");
		for (j = 0; j < sumRow.size(); j++) {
			append(sb, VIEW_TIME_COLUMN,sumRow.get(j),"+");
		}
		sb.replace(sb.length() - 1, sb.length(),")");
		
		writeFormulaData(sb,startRow, XlsxUtil.getColumnNum(VIEW_TIME_COLUMN));
	}//end writeNotGroupMeidaSumData

	private static int columnB = XlsxUtil.getColumnNum("B");
	private static int columnH = XlsxUtil.getColumnNum("H");
	/**
	 * 写入汇总前面的信息
	 * @param data		
	 * @param startRow
	 */
	private void writeRangMessage(String data,int startRow){
		xlsxUtil.setRangeCell(startRow,columnB , startRow, columnH);
		xlsxUtil.writeCellData(data, startRow,columnB);
	}
	
	/**
	 * 写入完成率公式
	 * =IFERROR(N117/K117,"N/A")
	 * List<String[]{"M","J","O"}> // 【0】：分子 【1】：分母 【2】：写入位置
	 * @param ctrList		公式说涉及的列
	 * @param startRow		数据所在行
	 * @param startRowSB	缓存
	 */
	private void iferror(List<String[]> ctrList,int startRow,StringBuffer startRowSB){
		String[] arrs ;
		for (int i = 0; i < ctrList.size(); i++) {
			arrs = ctrList.get(i);
			
			writeFormulaData(startRowSB, startRow, XlsxUtil.getColumnNum(arrs[2]), 
					"IFERROR(",arrs[0],startRow + 1,"/",arrs[1],startRow + 1,",\"N/A\")");
		}
	}//end iferror
	
	/**
	 * 汇总数据
	 * =SUBTOTAL(9,J81:J116)
	 * @param columns			汇总所在的列
	 * @param startWriteRow		汇总开始的行
	 * @param endRow			汇总结束的列
	 * @param writeRow			写入的行
	 * @param startRowSB
	 */
	private void subtotal(char[] columns,int startRow,int endRow,int writeRow,StringBuffer startRowSB){
		for (int i = 0; i < columns.length; i++) {
			writeFormulaData(startRowSB, writeRow, XlsxUtil.getColumnNum(String.valueOf(columns[i])), 
					"SUBTOTAL(9,",columns[i],startRow,":",columns[i],endRow,")");
		}
	}//end subtotal
	
}