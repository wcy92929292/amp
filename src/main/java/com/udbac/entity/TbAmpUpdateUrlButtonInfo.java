package com.udbac.entity;

/**
 * @author LFQ
 */
public class TbAmpUpdateUrlButtonInfo {

	private Integer updateBatch;// 修改批次
	private String buttonName; // 按钮名称
	private String buttonId; // 按钮ID
	private String buttonType; // 统计类别
	private String involveIndex;// 涉及指标
	private String buttonEvent; // 按钮事件

	public Integer getUpdateBatch() {
		return updateBatch;
	}

	public void setUpdateBatch(Integer updateBatch) {
		this.updateBatch = updateBatch;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

	public String getInvolveIndex() {
		return involveIndex;
	}

	public void setInvolveIndex(String involveIndex) {
		this.involveIndex = involveIndex;
	}

	public String getButtonEvent() {
		return buttonEvent;
	}

	public void setButtonEvent(String buttonEvent) {
		this.buttonEvent = buttonEvent;
	}

	@Override
	public String toString() {
		return "TbAmpUpdateUrlButtonInfo [updateBatch=" + updateBatch
				+ ", buttonName=" + buttonName + ", buttonId=" + buttonId
				+ ", buttonType=" + buttonType + ", involveIndex="
				+ involveIndex + ", buttonEvent=" + buttonEvent + "]";
	}
	
}
