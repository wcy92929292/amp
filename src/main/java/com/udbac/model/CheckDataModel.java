package com.udbac.model;

/**
 * 检查数据MODEL
 */
public class CheckDataModel {

	private String mic;
	private Integer hDirtyImpPv;
	private Integer dDirtyImpPv;
	private Integer hDirtyClkPv;
	private Integer dDirtyClkPv;

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public Integer gethDirtyImpPv() {
		return hDirtyImpPv;
	}

	public void sethDirtyImpPv(Integer hDirtyImpPv) {
		this.hDirtyImpPv = hDirtyImpPv;
	}

	public Integer getdDirtyImpPv() {
		return dDirtyImpPv;
	}

	public void setdDirtyImpPv(Integer dDirtyImpPv) {
		this.dDirtyImpPv = dDirtyImpPv;
	}

	public Integer gethDirtyClkPv() {
		return hDirtyClkPv;
	}

	public void sethDirtyClkPv(Integer hDirtyClkPv) {
		this.hDirtyClkPv = hDirtyClkPv;
	}

	public Integer getdDirtyClkPv() {
		return dDirtyClkPv;
	}

	public void setdDirtyClkPv(Integer dDirtyClkPv) {
		this.dDirtyClkPv = dDirtyClkPv;
	}

}
