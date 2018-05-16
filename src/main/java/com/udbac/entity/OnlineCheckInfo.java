package com.udbac.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author han 上线核查页面
 */
public class OnlineCheckInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 活动基础表
	private String activity_name; // 活动名称
	private String activity_code; // 活动编号
	// 媒体表
	private String media_name; // 媒体名称
	// 活动排期点位扩展表
	private String point_location; // 投放位置
	private String put_function; // 投放形式
	private String mic; // 短代码
	private Integer support_exposure; // 是否支持曝光
	private Integer support_click; // 是否支持点击
	// 活动排期点位表
	private String click_avg; // 点击预告
	private String exposure_avg; // 曝光预告
	// 监测小时基础表
	private Integer click; // 当前点击
	private Integer pv; // 当前曝光
	// 活动排期日历表
	private Date put_date; // 投放时间
	private String online_state; // 活动状态
	private String click_file; // 点击核查
	private String exposure_file; // 曝光核查
	private String after_file; // 后端核查
	private String memo; // 备注
	// 客户信息表
	private String customer_id; // 客户ID
	private String customer_code; // 客户ID
	private String customer_name; // 客户ID
	private Integer now_state_id; // 现在的点位状态
	private Integer last_state_id; // 上一个点位状态
	private String unit; // 投放单位
	private Integer put_value; // 当天投放量

	private String collect_user;

	public String getActivity_code() {
		return activity_code;
	}

	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
	}

	public String getOnline_state() {
		return online_state;
	}

	public Integer getNow_state_id() {
		return now_state_id;
	}

	public void setNow_state_id(Integer now_state_id) {
		this.now_state_id = now_state_id;
	}

	public Integer getLast_state_id() {
		return last_state_id;
	}

	public void setLast_state_id(Integer last_state_id) {
		this.last_state_id = last_state_id;
	}

	public void setOnline_state(String online_state) {
		this.online_state = online_state;
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

	public String getPut_function() {
		return put_function;
	}

	public void setPut_function(String put_function) {
		this.put_function = put_function;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public String getClick_avg() {
		return click_avg;
	}

	public void setClick_avg(String click_avg) {
		this.click_avg = click_avg;
	}

	public String getExposure_avg() {
		return exposure_avg;
	}

	public void setExposure_avg(String exposure_avg) {
		this.exposure_avg = exposure_avg;
	}

	public Integer getClick() {
		return click;
	}

	public void setClick(Integer click) {
		this.click = click;
	}

	public Integer getPv() {
		return pv;
	}

	public void setPv(Integer pv) {
		this.pv = pv;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getClick_file() {
		return click_file;
	}

	public void setClick_file(String click_file) {
		this.click_file = click_file;
	}

	public String getMemo() {
		return memo;
	}

	public void setRemark(String memo) {
		this.memo = memo;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getExposure_file() {
		return exposure_file;
	}

	public void setExposure_file(String exposure_file) {
		this.exposure_file = exposure_file;
	}

	public String getAfter_file() {
		return after_file;
	}

	public void setAfter_file(String after_file) {
		this.after_file = after_file;
	}

	public Integer getSupport_exposure() {
		return support_exposure;
	}

	public void setSupport_exposure(Integer support_exposure) {
		this.support_exposure = support_exposure;
	}

	public Integer getSupport_click() {
		return support_click;
	}

	public void setSupport_click(Integer support_click) {
		this.support_click = support_click;
	}

	public Date getPut_date() {
		return put_date;
	}

	public void setPut_date(Date put_date) {
		this.put_date = put_date;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getPut_value() {
		return put_value;
	}

	public void setPut_value(Integer put_value) {
		this.put_value = put_value;
	}

	public String getCustomer_code() {
		return customer_code;
	}

	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}

	public String getCollect_user() {
		return collect_user;
	}

	public void setCollect_user(String collect_user) {
		this.collect_user = collect_user;
	}

	@Override
	public String toString() {
		return "OnlineCheckInfo [activity_name=" + activity_name + ", activity_code=" + activity_code + ", media_name="
				+ media_name + ", point_location=" + point_location + ", put_function=" + put_function + ", mic=" + mic
				+ ", support_exposure=" + support_exposure + ", support_click=" + support_click + ", click_avg="
				+ click_avg + ", exposure_avg=" + exposure_avg + ", click=" + click + ", pv=" + pv + ", put_date="
				+ put_date + ", online_state=" + online_state + ", click_file=" + click_file + ", exposure_file="
				+ exposure_file + ", after_file=" + after_file + ", memo=" + memo + ", customer_id=" + customer_id
				+ ", customer_code=" + customer_code + ", customer_name=" + customer_name + ", now_state_id="
				+ now_state_id + ", last_state_id=" + last_state_id + ", unit=" + unit + ", put_value=" + put_value
				+ ", collect_user=" + collect_user + "]";
	}

}
