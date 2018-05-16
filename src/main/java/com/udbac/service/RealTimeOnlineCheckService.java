package com.udbac.service;

import java.util.List;

import com.udbac.model.MonthReportModel;
import com.udbac.model.OnlineDataNode;

/**
 * 上线实时查询当天的数据 
 * @author LFQ
 * @date 2016-08-22
 */
public interface RealTimeOnlineCheckService {
	
	/**
	 * 获取某个短代码当天的分小时数据
	 * @param mic
	 * @return
	 */
	public List<OnlineDataNode> getHourData(String mic);
	
	/**
	 * 获取历史7天的分天数据
	 * @param mic
	 * @return
	 */
	public List<MonthReportModel> getDateData(String dateStr,String mic);
}
