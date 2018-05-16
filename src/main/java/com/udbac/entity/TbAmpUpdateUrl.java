/*
 * ����          TbAmpUpdateUrl.java
 * ����ϵͳ      �����ƽ̨
 * ����Ȩ        Copyright(c) 2016 UDBAC
 * ������        2016/4/14
 */
package com.udbac.entity;


/**
 * TbAmpUpdateUrl
 * <pre>
 * Url����
 * <pre>
 *
 * @author LP
 */
public class TbAmpUpdateUrl  {

    public TbAmpUpdateUrl() {
      super();
    }
    private String updateState = "";
    private Integer updateBatch = 0;
    private String mic = "";
    private String activityCode = "";
	public String getUpdateState() {
		return updateState;
	}
	public void setUpdateState(String updateState) {
		this.updateState = updateState;
	}
	public Integer getUpdateBatch() {
		return updateBatch;
	}
	public void setUpdateBatch(Integer updateBatch) {
		this.updateBatch = updateBatch;
	}
	public String getMic() {
		return mic;
	}
	public void setMic(String mic) {
		this.mic = mic;
	}
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
  
	@Override
	public String toString() {
		return "TbAmpBasicActivityInfo [updateState="+updateState+",updateBatch="+updateBatch+","
				+ "mic="+mic+",activityCode="+activityCode+"]";
	}

}