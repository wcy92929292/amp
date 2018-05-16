package com.udbac.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udbac.dao.ScheduleDao;
import com.udbac.dao.UpdateUrlInfoDao;
import com.udbac.entity.TbAmpBasicScheduleInfo;
import com.udbac.entity.TbAmpUpdatUrlAndSchedule;
import com.udbac.entity.TbAmpUpdateUrlButtonInfo;
import com.udbac.entity.TbAmpUpdateUrlInfo;
import com.udbac.service.UpdateUrlService;
import com.udbac.thread.UrlUpdateThread;
import com.udbac.util.DateUtil;

/**
 * 增加新URL
 * @author LFQ
 */
@Service
public class UpdateUrlServiceImpl implements UpdateUrlService{

	@Autowired
	private ScheduleDao scheduleDao;
	
	@Autowired
	private UpdateUrlInfoDao updateUrlInfoDao;
	
	@Autowired
	private UrlUpdateThread urlUpdateThread;
	
	
	/**
	 * 审核URL变更信息
	 * @param buttonInfo
	 * @param updateBatch
	 * @param checkState
	 * @return
	 */
	@Transactional
	public String checkUrl(String buttonInfo,String updateBatch,String checkState){
		
		//解析按钮字符串
		List<TbAmpUpdateUrlButtonInfo> splitButInfo = splitButInfo(buttonInfo);
		
		Map<String,Object> params = new HashMap<>();
		
		//将按钮ID插入
		params.put("buttonInfos", splitButInfo);
		params.put("updateBatch",updateBatch);
		updateUrlInfoDao.updateButtonInfo(params);
		
		//保存审核状态
		params.remove("buttonInfos");
		params.put("updateBatch",updateBatch);
		params.put("checkState", checkState);
		updateUrlInfoDao.checkUpdateInfo(params);
		
//		List<TbAmpUpdateUrlInfo> batchOrState = updateUrlInfoDao.findByUpdateBatchOrState(Integer.parseInt(updateBatch),null);
//		TbAmpUpdateUrlInfo updateUrlInfo = null;
//		
//		//判断生效时间是否为过去时间，是则进行生效
//		for (int i = 0; i < batchOrState.size(); i++) {
//			updateUrlInfo = batchOrState.get(i);
//			if(new Date().compareTo(updateUrlInfo.getUrlUpdateTime()) > -1){
//				urlUpdateThread.changeUrlAndSendEmail(updateUrlInfo);
//			}
//		}
		
		urlUpdateThread.init();
		
		return "1";
	}
	
	
	
	/**
	 * 变更URL新增
	 */
	@Override
	public String newUrl(String actCode, String urlUpdateTime, String newUrl,
			String checkUserID, String mics, String memo, String butInfo,String createUser) {
		
		StringBuffer sb = new StringBuffer();
		
		//URL变更时间是否有效：1小时以上，并且格式正确：yyyy-MM-dd HH:mm:ss
		Date date = DateUtil.getDate(urlUpdateTime, "yyyy-MM-dd HH:mm:ss");
		if(date != null){
			if((date.getTime() - new Date().getTime()) < UPDATE_TIME){
				sb.append("生效时间不能小于当前时间五分钟！</BR>");
			}
		}else{
			sb.append("生效日期不正确！</BR>");
		}
		
		//新链接是否符合格式要求
		if(!newUrl.startsWith("http") || !String.valueOf(newUrl.charAt(newUrl.length()-1)).matches("[/\\w#$&?]") || newUrl.contains("<") || newUrl.contains(">")){
			sb.append("URL非法</BR>");
		}
		
		//判断所填写的mic是否都属于所填写actCode
		int i,j;
		mics = mics.replace(" ","");	//去除空格
		String[] micArr = mics.split("\n");
		HashSet<TbAmpUpdatUrlAndSchedule> set = new HashSet<>();;
		List<TbAmpBasicScheduleInfo> scheduleInfos = scheduleDao.findByMicAndActCode(actCode, micArr);
		TbAmpUpdatUrlAndSchedule urlAndSche = null;
		//判读是否有所填写的MIC都合法
			micFor:for (i = 0; i < micArr.length; i++) {
				for (j = 0; j < scheduleInfos.size(); j++) {
					if("".equals(micArr[i])){
						continue micFor;
					}
					//记录URL变更相关的点位
					if(scheduleInfos.get(j).getMic().equals(micArr[i])){
						urlAndSche = new TbAmpUpdatUrlAndSchedule();
						urlAndSche.setMic(micArr[i]);
						urlAndSche.setOldUrl(scheduleInfos.get(j).getUrlPc());
						set.add(urlAndSche);
						continue micFor;
					}
				}
				sb.append(micArr[i]);
				sb.append("不属于");
				sb.append(actCode);
				sb.append("</BR>");
			}//end micFor:for
		
		//添加URL变更相关的mic
//		for (int k = 0; k < micArr.length; k++) {
//				
//		}
		
		//有错误信息,进行返回
		if(sb.length() > 0){return sb.toString();}
		
		//解析按钮信息
		List<TbAmpUpdateUrlButtonInfo> butList = splitButInfo(butInfo);
		
		//设置活动信息
		TbAmpUpdateUrlInfo updateUrlInfo = new TbAmpUpdateUrlInfo();
		updateUrlInfo.setButtonInfo(butList);
		updateUrlInfo.setMics(set);
		updateUrlInfo.setCheckUser(checkUserID);
		updateUrlInfo.setCreateUser(createUser);
		updateUrlInfo.setMemo(memo);
		updateUrlInfo.setUrlUpdate(newUrl);
		updateUrlInfo.setUrlUpdateTime(date);
		//将URL变更信息入库
		saveUrlInfo(updateUrlInfo);
		
		return "1";
	}//end new Url
	
