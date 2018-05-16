package com.udbac.entity;
/****
 * 
 * 概览查询
 * @author lp
 * @date  2016-04-11 
 *
 */
public class IndexBean {

	private String mic; // 活动排期日历表.短代码
	private String put_date; // 活动排期日历表.投放日期
	private String activity_code; // 活动排期点位表.活动编号
	private String activity_name; //活动基础表.活动名称
	private String customer_id; // 活动基础表.客户ID
	private String customer_name; // 客户表.客户名称
	private Integer monitor_exposure_pv;// 监测小时数据基础表.曝光PV
	private Integer monitor_exposure_uv;//监测小时数据基础表.曝光UV
	private Integer monitor_click_pv;//监测小时数据基础表.点击pv
	private Integer monitor_click_uv;//监测小时数据基础表.点击pv
	private String create_date;//监测小时数据基础表.创建时间
	private String click_avg; //活动排期点位表.活动点击预估
	private String exposure_avg; //活动排期点位表.活动曝光预估
	private String unit;//活动排期点位表.投放单位
	private String support_exposure;//活动排期点位扩展表.是否支持曝光
	private String support_click;//活动排期点位扩展表.是否支持点击
	private Integer exception;//问题数
	public Integer getException() {
		return exception;
	}
	public void setException(Integer exception) {
		this.exception = exception;
	}
	public String getMic() {
		return mic;
	}
	public void setMic(String mic) {
		this.mic = mic;
	}
	public String getPut_date() {
		return put_date;
	}
	public void setPut_date(String put_date) {
		this.put_date = put_date;
	}
	public String getActivity_code() {
		return activity_code;
	}
	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
	}
	public String getActivity_name() {
		return activity_name;
	}
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
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
	public Integer getMonitor_exposure_pv() {
		return monitor_exposure_pv;
	}
	public void setMonitor_exposure_pv(Integer monitor_exposure_pv) {
		this.monitor_exposure_pv = monitor_exposure_pv;
	}
	public Integer getMonitor_exposure_uv() {
		return monitor_exposure_uv;
	}
	public void setMonitor_exposure_uv(Integer monitor_exposure_uv) {
		this.monitor_exposure_uv = monitor_exposure_uv;
	}
	public Integer getMonitor_click_pv() {
		return monitor_click_pv;
	}
	public void setMonitor_click_pv(Integer monitor_click_pv) {
		this.monitor_click_pv = monitor_click_pv;
	}
	public Integer getMonitor_click_uv() {
		return monitor_click_uv;
	}
	public void setMonitor_click_uv(Integer monitor_click_uv) {
		this.monitor_click_uv = monitor_click_uv;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getSupport_exposure() {
		return support_exposure;
	}
	public void setSupport_exposure(String support_exposure) {
		this.support_exposure = support_exposure;
	}
	public String getSupport_click() {
		return support_click;
	}
	public void setSupport_click(String support_click) {
		this.support_click = support_click;
	}
	@Override
	public String toString() {
		return "IndexBean [mic=" + mic + ", put_date=" + put_date + ", activity_code=" + activity_code
				+ ", activity_name=" + activity_name + ", customer_id=" + customer_id + ", customer_name="
				+ customer_name + ", monitor_exposure_pv=" + monitor_exposure_pv + ", monitor_exposure_uv="
				+ monitor_exposure_uv + ", monitor_click_pv=" + monitor_click_pv + ", monitor_click_uv="
				+ monitor_click_uv + ", create_date=" + create_date + ", click_avg=" + click_avg + ", exposure_avg="
				+ exposure_avg + ", unit=" + unit + ", support_exposure=" + support_exposure + ", support_click="
				+ support_click + ", exception=" + exception + ", getException()=" + getException() + ", getMic()=" + getMic()
				+ ", getPut_date()=" + getPut_date() + ", getActivity_code()=" + getActivity_code()
				+ ", getActivity_name()=" + getActivity_name() + ", getCustomer_id()=" + getCustomer_id()
				+ ", getCustomer_name()=" + getCustomer_name() + ", getMonitor_exposure_pv()="
				+ getMonitor_exposure_pv() + ", getMonitor_exposure_uv()=" + getMonitor_exposure_uv()
				+ ", getMonitor_click_pv()=" + getMonitor_click_pv() + ", getMonitor_click_uv()="
				+ getMonitor_click_uv() + ", getCreate_date()=" + getCreate_date() + ", getClick_avg()="
				+ getClick_avg() + ", getExposure_avg()=" + getExposure_avg() + ", getUnit()=" + getUnit()
				+ ", getSupport_exposure()=" + getSupport_exposure() + ", getSupport_click()=" + getSupport_click()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	

}
