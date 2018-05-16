package com.udbac.entity;

import java.util.Date;
import java.util.List;

/**
 * 地域频次导出
 * 
 */
public class RegionDataExport {

	private String activityName; // 活动名称
	private String mediaName; // 媒体名称
	private Date daytime; // 投放日期
	private Date startDate;
	private Date endDate;
	private Date eDate;
	private String pointLocation;
	private String putFunction;
	private String mic; // 识别码
	private Integer support_exposure; // 可添加第三方曝光监测（是/否） 1是/0否
	private Integer support_click; // 可添加第三方点击监测（是/否） 1是/0否
	private Double dirtyImpUV; // 标准清洗曝光人数
	private Double dirtyImpPV; // 标准清洗曝光次数
	private Double dirtyClkUV; // 标准清洗点击人数
	private Double dirtyClkPV; // 标准清洗点击次数
	private Double cleanImpUV; // 去重清洗曝光人数
	private Double cleanImpPV; // 去重清洗曝光次数
	private Double cleanClkUV; // 去重清洗点击人数
	private Double cleanClkPV; // 去重清洗点击次数
	private Double dcleanImpUV; // DSP清洗曝光人数
	private Double dcleanImpPV; // DSP清洗曝光次数
	private Double dcleanClkUV; // DSP清洗点击人数
	private Double dcleanClkPV; // DSP清洗点击次数

	private Double dirtyImpUV1; // 标准清洗曝光人数
	private Double dirtyImpPV1; // 标准清洗曝光次数
	private Double dirtyClkUV1; // 标准清洗点击人数
	private Double dirtyClkPV1; // 标准清洗点击次数
	private Double cleanImpUV1; // 去重清洗曝光人数
	private Double cleanImpPV1; // 去重清洗曝光次数
	private Double cleanClkUV1; // 去重清洗点击人数
	private Double cleanClkPV1; // 去重清洗点击次数
	private Double dcleanImpUV1; // DSP清洗曝光人数
	private Double dcleanImpPV1; // DSP清洗曝光次数
	private Double dcleanClkUV1; // DSP清洗点击人数
	private Double dcleanClkPV1; // DSP清洗点击次数
	private String impDataCaliber; // 口径
	private String clkDataCaliber; // 口径
	List<RegionTimes> nums; // 次数
	private String province; // 省份
	private Double sumDirtyImpPv; // 标准总曝光次数
	private Double sumCleanImpPv;// 清洗总曝光次数
	private Double sumDcleanImpPv;// 深度清洗总曝光次数
	private Double sumDirtyClkPv; // 标准总曝光次数
	private Double sumCleanClkPv;// 清洗总曝光次数
	private Double sumDcleanClkPv;// 深度清洗总曝光次数

	private Double sumImpPv; // 总曝光PV
	private Double sumImpUv;// 总曝光UV
	private Double sumClkPv;// 总点击PV
	private Double sumClkUv;// 总点击UV

	private Double impPv;// 曝光PV
	private Double impUv;// 曝光UV
	private Double clkPv;// 点击PV
	private Double clkUv;// 点击UV

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

	public Double getDirtyImpUV1() {
		return dirtyImpUV1;
	}

	public void setDirtyImpUV1(Double dirtyImpUV1) {
		this.dirtyImpUV1 = dirtyImpUV1;
	}

	public Double getDirtyImpPV1() {
		return dirtyImpPV1;
	}

	public void setDirtyImpPV1(Double dirtyImpPV1) {
		this.dirtyImpPV1 = dirtyImpPV1;
	}

	public Double getDirtyClkUV1() {
		return dirtyClkUV1;
	}

	public void setDirtyClkUV1(Double dirtyClkUV1) {
		this.dirtyClkUV1 = dirtyClkUV1;
	}

	public Double getDirtyClkPV1() {
		return dirtyClkPV1;
	}

	public void setDirtyClkPV1(Double dirtyClkPV1) {
		this.dirtyClkPV1 = dirtyClkPV1;
	}

	public Double getCleanImpUV1() {
		return cleanImpUV1;
	}

	public void setCleanImpUV1(Double cleanImpUV1) {
		this.cleanImpUV1 = cleanImpUV1;
	}

	public Double getCleanImpPV1() {
		return cleanImpPV1;
	}

	public void setCleanImpPV1(Double cleanImpPV1) {
		this.cleanImpPV1 = cleanImpPV1;
	}

