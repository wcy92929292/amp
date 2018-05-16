package com.udbac.entity;

public class QueryCustomerInfo {

	/**
	 * 查询活动对应的客户名称
	 */
	private String activity_name;
	private String activity_code;
	private String customer_id;
	private String customer_name;
	private String customer_code;
	private String parent_idf;

	/**
	 * 查询活动对应的人员名称
	 */
	private String monitor_people;
	private String font_support_people;
	private String after_support_people;
	private String user_id;
	private String real_name;

	public String getMonitor_people() {
		return monitor_people;
	}

	public void setMonitor_people(String monitor_people) {
		this.monitor_people = monitor_people;
	}

	public String getFont_support_people() {
		return font_support_people;
	}

	public void setFont_support_people(String font_support_people) {
		this.font_support_people = font_support_people;
	}

	public String getAfter_support_people() {
		return after_support_people;
	}

	public void setAfter_support_people(String after_support_people) {
		this.after_support_people = after_support_people;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public String getActivity_code() {
		return activity_code;
	}

	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
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

	public String getCustomer_code() {
		return customer_code;
	}

	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}
	
	

	public String getParent_idf() {
		return parent_idf;
	}

	public void setParent_idf(String parent_idf) {
		this.parent_idf = parent_idf;
	}

	@Override
	public String toString() {
		return "QueryCustomerInfo [activity_name=" + activity_name + ", activity_code=" + activity_code
				+ ", customer_id=" + customer_id + ", customer_name=" + customer_name + ", customer_code="
				+ customer_code + ", parent_idf=" + parent_idf + ", monitor_people=" + monitor_people
				+ ", font_support_people=" + font_support_people + ", after_support_people=" + after_support_people
				+ ", user_id=" + user_id + ", real_name=" + real_name + "]";
	}



}
