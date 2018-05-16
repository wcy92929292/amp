package com.udbac.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class TbAmpUpdateUrlInfo {

	private Integer updateBatch; // 修改批次
	private Date createDate; // 添加日期
	private String createUser; // 添加用户ID
	private String checkUser; // 审核用户ID
	private Date checkDate; // 审核时间
	private String urlUpdate; // 变更后URL
	private Date urlUpdateTime; // URL生效时间
	private String updateState; // 生效状态
	private Set<TbAmpUpdatUrlAndSchedule> mics;	//修改的短代码
	private String memo; // 备注
	private List<TbAmpUpdateUrlButtonInfo> buttonInfo;	//按钮信息

	public Integer getUpdateBatch() {
		return updateBatch;
	}

	public void setUpdateBatch(Integer updateBatch) {
		this.updateBatch = updateBatch;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
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

	public String getUpdateState() {
		return updateState;
	}

	public void setUpdateState(String updateState) {
		this.updateState = updateState;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}


	public List<TbAmpUpdateUrlButtonInfo> getButtonInfo() {
		return buttonInfo;
	}

	public void setButtonInfo(List<TbAmpUpdateUrlButtonInfo> buttonInfo) {
		this.buttonInfo = buttonInfo;
	}

	
	@Override
	public String toString() {
		return "TbAmpUpdateUrlInfo [updateBatch=" + updateBatch
				+ ", createDate=" + createDate + ", createUser=" + createUser
				+ ", checkUser=" + checkUser + ", checkDate=" + checkDate
				+ ", urlUpdate=" + urlUpdate + ", urlUpdateTime="
				+ urlUpdateTime + ", updateState=" + updateState + ", mics="
				+ mics + ", memo=" + memo + ", buttonInfo=" + buttonInfo + "]";
	}

	public Set<TbAmpUpdatUrlAndSchedule> getMics() {
		return mics;
	}

	public void setMics(Set<TbAmpUpdatUrlAndSchedule> mics) {
		this.mics = mics;
	}

}
