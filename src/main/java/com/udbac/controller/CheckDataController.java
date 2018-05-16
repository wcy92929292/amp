package com.udbac.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.exception.ScheduleException;
import com.udbac.model.CheckDataModel;
import com.udbac.service.CheckDataService;

@Controller
@RequestMapping("/checkData")
public class CheckDataController {

	@Autowired
	private CheckDataService checkDataService;
	
	/**
	 * 检测指定某天的监测数据是否正常
	 *    访问方式：http://xxxxxx/amp/checkData/hourAndDay/20160907.do
	 * @param dateStr
	 * @return
	 * 		正常监测返回：data is ok!
	 * 		数据异常返回：
	 * 			hour data is none!
	 *  		day data is none!
	 * 			day data and hour data has error!Detial：[]
	 * 		其他异常返回：
	 * 			format is error!
	 *			system error!
	 * 			 
	 */
	@RequestMapping(value="/hourAndDay/{dateStr}.do")
	@ResponseBody
	public String hourAndDay(@PathVariable("dateStr") String dateStr){
		StringBuffer sb = new StringBuffer(10);
		sb.append(dateStr);
		
		if(!dateStr.matches("\\d{8}")){
			sb.append(" format is error!");
		}else{
			try {
				List<CheckDataModel> checkHourAndDay = checkDataService.checkHourAndDay(dateStr);
				
				if(checkHourAndDay.size() == 0){
					sb.append(" data is ok!");
				}else{
					sb.append(" day data and hour data has error!Detial：\n");
					sb.append(checkHourAndDay);
				}
				
			} catch (Exception e) {
				if(e instanceof ScheduleException){
					sb.append(e.getMessage());
				}else{
					sb.append(" system error!");
				}
				e.printStackTrace();
			}//end try - catch
		}//end if - else
		
		return sb.toString();
	}//end 
	
}
