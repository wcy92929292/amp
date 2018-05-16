package com.udbac.model;

import java.util.Date;

/****
 * 营销活动Model
 */
public class AdMonitorModel {

	private String mic; //短代码
	private Date daytime; //时间
	private Integer vv; //次数
	private Integer uv; //人数
	private Integer clk; //点击
	private Integer pv; //浏览量
	private Integer bounce_times; //跳出次数
	private Double time_spent; //平均访问时长

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

	public Integer getVv() {
		return vv;
	}

	public void setVv(Integer vv) {
		this.vv = vv;
	}

	public Integer getUv() {
		return uv;
	}

	public void setUv(Integer uv) {
		this.uv = uv;
	}

	public Integer getClk() {
		return clk;
	}

	public void setClk(Integer clk) {
		this.clk = clk;
	}

	public Integer getPv() {
		return pv;
	}

	public void setPv(Integer pv) {
		this.pv = pv;
	}

	public Integer getBounce_times() {
		return bounce_times;
	}

	public void setBounce_times(Integer bounce_times) {
		this.bounce_times = bounce_times;
	}

	public Double getTime_spent() {
		return time_spent;
	}

	public void setTime_spent(Double time_spent) {
		this.time_spent = time_spent;
	}

	@Override
	public String toString() {
		return "AdMonitorModel [mic=" + mic + ", daytime=" + daytime + ", vv=" + vv + ", uv=" + uv + ", clk=" + clk
				+ ", pv=" + pv + ", bounce_times=" + bounce_times + ", time_spent=" + time_spent + "]";
	}
	
	

}
