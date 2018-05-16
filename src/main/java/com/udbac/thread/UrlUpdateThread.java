package com.udbac.thread;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.udbac.dao.MonitorPlanDao;
import com.udbac.dao.UpdateUrlInfoDao;
import com.udbac.entity.TbAmpBasicActivityInfo;
import com.udbac.entity.TbAmpUpdatUrlAndSchedule;
import com.udbac.entity.TbAmpUpdateUrlInfo;
import com.udbac.model.UserBean;
import com.udbac.service.user.UserService;
import com.udbac.util.DateUtil;
import com.udbac.util.MailInitBean;
import com.udbac.util.MonitoringCodeTool;
import com.udbac.util.XlsxUtil;
import com.udbac.util.mail;

/**
 * URL变更定时任务
 * @author LFQ
 */
public class UrlUpdateThread {
	
	@Autowired
	private UpdateUrlInfoDao updateUrlInfoDao;
//	private static UpdateUrlTask urlTask = null;
	@Autowired
	private UserService userService;
	
	@Autowired
	private MonitorPlanDao monitorPlanDao;
	
	public UrlUpdateThread(){
//		urlTask = new UpdateUrlTask(this);
		//启动一分钟之后执行
//		new Timer().schedule(urlTask, 600000);
		
		//每隔一小时，整点前5分钟，更新审核通过的URL变更
//		long time = 3600000 - (new Date().getTime() % 3600000);
//		time = time > 300000 ? time - 300000 : time;
//		new Timer().schedule(urlTask, time , 3600000);
		
		//每五分钟巡检一次。
//		new Timer().schedule(urlTask, 60000 , 300000);
		new Timer().schedule(new CommonTask<UrlUpdateThread>(this,"init",null,null)
				, 60000 , 300000);
	}
	
//	public static  UrlUpdateThread getUpdateUrlTask(){
//		return urlTask;
//	}
	
	//初始化所有定时任务
	@SuppressWarnings("deprecation")
	public void init(){
		System.out.println("init...................");
		//查找审核通过的URL变更信息
		List<TbAmpUpdateUrlInfo> updateInfoList = updateUrlInfoDao.findByUpdateBatchOrState(null,"2");
		
		TbAmpUpdateUrlInfo urlInfo = null;
		Date now = new Date();
		Date updateTime = null;
		long startTime = 0;
		
		for (int i = 0; i < updateInfoList.size(); i++) {
			
			urlInfo = updateInfoList.get(i);
			updateTime = urlInfo.getUrlUpdateTime();
			//提前5分钟，更新审核通过的URL变更
			startTime = updateTime.getTime() - now.getTime() - 300000;
			System.out.println("updateBatch:"+urlInfo.getUpdateBatch());
			System.out.println("startTime:"+startTime);
			//如果生效时间已经过了，则立马更新
			if(startTime <= 0){
				changeUrlAndSendEmail(urlInfo);
			}
			//否则生效时间未过
//			else{
//				//创建定时器任务，并生效
//				new UpdateUrlTimer(urlInfo,startTime);
//			}//end if - else
		}//end for
	}
	
