package com.udbac.entity;

import java.util.List;

/**
 * 页面信息
 * 监测方案新需求变更
 * Description:
 * @author root
 * @date 2017年7月5日 下午3:25:45
 */

public class AddMoniterPlanInfo {

	private String PAGE_NAME;  //页面名称
	private String PAGE_ID;
	private List<ButtonInfo> BTN_INFO; //按钮信息
	public String getPAGE_NAME() {
		return PAGE_NAME;
	}
	public void setPAGE_NAME(String pAGE_NAME) {
		PAGE_NAME = pAGE_NAME;
	}
	public List<ButtonInfo> getBTN_INFO() {
		return BTN_INFO;
	}
	public String getPAGE_ID() {
		return PAGE_ID;
	}
	public void setPAGE_ID(String pAGE_ID) {
		PAGE_ID = pAGE_ID;
	}
	public void setBTN_INFO(List<ButtonInfo> bTN_INFO) {
		BTN_INFO = bTN_INFO;
	}
	@Override
	public String toString() {
		return "AddMoniterPlanInfo [PAGE_NAME=" + PAGE_NAME + ", PAGE_ID=" + PAGE_ID + ", BTN_INFO=" + BTN_INFO + "]";
	}
}
