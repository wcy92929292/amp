package com.udbac.controller;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.entity.RegionDataExport;
import com.udbac.entity.RegionSum;
import com.udbac.entity.RegionSum2;
import com.udbac.entity.RegionTimes;
import com.udbac.model.UserBean;
import com.udbac.service.RegionReportService;
import com.udbac.util.DateUtil;
import com.udbac.util.FilePathManager;
import com.udbac.util.JSONUtil;

/**
 * 地域频次导出 2016-06-22
 * 
 * @author han
 */

@SuppressWarnings({ "unused" })
@Controller
@RequestMapping("/regoinExport")
public class RegoinExportController {

	@Autowired
	RegionReportService service;

	@Autowired
	private FilePathManager filePathManager;

	/**
	 * 根据当前用户的角色分不同的权限
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getUserInfoSeesion.do", method = RequestMethod.POST)
	public @ResponseBody void getUserInfoSeesion(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBean user;
		Map<String, Object> map = new HashMap<>();
		try {
			user = (UserBean) request.getSession().getAttribute("user");
			// 取得user的userID
			map.put("userRoleId", user.getROLE_ID()); // 得到当前用户的角色ID和所对应的客户ID，与页面的客户ID匹配，对应不同的操作
			map.put("customerId", user.getCustomerId());
			String msg = JSONUtil.beanToJson(map);
			response.setContentType("text/html;charset=UTF-8");
			// 设置不使用缓存
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(msg);
		} catch (Exception e) {
			response.getWriter().print("error");
		}
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/getProvince.do")
	public List getProvince(){
		List<RegionDataExport> list = service.getAllProvice();
		return list;
		
	} 
	/**
	 * 地域及频次数据导出
	 * @param request
	 * @param response
	 * @param date 选择的日期
	 * @param regoin 选择的是周还是月的
	 * @param cusName 客户名称
	 * @param actCode 活动编号
	 * @param map
	 */
	@SuppressWarnings("resource")
	@ResponseBody
	@RequestMapping("/regoin.do")
	public void listRegoinInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "regoin", required = false) String regoin,
			@RequestParam(value = "regoinProvince", required = false) String cusName,
			@RequestParam(value = "actCode", required = false) String actCode, 
			@RequestParam(value = "customerName", required = true) String customerName, 
			/*@RequestParam(value = "endDateCity",required = false) String endDateCity,*/
			@RequestParam(value = "city",required = true) String city,
			Map<String, Object> map){
			Map<String, Object> newMap = new HashMap<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = null;
			Date endDate = null;
			Date monthStartDate = null;
			Date monthEndDate = null;
			Date edate=null;
			try {
				if (date != null && date != "") { // 日期非空判断
					if(city.equals("0")){
					if ("1".equals(regoin)) { // 周
						newMap = getCycle(sdf.parse(date), true);
						startDate = (Date) newMap.get("weekFirst"); //周起始日期
						endDate = (Date) newMap.get("weekLast"); //周结束日期
					} else if ("2".equals(regoin)) { // 月
						newMap = getCycle(sdf.parse(date), false);
						monthStartDate = (Date) newMap.get("monthFirst"); //月起始日期
						monthEndDate = (Date) newMap.get("monthLast"); //月结束日期
					} else {
						map.put("regionError", "频次选择不正确!");
						response.getWriter().write(JSONUtil.beanToJson(map));
					}
					}
				}else{
					map.put("dateError", "日期不能为空!");
					response.getWriter().write(JSONUtil.beanToJson(map));
				}
				if(!"".equals(cusName) && cusName != null) {
					cusName = URLDecoder.decode(cusName, "UTF-8"); // 解码
				}else{
					map.put("cusNameError", "投放省份选择不正确!");
					response.getWriter().write(JSONUtil.beanToJson(map));
				}
				HttpSession session = request.getSession();
				String imgPath = session.getServletContext().getRealPath("/") + "images/logo.png";
				List<RegionDataExport> info = null;
				// 生成工作簿
				XSSFWorkbook workbook = new XSSFWorkbook();
				String title = cusName+"地域频次";
				try{
					if(city.equals("1")){
						 if("1".equals(regoin)){
								edate=DateUtil.getWeekDay(sdf.parse(date));
							}else if("2".equals(regoin)){
								edate=DateUtil.getMonthDay(sdf.parse(date));
						}
						info = service.listRegionInfoCity(sdf.parse(date),edate,actCode, cusName,customerName);
					}else{
						if("1".equals(regoin)){
							info = service.listRegionInfo(startDate,endDate, actCode, cusName,customerName);
						}else if("2".equals(regoin)){
							info = service.listRegionMonthInfo(monthStartDate,monthEndDate, actCode, cusName,customerName);
						}
					}
					 if(info.size() == 0 ){ //没查询到数据
						 response.setContentType("text/plain;charset=UTF-8");
						 map.put("len","数据结果为0条!");
						 response.getWriter().write(JSONUtil.beanToJson(map));
					 }else{//有数据的时候才创建workbook
						 if(city.equals("0")){
							 if ("1".equals(regoin)){
								 	workbook = exportExcel(workbook, title, info, startDate, endDate, cusName); // 生成excel
								 	saveExcel(cusName, startDate,endDate,workbook,response); // 保存excel
								}else if("2".equals(regoin)){
									workbook = exportExcel(workbook, title, info, monthStartDate, monthEndDate, cusName); // 生成excel
									saveExcel(cusName,monthStartDate,monthEndDate, workbook,response); // 保存excel
								}
						 }else{
							 if("1".equals(regoin)){
									edate=DateUtil.getWeekDay(sdf.parse(date));
								}else if("2".equals(regoin)){
									edate=DateUtil.getMonthDay(sdf.parse(date));
								}
							    workbook = exportExcel(workbook, title, info, sdf.parse(date),edate, cusName); // 生成excel
							 	saveExcel(cusName, sdf.parse(date),edate,workbook,response); // 保存excel
						 }
						}
				}catch(Exception e) {
					String dbError = "程序异常,请联系维护人员！!";
					e.printStackTrace();
					response.getWriter().write(JSONUtil.beanToJson(map.put("dberror", dbError)));
				}
			 }catch(Exception e){
				   e.printStackTrace();
		    }
	    }
	/**
	 * （累计）地域及频次数据导出
	 * @param request
	 * @param response
	 * @param date 选择的日期
	 * @param cusName 客户名称
	 * @param actCode 活动编号
	 * @param map
	 */
	@SuppressWarnings("resource")
	@ResponseBody
	@RequestMapping("/regoin2.do")
	public void listRegoinInfo2(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "date", required = true) String date,
			@RequestParam(value = "regoinProvince", required = false) String cusName,
			@RequestParam(value = "actCode", required = false) String actCode, 
			@RequestParam(value = "customerName", required = true) String customerName, 
			@RequestParam(value = "city", required = true) String city, 
			Map<String, Object> map){
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date aDate = null;
			Date eDate = null;
		try{	
			if(!"".equals(cusName) && cusName != null) {
				cusName = URLDecoder.decode(cusName, "UTF-8"); // 解码
			}else{
				map.put("cusNameError", "投放省份选择不正确!");
				response.getWriter().write(JSONUtil.beanToJson(map));
			}
			HttpSession session = request.getSession();
//			String imgPath = session.getServletContext().getRealPath("/") + "images/logo.png";
			List<RegionDataExport> info = null;
			List<RegionSum2> sum = null;
//			String radio ="1";
			// 生成工作簿
			XSSFWorkbook workbook = new XSSFWorkbook();
			String title = cusName+"地域累计频次";
			aDate = sdf.parse(date);
			eDate = addDateOneDay(aDate);
			if(city.equals("1")){
				info = service.listRegionInfo2City(eDate, actCode, cusName,customerName);
			}else{
				info = service.listRegionInfo2(eDate, actCode, cusName,customerName);
			}
//			sum = service.listSumRegionInfo2(eDate, actCode, cusName,customerName);
			 if(info.size() == 0){ //没查询到数据
				 response.setContentType("text/plain;charset=UTF-8");
				 map.put("len","数据结果为0条!");
				 response.getWriter().write(JSONUtil.beanToJson(map));
			 }else{//有数据的时候才创建workbook
				 
				 workbook = exportExcel2(workbook, title, info, aDate, aDate, cusName); // 生成excel
				 saveExcel(cusName, aDate,aDate,workbook,response); // 保存excel
			}
			
	}catch(Exception e){
		   e.printStackTrace();
 }
}
	//日期往后推一天
	public static Date addDateOneDay(Date date) {  
        if (null == date) {  
            return date;  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(date);   //设置当前日期  
        c.add(Calendar.DATE, 1); //日期加1天  
//     c.add(Calendar.DATE, -1); //日期减1天  
        date = c.getTime();  
        return date;  
    }

	/**
	 * 保存文件到磁盘目录
	 * @param cusName
	 * @param workbook
	 * @return
	 */
	public Map<String, Object> saveExcel(String cusName,Date dt1,Date dt2, Workbook workbook,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			StringBuffer buffer = new StringBuffer(50);
			if (filePathManager.getTmpExcel().isEmpty()) {
				File filePath = new File(filePathManager.toString());
				filePath.mkdirs();
			}
			buffer.append("D://");
			buffer.append("region");
			buffer.append("/");
			buffer.append(sdf1.format(new Date()));
			buffer.append("/");
			File file = new File(buffer.toString());
			if (!file.exists()) {
				file.mkdirs();
			}
			//判断是否累计
			if(dt1==dt2){
			buffer.append("项目地域频次_" + cusName + "互联网广告监测_截止到"+sdf.format(dt2)+".xlsx");	
			}
			else{
			buffer.append("项目地域频次_" + cusName + "互联网广告监测"+sdf.format(dt1)+"_"+sdf.format(dt2)+".xlsx");
			}
			FileOutputStream stream = new FileOutputStream(buffer.toString());
			map.put("savePath", buffer.toString());
			response.getWriter().write(JSONUtil.beanToJson(map));
			workbook.write(stream); // 输出到本地磁盘中的
			stream.flush();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	

	/**
	 * 从本地读取文件页面弹出下载框
	 * @param filePath
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getExcelPath.do")
	public static void outputWorkBook(String filePath, HttpServletResponse response) throws IOException {
		filePath = java.net.URLDecoder.decode(filePath, "UTF-8");
		File file = new File(filePath);
		String fileName = file.getName();
		// 以流的形式下载文件。
		InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		// 清空response
		response.reset();
		// 定义输出类型
		response.setContentType("application/vnd.ms-excel; charset=utf-8");
		response.setHeader("Content-disposition","attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
		response.setCharacterEncoding("utf-8");
		OutputStream ouputStream = new BufferedOutputStream(response.getOutputStream());
		ouputStream.write(buffer);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 通过一个传进来的日期判断分周还是月
	 * 
	 * @param date
	 * @param flag
	 */
	public static Map<String, Object> getCycle(Date date, boolean flag) {
		Map<String, Object> map = new HashMap<>();
		if (flag) {
			Calendar c1 = new GregorianCalendar();
			c1.setFirstDayOfWeek(Calendar.MONDAY);
			c1.setTime(date);
			c1.set(Calendar.DAY_OF_WEEK, c1.getFirstDayOfWeek()); // 星期一
			map.put("weekFirst", c1.getTime());
			Calendar c2 = new GregorianCalendar();
			c2.setFirstDayOfWeek(Calendar.MONDAY);
			c2.setTime(date);
			c2.set(Calendar.DAY_OF_WEEK, c2.getFirstDayOfWeek() + 6); // 星期日
			map.put("weekLast", c2.getTime());
		} else {
			Calendar c3 = Calendar.getInstance(); // 自然月第一天
			c3.setTime(date);
			c3.set(Calendar.DAY_OF_MONTH, 1);
			map.put("monthFirst", c3.getTime());
			Calendar c4 = Calendar.getInstance(); // 自然月最后一天
			c4.setTime(date);
			c4.set(Calendar.DATE, 1);
			c4.add(Calendar.MONTH, 1);
			c4.add(Calendar.DATE, -1);
			map.put("monthLast", c4.getTime());
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes" })
	public static XSSFWorkbook exportExcel(XSSFWorkbook workbook, String title, List list, Date s_dt, Date s_dt2,String cusName) {
		XSSFSheet sheet = null;
		XSSFRow row = null;
		// 创建样式
		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平
		style.setFillForegroundColor(new XSSFColor(new Color(55, 96, 145))); // 前景色为深蓝色
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		XSSFCellStyle style1 = (XSSFCellStyle) workbook.createCellStyle();
		style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直
		style1.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平
		style1.setFillForegroundColor(new XSSFColor(new Color(255, 255, 11))); // 前景色为黄色
		style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
		XSSFFont font = (XSSFFont) workbook.createFont(); // 生成一个字体
		font.setFontHeightInPoints((short) 9); // 9号字体
		font.setFontName("宋体");
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
		font.setColor(new XSSFColor(new Color(255, 255, 255))); // 字体颜色为白色
		XSSFFont font2 = (XSSFFont) workbook.createFont(); // 生成一个字体
		font2.setFontHeightInPoints((short) 9); // 9号字体
		font2.setFontName("宋体");
		font2.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
		style.setFont(font);
		style1.setFont(font2);
		
		XSSFCellStyle style2 = (XSSFCellStyle) workbook.createCellStyle();
		style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直
		style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平
		XSSFFont font3 = (XSSFFont) workbook.createFont(); // 生成一个字体
		font3.setFontHeightInPoints((short) 9); // 9号字体
		font3.setFontName("宋体");
		style2.setFont(font3);  //前几列用的 
		

		DecimalFormat df0 = new DecimalFormat("#,##0");// 不显示小数点,千分位
		DecimalFormat df1 = new DecimalFormat("#,##0.00");// 显示两位小数点，千分位
		
		XSSFCellStyle style3 = (XSSFCellStyle) workbook.createCellStyle();
		style3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直
		style3.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平
		style3.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		XSSFFont font4 = (XSSFFont) workbook.createFont(); // 生成一个字体
		font4.setFontHeightInPoints((short) 9); // 9号字体
		font4.setFontName("Calibri");  // Calibri
		style3.setFont(font4);  //后几列用的 6列开始-最后列
		
		setBoderStyle(style);
		setBoderStyle(style1);

		sheet = workbook.createSheet(title);
		sheet.setDisplayGridlines(false);// 设置无边框
		sheet.setColumnWidth(0,15*256);
		sheet.setColumnWidth(1,20*250);
		sheet.setColumnWidth(2,20*250);
		sheet.setColumnWidth(3,20*250);
		sheet.setColumnWidth(4,20*250);
//		sheet.setColumnWidth(5,25*300);
		row = sheet.createRow((int) 0); // 创建第一行，创建列名
		row.setHeight((short) 400);
		row.createCell((int) 0).setCellValue("投放媒体");
		row.createCell((int) 1).setCellValue("投放项目");
		row.createCell((int) 2).setCellValue("投放日期");
		row.createCell((int) 3).setCellValue("投放位置");
		row.createCell((int) 4).setCellValue("营销代码");
//		row.createCell((int) 5).setCellValue("投放类型");
		row.createCell((int) 5).setCellValue("曝光IP占比");
		row.createCell((int) 6).setCellValue("点击IP占比");
		row.createCell((int) 7).setCellValue("曝光次数");
		row.createCell((int) 8).setCellValue("曝光人数");
		row.createCell((int) 9).setCellValue("点击次数");
		row.createCell((int) 10).setCellValue("点击人数");
		
		for (int i = 0; i <= 4; i++) {
			row.getCell(i).setCellStyle(style);
		}
		for (int i = 1; i <= 7; i++) {
			row.createCell((int) i + 10).setCellValue("点击" + i + "次人数"); // 曝光多少次的人数
		}
		row.createCell((int) 18).setCellValue("点击次数8次以上");
		for (int i = 1; i <= 20; i++) {
			row.createCell((int) i + 18).setCellValue("曝光" + i + "次人数"); // 曝光多少次的人数
		}
		row.createCell((int) 39).setCellValue("曝光次数21次以上");
		for (int i = 5; i <= 39; i++) {
			row.getCell(i).setCellStyle(style1);
		}
		XSSFRow rowContent = null;
		RegionDataExport export = new RegionDataExport();
		for (int i = 1; i <= 10; i++) { // 遍历list
			rowContent = sheet.createRow(i);
		}
		for (int i = 0; i < 39; i++) {
			setBoderStyle(style);
			setBoderStyle(style1);
		}
		row.createCell((int) 40).setCellValue("省份");
		XSSFRow rowConcent = null;
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.dd");
		RegionDataExport region = null;
		List<RegionTimes> num =  null;
		//显示中心内容开始
		for(int j = 0;j < list.size();j++){
			region = (RegionDataExport)list.get(j);
			num = region.getNums();
			//1.取出得到的日期
			Date startDate = region.getStartDate();
			Date endDate = region.getEndDate();
			//2.格式化日期
			String sDate = sdf.format(startDate);
			String eDate = sdf.format(endDate);
			rowConcent = sheet.createRow(j+1); //循环创建行
			rowConcent.setHeight((short) 400);
			rowConcent.createCell(0).setCellValue(region.getMediaName()); //媒体名称
			rowConcent.createCell(1).setCellValue(region.getActivityName()); //活动名称
			rowConcent.createCell(2).setCellValue(sDate+"-"+eDate); //周期
			rowConcent.createCell(3).setCellValue(region.getPointLocation()); //投放位置
			rowConcent.createCell(4).setCellValue(region.getMic()); //短代码
//			rowConcent.createCell(5).setCellValue(region.getPutFunction()); //投放类型
			rowConcent.createCell(5);
			rowConcent.createCell(6);
			if(region.getSumImpPv() == 0 || region.getImpPv() == 0 && region.getSumImpPv()==0){
				rowConcent.getCell(5).setCellValue("#Div/0!");
			}else{
				rowConcent.getCell(5).setCellValue(df1.format(region.getImpPv()/region.getSumImpPv()*100)+"%"); //曝光IP
			}
			if(region.getSumClkPv() == 0 || region.getClkPv() ==0 && region.getSumClkPv() ==0 ){
				rowConcent.getCell(6).setCellValue("#Div/0!");
			}else{
				rowConcent.getCell(6).setCellValue(df1.format(region.getClkPv()/region.getSumClkPv()*100)+"%");//点击IP
			}
			rowConcent.createCell(7).setCellValue(region.getSumImpPv());//曝光PV
			rowConcent.createCell(8).setCellValue(region.getSumImpUv());//曝光UV
			rowConcent.createCell(9).setCellValue(region.getSumClkPv());//点击PV
			rowConcent.createCell(10).setCellValue(region.getSumClkUv());//点击PV
	        
			for(int v = 11;v<=41;v++){ //循环创建列
				rowConcent.createCell(v);
			}
			int cell = 10;
			for(int k =0;k<num.size();k++){ //循环num
				cell++; //11列开始++列
				if(num.get(k).getType().equals("IMP")){
					if("".equals(region.getImpDataCaliber()) || "0".equals(region.getImpDataCaliber())|| region.getImpDataCaliber()==null){
						rowConcent.getCell(cell).setCellValue(num.get(k).getDirtyUv());
					}else if(region.getImpDataCaliber() == "1"){
						rowConcent.getCell(cell).setCellValue(num.get(k).getCleanUv());
					}else{
						rowConcent.getCell(cell).setCellValue(num.get(k).getDcleanUv());
					}
				}
				if(num.get(k).getType().equals("CLK")){
					if("".equals(region.getClkDataCaliber()) || "0".equals(region.getClkDataCaliber())|| region.getClkDataCaliber()==null){
						rowConcent.getCell(cell).setCellValue(num.get(k).getDirtyUv());
					}else if(region.getClkDataCaliber() == "1"){
						rowConcent.getCell(cell).setCellValue(num.get(k).getCleanUv());
					}else{
						rowConcent.getCell(cell).setCellValue(num.get(k).getDcleanUv());
					}
				}
				
		 }
			
		//设置样式
		setBoderStyle(style3);
		setBoderStyle(style2);
		rowConcent.getCell(0).setCellStyle(style2);
		rowConcent.getCell(1).setCellStyle(style2);
		rowConcent.getCell(2).setCellStyle(style3);
		rowConcent.getCell(3).setCellStyle(style2);
		rowConcent.getCell(4).setCellStyle(style3);
//		rowConcent.getCell(5).setCellStyle(style2);
		for(int i = 5;i<=39;i++){
			rowConcent.getCell(i).setCellStyle(style3);
		}
		rowConcent.createCell((int)40).setCellValue(region.getProvince());
	 }
		return workbook;
	}

	
	@SuppressWarnings({ "rawtypes" })
	public static XSSFWorkbook exportExcel2(XSSFWorkbook workbook, String title, List list, Date s_dt, Date s_dt2,String cusName) {
		XSSFSheet sheet = null;
		XSSFRow row = null;
		// 创建样式
		XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平
		style.setFillForegroundColor(new XSSFColor(new Color(55, 96, 145))); // 前景色为深蓝色
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		XSSFCellStyle style1 = (XSSFCellStyle) workbook.createCellStyle();
		style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直
		style1.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平
		style1.setFillForegroundColor(new XSSFColor(new Color(255, 255, 11))); // 前景色为黄色
		style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
		XSSFFont font = (XSSFFont) workbook.createFont(); // 生成一个字体
		font.setFontHeightInPoints((short) 9); // 9号字体
		font.setFontName("宋体");
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
		font.setColor(new XSSFColor(new Color(255, 255, 255))); // 字体颜色为白色
		XSSFFont font2 = (XSSFFont) workbook.createFont(); // 生成一个字体
		font2.setFontHeightInPoints((short) 9); // 9号字体
		font2.setFontName("宋体");
		font2.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
		style.setFont(font);
		style1.setFont(font2);
		
		XSSFCellStyle style2 = (XSSFCellStyle) workbook.createCellStyle();
		style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直
		style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平
		XSSFFont font3 = (XSSFFont) workbook.createFont(); // 生成一个字体
		font3.setFontHeightInPoints((short) 9); // 9号字体
		font3.setFontName("宋体");
		style2.setFont(font3);  //前几列用的 
		

		DecimalFormat df0 = new DecimalFormat("#,##0");// 不显示小数点,千分位
		DecimalFormat df1 = new DecimalFormat("#,##0.00");// 显示两位小数点，千分位
		
		XSSFCellStyle style3 = (XSSFCellStyle) workbook.createCellStyle();
		style3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直
		style3.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平
		style3.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		XSSFFont font4 = (XSSFFont) workbook.createFont(); // 生成一个字体
		font4.setFontHeightInPoints((short) 9); // 9号字体
		font4.setFontName("Calibri");  // Calibri
		style3.setFont(font4);  //后几列用的 6列开始-最后列
		
		setBoderStyle(style);
		setBoderStyle(style1);

		sheet = workbook.createSheet(title);
		sheet.setDisplayGridlines(false);// 设置无边框
		sheet.setColumnWidth(0,15*256);
		sheet.setColumnWidth(1,20*250);
		sheet.setColumnWidth(2,20*250);
		sheet.setColumnWidth(3,20*250);
		sheet.setColumnWidth(4,20*250);
//		sheet.setColumnWidth(5,25*300);
		row = sheet.createRow((int) 0); // 创建第一行，创建列名
		row.setHeight((short) 400);
		row.createCell((int) 0).setCellValue("投放媒体");
		row.createCell((int) 1).setCellValue("投放项目");
		row.createCell((int) 2).setCellValue("投放日期");
		row.createCell((int) 3).setCellValue("投放位置");
		row.createCell((int) 4).setCellValue("营销代码");
//		row.createCell((int) 5).setCellValue("投放类型");
		row.createCell((int) 5).setCellValue("曝光IP占比");
		row.createCell((int) 6).setCellValue("点击IP占比");
		row.createCell((int) 7).setCellValue("曝光次数");
		row.createCell((int) 8).setCellValue("曝光人数");
		row.createCell((int) 9).setCellValue("点击次数");
		row.createCell((int) 10).setCellValue("点击人数");
		for (int i = 0; i <= 4; i++) {
			row.getCell(i).setCellStyle(style);
		}
		for (int i = 1; i <= 7; i++) {
			row.createCell((int) i + 10).setCellValue("点击" + i + "次人数"); // 曝光多少次的人数
		}
		row.createCell((int) 18).setCellValue("点击次数8次以上");
		for (int i = 1; i <= 20; i++) {
			row.createCell((int) i + 18).setCellValue("曝光" + i + "次人数"); // 曝光多少次的人数
		}
		row.createCell((int) 39).setCellValue("曝光次数21次以上");
		for (int i = 5; i <= 39; i++) {
			row.getCell(i).setCellStyle(style1);
		}
		XSSFRow rowContent = null;
		RegionDataExport export = new RegionDataExport();
		for (int i = 1; i <= 10; i++) { // 遍历list
			rowContent = sheet.createRow(i);
		}
		for (int i = 0; i < 39; i++) {
			setBoderStyle(style);
			setBoderStyle(style1);
		}
		row.createCell((int) 40).setCellValue("省份");
		
		XSSFRow rowConcent = null;
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.dd");
		RegionDataExport region = null;
		List<RegionTimes> num =  null;
		//显示中心内容开始
		for(int j = 0;j < list.size();j++){
			region = (RegionDataExport)list.get(j);
			num = region.getNums();
			//1.取出得到的日期
			Date startDate = region.getStartDate();
			Date endDate = region.getEndDate();
			//2.格式化日期
			String sDate = sdf.format(startDate);
			String eDate = sdf.format(endDate);
			rowConcent = sheet.createRow(j+1); //循环创建行
			rowConcent.setHeight((short) 400);
			rowConcent.createCell(0).setCellValue(region.getMediaName()); //媒体名称
			rowConcent.createCell(1).setCellValue(region.getActivityName()); //活动名称
			rowConcent.createCell(2).setCellValue(sDate+"-"+eDate); 
			rowConcent.createCell(3).setCellValue(region.getPointLocation()); //投放位置
			rowConcent.createCell(4).setCellValue(region.getMic()); //短代码
//			rowConcent.createCell(5).setCellValue(region.getPutFunction()); //投放类型
	        rowConcent.createCell(5);
	        rowConcent.createCell(6);
	        //2017-09-14 wcy
			if ("".equals(region.getImpDataCaliber())
					|| "0".equals(region.getImpDataCaliber())||region.getImpDataCaliber() == null) {
				if (region.getDirtyImpPV1() == 0
						|| region.getDirtyImpPV1() == null) {
					rowConcent.getCell(5).setCellValue("#Div/0!");
				} else {
					rowConcent.getCell(5).setCellValue(
							df1.format(region.getDirtyImpPV()
									/ region.getDirtyImpPV1() * 100)
									+ "%");
				}
			} else if ("1".equals(region.getImpDataCaliber())) {
				if (region.getCleanImpPV1() == 0
						|| region.getCleanImpPV1() == null) {
					rowConcent.getCell(5).setCellValue("#Div/0!");
				} else {
					rowConcent.getCell(5).setCellValue(
							df1.format(region.getCleanImpPV()
									/ region.getCleanImpPV1()* 100)
									+ "%");
				}
			} else {
				if (region.getDcleanImpPV1() == 0
						|| region.getDcleanImpPV1() == null) {
					rowConcent.getCell(5).setCellValue("#Div/0!");
				} else {
					rowConcent.getCell(5).setCellValue(
							df1.format(region.getDcleanImpPV()
									/ region.getDcleanImpPV1()* 100)
									+ "%");
				}
			}

			if ("".equals(region.getClkDataCaliber())
					|| "0".equals(region.getClkDataCaliber())||region.getClkDataCaliber() == null) {
				if (region.getDirtyClkPV1() == 0) {
					rowConcent.getCell(6).setCellValue("#Div/0!");
				} else {
					rowConcent.getCell(6).setCellValue(
							df1.format(region.getDirtyClkPV()
									/ region.getDirtyClkPV1()* 100)
									+ "%");
				}
			} else if ("1".equals(region.getClkDataCaliber())) {
				if (region.getCleanClkPV1() == 0) {
					rowConcent.getCell(6).setCellValue("#Div/0!");
				} else {
					rowConcent.getCell(6).setCellValue(
							df1.format(region.getCleanClkPV()
									/ region.getCleanClkPV1()* 100)
									+ "%");
				}
			} else {
				if (region.getDcleanClkPV1() == 0) {
					rowConcent.getCell(6).setCellValue("#Div/0!");
				} else {
					rowConcent.getCell(6).setCellValue(
							df1.format(region.getDcleanClkPV()
									/ region.getDcleanClkPV1()* 100)
									+ "%");
				}
			}

			if ("".equals(region.getImpDataCaliber())
					|| "0".equals(region.getImpDataCaliber())||region.getImpDataCaliber() == null) {
				rowConcent.createCell(7).setCellValue(region.getDirtyImpPV1()); // 曝光次数
			} else if ("1".equals(region.getImpDataCaliber())) {
				if (region.getCleanImpPV() == null) {
					rowConcent.createCell(7).setCellValue("N/A");
				} else {
					rowConcent.createCell(7).setCellValue(
							region.getCleanImpPV1());
				}
			} else {
				rowConcent.createCell(7).setCellValue(region.getDcleanImpPV1());
			}
			if ("".equals(region.getImpDataCaliber())
					|| "0".equals(region.getImpDataCaliber())) {
				rowConcent.createCell(8).setCellValue(region.getDirtyImpUV1()); // 曝光人数
			} else if ("1".equals(region.getImpDataCaliber())) {
				if (region.getCleanImpPV() == null) {
					rowConcent.createCell(8).setCellValue("N/A");
				} else {
					rowConcent.createCell(8).setCellValue(
							region.getCleanImpUV1());
				}
			} else {
				rowConcent.createCell(8).setCellValue(region.getDcleanImpUV1());
			}
			if ("".equals(region.getClkDataCaliber())
					|| "0".equals(region.getClkDataCaliber())||region.getClkDataCaliber() == null) {
				rowConcent.createCell(9).setCellValue(region.getDirtyClkPV1()); // 点击次数
			} else if ("1".equals(region.getClkDataCaliber())) {
				if (region.getCleanImpPV() == null) {
					rowConcent.createCell(9).setCellValue("N/A");
				} else {
					rowConcent.createCell(9).setCellValue(
							region.getCleanClkPV1());
				}
			} else {
				rowConcent.createCell(9)
						.setCellValue(region.getDcleanClkPV1());
			}
			if ("".equals(region.getClkDataCaliber())
					||"0".equals(region.getClkDataCaliber())||region.getClkDataCaliber() == null) {
				rowConcent.createCell(10).setCellValue(region.getDirtyClkUV1()); // 点击人数
			} else if ("1".equals(region.getClkDataCaliber())) {
				if (region.getCleanImpPV() == null) {
					rowConcent.createCell(10).setCellValue("N/A");
				} else {
					rowConcent.createCell(10).setCellValue(
							region.getCleanClkUV1());
				}
			} else {
				rowConcent.createCell(10).setCellValue(region.getDcleanClkUV1());
			}

			for(int v = 11;v<=41;v++){ //循环创建列
				rowConcent.createCell(v);
			}
			int cell = 10;
			for(int k =0;k<num.size();k++){ //循环num
				cell++; //11列开始++列
				if(num.get(k).getType().equals("IMP")){
					if("".equals(region.getImpDataCaliber()) || "0".equals(region.getImpDataCaliber())||region.getImpDataCaliber()==null){
						rowConcent.getCell(cell).setCellValue(num.get(k).getDirtyUv());
					}else if(region.getImpDataCaliber() == "1"){
						rowConcent.getCell(cell).setCellValue(num.get(k).getCleanUv());
					}else{
						rowConcent.getCell(cell).setCellValue(num.get(k).getDcleanUv());
					}
				}
				if(num.get(k).getType().equals("CLK")){
					if("".equals(region.getClkDataCaliber()) || "0".equals(region.getClkDataCaliber())||region.getClkDataCaliber()==null){
						rowConcent.getCell(cell).setCellValue(num.get(k).getDirtyUv());
					}else if(region.getClkDataCaliber() == "1"){
						rowConcent.getCell(cell).setCellValue(num.get(k).getCleanUv());
					}else{
						rowConcent.getCell(cell).setCellValue(num.get(k).getDcleanUv());
					}
				}
		    }	
			//设置样式
			setBoderStyle(style3);
			setBoderStyle(style2);
			rowConcent.getCell(0).setCellStyle(style2);
			rowConcent.getCell(1).setCellStyle(style2);
			rowConcent.getCell(2).setCellStyle(style3);
			rowConcent.getCell(3).setCellStyle(style2);
			rowConcent.getCell(4).setCellStyle(style3);
//			rowConcent.getCell(5).setCellStyle(style2);
			for(int i = 5;i<=39;i++){
				rowConcent.getCell(i).setCellStyle(style3);
			}
			rowConcent.createCell((int)40).setCellValue(region.getProvince());
		 }
			return workbook;
		}  
	
	
	public static void setBoderStyle(XSSFCellStyle style) {
		// 设置边框样式
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置边框颜色
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置位置居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
	}
}
