package com.udbac.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.udbac.dao.OnlineCheckDao;
import com.udbac.entity.OnlineCheckInfo;
import com.udbac.service.OnlineCheckService;
import com.udbac.util.FilePathManager;

/**
 * 
 * @author han
 *
 */
@Service
public class OnlineCheckServiceImpl implements OnlineCheckService {

	@Autowired
	OnlineCheckDao dao;
	

	@Autowired
	private FilePathManager filePathManager;


	@Override
	@Cacheable(cacheName="onlineCache")
	public List<OnlineCheckInfo> queryInfo(String date,String state,String mic,String cusName,String userName) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		map.put("date", date);
		map.put("state", state);
		map.put("mic", mic);
		map.put("cusName", cusName);
		System.out.println("========"+userName);
		map.put("userName", userName);
		
		List<OnlineCheckInfo> queryInfo = dao.queryInfo(map);
		return queryInfo;
	}

	@Override
	public void saveExposurePath(String exposureFile, String mic) {
		// TODO Auto-generated method stub
		dao.saveExposurePath(exposureFile, mic);
	}

	@Override
	public void saveClickPath(String exposureFile, String mic) {
		// TODO Auto-generated method stub
		dao.saveClickPath(exposureFile, mic);
	}

	@Override
	public void saveAfterPath(String afterFile, String mic) {
		// TODO Auto-generated method stub
		dao.saveAfterPath(afterFile, mic);
	}

	@Override
	public void changeState(String mic, String state,Date date) {
		// TODO Auto-generated method stub
		dao.changeState(mic, state,date);
	}

	@Override
	public void setMicState(Integer mic_state, String mic) {
		// TODO Auto-generated method stub
		dao.setMicState(mic_state, mic);
	}

	@Override
	public String queryLastState(String mic) {
		// TODO Auto-generated method stub
		return dao.queryLastState(mic);
	}

	

	

}
