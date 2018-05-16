package com.udbac.dao;

import com.udbac.model.UserBean;
/****
 * 用户登录
 * @author lp	
 * @date 2016-04-06
 *
 */
public interface LoginDao {
	//根据输入用户名，查询用户基础表信息
	public UserBean getUserByUserName(String username);
	public Integer QueryUserName(String  username);
}
