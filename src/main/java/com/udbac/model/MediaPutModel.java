package com.udbac.model;

import java.sql.Date;

/****
 * 营销活动-媒体投放Model
 */
public class MediaPutModel {

	private Date datetime;
	private Date create_date;
	private Date ecreate_date;
	private int id;
	private String mic;// 营销识别码
	private String activity_id;// 活动编号compaign_id
	private String a_code;//活动编号，展示编号，不做业务关联处理
	private String activity;// 活动名称
	private String contents;// 点位 投放形式
	private String medium;// //投放位置
	private String source;// 投放媒体
	private String filename;//文件名称
	private String filepath;//文件路径
	private int exposure;// 曝光次数
	private int exposure_peo;// 曝光人数
	private int pv;// 点击次数
	private int pv_peo;// 点击人数
	// 张海州提供数据
	private int exposure_pv_monitor;// 监测曝光PV,
	private int exposure_uv_monitor;// 监测曝光UV,
	private int click_pv_monitor;// 监测点击PV,
	private int click_uv_monitor;// 监测点击UV,

	// 若思修改数据
	private int exposure_pv_final;// 监测曝光PV, 若思修改数据
	private int exposure_uv_final;// 监测曝光UV,
	private int click_pv_final;// 监测点击PV,
	private int click_uv_final;// 监测点击UV,

	// 媒体提供数据
	private int exposure_pv_media;// 监测曝光PV,媒体提供数据
	private int exposure_uv_media;// 监测曝光UV,
	private int click_pv_media;// 监测点击PV,
	private int click_uv_media;// 监测点击UV,

	// GAP数据
	private int exposure_pv_gap;// 监测曝光PV,GAP数据
	private int exposure_uv_gap;// 监测曝光UV,
	private int click_pv_gap;// 监测点击PV,
	private int click_uv_gap;// 监测点击UV,
	
	private String terminal_type;//投放类型

	private String create_date_hours;//按照小时的时间
	
	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public int getExposure_pv_final() {
		return exposure_pv_final;
	}

	public void setExposure_pv_final(int exposure_pv_final) {
		this.exposure_pv_final = exposure_pv_final;
	}

	public int getExposure_uv_final() {
		return exposure_uv_final;
	}

	public void setExposure_uv_final(int exposure_uv_final) {
		this.exposure_uv_final = exposure_uv_final;
	}

	public int getClick_pv_final() {
		return click_pv_final;
	}

	public void setClick_pv_final(int click_pv_final) {
		this.click_pv_final = click_pv_final;
	}

	public int getClick_uv_final() {
		return click_uv_final;
	}

	public void setClick_uv_final(int click_uv_final) {
		this.click_uv_final = click_uv_final;
	}

	public int getExposure_pv_media() {
		return exposure_pv_media;
	}

	public void setExposure_pv_media(int exposure_pv_media) {
		this.exposure_pv_media = exposure_pv_media;
	}

	public int getExposure_uv_media() {
		return exposure_uv_media;
	}

	public void setExposure_uv_media(int exposure_uv_media) {
		this.exposure_uv_media = exposure_uv_media;
	}

	public int getClick_pv_media() {
		return click_pv_media;
	}

	public void setClick_pv_media(int click_pv_media) {
		this.click_pv_media = click_pv_media;
	}

	public int getClick_uv_media() {
		return click_uv_media;
	}

	public void setClick_uv_media(int click_uv_media) {
		this.click_uv_media = click_uv_media;
	}

	public int getExposure_pv_gap() {
		return exposure_pv_gap;
	}

	public void setExposure_pv_gap(int exposure_pv_gap) {
		this.exposure_pv_gap = exposure_pv_gap;
	}

	public int getExposure_uv_gap() {
		return exposure_uv_gap;
	}

	public void setExposure_uv_gap(int exposure_uv_gap) {
		this.exposure_uv_gap = exposure_uv_gap;
	}

	public int getClick_pv_gap() {
		return click_pv_gap;
	}

	public void setClick_pv_gap(int click_pv_gap) {
		this.click_pv_gap = click_pv_gap;
	}

