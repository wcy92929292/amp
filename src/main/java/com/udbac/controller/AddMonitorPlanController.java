package com.udbac.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.entity.AddMonitorPlanBean;
import com.udbac.entity.AddMonitorSchemeInfo;
import com.udbac.entity.QueryActivityInfo;
import com.udbac.entity.QueryCustomerInfo;
import com.udbac.entity.TbAmpBasicActivityInfo;
import com.udbac.entity.TodoInfoBean;
import com.udbac.entity.User;
import com.udbac.model.UserBean;
import com.udbac.service.MonitorPlanService;
import com.udbac.service.user.UserService;
import com.udbac.util.JSONUtil;
import com.udbac.util.LogUtil;
import com.udbac.util.MailInitBean;
import com.udbac.util.mail;

/**
 * @author HX-韩 监测方案子方案
 * 
 */
@Controller
@RequestMapping("/amp")
public class AddMonitorPlanController {

	@Autowired
	MonitorPlanService monitorPlanService;
	@Autowired
	private UserService userService;
	
	private LogUtil logUtil = new LogUtil(AddMonitorPlanController.class);

	/**
	 * 进入到监测方案子方案画面显示的信息
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/showMonitorPlan.do")

	public List showMonitorPlan(@RequestParam("actCode") String parent_activity_code, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		// 根据活动编号查询相应详细信息
		List list = monitorPlanService.showMonitorPlan(parent_activity_code);
		session.setAttribute("list", list);
		// monitorPlanService.getUser();

		// 查询列表下拉人员
		List<User> user = monitorPlanService.getUser();
		list.addAll(user);

		QueryActivityInfo qat = new QueryActivityInfo();
		List<QueryActivityInfo> all = monitorPlanService.queryAll(parent_activity_code, qat);
		all.addAll(list);
		System.out.println(all);
//		session.setAttribute("qat", all);
		return all;
	}

	/**
	 * 监测方案详细信息
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	@SuppressWarnings({ "rawtypes"})
	@ResponseBody
	@RequestMapping("/monitorPlanDetail.do")

	public List monitorPlanDetail(@RequestParam("actCode") String parent_activity_code, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// 根据活动编号查询相应详细信息s
		List list = monitorPlanService.monitorPlanDetail(parent_activity_code);
		
		return list;
	}
	// 客户列表信息
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "/queryCustomer.do")
	public List queryCustomer(HttpServletResponse response,Map<String, Object> map) throws Exception {
		List queryCus = null;
		
		try {
			queryCus = monitorPlanService.queryCustomer();
		} catch (DataAccessException e) {
//			System.out.println("test:"+e.getMessage());
			map.put("dbError", "连接数据库失败,请稍后重试!");
			response.getWriter().write(JSONUtil.beanToJson(map));
		}
		
		return queryCus;
	}

	@ResponseBody
	@RequestMapping("/activityState.do")
	public List<AddMonitorPlanBean> getActivityState(
			@RequestParam(value = "actCode", required = false) String actCode) {
		List<AddMonitorPlanBean> list = monitorPlanService.getActivityState(actCode);
		return list;
	}

	/****
	 * 添加活动信息和活动监测方案
	 * 
	 * @param param 活动监测方案
	 * @param bean 活动信息内容
	 */
	@RequestMapping(value = "/saveActivity.do")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@ResponseBody
	public Map<String, Object> saveActivity(@RequestParam(value="param",required = false) String param, @ModelAttribute AddMonitorPlanBean bean,
			@RequestParam(value = "parent_idf", required = false) String parent_idf,
			@RequestParam(value = "parent_actCode", required = false) String parent_actCode,
			HttpServletRequest request) {

		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		UserBean userById = userService.getUserById(bean.getAfter_support_people());
		TodoInfoBean todo = new TodoInfoBean();

		System.err.println(bean.getStat_mark());
		logUtil.logDebugSt();
		Map<String, Object> map = new HashMap<>();
		try {
			String flag = "";
			// 父子活动标识,0父级,1子级
			if ("0".equals(parent_idf)) {
				map.put("parent_idf", 0);
				map.put("parent_actCode", null);
			} else if ("1".equals(parent_idf)) {
				map.put("parent_idf", 1);
				map.put("parent_activity_code", parent_actCode);
			}
			if(param.length() != 0){
				flag = monitorPlanService.saveActivity(param, bean);
			}else{
				flag = monitorPlanService.saveActivity(null, bean);
			}
			
			map.put("status", 0);
			map.put("message", flag);
			String todoContent = "操作时间:" + new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()) + " "
					+ "操作人:" + user.getUSER_NAME() + " " + "增加的活动编号:" + bean.getActivity_code();
			todo.setTodoContent(todoContent);
			todo.setActivityCode(bean.getActivity_code());
			todo.setReceptionTaskUid(user.getUSER_NAME());
			todo.setTodoState("2");
			todo.setOfState(0);
			monitorPlanService.addTodoTask(todo);
			if ("0".equals(parent_idf)){
				mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, userById.getMAILBOX(), "", bean.getActivity_code()+" "+bean.getActivity_name()+" 需要后端审核！！", 
						
						"<br>新活动方案已创建，请尽快审核。（注意填写页面URL和按钮ID）"+
						"<br><br><br>提示：此信为系统自动发送邮件，请勿直接回复。"+
						"<br>可联系["+ user.getREAL_NAME()+ "] "+ 
						"Email:<a href='mailto:"+user.getMAILBOX()+"'</a>"+ user.getMAILBOX()
						, MailInitBean.username, MailInitBean.password);
				}else if ("1".equals(parent_idf)){
					mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, userById.getMAILBOX(), "", bean.getActivity_code()+" "+bean.getActivity_name()+" 需要后端审核！！", 
							
							"<br>新活动方案子方案已创建，请尽快审核。（注意填写页面URL和按钮ID）"+
							"<br><br><br>提示：此信为系统自动发送邮件，请勿直接回复。"+
							"<br>可联系["+ user.getREAL_NAME()+ "] "+ 
							"Email:<a href='mailto:"+user.getMAILBOX()+"'</a>"+ user.getMAILBOX()
							, MailInitBean.username, MailInitBean.password);
			}
		} catch (Exception e) {
			// logUtil.logErrorExc(e);
			e.getStackTrace();
		}
		logUtil.logDebugEd();
		return map;
	}

	/**
	 * 添加子方案时，显示父级活动名称和客户名称
	 * 
	 * @param actCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryActNameAndCus.do")
	public List<QueryCustomerInfo> queryActNameAndCus(@RequestParam(value = "actCode", required = false) String actCode,
			@RequestParam(value = "parent_idf", required = false) String parent_idf) {
		List<QueryCustomerInfo> nameAndCus = monitorPlanService.queryActNameAndCus(actCode);
		
		return nameAndCus;
	}

	/**
	 * 
	 * 查询当前选择活动对应的客户名称
	 * 
	 * @param parent_activity_code
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/queryActAndCus.do")
	public List queryqueryActAndCus(@RequestParam(value = "actCode", required = false) String activity_code) {
		List queryActAndCus = monitorPlanService.queryActAndCus(activity_code);
		return queryActAndCus;
	}

	/**
	 * 
	 * 获取活动对应的监测人员信息
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/queryMonitor.do")
	public List querymonitor(@RequestParam(value = "actCode", required = false) String parent_activity_code) {
		List querymonitor = monitorPlanService.queryMonitor(parent_activity_code);
		return querymonitor;
	}

	/**
	 * 
	 * 获取活动对应的前端监测中心人员信息
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/queryFontSupport.do")
	public List queryFontSupport(@RequestParam(value = "actCode", required = false) String parent_activity_code) {
		List querymonitor = monitorPlanService.queryFontSupport(parent_activity_code);
		return querymonitor;
	}

	/**
	 * 
	 * 获取活动对应的后端监测人员信息
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping("/queryAfterSupport.do")
	public List queryAfterSupport(@RequestParam(value = "actCode", required = false) String parent_activity_code) {
		List querymonitor = monitorPlanService.queryAfterSupport(parent_activity_code);
		return querymonitor;
	}

	/**
	 * 更新活动信息
	 * 
	 * @param param 接收参数：活动监测信息
	 * @param bean
	 * @param activity_code 活动编号
	 * @param info
	 * @param request
	 * @param flag 监测方案信息判断是修改还是增加的 ， 分别调用 0:修改方案信息 1:增加方案信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "/updateMonitor.do")
	public Map<String, Object> updateMonitor(@RequestParam(value = "param", required = false) String param,
			@ModelAttribute AddMonitorPlanBean bean,
			@RequestParam(value = "activity_code", required = false) String activity_code,
			@RequestParam(value = "activityState", required = false) Integer activityState,
			HttpServletRequest request,
			@ModelAttribute TodoInfoBean todo,
			@RequestParam(value = "delId", required = false) String delId) {
		
		logUtil.logDebugSt();
		System.out.println(delId == null);
		System.out.println(delId == "");
		System.out.println(delId == "null");
		if(delId != null && !delId.equals("") && !delId.equals("null")){
			String[] split = delId.split(",");
			List delList = new LinkedList<>();
			for (int i = 0; i < split.length; i++) {
				if("".equals(split[i])){continue;}
				delList.add(Integer.parseInt(split[i]));
			}
			
			//删除选择的监测方案
			if(delList.size() != 0){
				monitorPlanService.delMonitorInfo(delList);
			}
		}
		// 返回信息
		Map<String, Object> mapMsg = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> mapp = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		Integer uID = user.getUSER_ID();
		UserBean userById = userService.getUserById(bean.getAfter_support_people());
		// 取session域中的对象
		List<AddMonitorPlanBean> list= (List<AddMonitorPlanBean>) session.getAttribute("list");
		if(param.length() !=0){
			ObjectMapper map = new ObjectMapper();
			try {
				List<AddMonitorSchemeInfo> lst = map.readValue(param, new TypeReference<List<AddMonitorSchemeInfo>>() {
				});
				System.out.println(lst);
				String value = bean.getActivity_code();
		        String[] names = value.split(",");
				mapp.put("acid",names[0]);
				mapp.put("userID", uID);

				List addlist = new ArrayList<>();
				List uplist = new ArrayList<>();
				//增加操作
				if (lst != null) {
					for (int i = 0; i < lst.size(); i++) {
						Integer scheme_id = lst.get(i).getScheme_id();
						
						// scheme_id 为0 表示新加的活动方案，执行增加操作
						if (scheme_id == 0) {
							addlist.add(lst.get(i));
						}else{  //比较一下是否发生过变化，若是有变化，则更新一条信息
							AddMonitorSchemeInfo getInfo = lst.get(i);  //当前页面传过来的信息
							
							//数据库里查询到的信息
							AddMonitorSchemeInfo orignInfo = monitorPlanService.getOrignInfo(names[0], scheme_id);
							
//							if(getInfo.getPage_name().equals(orignInfo.getPage_name()) && getInfo.getPage_id().equals(orignInfo.getPage_id())
//								&& getInfo.getDcsid_s().equals(orignInfo.getDcsid_s())
//								&& getInfo.getBtn_type().equals(orignInfo.getBtn_type())	
//								&& getInfo.getBtn_name().endsWith(orignInfo.getBtn_name()) 
//								&& getInfo.getBtn_id().equals(orignInfo.getBtn_id())
//								&& getInfo.getPage_url().equals(orignInfo.getPage_url()) && getInfo.getMatch_url().equals(orignInfo.getMatch_url())
//								&& getInfo.getKey().equals(orignInfo.getKey()) && getInfo.getValue().equals(orignInfo.getValue()) &&
//								getInfo.getOp().equals(orignInfo.getOp())
//								){
//								continue;
//							}else{
								uplist.add(getInfo);
//							}
						}
					}
					if (addlist.size() != 0) { //增加监测方案
						mapp.put("userID", uID);
						mapp.put("list", addlist);
						monitorPlanService.addMonitorSchemeInfo(mapp);
						mapMsg.put("message", "1");
					}else if (uplist.size() != 0) {  //修改监测方案
						mapp.put("userID", uID);
						mapp.put("list", uplist);
						monitorPlanService.updateMonitorInfo(mapp);
						if(activityState == 6){ //上线后修改监测方案的发邮件
							mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, MailInitBean.DBUser, "", bean.getActivity_code()+" "+bean.getActivity_name()+" 已修改需要再次审核！！", 
									
									"<br>活动方案已修改，请尽快重新审核。（注意填写页面URL和按钮ID）"+
									"<br><br><br>提示：此信为系统自动发送邮件，请勿直接回复。"+
									"<br>可联系["+ user.getREAL_NAME()+ "] "+"</br>"+ 
									"Email:<a href='mailto:"+MailInitBean.DBUser+"'</a>"+ MailInitBean.DBUser
									, MailInitBean.username, MailInitBean.password);
						}
						}
						mapMsg.put("message", "1");
					mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, userById.getMAILBOX(), "", bean.getActivity_code()+" "+bean.getActivity_name()+" 已修改需要再次审核！！", 
							
							"<br>活动方案已修改，请尽快重新审核。（注意填写页面URL和按钮ID）"+
							"<br><br><br>提示：此信为系统自动发送邮件，请勿直接回复。"+
							"<br>可联系["+ user.getREAL_NAME()+ "] "+"</br>"+ 
							"Email:<a href='mailto:"+user.getMAILBOX()+"'</a>"+ user.getMAILBOX()
							, MailInitBean.username, MailInitBean.password);
				}
				
				String todoContent = "操作时间:" + new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()) + " "
						+ "操作人:" + user.getUSER_NAME() + " " + "修改的活动编号:" + bean.getActivity_code();
				todo.setTodoContent(todoContent);
				todo.setActivityCode(activity_code);
				todo.setReceptionTaskUid(user.getUSER_NAME());
				todo.setTodoState("2");
				// 得到这个活动的活动状态
				List<AddMonitorPlanBean> state = monitorPlanService.getActivityState(activity_code);
				if (state.size() != 0) {
					todo.setOfState(state.get(0).getActivity_state());
				}

				// monitorPlanService.addTodoTask(todo);
			} catch (Exception e) {
				e.printStackTrace();
				// logUtil.logErrorExc(e);
				mapMsg.put("message", "添加失败，请重试");
			}
			logUtil.logDebugEd();
		}else{
			mapMsg.put("message", "1"); //没有下面的监测方案信息
		}
		
		if (list.size()!= 0 && ((list.get(0).getPredict_start_date() != null && !list.get(0).getPredict_start_date().equals(bean.getPredict_start_date())) 
				|| (list.get(0).getCustomer_id() != null && !list.get(0).getCustomer_id().equals(bean.getCustomer_id()))
				|| (list.get(0).getActivity_name() != null && !list.get(0).getActivity_name().equals(bean.getActivity_name()))
				|| (list.get(0).getMonitor_people() != null && !list.get(0).getMonitor_people().equals(bean.getMonitor_people()))
				|| (list.get(0).getFont_support_people() != null && !list.get(0).getFont_support_people().equals(bean.getFont_support_people()))
				|| (list.get(0).getAfter_support_people() !=null && !list.get(0).getAfter_support_people().equals(bean.getAfter_support_people()))
				|| (list.get(0).getMemo() != null && !list.get(0).getMemo().equals(bean.getMemo())) 
				|| (list.get(0).getStat_mark() != null && !list.get(0).getStat_mark().equals(bean.getStat_mark())))) {
			String value = bean.getActivity_code();
	        String[] names = value.split(",");
			monitorPlanService.updateActMonitorInfo(names[0], bean, uID); //更新活动基础信息
			mapMsg.put("message", "1");
		  }else{
			  mapMsg.put("message", "1"); //活动基本信息没有发生变化
		  }
		
		return mapMsg;

	}

	@ResponseBody
	@RequestMapping("/deleteMonitorInfo.do")
	public void deleteMonitorInfo(@RequestParam(value = "delete_id", required = true) Integer delete_id) {
	}

	/***
	 * 添加活动Todo
	 * 
	 * @param bean
	 *            备注基本信息 说明： 1）更改活动状态和上线核查必传参数包括：活动编号、 当前状态、 下一个状态、 任务类别(0活动任务，1
	 *            点位任务)、mic(上线核查需要传) 2）备注包括：活动编号、执行人员、备注内容、todo类型、活动状态
	 *            3）通过todo类型，(0和1)处理是否需要先查询任务列表
	 * @author lily
	 * @return
	 */
	@RequestMapping(value = "/addTodoTask.do")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@ResponseBody
	public Map<String, Object> addTodoTask(@ModelAttribute TodoInfoBean bean) {

		logUtil.logDebugSt();
		Map<String, Object> map = new HashMap<>();
		try {
			String flag = monitorPlanService.addTodoTask(bean);
			map.put("status", 0);
			map.put("message", flag);

		} catch (Exception e) {
			// logUtil.logErrorExc(e);
			e.getStackTrace();
		}
		logUtil.logDebugEd();
		return map;
	}

	/***
	 * 查询活动Todo
	 * 
	 * @param _actCode
	 *            活动编号
	 * @param _sdate
	 *            开始时间
	 * @param _edate
	 *            结束时间
	 * @param _type
	 *            Todo类型
	 * @author lily
	 * @return
	 */
	@RequestMapping(value = "/queryTodoInfo.do")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@ResponseBody
	public ArrayList<TodoInfoBean> queryTodoInfo(@RequestParam(value = "actCode") String _actCode,
			@RequestParam(value = "sdate") String _sdate, @RequestParam(value = "edate") String _edate,
			@RequestParam(value = "type") String _type, @RequestParam(value = "userId") String _userId,
			HttpServletRequest request) {

		logUtil.logDebugSt();
		ArrayList<TodoInfoBean> list = null;
		Map<String, Object> map = new HashMap<>();
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		if (user != null) {
			_userId = user.getUSER_ID().toString();
			try {
				map.put("actCode", _actCode);
				map.put("sdate", _sdate);
				map.put("edate", _edate);
				map.put("_type", _type);
				map.put("userId", _userId);
				list = monitorPlanService.queryTodoInfo(map);
			} catch (Exception e) {
				logUtil.logErrorExc(e);
			}
			logUtil.logDebugEd();
		}

		return list;
	}

	/***
	 * 根据用户编号查询有关用户的活动
	 * 
	 * @param _userId
	 *            用户编号
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryActivityByUser.do")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@ResponseBody
	public ArrayList<TbAmpBasicActivityInfo> queryActivityByUser(@RequestParam(value = "userId") String _userId,
			HttpServletRequest request) {

		logUtil.logDebugSt();
		ArrayList<TbAmpBasicActivityInfo> list = null;
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		if (user != null) {
			_userId = user.getUSER_ID().toString();
			try {
				list = monitorPlanService.queryActivityByUser(_userId);
			} catch (Exception e) {
				logUtil.logErrorExc(e);
			}
			logUtil.logDebugEd();
		}

		return list;
	}

	/***
	 * 更新活动Todo状态
	 * 
	 * @param _todoId
	 *            todo编号
	 * @author lily
	 * @return
	 */
	@RequestMapping(value = "/updateTodoState.do")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@ResponseBody
	public Map<String, Object> updateTodoState(@RequestParam(value = "todoId") String _todoId) {

		logUtil.logDebugSt();
		Map<String, Object> map = new HashMap<>();
		try {
			String flag = monitorPlanService.updateTodoState(_todoId);
			map.put("status", 0);
			map.put("message", flag);

		} catch (Exception e) {
			logUtil.logErrorExc(e);
		}
		logUtil.logDebugEd();
		return map;
	}

	
	
	/**
	 * 2017年07月05日16:29:53
	 * 获取的是监测方案下面的信息
	 * @param actCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/planBean.do")
	public List<AddMonitorPlanBean> planBean(String actCode){
		List<AddMonitorPlanBean> bean = monitorPlanService.planBean(actCode);
		return bean;
	}
}
