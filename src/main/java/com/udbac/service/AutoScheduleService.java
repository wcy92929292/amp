package com.udbac.service;

import java.util.Date;
import java.util.List;

import com.udbac.entity.AutoScheduleInfo;

public interface AutoScheduleService {
	
	public List<AutoScheduleInfo> listAll(Date sdate,Date edate);

}
