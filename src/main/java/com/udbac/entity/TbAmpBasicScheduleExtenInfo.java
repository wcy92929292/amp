package com.udbac.entity;

import com.udbac.exception.ScheduleException;
import com.udbac.util.DecimalTool;

/**
 * 排期扩展信息
 * 
 * @author LFQ
 * 
 */
public class TbAmpBasicScheduleExtenInfo {

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
		this.pointLocation = pointLocation.length() < 50 ? pointLocation : pointLocation.substring(0,50);
	}

	public String getPutFunction() {
		return putFunction;
	}

	public void setPutFunction(String putFunction) {
		
		this.putFunction = putFunction.length() < 50 ? putFunction : putFunction.substring(0, 50);
	}

	public String getMaterialRequire() {
		return materialRequire;
	}

	public void setMaterialRequire(String materialRequire) {
		this.materialRequire = materialRequire.length() < 40 ? materialRequire :materialRequire.substring(0,40);
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

	public void setPlanName(String planName) throws ScheduleException {
		if(planName == null || "".equals(planName)){
			throw new ScheduleException("推广计划名称不能为空");
		}
		this.planName = planName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) throws ScheduleException {
		if(unitName == null || "".equals(unitName)){
			throw new ScheduleException("推广单元名称不能为空");
		}
		this.unitName = unitName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) throws ScheduleException {
		if(keyName == null || "".equals(keyName)){
			throw new ScheduleException("关键词名称不能为空");
		}
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
		this.resourcePosition = resourcePosition.length() < 28 ? resourcePosition : resourcePosition.substring(0, 28);
	}

	public String getPutFrequency() {
		return putFrequency;
	}

	public void setPutFrequency(String putFrequency) throws ScheduleException {
		if(putFrequency == null || "".equals(putFrequency) || "-".equals(putFrequency)){
			return;
		}
		try{
			putFrequency = DecimalTool.positiveInteger(putFrequency);
		}catch(Exception e){
			throw new ScheduleException("投放频次只能为正整数，或者为空");
		}
		
		this.putFrequency = putFrequency;
	}

	public String getExposureMeterial() {
		return exposureMeterial;
	}

	public void setExposureMeterial(String exposureMeterial) throws ScheduleException {
		if("是".equals(exposureMeterial) || "1".equals(exposureMeterial)){
			this.exposureMeterial = "1";
		}else if("否".equals(exposureMeterial) || "0".equals(exposureMeterial)){
			this.exposureMeterial = "0";
		}else{
			throw new ScheduleException("曝光代码是否嵌入物料：是/否");
		}
	}		

	public String getSupportExposure() {
		return supportExposure;
	}

	public void setSupportExposure(String supportExposure) throws ScheduleException {
		if("是".equals(supportExposure) || "1".equals(supportExposure)){
			this.supportExposure = "1";
		}else if("否".equals(supportExposure) || "0".equals(supportExposure)){
			this.supportExposure = "0";
		}else{
			throw new ScheduleException("支持曝光监测：是/否");
		}
	}

	public String getSupportClick() {
		return supportClick;
	}

	public void setSupportClick(String supportClick) throws ScheduleException {
		if("是".equals(supportClick) || "1".equals(supportClick)){
			this.supportClick = "1";
		}else if("否".equals(supportClick) || "0".equals(supportClick)){
			this.supportClick = "0";
		}else{
			throw new ScheduleException("支持点击监测：是/否");
		}
	}

	public String getClickMeterial() {
		return clickMeterial;
	}

	public void setClickMeterial(String clickMeterial) throws ScheduleException {
		if("是".equals(clickMeterial) || "1".equals(clickMeterial)){
			this.clickMeterial = "1";
		}else if("否".equals(clickMeterial) || "0".equals(clickMeterial)){
			this.clickMeterial = "0";
		}else{
			throw new ScheduleException("点击代码嵌入物料：是/否");
		}
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
		return "TbAmpBasicScheduleExtenInfo [mic=" + mic + ", pointLocation="
				+ pointLocation + ", putFunction=" + putFunction
				+ ", materialRequire=" + materialRequire + ", resourceType="
				+ resourceType + ", planName=" + planName + ", unitName="
				+ unitName + ", keyName=" + keyName + ", clearCode="
				+ clearCode + ", resourcePosition=" + resourcePosition
				+ ", putFrequency=" + putFrequency + ", exposureMeterial="
				+ exposureMeterial + ", supportExposure=" + supportExposure
				+ ", supportClick=" + supportClick + ", clickMeterial="
				+ clickMeterial + ", clickUrl=" + clickUrl + ", exposureUrl="
				+ exposureUrl + ", area=" + area + "]";
	}

	
	
}