	public Double getCleanClkUV1() {
		return cleanClkUV1;
	}

	public void setCleanClkUV1(Double cleanClkUV1) {
		this.cleanClkUV1 = cleanClkUV1;
	}

	public Double getCleanClkPV1() {
		return cleanClkPV1;
	}

	public void setCleanClkPV1(Double cleanClkPV1) {
		this.cleanClkPV1 = cleanClkPV1;
	}

	public Double getDcleanImpUV1() {
		return dcleanImpUV1;
	}

	public void setDcleanImpUV1(Double dcleanImpUV1) {
		this.dcleanImpUV1 = dcleanImpUV1;
	}

	public Double getDcleanImpPV1() {
		return dcleanImpPV1;
	}

	public void setDcleanImpPV1(Double dcleanImpPV1) {
		this.dcleanImpPV1 = dcleanImpPV1;
	}

	public Double getDcleanClkUV1() {
		return dcleanClkUV1;
	}

	public void setDcleanClkUV1(Double dcleanClkUV1) {
		this.dcleanClkUV1 = dcleanClkUV1;
	}

	public Double getDcleanClkPV1() {
		return dcleanClkPV1;
	}

	public void setDcleanClkPV1(Double dcleanClkPV1) {
		this.dcleanClkPV1 = dcleanClkPV1;
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

	public Date getDaytime() {
		return daytime;
	}

	public void setDaytime(Date daytime) {
		this.daytime = daytime;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public Double getDcleanImpUV() {
		return dcleanImpUV;
	}

	public void setDcleanImpUV(Double dcleanImpUV) {
		this.dcleanImpUV = dcleanImpUV;
	}

	public Double getDcleanImpPV() {
		return dcleanImpPV;
	}

	public void setDcleanImpPV(Double dcleanImpPV) {
		this.dcleanImpPV = dcleanImpPV;
	}

	public Double getDcleanClkUV() {
		return dcleanClkUV;
	}

	public void setDcleanClkUV(Double dcleanClkUV) {
		this.dcleanClkUV = dcleanClkUV;
	}

	public Double getDcleanClkPV() {
		return dcleanClkPV;
	}

	public void setDcleanClkPV(Double dcleanClkPV) {
		this.dcleanClkPV = dcleanClkPV;
	}

	public Double getDirtyImpUV() {
		return dirtyImpUV;
	}

	public void setDirtyImpUV(Double dirtyImpUV) {
		this.dirtyImpUV = dirtyImpUV;
	}

	public Double getDirtyImpPV() {
		return dirtyImpPV;
	}

	public void setDirtyImpPV(Double dirtyImpPV) {
		this.dirtyImpPV = dirtyImpPV;
	}

	public Double getDirtyClkUV() {
		return dirtyClkUV;
	}

	public void setDirtyClkUV(Double dirtyClkUV) {
		this.dirtyClkUV = dirtyClkUV;
	}

	public Double getDirtyClkPV() {
		return dirtyClkPV;
	}

	public void setDirtyClkPV(Double dirtyClkPV) {
		this.dirtyClkPV = dirtyClkPV;
	}

	public Double getCleanImpUV() {
		return cleanImpUV;
	}

	public void setCleanImpUV(Double cleanImpUV) {
		this.cleanImpUV = cleanImpUV;
	}

	public Double getCleanImpPV() {
		return cleanImpPV;
	}

	public void setCleanImpPV(Double cleanImpPV) {
		this.cleanImpPV = cleanImpPV;
	}

	public Double getCleanClkUV() {
		return cleanClkUV;
	}

	public void setCleanClkUV(Double cleanClkUV) {
		this.cleanClkUV = cleanClkUV;
	}

	public Double getCleanClkPV() {
		return cleanClkPV;
	}

	public void setCleanClkPV(Double cleanClkPV) {
		this.cleanClkPV = cleanClkPV;
	}


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

	public Integer getSupport_exposure() {
		return support_exposure;
	}

	public void setSupport_exposure(Integer support_exposure) {
		this.support_exposure = support_exposure;
	}

	public Integer getSupport_click() {
		return support_click;
	}

	public void setSupport_click(Integer support_click) {
		this.support_click = support_click;
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

	public String getPointLocation() {
		return pointLocation;
	}

	public void setPointLocation(String pointLocation) {
		this.pointLocation = pointLocation;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public List<RegionTimes> getNums() {
		return nums;
	}

	public void setNums(List<RegionTimes> nums) {
		this.nums = nums;
	}

	public String getPutFunction() {
		return putFunction;
	}

	public void setPutFunction(String putFunction) {
		this.putFunction = putFunction;
	}

	public Double getSumDirtyImpPv() {
		return sumDirtyImpPv;
	}

	public void setSumDirtyImpPv(Double sumDirtyImpPv) {
		this.sumDirtyImpPv = sumDirtyImpPv;
	}

	public Double getSumCleanImpPv() {
		return sumCleanImpPv;
	}

	public void setSumCleanImpPv(Double sumCleanImpPv) {
		this.sumCleanImpPv = sumCleanImpPv;
	}

	public Double getSumDcleanImpPv() {
		return sumDcleanImpPv;
	}

	public void setSumDcleanImpPv(Double sumDcleanImpPv) {
		this.sumDcleanImpPv = sumDcleanImpPv;
	}

	public Double getSumDirtyClkPv() {
		return sumDirtyClkPv;
	}

	public void setSumDirtyClkPv(Double sumDirtyClkPv) {
		this.sumDirtyClkPv = sumDirtyClkPv;
	}

	public Double getSumCleanClkPv() {
		return sumCleanClkPv;
	}

	public void setSumCleanClkPv(Double sumCleanClkPv) {
		this.sumCleanClkPv = sumCleanClkPv;
	}

	public Double getSumDcleanClkPv() {
		return sumDcleanClkPv;
	}

	public void setSumDcleanClkPv(Double sumDcleanClkPv) {
		this.sumDcleanClkPv = sumDcleanClkPv;
	}

	public Date geteDate() {
		return eDate;
	}

	public void seteDate(Date eDate) {
		this.eDate = eDate;
	}

	@Override
	public String toString() {
		return "RegionDataExport [activityName=" + activityName + ", mediaName=" + mediaName + ", daytime=" + daytime
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", eDate=" + eDate + ", pointLocation="
				+ pointLocation + ", putFunction=" + putFunction + ", mic=" + mic + ", support_exposure="
				+ support_exposure + ", support_click=" + support_click + ", dirtyImpUV=" + dirtyImpUV + ", dirtyImpPV="
				+ dirtyImpPV + ", dirtyClkUV=" + dirtyClkUV + ", dirtyClkPV=" + dirtyClkPV + ", cleanImpUV="
				+ cleanImpUV + ", cleanImpPV=" + cleanImpPV + ", cleanClkUV=" + cleanClkUV + ", cleanClkPV="
				+ cleanClkPV + ", dcleanImpUV=" + dcleanImpUV + ", dcleanImpPV=" + dcleanImpPV + ", dcleanClkUV="
				+ dcleanClkUV + ", dcleanClkPV=" + dcleanClkPV + ", dirtyImpUV1=" + dirtyImpUV1 + ", dirtyImpPV1="
				+ dirtyImpPV1 + ", dirtyClkUV1=" + dirtyClkUV1 + ", dirtyClkPV1=" + dirtyClkPV1 + ", cleanImpUV1="
				+ cleanImpUV1 + ", cleanImpPV1=" + cleanImpPV1 + ", cleanClkUV1=" + cleanClkUV1 + ", cleanClkPV1="
				+ cleanClkPV1 + ", dcleanImpUV1=" + dcleanImpUV1 + ", dcleanImpPV1=" + dcleanImpPV1 + ", dcleanClkUV1="
				+ dcleanClkUV1 + ", dcleanClkPV1=" + dcleanClkPV1 + ", impDataCaliber=" + impDataCaliber
				+ ", clkDataCaliber=" + clkDataCaliber + ", nums=" + nums + ", province=" + province
				+ ", sumDirtyImpPv=" + sumDirtyImpPv + ", sumCleanImpPv=" + sumCleanImpPv + ", sumDcleanImpPv="
				+ sumDcleanImpPv + ", sumDirtyClkPv=" + sumDirtyClkPv + ", sumCleanClkPv=" + sumCleanClkPv
				+ ", sumDcleanClkPv=" + sumDcleanClkPv + ", sumImpPv=" + sumImpPv + ", sumImpUv=" + sumImpUv
				+ ", sumClkPv=" + sumClkPv + ", sumClkUv=" + sumClkUv + ", impPv=" + impPv + ", impUv=" + impUv
				+ ", clkPv=" + clkPv + ", clkUv=" + clkUv + "]";
	}


}
