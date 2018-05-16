package com.udbac.entity;

/**
 * 排期基础信息
 * 
 * @author han
 * @DATE 2016-05-29
 */
public class CampBasicScheduleInfo {
	private String mic; // 短代码
	private String pointLocation; // 投放位置
	private String putFunction; // 投放形式
	private String materialRequire; // 物料要求
	private String resourceType; // 资源类型
	private String planName; // 推广计划名称
	private String unitName; // 推广单元名称
	private String keyName; // 关键词名称
	private String clearCode; // 广告明文代码-PC
	private String resourcePosition; // 资源定位
	private String putFrequency; // 投放频次
	private String exposureMeterial; // 曝光代码嵌入物料（是/否）
	private String supportExposure; // 可添加第三方曝光监测（是/否）
	private String supportClick; // 可添加第三方点击监测（是/否）
	private String clickMeterial; // 点击代码嵌入物料（是/否）
	private String clickUrl; // 广告点击监测代码
	private String exposureUrl; // 广告曝光监测代码
	private String area; // 区域定向

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
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

	@Override
	public String toString() {
		return "CampBasicScheduleInfo [mic=" + mic + ", pointLocation=" + pointLocation + ", putFunction=" + putFunction
				+ ", materialRequire=" + materialRequire + ", resourceType=" + resourceType + ", planName=" + planName
				+ ", unitName=" + unitName + ", keyName=" + keyName + ", clearCode=" + clearCode + ", resourcePosition="
				+ resourcePosition + ", putFrequency=" + putFrequency + ", exposureMeterial=" + exposureMeterial
				+ ", supportExposure=" + supportExposure + ", supportClick=" + supportClick + ", clickMeterial="
				+ clickMeterial + ", clickUrl=" + clickUrl + ", exposureUrl=" + exposureUrl + ", area=" + area + "]";
	}

}
