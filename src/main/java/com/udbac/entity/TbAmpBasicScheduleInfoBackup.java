package com.udbac.entity;

import java.util.Date;

/**
 * 排期备份
 * 
 * @author LFQ
 * @DATE 2016-04-27
 */
public class TbAmpBasicScheduleInfoBackup {
	
	private Date createTime;	//添加时间
	private String marketingCode; //营销代码(未加密)
	private String mic; //短代码
	private String activityCode; //活动编号
	private String mediaID;	//媒体ID
	private String urlPC;	//	目标链接地址URL(到达链接)(URL-PC)(URL-MOBILE)
	private String terminalType;	//终端类型
	private String unit;	//投放单位
	private String urlUpdate;	//变更后URL
	private Date urlUpdateTime;	//URL生效时间
	private String clickAvg;	//日均点击量预估
	private String exposureAvg;	//日均曝光量预估
	private Integer enable;	//是否有效
	private String pointLocation;	//投放位置
	private String putFunction;	//投放形式
	private String materialRequire;	//物料要求
	private String resourceType;	//资源类型
	private String planName;	//推广计划名称
	private String unitName;//推广单元名称
	private String keyName;	//关键词名称
	private String clearCode;	//广告明文代码-PC
	private String resourcePosition;	//资源定位
	private String putFrequency;	//投放频次
	private String exposureMeterial;	//曝光代码嵌入物料（是/否）
	private String supportExposure;	//可添加第三方曝光监测（是/否）
	private String supportClick;	//可添加第三方点击监测（是/否）
	private String clickMeterial;//点击代码嵌入物料（是/否）
	private String clickUrl;	//广告点击监测代码
	private String exposureUrl; //广告曝光监测代码
	private String area;	//区域定向
	private String putDate;	//投放时间安排
	private String isFrequency;	//是否需要频控
	private String numFrequency;	//频控次数
	private String periodFrequency;	//频控周期
	private String funFrequency;	//频控方法
	private String backupImg;	//备份图片确定路径
	private String schedulePath; //排期备份路径
	private String createUser; 	// 排期创建用户
	private String backupUser;  // 创建用户
	private Integer groupId = 0;		//点位分组
	private Integer micNo = 0;		//排期循序

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMarketingCode() {
		return marketingCode;
	}

	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getMediaID() {
		return mediaID;
	}

	public void setMediaID(String mediaID) {
		this.mediaID = mediaID;
	}

	public String getUrlPC() {
		return urlPC;
	}

