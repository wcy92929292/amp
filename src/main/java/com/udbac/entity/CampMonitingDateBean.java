package com.udbac.entity;

import java.sql.Time;
import java.util.Date;

/**
 * 营销活动——活动监测数据
 * @author LiQ
 * @date 2016-04-18
 */
public class CampMonitingDateBean  {
	private Date createDate;
	private Time createTime;
	private String activityCode; // 活动编号
	private String activityName; // 活动名称
	private Date realityStartDate; // 实际上线时间
	private Date activityEndDate; // 活动结束时间
	private Integer goLiveType; // 上线类型
	private String customerName;//客户名称
	private String mediaName;//媒体名称
	private String mic;//短代码
	private String pointLocation;//投放位置
	private String putFunction;//投放形式
	private String unit;//投放单位
	private String terminalType;//终端类型
	private String marketingCode;//营销代码
	private String clickAvg;//日均点击量预估
	private String exposureAvg;//日均曝光量预估
	private Integer monitorExposurePv;//监测曝光PV
	private Integer monitorExposureUv;//监测曝光UV
	private Integer monitorClickPv;//监测点击PV
	private Integer monitorClickUv;//监测点击UV
	private Integer visit;//访次
	private Integer visitor;//访客
	private Integer click;//点击
	private Integer pv;//浏览量
	private Integer bounceVisit;//跳出访次
	private String visitsTime;//停留时间
	private String urlUpdate;//变更后URL
	private String resourcePosition;//资源定位
	private String resourceType;//资源类型
	private String effectiveConversionRate ;// 有效行为转化率
	private String businessSuccessRate ;//业务成功转化率
	private Integer fore_imp_pv;//监测曝光PV
	private Integer fore_imp_uv;//监测曝光UV
	private Integer fore_clk_pv;//监测点击PV
	private Integer fore_clk_uv;//监测点击UV
	private String ctr;//ctr
	private String jump;//跳出率
	
	
	public Integer getFore_imp_pv() {
		return fore_imp_pv;
	}
	public void setFore_imp_pv(Integer fore_imp_pv) {
		this.fore_imp_pv = fore_imp_pv;
	}
	public Integer getFore_imp_uv() {
		return fore_imp_uv;
	}
	public void setFore_imp_uv(Integer fore_imp_uv) {
		this.fore_imp_uv = fore_imp_uv;
	}
	public Integer getFore_clk_pv() {
		return fore_clk_pv;
	}
	public void setFore_clk_pv(Integer fore_clk_pv) {
		this.fore_clk_pv = fore_clk_pv;
	}
	public Integer getFore_clk_uv() {
		return fore_clk_uv;
	}
	public void setFore_clk_uv(Integer fore_clk_uv) {
		this.fore_clk_uv = fore_clk_uv;
	}
	
	public String getCtr() {
		return ctr;
	}
	public void setCtr(String ctr) {
		this.ctr = ctr;
	}
	public String getJump() {
		return jump;
	}
	public void setJump(String jump) {
		this.jump = jump;
	}
	public String getMic() {
		return mic;
	}
	public void setMic(String mic) {
		this.mic = mic;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
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
	public Integer getGoLiveType() {
		return goLiveType;
	}
	public void setGoLiveType(Integer goLiveType) {
		this.goLiveType = goLiveType;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getTerminalType() {
		return terminalType;
	}
	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
	public String getMarketingCode() {
		return marketingCode;
	}
	public void setMarketingCode(String marketingCode) {
		this.marketingCode = marketingCode;
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
	public Integer getMonitorExposurePv() {
		return monitorExposurePv;
	}
	public void setMonitorExposurePv(Integer monitorExposurePv) {
		this.monitorExposurePv = monitorExposurePv;
	}
	public Integer getMonitorExposureUv() {
		return monitorExposureUv;
	}
	public void setMonitorExposureUv(Integer monitorExposureUv) {
		this.monitorExposureUv = monitorExposureUv;
	}
	public Integer getMonitorClickPv() {
		return monitorClickPv;
	}
	public void setMonitorClickPv(Integer monitorClickPv) {
		this.monitorClickPv = monitorClickPv;
	}
	public Integer getMonitorClickUv() {
		return monitorClickUv;
	}
	public void setMonitorClickUv(Integer monitorClickUv) {
		this.monitorClickUv = monitorClickUv;
	}
	public Integer getVisit() {
		return visit;
	}
	public void setVisit(Integer visit) {
		this.visit = visit;
	}
	public Integer getVisitor() {
		return visitor;
	}
	public void setVisitor(Integer visitor) {
		this.visitor = visitor;
	}
	public Integer getClick() {
		return click;
	}
	public void setClick(Integer click) {
		this.click = click;
	}
	public Integer getPv() {
		return pv;
	}
	public void setPv(Integer pv) {
		this.pv = pv;
	}
	public Integer getBounceVisit() {
		return bounceVisit;
	}
	public void setBounceVisit(Integer bounceVisit) {
		this.bounceVisit = bounceVisit;
	}
	public String getVisitsTime() {
		return visitsTime;
	}
	public void setVisitsTime(String visitsTime) {
		this.visitsTime = visitsTime;
	}
	public String getUrlUpdate() {
		return urlUpdate;
	}
	public void setUrlUpdate(String urlUpdate) {
		this.urlUpdate = urlUpdate;
	}
	public String getResourcePosition() {
		return resourcePosition;
	}
	public void setResourcePosition(String resourcePosition) {
		this.resourcePosition = resourcePosition;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getEffectiveConversionRate() {
		return effectiveConversionRate;
	}
	public void setEffectiveConversionRate(String effectiveConversionRate) {
		this.effectiveConversionRate = effectiveConversionRate;
	}
	public String getBusinessSuccessRate() {
		return businessSuccessRate;
	}
	public void setBusinessSuccessRate(String businessSuccessRate) {
		this.businessSuccessRate = businessSuccessRate;
	}
	@Override
	public String toString() {
		return "CampMonitingDateBean [createDate=" + createDate + ", activityCode=" + activityCode + ", activityName="
				+ activityName + ", realityStartDate=" + realityStartDate + ", activityEndDate=" + activityEndDate
				+ ", goLiveType=" + goLiveType + ", customerName=" + customerName + ", mediaName=" + mediaName
				+ ", mic=" + mic + ", pointLocation=" + pointLocation + ", putFunction=" + putFunction + ", unit="
				+ unit + ", terminalType=" + terminalType + ", marketingCode=" + marketingCode + ", clickAvg="
				+ clickAvg + ", exposureAvg=" + exposureAvg + ", monitorExposurePv=" + monitorExposurePv
				+ ", monitorExposureUv=" + monitorExposureUv + ", monitorClickPv=" + monitorClickPv
				+ ", monitorClickUv=" + monitorClickUv + ", visit=" + visit + ", visitor=" + visitor + ", click="
				+ click + ", pv=" + pv + ", bounceVisit=" + bounceVisit + ", visitsTime=" + visitsTime + ", urlUpdate="
				+ urlUpdate + ", resourcePosition=" + resourcePosition + ", resourceType=" + resourceType
				+ ", effectiveConversionRate=" + effectiveConversionRate + ", businessSuccessRate="
				+ businessSuccessRate + "]";
	}
	public Time getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Time createTime) {
		this.createTime = createTime;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	

	
	
	
}
