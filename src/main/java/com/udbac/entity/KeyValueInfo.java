package com.udbac.entity;


/**
 * 监测方案下的按钮信息对应的规则
 * Description:
 * @author root
 * @date 2017年7月5日 下午3:52:59
 */
public class KeyValueInfo {

	private String KEY;
	private String VALUE;
	private String OPERATION;
	private Integer SCHEME_ID; // 方案ID
	
	public String getKEY() {
		return KEY;
	}
	public void setKEY(String kEY) {
		KEY = kEY;
	}
	public String getVALUE() {
		return VALUE;
	}
	public void setVALUE(String vALUE) {
		VALUE = vALUE;
	}
	public String getOPERATION() {
		return OPERATION;
	}
	public void setOPERATION(String oPERATION) {
		OPERATION = oPERATION;
	}
	public Integer getSCHEME_ID() {
		return SCHEME_ID;
	}
	public void setSCHEME_ID(Integer sCHEME_ID) {
		SCHEME_ID = sCHEME_ID;
	}
	@Override
	public String toString() {
		return "KeyValueInfo [KEY=" + KEY + ", VALUE=" + VALUE + ", OPERATION=" + OPERATION + ", SCHEME_ID=" + SCHEME_ID
				+ "]";
	}
}
