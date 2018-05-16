package com.udbac.entity;

import java.util.Date;
import java.util.List;

/**
 * 活动排期信息
 * 2016年09月12日
 * @author han
 *
 */
public class AutoScheduleInfo {

	// 媒体
	private String mediaName;// 媒体名称
	// 活动
	private String activityName; // 活动名称
	private Date realityStartDate; // 实际上线时间
	private Date activityEndDate; // 活动结束时间
	private String customerCode; // 客户编号
	// 排期信息
	private String activityCode; // 活动编号
	private String mic; // 短代码
	private String urlPc; // 目标链接地址URL(到达链接)(URL-PC)(URL-MOBILE)
	private String terminalType; // 终端类型
	private String unit; // 投放单位
	private String clickAvg; // 日均点击量预估
	private String exposureAvg; // 日均曝光量预估
	// 活动扩展信息
	private String pointLocation; // 投放位置
	private String putFunction; // 投放形式
	private String materialRequire; // 物料要求
	private String supportExposure; // 可添加第三方曝光监测（是/否）
	private String supportClick; // 可添加第三方点击监测（是/否）
	private String area; // 区域定向
	// 活动日历信息
	private List<AutoSchedulCalendarInfo> calendarInfo; // 日历安排

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
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

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public String getUrlPc() {
		return urlPc;
	}

	public void setUrlPc(String urlPc) {
		this.urlPc = urlPc;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<AutoSchedulCalendarInfo> getCalendarInfo() {
		return calendarInfo;
	}

	public void setCalendarInfo(List<AutoSchedulCalendarInfo> calendarInfo) {
		this.calendarInfo = calendarInfo;
	}

	@Override
	public String toString() {
		return "AutoScheduleInfo [mediaName=" + mediaName + ", activityName=" + activityName + ", realityStartDate="
				+ realityStartDate + ", activityEndDate=" + activityEndDate + ", customerCode=" + customerCode
				+ ", activityCode=" + activityCode + ", mic=" + mic + ", urlPc=" + urlPc + ", terminalType="
				+ terminalType + ", unit=" + unit + ", clickAvg=" + clickAvg + ", exposureAvg=" + exposureAvg
				+ ", pointLocation=" + pointLocation + ", putFunction=" + putFunction + ", materialRequire="
				+ materialRequire + ", supportExposure=" + supportExposure + ", supportClick=" + supportClick
				+ ", area=" + area + ", calendarInfo=" + calendarInfo + "]";
	}

}
