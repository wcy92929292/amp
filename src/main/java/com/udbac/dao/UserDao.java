package com.udbac.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.entity.TbAmpBasicCustomerInfo;
import com.udbac.model.UserBean;

public interface UserDao {

	/**
	 * 注册用户
	 * @param map
	 * @author LiQ
	 * @return
	 * @date 2016-04-06
	 */
	public  Integer createUser(Map<Object, Object> map);
	/**
	 * 注册用户
	 * @param map
	 * @author LiQ
	 * @return
	 * @date 2016-04-07
	 */
	public  Integer queryUsername(Map<Object, Object> map);
	/**
	 * 编辑用户
	 * @author LiQ
	 * @param map
	 * @return
	 * @date 2016-04-06
	 */
	public  Integer updateUser(Map<Object, Object> map);
	/**
	 * 查看用户
	 * @author LiQ
	 * @param map
	 * @return
	 * @date 2016-04-06
	 */
	public List<Map<String, String>> queryUser(Map<Object, Object> map) ;
	
	/***
	 * 根据角色编号查询用户
	 * @param roleId 角色编号
	 * @return
	 */
	public List<UserBean> queryUserById(@Param("_roleId")String _roleId) ;
	/**
	 * 查询所有角色
	 * @author LiQ
	 * @param Object
	 * @return
	 * @date 2016-04-13
	 */
	public List<TbAmpBasicCustomerInfo>getRole( );
	/**
	 * 查询所有用户
	 * @author LiQ
	 * @param Object
	 * @return
	 * @date 2016-04-13
	 */
	public  List<TbAmpBasicCustomerInfo>getCustomer( );
	/**
	 * 删除用户
	 * @author LiQ
	 * @param map
	 * @return
	 * @date 2016-04-25
	 */
	public  Integer deleteUser(Map<Object, Object> map);
	
	
	@Select("select real_name,mailbox from tb_amp_basic_user_info where user_id = #{userId}::integer")
	UserBean getUserById(@Param("userId") String userId);
	
	@Select("select port_people USER_ID from tb_amp_basic_activity_info where activity_code = #{actCode}")
	UserBean getUserByCode(@Param("actCode") String actCode);
	
	@Select("select mailbox from tb_amp_basic_user_info where real_name = #{createUser}")
	UserBean getUserByName(@Param("createUser") String createUser);
}
