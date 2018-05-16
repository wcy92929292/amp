package com.udbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.model.CheckDataModel;

/**
 * 检查数据DAO
 * @author LFQ
 */
public interface CheckDataDao {

	/**
	 * 校验某天的小时数据
	 * @param startTime 某天的凌晨时间  '20160901 235959'
	 * @param endTime	某天的结束时间 '20160901'
	 * @return 有误的数据将会返回
	 */
	List<CheckDataModel> hourAndDay(@Param("startTime") String startTime,@Param("endTime") String endTime);
	
	/**
	 * 检查小时表某天中是否有数据
	 * @param startTime '20160901'
	 * @return
	 */
	Integer countHour(@Param("startTime") String startTime,@Param("endTime") String endTime); 
	
	/**
	 * 检查日表某天是否有数据
	 * @param startTime '20160901'
	 * @return
	 */
	Integer countDay(@Param("startTime") String startTime);
	
}