	public void setUrlPC(String urlPC) {
		this.urlPC = urlPC;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUrlUpdate() {
		return urlUpdate;
	}

	public void setUrlUpdate(String urlUpdate) {
		this.urlUpdate = urlUpdate;
	}

	public Date getUrlUpdateTime() {
		return urlUpdateTime;
	}

	public void setUrlUpdateTime(Date urlUpdateTime) {
		this.urlUpdateTime = urlUpdateTime;
	}

	public String getClickAvg() {
		return clickAvg;
	}

	public void setClickAvg(String clickAvg) {
		this.clickAvg = clickAvg;
	}

	public String getExposureAvg() {
		return exposureAvg;
	}

	public void setExposureAvg(String exposureAvg) {
		this.exposureAvg = exposureAvg;
	}

	public Integer getEnablel() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public String getPointLocation() {
		return pointLocation;
	}

	public void setPointLocation(String pointLocation) {
		this.pointLocation = pointLocation;
	}

	public String getPutFunction() {
		return putFunction;
	}

	public void setPutFunction(String putFunction) {
		this.putFunction = putFunction;
	}

	public String getMaterialRequire() {
		return materialRequire;
	}

	public void setMaterialRequire(String materialRequire) {
		this.materialRequire = materialRequire;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getClearCode() {
		return clearCode;
	}

	public void setClearCode(String clearCode) {
		this.clearCode = clearCode;
	}

	public String getResourcePosition() {
		return resourcePosition;
	}

	public void setResourcePosition(String resourcePosition) {
		this.resourcePosition = resourcePosition;
	}

	public String getPutFrequency() {
		return putFrequency;
	}

	public void setPutFrequency(String putFrequency) {
		this.putFrequency = putFrequency;
	}

	public String getExposureMeterial() {
		return exposureMeterial;
	}

	public void setExposureMeterial(String exposureMeterial) {
		this.exposureMeterial = exposureMeterial;
	}

	public String getSupportExposure() {
		return supportExposure;
	}

	public void setSupportExposure(String supportExposure) {
		this.supportExposure = supportExposure;
	}

	public String getSupportClick() {
		return supportClick;
	}

	public void setSupportClick(String supportClick) {
		this.supportClick = supportClick;
	}

	public String getClickMeterial() {
		return clickMeterial;
	}

	public void setClickMeterial(String clickMeterial) {
		this.clickMeterial = clickMeterial;
	}

	public String getClickUrl() {
		return clickUrl;
	}

	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl;
	}

	public String getExposureUrl() {
		return exposureUrl;
	}

	public void setExposureUrl(String exposureUrl) {
		this.exposureUrl = exposureUrl;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPutDate() {
		return putDate;
	}

	public void setPutDate(String putDate) {
		this.putDate = putDate;
	}

	public String getIsFrequency() {
		return isFrequency;
	}

	public void setIsFrequency(String isFrequency) {
		this.isFrequency = isFrequency;
	}

	public String getNumFrequency() {
		return numFrequency;
	}

	public void setNumFrequency(String numFrequency) {
		this.numFrequency = numFrequency;
	}

	public String getPeriodFrequency() {
		return periodFrequency;
	}

	public void setPeriodFrequency(String periodFrequency) {
		this.periodFrequency = periodFrequency;
	}

	public String getFunFrequency() {
		return funFrequency;
	}

	public void setFunFrequency(String funFrequency) {
		this.funFrequency = funFrequency;
	}

	public String getBackupImg() {
		return backupImg;
	}

	public void setBackupImg(String backupImg) {
		this.backupImg = backupImg;
	}

	public String getSchedulePath() {
		return schedulePath;
	}

	public void setSchedulePath(String schedulePath) {
		this.schedulePath = schedulePath;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getBackupUser() {
		return backupUser;
	}

	public void setBackupUser(String backupUser) {
		this.backupUser = backupUser;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getEnable() {
		return enable;
	}
	
	public Integer getMicNo() {
		return micNo;
	}

	public void setMicNo(Integer micNo) {
		this.micNo = micNo;
	}

	@Override
	public String toString() {
		return "TbAmpBasicScheduleInfoBackup [createTime=" + createTime
				+ ", marketingCode=" + marketingCode + ", mic=" + mic
				+ ", activityCode=" + activityCode + ", mediaID=" + mediaID
				+ ", urlPC=" + urlPC + ", terminalType=" + terminalType
				+ ", unit=" + unit + ", urlUpdate=" + urlUpdate
				+ ", urlUpdateTime=" + urlUpdateTime + ", clickAvg=" + clickAvg
				+ ", exposureAvg=" + exposureAvg + ", enablel=" + enable
				+ ", pointLocation=" + pointLocation + ", putFunction="
				+ putFunction + ", materialRequire=" + materialRequire
				+ ", resourceType=" + resourceType + ", planName=" + planName
				+ ", unitName=" + unitName + ", keyName=" + keyName
				+ ", clearCode=" + clearCode + ", resourcePosition="
				+ resourcePosition + ", putFrequency=" + putFrequency
				+ ", exposureMeterial=" + exposureMeterial
				+ ", supportExposure=" + supportExposure + ", supportClick="
				+ supportClick + ", clickMeterial=" + clickMeterial
				+ ", clickUrl=" + clickUrl + ", exposureUrl=" + exposureUrl
				+ ", area=" + area + ", putDate=" + putDate + ", isFrequency="
				+ isFrequency + ", numFrequency=" + numFrequency
				+ ", periodFrequency=" + periodFrequency + ", funFrequency="
				+ funFrequency + ", backupImg=" + backupImg + ", schedulePath="
				+ schedulePath + "]";
	}
	
}
