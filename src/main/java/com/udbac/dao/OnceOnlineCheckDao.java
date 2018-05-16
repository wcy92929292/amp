package com.udbac.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.udbac.entity.OnlineCheckInfo;

/**
 * 上线核查-已上线的
 * @author han
 *
 */

public interface OnceOnlineCheckDao {
	
	/**
	 * 上线核查页
	 * @param date
	 * @return
	 */
	List<OnlineCheckInfo> queryOnlineInfo(Map<String,String> map);
	
	/**保存上传图片路径
	 * 
	 * @param exposureFile 曝光核查
	 * @param mic 短代码
	 */
	void saveExposurePath(@Param("exposureFile") String exposureFile,@Param("mic") String mic);

	/**保存上传图片路径
	 * 
	 * @param click_file 点击核查
	 * @param mic 短代码
	 */
	void saveClickPath(@Param("click_file") String exposureFile,@Param("mic") String mic);
	
	/**保存上传图片路径
	 * 
	 * @param after_file 后端核查
	 * @param mic 短代码
	 */
	void saveAfterPath(@Param("after_file") String afterFile,@Param("mic") String mic);

	/**
	 * 改变上线状态
	 * @param mic
	 * @param state  想要更改的上线状态
	 * @param or_state  原始上线状态
	 */
	void changeState(@Param("mic") String mic,@Param("state") String state,@Param("date") Date date);
	
	/**
	 * 
	 * @param new_mic_state  新的点位状态
	 * @param mic //短代码
	 */
	void setMicState(@Param("mic_state") Integer mic_state,@Param("mic") String mic);
	
	String queryLastState(@Param("mic") String mic);
}
