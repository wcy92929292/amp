package com.udbac.model;

import java.util.Date;

/**
 * URL变更校验
 * 
 * @author King
 * @Date 20170323
 */
public class UrlUpateCheckModel {

	private String activity_name;
	private String activity_code;
	private String update_batch;
	private Date url_update_time; // 计划变更时间
	private Date update_time; // 实际变更时间
	private String mic;
	private String url_update; // 计划URL
	private String old_url; // 变更前URL
	private String url_pc; // 当前URL
	private String check_user_name;
	private String check_mailbox;
	private String user_name;
	private String mailbox;

	public String getActivity_code() {
		return activity_code;
	}

	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
	}

	public Date getUrl_update_time() {
		return url_update_time;
	}

	public void setUrl_update_time(Date url_update_time) {
		this.url_update_time = url_update_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public String getUrl_update() {
		return url_update;
	}

	public void setUrl_update(String url_update) {
		this.url_update = url_update;
	}

	public String getOld_url() {
		return old_url;
	}

	public void setOld_url(String old_url) {
		this.old_url = old_url;
	}

	public String getUrl_pc() {
		return url_pc;
	}

	public void setUrl_pc(String url_pc) {
		this.url_pc = url_pc;
	}

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public String getCheck_user_name() {
		return check_user_name;
	}

	public void setCheck_user_name(String check_user_name) {
		this.check_user_name = check_user_name;
	}

	public String getCheck_mailbox() {
		return check_mailbox;
	}

	public void setCheck_mailbox(String check_mailbox) {
		this.check_mailbox = check_mailbox;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getUpdate_batch() {
		return update_batch;
	}

	public void setUpdate_batch(String update_batch) {
		this.update_batch = update_batch;
	}

	@Override
	public String toString() {
		return "UrlUpateCheckMolde [activity_code=" + activity_code
				+ ", url_update_time=" + url_update_time + ", update_time="
				+ update_time + ", mic=" + mic + ", url_update=" + url_update
				+ ", old_url=" + old_url + ", url_pc=" + url_pc + "]";
	}
}
