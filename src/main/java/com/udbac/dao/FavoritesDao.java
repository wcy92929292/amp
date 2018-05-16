package com.udbac.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.FavoritesInfo;

public interface FavoritesDao {
	
	
	void addMyFavorites(@Param("mic") String mic,@Param("user") String user,@Param("date") String date);

	FavoritesInfo countFavorites(@Param("mic") String mic,@Param("user") String user,@Param("date") String date);
	
	void deleteFavorites(@Param("mic") String mic,@Param("user") String user,@Param("date") String date);
	
	
	void addMyManyFavorites(Map<String, Object> map);
	
	void deleteManyFavorites(Map<String, Object> map);
	
	List<FavoritesInfo> info(Map<String, Object> map);
}
