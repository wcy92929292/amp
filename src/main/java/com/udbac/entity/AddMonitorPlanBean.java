package com.udbac.entity;

import java.util.List;

/**
 * @author HX-韩 活动监测方案信息
 *
 */
public class AddMonitorPlanBean {

	// 活动信息表
	private String create_date;
	private String predict_start_date; // 预上线时间
	private String activity_name; // 活动名称
	private String parent_activity_code; // 父级活动编号
	private Integer stat_mark; // 计数规则

	private String activity_code; // 活动编号
	private String customer_id; // 客户ID
	private String customer_name;
	private String port_people;// 接口人ID
	private String monitor_people; // 监测人员
	private String font_support_people; // 前端监测人员
	private String after_support_people; // 后端监测人员
	private String memo; // 备注
	private Integer parent_idf;
	private Integer parent_activrty_code;
	private String s_code;// 活动编号前半部分 来源于投放单位的选择内容
	private String monitor_people_name; // 监测人员
	private String font_support_people_name; // 前端监测人员
	private String after_support_people_name; // 后端监测人员

	// 监测方案信息表
	private String page_name; // 页面名称
	private String page_url; // 页面URL
	private String button_name; // 按钮名称
	private String button_id; // 按钮ID
	private String button_event; // 按钮事件
	private String involve_index; // 涉及指标
	private String button_type; // 统计类别
	private Integer scheme_id; // 方案ID

	private Integer activity_state;
	
	private List<AddMoniterPlanInfo> planInfo;  //监测方案的信息

	public String getPage_name() {
		return page_name;
	}

	public void setPage_name(String page_name) {
		this.page_name = page_name;
	}

	public String getPage_url() {
		return page_url;
	}

	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}

	public String getButton_name() {
		return button_name;
	}

	public void setButton_name(String button_name) {
		this.button_name = button_name;
	}

	public String getButton_id() {
		return button_id;
	}

	public void setButton_id(String button_id) {
		this.button_id = button_id;
	}

	public String getButton_event() {
		return button_event;
	}

	public void setButton_event(String button_event) {
		this.button_event = button_event;
	}

	public String getInvolve_index() {
		return involve_index;
	}

	public void setInvolve_index(String involve_index) {
		this.involve_index = involve_index;
	}

	public String getButton_type() {
		return button_type;
	}

	public void setButton_type(String button_type) {
		this.button_type = button_type;
	}

	public Integer getScheme_id() {
		return scheme_id;
	}

	public void setScheme_id(Integer scheme_id) {
		this.scheme_id = scheme_id;
	}

	public String getMonitor_people_name() {
		return monitor_people_name;
	}

	public void setMonitor_people_name(String monitor_people_name) {
		this.monitor_people_name = monitor_people_name;
	}

	public String getFont_support_people_name() {
		return font_support_people_name;
	}

	public void setFont_support_people_name(String font_support_people_name) {
		this.font_support_people_name = font_support_people_name;
	}

	public String getAfter_support_people_name() {
		return after_support_people_name;
	}

	public void setAfter_support_people_name(String after_support_people_name) {
		this.after_support_people_name = after_support_people_name;
	}

	public Integer getActivity_state() {
		return activity_state;
	}

	public void setActivity_state(Integer activity_state) {
		this.activity_state = activity_state;
	}

	public String getS_code() {
		return s_code;
	}

	public void setS_code(String s_code) {
		this.s_code = s_code;
	}

	public String getPort_people() {
		return port_people;
	}

	public void setPort_people(String port_people) {
		this.port_people = port_people;
	}

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public String getPredict_start_date() {
		return predict_start_date;
	}

	public void setPredict_start_date(String predict_start_date) {
		this.predict_start_date = predict_start_date;
	}

	public String getFont_support_people() {
		return font_support_people;
	}

	public void setFont_support_people(String font_support_people) {
		this.font_support_people = font_support_people;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getMonitor_people() {
		return monitor_people;
	}

	public void setMonitor_people(String monitor_people) {
		this.monitor_people = monitor_people;
	}

	public String getAfter_support_people() {
		return after_support_people;
	}

	public void setAfter_support_people(String after_support_people) {
		this.after_support_people = after_support_people;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getParent_activity_code() {
		return parent_activity_code;
	}

	public void setParent_activity_code(String parent_activity_code) {
		this.parent_activity_code = parent_activity_code;
	}

	public String getActivity_code() {
		return activity_code;
	}

	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public Integer getParent_idf() {
		return parent_idf;
	}

	public void setParent_idf(Integer parent_idf) {
		this.parent_idf = parent_idf;
	}

	public Integer getParent_activrty_code() {
		return parent_activrty_code;
	}

	public void setParent_activrty_code(Integer parent_activrty_code) {
		this.parent_activrty_code = parent_activrty_code;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public Integer getStat_mark() {
		return stat_mark;
	}

	public void setStat_mark(Integer stat_mark) {
		this.stat_mark = stat_mark;
	}
	
	public List<AddMoniterPlanInfo> getPlanInfo() {
		return planInfo;
	}

	public void setPlanInfo(List<AddMoniterPlanInfo> planInfo) {
		this.planInfo = planInfo;
	}

	@Override
	public String toString() {
		return "AddMonitorPlanBean [create_date=" + create_date + ", predict_start_date=" + predict_start_date
				+ ", activity_name=" + activity_name + ", parent_activity_code=" + parent_activity_code + ", stat_mark="
				+ stat_mark + ", activity_code=" + activity_code + ", customer_id=" + customer_id + ", customer_name="
				+ customer_name + ", port_people=" + port_people + ", monitor_people=" + monitor_people
				+ ", font_support_people=" + font_support_people + ", after_support_people=" + after_support_people
				+ ", memo=" + memo + ", parent_idf=" + parent_idf + ", parent_activrty_code=" + parent_activrty_code
				+ ", s_code=" + s_code + ", monitor_people_name=" + monitor_people_name + ", font_support_people_name="
				+ font_support_people_name + ", after_support_people_name=" + after_support_people_name + ", page_name="
				+ page_name + ", page_url=" + page_url + ", button_name=" + button_name + ", button_id=" + button_id
				+ ", button_event=" + button_event + ", involve_index=" + involve_index + ", button_type=" + button_type
				+ ", scheme_id=" + scheme_id + ", activity_state=" + activity_state + ", planInfo=" + planInfo + "]";
	}

}
