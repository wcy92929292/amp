package com.udbac.entity;

import java.util.Date;

public class RegionSum2 { 
	private String mic; // 短代码
	private Date daytime;// 时间
	private Integer times;// 频次
	private Double dirty_uv; // 标准总曝光次数
	private Double clean_uv;// 清洗总曝光次数
	private Double dclean_uv;// 深度清洗总曝光次数
	private String cls;
	
	public void setMic(String mic) {
		this.mic = mic;
	}
	public void setDaytime(Date daytime) {
		this.daytime = daytime;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	
	public Double getDirty_uv() {
		return dirty_uv;
	}
	public void setDirty_uv(Double dirty_uv) {
		this.dirty_uv = dirty_uv;
	}
	public Double getClean_uv() {
		return clean_uv;
	}
	public void setClean_uv(Double clean_uv) {
		this.clean_uv = clean_uv;
	}
	public Double getDclean_uv() {
		return dclean_uv;
	}
	public void setDclean_uv(Double dclean_uv) {
		this.dclean_uv = dclean_uv;
	}
	


}
