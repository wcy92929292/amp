package com.udbac.util;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 周报
 * @author han
 * 2016年9月28日 下午4:02:05
 */
public class WeekUtil {

	
	/**
	 * 设置边框样式
	 * @param style
	 */
	public static void setBoderStyle(XSSFCell cell,XSSFCellStyle style,int align){
		//设置边框样式
	     style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	     style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	     style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	     //设置边框颜色
	     style.setTopBorderColor(HSSFColor.BLACK.index);
	     style.setBottomBorderColor(HSSFColor.BLACK.index);
	     style.setLeftBorderColor(HSSFColor.BLACK.index);
	     style.setRightBorderColor(HSSFColor.BLACK.index);
		if(align == 0){ //居左
			//设置位置居中
	    	 style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平
		     style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		     cell.setCellStyle(style);
		}else if(align == 1){ //居中
			 //设置位置居中
	    	 style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		     style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		     cell.setCellStyle(style);
		}else{
			//设置位置居中
	    	 style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平
		     style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		     cell.setCellStyle(style);
		}
	}
	
	
	/**
	 * 给第几个工作簿设置不同的样式
	 * @param workbook
	 * @param sheet
	 * @param num
	 */
	public static void setStyle(XSSFWorkbook workbook,XSSFCell cell,int num){
		XSSFCellStyle style;
		style = workbook.createCellStyle();
		
		//设置边框样式
	     style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	     style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	     style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	     style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	     //设置边框颜色
	     style.setTopBorderColor(HSSFColor.BLACK.index);
	     style.setBottomBorderColor(HSSFColor.BLACK.index);
	     style.setLeftBorderColor(HSSFColor.BLACK.index);
	     style.setRightBorderColor(HSSFColor.BLACK.index);
	     
		if(num == 0){
			XSSFFont font1 = workbook.createFont();
			font1.setFontName("calibri");
			font1.setFontHeightInPoints((short) 9);// 设置字体大小
			style.setFont(font1);// 选择需要用到的字体格式
			WeekUtil.setBoderStyle(cell,style,0);
			CreationHelper helper = workbook.getCreationHelper();
			style.setDataFormat(helper.createDataFormat().getFormat("yyyy-MM-dd"));// 设置日期的格式信息
			cell.setCellStyle(style);
		}else if(num == 1){
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
			style.setFillForegroundColor(new XSSFColor(new Color(55, 96, 145))); //表头的颜色
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			XSSFFont font2 = workbook.createFont();
			font2.setFontName("宋体");
			font2.setFontHeightInPoints((short) 9);// 设置字体大小
			font2.setColor(HSSFColor.WHITE.index);// 字体颜色
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			style.setFont(font2);
			cell.setCellStyle(style);
		}else if(num==3){//汇总的格式
			 XSSFFont fontHui = workbook.createFont();
			 fontHui.setFontName("calibri");
			 fontHui.setFontHeightInPoints((short) 9);// 设置字体大小
			 //fontHui.setColor(HSSFColor.WHITE.index);// 字体颜色
			 fontHui.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			 style.setFont(fontHui);// 选择需要用到的字体格式
			 WeekUtil.setBoderStyle(cell,style,0);
			 style.setFillForegroundColor(new XSSFColor(new Color(219, 229, 241)));//汇总 的背景色
			 style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			 cell.setCellStyle(style);
		}
	}
	
	
	/**   
     * 判断指定的单元格是否是合并单元格   
     * @param sheet    
     * @param row 行下标   
     * @param column 列下标   
     * @return   
     */   
     public static boolean isMergedRegion(XSSFWorkbook workbook,int row ,int column) { 
    	 XSSFSheet sheet=null;
		 for(int j = 0; j < workbook.getNumberOfSheets(); j++){
			 int sheetMergeCount = sheet.getNumMergedRegions();   
		       for (int i = 0; i < sheetMergeCount; i++) {   
		             CellRangeAddress range = (CellRangeAddress) sheet.getMergedRegion(i);   
		             int firstColumn = range.getFirstColumn(); 
		             int lastColumn = range.getLastColumn();   
		             int firstRow = range.getFirstRow();   
		             int lastRow = range.getLastRow();   
		             if(row >= firstRow && row <= lastRow){ 
		                     if(column >= firstColumn && column <= lastColumn){ 
		                        return true;   
		                     } 
		             }   
		       }
		 }
       return false;   
     } //end isMergedRegion
	
