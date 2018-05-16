package com.udbac.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.dao.DataExportDao;
import com.udbac.entity.DataExport;
import com.udbac.entity.TbAmpBasicScheduleInfo;
import com.udbac.service.DataExportService;
import com.udbac.util.FilePathManager;

@Service
public class DataExportServiceImpl implements DataExportService {

	
	@Autowired
	DataExportDao exportDao;
	
	@Autowired
	private FilePathManager filePathManager;

	@Override
	public List<DataExport> listInfo(Date date,String actName,String actCode,String customer_id,Date dte) {
			if(dte!=null){
				Date dt1=dte;
				return exportDao.listInfo(date,actName,actCode,customer_id,dt1);
			}else{
				Date dt1=new Date();
				dt1=getNextDay(dt1);
				return exportDao.listInfo(date,actName,actCode,customer_id,dt1);
			}
		}
		
		
//	}
	
	@Override
	public List queryCustomer(String province) {
		return exportDao.queryCustomer(province);
	}
	
	/**
	 * 
	 */
	@Override
	public Integer sumDay(String customer_id,Date dt, String actName, String actCode,Date dte) {
		
		if(dte!=null){
			Date dt1=dte;
			return exportDao.sumDay(customer_id,dt, actName, actCode,dt1);
		}else{
			Date dt1=new Date();
			dt1=getNextDay(dt1);
			return exportDao.sumDay(customer_id,dt, actName, actCode,dt1);
		}
	}
	//获得系统的前一天
	 public static Date getNextDay(Date date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			date = calendar.getTime();
			return date;
		}
	@Override
	public Integer sumDays(String customer_id, String dt, String actName,
			String actCode) {
		return exportDao.sumDays(customer_id,dt, actName, actCode);
	}
	@Override
	public ArrayList<DataExport> listInfos(String dt, String actName,
			String actCode, String customer_id) {

		return exportDao.listInfos(customer_id,dt, actName, actCode);
	}
	@Override
	public Integer sumHour(String customer_id, Date dt, String actName,
			String actCode, Date dte,String mic) {
		if(dte!=null){
			Date dt1=dte;
			return exportDao.sumHour(customer_id,dt, actName, actCode,dt1,mic);
		}else{
			Date dt1=new Date();
			dt1=getNextDay(dt1);
			return exportDao.sumHour(customer_id,dt, actName, actCode,dt1,mic);
		}
	}
	@Override
	public List<DataExport> listHourInfo(Date date, String actName,
			String actCode, String customer_id, Date dte, String mic) {
		if(dte!=null){
			Date dt1=dte;
			return exportDao.listHourInfo(date,actName,actCode,customer_id,dt1,mic);
		}else{
			Date dt1=new Date();
			dt1=getNextDay(dt1);
			return exportDao.listHourInfo(date,actName,actCode,customer_id,dt1,mic);
		}
	}


	@Override
	public Integer checkCode(String actCode) {
		return exportDao.checkCode(actCode);
	}


	@Override
	public Integer sumPZ(String actCode, Date dt, Date dte) {
		String  flag=getFlag(actCode);
		return exportDao.sumPZ(actCode,dt,dte,flag);
	}

	@Override
	public String selectProName(String actCode) {
		
		return exportDao.selectProName(actCode);
	}
	/**
	 * 品专报表导出
	 * 通过活动编号获得真实的周期
	 */
	@Override
	public String selectRealPer(String actCode) {
		String minDay=exportDao.minDay(actCode);
		String maxDay=exportDao.maxDay(actCode);
		return minDay.substring(0,10).replace("-", ".")+"-"+maxDay.substring(0,10).replace("-", ".");
	}


	@Override
	public ArrayList<DataExport> listInfoPZ(String actCode, Date dt, Date dte) {
		String  flag = getFlag(actCode);
		return exportDao.listInfoPZ(actCode,dt,dte,flag);
	}


	@Override
	public ArrayList<DataExport> listPZDay(String actCode, Date dt, Date dte, Integer Num) {
		String  flag=getFlag(actCode);	
		
		if(Num==0){
			Num=1;
		}else{
			Num=Num*10000+1;
		}
		return exportDao.listPZDay(actCode,dt,dte,flag,Num);
	}


	@Override
	public String saveEndPoints(Date dt, String mailbox, String mic) {
		  String flag = "";
		    try {
				exportDao.saveEndPoints(dt,mailbox,mic);
				flag="success";
			} catch (Exception e) {
				flag="error";
			}
		return flag;
	}


	@Override
	public Integer checkMic(String mic) {
	
		return exportDao.checkMic(mic);
	}
	
	//品专的判断该活动是海尔集团的
	@SuppressWarnings("unused")
	private static String getFlag(String actCode) {
		String flag="0";
		 if(actCode.indexOf("HRJT") != -1 && actCode != null){
			   flag="1";
		   }else{
			 flag="0";  
		   }
		return flag;
		
	}
	
	/**
	 * 曝光口径和点击口径更新
	 * 
	 * @param list
	 */
	@Override
	public void updateDataCaliber(List<TbAmpBasicScheduleInfo> list) {
		exportDao.updateDataCaliber(list);
	}
	
}
