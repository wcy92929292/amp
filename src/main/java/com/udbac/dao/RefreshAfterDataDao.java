package com.udbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.udbac.entity.SMSDayData;

/**
 * 同步后端数据数据
 * @author LFQ
 */
public interface RefreshAfterDataDao {

	/**
	 * 将后端数据插入到临时库中
	 * @param dataList
	 */
	void InsertToTmpData(@Param("dataList") List<SMSDayData> dataList); 
//	insert into tb_amp_sum_back_d_tmp(mic,daytime,vv,uv,clk,pv,bounce_times,TIME_SPENT)values('XASVdrfwZ2SZEQL2STim','2016-08-16',2041,1968,2717,3477,1511,16.03);
//	select summary_after_data();
	
	/**
	 * 将临时数据同步到后端日表中
	 */
	@Select("select summary_after_data();")
	int summaryAfterData();
}
