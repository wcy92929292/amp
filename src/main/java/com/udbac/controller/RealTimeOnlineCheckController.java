package com.udbac.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.model.MonthReportModel;
import com.udbac.model.OnlineDataNode;
import com.udbac.service.RealTimeOnlineCheckService;

/**
 * 实时数据 相关控制
 * @author LFQ
 */
@Controller
@RequestMapping("/real")
public class RealTimeOnlineCheckController {

	@Autowired
	private RealTimeOnlineCheckService realTimeOnlineCheckService;
	
	/**
	 * 获取目标Mic当天的小时数据
	 * @param mic
	 */
	@RequestMapping("/hour.do")
	@ResponseBody
	public List<OnlineDataNode>  getHourData(@RequestParam("mic") String mic){
		
		List<OnlineDataNode> hourData = realTimeOnlineCheckService.getHourData(mic);
		return hourData;
	}//end getHourData()
	
	/**
	 * 获取目标Mic当天的小时数据
	 * @param mic
	 */
	@RequestMapping("/history.do")
	@ResponseBody
	public List<MonthReportModel> getHistoryData(@RequestParam("endDate") String dateStr
			,@RequestParam("mic") String mic){
		
		List<MonthReportModel> dateData = realTimeOnlineCheckService.getDateData(dateStr, mic);
		return dateData;
	}//end getHourData()
	
}//end class RealDataController
