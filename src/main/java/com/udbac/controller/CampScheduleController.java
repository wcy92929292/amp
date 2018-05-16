package com.udbac.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.entity.CampScheduleInfo;
import com.udbac.service.CampScheduleService;
import com.udbac.util.JSONUtil;
import com.udbac.util.LogUtil;

/**
 * 活动排期信息
 * 
 * @author han
 *
 */
@Controller
@RequestMapping("/campaign")
public class CampScheduleController {

	@SuppressWarnings("unused")
	private LogUtil logUtil = new LogUtil(CampaignController.class);
	
	@Autowired
	CampScheduleService service;
	
	Pattern pattern = Pattern.compile("[0-9]*");
	/**
	 * 活动排期信息一览
	 * @param actCode 活动编号
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/campSchedule.do", method = RequestMethod.POST)
	//public List<CampScheduleInfo> listAll(@RequestParam(value = "actCode", required = true) String actCode,
	public List<CampScheduleInfo> listAll(@RequestParam(value = "actCode", required = true) String actCode,
			HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
		//List<CampScheduleInfo> listCamp = service.listAll(actCode);
		
		List<CampScheduleInfo> list = service.listSche(actCode);
		System.out.println(list);
		return list;
	}

	@RequestMapping("/updateClickAvg.do")
	public void updateClickAvg(@RequestParam(value="clickAvg",required=false) String clickAvg,
			@RequestParam(value = "mic",required = false) String mic,
			HttpServletResponse response){
			
			System.out.println(mic);
			System.out.println(clickAvg);
			Map<String, Object> map = new HashMap<>();
 			
			try {
				Matcher isNum = pattern.matcher(clickAvg);
				if( !isNum.matches() )
				{
					map.put("message", "不能为非数字或数值小于0");
					response.getWriter().write(JSONUtil.beanToJson(map));
				}else{
					if(Integer.valueOf(clickAvg) >= 0){
						service.updateClickAvg(clickAvg, mic);
						map.put("message", "修改成功！");
						response.getWriter().write(JSONUtil.beanToJson(map));
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				try {
					 map.put("message", "输入错误！");
					response.getWriter().write(JSONUtil.beanToJson(map));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
	}
	
	
	@RequestMapping("/updateExposureAvg.do")
	public void updateExposureAvg(@RequestParam(value="exposureAvg",required=false) String exposureAvg,
			@RequestParam(value = "mic",required = false) String mic,
			HttpServletResponse response){
			
			System.out.println(mic);
			System.out.println(exposureAvg);
			Map<String, Object> map = new HashMap<>();
			
			try {
				Matcher isNum = pattern.matcher(exposureAvg);
				if( !isNum.matches() )
				{
					map.put("message", "不能为非数字或数值小于0");
					response.getWriter().write(JSONUtil.beanToJson(map));
				}else{
					if(Integer.valueOf(exposureAvg) >= 0){
						service.updateExposureAvg(exposureAvg, mic);
						map.put("message", "修改成功！");
						response.getWriter().write(JSONUtil.beanToJson(map));
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				try {
					 map.put("message", "输入错误！");
					response.getWriter().write(JSONUtil.beanToJson(map));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
	}
	
	/**
	 * 活动排期文件下载
	 * @param path  排期路径
	 * @param response
	 */
	@RequestMapping("/downloadSchePath.do")
	public void downloadSchePath(@RequestParam(value = "path", required = true) String path,
			HttpServletResponse response,Map<String, String>  map) {
		try {
			// path是获取的文件的路径。
			// String path = "/base/插码表-T2奔跑吧兄弟4-未加码-已加码.xlsx/";
			File file = new File(path);
			// 取得文件名。
			String filename = file.getName();
			// 设置文件名下载时中文正常
			filename = new String(filename.getBytes("gb2312"), "iso8859-1");
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			// response.setContentType("application/vnd.ms-excel;charset=gb2312");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			String msg = "排期文件未找到!";
			map.put("msg", msg);
			try {
				String text=JSONUtil.beanToJson(map);
				//设置编码及文件格式   
			    response.setContentType("text/html;charset=UTF-8");
			    //设置不使用缓存   
			    response.setHeader("Cache-Control","no-cache"); 
			    response.getWriter().write(text);
			   // logUtil.logError("");
			} catch (Exception e) {
				// logUtil.logError("");
			}
		}
	}
	
}
