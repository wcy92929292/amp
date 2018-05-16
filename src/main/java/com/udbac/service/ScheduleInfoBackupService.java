package com.udbac.service;


/**
 * 排期备份服务
 * @author LFQ
 * 
 */
public interface ScheduleInfoBackupService {
	
	/**
	 * 根据活动编号备份排期
	 * @param activityCode
	 * @return
	 */
	int scheduleBackup(String activityCode);

}
