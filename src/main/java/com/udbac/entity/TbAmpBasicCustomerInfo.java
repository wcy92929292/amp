package com.udbac.entity;

/**
 * 客户信息表
 * @author LFQ
 * 
 */
public class TbAmpBasicCustomerInfo {

	private Integer customerId;
	private String customerName;
	private String specialCustomer;
	private String customerCode;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSpecialCustomer() {
		return specialCustomer;
	}

	public void setSpecialCustomer(String specialCustomer) {
		this.specialCustomer = specialCustomer;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	@Override
	public String toString() {
		return "TbAmpBasicCustomerInfo [customerId=" + customerId
				+ ", customerName=" + customerName + ", specialCustomer="
				+ specialCustomer + ", customerCode=" + customerCode + "]";
	}

	
}
