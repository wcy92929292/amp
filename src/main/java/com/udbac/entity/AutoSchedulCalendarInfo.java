package com.udbac.entity;

import java.util.Date;

public class AutoSchedulCalendarInfo {

	private String mic; // 短代码
	private Date putDate; // 投放日期
	private String putValue; // 投放量

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

	public void setPutValue(String putValue) {
		this.putValue = putValue;
	}

}
