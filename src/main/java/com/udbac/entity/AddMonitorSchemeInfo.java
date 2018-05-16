package com.udbac.entity;

/**
 * @author HX-韩 添加监测方案子方案 监测方案信息
 *
 */
public class AddMonitorSchemeInfo {

	// 监测方案信息表
	private String activity_code;// 活动编号
	private Integer scheme_id;// 监测方案ID
	private String create_date;// 创建时间
	private String page_name; // 页面名称
	private Integer page_id;
	private String dcsid_s;
	private String btn_type;
	private String btn_name;
	private String btn_id;
	private String match_url;
	private String page_url;
	private String key;
	private String value;
	private String op;
	
	private Integer isupdate;
	
	public String getActivity_code() {
		return activity_code;
	}
	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
	}
	public Integer getScheme_id() {
		return scheme_id;
	}
	public void setScheme_id(Integer scheme_id) {
		this.scheme_id = scheme_id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getPage_name() {
		return page_name;
	}
	public void setPage_name(String page_name) {
		this.page_name = page_name;
	}
	public Integer getPage_id() {
		return page_id;
	}
	public void setPage_id(Integer page_id) {
		this.page_id = page_id;
	}
	public String getDcsid_s() {
		return dcsid_s;
	}
	public void setDcsid_s(String dcsid_s) {
		this.dcsid_s = dcsid_s;
	}
	public String getBtn_type() {
		return btn_type;
	}
	public void setBtn_type(String btn_type) {
		this.btn_type = btn_type;
	}
	public String getBtn_name() {
		return btn_name;
	}
	public void setBtn_name(String btn_name) {
		this.btn_name = btn_name;
	}
	public String getBtn_id() {
		return btn_id;
	}
	public void setBtn_id(String btn_id) {
		this.btn_id = btn_id;
	}
	public String getMatch_url() {
		return match_url;
	}
	public void setMatch_url(String match_url) {
		this.match_url = match_url;
	}
	public String getPage_url() {
		return page_url;
	}
	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public Integer getIsupdate() {
		return isupdate;
	}
	public void setIsupdate(Integer isupdate) {
		this.isupdate = isupdate;
	}
	@Override
	public String toString() {
		return "AddMonitorSchemeInfo [activity_code=" + activity_code + ", scheme_id=" + scheme_id + ", create_date="
				+ create_date + ", page_name=" + page_name + ", page_id=" + page_id + ", dcsid_s=" + dcsid_s
				+ ", btn_type=" + btn_type + ", btn_name=" + btn_name + ", btn_id=" + btn_id + ", match_url="
				+ match_url + ", page_url=" + page_url + ", key=" + key + ", value=" + value + ", op=" + op
				+ ", isupdate=" + isupdate + "]";
	}
}
