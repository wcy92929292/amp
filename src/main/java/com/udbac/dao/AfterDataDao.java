package com.udbac.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.AfterData;

public interface AfterDataDao {

	List<AfterData> list(@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("mic") List<String> list);

	
}
