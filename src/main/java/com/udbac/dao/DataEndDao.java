package com.udbac.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.DataEndInfo;
/**
 * 结案数据报表
 * @author Wangli 
 *
 */
public interface DataEndDao {
	
	DataEndInfo listDateInfo(@Param("actName") String actName, @Param("actCode")String actCode);

	List checkName(@Param("actName") String actName);

	Integer sumName(@Param("actName")String actName);

	Integer sumSame(@Param("actName")String actName, @Param("actCode")String actCode);

	List<DataEndInfo> listInJCfo(@Param("actName")String actName, @Param("actCode")String actCode,
			String customer_id);

	List<DataEndInfo> listFMTfo(@Param("actName")String actName, @Param("actCode")String actCode,
			String customer_id);

	Integer listMeiNum(@Param("actName")String actName, @Param("actCode")String actCode, String customer_id);

	List<DataEndInfo> listGGfo(@Param("actName")String actName, @Param("actCode")String actCode,
			String customer_id);

	Integer listGGWNum(@Param("actName")String actName, @Param("actCode")String actCode, String customer_id);

	ArrayList<DataEndInfo> listDJFMTinfo(@Param("actName")String actName, @Param("actCode")String actCode,
			String customer_id);

	Integer listDJNuminfo(@Param("actName")String actName, @Param("actCode")String actCode, String customer_id);

	ArrayList<DataEndInfo> listZTBGinfo(@Param("actName")String actName, @Param("actCode")String actCode,
			String customer_id);

	Integer listBGRSinfo(@Param("actName")String actName, @Param("actCode")String actCode, String customer_id);

	Double listBGCSinfo(@Param("actName")String actName, @Param("actCode")String actCode, String customer_id);

	ArrayList<DataEndInfo> listZTDJinfo(@Param("actName")String actName, @Param("actCode")String actCode,
			String customer_id);

	Integer listDJRSinfo(@Param("actName")String actName, @Param("actCode")String actCode, String customer_id);

	Integer listDJCSinfo(@Param("actName")String actName, @Param("actCode")String actCode, String customer_id);

	ArrayList<DataEndInfo> listDJDYinfo(@Param("actName")String actName, @Param("actCode")String actCode,
			String customer_id);

	Integer listDJMeiNuminfo(@Param("actName")String actName, @Param("actCode")String actCode, String customer_id);

	ArrayList<DataEndInfo> listDJRSNuminfo(@Param("actName")String actName, @Param("actCode")String actCode,
			String customer_id);

	ArrayList<DataEndInfo> listDJBZT(@Param("actName")String actName, @Param("actCode")String actCode,
			String customer_id);

	String showDW(@Param("actName")String actName, @Param("actCode")String actCode);

	ArrayList<DataEndInfo> listDataHuiInfo(@Param("actName")String actName, @Param("actCode")String actCode);

	Integer listYgdj(@Param("actCode") String activityCode);

	ArrayList<DataEndInfo> listFirstInfo(@Param("actName")String actName, @Param("actCode")String actCode);


}
