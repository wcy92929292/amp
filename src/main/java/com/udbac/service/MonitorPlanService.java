package com.udbac.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.udbac.entity.AddMoniterPlanInfo;
import com.udbac.entity.AddMonitorPlanBean;
import com.udbac.entity.AddMonitorSchemeInfo;
import com.udbac.entity.ButtonInfo;
import com.udbac.entity.KeyValueInfo;
import com.udbac.entity.QueryCustomerInfo;
import com.udbac.entity.QueryActivityInfo;
import com.udbac.entity.TbAmpBasicActivityInfo;
import com.udbac.entity.TodoInfoBean;
import com.udbac.entity.User;

/**
 * 监测方案Service
 * 
 * @author LFQ
 *
 */
public interface MonitorPlanService {

	/**
	 * 监测方案一览
	 * 
	 * @param params
	 * @return
	 */
	List<TbAmpBasicActivityInfo> list(Map<String, Object> params);

	/**
	 * 提交上线状态
	 * 
	 * @param file
	 *            强制上线时,作为证据
	 * @param actCode
	 *            目标活动编号
	 * @param actState
	 *            当前活动状态
	 * @param actState
	 *            客户编号
	 * @return
	 */
	String updateState(MultipartFile file, String actCode, String actState, String cusCode,Integer updateState);

	/****
	 * 添加活动信息和监测方案信息
	 * 
	 * @param parm
	 *            监测方案内容
	 * @param bean
	 *            活动信息内容、
	 * @author lily
	 * @return 添加成功； 添加失败；活动编号已存在
	 */
	String saveActivity(String param, AddMonitorPlanBean bean);

	// 添加活动信息
	void addActivityinfo(AddMonitorPlanBean bean);

	// 添加监测方案信息
	void addMonitorSchemeInfo(Map<String, Object> map);

	/**
	 * 修改监测方案信息
	 * 
	 * @param map
	 */
	void updateMonitorInfo(Map<String, Object> map);

	//判断活动状态
	List<AddMonitorPlanBean> getActivityState(String actCode);

	// 进入到添加活动子方案后显示的页面信息
	List<AddMonitorPlanBean> showMonitorPlan(String actCode);

	// 获取添加活动子方案的option人员列表
	List<User> getUser();

	List<QueryCustomerInfo> queryActNameAndCus(String actCode);

	// 获取客户列表
	List<QueryCustomerInfo> queryCustomer();

	List queryMonitor(String actCode);

	List queryFontSupport(String actCode);

	List queryAfterSupport(String actCode);

	// /查询某个活动所属的客户名称
	@SuppressWarnings("rawtypes")
	List queryActAndCus(@Param("actCode") String actCode);

	// 查询修改活动方案
	List queryAll(String actCode, QueryActivityInfo qat);

	// 活动信息
	void updateActMonitorInfo(String activity_code, AddMonitorPlanBean bean,Integer updateUser);
	
	/**
	 * 删除单个的监测方案信息
	 * @param del_id
	 */
	void delMonitorInfo(List list); 

	/***
	 * 查询活动编号是否存在
	 * 
	 * @param acode
	 *            活动编号
	 * @author lily
	 * @return 1存在；0不存在
	 */
	Integer queryMonitorCode(String acode);

	/***
	 * 增加活动备注
	 * 
	 * @param bean
	 * @author lily
	 * @return
	 */
	String addTodoTask(TodoInfoBean bean);

	/***
	 * 查询活动备注
	 * 
	 * @param map
	 * @return
	 */
	ArrayList<TodoInfoBean> queryTodoInfo(Map<String, Object> map);

	/***
	 * 查询用户下的活动
	 * 
	 * @param _userId
	 *            用户编号
	 * @return
	 */
	ArrayList<TbAmpBasicActivityInfo> queryActivityByUser(@RequestParam(value = "userId") String _userId);

	/***
	 * 更新活动Todo状态
	 * 
	 * @param _todoId
	 *            todo编号
	 * @return
	 */
	public String updateTodoState(@Param("todoId") String _todoId);
	
	public TbAmpBasicActivityInfo findByCode(String actCode);

	AddMonitorPlanBean getMonitorInfoByCode(String actCode);

	List<AddMonitorPlanBean> monitorPlanDetail(String actCode);

	/**
	 * 查询监测方案下面的信息
	 * 2017年07月05日16:25:20
	 */
	List<AddMonitorPlanBean> planBean(@Param("actCode") String actCode);
	
	
	AddMonitorSchemeInfo getOrignInfo(String actCode,Integer schemeId);
}
