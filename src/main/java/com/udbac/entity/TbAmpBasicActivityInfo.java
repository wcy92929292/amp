package com.udbac.entity;

import java.util.Date;

/**
 * 活动信息基础表
 * 
 * @author LFQ
 * @date 2016/04/05
 */
public class TbAmpBasicActivityInfo {

	private String createDate; // 创建日期
	private String activityCode; // 活动编号
	private String activityName; // 活动名称
	private TbAmpBasicCustomerInfo customer; // 客户
	private Integer activityState; // 活动状态
	private Integer goLiveType; // 上线类型
	private Date predictStartDate; // 预计上线时间
	private Date realityStartDate; // 实际上线时间
	private Date activityEndDate; // 活动结束时间
	private TbAmpBasicUserInfo portPeople; // 接口人
	private TbAmpBasicUserInfo monitorPeople; // 监测中心人员
	private TbAmpBasicUserInfo frontSupportPeople; // 前端技术支撑人员
	private TbAmpBasicUserInfo afterSupportPeople; // 后端技术支撑人员
	private String schemeState; // 是否有监测方案
	private TbAmpBasicUserInfo updateUser; // 方案修改人
	private Date updateDate; // 方案修改时间
	private String schedulePath; // 排期文件路径
	private String memo; // 备注
	private String parentIdf; // 是否为父级
	private String parentActivityCode; // 父级的活动编号
	private Integer scheduleType;
	
	private String realName;//接口人
	private Integer countMedia;//媒体数量
	private String unit;//省

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public TbAmpBasicCustomerInfo getCustomer() {
		return customer;
	}

	public void setCustomer(TbAmpBasicCustomerInfo customer) {
		this.customer = customer;
	}

	public Integer getActivityState() {
		return activityState;
	}

	public void setActivityState(Integer activityState) {
		this.activityState = activityState;
	}

	public Integer getGoLiveType() {
		return goLiveType;
	}

	public void setGoLiveType(Integer goLiveType) {
		this.goLiveType = goLiveType;
	}

	public Date getPredictStartDate() {
		return predictStartDate;
	}

	public void setPredictStartDate(Date predictStartDate) {
		this.predictStartDate = predictStartDate;
	}

	public Date getRealityStartDate() {
		return realityStartDate;
	}

	public void setRealityStartDate(Date realityStartDate) {
		this.realityStartDate = realityStartDate;
	}

	public Date getActivityEndDate() {
		return activityEndDate;
	}

	public void setActivityEndDate(Date activityEndDate) {
		this.activityEndDate = activityEndDate;
	}

	public TbAmpBasicUserInfo getPortPeople() {
		return portPeople;
	}

	public void setPortPeople(TbAmpBasicUserInfo portPeople) {
		this.portPeople = portPeople;
	}

	public TbAmpBasicUserInfo getMonitorPeople() {
		return monitorPeople;
	}

	public void setMonitorPeople(TbAmpBasicUserInfo monitorPeople) {
		this.monitorPeople = monitorPeople;
	}

	public TbAmpBasicUserInfo getFrontSupportPeople() {
		return frontSupportPeople;
	}

	public void setFrontSupportPeople(TbAmpBasicUserInfo frontSupportPeople) {
		this.frontSupportPeople = frontSupportPeople;
	}

	public TbAmpBasicUserInfo getAfterSupportPeople() {
		return afterSupportPeople;
	}

	public void setAfterSupportPeople(TbAmpBasicUserInfo afterSupportPeople) {
		this.afterSupportPeople = afterSupportPeople;
	}

	public String getSchemeState() {
		return schemeState;
	}

	public void setSchemeState(String schemeState) {
		this.schemeState = schemeState;
	}

	public TbAmpBasicUserInfo getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(TbAmpBasicUserInfo updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getSchedulePath() {
		return schedulePath;
	}

	public void setSchedulePath(String schedulePath) {
		this.schedulePath = schedulePath;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getParentIdf() {
		return parentIdf;
	}

	public void setParentIdf(String parentIdf) {
		this.parentIdf = parentIdf;
	}

	public String getParentActivityCode() {
		return parentActivityCode;
	}

	public void setParentActivityCode(String parentActivityCode) {
		this.parentActivityCode = parentActivityCode;
	}
	
	

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getCountMedia() {
		return countMedia;
	}

	public void setCountMedia(Integer countMedia) {
		this.countMedia = countMedia;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public Integer getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(Integer scheduleType) {
		this.scheduleType = scheduleType;
	}

	@Override
	public String toString() {
		return "TbAmpBasicActivityInfo [createDate=" + createDate
				+ ", activityCode=" + activityCode + ", activityName="
				+ activityName + ", customer=" + customer + ", activityState="
				+ activityState + ", goLiveType=" + goLiveType
				+ ", predictStartDate=" + predictStartDate
				+ ", realityStartDate=" + realityStartDate
				+ ", activityEndDate=" + activityEndDate + ", portPeople="
				+ portPeople + ", monitorPeople=" + monitorPeople
				+ ", frontSupportPeople=" + frontSupportPeople
				+ ", afterSupportPeople=" + afterSupportPeople
				+ ", schemeState=" + schemeState + ", updateUser=" + updateUser
				+ ", updateDate=" + updateDate + ", schedulePath="
				+ schedulePath + ", memo=" + memo + ", parentIdf=" + parentIdf
				+ ", parentActivityCode=" + parentActivityCode + "]";
	}
}
