package com.udbac.entity;

import java.util.Date;

/**
 * 周报导出
 * 
 */
public class WeekDataExport {

	private String customerName; // 客户名称
	private String customerCode; // 客户编号
	private String activityCode; // 活动编号
	private String activityName; // 活动名称
	private String mediaName; // 媒体名称
	private String pointLocation; // 投放位置
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
	private String terminal_type; // 终端类型
	private String unit; // 投放单位
	private Double put_value; // 投放量
	private String sumDate; // 投放天数
	private Integer periodFrequency;
	private Double sumImpPv; // 曝光次数
	private Double sumImpUv; // 曝光人数
	private Double sumClkPv; // 点击次数
	private Double sumClkUv; // 点击人数
	private Double sumVisit; // 访次
	private Double sumVisitor; // 访客
	private Double sumClick;
	private Double sumPageView; // 浏览量
	private Double sumBounceTimes; // 跳出次数
	private Double sumBounceRate; // 跳出率
	private Double sumViewTime; // 平均停留时长
	private Double sumExposureAvg; // 曝光预估
	private Double sumClickAvg; // 点击预估
	private Integer  group_id;//分组合并的标记位
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
		if (mic != null) {
			this.mic = mic;
		}
	}

	public Double getVisit() {
		return visit;
	}

	public void setVisit(Double visit) {
		this.visit = visit;
	}

	public Double getClick() {
		return click;
	}

	public void setClick(Double click) {
		this.click = click;
	}

	public Double getVisitor() {
		return visitor;
	}

	public void setVisitor(Double visitor) {
		this.visitor = visitor;
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
		if (startDate != null && (this.startDate == null || this.startDate.compareTo(startDate) > 0)) {
			this.startDate = startDate;
		}
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		if (endDate != null && (this.endDate == null || this.endDate.compareTo(endDate) < 0)) {
			this.endDate = endDate;
		}
		this.endDate = endDate;
	}

	public String getTerminal_type() {
		return terminal_type;
	}

	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
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

	public Double getPut_value() {
		return put_value;
	}

	public void setPut_value(Double put_value) {
		this.put_value = put_value;
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

	public String getSumDate() {
		return sumDate;
	}

	public void setSumDate(String sumDate) {
		this.sumDate = sumDate;
	}

	public Double getSumImpPv() {
		return sumImpPv;
	}

	public void setSumImpPv(Double sumImpPv) {
		this.sumImpPv = sumImpPv;
	}

	public Double getSumImpUv() {
		return sumImpUv;
	}

	public void setSumImpUv(Double sumImpUv) {
		this.sumImpUv = sumImpUv;
	}

	public Double getSumClkPv() {
		return sumClkPv;
	}

	public void setSumClkPv(Double sumClkPv) {
		this.sumClkPv = sumClkPv;
	}

	public Double getSumClkUv() {
		return sumClkUv;
	}

	public void setSumClkUv(Double sumClkUv) {
		this.sumClkUv = sumClkUv;
	}

	public Double getSumVisit() {
		return sumVisit;
	}

	public void setSumVisit(Double sumVisit) {
		this.sumVisit = sumVisit;
	}

	public Double getSumVisitor() {
		return sumVisitor;
	}

	public void setSumVisitor(Double sumVisitor) {
		this.sumVisitor = sumVisitor;
	}

	public Double getSumClick() {
		return sumClick;
	}

	public void setSumClick(Double sumClick) {
		this.sumClick = sumClick;
	}

	public Double getSumPageView() {
		return sumPageView;
	}

	public void setSumPageView(Double sumPageView) {
		this.sumPageView = sumPageView;
	}

	public Double getSumBounceTimes() {
		return sumBounceTimes;
	}

	public void setSumBounceTimes(Double sumBounceTimes) {
		this.sumBounceTimes = sumBounceTimes;
	}

	public Double getSumBounceRate() {
		return sumBounceRate;
	}

	public void setSumBounceRate(Double sumBounceRate) {
		this.sumBounceRate = sumBounceRate;
	}

	public Double getSumViewTime() {
		return sumViewTime;
	}

	public void setSumViewTime(Double sumViewTime) {
		this.sumViewTime = sumViewTime;
	}

	public Double getSumExposureAvg() {
		return sumExposureAvg;
	}

	public void setSumExposureAvg(Double sumExposureAvg) {
		this.sumExposureAvg = sumExposureAvg;
	}

	public Double getSumClickAvg() {
		return sumClickAvg;
	}

	public void setSumClickAvg(Double sumClickAvg) {
		this.sumClickAvg = sumClickAvg;
	}

	public Integer getPeriodFrequency() {
		return periodFrequency;
	}

	public void setPeriodFrequency(Integer periodFrequency) {
		this.periodFrequency = periodFrequency;
	}

	

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	@Override
	public String toString() {
		return "WeekDataExport [customerName=" + customerName + ", customerCode=" + customerCode + ", activityCode="
				+ activityCode + ", activityName=" + activityName + ", mediaName=" + mediaName + ", pointLocation="
				+ pointLocation + ", putFunction=" + putFunction + ", putDate=" + putDate + ", mic=" + mic
				+ ", supportExposure=" + supportExposure + ", supportClick=" + supportClick + ", impPv=" + impPv
				+ ", impUv=" + impUv + ", clkPv=" + clkPv + ", clkUv=" + clkUv + ", visit=" + visit + ", visitor="
				+ visitor + ", click=" + click + ", pageView=" + pageView + ", bounceTimes=" + bounceTimes
				+ ", bounceRate=" + bounceRate + ", viewTime=" + viewTime + ", startDate=" + startDate + ", endDate="
				+ endDate + ", exposureAvg=" + exposureAvg + ", clickAvg=" + clickAvg + ", urlPc=" + urlPc
				+ ", terminal_type=" + terminal_type + ", unit=" + unit + ", put_value=" + put_value + ", sumDate="
				+ sumDate + ", periodFrequency=" + periodFrequency + ", sumImpPv=" + sumImpPv + ", sumImpUv=" + sumImpUv
				+ ", sumClkPv=" + sumClkPv + ", sumClkUv=" + sumClkUv + ", sumVisit=" + sumVisit + ", sumVisitor="
				+ sumVisitor + ", sumClick=" + sumClick + ", sumPageView=" + sumPageView + ", sumBounceTimes="
				+ sumBounceTimes + ", sumBounceRate=" + sumBounceRate + ", sumViewTime=" + sumViewTime
				+ ", sumExposureAvg=" + sumExposureAvg + ", sumClickAvg=" + sumClickAvg + ", group_id=" + group_id + "]";
	}

}
