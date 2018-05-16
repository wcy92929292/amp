package com.udbac.service.user;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.dao.UserDao;
import com.udbac.model.UserBean;


/**
 * 用户Service实现类
 * @author LiQ
 * @date 2016-04-06
 */
@Service
public class UserServiceImpl implements UserService {
	@Resource
	private UserDao userDao;

	@Override
	public Integer createUser(Map<Object, Object> map) {
		// TODO Auto-generated method stub
		return userDao.createUser(map);
	}
	/**
	 * 查询用户名
	 * @param map
	 * @return
	 * @date 2016-04-07
	 */
	

	@Override
	public Integer updateUser(Map<Object, Object> map) {
		// TODO Auto-generated method stub
		return userDao.updateUser(map);
	}


	@Override
	public List<Map<String, String>> queryUser(Map<Object, Object> map) {
		// TODO Auto-generated method stub
		return userDao.queryUser(map);
	}
	@Override
	public Integer queryUsername(Map<Object, Object> map) {
		// TODO Auto-generated method stub
		return userDao.queryUsername(map);
	}
	@Override
	public List<UserBean> queryUserById(String roleId) {
		// TODO Auto-generated method stub
		return userDao.queryUserById(roleId);
	}
	
	@Override
	public UserBean getUserById(String userId){
		return userDao.getUserById(userId);
	}
	@Override
	public UserBean getUserByCode(String actCode){
		return userDao.getUserByCode(actCode);
	}
	@Override
	public UserBean getUserByName(String createUser) {
		// TODO Auto-generated method stub
		return userDao.getUserByName(createUser);
	}
	
}
