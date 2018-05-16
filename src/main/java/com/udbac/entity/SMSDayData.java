package com.udbac.entity;

import java.util.Date;

public class SMSDayData {

	private Date date;
	private String mic;
	private String click;
	private String visit;
	private String visitor;
	private String pv;
	private String viewtime;
	private String bounceVisit;
	private String bounceRate;
	private String exposurePV;
	private String exposureUV;
	private String clickPV;
	private String clickUV;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		if(click == null || !click.matches("\\d+")){
			return;
		}
		this.click = click;
	}

	public String getVisit() {
		return visit;
	}

	public void setVisit(String visit) {
		if(visit == null || !visit.matches("\\d+")){
			return;
		}
		this.visit = visit;
	}

	public String getVisitor() {
		return visitor;
	}

	public void setVisitor(String visitor) {
		if(visitor == null || !visitor.matches("\\d+")){
			return;
		}
		this.visitor = visitor;
	}

	public String getPv() {
		return pv;
	}

	public void setPv(String pv) {
		if(pv == null || !pv.matches("\\d+")){
			return;
		}
		this.pv = pv;
	}

	public String getViewtime() {
		return viewtime;
	}

	public void setViewtime(String viewtime) {
		if(viewtime == null || (!viewtime.matches("\\d+") && !viewtime.matches("\\d+.\\d+"))){
			return;
		}
		this.viewtime = viewtime;
	}

	public String getBounceVisit() {
		return bounceVisit;
	}

	public void setBounceVisit(String bounceVisit) {
		if(bounceVisit == null || !bounceVisit.matches("\\d+")){
			return;
		}
		this.bounceVisit = bounceVisit;
	}

	public String getBounceRate() {
		return bounceRate;
	}

	public void setBounceRate(String bounceRate) {
		this.bounceRate = bounceRate;
	}

	public String getExposurePV() {
		return exposurePV;
	}

	public void setExposurePV(String exposurePV) {
		this.exposurePV = exposurePV;
	}

	public String getExposureUV() {
		return exposureUV;
	}

	public void setExposureUV(String exposureUV) {
		this.exposureUV = exposureUV;
	}

	public String getClickPV() {
		return clickPV;
	}

	public void setClickPV(String clickPV) {
		this.clickPV = clickPV;
	}

	public String getClickUV() {
		return clickUV;
	}

	public void setClickUV(String clickUV) {
		this.clickUV = clickUV;
	}

	@Override
	public String toString() {
		return "SMSDayData [date=" + date + ", mic=" + mic + ", click=" + click
				+ ", visit=" + visit + ", visitor=" + visitor + ", pv=" + pv
				+ ", viewtime=" + viewtime + ", bounceVisit=" + bounceVisit
				+ ", bounceRate=" + bounceRate + ", exposurePV=" + exposurePV
				+ ", exposureUV=" + exposureUV + ", clickPV=" + clickPV
				+ ", clickUV=" + clickUV + "]";
	}

	
}
