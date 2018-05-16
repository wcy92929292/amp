package com.udbac.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.dao.MonitorPlanDao;
import com.udbac.entity.AddMonitorPlanBean;
import com.udbac.entity.AddMonitorSchemeInfo;
import com.udbac.model.UserBean;
import com.udbac.service.MonitorPlanService;
import com.udbac.service.user.UserService;
import com.udbac.util.LogUtil;
import com.udbac.util.MailInitBean;
import com.udbac.util.mail;

/**
 * @author lp 审查监测方案
 * @return list
 * @param 活动编号
 * 
 */
@Controller
@RequestMapping("/auditMonitor")
public class AuditMonitorPlanController {

	@Autowired
	private  MonitorPlanDao monitorDao;
	
	@Autowired
	private UserService userService;
	@Autowired
	private MonitorPlanService monitorPlanService;
	private LogUtil logUtil = new LogUtil(AuditMonitorPlanController.class);


	@RequestMapping(value = "/queryMonitor.do", method = RequestMethod.POST)
	public @ResponseBody List<AddMonitorSchemeInfo> getListSite(@RequestParam(value = "actCode") String actCode
			) throws UnsupportedEncodingException {
		List<AddMonitorSchemeInfo> list = null;
		try {
			list = monitorDao.queryMonitorByActivityCode(actCode);
		} catch (Exception e) {
			logUtil.logErrorExc(e);
		}
		return list;
	}

/****
 * @author Lp 2016-05-13 
 * @param actCode
 * @param id
 * @param pageName
 * @param buttonName
 * @return
 * @throws IOException 
 * @throws JsonMappingException 
 * @throws JsonParseException 
 */
	@RequestMapping(value = "/updateMonitor.do", method = RequestMethod.POST)
	public @ResponseBody String UpdateMonitor(@RequestParam(value = "actCode") String actCode,
//			@RequestParam(value = "dcsid_s",required = false) String dcsid_s,
//			@RequestParam(value = "btn_id",required = false) String btn_id,
//			@RequestParam(value = "page_url",required = false) String page_url,
//			@RequestParam(value = "match_url",required = false) String match_url,
//			@RequestParam(value = "key",required = false) String key,
//			@RequestParam(value = "value",required = false) String value,
//			@RequestParam(value = "op",required = false) String op,
			@RequestParam(value = "delId", required = false) String delId,
			@RequestParam(value = "scheme_id",required = false) String scheme_id,
			@RequestParam(value="param",required = false) String param,
			@RequestParam(value = "ACTIVITY_STATE") String actState,
			HttpServletRequest request, HttpServletResponse response
			) throws JsonParseException, JsonMappingException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> mapp = new HashMap<String, Object>();
		List addlist = new ArrayList<>();
		ObjectMapper om = new ObjectMapper();
		UserBean user = (UserBean) request.getSession().getAttribute("user");
		UserBean userByCode = userService.getUserByCode(actCode);
		UserBean userById = userService.getUserById(userByCode.getUSER_ID()+"");
		AddMonitorPlanBean monitorInfo = monitorPlanService.getMonitorInfoByCode(actCode);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date =df.format(new Date());
		
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
		
		List<AddMonitorSchemeInfo> lst = om.readValue(param, new TypeReference<List<AddMonitorSchemeInfo>>() {
		});
		try {
			for (AddMonitorSchemeInfo info : lst) {
				map.put("dcsid_s", info.getDcsid_s());
				map.put("btn_id", info.getBtn_id());
				map.put("page_url", info.getPage_url());
				map.put("match_url", info.getMatch_url());
				map.put("key", info.getKey());
				map.put("value", info.getValue());
				map.put("op", info.getOp());
				map.put("scheme_id", String.valueOf(info.getScheme_id()));
				map.put("actCode", actCode);
				map.put("userId", user.getUSER_ID()+"");
				map.put("actState", actState);
				map.put("date", date);
				
					if(info.getScheme_id() == 0){ //新增操作
						addlist.add(info);
					}else{
						monitorDao.updateMonitorPageUrlAndButtonId(map);
						monitorDao.updateActState(map);
					}
			}
			
			mapp.put("acid",actCode);
			mapp.put("userID", user.getUSER_ID()+"");
			mapp.put("list", addlist);
			//执行新增操作
			monitorPlanService.addMonitorSchemeInfo(mapp);
			
			if("1".equals(actState)){
				mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, userById.getMAILBOX(), "", actCode+" "+monitorInfo.getActivity_name()+" 已审核通过！！", 
						"<br>活动方案已审核通过，请继续下一步骤。"+
						"<br><br><br>提示：此信为系统自动发送邮件，请勿直接回复。"+
						"<br>可联系["+ user.getREAL_NAME()+ "] "+ 
						"Email:<a href='mailto:"+user.getMAILBOX()+"'</a>"+ user.getMAILBOX()
						, MailInitBean.username, MailInitBean.password);
				}else if("2".equals(actState)){
					mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, userById.getMAILBOX(), "", actCode+" "+monitorInfo.getActivity_name()+" 未审核通过！！", 
							"<br>活动方案未审核通过，请修改重试。"+
							"<br><br><br>提示：此信为系统自动发送邮件，请勿直接回复。"+
							"<br>可联系["+ user.getREAL_NAME()+ "] "+ 
							"Email:<a href='mailto:"+user.getMAILBOX()+"'</a>"+ user.getMAILBOX()
							, MailInitBean.username, MailInitBean.password);
				}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		
		return "success";
		
	}
	
}
