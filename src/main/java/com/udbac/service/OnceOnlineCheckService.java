package com.udbac.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.OnlineCheckInfo;

/**
 * 
 * 上线核查
 * @author han
 *
 */
public interface OnceOnlineCheckService {

	/**
	 * 上线核查页
	 * @param date
	 * @return
	 */
	List<OnlineCheckInfo> queryOnlineInfo(String select_date,String cusName,String mic,String state,String userName);
	
	/**保存上传图片路径
	 * 
	 * @param exposureFile 曝光核查
	 * @param mic 短代码
	 */
	void saveExposurePath(String exposureFile,String mic);
	
	/**保存上传图片路径
	 * 
	 * @param click_file 点击核查
	 * @param mic 短代码
	 */
	void saveClickPath(String exposureFile,String mic);
	
	/**保存上传图片路径
	 * 
	 * @param after_file 后端核查
	 * @param mic 短代码
	 */
	void saveAfterPath(String afterFile,String mic);

	/**
	 * 改变上线状态
	 * @param mic
	 * @param state  想要更改的上线状态
	 * @param or_state  原始上线状态
	 */
	void changeState(String mic,String state,Date date);
	
	/**
	 * 改变点位状态
	 * @param new_mic_state
	 * @param mic
	 */
	void setMicState(Integer mic_state,String mic);
	
	String queryLastState(String mic);
}
