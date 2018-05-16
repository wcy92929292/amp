package com.udbac.util;

import java.io.File;
import java.util.List;

/**
 * 配置上传文件的路径
 * 
 * @author LFQ
 *
 */
public class FilePathManager {

	// 监测方案强制上线上传文件后缀名
	private List<String> onlineStateFileType;

	// 强制上线图片保存路径
	private String activityOnlineFile;

	// 排期文件保存路径
	private String scheduleFilePath;

	// 普通排期模板规则配置文件
	private String genSchedule;

	// 强制插码图片保存路径
	private String updateSchedulePath;

	private String checkClickFilePath;

	// 集团排期模板规则配置文件
	private String jtScheduleInfoPath;

	// 百度关键词排期模板规则配置文件
	private String keyScheduleInfoPath;

	// 同步跳转表保存路径
	private String micAndUrlPath;
	
	// 媒体名称映射文件路径
	private String mediaFilePath;

	private String tmpExcel;

	public String getMicAndUrlPath() {
		return micAndUrlPath;
	}

	public void setMicAndUrlPath(String micAndUrlPath) {
		this.micAndUrlPath = micAndUrlPath;
	}

	public String getJtScheduleInfoPath() {
		return jtScheduleInfoPath;
	}

	public void setJtScheduleInfoPath(String jtScheduleInfoPath) {
		this.jtScheduleInfoPath = jtScheduleInfoPath;
	}

	public String getCheckClickFilePath() {
		return checkClickFilePath;
	}

	public void setCheckClickFilePath(String checkClickFilePath) {
		this.checkClickFilePath = checkClickFilePath;
	}

	public String getActivityOnlineFile() {
		return activityOnlineFile;
	}

	public void setActivityOnlineFile(String activityOnlineFile) {
		File file = new File(activityOnlineFile);
		if (!file.exists()) {
			file.mkdirs();
		}

		this.activityOnlineFile = activityOnlineFile;
	}

	public List<String> getOnlineStateFileType() {
		return onlineStateFileType;
	}

	public void setOnlineStateFileType(List<String> onlineStateFileType) {
		this.onlineStateFileType = onlineStateFileType;
	}

	public String getScheduleFilePath() {
		return scheduleFilePath;
	}

	public void setScheduleFilePath(String scheduleFilePath) {
		File file = new File(scheduleFilePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		this.scheduleFilePath = scheduleFilePath;
	}

	public String getGenSchedule() {
		return genSchedule;
	}

	public void setGenSchedule(String genSchedule) {
		this.genSchedule = genSchedule;
	}

	public String getUpdateSchedulePath() {
		return updateSchedulePath;
	}

	public void setUpdateSchedulePath(String updateSchedulePath) {
		File file = new File(updateSchedulePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		this.updateSchedulePath = updateSchedulePath;
	}

	public String getKeyScheduleInfoPath() {
		return keyScheduleInfoPath;
	}

	public void setKeyScheduleInfoPath(String keyScheduleInfoPath) {
		this.keyScheduleInfoPath = keyScheduleInfoPath;
	}

	public String getTmpExcel() {
		return tmpExcel;
	}

	public void setTmpExcel(String tmpExcel) {
		File file = new File(tmpExcel);
		if (!file.exists()) {
			file.mkdirs();
		}
		this.tmpExcel = tmpExcel;
	}

	public String getMediaFilePath() {
		return mediaFilePath;
	}

	public void setMediaFilePath(String mediaFilePath) {
		this.mediaFilePath = mediaFilePath;
	}
	
	

}
