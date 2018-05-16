package com.udbac.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AfterData {

	private Date startDate;

	private Date endDate;

	private String mic;

	private Date daytime;

	private Integer visit;

	private Integer visitor;

	private Integer click;

	private Integer pageview;

	private Integer bounceTimes;

	private Double timeSpent;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public Date getDaytime() {
		return daytime;
	}

	public void setDaytime(Date daytime) {
		this.daytime = daytime;
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

	public Integer getPageview() {
		return pageview;
	}

	public void setPageview(Integer pageview) {
		this.pageview = pageview;
	}

	public Integer getBounceTimes() {
		return bounceTimes;
	}

	public void setBounceTimes(Integer bounceTimes) {
		this.bounceTimes = bounceTimes;
	}

	public Double getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(Double timeSpent) {
		this.timeSpent = timeSpent;
	}

	@Override
	public String toString() {
		return sdf.format(daytime) + "," + mic + "," + visit + ", " + visitor + ", " + click + ", " + pageview + ", "
				+ bounceTimes+ ", " + timeSpent;
	}

}
