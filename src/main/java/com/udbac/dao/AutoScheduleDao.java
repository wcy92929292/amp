package com.udbac.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.AutoScheduleInfo;

public interface AutoScheduleDao {
	
	public List<AutoScheduleInfo> listAll(@Param("sdate") Date sdate,@Param("edate") Date edate);

}
