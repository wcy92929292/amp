package com.udbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.TbAmpBasicMediaInfo;

/**
 * 媒体DAO
 * @author LFQ
 *	@DATE 2016-04-25
 */
public interface MediaDao {
	
	//获取所有的媒体信息列表
	List<TbAmpBasicMediaInfo> getMedias();
	
	//新增媒体信息
	int insertMedia(TbAmpBasicMediaInfo media);
	
	
	TbAmpBasicMediaInfo findMediaById(@Param("mediaId") Integer mediaId);
    //判断该媒体是否存在
	Integer getCount(@Param("mediaName")String mediaName);
}
