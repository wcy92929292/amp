package com.udbac.model;

import java.util.List;

public class ExposureApiModel {
	
	private String timestamp;
	private List<ExpolureModel> collections;
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public List<ExpolureModel> getCollections() {
		return collections;
	}
	public void setCollections(List<ExpolureModel> collections) {
		this.collections = collections;
	}
	
	
	public static void main(String[] args) {
		int i = 2288936;
	}
	
}

