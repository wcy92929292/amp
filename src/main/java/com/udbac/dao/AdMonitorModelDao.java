package com.udbac.dao;

import java.util.List;

import com.udbac.model.AdMonitorModel;

/****
 * 营销活动查询Dao
 *
 */
public interface AdMonitorModelDao {

	/***
	 * 插入临时数据
	 * 
	 * @param insertList
	 */
	public void insertTimedata(List<AdMonitorModel> insertList);
	
	/**
	 * 汇总后端数据
	 */
	public String summaryData();

	public String checkMic(String mic);

}
