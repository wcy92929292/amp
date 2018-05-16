
package com.udbac.entity;

import java.util.Date;

/**
 * 2016-05-20 报告导出
 * 
 * @author han
 *
 */
public class DataExport {

	private String activity_name; // 活动名称
	private String media_name; // 媒体名称
	private String point_location; // 投放位置
	private String mic; // 短代码
	private Date put_date; // 投放
	private Double pv; // 曝光次数
	private Integer uv; // 曝光人数
	private Double _pv; // 点击次数
	private Integer _uv; // 点击人数
	private Integer _clk_rate; // 点击率CTR
	private Double a_vv; // 访问次数
	private Integer a_uv; // 访问人数
	private Integer a_pv; // 浏览量
	private Double bounce_t; // 跳出次数
	private Double bounce_r; // 跳出率
	private Double time_s; // 平均浏览时间
	private Double exposure_avg;// 当日曝光预估量
	private Double click_avg;// 当日点击预估量
	private Date start_date; // 开始日期
	private String unit; // 投放单位
	private Double put_value;// 投放量
    private String url_pc;//url
    private String put_function;//投放形式
    private String terminal_Type;//投放类型
    private Integer group_id;
    private Integer clk;//到站点击
    private String activity_code;//活动编号
    private String supportexposure;//支持曝光
    private String supportclick;//支持点击
  
    
	

	public String getSupportexposure() {
		return supportexposure;
	}

	public void setSupportexposure(String supportexposure) {
		this.supportexposure = supportexposure;
	}

	public String getSupportclick() {
		return supportclick;
	}

	public void setSupportclick(String supportclick) {
		this.supportclick = supportclick;
	}

	public String getActivity_code() {
		return activity_code;
	}

	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
	}

	public String getPut_function() {
		return put_function;
	}

	public void setPut_function(String put_function) {
		this.put_function = put_function;
	}

	public String getTerminal_Type() {
		return terminal_Type;
	}

	public void setTerminal_Type(String terminal_Type) {
		this.terminal_Type = terminal_Type;
	}

	public Integer getClk() {
		return clk;
	}

	public void setClk(Integer clk) {
		this.clk = clk;
	}

	public String getUrl_pc() {
		return url_pc;
	}

	public void setUrl_pc(String url_pc) {
		this.url_pc = url_pc;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getPut_value() {
		return put_value;
	}

	public void setPut_value(Double put_value) {
		this.put_value = put_value;
	}

	private String customer_name;
	private String customer_code;

	public Double getExposure_avg() {
		return exposure_avg;
	}

	public void setExposure_avg(Double exposure_avg) {
		this.exposure_avg = exposure_avg;
	}

	public Double getClick_avg() {
		return click_avg;
	}

	public void setClick_avg(Double click_avg) {
		this.click_avg = click_avg;
	}

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public String getMedia_name() {
		return media_name;
	}

	public void setMedia_name(String media_name) {
		this.media_name = media_name;
	}

	public String getPoint_location() {
		return point_location;
	}

	public void setPoint_location(String point_location) {
		this.point_location = point_location;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public Date getPut_date() {
		return put_date;
	}

	public void setPut_date(Date put_date) {
		this.put_date = put_date;
	}

	public Double getPv() {
		return pv;
	}

	public void setPv(Double pv) {
		this.pv = pv;
	}

	public Integer getUv() {
		return uv;
	}

	public void setUv(Integer uv) {
		this.uv = uv;
	}

	public Double get_pv() {
		return _pv;
	}

	public void set_pv(Double _pv) {
		this._pv = _pv;
	}

	public Integer get_uv() {
		return _uv;
	}

	public void set_uv(Integer _uv) {
		this._uv = _uv;
	}

	public Integer get_clk_rate() {
		return _clk_rate;
	}

	public void set_clk_rate(Integer _clk_rate) {
		this._clk_rate = _clk_rate;
	}

	public Double getA_vv() {
		return a_vv;
	}

	public void setA_vv(Double a_vv) {
		this.a_vv = a_vv;
	}

	public Integer getA_uv() {
		return a_uv;
	}

	public void setA_uv(Integer a_uv) {
		this.a_uv = a_uv;
	}

	public Integer getA_pv() {
		return a_pv;
	}

	public void setA_pv(Integer a_pv) {
		this.a_pv = a_pv;
	}

	public Double getBounce_t() {
		return bounce_t;
	}

	public void setBounce_t(Double bounce_t) {
		this.bounce_t = bounce_t;
	}

	public Double getBounce_r() {
		return bounce_r;
	}

	public void setBounce_r(Double bounce_r) {
		this.bounce_r = bounce_r;
	}

	public Double getTime_s() {
		return time_s;
	}

	public void setTime_s(Double time_s) {
		this.time_s = time_s;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCustomer_code() {
		return customer_code;
	}

	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

}
