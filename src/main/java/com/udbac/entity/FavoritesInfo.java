package com.udbac.entity;

import java.util.Date;

public class FavoritesInfo {

	private String mic;

	private String userName;

	private Date putDate;

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getPutDate() {
		return putDate;
	}

	public void setPutDate(Date putDate) {
		this.putDate = putDate;
	}

	@Override
	public String toString() {
		return "FavoritesInfo [mic=" + mic + ", userName=" + userName + ", putDate=" + putDate + "]";
	}

}
