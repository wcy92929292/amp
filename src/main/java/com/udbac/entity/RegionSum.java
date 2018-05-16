package com.udbac.entity;

public class RegionSum {

	private Double sumDirtyImpPv; // 标准总曝光次数
	private Double sumCleanImpPv;// 清洗总曝光次数
	private Double sumDcleanImpPv;// 深度清洗总曝光次数
	private Double sumDirtyClkPv; // 标准总曝光次数
	private Double sumCleanClkPv;// 清洗总曝光次数
	private Double sumDcleanClkPv;// 深度清洗总曝光次数

	public Double getSumDirtyImpPv() {
		return sumDirtyImpPv;
	}

	public void setSumDirtyImpPv(Double sumDirtyImpPv) {
		this.sumDirtyImpPv = sumDirtyImpPv;
	}

	public Double getSumCleanImpPv() {
		return sumCleanImpPv;
	}

	public void setSumCleanImpPv(Double sumCleanImpPv) {
		this.sumCleanImpPv = sumCleanImpPv;
	}

	public Double getSumDcleanImpPv() {
		return sumDcleanImpPv;
	}

	public void setSumDcleanImpPv(Double sumDcleanImpPv) {
		this.sumDcleanImpPv = sumDcleanImpPv;
	}

	public Double getSumDirtyClkPv() {
		return sumDirtyClkPv;
	}

	public void setSumDirtyClkPv(Double sumDirtyClkPv) {
		this.sumDirtyClkPv = sumDirtyClkPv;
	}

	public Double getSumCleanClkPv() {
		return sumCleanClkPv;
	}

	public void setSumCleanClkPv(Double sumCleanClkPv) {
		this.sumCleanClkPv = sumCleanClkPv;
	}

	public Double getSumDcleanClkPv() {
		return sumDcleanClkPv;
	}

	public void setSumDcleanClkPv(Double sumDcleanClkPv) {
		this.sumDcleanClkPv = sumDcleanClkPv;
	}

	@Override
	public String toString() {
		return "RegionSum [sumDirtyImpPv=" + sumDirtyImpPv + ", sumCleanImpPv=" + sumCleanImpPv + ", sumDcleanImpPv="
				+ sumDcleanImpPv + ", sumDirtyClkPv=" + sumDirtyClkPv + ", sumCleanClkPv=" + sumCleanClkPv
				+ ", sumDcleanClkPv=" + sumDcleanClkPv + "]";
	}

}
