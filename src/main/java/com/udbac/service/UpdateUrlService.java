package com.udbac.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.udbac.entity.TbAmpUpdateUrlInfo;


/**
 * URL变更Service
 * @author LFQ
 *
 */
public interface UpdateUrlService {
	
	//提前多长时间生效
	public static final int UPDATE_TIME = 300000; 
	
	/**
	 * 新增URL变更信息
	 * @param actCode
	 * @param urlUpdateTime
	 * @param newUrl
	 * @param checkUserID
	 * @param mics
	 * @param memo
	 * @param butInfo
	 * @param createUser
	 * @return
	 */
	String newUrl(String actCode,String urlUpdateTime,String newUrl,String checkUserID,String mics,String memo,String butInfo,String createUser);
	
	/**
	 * 通过活动编号查找该活动下面的短代码
	 * @param actCode
	 * @return
	 */
	List<TbAmpUpdateUrlInfo> findUrlByActCode(String actCode);
	
	/**
	 * 审核URL变更信息
	 * @param buttonInfo	按钮信息
	 * @param updateBatch	更改URL批次
	 * @param checkState	审核状态	2:审核通过		3:审核不通过
	 * @return
	 */
	public String checkUrl(String buttonInfo,String updateBatch,String checkState);
	
	/**
	 * URL生效时间变更
	 * @param updateBatch
	 * @param urlUpdateTime
	 * @return
	 */
	public String updateTime(String updateBatch,String urlUpdateTime);
}
