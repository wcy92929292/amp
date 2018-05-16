package com.udbac.controller;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.udbac.util.DateUtil;
import com.udbac.entity.TbAmpBasicActivityInfo;
import com.udbac.model.UserBean;
import com.udbac.service.MonitorPlanService;
import com.udbac.util.FilePathManager;
import com.udbac.util.LogUtil;

/**
 * 监测方案
 * @author LFQ
 * @date 2016/04/05
 */
@Controller
@RequestMapping("/monitorPlan")
public class MonitorPlanController {
	
	@Autowired
	private MonitorPlanService monitorPlanService;
	
	@Autowired
	private FilePathManager filePathManager;
	
	private LogUtil logUtil = new LogUtil(AddMonitorPlanController.class);
	
	/**
	 *  查询监测方案
	 * @param 
	 * 		startDateStr	添加活动时间
	 * 		endDateStr		添加活动时间
	 * 		activityCode 	活动编号
	 * @return	
	 * @author LFQ
	 * @date 2016-04-05
	 */
	@ResponseBody
	@RequestMapping(value = "/list.do", method = RequestMethod.POST)
	public List<Object> list(
				@RequestParam(value = "startDateStr") String startDateStr,
				@RequestParam(value = "endDateStr") String endDateStr,
				@RequestParam(value = "portUser",required = false) String portUser,
				@RequestParam(value = "activityCode",required = false) String activityCode,
				HttpServletRequest request,
				Map<String,Object> map
	){
		
		//将查询结果转成JSON数组 
		List<Object> list = new LinkedList<>();
		
		//判断是否已经登录
		UserBean user = (UserBean)request.getSession().getAttribute("user");
		
		if(user == null){
			map.put("error", "401");
			list.add(map);
			return list;
		}
		
		try{
			//判断时间格式是否正确   YYYY-MM-dd,并且加入配置
			Date startDate = DateUtil.getDate(startDateStr, null);
			Date endDate = DateUtil.getDate(endDateStr, null);
			
			if(startDate == null || endDate == null){
				map.put("error", "开始时间或者结束时间格式不正确！");
			}else{
				
				endDate.setTime(endDate.getTime() + 86400000);
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				System.out.println(portUser);
				if(portUser != null && portUser != ""){
					map.put("portUser", portUser.trim());
				}
				if(activityCode != null && activityCode != ""){
				    map.put("activityCode", activityCode.trim());
				}
				//根据查询条件查询结果
				List<TbAmpBasicActivityInfo> actList = monitorPlanService.list(map);
				
				map.clear();
				if(actList == null){
					map.put("error", "500");
				}else{
					list.add(actList);
				}
			}//end if else
			
		}catch(Exception e){
			e.printStackTrace();
			logUtil.logErrorExc(e);
			
			map.clear();
			map.put("error", "500");
		}finally{
			
			map.put("role", user.getROLE_NAME());
			map.put("userID", user.getUSER_ID());
			list.add(map);
		}
		
		
		return list;
	}//end /list.do
	
	
	/**
	 *  提交上线状态
	 * @param
	 * 		stateFile	强制上线的图片证据
	 * 		actCode		目标活动编号
	 * 		actState	当前的活动状态
	 * @return
	 * 
	 * @author LFQ
	 * @throws IOException 
	 * @date 2016-04-14
	 */
	@ResponseBody
	@RequestMapping(value = "/updateState.do", method = RequestMethod.POST)
	public void updateState(
				@RequestParam(value="stateFile",required=false) MultipartFile file,
				@RequestParam("actCode") String actCode,
				@RequestParam("actState") String actState,
				@RequestParam("cusCode") String cusCode,
				@RequestParam("updateState") Integer updateState,
				HttpServletRequest request,
				HttpServletResponse response,
				Map<String,Object> map
	) throws IOException{
		actCode = actCode.trim();
		actState = actState.trim();
		cusCode = cusCode.trim();
		int i = 0;
		
		List<String> onlineStateFileType = filePathManager.getOnlineStateFileType();
		
		//判断是否已经登录
		UserBean user = (UserBean)request.getSession().getAttribute("user");
				
		if(user == null){
			map.put("error", "401");
		}
		//只有接口人有操作权限
		else if(!user.getROLE_NAME().contains("接口人")){
				map.put("error", "没有权限");
		}
		//不具备上线条件,则需要保存强制上线的证据
		else if("2".equals(actState) && ("".equals(file) || "null".equals(file))){
				map.put("error", "强制上线需要上传证据!");
		}
		//不具备上线条件，上传的文件后缀名为  .png  .gif  .jpeg  .jpg
		else if("2".equals(actState)){
			String fileName = file.getOriginalFilename();
			for (;i < onlineStateFileType.size(); i++) {
				if(fileName.endsWith(onlineStateFileType.get(i))){
					break;
				}
			}
		}
		
		//所有条件都满足，进行状态更改
		if(i<onlineStateFileType.size()){
			String message = monitorPlanService.updateState(file, actCode, actState,cusCode,updateState);
			
			logUtil.logInfo("OLM-IA99999",new String[]{user.getREAL_NAME(),actCode,actState});
			map.put("message", message);
		}else{
			map.put("error", "文件格式不正确！"+onlineStateFileType);
		}
		
		//通知页面更改状态  1:成功    0:失败
		response.getWriter().write("<script>parent.window.onlineState('"+map+"');</script>");
		
	}//end updateState

	@RequestMapping("findByCode.do")
	@ResponseBody
	public TbAmpBasicActivityInfo findByCode(@RequestParam("actCode") String actCode){
		TbAmpBasicActivityInfo activityInfo = monitorPlanService.findByCode(actCode);
		return activityInfo;
	}
	
}
