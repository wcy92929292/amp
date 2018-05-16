package com.udbac.entity;

public class RegionTimes {

	private Integer time; // 次数
	private Integer dirtyUv;
	private Integer cleanUv;
	private Integer dcleanUv;
	private String type;

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getDirtyUv() {
		return dirtyUv;
	}

	public void setDirtyUv(Integer dirtyUv) {
		this.dirtyUv = dirtyUv;
	}

	public Integer getCleanUv() {
		return cleanUv;
	}

	public void setCleanUv(Integer cleanUv) {
		this.cleanUv = cleanUv;
	}

	public Integer getDcleanUv() {
		return dcleanUv;
	}

	public void setDcleanUv(Integer dcleanUv) {
		this.dcleanUv = dcleanUv;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "RegionTimes [time=" + time + ", dirtyUv=" + dirtyUv + ", cleanUv=" + cleanUv + ", dcleanUv=" + dcleanUv
				+ ", type=" + type + "]";
	}

}
