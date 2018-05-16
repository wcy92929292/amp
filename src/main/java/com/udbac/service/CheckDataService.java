package com.udbac.service;

import java.util.List;

import com.udbac.exception.ScheduleException;
import com.udbac.model.CheckDataModel;

/**
 * 校验前端数据
 * @author LFQ
 */
public interface CheckDataService {
	
	List<CheckDataModel> checkHourAndDay(String dateStr) throws ScheduleException;
	
	
}
