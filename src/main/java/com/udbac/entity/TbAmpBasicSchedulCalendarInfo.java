package com.udbac.entity;

import java.util.Date;

import com.udbac.util.DateUtil;
import com.udbac.exception.ScheduleException;
import com.udbac.util.DecimalTool;

public class TbAmpBasicSchedulCalendarInfo {

	private String mic; // 短代码
	private Date putDate; // 投放日期
	private String putValue; // 投放量
	private String onlineState; // 上线状态
	private String memo; // 备注
	private String clickFile; // 上线点击核查图片路径
	private String exposureFile; // 曝光点击核查图片路径
	private String afterFile; // 后端加码核查图片路径

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public Date getPutDate() {
		return putDate;
	}

	public void setPutDate(Date putDate) {
		this.putDate = putDate;
	}

	public String getPutValue() {
		return putValue;
	}

	//投放量只能是正整数
	public void setPutValue(String putValue) throws ScheduleException{
		try{
			putValue = DecimalTool.positiveInteger(putValue);
		}catch(Exception e){
			throw new ScheduleException("投放量只能为正整数");
		}
		this.putValue = putValue;
	}

	public String getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(String onlineState) {
		this.onlineState = onlineState;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getClickFile() {
		return clickFile;
	}

	public void setClickFile(String clickFile) {
		this.clickFile = clickFile;
	}

	public String getExposureFile() {
		return exposureFile;
	}

	public void setExposureFile(String exposureFile) {
		this.exposureFile = exposureFile;
	}

	public String getAfterFile() {
		return afterFile;
	}

	public void setAfterFile(String afterFile) {
		this.afterFile = afterFile;
	}

	@Override
	public String toString() {
		return "CalendarInfo [mic=" + mic + ", putDate="
				+ DateUtil.getDateStr(putDate, null) + ", putValue=" + putValue + "]";
	}
}
