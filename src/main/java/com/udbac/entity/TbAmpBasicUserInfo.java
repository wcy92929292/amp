package com.udbac.entity;

import java.util.Date;

/**
 * 用户表
 * 
 * @author LFQ
 * 
 */
public class TbAmpBasicUserInfo {

	private Date createDate;
	private Integer userId;
	private String userName;
	private String password;
	private String realName;
	private String provinceId;
	private String mailbox;
	private String phoneNumber;
	private String userState;
	private String roleId;
	/*by LQ start*/
	private String role_Name;//角色名称
	private String customerId;//客户ID
	private String customerName;//客户名称
	/*by LQ end*/

	
	
	public String getRole_Name() {
		return role_Name;
	}

	public void setRole_Name(String role_Name) {
		this.role_Name = role_Name;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "TbAmpBasicUserInfo [createDate=" + createDate + ", userId="
				+ userId + ", userName=" + userName + ", password=" + password
				+ ", realName=" + realName + ", provinceId=" + provinceId
				+ ", mailbox=" + mailbox + ", phoneNumber=" + phoneNumber
				+ ", userState=" + userState + ", roleId=" + roleId
				+ ", role_Name=" + role_Name + ", customerId=" + customerId
				+ ", customerName=" + customerName + "]";
	}

	
	
	
}
