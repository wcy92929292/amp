package com.udbac.entity;

import java.util.List;

/**
 * 按钮信息
 * Description:
 * @author root
 * @date 2017年7月5日 下午3:44:33
 */
public class ButtonInfo {

	private String DCSID_S;  
	private String BTN_TYPE; //按钮类型
	private String BTN_NAME; //按钮名称
	private String BTN_ID;  //按钮ID
	private String PAGE_URL; //页面URL
	private String MATCH_RULE; // 匹配规则
	private List<KeyValueInfo> KEY_VALUE; 
	
	public String getDCSID_S() {
		return DCSID_S;
	}
	public void setDCSID_S(String dCSID_S) {
		DCSID_S = dCSID_S;
	}
	public String getBTN_TYPE() {
		return BTN_TYPE;
	}
	public void setBTN_TYPE(String bTN_TYPE) {
		BTN_TYPE = bTN_TYPE;
	}
	public String getBTN_NAME() {
		return BTN_NAME;
	}
	public void setBTN_NAME(String bTN_NAME) {
		BTN_NAME = bTN_NAME;
	}
	public String getBTN_ID() {
		return BTN_ID;
	}
	public void setBTN_ID(String bTN_ID) {
		BTN_ID = bTN_ID;
	}
	public String getPAGE_URL() {
		return PAGE_URL;
	}
	public void setPAGE_URL(String pAGE_URL) {
		PAGE_URL = pAGE_URL;
	}
	public String getMATCH_RULE() {
		return MATCH_RULE;
	}
	public void setMATCH_RULE(String mATCH_RULE) {
		MATCH_RULE = mATCH_RULE;
	}
	public List<KeyValueInfo> getKEY_VALUE() {
		return KEY_VALUE;
	}
	public void setKEY_VALUE(List<KeyValueInfo> kEY_VALUE) {
		KEY_VALUE = kEY_VALUE;
	}
	@Override
	public String toString() {
		return "ButtonInfo [DCSID_S=" + DCSID_S + ", BTN_TYPE=" + BTN_TYPE + ", BTN_NAME=" + BTN_NAME + ", BTN_ID="
				+ BTN_ID + ", PAGE_URL=" + PAGE_URL + ", MATCH_RULE=" + MATCH_RULE + ", KEY_VALUE=" + KEY_VALUE + "]";
	}
}
