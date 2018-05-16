package com.udbac.model;

import java.sql.Date;

public class MonitorUpdateModel {
	
	/**
	 * create_date timestamp without time zone, --创建时间
      mic character varying(200), --广告识别码

      exposure_pv_media integer, --媒体曝光次数
      exposure_pv_monitor integer,    --监测曝光次数
      exposure_pv_gap character varying(20), -- 差距百分比

      exposure_uv_media integer, --媒体曝光人数
      exposure_uv_monitor integer, --监测曝光人数
      exposure_uv_gap character varying(20), -- 差距百分比

      click_pv_media  integer,       --媒体点击次数
      click_pv_monitor  integer,    --监测点击次数
      click_pv_gap character varying(20), -- 差距百分比

      click_uv_media  integer, -- 媒体点击人数
      click_uv_monitor  integer,  --监测点击人数    
      click_uv_gap  character varying(20), -- 差距百分比
	 */

	private Date create_date;
	private String mic;
	private long exposure_pv_media;
	private long exposure_pv_monitor;
	private String exposure_pv_gap;

	private long exposure_uv_media;
	private long  exposure_uv_monitor;
	private String exposure_uv_gap;

	private long click_pv_media;
	private long click_pv_monitor ;
    private String click_pv_gap;

    private long click_uv_media;
    private long click_uv_monitor;
    private String click_uv_gap;
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getMic() {
		return mic;
	}
	public void setMic(String mic) {
		this.mic = mic;
	}
	public long getExposure_pv_media() {
		return exposure_pv_media;
	}
	public void setExposure_pv_media(long exposure_pv_media) {
		this.exposure_pv_media = exposure_pv_media;
	}
	public long getExposure_pv_monitor() {
		return exposure_pv_monitor;
	}
	public void setExposure_pv_monitor(long exposure_pv_monitor) {
		this.exposure_pv_monitor = exposure_pv_monitor;
	}
	
	public long getExposure_uv_media() {
		return exposure_uv_media;
	}
	public void setExposure_uv_media(long exposure_uv_media) {
		this.exposure_uv_media = exposure_uv_media;
	}
	public long getExposure_uv_monitor() {
		return exposure_uv_monitor;
	}
	public void setExposure_uv_monitor(long exposure_uv_monitor) {
		this.exposure_uv_monitor = exposure_uv_monitor;
	}
	
	public long getClick_pv_media() {
		return click_pv_media;
	}
	public void setClick_pv_media(long click_pv_media) {
		this.click_pv_media = click_pv_media;
	}
	public long getClick_pv_monitor() {
		return click_pv_monitor;
	}
	public void setClick_pv_monitor(long click_pv_monitor) {
		this.click_pv_monitor = click_pv_monitor;
	}
	
	public long getClick_uv_media() {
		return click_uv_media;
	}
	public void setClick_uv_media(long click_uv_media) {
		this.click_uv_media = click_uv_media;
	}
	public long getClick_uv_monitor() {
		return click_uv_monitor;
	}
	public void setClick_uv_monitor(long click_uv_monitor) {
		this.click_uv_monitor = click_uv_monitor;
	}
	public String getExposure_pv_gap() {
		return exposure_pv_gap;
	}
	public void setExposure_pv_gap(String exposure_pv_gap) {
		this.exposure_pv_gap = exposure_pv_gap;
	}
	public String getExposure_uv_gap() {
		return exposure_uv_gap;
	}
	public void setExposure_uv_gap(String exposure_uv_gap) {
		this.exposure_uv_gap = exposure_uv_gap;
	}
	public String getClick_pv_gap() {
		return click_pv_gap;
	}
	public void setClick_pv_gap(String click_pv_gap) {
		this.click_pv_gap = click_pv_gap;
	}
	public String getClick_uv_gap() {
		return click_uv_gap;
	}
	public void setClick_uv_gap(String click_uv_gap) {
		this.click_uv_gap = click_uv_gap;
	}
	
    
    
	
}