	/**
	 * 最终生效发送邮件
	 * @param urlInfo
	 * @throws Exception 
	 */
	public void sendFinishEmail(TbAmpUpdateUrlInfo urlInfo){
		
		//获取创建人
		UserBean user = userService.getUserById(urlInfo.getCreateUser());
		
		//获取活动信息
		Set<String> mics = new HashSet<>();
		
		Set<TbAmpUpdatUrlAndSchedule> schedules = urlInfo.getMics();
		Iterator<TbAmpUpdatUrlAndSchedule> iterator = schedules.iterator();
		
		while(iterator.hasNext()){
			mics.add(iterator.next().getMic());
		}
		
		TbAmpBasicActivityInfo actInfo = monitorPlanDao.findActByMics(mics).get(0);
		
		//邮件标题
		StringBuffer titleSb = new StringBuffer(10);
		titleSb.append("营销活动");
		titleSb.append(actInfo.getActivityCode());
		titleSb.append(" ");
		titleSb.append(actInfo.getActivityName());
		titleSb.append("将在十分钟内正式生效！");
		
		//邮件内容
		StringBuffer messageSb = new StringBuffer(100);
		messageSb.append("<br>批次：");
		messageSb.append(urlInfo.getUpdateBatch());
		messageSb.append("<br>Url变更已通过，进入同步阶段。将在十分钟内进行最终同步跳转生效！");
		messageSb.append("<br>新链接：");
		messageSb.append(urlInfo.getUrlUpdate());
		messageSb.append("<br><br><br>提示：此信为系统自动发送邮件，请勿直接回复。");
		
		//发送邮件
		mail.sendAndCc(MailInitBean.smtp, MailInitBean.from, user.getMAILBOX(), "yin.bai@udbac.com,xiaoyun.deng@udbac.com",titleSb.toString(), messageSb.toString(), MailInitBean.username, MailInitBean.password);
		System.gc();
		
	}
	
	
	//执行的任务
	@Transactional
	public void changeUrl(TbAmpUpdateUrlInfo urlInfo){
		//更改URL
		updateUrlInfoDao.changeScheUrl(urlInfo);
		//更改生效的状态
		updateUrlInfoDao.changeUpdateState(urlInfo.getUpdateBatch(),"4");
		
		Set<TbAmpUpdatUrlAndSchedule> mics = urlInfo.getMics();
		TbAmpUpdatUrlAndSchedule[] micArr = new TbAmpUpdatUrlAndSchedule[mics.size()];
		micArr = mics.toArray(micArr);
		
		if(micArr.length > 0){
			//获取排期文件
			TbAmpBasicActivityInfo activityInfo = updateUrlInfoDao.findSchedulePathByMic(micArr[0].getMic());
			writeNewUrl(activityInfo, micArr, urlInfo.getUrlUpdate());
		}
		
	}//end changeUrl()
	
	/**
	 * 更改URL并发送邮件
	 * @param urlInfo
	 * @throws Exception 
	 */
	public void changeUrlAndSendEmail(TbAmpUpdateUrlInfo urlInfo) {
		changeUrl(urlInfo);
		//发送邮件
		sendFinishEmail(urlInfo);
	}//end changeUrlAndSendEmail()
	
	/**
	 * 将新URL写回对应排期
	 * @param activityInfo 
	 * @param micArr
	 * @param url
	 */
	public static void writeNewUrl(TbAmpBasicActivityInfo activityInfo,TbAmpUpdatUrlAndSchedule[] micArr,String url){
		
		XlsxUtil xlsxUtil;
		try {
			xlsxUtil = new XlsxUtil(activityInfo.getSchedulePath());
		} catch (Exception e) {
			return;
		}
		
		int startRow,micY,urlY,clickY;
		
		//集团的排期格式
		if("JT".equals(activityInfo.getActivityCode().replaceAll("\\d",""))){
			startRow = 7;
			urlY = XlsxUtil.getColumnNum("G");
			micY = XlsxUtil.getColumnNum("Q");
		}else{
			//普通排期
			startRow = 8;
			urlY = XlsxUtil.getColumnNum("D");
			micY = XlsxUtil.getColumnNum("R");
		}
		writeUrl2File(xlsxUtil, startRow, url, urlY, micY, micArr);
		
		//百度关键词
		if("推广计划名称".equals(xlsxUtil.readCellData(3, 0).trim())){
			//PC端
			startRow = 4;
			urlY = XlsxUtil.getColumnNum("D");
			micY = XlsxUtil.getColumnNum("H");
			clickY =  XlsxUtil.getColumnNum("F");
			writeKeyUrl2File(xlsxUtil, startRow, url, urlY, micY, clickY, micArr);
			
			//Mobile端
			startRow = 4;
			urlY = XlsxUtil.getColumnNum("E");
			micY = XlsxUtil.getColumnNum("I");
			clickY =  XlsxUtil.getColumnNum("G");
			writeKeyUrl2File(xlsxUtil, startRow, url, urlY, micY, clickY, micArr);
		}//end if
		
		xlsxUtil.close(null);
	}
	
	
	private static void writeUrl2File(XlsxUtil xlsxUtil,int startRow,String url,int urlY,int micY,TbAmpUpdatUrlAndSchedule[] micArr){
		List<TbAmpUpdatUrlAndSchedule> micList = Arrays.asList(micArr);
		int lastRow = xlsxUtil.getLastRow();
		
		//遍历排期表的mic
		int i = 0;
		String readMic = "",mic = "";
		
		for (; startRow < lastRow; startRow++) {
			readMic = xlsxUtil.readCellData(startRow, micY).trim();
			
			//遍历需要更改的mic
			for (i = 0; i < micList.size(); i++) {
				mic = micList.get(i).getMic();
				//如果找到在排期表中找到需要更改MIC,将后端代码写入对应位置
				if(readMic.equals(mic)){
					
					//将URL拼接成的 后端代码写回文件
					xlsxUtil.writeCellData(MonitoringCodeTool.afterUrl(url, mic), startRow, urlY);
					break;
				}
			}//end for (i = 0; i < micList.size(); i++)
		}//end for (; startRow < lastRow; startRow++) 
	}
	
