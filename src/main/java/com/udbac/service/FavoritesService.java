package com.udbac.service;

import java.util.List;
import java.util.Map;

import com.udbac.entity.FavoritesInfo;

public interface FavoritesService {

	void addMyFavorites(String mic,String user,String date);
	
	FavoritesInfo countFavorites(String mic,String user,String date);
	
	void deleteFavorites(String mic,String user,String date);
	
	void addMyManyFavorites(Map<String, Object> map);
	
	void deleteManyFavorites(Map<String, Object> map);
	
	List<FavoritesInfo> info(Map<String, Object> map);
}
