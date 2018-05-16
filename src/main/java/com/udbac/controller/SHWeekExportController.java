package com.udbac.controller;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.entity.SHWeekDataExport;
import com.udbac.service.WeekReportService;
import com.udbac.util.FilePathManager;
import com.udbac.util.JSONUtil;

@Controller
@RequestMapping("/shweekExport")
public class SHWeekExportController {
	
	@Autowired
	WeekReportService weekService;
	
	@Autowired
	private FilePathManager filePathManager;
	
	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 上海周报导出
	 * @param request
	 * @param response
	 * @param stratDate 周报起始时间
	 * @param actCode 活动编号
	 * @param endDate 周报结束时间
	 * @param cusName 客户名称
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/exportSHWeek.do")
	public void listWeekInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "startDate", required = true) String stratDate,
			@RequestParam(value = "actCode", required = false) String actCode,
			@RequestParam(value = "cusName", required = true) String cusName,
			@RequestParam(value = "cusCode", required = true) String cusCode) throws Exception {
		Date dt = null;
		Date dt1 = null;
		Date dt2 = null;
		
		// 日期格式化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dt = sdf.parse(stratDate);
		
		// 根据开始时间算出自然周的结束日期
		Map<String, Object> cycle = RegoinExportController.getCycle(dt, true);
		dt1 = (Date) cycle.get("weekFirst");
		dt2 = (Date) cycle.get("weekLast");
		System.out.println(dt1+"     "+dt2);
		System.out.println(cusCode);
		//得到上海周报数据
	    List<SHWeekDataExport> info = weekService.listSHWeekInfo(dt1,dt2, actCode, cusCode);
	    
	    List<SHWeekDataExport> sumMediaInfo = weekService.listSHSumWeekInfo(dt1,dt2, actCode, cusCode);
	    
	    List listAddMergin = new ArrayList();
		for (int i = 0; i < info.size(); i++) {  //只有基础信息 第一个sheet页
			SHWeekDataExport de = info.get(i);
			if(de.getImpPv()==0&&de.getImpUv()==0&&de.getClkPv()==0&&de.getClkUv()==0&&de.getVisit()==null&&de.getVisitor()==null&&de.getPageView()==null&&de.getBounceTimes()==null){
				//去掉前后端数据为空的数据
			}else{
					listAddMergin.add(checkOut(de, false));
			}
		}
		
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("custName", cusName);
		mp.put("list", listAddMergin);
		mp.put("dt1", dt1);
		mp.put("dt2", dt2);
		
		mp.put("sumMediaInfo", sumMediaInfo); //媒体信息汇总
		
		createWb(mp,response);  //生成工作簿
	}

	/**
	 * 拼接数组	
	 * @param de
	 * @param unit
	 * @return
	 */
	public static String[] checkOut(SHWeekDataExport de,boolean unit){
		String bgwcl="";//曝光完成率
		String djwcl ="";//点击完成率
		String djyg ="";//点击预估
		String tcl="";//跳出率 
		String fwcs="";//访问次数
		String bgcs="";//曝光次数
		String djl="";//点击率CTR
		String dldjl="";//独立点击率CTR
		String bgyg="";//曝光预估
		String bgrs="";//曝光人数
		String djcs="";//点击次数
		String djrs="";//点击人数
		String fwrs="";//访问人数
		String clk = ""; //到站点击
		String lll="";//浏览量
		String tcsc="";//跳出次数
		String pjfwsj="";//平均访问时间
		DecimalFormat df0 = new DecimalFormat("#,##0");// 不显示小数点,千分位
		DecimalFormat df1 = new DecimalFormat("#,##0.00");// 显示两位小数点，千分位
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(de.getPutValue()==null||de.getPutValue()==0){
				bgwcl="#Div/0!";
				djwcl="#Div/0!";
				}else{
				  if(de.getClickAvg()==0){
					djyg ="0";//点击预估
					djwcl="#Div/0!";
				  }else if(de.getClickAvg() == null || "".equals(de.getClickAvg())){
					djyg="N/A";
					djwcl="N/A";
				 }else{
					djwcl=df1.format(de.getClkPv()/de.getClickAvg() * 100)+ "%";
					djyg=df0.format(de.getClickAvg());
					}
				}
			if( de.getExposureAvg() == -1 || "".equals(de.getExposureAvg()) || de.getExposureAvg()==null){
				bgyg="N/A";
				bgwcl="N/A";
			}else if(de.getExposureAvg()==0){
				bgyg="0";
				bgwcl="#Div/0!";
			}else{
				bgwcl=df1.format(de.getImpPv() /de.getExposureAvg() * 100)+"%";
				bgyg=df0.format(de.getExposureAvg()); //曝光预估
			}
			if(de.getImpPv()==0){
				bgcs="0";
				djl="#Div/0!";
				bgwcl="#VALUE!";
			}else if(de.getImpPv()==null){
				bgcs="N/A";
				djl="N/A";
				bgwcl="N/A";
			}else{
				djl=df1.format(de.getClkPv()/de.getImpPv()*100)+ "%";
				bgcs=df0.format(de.getImpPv()); //曝光次数
			}
			if(de.getImpUv()==0){
				bgrs="0";
				dldjl="#Div/0!";
			}else if(de.getImpUv()==null){
				bgrs="N/A";
				dldjl="N/A";
			}else{
				bgrs=df0.format(de.getImpUv()); //曝光人数
			}
			if(de.getClkPv()==0){
				djcs="0";
				djwcl="#VALUE!";
			}else if(de.getClkPv()==null){
				djcs="N/A";
				djwcl="N/A";
			}else{
				djcs=df0.format(de.getClkPv()); //点击次数
			}
			if(de.getClkUv()==0){
				djrs="0";
			}else if(de.getClkUv() == null){
				djrs="N/A";
			}else{
				djrs=df0.format(de.getClkUv()); //点击人数
				System.out.println(de.getImpUv());
				System.out.println(de.getImpUv()==0);
				if(de.getImpUv() == 0){
					dldjl = "#Div/0!";
				}else{
					dldjl=df1.format(de.getClkUv()/de.getImpUv()*100)+"%";
				}
			}
			if(de.getVisit()==null || de.getBounceTimes() ==null){
				fwcs="N/A";
				tcl="N/A";
			}else if(de.getVisit()==0){
				fwcs="0";
				tcl="#Div/0!";
			}else{
				tcl=df1.format(de.getBounceTimes()/de.getVisit()*100)+ "%";
				fwcs=df0.format(de.getVisit());//访问次数
			}
			if(de.getVisitor()==null){
				fwrs="N/A";
			}else if(de.getVisitor()==0){
				fwrs="0";
			}else{
				fwrs=df0.format(de.getVisitor());
			}
			if(de.getClick()==null){
				clk="N/A";
			}else if(de.getClick()==0){
				clk="0";
			}else{
				clk=df0.format(de.getClick());
			 }
			if(de.getPageView()==null){
				lll="N/A";
			}else if(de.getPageView()==0){
				lll="0";
			}else{
				lll=df0.format(de.getPageView());
			}
			if(de.getBounceTimes()==null){
				tcsc="N/A";
			}else if(de.getBounceTimes()==0){
				tcsc="0";
			}else{
				tcsc=df0.format(de.getBounceTimes());
			}
			if(de.getViewTime()==null){
				pjfwsj="N/A";
			}else if(de.getViewTime()==0){
				pjfwsj="0";
			}else{
				pjfwsj=df1.format(de.getViewTime());
			}
		 String[] checkout2 = {sdf.format(de.getStartDate()),sdf.format(de.getEndDate()),"N/A","N/A",de.getActivityName(),de.getMediaType(),
				 de.getMediaName(),de.getTerminalType(), bgcs,bgrs,djcs,djrs,"N/A","N/A",djl,dldjl,"N/A","N/A",tcl,pjfwsj,"N/A","N/A","N/A","N/A",lll,fwcs,
				 fwrs,tcsc,de.getPointLocation(), de.getMic(),bgyg,djyg,bgwcl,djwcl ,de.getUrlPc(),""};
		          return  checkout2;
		}
		
	/**
	 * 生成工作簿信息
	 * @param header
	 * @param workbook
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createWb(Map<String, Object> map,HttpServletResponse response) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("上海");
		XSSFSheet mediaSheet = workbook.createSheet("媒体汇总");
		sheet.setDisplayGridlines(false);// 设置无边框
		mediaSheet.setDisplayGridlines(false);// 设置无边框
		XSSFRow row0 = sheet.createRow(0);
		XSSFRow row1 = sheet.createRow(1);
		
		sheet.setColumnWidth(4,20 * 256);//设置第一列的列宽，估计值
		sheet.setColumnWidth(28,15 * 256);//设置第二列的列宽
		sheet.setColumnWidth(29,18 * 256);//存放的是广告位的数据
		sheet.setColumnWidth(34,25 * 256);//存放的是广告位的数据
		
		// 设置文本格式，文字大小
		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontName("calibri");
		font.setFontHeightInPoints((short) 9);// 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
		style.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		style.setFont(font);// 选择需要用到的字体格式

		XSSFCellStyle titleGreen = workbook.createCellStyle();
		XSSFFont titleFont = workbook.createFont();
		titleGreen.setFillForegroundColor(new XSSFColor(new Color(98, 128, 45))); // 表头的颜色
		titleGreen.setFillPattern(CellStyle.SOLID_FOREGROUND);
		titleGreen.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		titleGreen.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
		titleFont.setFontName("宋体");
		titleFont.setFontHeightInPoints((short) 9);// 设置字体大小
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
		titleGreen.setFont(titleFont);// 选择需要用到的字体格式

		XSSFCellStyle titleBlue = workbook.createCellStyle();
		titleBlue.setFillForegroundColor(new XSSFColor(new Color(123, 164, 219))); // 表头的颜色
		titleBlue.setFillPattern(CellStyle.SOLID_FOREGROUND);
		titleBlue.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		titleBlue.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
		titleBlue.setFont(titleFont);// 选择需要用到的字体格式
		
		XSSFCellStyle mediaStyle = workbook.createCellStyle();
		XSSFFont mediaFont = workbook.createFont();
		mediaFont.setFontName("宋体");
		mediaFont.setFontHeightInPoints((short) 9);// 设置字体大小
		mediaStyle.setFillForegroundColor(new XSSFColor(new Color(255, 255, 11))); // 表头的颜色
		mediaStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		mediaFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
		mediaStyle.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		mediaStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		mediaStyle.setFont(mediaFont);// 选择需要用到的字体格式

		// 1.设置中心内容文本格式，文字大小
		XSSFCellStyle styleContent = workbook.createCellStyle();
		XSSFFont font1 = workbook.createFont();
		font1.setFontName("宋体");
		font1.setFontHeightInPoints((short) 9);// 设置字体大小
		styleContent.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		styleContent.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		styleContent.setFont(font1);// 选择需要用到的字体格式
		
		// 2.设置中心内容文本格式，文字大小
		XSSFCellStyle styleContent2 = workbook.createCellStyle();
		XSSFFont font2 = workbook.createFont();
		font2.setFontName("calibri");
		font2.setFontHeightInPoints((short) 9);// 设置字体大小
		styleContent2.setAlignment(CellStyle.ALIGN_CENTER);//水平居中  
		styleContent2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中  
		styleContent2.setFont(font2);// 选择需要用到的字体格式
		
		//设置边框样式
		new DataExportController().setBoderStyle(style);
		new DataExportController().setBoderStyle(titleBlue);
		new DataExportController().setBoderStyle(titleGreen);
		new DataExportController().setBoderStyle(mediaStyle);
		new DataExportController().setBoderStyle(styleContent);
		new DataExportController().setBoderStyle(styleContent2);

		String[] header = { "开始日期", "结束日期", "活动类型", "活动子类", "活动名称", "媒体类别", "媒体名称", "投放终端", "广告曝光量", "独立曝光量", "广告点击量",
				"独立点击量", "广告到站量", "广告成本量", "点击率", "	独立点击率	", "到达率", "	跳出率", "	平均访问时长", "平均访问深度", "业务办理量",
				"	活动转化率	", "千次曝光成本", "点击成本", "到站成本", "进站PV", "访问人数", "跳出访次", "投放点位", "短代码", "预估曝光量", "预估点击量",
				"曝光完成率", "点击完成率", "备注" };
		
		// 设置宽高
		row0.setHeight((short) 395);
		row1.setHeight((short) 595);
		
		//设置标题头信息
		for (int i = 0; i < header.length; i++) {
			row0.createCell(i).setCellValue(i + 1);
			row1.createCell(i).setCellValue(header[i]);

			row0.getCell(i).setCellStyle(style);
			if (i >= 16 && i <= 27 || i == 12) {
				row1.getCell(i).setCellStyle(titleBlue);
			} else {
				row1.getCell(i).setCellStyle(titleGreen);
			}
		}

		//填充内容数据
		List<String[]> list = new ArrayList<String[]>();
		list = (List<String[]>) map.get("list");// 获得list对象，并且添加到excel中
		int rowIndex = 2;// 起始行
		for (int i = 0; i < list.size(); i++) {
			XSSFRow row = sheet.createRow(rowIndex++);
			row.setHeight((short) 495);// 目的是想把行高设置成24.75*20,poi转化为像素需要*20
			String[] str = list.get(i);
			for (int j = 0; j < str.length; j++) {
				XSSFCell cell = row.createCell(j);
				cell.setCellValue(str[j]);
				if(j<=34){
					if( j==2 || j==3 || j==4 ||j ==5 || j==6 || j==28 ){
						cell.setCellStyle(styleContent);
					}else{
						cell.setCellStyle(styleContent2);
					}
				}
			}
		}
		
		
		//第二个sheet媒体汇总
		XSSFRow rowTitle = mediaSheet.createRow(0);//设置表头样式
		rowTitle.createCell(0).setCellValue("媒体");
		rowTitle.createCell(1).setCellValue("广告曝光量");
		rowTitle.createCell(2).setCellValue("广告点击量");
		rowTitle.getCell(0).setCellStyle(mediaStyle);
		rowTitle.getCell(1).setCellStyle(mediaStyle);
		rowTitle.getCell(2).setCellStyle(mediaStyle);
		
		List<SHWeekDataExport> sumMediaInfo = (List) map.get("sumMediaInfo");
		for(int i=0;i<sumMediaInfo.size();i++){
			XSSFRow row = mediaSheet.createRow(i+1);
			row.createCell(0).setCellValue(sumMediaInfo.get(i).getMediaName());
			row.createCell(1).setCellValue(sumMediaInfo.get(i).getImpPv());
			row.createCell(2).setCellValue(sumMediaInfo.get(i).getClkPv());
			row.getCell(0).setCellStyle(styleContent);
			row.getCell(1).setCellStyle(styleContent2);
			row.getCell(2).setCellStyle(styleContent2);
		}
		
		//保存excel,并弹出下载框
		saveExcel("上海移动", workbook, response, (Date)map.get("dt1"), (Date)map.get("dt2"));
		
	}
	
	/**
	 * 保存文件到磁盘目录
	 * @param cusName
	 * @param workbook
	 * @return
	 */
	public Map<String, Object> saveExcel(String cusName, Workbook workbook, HttpServletResponse response, Date dt1,
			Date dt2) {
		Map<String, Object> map = new HashMap<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			StringBuffer buffer = new StringBuffer(50);
			if (filePathManager.getTmpExcel().isEmpty()) {
				File filePath = new File(filePathManager.toString());
				filePath.mkdirs();
			}
			buffer.append(filePathManager.getTmpExcel());
			buffer.append("week");
			buffer.append("/");
			buffer.append(sdf1.format(new Date()));
			buffer.append("/");
			File file = new File(buffer.toString());
			if (!file.exists() && !file.isDirectory()) {
				file.mkdirs();
			}
			String t1 = sd.format(dt1);
			String t2 = sd.format(dt2);
			String fileName = "项目周报_" + cusName + "互联网广告监测" + t1 + "_" + t2 + ".xlsx";
			buffer.append(fileName);
			FileOutputStream stream = new FileOutputStream(buffer.toString());
			map.put("savePath", buffer.toString());
			response.getWriter().write(JSONUtil.beanToJson(map));
			workbook.write(stream); // 输出到本地磁盘中的
			stream.flush();
			stream.close();
		} catch (Exception e) {
			map.put("message", "文件保存到本地失败");
			e.printStackTrace();
		}
		return map;
	}
}
