package com.udbac.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;

import com.udbac.entity.AddMoniterPlanInfo;
import com.udbac.entity.AddMonitorPlanBean;
import com.udbac.entity.AddMonitorSchemeInfo;
import com.udbac.entity.ButtonInfo;
import com.udbac.entity.KeyValueInfo;
import com.udbac.entity.QueryActivityInfo;
import com.udbac.entity.QueryCustomerInfo;
import com.udbac.entity.TbAmpBasicActivityInfo;
import com.udbac.entity.TodoInfoBean;
import com.udbac.entity.User;

/**
 * 监测方案Dao
 * 
 * @author LFQ
 * @date 2016/4/5
 */
public interface MonitorPlanDao {

	/**
	 * 查询监测方案
	 * 
	 * @param params
	 *            startDate 活动实际上线时间 endDate 活动结束时间 activityCode 活动编号
	 * @return 查询监测方案的结果
	 */
	List<TbAmpBasicActivityInfo> list(Map<String, Object> params);
	
	
	/**
	 * 通过短代码查找活动信息
	 * @param mics
	 * @return
	 */
	List<TbAmpBasicActivityInfo> findActByMics(@Param("mics") Set<String> mics);

	/**
	 * 更改上线活动状态
	 * 
	 * @param ampbBean
	 * @return
	 */
	int updateState(TbAmpBasicActivityInfo actInfo);
	
	//活动投放结束，接口人置将活动状态置为8，此时汇总程序会自动汇总活动状态为8的结案数据，
//	接口人拿到汇总结案报告后，发现活动需要补量，则将活动状态置为6【已投放】，同时请帮忙更新【结案报告数据汇总断点标记表】表中的sum_status字段为0。
//	（若多次补量，则只要有活动状态从8变为6，都需要更新断点标记表的sum_status字段）
	@Update("update tb_amp_closed_break_point set sum_status = 0 where activity_code = #{actCode}")
	int updateSumState(@Param("actCode") String actCode);

	/**
	 * 根据活动编号查找某个监测方案
	 * 
	 * @param actCode
	 * @return
	 */
	TbAmpBasicActivityInfo findByCode(@Param("actCode") String actCode);

	/**
	 * 修改监测活动方案
	 * 
	 * @param map
	 */
	void updateMonitor(TbAmpBasicActivityInfo activityInfo);

	@Update("UPDATE tb_amp_basic_activity_info SET activity_end_date = #{activityEndDate}, reality_start_date = #{realityStartDate} WHERE activity_code =  #{activityCode}")
	void updateMonitorEdate(TbAmpBasicActivityInfo activityInfo);

	/**
	 * 新增活动信息
	 * 
	 * @param activityInfo
	 */
	void insertActivity(TbAmpBasicActivityInfo activityInfo);

	// 添加活动信息
	void addActivityinfo(AddMonitorPlanBean bean);

	// 添加监测方案信息
	void addMonitorSchemeInfo(Map<String, Object> map);
	
	// 进入到添加活动子方案后显示的活动编号
	List<AddMonitorPlanBean> showMonitorPlan(@Param("actCode") String actCode);

	// 获取添加活动子方案的option人员列表
	List<User> getUser();

	// 添加子活动方案时，页面显示父级活动名称和客户名称
	public List<QueryCustomerInfo> queryActNameAndCus(@Param("actCode") String actCode);

	//判断活动状态
	public List<AddMonitorPlanBean> getActivityState(@Param("actCode") String actCode);
	
	// 获取客户列表
	List<QueryCustomerInfo> queryCustomer();

	// 查询某个活动所属的客户名称
	@SuppressWarnings("rawtypes")
	List queryActAndCus(@Param("actCode") String actCode);

	// 查询监测中心人员
	@SuppressWarnings("rawtypes")
	List queryFontSupport(@Param("actCode") String actCode);

	// 查询前端监测人员
	@SuppressWarnings("rawtypes")
	List queryAfterSupport(@Param("actCode") String actCode);

	// 查询后端监测人员
	@SuppressWarnings("rawtypes")
	List querymonitor(@Param("actCode") String actCode);

	// 查询修改活动方案
	@SuppressWarnings("rawtypes")
	List queryAll(@Param("actCode") String actCode, QueryActivityInfo qat);

	/**
	 * 删除单个的监测方案信息
	 * @param del_id
	 */
	void delMonitorInfo(@Param("list") List list); 
	
	/**
	 * 修改监测方案信息 2016-05-18
	 * @param activity_code
	 * @param info
	 */
	void updateMonitorInfo(Map<String , Object> map);

	// 修改活动信息
	void updateActMonitorInfo(@Param("activity_code") String activity_code, @Param("bean") AddMonitorPlanBean bean, @Param("updateUser") Integer updateUser);

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
	 * 添加活动备注
	 * 
	 * @param bean
	 * @author lily
	 */
	void addTodoTask(TodoInfoBean bean);

	/***
	 * 查询活动备注
	 * 
	 * @param actCode
	 *            活动编号
	 * @author lily
	 * @return
	 */
	ArrayList<TodoInfoBean> queryTodoInfo(Map<String, Object> map);

	/***
	 * 更新活动问题数
	 * 
	 * @param activity_code
	 */
	void updateActivityException(@Param("actCode") String activity_code);

	/***
	 * 查询任务模板
	 * 
	 * @param bean
	 *            根据上一个状态、下一个状态、角色查询
	 * @return
	 */
	ArrayList<TodoInfoBean> queryTaskList(TodoInfoBean bean);

	/***
	 * 根据用户编号查询活动
	 * 
	 * @return
	 */
	public ArrayList<TbAmpBasicActivityInfo> queryActivityByUser(@Param("userId") String userId);

	/***
	 * 根据活动编号查询相关信息
	 * 
	 * @param activity_code
	 *            lp 2016-05-12 审查监测活动用
	 *
	 */
	public List<AddMonitorSchemeInfo> queryMonitorByActivityCode(String activity_code);

	/***
	 * 更新页面URL 和按钮id
	 * 
	 * @param map
	 */
	void updateMonitorPageUrlAndButtonId(Map<String, String> map);

	/***
	 * 更新活动状态
	 * 
	 * @param map
	 */
	void updateActState(Map<String, String> map);

	/***
	 * 更新活动Todo状态
	 * 
	 * @param _todoId
	 *            编号
	 * @author lily
	 * @return
	 */
	 void updateTodoState(@RequestParam(value = "todoId") String _todoId);
	 
	 @Select("select activity_name from tb_amp_basic_activity_info where activity_code = #{actCode}")
	 AddMonitorPlanBean getMonitorInfoByCode(@Param("actCode") String actCode);

	
	List<AddMonitorPlanBean> monitorPlanDetail(@Param("actCode") String actCode);
	
	
	
	
	/**
	 * 查询监测方案下面的信息
	 * 2017年07月05日16:25:20
	 */
	List<AddMonitorPlanBean> planBean(@Param("actCode") String actCode);
	
	
	
	/**
	 * 2017年07月06日11:01:17
	 * @param map
	 */
	AddMonitorSchemeInfo getOrignInfo(@Param("actCode")String actCode,@Param("schemeId")Integer schemeId);
}
