package com.udbac.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.udbac.dao.UserDao;
import com.udbac.entity.TbAmpBasicCustomerInfo;
import com.udbac.model.UserBean;
import com.udbac.service.user.UserService;
import com.udbac.util.LogUtil;


/**
 * 用户管理Controller
 * @author LiQ
 * @date 2016-04-06
 */



@RestController
@RequestMapping("/user")
public class UserController {
	private LogUtil logUtil = new LogUtil(UserController.class);
	//private static final Log logger = LogFactory.getLog(LoginController.class);
	@Autowired(required = true)
	private UserService userService;
	
	@Autowired(required = true)
	private UserDao userDao;
	
	

	/**
	 * 注册用户
	 * @author LiQ
	 * @param _username
	 * @param _password
	 * @param _uname
	 * @param _tel
	 * @param _email
	 * @param _province
	 * @param _role
	 * @return
	 * @throws Exception
	 * @date 2016-04-06
	 */
	@RequestMapping(value = "/createUser.do",  method = RequestMethod.POST)
	public @ResponseBody  Integer createUser(
			@RequestParam(value = "username") String  _username,
			@RequestParam(value = "password") String  _password,
			@RequestParam(value = "uname") String  _uname,
			@RequestParam(value = "tel") String  _tel,
			@RequestParam(value = "email") String  _email,
			@RequestParam(value = "province") String  _province,
			@RequestParam(value = "role") String  _role
			)throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", _username);
		/*String pwd=MD5Util3.convertMD5(_password);
				//MD5Util.md5Encode(_password);
		String pwd1=MD5Util2.MD5(_password);
		String pwd2=MD5Util2.KL(pwd1);
		String pwd3=MD5Util2.JM(pwd2); 
		System.out.println("MD5Util2:MD5后："+MD5Util2.MD5(_password));
		System.out.println("MD5Util2:MD5后再加密："+MD5Util2.KL(MD5Util2.MD5(_password)));
		System.out.println("MD5Util2:解密MD5后的："+MD5Util2.JM(MD5Util2.KL(MD5Util2.MD5(_password))));
		System.out.println("MD5Util3:MD5后：" + MD5Util3.string2MD5(_password));  
		System.out.println("MD5Util3:加密的：" + MD5Util3.convertMD5(_password)); 
		String pwd4=MD5Util3.convertMD5(_password);
		System.out.println("MD5Util3:解密的：" + MD5Util3.convertMD5(pwd4)); */
		map.put("password", _password);
		map.put("uname", _uname);
		map.put("tel", _tel);
		map.put("email", _email);
		map.put("province", _province);
		map.put("role", _role);
		try {
			userService.createUser(map);
			logUtil.logInfo("OLM-ID00013");
			logUtil.logInfoCon("注册用户添加信息："+map.toString());
			//logger.info("——————————————注册用户成功！——————————————");
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			logUtil.logErrorExc(e);
			//logger.error(e);
			
		}
		logUtil.logError("OLM-ED00013");
		logUtil.logInfoCon("注册用户添加信息："+map.toString());
		//logger.info("——————————————注册用户失败！——————————————");
		return  0;
	}
	/*private String convertMD5(String _password) {
		// TODO Auto-generated method stub
		return null;
	}
	private String KL(String md5) {
		// TODO Auto-generated method stub
		return null;
	}
	private String MD5(String _password) {
		// TODO Auto-generated method stub
		return null;
	}*/
	/****
	 * 查询用户名是否可用
	 * @author LiQ
	 * @param _username
	 * @return 
	 * @throws Exception
	 * @data 2016-04-07
	 */
	@RequestMapping(value = "/queryUsername.do",  method = RequestMethod.POST)
	public @ResponseBody Integer queryUsername(
			@RequestParam(value = "username") String  _username
			)throws Exception {
		//System.out.println(_username);
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", _username);
		Integer count = userService.queryUsername(map);
		//System.out.println(count);
		try {
			if(count>0){
				logUtil.logInfo("OLM-ED00010");
				//logger.info("用户名已存在————————数据库查询到用户名！");
				return 1;
			}
		} catch (Exception e) {
			// TODO: handle exception
			//logger.error(e);
			logUtil.logErrorExc(e);
		}
		logUtil.logInfo("OLM-ID00010");
		//logger.info("用户名可以使用————————数据库未查询到用户名！");
		return 0;
		
	}
	
	/***
	 * 根据角色编号查询用户
	 * @param _roleId 角色编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryUserById.do",  method = RequestMethod.POST)
	public @ResponseBody List<UserBean>  queryUserById(
			@RequestParam(value = "roleId") String  _roleId
			)throws Exception {
 
		List<UserBean> list = userService.queryUserById(_roleId);
		return list;
	}
	
	
	/**
	 * 编辑用户获取session信息
	 * @author LiQ
	 * @param request
	 * @param response
	 * @return list
	 * @throws Exception
	 * @data 2016-04-12
	 */
	@RequestMapping(value = "/getUser.do",  method = RequestMethod.POST)
	public @ResponseBody UserBean getloginUser(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		if(user == null){
			return null;
		}
		 try {
			if(user.getREAL_NAME()!=""&&user.getREAL_NAME()!=null){
				 logUtil.logInfoCon("已获取到session内容"+user.toString());
			}
				logUtil.logInfoCon("未获取到session内容");
			 logUtil.logDebugEd();
		} catch (Exception e) {
			// TODO: handle exception
			logUtil.logInfo(e.toString());
		}
		 
		return user;
	}
	/**
	 * 获取客户名称
	 * @author LiQ
	 * @return
	 * @throws Exception
	 * @data 2016-04-13
	 */
	@RequestMapping(value = "/getCustomer.do",  method = RequestMethod.POST)
	public @ResponseBody List<TbAmpBasicCustomerInfo>getCustomer(
			)throws Exception {
		List<TbAmpBasicCustomerInfo> list=userDao.getCustomer();
		logUtil.logInfo("OLM-ID00020");
		logUtil.logInfoCon(list.toString());
		return list;
	}
	/**
	 * 获取角色名称
	 * @author LiQ
	 * @return list
	 * @throws Exception
	 * @data 2016-04-13
	 */
	@RequestMapping(value = "/getRole.do",  method = RequestMethod.POST)
	public @ResponseBody List<TbAmpBasicCustomerInfo>getRole(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		List<TbAmpBasicCustomerInfo> list = userDao.getRole();
		logUtil.logInfo("OLM-ID00019");
		logUtil.logInfoCon(list.toString());
		return list;
	}
	
	
	/**
	 * 编辑用户
	 * @author LiQ
	 * @param _username
	 * @param _password
	 * @param _uname
	 * @param _tel
	 * @param _email
	 * @param _province
	 * @param _role
	 * @return
	 * @throws Exception
	 * @date 2016-04-13
	 */
	@RequestMapping(value = "/updateUser.do",  method = RequestMethod.POST)
	public @ResponseBody  Integer updateUser(
			@RequestParam(value = "userid") String  _userid,
			@RequestParam(value = "username") String  _username,
			@RequestParam(value = "password") String  _password,
			@RequestParam(value = "uname") String  _uname,
			@RequestParam(value = "tel") String  _tel,
			@RequestParam(value = "email") String  _email,
			@RequestParam(value = "province") String  _province,
			@RequestParam(value = "role") String  _role,
			@RequestParam(value = "user_state") String  _user_state
			
			)throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("userid", _userid);
		map.put("username", _username);
		//String password= MD5Util3.convertMD5(_password);
		map.put("password",  _password);
		//System.out.println("修改密码为："+password);
		map.put("uname", _uname);
		map.put("tel", _tel);
		map.put("email", _email);
		map.put("province", _province);
		map.put("role", _role);
		map.put("user_state", _user_state);
		try {
			userDao.updateUser(map);
			logUtil.logInfo("OLM-ID00013");
			logUtil.logInfo("编辑用户信息："+map.toString());
			//logger.info("——————————————编辑用户成功！——————————————");
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			logUtil.logErrorExc(e);
		}
		logUtil.logError("OLM-ED00013");
		logUtil.logInfo("编辑用户信息："+map.toString());
		//logger.info("——————————————编辑用户失败！——————————————");
		return  0;
	}
	
	/**
	 * 查询用户表
	 * @author LiQ
	 * @param _uname
	 * @param _role
	 * @return list
	 * @throws Exception
	 * @data 2016-04-22
	 */
	@RequestMapping(value = "/queryUser.do",  method = RequestMethod.POST)
	public @ResponseBody List<Map<String, String>> queryUser(
			@RequestParam(value = "sdate") String _sdate,
			@RequestParam(value = "edate") String _edate,
			@RequestParam(value = "uname") String  _uname,
			@RequestParam(value = "user_name") String  user_name,
			@RequestParam(value = "role") String  _role_name
			)throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("sdate", _sdate);
		map.put("edate", _edate);
		map.put("uname", _uname);
		map.put("role_name", _role_name);
		map.put("user_name", user_name);
		System.out.println(map.toString());
		List<Map<String,String>> list = userService.queryUser(map);
		System.out.println(list.toString());
		try {
			if(list.size()>0){
				logUtil.logInfoCon(list.toString());
				//logger.info("——————————————数据库已查询到用户信息！——————————————");
			}
			logUtil.logDebugEd();
		} catch (Exception e) {
			// TODO: handle exception
			logUtil.logErrorExc(e);
		}
		
		
		return list;
	}
	/**
	 * 管理用户_删除用户
	 * @author LiQ
	 * @param _username
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteUserByusername.do",  method = RequestMethod.POST)
	public @ResponseBody  Integer deleteUser(
			@RequestParam(value = "userName") String  _username
			)throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", _username);
		System.out.println(_username);
		try {
			userDao.deleteUser(map);
			
			logUtil.logInfoCon("删除用户信息："+_username+map.toString());
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			logUtil.logErrorExc(e);
		}
		logUtil.logInfoCon("删除用户信息："+_username+map.toString());
		return  0;
	}
	
	
	
	
	
	
}
