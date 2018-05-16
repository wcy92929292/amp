package com.udbac.model;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.udbac.entity.TbAmpBasicSchedulCalendarInfo;

public class RefreshCalendarModel {

	private String mic;
	private Date minDate;
	private Date maxDate;
	private String schedulePath;
	private Set<TbAmpBasicSchedulCalendarInfo> calendars = new TreeSet<>();

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public Date getMinDate() {
		return minDate;
	}

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public Set<TbAmpBasicSchedulCalendarInfo> getCalendars() {
		return calendars;
	}

	public void setCalendars(Set<TbAmpBasicSchedulCalendarInfo> calendars) {
		this.calendars = calendars;
	}
	
	
	public String getSchedulePath() {
		return schedulePath;
	}

	public void setSchedulePath(String schedulePath) {
		this.schedulePath = schedulePath;
	}

	@Override
	public String toString() {
		return "RefreshCalendarModel [mic=" + mic + ", minDate=" + minDate
				+ ", maxDate=" + maxDate + ", calendars=" + calendars + "]";
	}
}
