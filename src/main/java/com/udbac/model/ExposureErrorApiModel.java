package com.udbac.model;

public class ExposureErrorApiModel {
	
	short errorCode;
	
	String errorMessage;
	

	public ExposureErrorApiModel() {
		
	}
	public ExposureErrorApiModel(short errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public short getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(short errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
