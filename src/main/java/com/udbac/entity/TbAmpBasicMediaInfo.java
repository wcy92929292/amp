package com.udbac.entity;

/**
 * 媒体信息表
 * 
 * @author LFQ
 * 
 */
public class TbAmpBasicMediaInfo {

	private Integer mediaId;// 媒体ID
	private String mediaType;// 媒体类别
	private String mediaName;// 媒体名称
	private String parentMedia;// 所属媒体
	private String addUser;//添加人
	

	public String getAddUser() {
		return addUser;
	}

	public void setAddUser(String addUser) {
		this.addUser = addUser;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName){
		this.mediaName = mediaName;
	}

	public String getParentMedia() {
		return parentMedia;
	}

	public void setParentMedia(String parentMedia) {
		this.parentMedia = parentMedia;
	}

	@Override
	public String toString() {
		return "TbAmpBasicMediaInfo [mediaId=" + mediaId + ", mediaType="
				+ mediaType + ", mediaName=" + mediaName + ", parentMedia="
				+ parentMedia + "]";
	}
	
	

}
