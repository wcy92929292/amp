package com.udbac.service.user;

import java.util.List;
import java.util.Map;

import com.udbac.model.UserBean;

/**
 * 用户Service
 * @author LiQ
 * @date 2016-04-06
 */
public interface UserService {

	/**
	 * 注册用户
	 * @param map
	 * @return
	 * @date 2016-04-06
	 */
	public  Integer createUser(Map<Object, Object> map);
	/**
	 * 查询用户名
	 * @param map
	 * @return
	 * @date 2016-04-07
	 */
	public  Integer queryUsername(Map<Object, Object> map);
	/**
	 * 编辑用户
	 * @param map
	 * @return
	 * @date 2016-03-14
	 */
	public  Integer updateUser(Map<Object, Object> map);
	/**
	 * 查看用户
	 * @param map
	 * @return
	 * @date 2016-03-14
	 */
	List<Map<String,String>> queryUser(Map<Object, Object> map);
	
	List<UserBean>  queryUserById(String roleId);
	
	UserBean getUserById(String userId);
	UserBean getUserByCode(String actCode);
	UserBean getUserByName(String createUser);
	
}
