package com.udbac.controller;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.udbac.entity.DataExport;
import com.udbac.model.UserBean;
import com.udbac.service.DataExportService;
import com.udbac.util.FilePathManager;

/**
 * 
 * @author wangli
 * @date 2017-2-7 15:27:51
 * 集团品专报表导出类
 */
@Controller
@RequestMapping("/pzExport")
public class ProController {
	@Autowired
	DataExportService service;

	@Autowired
	private FilePathManager filePathManager;
	
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 //根据导出的页面内容定义的日期格式
	 //SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd");


	/**
	 * 验证输入的活动编号是否存在
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value="checkCode.do",method = RequestMethod.POST)
	public String sumName(@RequestParam(value = "actCode", required = true) String actCode,
			HttpServletResponse response, HttpServletRequest request) throws IOException{
		    Integer sumCode=service.checkCode(actCode.trim());
		    String falg="";
		    if(sumCode>0){
		    	falg="1";
			   }else{
				falg="2";
			   }
		    return falg;
	}
	/**
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@ResponseBody
	@RequestMapping(value="sumPZ.do",method = RequestMethod.POST)
	public String sumPZ(@RequestParam(value = "beginDate", required = true)String beginDate,@RequestParam(value = "endDate", required = true)String endDate,
			@RequestParam(value = "actCode", required = false)String actCode) throws ParseException{
		    Date dt = sdf.parse(beginDate);
		    Date dte = sdf.parse(endDate);
		    String falg="";
			Integer sumName=0;
			sumName=service.sumPZ(actCode.trim(),dt,dte);
		    if(sumName>0){
		    	falg="1";
			   }else{
				falg="2";
			   }
		    return falg;
		  
		}
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	@ResponseBody
	@RequestMapping("/exportPZ.do")
	public ModelAndView ListInfo(@RequestParam(value = "beginDate", required = true)String beginDate,@RequestParam(value = "endDate", required = true)String endDate,
			@RequestParam(value = "actCode", required = false)String actCode,HttpServletResponse response, HttpServletRequest request) throws IOException, ParseException{
		    Map map=new HashMap();
		    ArrayList<DataExport> infos = new ArrayList();  
		 	List list1 = new ArrayList();//存放的是标题(总的页面)
		 	List list2 = new ArrayList();//存放的是标题(分的页面)
		 	List list=new ArrayList();//存放的是内容(总的页面)
		 	List listDay=new ArrayList();//存放的是内容(每一天的)

		    DecimalFormat df0 = new DecimalFormat("###0");// 不显示小数点,千分位
			DecimalFormat df1 = new DecimalFormat("###0.00");// 显示两位小数点，千分位
			Date dt = sdf.parse(beginDate);
			Date dte=sdf.parse(endDate);//存放的是结束时间
		//获得图片的路径
		HttpSession session = request.getSession();
		String path="";
		String imgPath = session.getServletContext().getRealPath("/") + "images/logo.png";
		//项目名称
		//String proName=service.selectProName(actCode.trim());
		//页面填写的开始及结束时间
		String period=beginDate.replace("-", ".")+"-"+endDate.replace("-", ".");
		//sheet页的名称
		String sheetName=beginDate.substring(5, 10).replace("-", "")+"-"+endDate.substring(5, 10).replace("-", "");
		
		//投放周期
		//String realPeriod=service.selectRealPer(actCode.trim());
	
			path=  filePathManager.getTmpExcel() + "集团品专"+period+ ".xlsx";// 临时的文件目录
		
		
	   
		String[] title = { " 资源位置 "," 按钮名称 ","营销代码 ","广告监测数据","","活动网站监测","","","",""};
		String[] titles = { "","","", "曝光次数", "点击次数","访问次数", "访问人数",
				"浏览量","跳出次数",  "平均访问时长(s)"};
		list1.add(title);
		list1.add(titles);
		String[] titleFY = { " 资源位置 "," 按钮名称 ","营销代码 ","投放时间","广告监测数据","","活动网站监测","","","",""};
		String[] titleFYS = { "","","","", "曝光次数", "点击次数","访问次数", "访问人数",
				"浏览量","跳出次数",  "平均访问时长(s)"};
		list2.add(titleFY);
		list2.add(titleFYS);
		
		//品专总的
		ArrayList<DataExport> info = (ArrayList<DataExport>) service.listInfoPZ(actCode.trim(),dt,dte);
       for(int i=0;i<info.size();i++){
    	   DataExport de =info.get(i);
    	   String [] checkout={de.getTerminal_Type(),de.getPut_function(),de.getMic(),df0.format(de.getPv()),df0.format(de.get_pv()),
    			    df0.format(de.getA_vv()),df0.format(de.getA_uv()),df0.format(de.getA_pv()),df0.format(de.getBounce_t()),df1.format(de.getTime_s())};
           list.add(checkout);
       }
        //分页日数据：
       //先查询出来数据总的条数
//        Integer  list_count=service.sumPZ(actCode.trim(),dt,dte);
//        int page_size = 10000;// 定义每次查询的数据
//        //总数量除以每页显示条数等于页数
//        Integer  export_times = list_count % page_size > 0 ? list_count / page_size
//                + 1 : list_count / page_size;
//        //循环获取产生每页数据
//        for (int m = 0; m < export_times; m++) {
//        	ArrayList<DataExport> bean = (ArrayList<DataExport>) service.listPZDay(actCode.trim(),dt,dte,m);
// 		   for(int i=0;i<bean.size();i++){
// 	    	   DataExport de =bean.get(i);
// 	    	   String [] checkout={de.getTerminal_Type(),de.getPut_function(),de.getMic(),sdf.format(de.getPut_date()),df0.format(de.getPv()),df0.format(de.get_pv()),
// 	    			    df0.format(de.getA_vv()),
// 	    			    df0.format(de.getA_uv()),
// 	    			    df0.format(de.getA_pv()),
// 	    			    df0.format(de.getBounce_t()),
// 	    			    df1.format(de.getTime_s())};
// 	    	   listDay.add(checkout);
// 	       }
//        };
		

       
	    map.put("list", list);
	    map.put("list1", list1);
	    map.put("list2", list2);
	    map.put("listDay", listDay);
		map.put("imgPath", imgPath);
		map.put("path", path);
		//map.put("proName", proName);
		map.put("period", period);
		//map.put("realPeriod", realPeriod);
		map.put("sheetName", sheetName);
		WriteExcel(map);
		return this.LoadExcel(request, response, path);
		}
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation", "unused", "resource" })
	public void WriteExcel(Map map) throws IOException{
		String outputFile = (String) map.get("path");
		File file = new File(outputFile);
		try {
			FileOutputStream out = new FileOutputStream(outputFile);
			XSSFWorkbook workbook = new XSSFWorkbook();
			// 建立一张表格
		    XSSFSheet sheet = workbook.createSheet((String) map.get("sheetName")+"数据");
		 // 插入logo图片
		 	insetImages((String)map.get("imgPath"), workbook, sheet);
		 	sheet.setDisplayGridlines(false);// 设置无边框
			XSSFCellStyle style = workbook.createCellStyle();
			new DataExportController().setBoderStyle(style);
			style.setFillForegroundColor(new XSSFColor(new Color(55, 96, 145))); //表头的颜色
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			XSSFFont font2 = workbook.createFont();
			font2.setFontName("宋体");
			font2.setFontHeightInPoints((short) 9);// 设置字体大小
			font2.setColor(HSSFColor.WHITE.index);// 字体颜色
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
			style.setFont(font2);
			
			XSSFFont font = workbook.createFont();
			font.setFontName("calibri");
			font.setFontHeightInPoints((short) 9); // 9号字体
			XSSFCellStyle cellStyle= workbook.createCellStyle();
			new DataExportController().setBoderStyle(cellStyle);
			cellStyle.setFont(font);
			
		 // 单独的把表头提出来
		 			List<String[]> l1 = new ArrayList<String[]>();
		 			l1 = (List<String[]>) map.get("list1");// 获得list对象，并且添加到excel中
		 			int rowIndex = 6;// 起始行
		 			for (int i = 0; i < l1.size(); i++) {
		 				XSSFRow row = sheet.createRow(rowIndex++);
		 				row.setHeight((short) 450);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
		 				String[] str = l1.get(i);
		 				for (int j = 0; j < str.length; j++) {
		 					XSSFCell cell = row.createCell(j);
		 					cell.setCellValue(str[j]);
		 					cell.setCellStyle(style);
		 				}
		 			}
		 			
		 			l1 = (List<String[]>) map.get("list");// 获得list对象，并且添加到excel中
		 			 rowIndex = 8;// 起始行
		 			for (int i = 0; i < l1.size(); i++) {
		 				XSSFRow row = sheet.createRow(rowIndex++);
		 				row.setHeight((short) 450);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
		 				String[] str = l1.get(i);
		 				for (int j = 0; j < str.length; j++) {
		 					XSSFCell cell = row.createCell(j);
		 				  if(j>2){//将字符串类型转换为数值类型
		 						if(str[j]==null||str[j].equals("")){
		 							cell.setCellValue("");
		 							//数字格式有小数点连两位的
		 						}else if (str[j].equals("-1")||str[j].equals("-1.00")){
		 							cell.setCellValue("N/A");
		 						}else{
		 							cell.setCellValue(Double.parseDouble(str[j]));
		 						}
		 					}else{
		 						cell.setCellValue(str[j]);
		 					}
		 					cell.setCellStyle(cellStyle);
		 				}
		 			}
		 			
		 			 XSSFSheet sheet1 = workbook.createSheet("分天数据");
		 			// 插入logo图片
		 		 	insetImages((String)map.get("imgPath"), workbook, sheet);
		 			 // 单独的把表头提出来
			 			l1 = new ArrayList<String[]>();
			 			l1 = (List<String[]>) map.get("list2");// 获得list对象，并且添加到excel中
			 			 rowIndex = 6;// 起始行
			 			for (int i = 0; i < l1.size(); i++) {
			 				XSSFRow row = sheet1.createRow(rowIndex++);
			 				row.setHeight((short) 450);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
			 				String[] str = l1.get(i);
			 				for (int j = 0; j < str.length; j++) {
			 					XSSFCell cell = row.createCell(j);
			 					cell.setCellValue(str[j]);
			 					cell.setCellStyle(style);
			 				}
			 			}
			 			
			 			l1 = (List<String[]>) map.get("listDay");// 获得list对象，并且添加到excel中
			 			 rowIndex = 8;// 起始行
			 			for (int i = 0; i < l1.size(); i++) {
			 				XSSFRow row = sheet1.createRow(rowIndex++);
			 				row.setHeight((short) 450);// 目的是想把行高设置成22.5*20,poi转化为像素需要*20
			 				String[] str = l1.get(i);
			 				for (int j = 0; j < str.length; j++) {
			 					XSSFCell cell = row.createCell(j);
			 				  if(j>3){//将字符串类型转换为数值类型
			 				     if(str[j]==null||str[j].equals("")){
			 							cell.setCellValue("");
			 							//数字格式有小数点连两位的
			 						}else if (str[j].equals("-1")||str[j].equals("-1.00")){
			 							cell.setCellValue("N/A");
			 						}else{
			 							cell.setCellValue(Double.parseDouble(str[j]));
			 						}
			 					}else{
			 						cell.setCellValue(str[j]);
			 					}
			 					cell.setCellStyle(cellStyle);
			 				}
			 			}
			 			
		 			 
		 			 
		 			
					//设置表头
					setTitle(workbook, sdf, map);
					//设置宽度
					addMergedRegion(workbook);
					
		 			FileOutputStream fout = new FileOutputStream(outputFile);
					workbook.write(fout);
					fout.flush();
					fout.close();
		 			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	};

	/**
	 * 得到一个图片，插入到excel中
	 * @param ImgPath  //传入的图片路径
	 * @param workbook //所在的工作簿对象
	 * @param sheet //工作簿的第几个sheet页
	 */
	public 	static  void insetImages(String ImgPath, XSSFWorkbook workbook, XSSFSheet sheet) {
		try {
			InputStream is = new FileInputStream(ImgPath);
			byte[] bytes = IOUtils.toByteArray(is);

			int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

			CreationHelper helper = workbook.getCreationHelper();
			Drawing drawing = sheet.createDrawingPatriarch();
			ClientAnchor anchor = helper.createClientAnchor();

			// 图片插入坐标
			anchor.setRow1(1); // 第一行
			anchor.setCol1(7); // 第七列

			// 插入图片
			Picture pict = drawing.createPicture(anchor, pictureIdx);
			pict.resize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param obj
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unused")
	public ModelAndView LoadExcel(HttpServletRequest request, HttpServletResponse response, String obj) throws IOException {
		String path = obj.toString();
		try {
			// path是指欲下载的文件的路径。
			File file = new File(path);
			// 取得文件名。
			String filename = file.getName();
			// 取得文件的后缀名。
			String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			
			String agent = request.getHeader("USER-AGENT").toLowerCase();
		    //根据浏览器类型处理文件名称
		    if(agent.indexOf("msie")>-1){
		      //extfilename = Tools.toUtf8String(extfilename);
		    	filename = java.net.URLEncoder.encode(filename, "UTF-8");
		    }
		    else{  //firefox/safari不转码
		    	filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		    }
		    response.setContentType("application/msexcel");
		    // 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + DataExportController.toUtf8String(filename));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	/**
	 * 设置顶部的标题信息
	 * @param workbook
	 */
	public static void setTitle(XSSFWorkbook workbook,SimpleDateFormat sd,Map<String, Object> map){
		XSSFSheet sheet=null;
		 for(int i = 0; i < workbook.getNumberOfSheets(); i++){//获取每个Sheet表
			 sheet=(XSSFSheet) workbook.getSheetAt(i);
			//英文字体

				XSSFFont font = workbook.createFont();
				font.setFontName("calibri");
				font.setFontHeightInPoints((short) 9); // 9号字体
				//XSSFFont fonts = workbook.createFont();
				//fonts.setFontHeightInPoints((short) 9); // 9号字体
				//fonts.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
				CellStyle cellStyles = workbook.createCellStyle();
				cellStyles.setFont(font);
				//汉字9号字体
				CellStyle ceStyle = workbook.createCellStyle();
				XSSFFont fontStyle = workbook.createFont();
				fontStyle.setFontName("宋体");
				fontStyle.setFontHeightInPoints((short) 9);// 设置字体大小
				ceStyle.setFont(fontStyle);

				XSSFRow row1 = sheet.createRow(1);// 需要新建表格，来获取当前的信息
				XSSFRow row2 = sheet.createRow(2);
				XSSFRow row3 = sheet.createRow(3);
				XSSFRow row4 = sheet.createRow(4);

				XSSFCell cell1 = row1.createCell(0);
				XSSFCell cell2 = row2.createCell(0);
				XSSFCell cell3 = row3.createCell(0);
				XSSFCell cell7 = row4.createCell(0);
				XSSFCell cell5 = row2.createCell(1);
				XSSFCell cell6 = row3.createCell(1);
				XSSFCell cell8 = row4.createCell(1);
				
				//cell1.setCellValue( (String)map.get("proName"));
				//cell1.setCellStyle(cellStyles);
				//cell2.setCellValue("投放周期:");
				//cell2.setCellStyle(cellStyles);
				cell3.setCellValue("统计周期:");
				cell3.setCellStyle(cellStyles);
				cell7.setCellValue("数据来源:");
				cell7.setCellStyle(cellStyles);
				//cell5.setCellValue((String)map.get("period"));// 获得填写的日期信息
				//cell5.setCellStyle(cellStyles);
				cell6.setCellValue((String) map.get("period"));
				cell6.setCellStyle(cellStyles);
				cell8.setCellValue("UDBAC");
				cell8.setCellStyle(cellStyles);
		 }
		
	}

	@SuppressWarnings("deprecation")
	public static void addMergedRegion(XSSFWorkbook workbook){
		 XSSFSheet sheet=null;
		 for(int i = 0; i < workbook.getNumberOfSheets(); i++){//获取每个Sheet表
			 sheet=(XSSFSheet) workbook.getSheetAt(i);
			 sheet.setColumnWidth(1, 25 * 256);// 设置第一列的列宽，估计值
			 sheet.setColumnWidth(2, 20 * 256);// 设置第二列的列宽
			 if(i==0){

		 			sheet.addMergedRegion(new CellRangeAddress(6, 7, 0, 0));
		 			sheet.addMergedRegion(new CellRangeAddress(6, 7, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(6, 7, 2, 2));
					sheet.addMergedRegion(new CellRangeAddress(6, 6, 3, 4));
					sheet.addMergedRegion(new CellRangeAddress(6, 6, 5, 9)); 
			 }else{
				 sheet.addMergedRegion(new CellRangeAddress(6, 7, 0, 0));
		 			sheet.addMergedRegion(new CellRangeAddress(6, 7, 1, 1));
					sheet.addMergedRegion(new CellRangeAddress(6, 7, 2, 2));
					sheet.addMergedRegion(new CellRangeAddress(6, 7, 3, 3));
					sheet.addMergedRegion(new CellRangeAddress(6, 6, 4, 5));
					sheet.addMergedRegion(new CellRangeAddress(6, 6, 6, 10)); 
			 }
			
		 }
	}
	/**
	 * 
	 * @param tqrq
	 * @param mic
	 * @param response
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="endPoints.do",method = RequestMethod.POST)
	public String endPoints(@RequestParam(value = "tqrq", required = true)String tqrq,
			@RequestParam(value = "mic", required = false)String mic,HttpServletResponse response, HttpServletRequest request) throws ParseException{
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String mailbox=user.getMAILBOX();
		Date dt = sdf.parse(tqrq);
		  String falg=service.saveEndPoints(dt,mailbox,mic);
		    return falg;
		  
		}
	
	/**
	 * 验证输入的短代码是否存在
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value="checkMic.do",method = RequestMethod.POST)
	public String sumMic(@RequestParam(value = "mic", required = true) String mic,
			HttpServletResponse response, HttpServletRequest request) throws IOException{
		    Integer sumMic=service.checkMic(mic.trim());
		    String falg="";
		    if(sumMic>0){
		    	falg="1";
			   }else{
				falg="2";
			   }
		    return falg;
	}
	
}