	/**
	 * URL变更信息入库
	 * @param updateUrlInfo
	 * @return
	 */
	@Transactional
	public String saveUrlInfo(TbAmpUpdateUrlInfo updateUrlInfo){
		
		synchronized (this.getClass()) {
			Integer updateBatch = Integer.parseInt(String.valueOf(new Date().getTime()).substring(4));
			updateUrlInfo.setUpdateBatch(updateBatch);
		}
		
		//保存URL变更信息
		updateUrlInfoDao.saveUpdateInfo(updateUrlInfo);
		
		//保存按钮信息
		Map<String,Object> params = new HashMap<>();
		params.put("updateBatch",updateUrlInfo.getUpdateBatch());
		params.put("buttonInfos",updateUrlInfo.getButtonInfo());
		updateUrlInfoDao.saveButtonInfo(params);
		
		//保存点击以及短代码关系
		params.clear();
		params.put("updateBatch",updateUrlInfo.getUpdateBatch());
		params.put("mics",updateUrlInfo.getMics());
		updateUrlInfoDao.saveUrlAndSchedule(params);
		
		return "1";
	}//end saveUrlInfo()
	
	/**
	 * 根据活动编号查找新URL变更信息
	 */
	public List<TbAmpUpdateUrlInfo> findUrlByActCode(String actCode){
		List<TbAmpUpdateUrlInfo> urlInfo = updateUrlInfoDao.findUpdateUrlInfo(actCode);
		
		return urlInfo;
	}//end findUrlByActCode
	
	/**
	 * 解析按钮信息
	 */
	public List<TbAmpUpdateUrlButtonInfo> splitButInfo(String butInfo){
		
		String[] butInfos = butInfo.split("\\],\\[");
		String[] infos;
//		buttonName
//		buttonID
//		buttonEvent
//		involveIndex
//		buttonType
		TbAmpUpdateUrlButtonInfo buttonInfo;
		List<TbAmpUpdateUrlButtonInfo> butList = new LinkedList<>();
		
		for (int i = 0; i < butInfos.length; i++) {
			//防止HTML代码注入
			butInfos[i] = butInfos[i].replaceAll("<","&lt;");
			butInfos[i] = butInfos[i].replaceAll(">","&gt;");
			infos = butInfos[i].split("&,&");
			if(infos.length == 5){
				buttonInfo = new TbAmpUpdateUrlButtonInfo();
				buttonInfo.setButtonName(infos[0]);
				buttonInfo.setButtonId(infos[1]);
				buttonInfo.setButtonEvent(infos[2]);
				buttonInfo.setInvolveIndex(infos[3]);
				buttonInfo.setButtonType(infos[4]);
				
				butList.add(buttonInfo);
			}//end if
		}//end for
		
		return butList;
	}//end splitButInfo()



	@Override
	public String updateTime(String updateBatch, String urlUpdateTime) {
		
		Date date = DateUtil.getDate(urlUpdateTime, "yyyy-MM-dd HH:mm:ss");
		
		if(date != null){
			if((date.getTime() - new Date().getTime()) < UPDATE_TIME){
				return "2";
			}
		}else{
			return "3";
		}
		if(updateBatch.matches("\\d{1,}")){
			TbAmpUpdateUrlInfo tbAmpUpdateUrlInfo = new TbAmpUpdateUrlInfo();
			tbAmpUpdateUrlInfo.setUpdateBatch(Integer.parseInt(updateBatch));
			tbAmpUpdateUrlInfo.setUrlUpdateTime(date);
			
			updateUrlInfoDao.updateTime(tbAmpUpdateUrlInfo);
			return "1";
		}
		
		return "0";
	}
	
}
