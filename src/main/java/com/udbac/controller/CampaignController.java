package com.udbac.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.udbac.dao.CampaignDao;
import com.udbac.entity.TbAmpUpdateUrl;
import com.udbac.model.UserBean;
import com.udbac.util.LogUtil;
/****
 * 营销活动一览
 * @author lp
 * @date 2016-04-14
 *
 */
@RestController
@RequestMapping("/campaign")
public class CampaignController {
	private LogUtil logUtil = new LogUtil(CampaignController.class);

	@Autowired(required = true)
	private CampaignDao campaignDao;
	private LogUtil log;

	/****
	 * 查询活动信息 lp 2016-04-13
	 * 
	 * @param sdate
	 * @param edate
	 * @param unit
	 * @param activityCode
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/queryCampaignList.do", method = RequestMethod.POST)
	public @ResponseBody List<Object> getListSite(@RequestParam(value = "sdate") String sdate,
			@RequestParam(value = "edate") String edate, @RequestParam(value = "unit") String unit,
			@RequestParam(value = "activityCode") String activityCode) throws UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sdate", sdate);
		map.put("edate", edate);
		map.put("unit", unit);
		map.put("activityCode", activityCode);
		List<Object> list = null;
		try {
			list = campaignDao.queryCampaignList(map);
			System.out.println(list.toString());
		} catch (Exception e) {
			log.logError("OLM-EC00012");
		}
		return list;
	}

	/****
	 * 获取Session中用户角色 lp 2016-04-14
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/getUserSession.do", method = RequestMethod.POST)
	public @ResponseBody void getUserSeesion(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserBean user;
		try {
			user = (UserBean) request.getSession().getAttribute("user");
			response.getWriter().print(user.getROLE_NAME());
		} catch (Exception e) {
			response.getWriter().print("error");
		}
	
	}

	/****
	 * 查询URL变更表，找到相应的活动编号
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getUpdateUrl.do", method = RequestMethod.POST)
	public @ResponseBody List<TbAmpUpdateUrl> getUpdateUrl() throws IOException {
		System.out.println("查询UpdateUrl开始");
		List<TbAmpUpdateUrl> list =null;
		try {
			list = campaignDao.queryCampaignUpdate();
		} catch (Exception e) {
			log.logError("OLM-EC00011");
			e.printStackTrace();
		}
		return list;
	}
	/****
	 *  查询活动编号是否存在
	 * @param _activitycode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/QueryActivitycode.do",  method = RequestMethod.POST)
	public @ResponseBody Integer queryUsername(
			@RequestParam(value = "activitycode") String  _activitycode
			)throws Exception {
		Integer count = campaignDao.QueryActivitycode(_activitycode);
		try {
			if(count>0){
				return 1;
			}
		} catch (Exception e) {
			log.logError("OLM-EC00011");
		}
		return 0;
		
	}
	
	/****
	 *  查询投放单位是否存在
	 * @param _activitycode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/QueryUnit.do",  method = RequestMethod.POST)
	public @ResponseBody Integer queryUnit(
			@RequestParam(value = "unit") String  _unit
			)throws Exception {
		Integer count = campaignDao.QueryUnit(_unit);
		try {
			if(count>0){
				return 1;
			}
		} catch (Exception e) {
			log.logError("OLM-EC00010");
		}
		return 0;
		
	}
	/**
	 * 营销活动——活动监测数据
	 * @author LQ
	 * @param _sdate
	 * @param _edate
	 * @return list
	 * @date 2016-04-18
	 */
	@RequestMapping(value = "/queryMonitingData.do",  method = RequestMethod.POST)
	public @ResponseBody List<Object> queryMonitingData(
			
			@RequestParam(value = "sdate") String _sdate,
			@RequestParam(value = "edate") String _edate,
			@RequestParam(value = "activityCode") String activityCode){
		System.out.println(_sdate+"===="+_edate+"=========="+activityCode);
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("sdate", _sdate);
		map.put("edate", _edate);
		map.put("activityCode", activityCode);
		
			List<Object> list = campaignDao.queryMonitingData(map);
			System.out.println(list.toString()+"查询结束");
		try {
			if(list.size()>0){
				logUtil.logInfo("OLM-IC00012");
				logUtil.logInfoCon(_edate+_sdate+activityCode);			
			}else{
				logUtil.logInfo("OLM-EC00012");
				logUtil.logInfoCon(_edate+_sdate+activityCode+list.toString());
			}
				
		} catch (Exception e) {
			// TODO: handle exception
			logUtil.logInfo("OLM-EC00013"+e);
		}
		
	return list;
	}
	
