package com.udbac.service;

import java.util.Date;
import java.util.List;

import com.udbac.entity.AfterData;

public interface AfterDataService {
	
	List<AfterData> list(Date startDate,Date endDate,List<String> list);

}
