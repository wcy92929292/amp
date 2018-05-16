package com.udbac.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.udbac.dao.CampScheduleDao;
import com.udbac.entity.CampBasicActivityInfo;
import com.udbac.entity.CampScheduleInfo;
import com.udbac.entity.TbAmpBasicScheduleInfoBackup;
import com.udbac.service.CampScheduleService;

@Service
public class CampScheduleServiceImpl implements CampScheduleService {

	@Autowired
	CampScheduleDao dao;
	
	@Override
	public List<CampScheduleInfo> listAll(String actCode) {
		List<CampScheduleInfo> list = dao.listAll(actCode);
		return list;
	}

	@Override
	public List<CampScheduleInfo> listSche(String actCode) {
		
		return dao.findScheduleByActCode(actCode);
	}

	/**
	 * 将scheduleInfo 的信息转移到 backup  Bean中
	 * @param scheduleInfo
	 * @param backup
	 */
	void Schedule2Backup(CampScheduleInfo sche,TbAmpBasicScheduleInfoBackup backup){
		
		backup.setActivityCode(sche.getActivityCode());
		backup.setMarketingCode(sche.getMarketingCode());
		backup.setMic(sche.getMic());
		backup.setMediaID(String.valueOf(sche.getMedia().getMediaId()));
		backup.setUrlPC(sche.getUrlPc());
		backup.setTerminalType(sche.getTerminalType());
		backup.setUnit(sche.getUnit());
		backup.setUrlUpdate(sche.getUrlUpdate());
		backup.setClickAvg(sche.getClickAvg());
		backup.setExposureAvg(sche.getExposureAvg());
		backup.setEnable(Integer.parseInt(sche.getEnable()));
		backup.setPointLocation(sche.getExtenInfo().getPointLocation());
		backup.setPutFunction(sche.getExtenInfo().getPutFunction());
		backup.setMaterialRequire(sche.getExtenInfo().getMaterialRequire());
		backup.setResourceType(sche.getExtenInfo().getResourceType());
		backup.setPlanName(sche.getExtenInfo().getPlanName());
		backup.setUnitName(sche.getExtenInfo().getUnitName());
		backup.setKeyName(sche.getExtenInfo().getKeyName());
		backup.setClearCode(sche.getExtenInfo().getClearCode());
		backup.setResourcePosition(sche.getExtenInfo().getResourcePosition());
		backup.setPutFrequency(sche.getExtenInfo().getPutFrequency());
		backup.setExposureMeterial(sche.getExtenInfo().getExposureMeterial());
		backup.setSupportExposure(sche.getExtenInfo().getSupportExposure());
		backup.setSupportClick(sche.getExtenInfo().getSupportClick());
		backup.setClickMeterial(sche.getExtenInfo().getClickMeterial());
		backup.setClickUrl(sche.getExtenInfo().getClickUrl());
		backup.setExposureUrl(sche.getExtenInfo().getExposureUrl());
		backup.setArea(sche.getExtenInfo().getArea());
		backup.setIsFrequency(sche.getIsFrequency());
		backup.setNumFrequency(sche.getNumFrequency());
		backup.setPeriodFrequency(sche.getPeriodFrequency());
		backup.setFunFrequency(sche.getFunFrequency());
		backup.setSchedulePath(sche.getActivityInfo().getSchedulePath());
		
	}//end Schedule2Backup
	
	
	/**
	 * 根据活动编号下载排期
	 */
	@Override
	public CampBasicActivityInfo downloadSchedule(String activityCode) {
		
		CampBasicActivityInfo activityInfo = dao.findByCode(activityCode);
		
		return activityInfo;
	}//end downloadSchedule()

	@Override
	public void updateClickAvg(String clickAvg,String mic) {
		dao.updateClickAvg(clickAvg,mic);
	}

	@Override
	public void updateExposureAvg(String exposureAvg, String mic) {
		dao.updateExposureAvg(exposureAvg, mic);
	}
	
	
	/**
	 * 备份排期信息
	 */
	/*@SuppressWarnings("rawtypes")
	public void backupSchedule(String activityCode,String backupImgPath){
		
		List<CampScheduleInfo> scheduleInfos = dao.findScheduleByActCode(activityCode);
		CampScheduleInfo scheduleInfo = null;
		List<CampBasicSchedulCalendarInfo> calendarInfos = null;
		CampBasicSchedulCalendarInfo calendarInfo = null;
		int i,j;
		String year = "",month= "";
		List<TbAmpBasicScheduleInfoBackup> backupList = new LinkedList<>();
		TbAmpBasicScheduleInfoBackup backup = null;
		
		
		Map<String,Map> micMap = null;
		Map<String,Map> yearMap = null;
		Map<String,Map> monthMap = null;
		Map<String,String> dayMap = null;
		*//**
		 * 将要投放时间安排拼接成这样的形式。
		 {mic:{
			   	2015:{
			     	11:{01:value,
			          	02:value,
			          	03:value},
			      	12:{04:value}
			     },
			    2016:{
			    	01:{01:value,
			          	02:value,
			          	03:value},
			      	02:{04:value}
			     }
			   }
		  }
		 *//*
		for (i = 0; i < scheduleInfos.size(); i++) {
			
			scheduleInfo = scheduleInfos.get(i);
			backup = new TbAmpBasicScheduleInfoBackup();
			micMap = new HashMap<>();
			yearMap = new HashMap<>();
			monthMap = null;
			dayMap = null;
			year = "";month= "";
			
			calendarInfos = scheduleInfo.getCalendarInfo();
			
			for (j = 0; j < calendarInfos.size(); j++) {
				calendarInfo = calendarInfos.get(j);
				if(!year.equals(DateUtil.getDateStr(calendarInfo.getPutDate(), "yyyy"))){
					year = DateUtil.getDateStr(calendarInfo.getPutDate(), "yyyy");
					monthMap = new HashMap<>();
					micMap.put(scheduleInfo.getMic(), yearMap);
					yearMap.put(year, monthMap);
				}
				if(!month.equals(DateUtil.getDateStr(calendarInfo.getPutDate(), "MM"))){
					month = DateUtil.getDateStr(calendarInfo.getPutDate(), "MM");
					dayMap = new HashMap<>();
					monthMap.put(month, dayMap);
				}
				dayMap.put(DateUtil.getDateStr(calendarInfo.getPutDate(), "dd"), calendarInfo.getPutValue());
			}//end for (j < calendarInfos.size())
			
			 backup.setPutDate(micMap.toString().replaceAll("=", "\\:").replaceAll(" ", ""));
			 backup.setBackupImg(backupImgPath);
			 
			 Schedule2Backup(scheduleInfo,backup);
			 backupList.add(backup);
		}//end for(i < scheduleInfos.size())
		
		scheduleInfoBackupDao.scheduleBackup(backupList,);
		
	}//end backupSchedule
*/	
	
}
