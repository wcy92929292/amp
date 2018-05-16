package com.udbac.entity;

import java.util.Date;
import java.util.List;

/**
 * 活动排期信息
 * 
 * @author han
 *
 */
public class CampScheduleInfo {
	private Date createTime; // 添加时间
	private String marketingCode; // 营销代码(未加密)
	private String activityCode; // 活动编号
	private String mic; // 短代码
	private TbAmpBasicMediaInfo media; // 媒体信息
	private String urlPc; // 目标链接地址URL(到达链接)(URL-PC)(URL-MOBILE)
	private String terminalType; // 终端类型
	private String unit; // 投放单位
	private String urlUpdate; // 变更后URL
	private Date urlUpdateTime; // URL生效时间
	private String clickAvg; // 日均点击量预估
	private String exposureAvg; // 日均曝光量预估
	private String enable; // 是否有效
	private String isFrequency; // 是否需要频控
	private String numFrequency; // 频控次数
	private String periodFrequency; // 频控周期
	private String funFrequency; // 频控方法
	private CampBasicScheduleInfo extenInfo; // 排期扩展信息
	private List<CampBasicSchedulCalendarInfo> calendarInfo; // 排期安排
	private CampBasicActivityInfo activityInfo; // 活动信息
	private String impDataCaliber; // 曝光去重口径
	private String clkDataCaliber; // 点击去重口径
	
	public String getImpDataCaliber() {
		return impDataCaliber;
	}

	public void setImpDataCaliber(String impDataCaliber) {
		this.impDataCaliber = impDataCaliber;
	}

	public String getClkDataCaliber() {
		return clkDataCaliber;
	}

	public void setClkDataCaliber(String clkDataCaliber) {
		this.clkDataCaliber = clkDataCaliber;
	}

	

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

	public TbAmpBasicMediaInfo getMedia() {
		return media;
	}

	public void setMedia(TbAmpBasicMediaInfo media) {
		this.media = media;
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

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
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

	public CampBasicScheduleInfo getExtenInfo() {
		return extenInfo;
	}

	public void setExtenInfo(CampBasicScheduleInfo extenInfo) {
		this.extenInfo = extenInfo;
	}

	public List<CampBasicSchedulCalendarInfo> getCalendarInfo() {
		return calendarInfo;
	}

	public void setCalendarInfo(List<CampBasicSchedulCalendarInfo> calendarInfo) {
		this.calendarInfo = calendarInfo;
	}

	public CampBasicActivityInfo getActivityInfo() {
		return activityInfo;
	}

	public void setActivityInfo(CampBasicActivityInfo activityInfo) {
		this.activityInfo = activityInfo;
	}

	@Override
	public String toString() {
		return "CampScheduleInfo [createTime=" + createTime + ", marketingCode=" + marketingCode + ", activityCode="
				+ activityCode + ", mic=" + mic + ", media=" + media + ", urlPc=" + urlPc + ", terminalType="
				+ terminalType + ", unit=" + unit + ", urlUpdate=" + urlUpdate + ", urlUpdateTime=" + urlUpdateTime
				+ ", clickAvg=" + clickAvg + ", exposureAvg=" + exposureAvg + ", enable=" + enable + ", isFrequency="
				+ isFrequency + ", numFrequency=" + numFrequency + ", periodFrequency=" + periodFrequency
				+ ", funFrequency=" + funFrequency + ", extenInfo=" + extenInfo + ", calendarInfo=" + calendarInfo
				+ ", activityInfo=" + activityInfo + "]";
	}

}
