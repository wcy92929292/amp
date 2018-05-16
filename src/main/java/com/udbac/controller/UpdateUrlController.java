package com.udbac.controller;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.entity.TbAmpUpdateUrlInfo;
import com.udbac.model.UserBean;
import com.udbac.service.UpdateUrlService;
import com.udbac.service.user.UserService;
import com.udbac.util.MailInitBean;
import com.udbac.util.mail;

/**
 * URL变更以及审核Controller
 * @author LFQ
 *
 */
@Controller
@RequestMapping("/updateUrl")
public class UpdateUrlController {

	@Autowired
	private UpdateUrlService updateUrlService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 新增URL变更信息
	 */
	@RequestMapping(value="/newUrl.do",method={RequestMethod.POST})
	@ResponseBody
	public String newUrl(@RequestParam("actCode")String actCode,@RequestParam("urlUpdateTime")String urlUpdateTime,
			@RequestParam("newUrl")String newUrl,@RequestParam("checkUserID")String checkUserID,
			@RequestParam("mics")String mics,@RequestParam("memo")String memo,
			@RequestParam("butInfo")String butInfo,@RequestParam("actName")String actName,
			HttpServletRequest request){
		
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");
		UserBean user = null;
		if(obj instanceof UserBean){
			user = (UserBean)obj;
		}
		String message = "";
		UserBean mailbox = userService.getUserById(checkUserID);
		try{
			message = updateUrlService.newUrl(actCode, urlUpdateTime, newUrl, checkUserID, mics, memo, butInfo, String.valueOf(user.getUSER_ID()));
			message =URLEncoder.encode(message,"UTF-8");
			mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, mailbox.getMAILBOX(), "", "营销活动"+actCode+" "+actName+"Url修改需要后端审核！！", 
					
					"<br>Url已修改，请审核。（注意填写按钮id）"+
					"<br><br><br>提示：此信为系统自动发送邮件，请勿直接回复。"+
					"<br>可联系["+ user.getREAL_NAME()+ "] "+ 
					"Email:<a href='mailto:"+user.getMAILBOX()+"'</a>"+ user.getMAILBOX()
					, MailInitBean.username, MailInitBean.password);
			
		}catch(Exception e){
			e.printStackTrace();
			return "500";
		}
		return message;
	}//end newUrl
	
	/**
	 * 根据活动编号查找URL变更信息
	 * @return
	 */
	@RequestMapping(value="/findUrlByActCode.do",method={RequestMethod.POST})
	@ResponseBody
	public List<TbAmpUpdateUrlInfo> findUrlByActCode(@RequestParam("actCode")String actCode,
			@RequestParam("operation")String operation,HttpServletRequest request){
		List<TbAmpUpdateUrlInfo> urlByActCode = null;
		
//		Object user = request.getSession().getAttribute("user");
//		System.out.println(user);
		
		try{
			urlByActCode = updateUrlService.findUrlByActCode(actCode);
		}catch(Exception e){
			e.printStackTrace();
			return urlByActCode;
		}
		System.out.println(urlByActCode);
		return urlByActCode;
	}//end findUrlByActCode
	
//	/**
//	 * 根据修改批次获取URL变更信息
//	 * @return
//	 */
//	@RequestMapping(value="findUrlByUpdateBatch.do",method={RequestMethod.POST})
//	@ResponseBody
//	public String findUrlByUpdateBatch(@RequestParam("updateBatch")String updateBatch){
//		
//		
//		
//		return "1";
//	}//end findUrlByUpdateBatch
	
	/**
	 * 后端支撑人员审核
	 * @return
	 */
	@RequestMapping(value="/checkUrl.do",method={RequestMethod.POST})
	@ResponseBody
	public String checkUrl(@RequestParam("butInfo")String buttonInfo,
			@RequestParam("updateBatch")String updateBatch,
			@RequestParam("checkState")String checkState,
			@RequestParam("actCode")String actCode,
			@RequestParam("actName")String actName,HttpServletRequest request,
			@RequestParam("createUser")String createUser){
		
//		System.out.println(buttonInfo);
//		System.out.println(updateBatch);
//		System.out.println(checkState);
		
		updateUrlService.checkUrl(buttonInfo, updateBatch, checkState);
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");
		UserBean user = null;
		if(obj instanceof UserBean){
			user = (UserBean)obj;
		}
		
		UserBean users = userService.getUserByName(createUser);
		if("2".equals(checkState)){
		mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, users.getMAILBOX(), "yin.bai@udbac.com,xiaoyun.deng@udbac.com", "营销活动"+actCode+" "+actName+"Url修改已审核通过！！", 
				
				"Url修改已审核通过，请继续下面的步骤。"+
				"<br><br><br>提示：此信为系统自动发送邮件，请勿直接回复。"+
				"<br>可联系["+ user.getREAL_NAME()+ "] "+ 
				"Email:<a href='mailto:"+user.getMAILBOX()+"'</a>"+ user.getMAILBOX()
				, MailInitBean.username, MailInitBean.password);
		}else if("3".equals(checkState)){
			mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, users.getMAILBOX(), "", "营销活动"+actCode+" "+actName+"Url修改未审核通过！！", 
					
					"Url修改未审核通过，请重新修改。"+
					"<br><br><br>提示：此信为系统自动发送邮件，请勿直接回复。"+
					"<br>可联系["+ user.getREAL_NAME()+ "] "+ 
					"Email:<a href='mailto:"+user.getMAILBOX()+"'</a>"+ user.getMAILBOX()
					, MailInitBean.username, MailInitBean.password);
		}
		return "1";
	}//end checkUrl
	
	
	/**
	 * 后端支撑人员审核
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/update.do")
	public String updateTime(
				@RequestParam("updateBatch")String updateBatch,
				@RequestParam("urlUpdateTime")String urlUpdateTime,
				@RequestParam("createUser")String createUser,
				HttpServletRequest request
			){
		
		updateUrlService.updateTime(updateBatch, urlUpdateTime);
		
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");
		UserBean user = null;
		if(obj instanceof UserBean){
			user = (UserBean)obj;
		}
		
		UserBean users = userService.getUserByName(createUser);
		mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, users.getMAILBOX(), "", "Url生效时间已修改！！", 
				
				updateBatch + " Url生效时间有变更，生效时间" + urlUpdateTime + 
				"<br><br><br>提示：此信为系统自动发送邮件，请勿直接回复。"+
				"<br>可联系["+ user.getREAL_NAME()+ "] "+ 
				"Email:<a href='mailto:"+user.getMAILBOX()+"'</a>"+ user.getMAILBOX()
				, MailInitBean.username, MailInitBean.password);
			
		return "1";
	}//end checkUrl
	
}