    /**
     * 给合并的单元格行元素设置样式
     * @param workbook
     * @param row
     * @param column
     */
	public static void setMergedRegionStyle(XSSFWorkbook workbook,int row,int column,XSSFCell cell,int num){
		boolean b = WeekUtil.isMergedRegion(workbook,row, column);
		if(b){ //是合并的单元格
			WeekUtil.setStyle(workbook, cell, num);
		}
	}
	
	
	
	/**
	 * 给所有的sheet设置相同的列宽
	 * @param workbook
	 */
	public static void setColumnWidth(XSSFWorkbook workbook){
		 XSSFSheet sheet=null;
		 for(int i = 0; i < workbook.getNumberOfSheets(); i++){//获取每个Sheet表
			 sheet=(XSSFSheet) workbook.getSheetAt(i);
			 sheet.setColumnWidth(1, 20 * 256);// 设置第一列的列宽，估计值
			 sheet.setColumnWidth(2, 15 * 256);// 设置第二列的列宽
			 sheet.setColumnWidth(3, 10 * 256);//存放的是广告位的数据
			 sheet.setColumnWidth(4, 20 * 256);
			 sheet.setColumnWidth(5, 15 * 256);
			 sheet.setColumnWidth(6, 15 * 256);
			 sheet.setColumnWidth(24, 25 * 256);
			 sheet.setColumnWidth(26, 0 * 256);
		 }
	}
	
	
	/**
	 * 设置无边框
	 * @param workbook
	 */
	public static void setDisplayGridlines(XSSFWorkbook workbook){
		 XSSFSheet sheet=null;
		 for(int i = 0; i < workbook.getNumberOfSheets(); i++){//获取每个Sheet表
			 sheet = (XSSFSheet) workbook.getSheetAt(i);
			 System.out.println(i);
			 sheet.setDisplayGridlines(false);// 设置无边框
		 }
	}
	
