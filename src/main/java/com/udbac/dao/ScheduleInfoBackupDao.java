package com.udbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.TbAmpBasicScheduleInfoBackup;

public interface ScheduleInfoBackupDao {

	void scheduleBackup(@Param("scheduleBackups") List<TbAmpBasicScheduleInfoBackup> scheduleBackups,@Param("backupUser")String backupUser);
}
