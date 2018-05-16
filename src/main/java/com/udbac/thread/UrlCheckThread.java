package com.udbac.thread;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;

import com.udbac.dao.MonitorPlanDao;
import com.udbac.dao.UpdateUrlInfoDao;
import com.udbac.model.UrlUpateCheckModel;
import com.udbac.service.user.UserService;
import com.udbac.util.DateUtil;
import com.udbac.util.MailInitBean;
import com.udbac.util.StringBufferUtil;
import com.udbac.util.mail;


public class UrlCheckThread {

	@Autowired
	private UpdateUrlInfoDao updateUrlInfoDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MonitorPlanDao monitorPlanDao;
	
	public UrlCheckThread(){
		new Timer().schedule(new CommonTask<UrlCheckThread>(this, "check", null, null), 
				10000, 3600000);
	}
	
	public void check() {
		//查找未来五分钟生效的点位。
		List<UrlUpateCheckModel> checkUpdate = updateUrlInfoDao.checkUpdate();
		System.out.println("check.................");
		//对未生效的点位按照活动进行分类
		Map<String,List<UrlUpateCheckModel>> checkMap = new HashMap<>();
		UrlUpateCheckModel checkMolde = null;
		List<UrlUpateCheckModel> list = null;
		String key = "";
		StringBuffer sb = new StringBuffer(16);
		for (int i = 0; i < checkUpdate.size(); i++) {
			checkMolde = checkUpdate.get(i);
			key = StringBufferUtil.append(sb, 
						checkMolde.getActivity_code(),"_",checkMolde.getActivity_name(),"_","URL变更提前生效"
					).toString();
			
			list = checkMap.get(key);
			//新的活动
			if(list == null){
				list = new LinkedList<>();
				checkMap.put(key, list);
			}
			list.add(checkMolde);
			StringBufferUtil.clear(sb);
		}//end for (int i = 0; i < checkUpdate.size(); i++) {
		
		sendCheckEmail(checkMap);
	}
	
	
	/**
	 * 告警发送邮件
	 * 	告警内容
		活动编号；变更的点位；变更前链接；变更后链接；计划生效时间；实际生效时间；接口人；后端支撑
		告警邮件发你、我、张总、接口人、后端支撑
		邮件标题：活动编号_活动名称_更改批次_url变更提前生效
	 * @param urlInfo
	 * @throws Exception 
	 */
	public void sendCheckEmail(Map<String,List<UrlUpateCheckModel>> checkMap){
		
		Set<String> keySet = checkMap.keySet();
		List<UrlUpateCheckModel> list;
		UrlUpateCheckModel checkModel = null;
		Iterator<UrlUpateCheckModel> iterator;
		StringBuffer message = new StringBuffer(200);
		
		for (String key : keySet) {
			list = checkMap.get(key);
			iterator = list.iterator();
			StringBufferUtil.append(message,"<table><tr><td>活动编号</td><td>更改批次</td><td>变更的点位</td><td>变更前链接</td><td>变更后链接</td><td>计划生效时间</td><td>实际生效时间</td><td>接口人</td><td>后端支撑</td></tr>");
			while (iterator.hasNext()) {
				checkModel = iterator.next();
				
				StringBufferUtil.append(message,
						"<tr><td>",checkModel.getActivity_code(),"</td>",
						"<td>",checkModel.getUpdate_batch(),"</td>",
						"<td>",checkModel.getMic(),"</td>",
						"<td>",checkModel.getOld_url(),"</td>",
						"<td>",checkModel.getUrl_pc(),"</td>",
						"<td>",DateUtil.getDateStr(checkModel.getUrl_update_time(),"yyyyMMdd HH:mm:ss"),"</td>",
						"<td>",DateUtil.getDateStr(checkModel.getUpdate_time(),"yyyyMMdd HH:mm:ss"),"</td>",
						"<td>",checkModel.getUser_name(),"</td>",
						"<td>",checkModel.getCheck_user_name(),"</td></tr>"
					);
//				活动编号;更改批次；变更的点位；变更前链接；变更后链接；计划生效时间；实际生效时间；接口人；后端支撑
			}
			
			StringBufferUtil.append(message,"</table><style>td{border:1px solid black;padding:3px;}</style>");
			//发送邮件
			mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, 
					checkModel.getMailbox() + "," + checkModel.getCheck_mailbox(),  //发送 接口人，WT人员
					"yan.li@udbac.com,fuqin.li@udbac.com,haizhou.zhang@udbac.com",  //抄送
					key,message.toString(), MailInitBean.username, MailInitBean.password);
			
			StringBufferUtil.clear(message);
			try {
				//多个邮件缓冲时间。
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}//end for (String key : keySet) {
		
		System.gc();
	}
}
