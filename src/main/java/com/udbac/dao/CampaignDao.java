package com.udbac.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.TbAmpUpdateUrl;

public interface CampaignDao {
	
	/**
	 * 查询营销活动一览列表
	 * @param map
	 * @return
	 */
	List<Object> queryCampaignList(Map<String, String> map);
	List<TbAmpUpdateUrl> queryCampaignUpdate();
	public Integer QueryActivitycode(String  activitycode);
	public Integer QueryUnit(String  unit);
	/**
	 * 营销活动——活动监测数据 
	 * @author LQ
	 * @param map
	 * @return 
	 * @date 2016-04-18
	 */
	List<Object> queryMonitingData(Map<String, String> map);
	/**
	 * 营销活动——活动监测数据 查询按钮模糊查询
	 * @author LQ
	 * @param map
	 * @return 
	 * @date 2016-04-20
	 */
	List<Object> searchMonitingData(Map<String, String> map);
	
	List<Object> searchTodayMonitingData(Map<String, String> map);
	
	List<Object> searchHourMonitingData(@Param("_sdate") Date _sdate, @Param("_edate") Date _edate, @Param("_mediaName") String _mediaName, @Param("_marketingCode") String _marketingCode,
			@Param("activityCode") String activityCode);
	
	}
