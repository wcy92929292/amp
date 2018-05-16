package com.udbac.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.model.UserBean;
import com.udbac.service.MonthDayReportService;
import com.udbac.service.MonthReportService;

/**
 * 月报Controller
 * @author LFQ
 *
 */
@Controller
@RequestMapping("/report")
public class MonthReportController {

	@Resource(name="monthService")
	private MonthReportService monthReportService;
	
	@Autowired
	private MonthDayReportService monthDayReportService;
	
	public static String MonthLogo;
	
	/**
	 * 导出月结报告
	 * @param month_customer	月结报告的客户
	 * @param month_date		几月的报告
	 * @param month_location	是否合并相同点位 0 否，1 是
	 * @param month_type		月报类型：	0、普通类型，百度关键词以及其它点位都在一块，
	 * 									1、不包含百度关键词
	 * 									2、只有百度关键词
	 * @param monthSummaryData 汇总方式： 0、累加汇总    1、频控汇总
	 * @param request
	 * @return
	 */
	@RequestMapping("/month.do")
	public String MonthReport(@RequestParam("month_customer") String monthCustomer,
							@RequestParam("month_date") String monthDate,
							@RequestParam("month_location") String monthLocation,
							@RequestParam("month_type") String monthType,
							@RequestParam("month_file") Integer monthFile,
							@RequestParam("month_summary_data") Integer monthSummaryData,
							HttpServletRequest request
							){
		
		if(MonthLogo == null){
			MonthLogo = request.getRealPath("/") + "/images/udbac_logo.png";
		}
		
		try {
			new FileInputStream(MonthLogo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String filePathName = "";
		Object object = request.getSession().getAttribute("user");
		String userName = "";
		if(object instanceof UserBean){
			userName = ((UserBean)(object)).getUSER_NAME();
		}
		if(monthFile == 0){
			System.out.println(monthReportService+"======");
			filePathName = monthReportService.monthReportList(userName,monthCustomer,monthDate,monthType,monthLocation,monthFile,monthSummaryData);
		}else{
			filePathName = monthDayReportService.monthReportList(userName,monthCustomer,monthDate,monthType,monthLocation,monthFile,monthSummaryData);
		}
		
		//未找到数据
		if("0".equals(filePathName)){
			return "forward:sendMessage.do";
		}
		
		request.setAttribute("filePath",filePathName);
		request.setAttribute("fileName",filePathName.substring(filePathName.lastIndexOf("/") + 1));
		
		return "forward:exportReport.do";
	}//end month
	
	/**
	 * 返回信息
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendMessage.do")
	public String sendMessage(HttpServletRequest request,HttpServletResponse response){
			return "<script>parent.setMes('0')</script>";
	}//end sendMessage()
	
	
	/**
	 * 导出插码完成的文件。
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/exportReport.do")
	public ResponseEntity<byte[]> exportReport(HttpServletRequest request) {
		
		Object inputStreamObj = request.getAttribute("inputStream");
		Object filePathObj = request.getAttribute("filePath");
		Object fileNameObj = request.getAttribute("fileName");
		
		InputStream in = null;
		String filePath = null;
		String fileName = null;
		
		if(inputStreamObj instanceof InputStream){
			in = (InputStream)inputStreamObj;
		}
		
		if(filePathObj instanceof String){
			filePath = (String)filePathObj;
		}
		
		if(filePathObj instanceof String){
			fileName = (String)fileNameObj;
			fileName.replace("/", "");
		}
		
		ResponseEntity<byte[]> responseEntity = null;
		try {
			if(in == null){
				in = new FileInputStream(filePath);
			}
			
			byte[] body = null;
			
			body = new byte[in.available()];
			in.read(body);
			fileName=new String(fileName.getBytes("UTF-8"),"iso-8859-1");
			
			//设置响应头，文件名
			HttpHeaders headers = new HttpHeaders();
			headers.put("Content-Disposition", Arrays.asList("attachment;filename="+fileName));
			//响应状态码
			HttpStatus status = HttpStatus.OK;
			
			responseEntity = new ResponseEntity<>(body,headers,status);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return  responseEntity; 
	}//end 
	
}
