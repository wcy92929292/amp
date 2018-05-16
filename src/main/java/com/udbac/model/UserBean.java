package com.udbac.model;

/****
 *用户信息Bean 
 * @author lp
 * @date 2016-04-06
 *
 */
public class UserBean {
	private Integer USER_ID;//用户ID
	private String USER_NAME;//用户名	
	private String PASSWORD;//密码
	private String REAL_NAME;//姓名
	private String PROVINCE_ID;//省份ID
	private String MAILBOX;//邮箱
	private String PHONE_NUMBER;//手机号码
	private String USER_STATE;//用户状态
	private String ROLE_ID;//角色ID
	private String ROLE_NAME;//角色
	private String PERMISSIONS_ID;//权限ID
	private String PERMISSIONS_NAME;//权限名称
	private String customerId;//客户表的客户ID
	private String customerName;//客户表的客户名称
	
	
	
	
	
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
	public String getPERMISSIONS_ID() {
		return PERMISSIONS_ID;
	}
	public void setPERMISSIONS_ID(String pERMISSIONS_ID) {
		PERMISSIONS_ID = pERMISSIONS_ID;
	}
	public String getPERMISSIONS_NAME() {
		return PERMISSIONS_NAME;
	}
	public void setPERMISSIONS_NAME(String pERMISSIONS_NAME) {
		PERMISSIONS_NAME = pERMISSIONS_NAME;
	}
	public String getROLE_NAME() {
		return ROLE_NAME;
	}
	public void setROLE_NAME(String rOLE_NAME) {
		ROLE_NAME = rOLE_NAME;
	}
	public Integer getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(Integer uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getUSER_NAME() {
		return USER_NAME;
	}
	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}
	public String getPASSWORD() {
		return PASSWORD;
	}
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}
	
	public String getREAL_NAME() {
		return REAL_NAME;
	}
	public void setREAL_NAME(String rEAL_NAME) {
		REAL_NAME = rEAL_NAME;
	}
	public String getPROVINCE_ID() {
		return PROVINCE_ID;
	}
	public void setPROVINCE_ID(String pROVINCE_ID) {
		PROVINCE_ID = pROVINCE_ID;
	}
	public String getMAILBOX() {
		return MAILBOX;
	}
	public void setMAILBOX(String mAILBOX) {
		MAILBOX = mAILBOX;
	}
	public String getPHONE_NUMBER() {
		return PHONE_NUMBER;
	}
	public void setPHONE_NUMBER(String pHONE_NUMBER) {
		PHONE_NUMBER = pHONE_NUMBER;
	}
	public String getUSER_STATE() {
		return USER_STATE;
	}
	public void setUSER_STATE(String uSER_STATE) {
		USER_STATE = uSER_STATE;
	}
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}
	@Override
	public String toString() {
		return "UserBean [USER_ID=" + USER_ID + ", USER_NAME=" + USER_NAME + ", PASSWORD=" + PASSWORD + ", REAL_NAME="
				+ REAL_NAME + ", PROVINCE_ID=" + PROVINCE_ID + ", MAILBOX=" + MAILBOX + ", PHONE_NUMBER=" + PHONE_NUMBER
				+ ", USER_STATE=" + USER_STATE + ", ROLE_ID=" + ROLE_ID + ", ROLE_NAME=" + ROLE_NAME
				+ ", PERMISSIONS_ID=" + PERMISSIONS_ID + ", PERMISSIONS_NAME=" + PERMISSIONS_NAME + ", customerId="
				+ customerId + ", customerName=" + customerName + "]";
	}
	
	
	
}
