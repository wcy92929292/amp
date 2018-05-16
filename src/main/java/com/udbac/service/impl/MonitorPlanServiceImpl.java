package com.udbac.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.udbac.dao.MonitorPlanDao;
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
import com.udbac.service.MonitorPlanService;
import com.udbac.util.DateUtil;
import com.udbac.util.FilePathManager;
import com.udbac.util.LogUtil;

/**
 * 监测方案ServiceImpl
 * 
 * @author LFQ
 *
 */
@Service
public class MonitorPlanServiceImpl implements MonitorPlanService {

	@Autowired
	private MonitorPlanDao monitorPlanDao;

	@Autowired
	private FilePathManager filePathManager;
	// 返回信息
	String message = null;

	private LogUtil logUtil = new LogUtil(MonitorPlanServiceImpl.class);

	/**
	 * 监测方案一览
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TbAmpBasicActivityInfo> list(Map<String, Object> params) {
		List<TbAmpBasicActivityInfo> list = null;
		try {
			System.out.println("qqqqq"+params);
			list = monitorPlanDao.list(params);
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
			logUtil.logErrorExc(e);
		}
		return list;
	}// end list()

	/**
	 * 提交上线状态
	 * 
	 * @param file
	 *            强制上线时,作为证据
	 * @param actCode
	 *            目标活动编号
	 * @param actState
	 *            当前活动状态
	 * @return
	 */
	@Override
	@Transactional
	public String updateState(MultipartFile file, String actCode, String actState, String cusCode,Integer updateState) {

		int state = 0; // 状态
		int updateLine = 0; // 更改活动数
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		
		try {
			state = Integer.parseInt(actState);

			TbAmpBasicActivityInfo actInfo = new TbAmpBasicActivityInfo();
			actInfo.setActivityCode(actCode);
			TodoInfoBean todoInfoBean = new TodoInfoBean();
			//活动编号、 当前状态、 下一个状态、 任务类别(0活动任务
			todoInfoBean.setActivityCode(actCode);
			todoInfoBean.setNowState(state);
			todoInfoBean.setOfTaskType("0");
			// 正常上线
			if (state == 1) {
				actInfo.setGoLiveType(0);
				state = 3;
				todoInfoBean.setNextState(3);
			}
			// 强制上线
			else if (state == 2) {
				actInfo.setGoLiveType(1);
				state = 3;
				todoInfoBean.setNextState(3);
				String originalFilename = file.getOriginalFilename();

				// 保存强制上线图片
				StringBuffer sb = new StringBuffer(80);
				sb.append(filePathManager.getActivityOnlineFile());
				sb.append(cusCode);

				// 判断客户文件夹是否存在
				File f = new File(sb.toString());
				if (!f.exists()) {
					f.mkdirs();
				}

				// 拼接文件名
				sb.append("/");
				sb.append(actCode);
				sb.append("_");
				sb.append(DateUtil.getDateStr(new Date(), "YYYYMMddhhmmsssss")); //
				sb.append(originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length()));

				String fileName = sb.toString();
				bos = new BufferedOutputStream(new FileOutputStream(fileName));
				bis = new BufferedInputStream(file.getInputStream());

				byte[] bs = new byte[256];
				int len = 0;
				while ((len = bis.read(bs)) != -1) {
					bos.write(bs, 0, len);
				}

				bos.flush();
			}
			// 其它上线状态
			else if (3 <= state && state <= 5) {
				state++;
			}
			//已经结束或者  actStateArr[6] = "已上线";
		    //actStateArr[7] = "已结束";
		    //actStateArr[8] = "准备结案基础数据";
			else if( 6 == state){
				state = 8;
			}else if(8 == state){
				if(updateState == 5){
					state = 7;
				}else if(updateState == 4){
					state = 6;
				}else {
					return "0";
				}
			}
			else {
				return "0";
			}
			
			actInfo.setActivityState(state);
			//todoInfoBean.setNextState(state);
			
			// 更改活动数据信息
			monitorPlanDao.updateState(actInfo);
			monitorPlanDao.updateSumState(actCode);
//			String addTodoTask = addTodoTask(todoInfoBean);
//			System.out.println(addTodoTask+"==========");
		} catch (Exception e) {
			e.printStackTrace();
			logUtil.logErrorExc(e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					logUtil.logErrorExc(e1);
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e1) {
					logUtil.logErrorExc(e1);
				}
			}
		} // end try - catch
		return String.valueOf(updateLine);
	}// end updateState()

	@Override
	public void addActivityinfo(AddMonitorPlanBean bean) {
		monitorPlanDao.addActivityinfo(bean);

	}

	@Override
	public void addMonitorSchemeInfo(Map<String, Object> map) {
		monitorPlanDao.addMonitorSchemeInfo(map);
	}

	@Override
	public void updateMonitorInfo(Map<String, Object> map) {
		monitorPlanDao.updateMonitorInfo(map);
	}

	@Override
	public void delMonitorInfo(List list) {
		// TODO Auto-generated method stub
		monitorPlanDao.delMonitorInfo(list);
	}
	
	@Override
	public List<AddMonitorPlanBean> showMonitorPlan(String actCode) {
		return monitorPlanDao.showMonitorPlan(actCode);
	}
	@Override
	public List<AddMonitorPlanBean> monitorPlanDetail(String actCode) {
		return monitorPlanDao.monitorPlanDetail(actCode);
	}

	@Override
	public List<User> getUser() {
		return monitorPlanDao.getUser();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List queryAll(String actCode, QueryActivityInfo qat) {
		List list = monitorPlanDao.queryAll(actCode, qat);
		return list;
	}

	@Override
	public List<QueryCustomerInfo> queryCustomer() {

		return monitorPlanDao.queryCustomer();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List queryActAndCus(String actCode) {
		return monitorPlanDao.queryActAndCus(actCode);
	}

	@Override
	public List queryMonitor(String actCode) {
		return monitorPlanDao.querymonitor(actCode);
	}

	@Override
	public List queryFontSupport(String actCode) {
		// TODO Auto-generated method stub
		return monitorPlanDao.queryFontSupport(actCode);
	}

	@Override
	public List queryAfterSupport(String actCode) {
		// TODO Auto-generated method stub
		return monitorPlanDao.queryAfterSupport(actCode);
	}

	@Override
	public void updateActMonitorInfo(String activity_code, AddMonitorPlanBean bean,Integer updateUser) {
		monitorPlanDao.updateActMonitorInfo(activity_code, bean,updateUser);
	}

	@Override
	public Integer queryMonitorCode(String acode) {

		return monitorPlanDao.queryMonitorCode(acode);
	}

	@Override
	public String saveActivity(String param, AddMonitorPlanBean bean) {
		try {
			ObjectMapper om = new ObjectMapper();

			// 完整活动编号
			String acode = bean.getS_code().trim() + bean.getActivity_code().trim();

			Integer flag = monitorPlanDao.queryMonitorCode(acode);

			// 活动编号不存在添加基本信息和活动方案
			if (flag == 0) {
				// 先添加活动基本信息，成功后在添加活动监测方案信息，如果监测方案信息添加失败，对添加活动基本信息操作进行事物回滚
				bean.setActivity_code(acode);
				monitorPlanDao.addActivityinfo(bean);
				message = "1";
				logUtil.logInfoCon("活动基本信息为：" + bean.toString());
				if(param !=null){
					System.out.println(param);
					List<AddMonitorSchemeInfo> lst = om.readValue(param, new TypeReference<List<AddMonitorSchemeInfo>>() {
					});
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("acid", bean.getActivity_code());
					map.put("list", lst);
						monitorPlanDao.addMonitorSchemeInfo(map);
						message = "1";
					logUtil.logInfoCon("活动监测方案信息添加成功" + lst.toString());
				}
			} else {
				message = "活动编号已存在，请重新输入";
			}

		} catch (Exception e) {
			e.printStackTrace();
			logUtil.logErrorExc(e);
			message = "添加失败，请重试";
		}
		System.out.println(message);
		return message;
	}

	@Override
	public String addTodoTask(TodoInfoBean bean) {
		try {
			if (bean != null) {

				// 判断todo类型是0(活动任务)和1(点位任务)的，先设置默认的todo状态为1(待完成)
				// 再查询任务列表，插入todo
				String type = bean.getTodoType();
				if ("0".equals(type) || "1".equals(type)) {
					bean.setTodoState("1");
					ArrayList<TodoInfoBean> list = monitorPlanDao.queryTaskList(bean);
					System.out.println(list.size());
					for (TodoInfoBean tBean : list) {
						tBean.setMic(bean.getMic());
						/** 遍历插入todo **/
						monitorPlanDao.addTodoTask(tBean);
					}
				} else {
					// todo类型是2（普通任务）设置默认的todo状态为2（无状态），其他为1（待完成）
					if ("2".equals(type)) {
						bean.setTodoState("2");
					} else if ("3".equals(type)) {
						bean.setTodoState("1");
						// 更新问题数
						monitorPlanDao.updateActivityException(bean.getActivityCode());
					}
					/** 插入todo **/
					monitorPlanDao.addTodoTask(bean);
				}
				logUtil.logInfoCon(bean.getActivityCode() + "活动备注添加成功");
				message = "1";
			}
		} catch (Exception e) {
//			logUtil.logErrorExc(e);
//			message = "活动备注添加失败，请重试";
			e.getStackTrace();
		}

		return message;
	}

	@Override
	public ArrayList<TodoInfoBean> queryTodoInfo(Map<String, Object> map) {
		ArrayList<TodoInfoBean> list = monitorPlanDao.queryTodoInfo(map);
		return list;
	}

	@Override
	public List<QueryCustomerInfo> queryActNameAndCus(String actCode) {
		// TODO Auto-generated method stub
		return monitorPlanDao.queryActNameAndCus(actCode);
	}

	@Override
	public ArrayList<TbAmpBasicActivityInfo> queryActivityByUser(String userId) {
		// TODO Auto-generated method stub
		return monitorPlanDao.queryActivityByUser(userId);
	}

	@Override
	public String updateTodoState(String _todoId) {

		try {
			monitorPlanDao.updateTodoState(_todoId);
			message = "1";
		} catch (Exception e) {
			logUtil.logErrorExc(e);
			message = "Todo状态更新失败，请重试";
		}
		return message;
	}

	/**
	 * 判断活动的上线状态
	 */
	@Override
	public List<AddMonitorPlanBean> getActivityState(String actCode) {
		// TODO Auto-generated method stub
		List<AddMonitorPlanBean> list = monitorPlanDao.getActivityState(actCode);
		return list;
	}

	@Override
	public TbAmpBasicActivityInfo findByCode(String actCode) {
		return monitorPlanDao.findByCode(actCode);
	}
	@Override
	public AddMonitorPlanBean getMonitorInfoByCode(String actCode){
		return monitorPlanDao.getMonitorInfoByCode(actCode);
	}

	@Override
	public List<AddMonitorPlanBean> planBean(String actCode) {
		return monitorPlanDao.planBean(actCode);
	}

	@Override
	public AddMonitorSchemeInfo getOrignInfo(String actCode, Integer schemeId) {
		return monitorPlanDao.getOrignInfo(actCode, schemeId);
	}

}
