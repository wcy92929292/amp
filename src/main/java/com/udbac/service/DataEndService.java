package com.udbac.service;

import java.util.ArrayList;
import java.util.List;

import com.udbac.entity.DataEndInfo;

public interface DataEndService {
	//结案数据基础表
	List<DataEndInfo> listDataEndInfo(String actName,String actCode, String customer_id);
	//频次曝光分媒体
	List<DataEndInfo> listDataFMTInfo(String actName,String actCode,String customer_id);
	//频次曝光分媒体的媒体数量
	Integer ListMeiNum(String actName, String actCode, String customer_id);
	//频次曝光分广告位
	List<DataEndInfo> listDataFGGWInfo(String actName, String actCode,
			String customer_id);
	//频次曝光广告位的数量
	Integer listDataGGWNum(String actName, String actCode, String customer_id);
	//点击频次分媒体
	ArrayList<DataEndInfo> listDataDJFMTInfo(String actName, String actCode,
			String customer_id);
	//点击媒体的数量
	Integer listDatDJNum(String actName, String actCode, String customer_id);
	//曝光整体
	ArrayList<DataEndInfo> listDataZTBGInfo(String actName, String actCode,
			String customer_id);
	//曝光人数汇总
	Integer listDataBGRSInfo(String actName, String actCode, String customer_id);
	//曝光次数汇总
	Double listDataBGCSInfo(String actName, String actCode, String customer_id);
	//点击整体
	ArrayList<DataEndInfo> listDataDJZTInfo(String actName, String actCode,
			String customer_id);
	//点击人数汇总
	Integer listDataDJRSInfo(String actName, String actCode, String customer_id);
	//点击次数汇总
	Integer listDataDJCSInfo(String actName, String actCode, String customer_id);
	//点击地域分布
	ArrayList<DataEndInfo> listDataDJDYInfo(String actName, String actCode,
			String customer_id);
	//点击地域分布媒体名称
	Integer listDataDJMeiNumInfo(String actName, String actCode, String customer_id);
	//点击分媒体
	ArrayList<DataEndInfo> listDataDJRSNumInfo(String actName, String actCode,
			String customer_id);
	//点击地域分布的饼状图的取值
	ArrayList<DataEndInfo> listDataDJDBZTInfo(String actName, String actCode,
			String customer_id);
	//通过活动编号获得当前的活动名称以及活动的开始及结束时间
	DataEndInfo listDateInfo(String actName,String actCode);
	//通过模糊查询将查询出来的活动名称放在下拉框中
	List checkName(String actName);
	//判断活动表里面是否包含活动编号
	Integer checkCode(String actCode);
	//判断活动表里面是否存在该活动名称
	Integer sumName(String actName);
	//判断活动表里面是否存在该活动名称
	Integer sumSame(String actName,String actCode);
	//存放活动的汇总点位信息
	String showDW(String actName, String actCode);
	//存放的是结案数据报表的头一张，汇总报表
	ArrayList<DataEndInfo> listDataHuiInfo(String actName, String actCode,
			String customer_id);
	Integer listYgdj(String activityCode);
	//分媒体广告位的数量
	ArrayList<DataEndInfo> listDataFGGFirstInfo(String actName, String actCode,
			String customer_id);

}
