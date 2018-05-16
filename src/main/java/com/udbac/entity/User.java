package com.udbac.entity;

import java.util.Date;

/**
 * 用户信息
 * 
 * @author HX-韩
 *
 */
public class User {

	private Date create_date; // 创建日期
	private String user_id; // 用户ID
	private String user_name; // 用户名
	private String password; // 密码
	private String real_name; // 姓名
	private String provice_id; // 省份
	private String mailbox; // 邮箱
	private Integer phone_number; // 手机号
	private Integer user_state; // 用户状态
	private Integer role_id; // 角色ID，关联角色表
	private String role_name;// 角色

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getProvice_id() {
		return provice_id;
	}

	public void setProvice_id(String provice_id) {
		this.provice_id = provice_id;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public Integer getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(Integer phone_number) {
		this.phone_number = phone_number;
	}

	public Integer getUser_state() {
		return user_state;
	}

	public void setUser_state(Integer user_state) {
		this.user_state = user_state;
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	@Override
	public String toString() {
		return "User [create_date=" + create_date + ", user_id=" + user_id + ", user_name=" + user_name + ", password="
				+ password + ", real_name=" + real_name + ", provice_id=" + provice_id + ", mailbox=" + mailbox
				+ ", phone_number=" + phone_number + ", user_state=" + user_state + ", role_id=" + role_id
				+ ", role_name=" + role_name + "]";
	}

}
