package com.udbac.entity;

/**
 * URL变更信息与点位关系bean
 * 
 * @author LFQ
 */
public class TbAmpUpdatUrlAndSchedule {

	private Integer updateBatch;
	private String mic;
	private String supportClick;
	private String oldUrl;

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

	public String getSupportClick() {
		return supportClick;
	}

	public void setSupportClick(String supportClick) {
		this.supportClick = supportClick;
	}

	public String getOldUrl() {
		return oldUrl;
	}

	public void setOldUrl(String oldUrl) {
		this.oldUrl = oldUrl;
	}

	@Override
	public String toString() {
		return "TbAmpUpdatUrlAndSchedule [updateBatch=" + updateBatch
				+ ", mic=" + mic + ", supportClick=" + supportClick + "]";
	}
	
}
