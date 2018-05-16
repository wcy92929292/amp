package com.udbac.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.service.RefreshAfterDataService;

/**
 * 从旧系统中同步后端数据Controller
 * @author LFQ
 */
@Controller
@RequestMapping("/refresh")
public class RefreshAfterDataController {

	@Autowired
	private RefreshAfterDataService refreshAfterDataService;
	
	public static String MonthLogo;
	
	private static String dateMatch = "\\d{4}-\\d{2}-\\d{2}_\\d{2}:\\d{2}:\\d{2}";
	
	/**
	 * 同步后端数据。
	 * 	http://localhost:8080/amp/refresh/refreshAfter.do?sdateStr=2016-08-20_00:00:00&edateStr=2016-08-22_00:00:00&atOnce=atOnce
	 * @param sdateStr 后端数据开始时间
	 * @param edateStr 后端数据结束时间
	 * @param atOnce	是否立即生效
	 * @return
	 * @throws InterruptedException
	 */
	@RequestMapping("/refreshAfter.do")
	@ResponseBody
	public String refreshAfter(@RequestParam("sdateStr") String sdateStr,
							@RequestParam("edateStr") String edateStr,
							@RequestParam(value="atOnce",required=false) String atOnce
			) throws InterruptedException{
		
		if(sdateStr.matches(dateMatch) && edateStr.matches(dateMatch)){
			if(!"atOnce".equals(atOnce)){
				synchronized (this.getClass()) {
					//数据将在10分钟后进行同步
					Thread.sleep(600000);
				}
			}
			
			System.out.println("tong ....");
			refreshAfterDataService.refreshAfter(sdateStr, edateStr);
			System.out.println("ok ....");
			System.gc();
		}
		
		return "1";
	}//end month
	
	
}
