package com.udbac.service;

import java.util.List;
import java.util.Map;

import com.udbac.entity.TbAmpBasicMediaInfo;

/**
 * 媒体服务接口
 * @author LFQ
 * @DATE 2016-04-25
 */
public interface MediaService {

	Map<String,TbAmpBasicMediaInfo> getMedias(boolean isFlush);
	
	String mapMedia(String newMediaName,String mediaId, String userName);
	
	List<TbAmpBasicMediaInfo> getDBMedia();
	
	void setGeneralization();
	
	Map<String,String> getGeneralization();

	String addMedia(String mediaName, String mediaType, String userName);

	Integer checkMedia(String mediaName);
}