	/**
	 * 百度关键词写会排期文件
	 * @param xlsxUtil
	 * @param startRow
	 * @param url
	 * @param urlY
	 * @param micY
	 * @param micArr
	 */
	private static void writeKeyUrl2File(XlsxUtil xlsxUtil,int startRow,String url,int urlY,int micY,int clickY,TbAmpUpdatUrlAndSchedule[] micArr){
		List<TbAmpUpdatUrlAndSchedule> micList = Arrays.asList(micArr);
		int lastRow = xlsxUtil.getLastRow();
		
		//遍历排期表的mic
		int i = 0;
		String readMic = "",mic = "";
		
		for (; startRow < lastRow; startRow++) {
			readMic = xlsxUtil.readCellData(startRow, micY).trim();
			
			//遍历需要更改的mic
			for (i = 0; i < micList.size(); i++) {
				mic = micList.get(i).getMic();
				//如果找到在排期表中找到需要更改MIC,将后端代码写入对应位置
				if(readMic.equals(mic)){
					
					//将URL拼接成的 后端代码写回文件
					xlsxUtil.writeCellData(MonitoringCodeTool.afterUrl(url, mic), startRow, clickY);
					//将URL写回到排期文件
					xlsxUtil.writeCellData(url, startRow, urlY);
					break;
				}
			}//end for (i = 0; i < micList.size(); i++)
		}//end for (; startRow < lastRow; startRow++) 
	}
	
	//更改URL定时器
	private class UpdateUrlTimer{
		
		private TbAmpUpdateUrlInfo urlInfo = null;
		private long startTime;
		
		UpdateUrlTimer(TbAmpUpdateUrlInfo urlInfo,long startTime){
			this.urlInfo = urlInfo;
			this.startTime = startTime;
			this.bind();
		}
		
		void bind(){
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					System.out.println("生效时间："+startTime);
					System.out.println("生效咯："+urlInfo);
					//try {
						changeUrlAndSendEmail(urlInfo);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
				}
			},startTime);
		}
		
	}//end UpdateUrlTimer()

	
//	public class UpdateUrlTask extends TimerTask{
//		
//		UrlUpdateThread updateThread;
//		
//		UpdateUrlTask(UrlUpdateThread updateThread){
//			this.updateThread = updateThread;
//		}
//		
//		@Override
//		public void run() {
//			updateThread.init();
//		}
//	}
	
	
	public static void main(String[] args) {
		Date updateTime = DateUtil.getDate("2017-12-03 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		long startTime;
		//提前20分钟生效
		startTime = updateTime.getTime() - now.getTime() - 1200000;
		startTime = (startTime > 0) ? startTime : 1;
		System.out.println(startTime);
		
		System.out.println(now.compareTo(updateTime));
		//创建定时器任务，并生效
//		UpdateUrlTimer urlTimer = new UpdateUrlTimer(urlInfo,startTime);
//		urlTimer.bind();
	}
}
