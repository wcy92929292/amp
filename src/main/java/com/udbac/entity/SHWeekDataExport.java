package com.udbac.entity;

import java.util.Date;

/**
 * 上海周报特例
 * 
 * @author han 2016-08-04
 */
public class SHWeekDataExport {

	private String activityCode; // 活动编号
	private String activityName; // 活动名称
	private String mediaName; // 媒体名称
	private String mediaType; // 媒体名称
	private String pointLocation; // 投放位置
	private String terminalType;
	private String putFunction; // 投放形式
	private Date putDate; // 投放日期
	private String mic; // 识别码
	private Integer supportExposure; // 可添加第三方曝光监测（是/否） 1是/0否
	private Integer supportClick; // 可添加第三方点击监测（是/否） 1是/0否
	private Double impPv; // 曝光次数
	private Double impUv; // 曝光人数
	private Double clkPv; // 点击次数
	private Double clkUv; // 点击人数
	private Double visit; // 访次
	private Double visitor; // 访客
	private Double click;
	private Double pageView; // 浏览量
	private Double bounceTimes; // 跳出次数
	private Double bounceRate; // 跳出率
	private Double viewTime; // 平均停留时长
	private Date startDate; // 开始时间
	private Date endDate; // 结束时间
	private Double exposureAvg; // 曝光预估
	private Double clickAvg; // 点击预估
	private String urlPc; // URL链接地址
	private String unit; // 投放单位
	private Double putValue; // 投放量
	private Integer sumDay;

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

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaType() {
		return mediaType;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
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

	public Date getPutDate() {
		return putDate;
	}

	public void setPutDate(Date putDate) {
		this.putDate = putDate;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public Integer getSupportExposure() {
		return supportExposure;
	}

	public void setSupportExposure(Integer supportExposure) {
		this.supportExposure = supportExposure;
	}

	public Integer getSupportClick() {
		return supportClick;
	}

	public void setSupportClick(Integer supportClick) {
		this.supportClick = supportClick;
	}

	public Double getImpPv() {
		return impPv;
	}

	public void setImpPv(Double impPv) {
		this.impPv = impPv;
	}

	public Double getImpUv() {
		return impUv;
	}

	public void setImpUv(Double impUv) {
		this.impUv = impUv;
	}

	public Double getClkPv() {
		return clkPv;
	}

	public void setClkPv(Double clkPv) {
		this.clkPv = clkPv;
	}

	public Double getClkUv() {
		return clkUv;
	}

	public void setClkUv(Double clkUv) {
		this.clkUv = clkUv;
	}

	public Double getVisit() {
		return visit;
	}

	public void setVisit(Double visit) {
		this.visit = visit;
	}

	public Double getVisitor() {
		return visitor;
	}

	public void setVisitor(Double visitor) {
		this.visitor = visitor;
	}

	public Double getClick() {
		return click;
	}

	public void setClick(Double click) {
		this.click = click;
	}

	public Double getPageView() {
		return pageView;
	}

	public void setPageView(Double pageView) {
		this.pageView = pageView;
	}

	public Double getBounceTimes() {
		return bounceTimes;
	}

	public void setBounceTimes(Double bounceTimes) {
		this.bounceTimes = bounceTimes;
	}

	public Double getBounceRate() {
		return bounceRate;
	}

	public void setBounceRate(Double bounceRate) {
		this.bounceRate = bounceRate;
	}

	public Double getViewTime() {
		return viewTime;
	}

	public void setViewTime(Double viewTime) {
		this.viewTime = viewTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getExposureAvg() {
		return exposureAvg;
	}

	public void setExposureAvg(Double exposureAvg) {
		this.exposureAvg = exposureAvg;
	}

	public Double getClickAvg() {
		return clickAvg;
	}

	public void setClickAvg(Double clickAvg) {
		this.clickAvg = clickAvg;
	}

	public String getUrlPc() {
		return urlPc;
	}

	public void setUrlPc(String urlPc) {
		this.urlPc = urlPc;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getPutValue() {
		return putValue;
	}

	public void setPut_value(Double putValue) {
		this.putValue = putValue;
	}

	public Integer getSumDay() {
		return sumDay;
	}

	public void setSumDay(Integer sumDay) {
		this.sumDay = sumDay;
	}

	public void setPutValue(Double putValue) {
		this.putValue = putValue;
	}

	@Override
	public String toString() {
		return "[activityCode=" + activityCode + ", activityName=" + activityName + ", mediaName="
				+ mediaName + ", mediaType=" + mediaType + ", pointLocation=" + pointLocation + ", terminalType="
				+ terminalType + ", putFunction=" + putFunction + ", putDate=" + putDate + ", mic=" + mic
				+ ", supportExposure=" + supportExposure + ", supportClick=" + supportClick + ", impPv=" + impPv
				+ ", impUv=" + impUv + ", clkPv=" + clkPv + ", clkUv=" + clkUv + ", visit=" + visit + ", visitor="
				+ visitor + ", click=" + click + ", pageView=" + pageView + ", bounceTimes=" + bounceTimes
				+ ", bounceRate=" + bounceRate + ", viewTime=" + viewTime + ", startDate=" + startDate + ", endDate="
				+ endDate + ", exposureAvg=" + exposureAvg + ", clickAvg=" + clickAvg + ", urlPc=" + urlPc + ", unit="
				+ unit + ", putValue=" + putValue + ", sumDay=" + sumDay + " \n]";
	}

	

}
