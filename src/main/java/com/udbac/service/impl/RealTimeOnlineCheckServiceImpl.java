package com.udbac.service.impl;


import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.util.DateUtil;
import com.udbac.dao.RealDataDao;
import com.udbac.model.MonthReportModel;
import com.udbac.model.OnlineDataNode;
import com.udbac.service.RealTimeOnlineCheckService;
import com.udbac.util.RealTimeOnlineCheckUtil;

/**
 * 实时数据相关Service
 * @author LFQ
 *
 */
@Service
public class RealTimeOnlineCheckServiceImpl implements RealTimeOnlineCheckService {

	@Autowired
	private RealDataDao realDataDao;
	/**
	 * 获取某个短代码当天的小时数据
	 * @param mic	目标MIC
	 * @return
	 */
	public List<OnlineDataNode> getHourData(String mic){
		
//		String mic = "r0PfKDQNgbTqwufzjeGc";
		
		RealTimeOnlineCheckUtil realTimeOnlineCheckUtil = RealTimeOnlineCheckUtil.getRealTimeOnlineCheckUtil();
		
		//刷新当前的时间
		realTimeOnlineCheckUtil.refreshData();
		
		//查询当天的小时数据
		List<OnlineDataNode> totailPVClick = realTimeOnlineCheckUtil.totailPVClick(mic);
		
		return totailPVClick;
	}//end getHourData

	
	
	/**
	 * 查询历史7天的数据
	 * @param mic	目标MIC
	 * @param dateStr	截止日期
	 */
	@Override
	public List<MonthReportModel> getDateData(String dateStr,String mic) {
		
		Date date = DateUtil.getDate(dateStr, null);
		if(date == null){
			date = DateUtil.getDate(DateUtil.getDateStr(new Date(), null), null);
		}
		
		return realDataDao.listHistoryWeek(date, mic);
	}//end getDateData()
	
}//end class
