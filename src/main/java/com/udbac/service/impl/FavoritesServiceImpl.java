package com.udbac.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.dao.FavoritesDao;
import com.udbac.entity.FavoritesInfo;
import com.udbac.service.FavoritesService;

@Service
public class FavoritesServiceImpl implements FavoritesService {

	@Autowired
	FavoritesDao dao;

	@Override
	public void addMyFavorites(String mic,String user,String date) {

		dao.addMyFavorites(mic,user,date);
	}

	@Override
	public FavoritesInfo countFavorites(String mic, String user, String date) {
		return dao.countFavorites(mic, user, date);
	}

	@Override
	public void deleteFavorites(String mic, String user, String date) {
		dao.deleteFavorites(mic, user, date);
	}

	@Override
	public void addMyManyFavorites(Map<String, Object> map) {
		dao.addMyManyFavorites(map);
	}

	@Override
	public void deleteManyFavorites(Map<String, Object> map) {
		dao.deleteManyFavorites(map);
	}

	@Override
	public List<FavoritesInfo> info(Map<String, Object> map) {
		return dao.info(map);
	}

}