	/**
	 * 营销活动——活动监测数据 条件查询
	 * @author LQ
	 * @date 2016-04-20
	 * @param _sdate
	 * @param _edate
	 * @param _mediaName
	 * @param _marketingCode
	 * @return list
	 */
	@RequestMapping(value = "/searchMonitingData.do",  method = RequestMethod.POST)
	public @ResponseBody List<Object> searchMonitingData(
			@RequestParam(value = "sdate") String _sdate,
			@RequestParam(value = "edate") String _edate,
			@RequestParam(value = "mediaName") String _mediaName,
			@RequestParam(value = "activityCode") String activityCode,
			@RequestParam(value = "marketingCode") String _marketingCode){
		System.out.println(_sdate+"===="+_edate+"=========="+_mediaName+"========"+_marketingCode);
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("sdate", _sdate);
		map.put("edate", _edate);
		map.put("mediaName", _mediaName);
		map.put("marketingCode", _marketingCode);
		map.put("activityCode", activityCode);
		List<Object> list = campaignDao.searchMonitingData(map);
		System.out.println(list.toString()+"查询结束");
		try {
			if(list.size()>0){
				logUtil.logInfo("OLM-IC00025");
				logUtil.logInfoCon(_marketingCode+_edate+_sdate+_mediaName+list.toString());
			}else{
				logUtil.logInfo("OLM-EC00025");
				logUtil.logInfoCon(_marketingCode+_edate+_sdate+_mediaName+list.toString());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			logUtil.logErrorExc(e);
		}
	return list;
	}
	/**
	 * 营销活动——活动监测数据 条件查询
	 * @author LQ
	 * @date 2016-04-20
	 * @param _sdate
	 * @param _edate
	 * @param _mediaName
	 * @param _marketingCode
	 * @return list
	 */
	@RequestMapping(value = "/searchTodayMonitingData.do",  method = RequestMethod.POST)
	public @ResponseBody List<Object> searchTodayMonitingData(
			@RequestParam(value = "nowDate") String nowDate,
			@RequestParam(value = "mediaName") String _mediaName,
			@RequestParam(value = "activityCode") String activityCode,
			@RequestParam(value = "marketingCode") String _marketingCode){
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("nowDate", nowDate);
		map.put("mediaName", _mediaName);
		map.put("marketingCode", _marketingCode);
		map.put("activityCode", activityCode);
		List<Object> list = campaignDao.searchTodayMonitingData(map);
		System.out.println(list.toString()+"查询结束");
		try {
			if(list.size()>0){
				logUtil.logInfo("OLM-IC00025");
				logUtil.logInfoCon(_marketingCode+nowDate+_mediaName+list.toString());
			}else{
				logUtil.logInfo("OLM-EC00025");
				logUtil.logInfoCon(_marketingCode+nowDate+_mediaName+list.toString());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			logUtil.logErrorExc(e);
		}
	return list;
	}
	/**
	 * 营销活动——活动监测数据 条件查询
	 * @author LQ
	 * @date 2016-04-20
	 * @param _sdate
	 * @param _edate
	 * @param _mediaName
	 * @param _marketingCode
	 * @return list
	 * @throws Exception 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/searchHourMonitingData.do",  method = RequestMethod.POST)
	public @ResponseBody List<Object> searchHourMonitingData(
			@RequestParam(value = "_sdate") String sdate,
			@RequestParam(value = "_edate") String edate,
			@RequestParam(value = "mediaName") String _mediaName,
			@RequestParam(value = "activityCode") String activityCode,
			@RequestParam(value = "marketingCode") String _marketingCode) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		
		SimpleDateFormat sdH = new SimpleDateFormat("yyyy-MM-dd HH:00:00");	
		Date _sdate= sdH.parse(sdate);
		Date _edate= sdH.parse(edate);
		List<Object> list = campaignDao.searchHourMonitingData(_sdate,_edate,_mediaName,_marketingCode,activityCode);
		System.out.println(list.toString()+"查询结束");
		try {
			if(list.size()>0){
				logUtil.logInfo("OLM-IC00025");
				logUtil.logInfoCon(_marketingCode+_sdate+_edate+_mediaName+list.toString());
			}else{
				logUtil.logInfo("OLM-EC00025");
				logUtil.logInfoCon(_marketingCode+_sdate+_edate+_mediaName+list.toString());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			logUtil.logErrorExc(e);
		}
	return list;
	}

}
