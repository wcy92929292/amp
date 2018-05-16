package com.udbac.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.udbac.entity.TbAmpBasicActivityInfo;
import com.udbac.entity.TbAmpUpdateUrlInfo;
import com.udbac.model.UrlUpateCheckModel;

/**
 * URL变更DAO
 * @author LFQ
 *
 */
public interface UpdateUrlInfoDao {

	/**
	 * 新增URL变更信息
	 * @param updateUrlInfo
	 * @return
	 */
	void saveUpdateInfo(TbAmpUpdateUrlInfo updateUrlInfo);
	
	/**
	 * 保存URL变更信息以及点位信息的关系
	 * @param params
	 * 		Integer	updateBatch 修改批次
	 * 		List<String> mics 涉及短代码
	 */
	void saveUrlAndSchedule(Map<String,Object> params);
	
	/**
	 * 新增URL变更按钮信息 
	 * @param params
	 * 		Integer	updateBatch 修改批次
	 * 		List<TbAmpUpdateUrlInfo> buttonInfos 按钮信息
	 */
	void saveButtonInfo(Map<String,Object> params);
	
	/**
	 * 根据活动编号查找URL变更信息
	 * @return
	 */
	List<TbAmpUpdateUrlInfo> findUpdateUrlInfo(@Param("actCode")String actCode);
	
	/**
	 * 新增URL变更按钮信息的按钮ID
	 * @param params
	 * 		Integer	updateBatch 修改批次
	 * 		List<TbAmpUpdateUrlInfo> buttonInfos 按钮信息
	 */
	void updateButtonInfo(Map<String,Object> params);
	
	/**
	 * 改变URL变更批次的状态 
	 * @param params
	 * 		Integer	updateBatch 修改批次
	 * 		String checkState：2:审核通过   3：审核不通过 
	 */
	void checkUpdateInfo(Map<String,Object> params);
	
	/**
	 * 根据修改批次或者变更状态获取URL变更信息
	 */
	List<TbAmpUpdateUrlInfo> findByUpdateBatchOrState(@Param("updateBatch")Integer updateBatch,@Param("state")String state);
	
	/**
	 * 更改URL变更状态
	 * @param updateBatch
	 * @param state
	 */
	void changeUpdateState(@Param("updateBatch")Integer updateBatch,@Param("state")String state);
	
	/**
	 * URL变更信息生效
	 * @param tbAmpUpdateUrlInfo
	 */
	void changeScheUrl(@Param("updateUrlInfo")TbAmpUpdateUrlInfo tbAmpUpdateUrlInfo);
	
	@Select("SELECT schedule_path schedulePath,act.activity_code activityCode FROM tb_amp_basic_activity_info act,tb_amp_basic_schedule_info sche WHERE sche.activity_code = act.activity_code AND mic = #{mic}")
	TbAmpBasicActivityInfo findSchedulePathByMic(@Param("mic") String mic);
	
	public void updateTime(@Param("updateUrlInfo")TbAmpUpdateUrlInfo tbAmpUpdateUrlInfo);
	
	/**
	 * 获取提前生效的URL变更信息
	 * @return
	 */
	List<UrlUpateCheckModel> checkUpdate();
}
