package com.udbac.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.udbac.entity.TbAmpBasicActivityInfo;
import com.udbac.model.UserBean;
import com.udbac.util.XlsxUtil;

/**
 * 排期插码服务类
 * @author LFQ
 * @data  2016-04-13
 */
public interface ScheduleService {
	
	/**
	 *
	 * @param scheduleFile	排期文件
	 * @param pointLine		排期频控点位属性
	 * @param updateFile	强制插码确认
	 * @return
	 */
//	Map<String,Object>  genSchedule(MultipartFile scheduleFile,String[] pointLine,MultipartFile updateFile,UserBean user,String after,String before,String center);
//	Map<String,Object>  genSchedule(MultipartFile scheduleFile,MultipartFile updateFile,UserBean user,String after,String before,String center,String createUser);
	/**
	 *  普通排期插码
	 * @param scheduleFile	排期文件
	 * @param updateFile	强制插码文件
	 * @param user			操作用户
	 * @param after			后端人员
	 * @param before		前端人员
	 * @param center		监测中心
	 * @param isAddPoint	是否增加点位
	 * @return
	 */
	Map<String,Object>  genSchedule(MultipartFile scheduleFile,
					MultipartFile updateFile,
					UserBean user,
					String after,
					String before,
					String center,
					boolean isAddPoint
			);
	
	/**
	 * 集团排期插码
	 * @param 排期文件流  
	 * @return
	 */
//	Map<String,Object>  jtSchedule(MultipartFile scheduleFile,String[] pointLine,MultipartFile updateFile,UserBean user,String after,String before,String center);
	 Map<String,Object>  jtSchedule(MultipartFile scheduleFile,MultipartFile updateFile,UserBean user,String after,String before,String center,boolean isAddPoint);
	
	/**
	 * 关键词排期插码
	 * @param 排期文件流 
	 * @return
	 */
//	 Map<String,Object> keySchedule(MultipartFile scheduleFile,String[] pointLine,MultipartFile updateFile,UserBean user,String after,String before,String center);
	 Map<String,Object> keySchedule(MultipartFile scheduleFile,MultipartFile updateFile,UserBean user,String after,String before,String center,boolean isAddPoint);
	
	 /**
	  * 后端补排期
	  * @param scheduleFile
	  * @param user
	  * @param after 后端人员
	  * @param before	前端人员
	  * @param center	监测中心人员
	  * @return
	  */
	 Map<String,Object> afterSchedule(MultipartFile scheduleFile,UserBean user,String after,String before,String center);

	/**
	 * 根据活动编号下载排期
	 * @param activityCode
	 * @return
	 */
	 TbAmpBasicActivityInfo  downloadSchedule(String activityCode);
	
	 /**
	  * 同步跳转表
	  * @return
	  */
	 String downloadMicAndUrl();
	 
	 /**
	  * 延长排期时期
	  * @param scheduleType
	  * @param scheduleFile
	  * @return
	  */
	 public List<String> addScheduleDate(String scheduleType,MultipartFile scheduleFile);
	 
	 /**
	  * 更改排期预估
	  * @param scheduleType
	  * @param scheduleFile
	  * @return
	  */
	 public List<String> updateForecase(String scheduleType,MultipartFile scheduleFile);
	 
}
