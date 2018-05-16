package com.udbac.model;

/**
 * 保存监测数据的节点
 * 
 * @author LFQ
 * @date 2016-08-22
 */
public class OnlineDataNode {

	// hour=20160822_01 himp=414407 hclk=7063 adid=TestMMarketReader01 imp=0
	// clk=0
	private Integer hour; // 小时
	private String mic; // 识别码
	private Integer imp; // 曝光数据
	private Integer clk; // 点击数据
	private Integer himp; // 当前小时总的曝光
	private Integer hclk; // 当前小时总的点击
	private Integer dimp; // 当天总的曝光
	private Integer dclk; // 当天总的点击

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public Integer getImp() {
		return imp;
	}

	public void setImp(Integer imp) {
		this.imp = imp;
	}

	public Integer getClk() {
		return clk;
	}

	public void setClk(Integer clk) {
		this.clk = clk;
	}

	public Integer getHimp() {
		return himp;
	}

	public void setHimp(Integer himp) {
		this.himp = himp;
	}

	public Integer getHclk() {
		return hclk;
	}

	public void setHclk(Integer hclk) {
		this.hclk = hclk;
	}

	public Integer getDimp() {
		return dimp;
	}

	public void setDimp(Integer dimp) {
		this.dimp = dimp;
	}

	public Integer getDclk() {
		return dclk;
	}

	public void setDclk(Integer dclk) {
		this.dclk = dclk;
	}

	@Override
	public String toString() {
		return "OnlineDataNode [hour=" + hour + ", mic=" + mic + ", imp=" + imp
				+ ", clk=" + clk + ", himp=" + himp + ", hclk=" + hclk
				+ ", dimp=" + dimp + ", dclk=" + dclk + "]";
	}
}
