package com.udbac.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.dao.DataEndDao;
import com.udbac.entity.DataEndInfo;
import com.udbac.service.DataEndService;
@Service
public class DataEndServiceImpl implements DataEndService {
	@Autowired
	DataEndDao dataEndDao;
	@Override
	public List<DataEndInfo> listDataEndInfo( String actName,
			String actCode,String customer_id) {
		return dataEndDao.listInJCfo(actName,actCode,customer_id);
	}
	@Override
	public List<DataEndInfo> listDataFMTInfo(String actName, String actCode,
			String customer_id) {
		return dataEndDao.listFMTfo(actName,actCode,customer_id);
	}
	@Override
	public Integer ListMeiNum(String actName, String actCode, String customer_id) {
		return dataEndDao.listMeiNum(actName,actCode,customer_id);
	}
	@Override
	public List<DataEndInfo> listDataFGGWInfo(String actName, String actCode,
			String customer_id) {
		
		return dataEndDao.listGGfo(actName,actCode,customer_id);
	}
	@Override
	public Integer listDataGGWNum(String actName, String actCode, String customer_id) {
		
		return dataEndDao.listGGWNum(actName,actCode,customer_id);
	}
	@Override
	public ArrayList<DataEndInfo> listDataDJFMTInfo(String actName,
			String actCode, String customer_id) {
		return dataEndDao.listDJFMTinfo(actName,actCode,customer_id);
	}
	@Override
	public Integer listDatDJNum(String actName, String actCode, String customer_id) {
		return dataEndDao.listDJNuminfo(actName,actCode,customer_id);
	}
	@Override
	public ArrayList<DataEndInfo> listDataZTBGInfo(String actName,
			String actCode, String customer_id) {
		return dataEndDao.listZTBGinfo(actName,actCode,customer_id);
	}
	@Override
	public Integer listDataBGRSInfo(String actName, String actCode,
			String customer_id) {
		return dataEndDao.listBGRSinfo(actName,actCode,customer_id);
	}
	@Override
	public Double listDataBGCSInfo(String actName, String actCode,
			String customer_id) {
		return dataEndDao.listBGCSinfo(actName,actCode,customer_id);
	}
	@Override
	public ArrayList<DataEndInfo> listDataDJZTInfo(String actName,
			String actCode, String customer_id) {
		return dataEndDao.listZTDJinfo(actName,actCode,customer_id);
	}
	@Override
	public Integer listDataDJRSInfo(String actName, String actCode,
			String customer_id) {
		return dataEndDao.listDJRSinfo(actName,actCode,customer_id);
	}
	@Override
	public Integer listDataDJCSInfo(String actName, String actCode,
			String customer_id) {
		return dataEndDao.listDJCSinfo(actName,actCode,customer_id);
	}
	@Override
	public ArrayList<DataEndInfo> listDataDJDYInfo(String actName,
			String actCode, String customer_id) {
		return dataEndDao.listDJDYinfo(actName,actCode,customer_id);
	}
	@Override
	public Integer listDataDJMeiNumInfo(String actName, String actCode,
			String customer_id) {
		
		return dataEndDao.listDJMeiNuminfo(actName,actCode,customer_id);
	}
	@Override
	public ArrayList<DataEndInfo> listDataDJRSNumInfo(String actName,
			String actCode, String customer_id) {
		
		return dataEndDao.listDJRSNuminfo(actName,actCode,customer_id);
	}
	@Override
	public ArrayList<DataEndInfo> listDataDJDBZTInfo(String actName,
			String actCode, String customer_id) {
		return dataEndDao.listDJBZT(actName,actCode,customer_id);
	}
	@Override
	public DataEndInfo listDateInfo(String actName,String actCode) {
		return dataEndDao.listDateInfo(actName,actCode);
	}
	@Override
	public List checkName(String actName) {
		return dataEndDao.checkName(actName);
	}
	@Override
	public Integer checkCode(String actCode) {
		return null;
	}
	@Override
	public Integer sumName(String actName) {
		return dataEndDao.sumName(actName);
	}
	@Override
	public Integer sumSame(String actName,String actCode) {
		return dataEndDao.sumSame(actName, actCode);
	}
	@Override
	public String showDW(String actName, String actCode) {
		
		return dataEndDao.showDW(actName, actCode);
	}
	@Override
	public ArrayList<DataEndInfo> listDataHuiInfo(String actName,
			String actCode, String customer_id) {
		return dataEndDao.listDataHuiInfo(actName, actCode);
	}
	@Override
	public Integer listYgdj(String activityCode) {
		return dataEndDao.listYgdj(activityCode);
	}
	@Override
	public ArrayList<DataEndInfo> listDataFGGFirstInfo(String actName,
			String actCode, String customer_id) {
		return dataEndDao.listFirstInfo(actName, actCode);
	}

	

}
