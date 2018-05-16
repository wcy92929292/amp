package com.udbac.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.udbac.entity.TbAmpBasicActivityInfo;
import com.udbac.model.UserBean;
import com.udbac.service.ScheduleService;

/**
 * 排期控制类
 * @author LFQ
 * @DATE 2016-04-11
 */
@Controller
@RequestMapping("/schedule")
public class Schedule {

	@Autowired
	private ScheduleService scheduleService;
	
	/**
	 * 排期插码
	 * @param scheduleFile	排期文件
	 * @param scheduleType	排期类型： 0、普通排期	1、集团排期		2、百度关键词排期
	 * @param updateFile	确定修改排期信息
	 * @param pointLine		需要处理频控的点位的频控属性
	 * @param isAddPoint	是否是新增点位
	 * @param after			后端支撑人员ID
	 * @param before		前端支撑人员ID
	 * @param center		监测中心人员ID
	 * @param request		
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/plugInCode.do")
	public String generalSchedule(
			@RequestParam("scheduleFile") MultipartFile scheduleFile,
			@RequestParam("scheduleType") Integer scheduleType,
			@RequestParam(value="updateFile",required=false) MultipartFile updateFile,
			@RequestParam(value="after",required=false) String after,
			@RequestParam(value="before",required=false) String before,
			@RequestParam(value="center",required=false) String center,
			@RequestParam(value="isAddPoint",required=false) Integer isAddPoint,
//			@RequestParam(value="pointLine",required=false) String[] pointLine,
			HttpServletRequest request,
			HttpServletResponse response
	) throws IOException{
		
		String fileName = scheduleFile.getOriginalFilename();
		//request.getParameterValues("");
//		if(pointLine != null){
//			for (int i = 0; i < pointLine.length; i++) {
//				System.out.println(pointLine[i]);
//			}
//		}
		UserBean user = (UserBean)request.getSession().getAttribute("user");
		
		Map<String,Object> map = new HashMap<>();
		List<String> mesList = new LinkedList<>();
		//文件格式只能是.xlsx
		if(fileName.endsWith(".xlsx") && scheduleFile.getSize() < 1024 * 3096 *10){
			
			switch (scheduleType) {
				case 0:
					map = scheduleService.genSchedule(scheduleFile,updateFile,user,after,before,center,isAddPoint != null);
					break;
				case 1:
					map = scheduleService.jtSchedule(scheduleFile,updateFile,user,after,before,center,isAddPoint != null);
					break;
				case 2:
					map = scheduleService.keySchedule(scheduleFile, updateFile,user,after,before,center,isAddPoint != null);
					break;
				case 3:
					if(after == null || before == null || center == null){
						map.put("err","后端补排期请填写后端人员，前端人员，监测中心人员");
						break;
					}
					map = scheduleService.afterSchedule(scheduleFile,user,after,before,center);
					break;
				default:
					map.put("err","没有对应排期格式");
					break;
			}
			
			//排期插完码之后，通知GC及时清理无用对象
			System.gc();
			
			if(map.get("err") != null){
				mesList = (List<String>) map.get("err");
			}else{
//				System.out.println( map.get("filePath")+"=="+map.get("fileName"));
				if(map.get("filePath") == null){
					mesList.add("文件读取出错，请更换文件！");
					request.setAttribute("mes", mesList);
				}
				request.setAttribute("filePath", map.get("filePath"));
				request.setAttribute("fileName", map.get("fileName"));
			}
		}else{
			mesList.add("排期格式只能是.xlsx文件,并且文件不能大于3M,请检查隐藏列、行。隐藏Sheet");
		}
			
		if(mesList.size() != 0){
			request.setAttribute("mes", mesList);
			return "forward:sendMessage.do";
		}
		
		//转到排期文件下载
		return "forward:exportSchedule.do";
	}//checkSchedule()
	
	
	/**
	 * 返回显示错误信息
	 * @return 错误信息
	 */
	@ResponseBody
	@RequestMapping("/sendMessage.do")
	public void sendMessage(HttpServletRequest request,HttpServletResponse response){
		
		List<?> mes = (List<?>)request.getAttribute("mes");
		try {
			response.getWriter().write("<script>parent.error('"+mes+"')</script>");
			mes.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//end sendErr()
	
	/**
	 * 导出插码完成的文件。
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/exportSchedule.do")
	public ResponseEntity<byte[]> exportSchedule(HttpServletRequest request) {
		
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
	}//end daily
	
	@RequestMapping("/downSchedule.do")
	public String downSchedule(@RequestParam("actCode") String actCode,HttpServletRequest request){
		
		TbAmpBasicActivityInfo activityInfo = scheduleService.downloadSchedule(actCode);
		String filePath = "";
		if(activityInfo == null  || activityInfo.getSchedulePath() == null || !new File(activityInfo.getSchedulePath()).exists()){
			ArrayList<Object> list = new ArrayList<>();
			list.add("未找到相关排期");
			request.setAttribute("mes", list);
			return "forward:sendMessage.do";
		}
		
		filePath = activityInfo.getSchedulePath();
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
		
		request.setAttribute("filePath", filePath);
		request.setAttribute("fileName", fileName);
		
		if(filePath == ""){
			ArrayList<String> list = new ArrayList<>();
			list.add("排期文件有误，请重新选择文件。");
			request.setAttribute("mes", list);
			return "forward:sendMessage.do";
		}
		
		return "forward:exportSchedule.do";
	}
	
	/**
	 * 延长排期时间
	 * @return
	 */
	@RequestMapping("/addScheduleDate.do")
	public String addScheduleDate(
			@RequestParam("scheduleFile") MultipartFile scheduleFile,
			@RequestParam("scheduleType") String scheduleType,
			HttpServletRequest request){
		
		List<String> list = null;
		if(!scheduleFile.getOriginalFilename().endsWith(".xlsx")){
			list = new ArrayList<>();
			list.add("文件格式不正确，请使用.xlsx文件！");
		}else{
			list = scheduleService.addScheduleDate(scheduleType,scheduleFile);
			if(list.size() == 0){list.add("延期成功");}
		}
		request.setAttribute("mes", list);
		return "forward:sendMessage.do";
	}
	
	/**
	 * 更改排期预估
	 * @param scheduleFile
	 * @param scheduleType
	 * @param request
	 * @return
	 */
	@RequestMapping("/setScheduleNewForecast.do")
	public String setScheduleNewForecast(
			@RequestParam("scheduleFile") MultipartFile scheduleFile,
			@RequestParam("scheduleType") String scheduleType,
			HttpServletRequest request){
		List<String> list = null;
		if(!scheduleFile.getOriginalFilename().endsWith(".xlsx")){
			list = new ArrayList<>();
			list.add("文件格式不正确，请使用.xlsx文件！");
		}else{
			list = scheduleService.updateForecase(scheduleType,scheduleFile);
			if(list.size() == 0){list.add("更改排期预估成功");}
		}
		request.setAttribute("mes", list);
		return "forward:sendMessage.do";
	}//end /setScheduleNewForecast.do
	
	/**
	 * 下载同步跳转表 
	 */
	@RequestMapping("/DownloadMicAndUrl.do")
	public String downloadMicAndUrl(HttpServletRequest request){
		String filePath = scheduleService.downloadMicAndUrl();
		
		String fileName = filePath.substring(filePath.lastIndexOf("/"), filePath.length());
		
		request.setAttribute("filePath", filePath);
		request.setAttribute("fileName", fileName);
		
		return "forward:exportSchedule.do";
	}
	
	
}//end class Schedule