	/**
	 * 设置表头合并信息
	 * @param workbook
	 */
	@SuppressWarnings("deprecation")
	public static void addMergedRegion(XSSFWorkbook workbook){
		 XSSFSheet sheet=null;
		 for(int i = 0; i < workbook.getNumberOfSheets(); i++){//获取每个Sheet表
			 sheet=(XSSFSheet) workbook.getSheetAt(i);
			//设置表头的合并单元格信息
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 6, 14)); //广告曝光监测
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 15, 21)); //活动网站监测
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 22, 23));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 4, 4));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 5, 5));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 24, 24));
			sheet.addMergedRegion(new CellRangeAddress(7, 8, 25, 25));
		 }
	}
	
	/**
	 * 设置顶部的标题信息
	 * @param workbook
	 */
	public static void setTitle(XSSFWorkbook workbook,SimpleDateFormat sd,Map<String, Object> map){
		
		//英文字体
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 9); // 9号字体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		
		
		//日期字体
		XSSFFont font2 = workbook.createFont();
		font2.setFontName("calibri");
		font2.setFontHeightInPoints((short) 9); // 9号字体
		CellStyle cellStyle2 = workbook.createCellStyle();
		cellStyle2.setFont(font2);
		
		
		//汉字9号字体
		CellStyle ceStyle = workbook.createCellStyle();
		XSSFFont fontStyle = workbook.createFont();
		fontStyle.setFontName("宋体");
		fontStyle.setFontHeightInPoints((short) 9);// 设置字体大小
		ceStyle.setFont(fontStyle);
		
		XSSFSheet sheet=null;
		 for(int i = 0; i < workbook.getNumberOfSheets(); i++){//获取每个Sheet表
			 sheet=(XSSFSheet) workbook.getSheetAt(i);
			 XSSFRow row1 = sheet.createRow(2);// 需要新建表格，来获取当前的信息
			 XSSFRow row2 = sheet.createRow(3);
			 XSSFRow row3 = sheet.createRow(4);
			 XSSFRow row4 = sheet.createRow(5);
			 XSSFCell cell1 = row1.createCell(1);
			 XSSFCell cell2 = row2.createCell(1);
			 XSSFCell cell3 = row3.createCell(1);
			 XSSFCell cell4 = row3.createCell(2);
			 XSSFCell cell5 = row2.createCell(2);
			 XSSFCell cell6 = row1.createCell(2);
			 XSSFCell cell7 = row4.createCell(1);
			 XSSFCell cell8 = row4.createCell(2);
			 
			 cell1.setCellValue("项目名称:");
			 cell1.setCellStyle(cellStyle);
			 cell2.setCellValue("报表周期:");
			 cell2.setCellStyle(cellStyle);
			 cell3.setCellValue("交付日期:");
			 cell3.setCellStyle(cellStyle);
			 cell4.setCellValue(sd.format(new Date()));
			 cell4.setCellStyle(cellStyle2);
			 cell5.setCellValue( sd.format((Date)map.get("dt1"))+"-"+sd.format((Date)map.get("dt2")));// 获得填写的日期信息
			 cell5.setCellStyle(cellStyle2);
			 cell6.setCellValue((String) map.get("custName"));
			 cell6.setCellStyle(ceStyle);
			 cell7.setCellValue("交付人:");
			 cell7.setCellStyle(cellStyle);
			 cell8.setCellValue((String)map.get("username"));
			 cell8.setCellStyle(ceStyle);
		 }
	}
	
	
	/**
	 * 保存文件到磁盘目录
	 * @param cusName
	 * @param workbook
	 * @return
	 */
	public static Map<String, Object> saveExcel(String cusName, Workbook workbook, HttpServletResponse response, Date dt1,
			Date dt2,FilePathManager filePathManager,SimpleDateFormat sd) {
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
		}
		return map;
	}
	
	/**
	 * 得到一个图片，插入到excel中
	 * @param ImgPath 传入的图片路径
	 * @param workbook 所在的工作簿对象
	 * @param sheet 工作簿的第几个sheet页
	 */
	public static void insetImages(String ImgPath, XSSFWorkbook workbook) {
		try {
			 XSSFSheet sheet = null;
			 for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
	             sheet=(XSSFSheet) workbook.getSheetAt(i);
	             InputStream is = new FileInputStream(ImgPath);
	 			byte[] bytes = IOUtils.toByteArray(is);
	 			int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
	 			CreationHelper helper = workbook.getCreationHelper();
	 			Drawing drawing = sheet.createDrawingPatriarch();
	 			ClientAnchor anchor = helper.createClientAnchor();
	 			// 图片插入坐标
	 			anchor.setRow1(2); // 第一行
	 			anchor.setCol1(20); // 第14列
	 			// 插入图片
	 			Picture pict = drawing.createPicture(anchor, pictureIdx);
	 			pict.resize();
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置成数字类型并以公式输出
	 * @param workbook
	 */
	public static void setNumricValue(Workbook workbook,Map<String,Object> map){
		 XSSFSheet sheet=null;
		 DataFormat format = workbook.createDataFormat();
		 String value = "";
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
             sheet=(XSSFSheet) workbook.getSheetAt(i);
             for (int j = 9; j < sheet.getLastRowNum()+1; j++) {//获取每行
            	 XSSFRow row=sheet.getRow(j);
            	 
                for(int k=1;k<24;k++){ //起始列和结束列
//                	
                	value = row.getCell(k).getStringCellValue();
//                	
	                if(k==14 || k==20 || k==22 || k==23){
//	                	XSSFCellStyle cellStyle2 = row.getCell(k).getCellStyle(); 
////	                	
////	                	System.out.println("value=========="+value);
////	                	
//	                	if(!value.equals("N/A") && !value.equals("#Div/0!") && !value.equals("#VALUE!")){
//	                		
//	                            cellStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
//	                            row.getCell(k).setCellStyle(cellStyle2);
//	                            XSSFFont font = (XSSFFont) workbook.createFont();
//	                			font.setFontName("calibri");
//	                			font.setFontHeightInPoints((short) 9);// 设置字体大小
//	                			cellStyle2.setFont(font);
//	                            setBoderStyle( row.getCell(k),cellStyle2,2);
//	                            if(!value.equals("") && !(value == null)){
//	                            	row.getCell(k).setCellValue(Double.parseDouble(value));
//	                            }
//                		}else{
//                			cellStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平
//                			cellStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
//                			row.getCell(k).setCellStyle(cellStyle2);
//                		}
//
//	                	XSSFFont fontNumRed = (XSSFFont)workbook.createFont();
//                		fontNumRed.setColor(Font.COLOR_RED);
//        				
////                		if(i==0){ //只获取前两个页的，设置标红规则
////                			if((String)map.get("tcl") != null && !"".equals((String)map.get("tcl"))  
////                					&& (String)map.get("ctrs") != null && !"".equals((String)map.get("ctrs"))
////                					&& (String)map.get("ctre") != null && !"".equals((String)map.get("ctre"))
////                					&& (String)map.get("bwcls") != null && !"".equals((String)map.get("bwcls")) 
////                					&& (String)map.get("bwcle") != null && !"".equals((String)map.get("bwcle"))  
////                					&& (String)map.get("dwcls") != null && !"".equals((String)map.get("dwcls")) 
////                					&& (String)map.get("dwcle") != null && !"".equals((String)map.get("dwcle"))){
////                				if(isDouble(value)){ //是一个浮点数
////                					System.out.println("11111111");
////                					if(k==XlsxUtil.getColumnNum("O") && (String)map.get("ctrs") != null && !"".equals((String)map.get("ctrs")) && (String)map.get("ctre") != null && !"".equals((String)map.get("ctre"))){//点击率
////                                    	if(Double.parseDouble(value)*100 >= Double.parseDouble((String)map.get("ctrs")) && Double.parseDouble(value)*100 <= Double.parseDouble((String)map.get("ctre"))){
////                	                	}else{
////                	                		cellStyle2.setFont(fontNumRed);
////                	                	}
////                                    }else if(k==XlsxUtil.getColumnNum("U") && (String)map.get("tcl") != null && !"".equals((String)map.get("tcl"))){//跳出率
////                                    	if(Double.parseDouble(value)*100 <= Double.parseDouble((String)map.get("tcl"))){
////                	                	}else{
////                	                		cellStyle2.setFont(fontNumRed);
////                	                	}
////                                    }else if(k==XlsxUtil.getColumnNum("W") && (String)map.get("bwcls") != null && !"".equals((String)map.get("bwcls")) && (String)map.get("bwcle") != null && !"".equals((String)map.get("bwcle"))){//曝光完成率
////                                    	if(Double.parseDouble(value)*100 >= Double.parseDouble((String)map.get("bwcls")) && Double.parseDouble(value)*100 <= Double.parseDouble((String)map.get("bwcle"))){
////                	                	}else{
////                	                		cellStyle2.setFont(fontNumRed);
////                	                	}
////                                    }else if(k==XlsxUtil.getColumnNum("X") && (String)map.get("dwcls") != null && !"".equals((String)map.get("dwcls")) && (String)map.get("dwcle") != null && !"".equals((String)map.get("dwcle"))){//点击完成率
////                                    	if(Double.parseDouble(value)*100 >= Double.parseDouble((String)map.get("dwcls")) && Double.parseDouble(value)*100 <= Double.parseDouble((String)map.get("dwcle"))){
////                	                	}else{
////                	                		cellStyle2.setFont(fontNumRed);
////                	                	}
////                                    }
////        	                	}else{
////        	                		System.out.println("2222222");
////        	                		cellStyle2.setFont(fontNumRed);
////        	                	}
////                			}
////                		}
            		}else{ //纯数值列
            			if(k>=8&&k<24){
            				XSSFCellStyle style = row.getCell(k).getCellStyle();
                    		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
                    		style.setDataFormat(format.getFormat("#,##0")); 
                    		setBoderStyle(row.getCell(k),style,2);
                    		if(!value.equals("N/A") && !value.equals("#Div/0!") && !value.equals("#VALUE!")){
                    			row.getCell(k).setCellValue(Double.parseDouble(value));
                    		}else{
                    			row.getCell(k).setCellStyle(style);
                    		}
            			}
            		}
                }
          }
       }
	}
	
	/**
	 * 判断字符串是否是浮点数
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains("."))
				return true;
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