	public int getClick_uv_gap() {
		return click_uv_gap;
	}

	public void setClick_uv_gap(int click_uv_gap) {
		this.click_uv_gap = click_uv_gap;
	}

	private int visit;// 访次
	private int visitor;// 访客 (点击人数)
	private int click;// 点击量
	private String fwsd;// 访问深度
	private int viewtime;// 访问时长
	private String viewtimeFloat;// 访问时长
	private double ctr;//
	private int bounce_visit;// 跳出访次
	private int bounce_rate;// 跳出率
	private String hdnum;// 参与活动人数
	private String blnum;// 办理业务人数
	private String visitRate;//

	public String getViewtimeFloat() {
		return viewtimeFloat;
	}

	public void setViewtimeFloat(String viewtimeFloat) {
		this.viewtimeFloat = viewtimeFloat;
	}

	public String getVisitRate() {
		return visitRate;
	}

	public void setVisitRate(String visitRate) {
		this.visitRate = visitRate;
	}

	public int getPv_peo() {
		return pv_peo;
	}

	public void setPv_peo(int pv_peo) {
		this.pv_peo = pv_peo;
	}

	public int getExposure_pv_monitor() {
		return exposure_pv_monitor;
	}

	public void setExposure_pv_monitor(int exposure_pv_monitor) {
		this.exposure_pv_monitor = exposure_pv_monitor;
	}

	public int getExposure_uv_monitor() {
		return exposure_uv_monitor;
	}

	public String getA_code() {
		return a_code;
	}

	public void setA_code(String a_code) {
		this.a_code = a_code;
	}

	public void setExposure_uv_monitor(int exposure_uv_monitor) {
		this.exposure_uv_monitor = exposure_uv_monitor;
	}

	public int getClick_pv_monitor() {
		return click_pv_monitor;
	}

	public void setClick_pv_monitor(int click_pv_monitor) {
		this.click_pv_monitor = click_pv_monitor;
	}

	public int getClick_uv_monitor() {
		return click_uv_monitor;
	}

	public void setClick_uv_monitor(int click_uv_monitor) {
		this.click_uv_monitor = click_uv_monitor;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public Date getEcreate_date() {
		return ecreate_date;
	}

	public void setEcreate_date(Date ecreate_date) {
		this.ecreate_date = ecreate_date;
	}

	public String getFwsd() {
		return fwsd;
	}

	public void setFwsd(String fwsd) {
		this.fwsd = fwsd;
	}

	public String getHdnum() {
		return hdnum;
	}

	public void setHdnum(String hdnum) {
		this.hdnum = hdnum;
	}

	public String getBlnum() {
		return blnum;
	}

	public void setBlnum(String blnum) {
		this.blnum = blnum;
	}

	public int getExposure_peo() {
		return exposure_peo;
	}

	public void setExposure_peo(int exposure_peo) {
		this.exposure_peo = exposure_peo;
	}

	public double getCtr() {
		return ctr;
	}

	public void setCtr(double ctr) {
		this.ctr = ctr;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

 

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getExposure() {
		return exposure;
	}

	public void setExposure(int exposure) {
		this.exposure = exposure;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getVisit() {
		return visit;
	}

	public void setVisit(int visit) {
		this.visit = visit;
	}

	public int getVisitor() {
		return visitor;
	}

	public void setVisitor(int visitor) {
		this.visitor = visitor;
	}

	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
	}

	public int getViewtime() {
		return viewtime;
	}

	public void setViewtime(int viewtime) {
		this.viewtime = viewtime;
	}

	public int getBounce_visit() {
		return bounce_visit;
	}

	public void setBounce_visit(int bounce_visit) {
		this.bounce_visit = bounce_visit;
	}

	public int getBounce_rate() {
		return bounce_rate;
	}

	public void setBounce_rate(int bounce_rate) {
		this.bounce_rate = bounce_rate;
	}

	public String getTerminal_type() {
		return terminal_type;
	}

	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
	}

	public String getCreate_date_hours() {
		return create_date_hours;
	}

	public void setCreate_date_hours(String create_date_hours) {
		this.create_date_hours = create_date_hours;
	}

}
