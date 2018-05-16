package com.udbac.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.udbac.util.DateUtil;
import com.udbac.dao.CustomerDao;
import com.udbac.dao.MonitorPlanDao;
import com.udbac.dao.ScheduleCalendarDao;
import com.udbac.dao.ScheduleDao;
import com.udbac.dao.ScheduleInfoBackupDao;
import com.udbac.dao.ScheduleInfoMaterialSeqDao;
import com.udbac.entity.TbAmpBasicActivityInfo;
import com.udbac.entity.TbAmpBasicCustomerInfo;
import com.udbac.entity.TbAmpBasicMediaInfo;
import com.udbac.entity.TbAmpBasicSchedulCalendarInfo;
import com.udbac.entity.TbAmpBasicScheduleInfo;
import com.udbac.entity.TbAmpBasicScheduleInfoBackup;
import com.udbac.entity.TbAmpBasicUserInfo;
import com.udbac.exception.ScheduleException;
import com.udbac.model.RefreshCalendarModel;
import com.udbac.model.UserBean;
import com.udbac.service.MediaService;
import com.udbac.service.ScheduleService;
import com.udbac.util.BeanUtil;
import com.udbac.util.FilePathManager;
import com.udbac.util.FileUtil;
import com.udbac.util.LogUtil;
import com.udbac.util.MonitoringCodeTool;
import com.udbac.util.ScheduleProperties;
import com.udbac.util.XlsxUtil;

/**
 * 排期插码服务类
 * @author LFQ
 * @data 2016-04-13
 */
@Service
public class ScheduleServiceImpl implements ScheduleService{

	//排期文件操作
	private XlsxUtil xu;
	private LogUtil logUtil = new LogUtil(ScheduleServiceImpl.class);
	//排期路径管理
	@Autowired
	private FilePathManager fpm;
	//普通排期基础坐标
	private static Map<int[],String> genScheduleMap;
	//集团排期基础坐标
	private static Map<int[],String> jtScheduleMap;
	//关键词排期基础坐标
	private static Map<int[],String> keyScheduleMap;
	//普通排期Field属性
	private String[] genFieldNames = null;
	//集团排期Field属性
	private String[] jtFieldNames = null;
	//关键词排期表中排期PC端Field属性
	private static String[] pcfieldName;
	//关键词排期表中排期Mobile端Field属性
	private static String[] mobilefieldName;
	
	//错误信息拼接
	private StringBuffer mesSB = new StringBuffer(30);
	private static Map<String,TbAmpBasicCustomerInfo> cusMap = new HashMap<>();
	
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private MediaService mediaService;
	@Autowired
	private ScheduleInfoMaterialSeqDao scheduleInfoMaterialSeqDao;
	@Autowired
	private MonitorPlanDao monitorPlanDao;
	@Autowired
	private ScheduleDao scheduleDao;
	@Autowired
	private ScheduleCalendarDao scheduleCalendarDao;
	@Autowired
	private ScheduleInfoBackupDao scheduleInfoBackupDao;
	
	//信息提示集合
	private List<String> mesList = new LinkedList<>();
	
	private Map<String,Object> map; 
	
	//初始化服务类
	private void init(){
		if(cusMap.size() == 0){
			List<TbAmpBasicCustomerInfo> cusList = customerDao.selectAll();
			TbAmpBasicCustomerInfo customerInfo;
			for (int i = 0; i < cusList.size(); i++) {
				customerInfo = cusList.get(i);
				cusMap.put(customerInfo.getCustomerName(), customerInfo);
			}
		}
		
		//获取排期基本配置项
		if(genScheduleMap == null){
			ScheduleProperties.setGenSchedule(fpm.getGenSchedule());
			genScheduleMap = ScheduleProperties.getGenSchedule();
			genFieldNames = ScheduleProperties.getGenFieldNames();
		}
		
		//获取集团排期基本配置项
		if(jtScheduleMap == null){
			ScheduleProperties.setJTSchedule(fpm.getJtScheduleInfoPath());
			jtScheduleMap = ScheduleProperties.getJtSchedule();
			jtFieldNames = ScheduleProperties.getJtFieldNames();
		}
		
		//获取集团排期基本配置项
		if(keyScheduleMap == null){
			ScheduleProperties.setKeySchedule(fpm.getKeyScheduleInfoPath());
			keyScheduleMap = ScheduleProperties.getKeySchedule();
			pcfieldName = ScheduleProperties.getPcfieldName();
			mobilefieldName = ScheduleProperties.getMobilefieldName();
		}
	}//end init()
	
	/**
	 * 将每行点位录入数组中
	 * @param xu
	 * @param lastRow
	 * @param startRow
	 * @param scheduleMinCol
	 * @param lineDataMap
	 * @param clickAvgCol
	 * @param exposureAvgCol
	 */
	public void readSchedule2Arr(XlsxUtil xu,int lastRow,int startRow,int scheduleMinCol,List<String[]> lineDataList,
			Map<String[],Integer> lineDataMap,int clickAvgCol,int exposureAvgCol){
		int lastCell = 0;
		int row,column;
		String[] lineDataArr;
		int group,groupId = 0;
		for (row = startRow; row <= lastRow; row++) {
			
			lastCell = xu.getLastCell(row);
			if(lastCell<= 0 || scheduleMinCol > lastCell){
				lineDataArr = new String[]{""};
				lineDataList.add(lineDataArr);
				continue;
			};
			lineDataArr = new String[lastCell];
			for (column = 0; column < lastCell; column++) {	
				//判断预估是否合并，读取
				if((column == clickAvgCol || column == exposureAvgCol) && xu.isMergedRegion(row, column)){
					group = xu.getMergedRegion(row,column) + 1;
					if(groupId == 0) groupId = group;
				}
				lineDataArr[column] = xu.readCellData(row, column).replaceAll(" ","");
				lineDataArr[column] = lineDataArr[column].replaceAll("\n", "");
			}//end for
					
			lineDataList.add(lineDataArr);
			lineDataMap.put(lineDataArr,groupId);
			groupId = 0;
		}//end for
	}
	
	
	/**
	 * 普通排期插码
	 */
	@Override
	public Map<String,Object> genSchedule(MultipartFile scheduleFile,MultipartFile updateFile,UserBean user,String after,String before,String center,boolean isAddPoint){
		map = new HashMap<String,Object>();
		if(genScheduleMap == null || cusMap.size() == 0){
			init();
		}
		
		int row = 0,column = 0;
		
		try {
			xu = new XlsxUtil(scheduleFile.getInputStream());
			
			checkCellTitile(genScheduleMap);
			
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			
			/////////////////////基础格式如果有误，则不往下校验/////////////////////////////////////
			
			//活动信息校验
			int[] cusNameXY = new int[]{2,1};
			int[] actNameXY = new int[]{3,1};
			int[] actCodeXY = new int[]{5,1};
			int[] actDateXY = new int[]{3,4};
			
			TbAmpBasicActivityInfo activityInfo = checkActivityInfo(cusNameXY, actNameXY, actCodeXY, actDateXY);
			
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			activityInfo.setScheduleType(0);
			/////////////////////排期活动信息如果有误，则不往下校验/////////////////////////////////////
			
			//读取排期信息
			int lastRow = xu.getLastRow();
			List<String[]> lineDataList = new LinkedList<>();	//保存所有排期数据
			String[] lineDataArr = null;	//保存一行排期数据
			Map<String[],Integer> lineDataMap = new HashMap<>(); //保存所有排期数据以及 对应的分组编号
			int scheduleMinCol = XlsxUtil.getColumnNum("Q"); //有效最小的排期列数
			int clickAvgCol = XlsxUtil.getColumnNum("J"); //点击预估所在 列
			int exposureAvgCol = XlsxUtil.getColumnNum("K"); //曝光预估所在列
			int startRow = 8;
			
			//普通排期信息从第八行开始，把所有排期数据都读取封装进字符数组
			readSchedule2Arr(xu, lastRow, startRow, scheduleMinCol, lineDataList, lineDataMap, clickAvgCol, exposureAvgCol);
			
			TbAmpBasicScheduleInfo scheduleInfo = null;
			List<TbAmpBasicScheduleInfo> scheduleList = new LinkedList<>();	//有效的排期信息
			TbAmpBasicSchedulCalendarInfo calendarInfo = null;	//排期投放日期
			TbAmpBasicMediaInfo mediaInfo = null;	//媒体信息
			List<TbAmpBasicSchedulCalendarInfo> calendarList = null;	//排期日历
			int currentYear = 0,currentMonth = 0,currentDay;	//投放日期
			int readMonth;	//读取的月份
//			String[] point = null; //存放频控信息
			//判断是否为排期信息，并根据  genSchedule.properties 配置的 fieldName 封装排期属性，以及投放时期安排
			for(row=0;row < lineDataList.size();row++){
				
				lineDataArr = lineDataList.get(row);
				
				//忽略小计以及空行
				if(lineDataArr.length < 4 || ("".equals(lineDataArr[2]) && "".equals(lineDataArr[3]) && "".equals(lineDataArr[4]) && "".equals(lineDataArr[5]))
						||	lineDataArr[4].contains("小计"))
				{continue;}
				
				
				//是增加点位则忽略识别码有值的点位
				if(lineDataArr.length < genFieldNames.length || (isAddPoint &&!"".equals(lineDataArr[XlsxUtil.getColumnNum("R")]))){
					continue;
				}
				
				scheduleInfo = new TbAmpBasicScheduleInfo();
				calendarList = new LinkedList<>();
				scheduleInfo.setCreateUser(user.getUSER_ID()+"");
				currentYear = Integer.parseInt(DateUtil.getDateStr(activityInfo.getRealityStartDate(), "yyyy"));
				currentMonth = Integer.parseInt(DateUtil.getDateStr(activityInfo.getRealityStartDate(), "MM"));
				
				//设置分组编号
				scheduleInfo.setGroupId(lineDataMap.get(lineDataArr));
				
				for (column = 0; column < lineDataArr.length; column++) {
					//将排期日期前半部分注入bean
					if(column < genFieldNames.length){
						//查看排期点位投放位置，投放形式中是否带有频控、频次字眼
						if(column == 2 || column == 4){
							if(lineDataArr[column].contains("频控") || lineDataArr[column].contains("频次")){
//								12频控次数		13频控方法规则		14频控周期
								if("".equals(lineDataArr[12]) || "".equals(lineDataArr[13]) || "".equals(lineDataArr[14])){
									setMessage(row+8, column, "有频控、频次字眼，但是对应频控属性未填写完整。");
								}
//								int i = 0;
//								//未填写频控处理
//								if(pointLine == null){
//									setMessage(row+8, column, "有频控、频次字眼，未匹配对应的频控属性");
//								}else{
//									//查找是否已经填写了频控规则：频控点位所在行	是否需要频控	频控次数	频控方法规则	频控周期
//									pointsFor:for (String points : pointLine) {
//										i++;
//										point = points.split(",");
//										//忽略未填写点位所在行的频控数据
//										if("".equals(point[0])){
//											continue pointsFor;
//										}
//										//找到需要频控的点位行
//										if((row+9) == Integer.valueOf(point[0])){
//											scheduleInfo.setIsFrequency(point[1]);
//											scheduleInfo.setNumFrequency(point[2]);
//											scheduleInfo.setFunFrequency(point[3]);
//											scheduleInfo.setPeriodFrequency(point[4]);
//											break pointsFor;
//										}
//										//未找到频控处理
//										if(i == pointLine.length){
//											setMessage(row+8, column, "有频控、频次字眼，未匹配对应的频控属性");
//										}
//									}//end pointsFor:for 
//								}
							}//end if
						}//end 频控处理
						
						//将信息注入bean
						BeanUtil.setProperties(scheduleInfo,lineDataArr[column], genFieldNames[column]);
					}
					//排期日期注入bean
					else if(!"".equals(lineDataArr[column])){
						//判断投放值是否格式是否正确
						if(!lineDataArr[column].matches("\\d{1,}") && !lineDataArr[column].matches("\\d{1,}\\.\\d{1,}")){
							setMessage(row + 7, column, "投放量有误，投放量只能是数值。");
							continue;
						}
						
						calendarInfo = new TbAmpBasicSchedulCalendarInfo();
						
						//获取投放月份
						String monthStr = xu.readCellData(5,column).replaceAll("\\D", "");
						if(!monthStr.matches("\\d{1,2}")){
							setMessage(5,column,"月份格式不正确");
							break;
						}
						readMonth = Integer.parseInt(monthStr);
						if(readMonth != currentMonth){
							if(readMonth < currentMonth){
								++currentYear;
							}
							currentMonth = readMonth;
						}
						
						//获取投放日期
						String readDay = xu.readCellData(7,column).trim();
						if(readDay.indexOf(".") > 0){
							currentDay = Integer.parseInt(readDay.substring(0, readDay.indexOf(".")));
						}else{
							currentDay = Integer.parseInt(readDay);
						}
						//判断日期是否有效
						if(!DateUtil.isLegalDate(currentYear, currentMonth, currentDay)){
							setMessage(row+7, column, "日期格式有误");
							continue;
						}
						
						//排期日历的时间比活动结束时间还大，
						Date putDate = DateUtil.getDate(currentYear,currentMonth,currentDay);
						if(putDate.compareTo(activityInfo.getActivityEndDate()) > 0){
							setMessage(row+7,column,"活动结束日期小于投放日期安排");
							continue;
						}
						calendarInfo.setPutDate(putDate);
						try{
							//过滤掉小数点后面的数据
							calendarInfo.setPutValue(lineDataArr[column].substring(0,lineDataArr[column].indexOf(".")));
						}catch(Exception e){
							e.printStackTrace();
							if(e instanceof InvocationTargetException){
								InvocationTargetException targetExc = (InvocationTargetException)e;
								setMessage(row + 8,column, targetExc.getTargetException().getMessage());
							}
						}//end try - catch
						calendarList.add(calendarInfo);
					}//end if - else
				}//end for
				
				//根据媒体名称查看媒体是否存在
				Map<String, String> generalization = mediaService.getGeneralization();
				//通过泛化名称关系，找到标准媒体名称，
				mediaInfo = mediaService.getMedias(false).get(generalization.get(scheduleInfo.getMedia().getMediaName()));
				
				if(mediaInfo == null){
					setMessage(8,1,scheduleInfo.getMedia().getMediaName().replaceAll("\\n",""),"，未匹配该媒体名称，请进行媒体名称泛化操作。");
					continue;
				}else{
					scheduleInfo.getMedia().setMediaId(mediaInfo.getMediaId());
					// 设置标准化后的媒体名称
					scheduleInfo.getMedia().setMediaName(mediaInfo.getMediaName());
				}
				//设置数据口径
				setDataCaliber(scheduleInfo,lineDataArr[2],lineDataArr[4]);
				
				//设置数据频控
				setIsFrequency(scheduleInfo);
				
				scheduleInfo.setActivityInfo(activityInfo);
				scheduleInfo.setCalendarInfo(calendarList);
				scheduleList.add(scheduleInfo);
				//设置活动监测代码
				setScheduleMonitorCode(scheduleInfo);
				//将生成的监测代码写回对应的排期信息
				writeGenScheduleMonitorCode(scheduleInfo, row + 8,xu);
				
			}//end for(row < lineDataList.size())
	
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			
			/////////////////////排期详细信息如果有误，则不往下执行/////////////////////////////////////
			//排期数据入库
			//没有排期数据
			if(scheduleList.size()==0){
				setMessage(8, 0, "没有排期数据！");
				return map;
			}
			
			String filePath = saveSchedule(scheduleList, updateFile,scheduleFile.getOriginalFilename(),user,after,before,center,false,isAddPoint);
			
			if("".equals(filePath)){
				map.put("err", mesList);
				return map;
			}
			
			map.put("filePath", filePath);
			map.put("fileName", filePath.substring(filePath.lastIndexOf("/") + 1,filePath.length() ));
			
			xu.close(filePath);
		} catch (Exception e) {
			//排期数据异常
			if(e instanceof InvocationTargetException){
				InvocationTargetException targetExc = (InvocationTargetException)e;
				setMessage(row + 8,column, targetExc.getTargetException().getMessage());
			}
			else if(e instanceof NumberFormatException){
				setMessage(row+6,column, "格式不正确");
			}
			else if(e instanceof IllegalStateException){
				//java.lang.IllegalStateException: The hyperlink for cell D11 references relation rId2, but that didn't exist!
				IllegalStateException ise = (IllegalStateException)e;
				StringBuffer esb = new StringBuffer(ise.getMessage());
				esb.replace(0,"The hyperlink for cell ".length(),"");
				esb.replace(esb.indexOf(" references relation"),esb.length(),"");
				String[] rows = esb.toString().split("\\D{1,}");
				String[] columns = esb.toString().split("\\d{1,}");
				
				if(rows.length > 1 && columns.length > 0){
					mesList.add(rows[1]+"行，"+columns[0]+"列，请右键，取消超链接！或者前后有特殊字符，请去掉。");
				}else{
					mesList.add("文件格式有误，请重新使用模板或者联系管理员。");
				}
			}
			else if(e instanceof ScheduleException){
					mesList.add(row + 9 + "行，"+ e.getMessage());
			}else{
				mesList.add("文件格式有误，请重新使用模板或者联系管理员。");
			}
			
			map.put("err", mesList);
			e.printStackTrace();
//			logUtil.logErrorExc(e);
		}
		
		return map;
	}//end genSchedule()

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 排期入库处理
	 * @param scheduleList	排期有效信息
	 * @param updateFile	强制插码图片
	 * @param fileName		排期文件名称
	 * @param user			插码的用户
	 * @param after			后端人员
	 * @param before		前端人员
	 * @param center		监测 中心人员
	 * @param isAfterSche	是否为后端补排期
	 * @param isAddPoint 	时候新增点位
	 * @return
	 * @throws Exception 
	 * @throws IOException
	 */
	@Transactional(rollbackForClassName={"java.lang.Exception"})
	public String saveSchedule(List<TbAmpBasicScheduleInfo> scheduleList,MultipartFile updateFile,String fileName,UserBean user, 
			String after,String before,String center,boolean isAfterSche,boolean isAddPoint) throws Exception{
		
		TbAmpBasicActivityInfo activityInfo = null;
		TbAmpBasicScheduleInfo scheduleInfo = null;
		
		scheduleInfo = scheduleList.get(0);
		
		//排期存储路径
		StringBuffer fileSB = new StringBuffer(50);
		fileSB.append(fpm.getScheduleFilePath());
		fileSB.append(scheduleList.get(0).getActivityInfo().getCustomer().getCustomerCode());
		fileSB.append("/");
		
		File f = new File(fileSB.toString());
		if(!f.exists()){
			f.mkdirs();
		}
		
			fileSB.append(fileName);
			int lastIndexOf = fileSB.lastIndexOf(".");
			fileSB.replace(lastIndexOf,lastIndexOf+1,DateUtil.getDateStr(new Date(), "MMddHHmm."));
			lastIndexOf = fileSB.lastIndexOf(".");
			fileSB.replace(lastIndexOf,lastIndexOf+1,"-已加码.");
			
			//根据排期活动编号查找是否有对应监测方案
			activityInfo = monitorPlanDao.findByCode(scheduleInfo.getActivityInfo().getActivityCode());
			
			//未找到监测活动，
			if(activityInfo == null ){
				
				//增加点位
				if(isAddPoint){
					setMessage(3, 1, "未找到监测活动");
					return "";
				}
				
				//并且该客户不允许没有监测方案出排期  并且不是  后端补插码排期
				if(String.valueOf(0).equals(cusMap.get(scheduleInfo.getActivityInfo().getCustomer().getCustomerName()).getSpecialCustomer()) && !isAfterSche){
					setMessage(3, 1, "未找到监测活动，并且",scheduleInfo.getActivityInfo().getCustomer().getCustomerName(),"不允许没有监测方案出排期");
					return "";
				}
				//客户允许没有监测方案出排期
				else{
					//新增监测排期活动信息
					activityInfo = scheduleInfo.getActivityInfo();
					//后端支撑
					TbAmpBasicUserInfo afterUser = new TbAmpBasicUserInfo();
					afterUser.setUserId(Integer.parseInt(after));
					activityInfo.setAfterSupportPeople(afterUser);
					//前端支撑
					TbAmpBasicUserInfo beforeUser = new TbAmpBasicUserInfo();
					beforeUser.setUserId(Integer.parseInt(before));
					activityInfo.setFrontSupportPeople(beforeUser);
					//监测中心
					TbAmpBasicUserInfo centerUser = new TbAmpBasicUserInfo();
					centerUser.setUserId(Integer.parseInt(center));
					activityInfo.setMonitorPeople(centerUser);
					//接口人
					TbAmpBasicUserInfo portUser = new TbAmpBasicUserInfo();
					portUser.setUserId(user.getUSER_ID());
					activityInfo.setPortPeople(portUser);
					
					if(afterUser.getUserId() == -1 || 
						beforeUser.getUserId() == -1 || 
						centerUser.getUserId() == -1){
						
						setMessage(3, 1, "请指定项目相关人员！");
						return "";
					}
					
					//预上线日期
					activityInfo.setPredictStartDate(activityInfo.getRealityStartDate());
					activityInfo.setSchedulePath(fileSB.toString());
				}
				
				monitorPlanDao.insertActivity(activityInfo);
			}//end if  未找到监测活动，
			
			//找到监测方案
			else{	
				//如果是后端补排期，必须不能有相同的监测活动
				if(isAfterSche){
					setMessage(5, 1,activityInfo.getActivityCode(),"后端补排期不允许有相同的监测活动编号");
					return "";
				}
				
				//不是增加点位活动信息
				if(!isAddPoint){
					//但是该监测方案已经插过码
					if(activityInfo.getSchedulePath() != null && !"".equals(activityInfo.getSchedulePath())){
						//未上传强制插码的确认图片
						if(updateFile.isEmpty()){
							setMessage(5, 1,activityInfo.getActivityCode(),"已经插过代码");
							return "";
						}
						//保存强制插码的确认图片
						String updateFilename = updateFile.getOriginalFilename();
						StringBuffer sb = new StringBuffer();
						sb.append(fpm.getUpdateSchedulePath());
						sb.append(activityInfo.getActivityCode());
						sb.append(DateUtil.getDateStr(new Date(),"yyyyMMddhhmmsssss"));
						sb.append(updateFilename.substring(updateFilename.lastIndexOf("."),updateFilename.length()));
	
						InputStream is = updateFile.getInputStream();
						OutputStream os = new FileOutputStream(sb.toString());
						
						FileUtil.saveFile(is, os);
						//备份排期
						backupSchedule(activityInfo.getActivityCode(),sb.toString(),scheduleInfo.getCreateUser());
					
						//将原有的排期信息删除
						scheduleCalendarDao.deleteCalendars(scheduleList);
						scheduleDao.deleteExtendByActCode(activityInfo.getActivityCode());
						scheduleDao.deleteScheduleByActCode(activityInfo.getActivityCode());
					}
				}//end if(!isAddPoint)
				
				activityInfo = scheduleInfo.getActivityInfo();
				activityInfo.setSchedulePath(fileSB.toString());
				
				//更改活动信息
				monitorPlanDao.updateMonitor(activityInfo);
				
			}//end else 找到监测方案
			
			insertSchedule(scheduleList);
			
			return fileSB.toString();
//		} catch (IOException e) {
//			logUtil.logErrorExc(e);
//			e.printStackTrace();
//		}
		
		
	}//end saveSchedule()
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	public void insertSchedule(List<TbAmpBasicScheduleInfo> scheduleList){
		//将新排期信息入库
		scheduleDao.insertSchedules(scheduleList);
		scheduleDao.insertScheduleExtens(scheduleList);
		for (int i = 0; i < scheduleList.size(); i++) {
			 scheduleCalendarDao.insertCalendar(scheduleList.get(i).getCalendarInfo());
		}
	}
	
	
	/**
	 * 后端补排期插码
	 */
	@Override
	public Map<String,Object> afterSchedule(MultipartFile scheduleFile,UserBean user,String after,String before,String center){
		map = new HashMap<String,Object>();
		if(genScheduleMap == null || cusMap.size() == 0){
			init();
		}
		
		int row = 0,column = 0;
		
		try {
			xu = new XlsxUtil(scheduleFile.getInputStream());
			
			checkCellTitile(genScheduleMap);
			
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			
			/////////////////////基础格式如果有误，则不往下校验/////////////////////////////////////
			
			//活动信息校验
			int[] cusNameXY = new int[]{2,1};
			int[] actNameXY = new int[]{3,1};
			int[] actCodeXY = new int[]{5,1};
			int[] actDateXY = new int[]{3,4};
			
			TbAmpBasicActivityInfo activityInfo = checkActivityInfo(cusNameXY, actNameXY, actCodeXY, actDateXY);
			
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			/////////////////////排期活动信息如果有误，则不往下校验/////////////////////////////////////
			activityInfo.setScheduleType(0);
			//读取排期信息
			int lastRow = xu.getLastRow();
			int lastCell = 0;
			List<String[]> lineDataList = new LinkedList<>();	//保存所有排期数据
			String[] lineDataArr = null;						//保存一行排期数据
			
			//普通排期信息从第八行开始，把所有排期数据都读取封装进字符数组
			for (row = 8; row <= lastRow; row++) {
				
				lastCell = xu.getLastCell(row);
				lineDataArr = new String[lastCell];
				for (column = 0; column < lastCell; column++) {	
					lineDataArr[column] = xu.readCellData(row, column).replaceAll(" ","");
				}//end for
				
				lineDataList.add(lineDataArr);
			}//end for
			
			TbAmpBasicScheduleInfo scheduleInfo = null;
			List<TbAmpBasicScheduleInfo> scheduleList = new LinkedList<>();	//有效的排期信息
			TbAmpBasicSchedulCalendarInfo calendarInfo = null;	//排期投放日期
			TbAmpBasicMediaInfo mediaInfo = null;	//媒体信息
			List<TbAmpBasicSchedulCalendarInfo> calendarList = null;	//排期日历
			
			int currentYear = 0,currentMonth = 0,currentDay;	//投放日期
			int readMonth;	//读取的月份
			//判断是否为排期信息，并根据  genSchedule.properties 配置的 fieldName 封装排期属性，以及投放时期安排
			for(row=0;row < lineDataList.size();row++){
				
				lineDataArr = lineDataList.get(row);
				
				//忽略小计以及空行
				if(lineDataArr.length < 4 || ("".equals(lineDataArr[2]) && "".equals(lineDataArr[3]) && "".equals(lineDataArr[4]) && "".equals(lineDataArr[5]))
						||	lineDataArr[4].contains("小计"))
				{continue;}
				
				scheduleInfo = new TbAmpBasicScheduleInfo();
				calendarList = new LinkedList<>();
				
				currentYear = Integer.parseInt(DateUtil.getDateStr(activityInfo.getRealityStartDate(), "yyyy"));
				currentMonth = Integer.parseInt(DateUtil.getDateStr(activityInfo.getRealityStartDate(), "MM"));
				
				for (column = 0; column < lineDataArr.length; column++) {
					//将排期日期前半部分注入bean
					if(column < genFieldNames.length){
						//将信息注入bean
						BeanUtil.setProperties(scheduleInfo,lineDataArr[column], genFieldNames[column]);
					}
					//排期日期注入bean
					else if(!"".equals(lineDataArr[column])){
						//判断投放值是否格式是否正确
						if(!lineDataArr[column].matches("\\d{1,}") && !lineDataArr[column].matches("\\d{1,}\\.\\d{1,}")){
							setMessage(row + 7, column, "投放量有误，投放量只能是数值。");
							continue;
						}
						calendarInfo = new TbAmpBasicSchedulCalendarInfo();
						
						//获取投放月份
						String monthStr = xu.readCellData(5,column).replaceAll("\\D", "");
						if(!monthStr.matches("\\d{1,2}")){
							setMessage(5,column,"月份格式不正确");
							break;
						}
						readMonth = Integer.parseInt(monthStr);
						if(readMonth != currentMonth){
							if(readMonth < currentMonth) {
								++currentYear;
							}
							currentMonth = readMonth;
						}
						
						//获取投放日期
						String readDay = xu.readCellData(7,column).trim();
						currentDay = Integer.parseInt(readDay.substring(0, readDay.indexOf(".")));
						
						//判断日期是否有效
						if(!DateUtil.isLegalDate(currentYear, currentMonth, currentDay)){
							setMessage(row+7, column, "日期格式有误");
							continue;
						}
						
						//排期日历的时间比活动结束时间还大，
						Date putDate = DateUtil.getDate(currentYear,currentMonth,currentDay);
						if(putDate.compareTo(activityInfo.getActivityEndDate()) > 0){
							setMessage(row+7,column,"活动结束日期小于投放日期安排");
							continue;
						}
						calendarInfo.setPutDate(putDate);
						calendarInfo.setMic(scheduleInfo.getExtenInfo().getMaterialRequire());
						try{
							//过滤掉小数点后面的数据
							calendarInfo.setPutValue(lineDataArr[column].substring(0,lineDataArr[column].indexOf(".")));
						}catch(Exception e){
							if(e instanceof InvocationTargetException){
								InvocationTargetException targetExc = (InvocationTargetException)e;
								setMessage(row + 8,column, targetExc.getTargetException().getMessage());
							}
						}//end try - catch
						calendarList.add(calendarInfo);
					}//end if - else
				}//end for
				
				//根据媒体名称查看媒体是否存在
				Map<String, String> generalization = mediaService.getGeneralization();
				//通过泛化名称关系，找到标准媒体名称，
				mediaInfo = mediaService.getMedias(false).get(generalization.get(scheduleInfo.getMedia().getMediaName()));
				
				if(mediaInfo == null){
					setMessage(8,1,scheduleInfo.getMedia().getMediaName().replaceAll("\\n",""),"，未匹配该媒体名称，请进行媒体名称泛化操作。");
					continue;
				}else{
					scheduleInfo.getMedia().setMediaId(mediaInfo.getMediaId());
					// 设置标准化后的媒体名称
					scheduleInfo.getMedia().setMediaName(mediaInfo.getMediaName());
				}
				
				scheduleInfo.setActivityInfo(activityInfo);
				scheduleInfo.setCalendarInfo(calendarList);
				scheduleList.add(scheduleInfo);
				
				//设置物料要求的填写客户生成的监测代码，将此监测代码作为MIC
				scheduleInfo.setMic(scheduleInfo.getExtenInfo().getMaterialRequire());
				scheduleInfo.setMarketingCode(scheduleInfo.getMic());
				scheduleInfo.getExtenInfo().setMic(scheduleInfo.getMic());
				
			}//end for(row < lineDataList.size())
	
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			
			/////////////////////排期详细信息如果有误，则不往下执行/////////////////////////////////////
			//排期数据入库
			//没有排期数据
			if(scheduleList.size()==0){
				setMessage(8, 0, "没有排期数据！");
				return map;
			}
			
			String filePath = saveSchedule(scheduleList, null,scheduleFile.getOriginalFilename(),user,after,before,center,true,false);
			
			if("".equals(filePath)){
				map.put("err", mesList);
				return map;
			}
			
			map.put("filePath", filePath);
			map.put("fileName", filePath.substring(filePath.lastIndexOf("/") + 1,filePath.length() ));
			
			xu.close(filePath);
		} catch (Exception e) {
			//排期数据异常
			if(e instanceof InvocationTargetException){
				InvocationTargetException targetExc = (InvocationTargetException)e;
				setMessage(row + 8,column, targetExc.getTargetException().getMessage());
			}
			
			if(e instanceof NumberFormatException){
				setMessage(row+4,column, "格式不正确");
			}
			
			map.put("err", mesList);
			e.printStackTrace();
//			logUtil.logErrorExc(e);
		}
		
		return map;
	}//end afterSchedule()

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 校验排期基础标题
	 */
	private void checkCellTitile(Map<int[],String> scheduleMap){
		
		int[] xy = null;
		String value = "";
		
		Set<int[]> xySet = scheduleMap.keySet();
		Iterator<int[]> iterator = xySet.iterator();
		String cellVal = "";
		while (iterator.hasNext()) {
			xy = iterator.next();
			value = scheduleMap.get(xy);
			
			//排期对应位置是否为配置的项
			cellVal = xu.readCellData(xy[0], xy[1]).toUpperCase().trim();
			if(!cellVal.contains(value) && !cellVal.matches(value)){
				setMessage(xy[0],xy[1], value);
			}
		}//end while(iterator.hasNext())
		
	}//end checkCellTitile()
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 校验活动信息
	 * @param cusNameXY	客户名称坐标
	 * @param actNameXY	活动名称坐标
	 * @param actCodeXY	活动编号坐标
	 * @param actDateXY	投放时期坐标
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private TbAmpBasicActivityInfo checkActivityInfo(int[] cusNameXY,int[] actNameXY,int[] actCodeXY,int[] actDateXY){
		
		String cusName = xu.readCellData(cusNameXY[0], cusNameXY[1]).trim();	//投放单位
		String actName = xu.readCellData(actNameXY[0], actNameXY[1]).trim();	//活动名称
		String actCode = xu.readCellData(actCodeXY[0], actCodeXY[1]).trim();	//活动编号
		String actDate = xu.readCellData(actDateXY[0], actDateXY[1]).trim();	//投放时期
		
		//投放单位校验
		TbAmpBasicCustomerInfo customerInfo = cusMap.get(cusName);
		//对应位置投放单位未填写
		if(customerInfo==null){
			setMessage(cusNameXY[0], cusNameXY[1],cusName,"投放单位不存在");
		}
		//投放单位与活动编号是否匹配
		else if(!actCode.replaceAll("\\d","").equals(customerInfo.getCustomerCode())){
			setMessage(actCodeXY[0], actCodeXY[1],cusName,"与",actCode,"不匹配");
		}
		
		//判断投放时期是否符合规范
		String sDateStr = "",eDateStr = "";
		String dateFormat = "yyyy.MM.dd";
		if(actDate.matches("\\d{4}\\.\\d{2}\\.\\d{2}\\-\\d{4}\\.\\d{2}\\.\\d{2}")){
			String[] dates = actDate.split("-");
			sDateStr = dates[0];
			eDateStr = dates[1];
		}else if(actDate.matches("\\d{4}\\.\\d{2}\\.\\d{2}")){
			sDateStr = actDate;
			eDateStr = actDate;
		}
		if(!"".equals(sDateStr)){
			if(!DateUtil.isLegalDate(sDateStr,"\\.")){
				sDateStr = "";
			}
			if(!DateUtil.isLegalDate(eDateStr,"\\.")){
				eDateStr = "";
			}
		}
		
		Date sDate = DateUtil.getDate(sDateStr, dateFormat);
		Date eDate = DateUtil.getDate(eDateStr, dateFormat);
		
		if(sDate == null || eDate == null){
			setMessage(actDateXY[0], actDateXY[1], actDate,"格式或者日期值不正确，格式：",dateFormat,"-"+dateFormat);
		}else{
			//活动结束时间不能小于当前时间，并且不能小于活动开始时间
			if(DateUtil.isPast(eDateStr, dateFormat) < 0 || sDate.compareTo(eDate) > 0){
				setMessage(actDateXY[0], actDateXY[1], actDate,"活动结束时间不能小于当前时间，并且不能小于活动开始时间");
			}else if(new Date().getYear() - sDate.getYear() > 1){	//活动开始时间不能比当前时间大于一年
				setMessage(actDateXY[0], actDateXY[1], actDate,"活动开始时间不能比当前时间大于一年");
			}
		}
		
		TbAmpBasicActivityInfo activityInfo = new TbAmpBasicActivityInfo();
		activityInfo.setActivityCode(actCode);
		activityInfo.setActivityName(actName);
		activityInfo.setActivityEndDate(eDate);
		activityInfo.setRealityStartDate(sDate);
		activityInfo.setCustomer(customerInfo);
		
		return activityInfo;
	}//end checkActivityInfo()
	
	public static void printArr(Object[] objs){
		for (int i = 0; i < objs.length; i++) {
			System.out.print(objs[i] + "\t");
		}
		System.out.println();
	}
	
	/**
	 * 集团排期插码
	 */
	@Override
	public Map<String,Object> jtSchedule(MultipartFile scheduleFile,MultipartFile updateFile,UserBean user,String after,String before,String center,boolean isAddPoint){
		map = new HashMap<String,Object>();
		if(jtScheduleMap == null || cusMap.size() == 0){
			init();
		}
		
		int row = 0,column = 0;
		
		try {
			xu = new XlsxUtil(scheduleFile.getInputStream());
			
			checkCellTitile(jtScheduleMap);
			
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			
			/////////////////////基础格式如果有误，则不往下校验/////////////////////////////////////
			
			//活动信息校验
			int[] cusNameXY = new int[]{2,1};
			int[] actNameXY = new int[]{3,1};
			int[] actCodeXY = new int[]{5,1};
			int[] actDateXY = new int[]{3,5};
			
			TbAmpBasicActivityInfo activityInfo = checkActivityInfo(cusNameXY, actNameXY, actCodeXY, actDateXY);
			
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			
			/////////////////////排期活动信息如果有误，则不往下校验/////////////////////////////////////
			activityInfo.setScheduleType(1);
			//读取排期信息
			int lastRow = xu.getLastRow();
			List<String[]> lineDataList = new LinkedList<>();	//保存所有排期数据
			String[] lineDataArr = null;	//保存一行排期数据
			Map<String[],Integer> lineDataMap = new HashMap<>(); //保存所有排期数据以及 对应的分组编号
			int scheduleMinCol = XlsxUtil.getColumnNum("W"); //有效最小的排期列数
			int clickAvgCol = XlsxUtil.getColumnNum("L"); //点击预估所在 列
			int exposureAvgCol = XlsxUtil.getColumnNum("M"); //曝光预估所在列
			int startRow = 7;
			
			//集团排期信息从第7行开始，把所有排期数据都读取封装进字符数组
			readSchedule2Arr(xu, lastRow, startRow, scheduleMinCol, lineDataList, lineDataMap, clickAvgCol, exposureAvgCol);
			
			TbAmpBasicScheduleInfo scheduleInfo = null;
			List<TbAmpBasicScheduleInfo> scheduleList = new LinkedList<>();	//有效的排期信息
			List<TbAmpBasicScheduleInfo> scheduleMicNos = new LinkedList<>();	//存储需要变更点位顺序编号的点位
			TbAmpBasicSchedulCalendarInfo calendarInfo = null;	//排期投放日期
			TbAmpBasicMediaInfo mediaInfo = null;	//媒体信息
			List<TbAmpBasicSchedulCalendarInfo> calendarList = null;	//排期日历
			int currentYear = 0,currentMonth = 0,currentDay;	//投放日期
			int readMonth;	//读取的月份
//			String[] point = null; //存放频控信息
			//判断是否为排期信息，并根据  jtSchedule.properties 配置的 fieldName 封装排期属性，以及投放时期安排
			for(row=0;row < lineDataList.size();row++){
				
				lineDataArr = lineDataList.get(row);
				scheduleInfo = new TbAmpBasicScheduleInfo();
				
				//忽略小计以及空行
				if(lineDataArr.length < 7 || ("".equals(lineDataArr[2]) && "".equals(lineDataArr[3]) && "".equals(lineDataArr[1]))
						||	(lineDataArr[5].contains("小计") && "".equals(lineDataArr[4]))
						||	(lineDataArr[5].contains("总计") && "".equals(lineDataArr[4]))
						||  (lineDataArr[5].contains("合计") && "".equals(lineDataArr[4]))
						||	(lineDataArr[4].contains("中国移动") && "".equals(lineDataArr[6]) )
						||	(lineDataArr[4].contains("签章") && "".equals(lineDataArr[6]) )	 
						||	(lineDataArr[4].contains("日期") && "".equals(lineDataArr[6]) )
				){continue;}
				
				//是增加点位则忽略识别码有值的点位
				if(lineDataArr.length < jtFieldNames.length || (isAddPoint && !"".equals(lineDataArr[XlsxUtil.getColumnNum("Q")]))){
					scheduleInfo.setMic(lineDataArr[XlsxUtil.getColumnNum("Q")]);
					scheduleInfo.setMicNo(row);
					scheduleMicNos.add(scheduleInfo);
					continue;
				}
				
				
				calendarList = new LinkedList<>();
				scheduleInfo.setCreateUser(user.getUSER_ID()+"");
				
				currentYear = Integer.parseInt(DateUtil.getDateStr(activityInfo.getRealityStartDate(),"yyyy"));
				currentMonth = Integer.parseInt(DateUtil.getDateStr(activityInfo.getRealityStartDate(), "MM"));
				
				//设置分组编号
				scheduleInfo.setGroupId(lineDataMap.get(lineDataArr));
				
				for (column = 0; column < lineDataArr.length; column++) {
					//将排期日期前半部分注入bean
					if(column < jtFieldNames.length){
						//查看排期点位投放位置，投放形式中是否带有频控、频次字眼
						if(column == 4 || column == 5){
							if(lineDataArr[column].contains("频控") || lineDataArr[column].contains("频次")){
//								频控次数	频控方法规则	频控周期
//								13	14	15
								if("".equals(lineDataArr[13]) || "".equals(lineDataArr[14]) || "".equals(lineDataArr[15])){
									setMessage(row+7, column, "有频控、频次字眼，但是对应频控属性未填写完整。");
								}
							}//end if
						}//end 频控处理
						
						//将信息注入bean
						BeanUtil.setProperties(scheduleInfo,lineDataArr[column], jtFieldNames[column]);
					}
					//排期日期注入bean
					else if(!"".equals(lineDataArr[column])){
						//判断投放值是否格式是否正确
						if(!lineDataArr[column].matches("\\d{1,}") && !lineDataArr[column].matches("\\d{1,}\\.\\d{1,}")){
							setMessage(row + 6, column, "投放量有误，投放量只能是数值。");
							continue;
						}
						calendarInfo = new TbAmpBasicSchedulCalendarInfo();
						
						//获取投放月份
						readMonth = Integer.parseInt(xu.readCellData(4,column).replaceAll("\\D", ""));
						if(readMonth != currentMonth){
							if(readMonth < currentMonth){
								++currentYear;
							}
							currentMonth = readMonth;
						}
						
						//获取投放日期
						String readDay = xu.readCellData(6,column).trim();
						if(readDay.indexOf(".") > 0){
							currentDay = Integer.parseInt(readDay.substring(0, readDay.indexOf(".")));
						}else{
							currentDay = Integer.parseInt(readDay);
						}
						//判断日期是否有效
						if(!DateUtil.isLegalDate(currentYear, currentMonth, currentDay)){
							setMessage(6, column, "日期格式有误");
							continue;
						}
						
						//排期日历的时间比活动结束时间还大，
						Date putDate = DateUtil.getDate(currentYear,currentMonth,currentDay);
						if(putDate.compareTo(activityInfo.getActivityEndDate()) > 0){
							setMessage(6,column,"活动结束日期小于投放日期安排");
							continue;
						}
						calendarInfo.setPutDate(putDate);
						try{
							//过滤掉小数点后面的数据
							calendarInfo.setPutValue(lineDataArr[column].substring(0,lineDataArr[column].indexOf(".")));
						}catch(Exception e){
							if(e instanceof InvocationTargetException){
								InvocationTargetException targetExc = (InvocationTargetException)e;
								setMessage(row + startRow,column, targetExc.getTargetException().getMessage());
							}
						}//end try - catch
						calendarList.add(calendarInfo);
					}//end if - else
				}//end for
				
				//根据媒体名称查看媒体是否存在
				Map<String, String> generalization = mediaService.getGeneralization();
				//通过泛化名称关系，找到标准媒体名称，
				mediaInfo = mediaService.getMedias(false).get(generalization.get(scheduleInfo.getMedia().getMediaName()));
				if(mediaInfo == null){
					setMessage(row + startRow,1,scheduleInfo.getMedia().getMediaName(),"，未匹配该媒体名称，请进行媒体名称泛化操作。");
					continue;
				}else{
					scheduleInfo.getMedia().setMediaId(mediaInfo.getMediaId());
					// 设置标准化后的媒体名称
					scheduleInfo.getMedia().setMediaName(mediaInfo.getMediaName());
				}
				
				//设置数据口径
				setDataCaliber(scheduleInfo,lineDataArr[4],lineDataArr[5]);
				
				//确定是否需要频控
				this.setIsFrequency(scheduleInfo);
				
				scheduleInfo.setMicNo(row);	//设置排期序列号
				scheduleInfo.getExtenInfo().setArea("全国");;
				scheduleInfo.setActivityInfo(activityInfo);
				scheduleInfo.setCalendarInfo(calendarList);
				scheduleList.add(scheduleInfo);
				//设置活动监测代码
				setScheduleMonitorCode(scheduleInfo);
				//将生成的监测代码写回对应的排期信息
				writeJTScheduleMonitorCode(scheduleInfo, row + startRow,xu);
			}//end for(row < lineDataList.size())
	
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			
			/////////////////////排期详细信息如果有误，则不往下执行/////////////////////////////////////
			//排期数据入库
			//没有排期数据
			if(scheduleList.size()==0){
				setMessage(7, 0, "没有排期数据！");
				return map;
			}
			
			//排期入库
			String filePath = saveSchedule(scheduleList, updateFile,scheduleFile.getOriginalFilename(),user,after,before,center,false,isAddPoint);
			//更改排期序号
			if(isAddPoint){
				scheduleDao.updateMicNo(scheduleMicNos);
			}
			
			if("".equals(filePath)){
				map.put("err", mesList);
				return map;
			}
			
			map.put("filePath", filePath);
			map.put("fileName", filePath.replace(fpm.getScheduleFilePath(), ""));
			
			xu.close(filePath);
		} catch (Exception e) {
			//排期数据异常
			if(e instanceof InvocationTargetException){
				InvocationTargetException targetExc = (InvocationTargetException)e;
				setMessage(row + 7,column, targetExc.getTargetException().getMessage());
			}
			
			else if(e instanceof NumberFormatException){
				setMessage(row+7,column, "格式不正确");
			}else if(e instanceof ScheduleException){
				mesList.add(row + 8 + "行，" + e.getMessage());
			}
			else{
				mesList.add("读取文件出错，请联系管理员！");
			}
			
			map.put("err", mesList);
			e.printStackTrace();
//			logUtil.logErrorExc(e);
		}
		
		return map;
	}//end jtSchedule()
	
	///////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 百度关键词排期插码
	 */
	@Override
	public Map<String,Object> keySchedule(MultipartFile scheduleFile,MultipartFile updateFile,UserBean user,String after,String before,String center,boolean isAddPoint){
		map = new HashMap<String,Object>();
		if(keyScheduleMap == null || cusMap.size() == 0){
			init();
		}
		
		int row = 0,column = 0;
		
		try {
			xu = new XlsxUtil(scheduleFile.getInputStream());
			checkCellTitile(keyScheduleMap);
			
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			
			/////////////////////基础格式如果有误，则不往下校验/////////////////////////////////////
			
			//活动信息校验
			int[] cusNameXY = new int[]{0,1};
			int[] actNameXY = new int[]{1,1};
			int[] actCodeXY = new int[]{2,1};
			int[] actDateXY = new int[]{1,3};
			
			TbAmpBasicActivityInfo activityInfo = checkActivityInfo(cusNameXY, actNameXY, actCodeXY, actDateXY);
			
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			activityInfo.setScheduleType(2);
			/////////////////////排期活动信息如果有误，则不往下校验/////////////////////////////////////
			
			//读取排期信息
			int lastRow = xu.getLastRow();
			int lastCell = 0;
			List<String[]> lineDataList = new LinkedList<>();	//保存所有排期数据
			String[] lineDataArr = null;	//保存一行排期数据
			
			//关键词排期信息从第五行开始，把所有排期数据都读取封装进字符数组
			for (row = 4; row <= lastRow; row++) {
				
				lastCell = xu.getLastCell(row);
				if(lastCell <= 0){continue;}
				lineDataArr = new String[lastCell];
				
				for (column = 0; column < lastCell; column++) {	
					lineDataArr[column] = xu.readCellData(row, column).replaceAll(" ","");
				}//end for
				
				lineDataList.add(lineDataArr);
			}//end for
			
			TbAmpBasicScheduleInfo scheduleInfo = null;
			List<TbAmpBasicScheduleInfo> scheduleList = new LinkedList<>();	//有效的排期信息
			//判断是否为排期信息，并根据  jtSchedule.properties 配置的 fieldName 封装排期属性
			for(row=0;row < lineDataList.size();row++){
				
				lineDataArr = lineDataList.get(row);
				
				//忽略空行
				if(lineDataArr.length < 4 || ("".equals(lineDataArr[0]) && "".equals(lineDataArr[1]) && "".equals(lineDataArr[2])))
				{continue;}
				
				//将排期PC端部分注入bean
				if(!"".equals(lineDataArr[3].trim())){
					
					//是增加点位则忽略识别码有值的点位
//					if(lineDataArr.length < pcfieldName.length || (isAddPoint && !"".equals(lineDataArr[XlsxUtil.getColumnNum("H")]))){
					//if(isAddPoint && !"".equals(lineDataArr[XlsxUtil.getColumnNum("H")])){
					if(isAddPoint && lineDataArr.length > 5 && !"".equals(lineDataArr[XlsxUtil.getColumnNum("H")])){
						continue;
					}
					
					scheduleInfo = new TbAmpBasicScheduleInfo();
					scheduleInfo.setCreateUser(user.getUSER_ID()+"");
					for (column = 0; column < pcfieldName.length; column++) {
						if(lineDataArr.length <= column){continue;}
						BeanUtil.setProperties(scheduleInfo,lineDataArr[column], pcfieldName[column]);
					}
					//根据媒体名称查看媒体是否存在
					initKey(scheduleInfo,true);
					
					scheduleInfo.setActivityInfo(activityInfo);
					scheduleList.add(scheduleInfo);
					
					//设置活动监测代码
					setScheduleMonitorCode(scheduleInfo);
					
					//短代码
					xu.writeCellData(scheduleInfo.getMic(), row + 4,7);
					//点击监测代码
					xu.writeCellData(scheduleInfo.getExtenInfo().getClickUrl(), row + 4,5);
				}
				
				//将排期MOBILE端部分注入bean
				if(lineDataArr.length > 4 && !"".equals(lineDataArr[4].trim())){
					
					//是增加点位则忽略识别码有值的点位
//					if(lineDataArr.length < mobilefieldName.length || (isAddPoint && !"".equals(lineDataArr[XlsxUtil.getColumnNum("I")]))){
					if(isAddPoint && lineDataArr.length > 5 && !"".equals(lineDataArr[XlsxUtil.getColumnNum("I")])){
						continue;
					}
					scheduleInfo = new TbAmpBasicScheduleInfo();
					scheduleInfo.setCreateUser(user.getUSER_ID()+"");
					for (column = 0; column < mobilefieldName.length; column++) {
						if("".equals(mobilefieldName[column]) || lineDataArr.length <= column){continue;}
						//忽略ULR-PC
						if(column == 3){ ++column;}	
						BeanUtil.setProperties(scheduleInfo,lineDataArr[column], mobilefieldName[column]);
					}
					//初始化百度关键词基本信息
					initKey(scheduleInfo,false);
					
					scheduleInfo.setActivityInfo(activityInfo);
					scheduleList.add(scheduleInfo);
					
					//设置活动监测代码
					setScheduleMonitorCode(scheduleInfo);
					
					//短代码
					xu.writeCellData(scheduleInfo.getMic(), row + 4,8);
					//点击监测代码
					xu.writeCellData(scheduleInfo.getExtenInfo().getClickUrl(), row + 4,6);
				}
				
			}//end for(row < lineDataList.size())
	
			if(mesList.size() != 0){map.put("err", mesList); return map;}
			
			/////////////////////排期详细信息如果有误，则不往下执行/////////////////////////////////////
			//排期数据入库
			//没有排期数据
			if(scheduleList.size()==0){
				setMessage(5, 0, "没有排期数据！");
				return map;
			}
			
			String filePath = saveSchedule(scheduleList, updateFile,scheduleFile.getOriginalFilename(),user,after,before,center,false,isAddPoint);
			
			if("".equals(filePath)){
				map.put("err", mesList);
				return map;
			}
			
			map.put("filePath", filePath);
			map.put("fileName", filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length()));
			
			xu.close(filePath);
		} catch (Exception e) {
			//排期数据异常
			if(e instanceof InvocationTargetException){
				InvocationTargetException targetExc = (InvocationTargetException)e;
				setMessage(row + 4,column, targetExc.getTargetException().getMessage());
			}
			
			map.put("err", mesList);
			e.printStackTrace();
//			logUtil.logErrorExc(e);
		}
		
		return map;
	}//end keySchedule()
	
	/**
	 * 初始化百度关键词信息
	 * @param scheduleInfo 需要初始化的点位
	 * @param isPc true PC端		False Mobile端口
	 * @throws ScheduleException 
	 */
	private void initKey(TbAmpBasicScheduleInfo  scheduleInfo,boolean isPc) throws ScheduleException{
		
		TbAmpBasicMediaInfo mediaInfo = mediaService.getMedias(false).get("百度");
		scheduleInfo.setMedia(new TbAmpBasicMediaInfo());
		scheduleInfo.getMedia().setMediaId(mediaInfo.getMediaId());
		scheduleInfo.getMedia().setMediaName("百度");
		scheduleInfo.setTerminalType(isPc ? "SEM_PC" : "SEM_MOBILE");
		scheduleInfo.setUnit("天");
		
		scheduleInfo.getExtenInfo().setArea("全国");
		scheduleInfo.getExtenInfo().setSupportClick("是");
		scheduleInfo.getExtenInfo().setSupportExposure("否");
		
		//设置数据口径
		this.setDataCaliber(scheduleInfo, "", "");
		
		//确定是否需要频控
		scheduleInfo.setIsFrequency("0");
		
		//设置关键词标识
		scheduleInfo.setIsKeySchedule("1");
		
		scheduleInfo.setCalendarInfo(new LinkedList<TbAmpBasicSchedulCalendarInfo>());
	}//end initKey
	
	//////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 根据活动编号下载排期
	 */
	@Override
	public TbAmpBasicActivityInfo downloadSchedule(String activityCode) {
		
		TbAmpBasicActivityInfo activityInfo = monitorPlanDao.findByCode(activityCode);
		
		return activityInfo;
	}//end downloadSchedule()
	
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 备份排期信息
	 */
	@SuppressWarnings("rawtypes")
	public void backupSchedule(String activityCode,String backupImgPath,String backupUser){
		
		List<TbAmpBasicScheduleInfo> scheduleInfos = scheduleDao.findScheduleByActCode(activityCode);
		TbAmpBasicScheduleInfo scheduleInfo = null;
		List<TbAmpBasicSchedulCalendarInfo> calendarInfos = null;
		TbAmpBasicSchedulCalendarInfo calendarInfo = null;
		int i,j;
		String year = "",month= "";
		List<TbAmpBasicScheduleInfoBackup> backupList = new LinkedList<>();
		TbAmpBasicScheduleInfoBackup backup = null;
		
		Map<String,Map> micMap = null;
		Map<String,Map> yearMap = null;
		Map<String,Map> monthMap = null;
		Map<String,String> dayMap = null;
		/**
		 * 将要投放时间安排拼接成这样的形式。
		 {mic:{
			   	2015:{
			     	11:{01:value,
			          	02:value,
			          	03:value},
			      	12:{04:value}
			     },
			    2016:{
			    	01:{01:value,
			          	02:value,
			          	03:value},
			      	02:{04:value}
			     }
			   }
		  }
		 */
		for (i = 0; i < scheduleInfos.size(); i++) {
			
			scheduleInfo = scheduleInfos.get(i);
			backup = new TbAmpBasicScheduleInfoBackup();
			micMap = new HashMap<>();
			yearMap = new HashMap<>();
			monthMap = null;
			dayMap = null;
			year = "";month= "";
			
			calendarInfos = scheduleInfo.getCalendarInfo();
			
			for (j = 0; j < calendarInfos.size(); j++) {
				calendarInfo = calendarInfos.get(j);
				if(!year.equals(DateUtil.getDateStr(calendarInfo.getPutDate(), "yyyy"))){
					year = DateUtil.getDateStr(calendarInfo.getPutDate(), "yyyy");
					monthMap = new HashMap<>();
					micMap.put(scheduleInfo.getMic(), yearMap);
					yearMap.put(year, monthMap);
				}
				if(!month.equals(DateUtil.getDateStr(calendarInfo.getPutDate(), "MM"))){
					month = DateUtil.getDateStr(calendarInfo.getPutDate(), "MM");
					dayMap = new HashMap<>();
					monthMap.put(month, dayMap);
				}
				dayMap.put(DateUtil.getDateStr(calendarInfo.getPutDate(), "dd"), calendarInfo.getPutValue());
			}//end for (j < calendarInfos.size())
			
			 backup.setPutDate(micMap.toString().replaceAll("=", "\\:").replaceAll(" ", ""));
			 backup.setBackupImg(backupImgPath);
			 Schedule2Backup(scheduleInfo,backup);
			 backupList.add(backup);
		}//end for(i < scheduleInfos.size())
		
		scheduleInfoBackupDao.scheduleBackup(backupList,backupUser);
		
	}//end backupSchedule
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * 设置普通排期的营销代码，点击监测代码，曝光监测代码
	 * @return
	 * @throws InterruptedException 
	 */
	public void setScheduleMonitorCode(TbAmpBasicScheduleInfo scheduleInfo) throws InterruptedException{
		String mic = "";
		StringBuffer sb = new StringBuffer(20);
		//获取营销代码，未加密: 日期-活动编号C媒体编号P物料编号   格式：1510-01HUB15092501C123P412
		sb.append(DateUtil.getDateStr(new Date(), "yyMM-dd"));
		sb.append(scheduleInfo.getActivityInfo().getActivityCode());
		sb.append("C");
		sb.append(scheduleInfo.getMedia().getMediaId());
		sb.append("P");
		sb.append(scheduleInfoMaterialSeqDao.nextval());
		sb.append("T");
		sb.append(System.currentTimeMillis() % 86400000);
		scheduleInfo.setMarketingCode(sb.toString());
		
		Thread.sleep(1);
		
		//设置营销代码（加密）
		try {
			mic = MonitoringCodeTool.createMic(sb.toString());
			scheduleInfo.setMic(mic);
			List<TbAmpBasicSchedulCalendarInfo> calendarInfo = scheduleInfo.getCalendarInfo();
			if(calendarInfo != null){
				for (int i = 0; i < calendarInfo.size(); i++) {
					calendarInfo.get(i).setMic(mic);
				}
			}
			
			scheduleInfo.getExtenInfo().setMic(mic);
			
		} catch (Exception e) {
			e.printStackTrace();
			logUtil.logErrorExc(e);
		}
		//生成点击代码
		scheduleInfo.getExtenInfo().setClickUrl(
			MonitoringCodeTool.clickUrl(
				scheduleInfo.getUrlPc(), 
				mic, 
				scheduleInfo.getMedia().getMediaName(),
				scheduleInfo.getExtenInfo().getPointLocation(),
				scheduleInfo.getExtenInfo().getPutFunction(),
				scheduleInfo.getExtenInfo().getSupportClick(), 
				scheduleInfo.getTerminalType()
			)
		);
		
		//生成曝光代码
		scheduleInfo.getExtenInfo().setExposureUrl(
		MonitoringCodeTool.exposureUrl(
					mic, 
					scheduleInfo.getMedia().getMediaName(),
					scheduleInfo.getExtenInfo().getPointLocation(),
					scheduleInfo.getExtenInfo().getPutFunction(),
					scheduleInfo.getExtenInfo().getSupportExposure(), 
					scheduleInfo.getTerminalType()
				)
		);
	}//end setScheduleMonitorCode()
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 将监测代码写会对应的普通排期区域
	 * @param 
	 * 		scheduleInfo
	 * 		row:点位所在行
	 */
	public void writeGenScheduleMonitorCode(TbAmpBasicScheduleInfo scheduleInfo,int row,XlsxUtil xu){
		//后端监测代码
		xu.writeCellData(MonitoringCodeTool.afterUrl(scheduleInfo.getUrlPc(), scheduleInfo.getMic()), row, 3);
		//短代码
		xu.writeCellData(scheduleInfo.getMic(), row,17);
		//曝光监测代码
		xu.writeCellData(scheduleInfo.getExtenInfo().getExposureUrl(), row,18);
		//点击监测代码
		xu.writeCellData(scheduleInfo.getExtenInfo().getClickUrl(), row,19);
		
	}//end writeGenScheduleMonitorCode()
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 将监测代码写会对应的集团排期区域
	 * @param 
	 * 		scheduleInfo
	 * 		row:点位所在行
	 */
	public void writeJTScheduleMonitorCode(TbAmpBasicScheduleInfo scheduleInfo,int row,XlsxUtil xu){
		
		//后端监测代码
		xu.writeCellData(MonitoringCodeTool.afterUrl(scheduleInfo.getUrlPc(), scheduleInfo.getMic()), row, 6);
		//短代码
		xu.writeCellData(scheduleInfo.getMic(), row,16);
		//曝光监测代码
		xu.writeCellData(scheduleInfo.getExtenInfo().getExposureUrl(), row,18);
		//点击监测代码
		xu.writeCellData(scheduleInfo.getExtenInfo().getClickUrl(), row,17);
		
	}//end writeJTScheduleMonitorCode()
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 将scheduleInfo 的信息转移到 backup  Bean中
	 * @param scheduleInfo
	 * @param backup
	 */
	void Schedule2Backup(TbAmpBasicScheduleInfo sche,TbAmpBasicScheduleInfoBackup backup){
		
		backup.setActivityCode(sche.getActivityCode());
		backup.setMarketingCode(sche.getMarketingCode());
		backup.setMic(sche.getMic());
		backup.setMediaID(String.valueOf(sche.getMedia().getMediaId()));
		backup.setUrlPC(sche.getUrlPc());
		backup.setTerminalType(sche.getTerminalType());
		backup.setUnit(sche.getUnit());
		backup.setUrlUpdate(sche.getUrlUpdate());
		backup.setClickAvg(sche.getClickAvg());
		backup.setExposureAvg(sche.getExposureAvg());
		backup.setEnable(Integer.parseInt(sche.getEnable()));
		backup.setPointLocation(sche.getExtenInfo().getPointLocation());
		backup.setPutFunction(sche.getExtenInfo().getPutFunction());
		backup.setMaterialRequire(sche.getExtenInfo().getMaterialRequire());
		backup.setResourceType(sche.getExtenInfo().getResourceType());
		backup.setPlanName(sche.getExtenInfo().getPlanName());
		backup.setUnitName(sche.getExtenInfo().getUnitName());
		backup.setKeyName(sche.getExtenInfo().getKeyName());
		backup.setClearCode(sche.getExtenInfo().getClearCode());
		backup.setResourcePosition(sche.getExtenInfo().getResourcePosition());
		backup.setPutFrequency(sche.getExtenInfo().getPutFrequency());
		backup.setExposureMeterial(sche.getExtenInfo().getExposureMeterial());
		backup.setSupportExposure(sche.getExtenInfo().getSupportExposure());
		backup.setSupportClick(sche.getExtenInfo().getSupportClick());
		backup.setClickMeterial(sche.getExtenInfo().getClickMeterial());
		backup.setClickUrl(sche.getExtenInfo().getClickUrl());
		backup.setExposureUrl(sche.getExtenInfo().getExposureUrl());
		backup.setArea(sche.getExtenInfo().getArea());
		backup.setIsFrequency(sche.getIsFrequency());
		backup.setNumFrequency(sche.getNumFrequency());
		backup.setPeriodFrequency(sche.getPeriodFrequency());
		backup.setFunFrequency(sche.getFunFrequency());
		backup.setSchedulePath(sche.getActivityInfo().getSchedulePath());
		backup.setCreateUser(sche.getCreateUser());
		backup.setGroupId(sche.getGroupId());
	}//end Schedule2Backup
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 错误信息拼接
	 */
	public String setMessage(int x,int y,Object... message){
		
		mesSB.delete(0, mesSB.length());
		
		mesSB.append(x + 1);
		mesSB.append("行,");
		mesSB.append(XlsxUtil.getColumn(y));
		mesSB.append("列：");
		
		for (int i = 0; i < message.length; i++) {
			mesSB.append(message[i]);
		}
		mesSB.append("\\n");
		
		mesList.add(mesSB.toString());
		return mesSB.toString();
	}//end setMessage()
	
	////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 同步跳转表
	 */
	public String downloadMicAndUrl(){
	
		BufferedWriter bw = null;
		List<TbAmpBasicScheduleInfo> micAndUrl = scheduleDao.micAndUrl();
		StringBuffer sb = new StringBuffer();
		String fileName = fpm.getMicAndUrlPath();
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
			
			bw.write("mic,url,click,exposure,sdate,edate");
			bw.newLine();
			
			for (int i = 0; i < micAndUrl.size(); i++) {
				sb.delete(0, sb.length());
				sb.append(micAndUrl.get(i).getMic());
				sb.append(",");
				sb.append(micAndUrl.get(i).getUrlPc());
				sb.append(",");
				sb.append("1".equals(micAndUrl.get(i).getExtenInfo().getSupportClick()) ? "T" : "F");
				sb.append(",");
				sb.append("1".equals(micAndUrl.get(i).getExtenInfo().getSupportExposure()) ? "T" : "F");
				sb.append(",");
				sb.append(",");
				
				bw.write(sb.toString());
				bw.newLine();
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bw != null){
				try {bw.close();} catch (IOException e) {e.printStackTrace();}
			}
		}
		
		return fileName;
	}//end downloadMicAndUrl()
	
	////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 更改排期日历安排
	 */
	public List<String> addScheduleDate(String scheduleType,MultipartFile scheduleFile){
		Map<int[],String> scheMap = null;
		
		int[] cusNameXY;
		int[] actNameXY;
		int[] actCodeXY;
		int[] actDateXY;
		int[] monthXY;
		int startRow, micY, dateY, monthRow, dayRow,micColumn;
		
		try {
			xu = new XlsxUtil(scheduleFile.getInputStream());
			
			if(genScheduleMap == null){
				init();
			}
			
			//校验基础格式
			if("0".equals(scheduleType)){
				scheMap = genScheduleMap;
				cusNameXY = new int[]{2,1};
				actNameXY = new int[]{3,1};
				actCodeXY = new int[]{5,1};
				actDateXY = new int[]{3,4};
				monthXY = new int[]{5,XlsxUtil.getColumnNum("U")};
				startRow = 8; micY = 17; dateY = 20; monthRow = 5; dayRow = 7; micColumn = XlsxUtil.getColumnNum("R");
			}else if("1".equals(scheduleType)){
				scheMap = jtScheduleMap;
				cusNameXY = new int[]{2,1};
				actNameXY = new int[]{3,1};
				actCodeXY = new int[]{5,1};
				actDateXY = new int[]{3,5};
				monthXY = new int[]{4,XlsxUtil.getColumnNum("X")};
				startRow = 7; micY = 16; dateY = 23; monthRow = 4; dayRow = 6; micColumn = XlsxUtil.getColumnNum("Q");
			}else{
				mesList.add("文件格式不正确");
				return mesList;
			}
			
			//校验标题格式
			checkCellTitile(scheMap);
			if(mesList.size() != 0){return mesList;}
			
			//活动信息校验
			TbAmpBasicActivityInfo activityInfo = checkActivityInfo(cusNameXY, actNameXY, actCodeXY, actDateXY);
			if(mesList.size() != 0){return mesList;}
			
//			XlsxUtil xu = new XlsxUtil(scheduleFile.getInputStream());
			
			readScheduleNewDate(xu, activityInfo, startRow, micY, dateY, monthRow, dayRow);
			
			if(mesList.size() != 0){return mesList;}
			
			//同步到数据库
			monitorPlanDao.updateMonitorEdate(activityInfo);
			//将日历写回排期文件中
			writeBackCalendar(activityInfo.getActivityCode(),monthXY,micColumn);
			
		} catch (Exception e) {
			e.printStackTrace();
			logUtil.logErrorExc(e);
			mesList.add("操作失败，请联系系统管理员");
		}
		
		return mesList;
	}//end addScheduleDate()
	
	/**
	 * 将排期日历写回排期文件中
	 * @param actCode	排期日历所属的活动编号
	 * @param micColumn mic所在的列
	 * @param monthXY	日历开始坐标
	 */
	private void writeBackCalendar(String actCode,int[] monthXY,int micColumn){
		List<RefreshCalendarModel> calendarModels = scheduleCalendarDao.findByActCode(actCode);
		RefreshCalendarModel model = null;
		XlsxUtil xlsxUtil = null;
		XSSFCellStyle cellStyle = null;
		int days = 0;
		try {
			//初始化排期日历
			if(calendarModels.size() > 0){
				model = calendarModels.get(0);
				if(model.getSchedulePath() == null){
					return;
				}
				xlsxUtil = new XlsxUtil(model.getSchedulePath());
				cellStyle = xlsxUtil.getCell(monthXY[0], monthXY[1]).getCellStyle();
				
				days = initCalednarMonth(xlsxUtil, model.getMinDate(), model.getMaxDate(),monthXY,cellStyle);
				if(days != 0){
					writeNewCalendar(calendarModels, days, monthXY[0] + 3, monthXY[1], micColumn, model.getMinDate(), xlsxUtil, cellStyle);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(xlsxUtil != null){xlsxUtil.close(null);}
		}
		
	}//end writeBackCalendar()

	
	/**
	 * 将点位新的日历写回到排期文件中
	 * @param calendarModels	点位日历集合
	 * @param days				活动投放天数
	 * @param startRow			日历所在行
	 * @param startColumn		日历所在列
	 * @param micColumn			mic所在列
	 * @param minDate			最小投放日期
	 * @param xlsxUtil			排期文件流
	 */
	private void writeNewCalendar(List<RefreshCalendarModel> calendarModels,int days,int startRow,int startColumn,int micColumn,Date minDate,XlsxUtil xlsxUtil,XSSFCellStyle cellStyle){
		
		RefreshCalendarModel calendarModel = null;
		Set<TbAmpBasicSchedulCalendarInfo> calendars = null;
		Iterator<TbAmpBasicSchedulCalendarInfo> iterator = null;
		TbAmpBasicSchedulCalendarInfo calendarInfo = null;
		
		int day;
		String mic;
		for (int i = startRow; i < xlsxUtil.getLastRow(); i++) {
			startRow ++;
			mic = xlsxUtil.readCellData(i, micColumn);
			calendarModel = searchCalendarModelByMic(calendarModels, mic);
			
			//迭代某个点位的所有投放日期
			if(calendarModel != null){
				calendars = calendarModel.getCalendars();
				
				iterator = calendars.iterator();
				while (iterator.hasNext()) {
					calendarInfo = iterator.next();
					day =(int) ((calendarInfo.getPutDate().getTime() - minDate.getTime()) / 86400000);
					xlsxUtil.writeCellData(Double.valueOf(calendarInfo.getPutValue()),i, startColumn + day);
					xlsxUtil.format(i,startColumn + day ,XlsxUtil.NUMBER_FORMAT_THOUSANDS);
				}
			}//end if
			
			//设置日历样式
			if(!"".equals(mic) && mic.length() > 10 && mic.length() < 30){
				for (int j = 0; j <= days; j++) {
					xlsxUtil.getCell(i, startColumn + j).setCellStyle(cellStyle);
				}
			}//end if 
		}
	}//end writeNewCalendar();

	/**
	 * 在集合列表中找到对应 calendarModels
	 * @param calendarModels
	 * @param mic
	 * @return
	 */
	private RefreshCalendarModel searchCalendarModelByMic(List<RefreshCalendarModel> calendarModels,String mic){
		for (int i = 0; i < calendarModels.size(); i++) {
			if(mic.equals(calendarModels.get(i).getMic())){
				return calendarModels.get(i);
			}
		}
		
		return null;
	}//end searchCalendarModelByMic()
	

	//0 = Sunday 1 = Monday, 2 = Tuesday, 3 = Wednesday, 4 = Thursday, 5 = Friday, 6 = Saturday 7 = Sunday
	public static final String WEEK[] = {"日","一","二","三","四","五","六","日"};
		
	/**
	 * 修改日历后，重新对排期文件日历进行编排
	 * @param xlsxUtil
	 * @param minDate
	 * @param maxDate
	 * @return int 整个活动编号的投放天数
	 */
	@SuppressWarnings("deprecation")
	private int initCalednarMonth(XlsxUtil xlsxUtil,Date minDate,Date maxDate,int[] monthXY,XSSFCellStyle cellStyle){
		
		//清空日历
		xlsxUtil.removeRectangleCell(monthXY[0], monthXY[1], xlsxUtil.getLastRow(),xlsxUtil.getLastCell());
		Date date = null; 
		//没有日历，则
		if(maxDate == null){
			date = new Date();
			xlsxUtil.writeCellData(date.getMonth() + 1 + "月",monthXY[0],monthXY[1],cellStyle);			
			//星期
			xlsxUtil.writeCellData(WEEK[date.getDay()],monthXY[0] + 1,monthXY[1],cellStyle);
			//日期
			xlsxUtil.writeCellData(date.getDate(),monthXY[0] + 2,monthXY[1],cellStyle);
			return 0;
		}
		
		long time = maxDate.getTime() - minDate.getTime();
		int days = (int)(time / 86400000);
		int firstCol, lastCol;
		
		//移动的日期
		date = new Date(minDate.getTime() - 86400000);
		//日历开始的Y轴c
		int month = date.getMonth() + 1;
		String monthStr = month + "月";
		firstCol = monthXY[1];
		xlsxUtil.writeCellData(monthStr,monthXY[0],monthXY[1],cellStyle);
		
		int i = 0;
		for (; i <= days; i++) {
			//往后移动日期
			date.setDate(date.getDate() + 1);
			
			//月份
			if(month != date.getMonth() + 1){
				//合并月份单元格
				lastCol = monthXY[1] + i - 1;
				setMonthRange(xlsxUtil, lastCol, firstCol, monthXY);
				//初始化新的月份
				firstCol = lastCol + 1; 
				month = date.getMonth() + 1;
				monthStr = month + "月";
				xlsxUtil.writeCellData(monthStr,monthXY[0],monthXY[1] + i,cellStyle);
			}//end if(month != date.getMonth() + 1){
			
			//星期
			xlsxUtil.writeCellData(WEEK[date.getDay()],monthXY[0] + 1,monthXY[1] + i,cellStyle);
			//日期
			xlsxUtil.writeCellData(date.getDate(),monthXY[0] + 2,monthXY[1] + i,cellStyle);
		}//end for (; i <= days; i++) 
		
		//对最后的月份进行合并单元格
		lastCol = monthXY[1] + i - 1;
		setMonthRange(xlsxUtil, lastCol, firstCol, monthXY);
		
		return days;
	}//end  initCalednarMonth
	
	/**
	 * 合并月份的单元格
	 * @param xlsxUtil
	 * @param lastCol
	 * @param firstCol
	 * @param monthXY
	 */
	private void setMonthRange(XlsxUtil xlsxUtil,int lastCol,int firstCol,int[] monthXY){
		if((lastCol - firstCol) > 1){
			xlsxUtil.setRangeCell(monthXY[0], firstCol, monthXY[0], lastCol);
			xlsxUtil.setMergedRegionBorder(monthXY[0], firstCol,XlsxUtil.BORDER_THIN,XlsxUtil.BORDER_ALL);
		}
	}
	
	/**
	 * 更改排期预估
	 */
	public List<String> updateForecase(String scheduleType,MultipartFile scheduleFile){
		Map<int[],String> scheMap = null;
		
		int startRow, micY, clickY, exposureY;
		
		try {
			xu = new XlsxUtil(scheduleFile.getInputStream());
			
			if(genScheduleMap == null){
				init();
			}
			
			//校验基础格式
			if("0".equals(scheduleType)){
				scheMap = genScheduleMap;
				startRow = 8; micY = XlsxUtil.getColumnNum("R"); clickY = XlsxUtil.getColumnNum("J"); 
				exposureY = XlsxUtil.getColumnNum("K");
			}else if("1".equals(scheduleType)){
				scheMap = jtScheduleMap;
				startRow = 7; micY = XlsxUtil.getColumnNum("Q"); clickY = XlsxUtil.getColumnNum("L"); 
				exposureY =XlsxUtil.getColumnNum("M"); 
			}else{
				mesList.add("文件格式不正确");
				return mesList;
			}
			
			//校验标题格式
			checkCellTitile(scheMap);
			if(mesList.size() != 0){return mesList;}
			
//			XlsxUtil xu = new XlsxUtil(scheduleFile.getInputStream());
			rewScheduleNewForecast(xu, startRow, micY, clickY, exposureY);
//			readScheduleNewDate(xu, activityInfo, startRow, micY, dateY, monthRow, dayRow);
			
			if(mesList.size() != 0){return mesList;}
		} catch (Exception e) {
			e.printStackTrace();
			logUtil.logErrorExc(e);
			mesList.add("操作失败，请联系系统管理员");
		}
		
		return mesList;
	}//end updateForecase()


	/**
	 * 更改排期预估
	 * @param xu
	 * @param startRow	开始读取的行
	 * @param micY		mic所在的列
	 * @param clickY	点击预估所在列
	 * @param exposureY	曝光预估所在列
	 */
	public void rewScheduleNewForecast(XlsxUtil xu,int startRow,int micY,int clickY,int exposureY){
		
		List<TbAmpBasicScheduleInfo> schedules = new LinkedList<>();
		TbAmpBasicScheduleInfo scheduleInfo;
		String mic = "";
		int column = 0;
		try {
			int lastRow = xu.getLastRow();
			
			//从开始行遍历每行数据
			for (; startRow <= lastRow; startRow++) {
				column = micY;
				mic = xu.readCellData(startRow, micY).trim();
				
				//忽略短代码为空的行
				if("".equals(mic) || mic.length() < 18){continue;}
				
				scheduleInfo = new TbAmpBasicScheduleInfo();
				scheduleInfo.setMic(mic);
				column = clickY;
				scheduleInfo.setClickAvg(xu.readCellData(startRow, clickY).trim());
				column = exposureY;
				scheduleInfo.setExposureAvg(xu.readCellData(startRow, exposureY).trim());
				
				schedules.add(scheduleInfo);
			}//end for (; startRow <= lastRow; startRow++)
			
			//更改预估值
			scheduleDao.updateClickAndExposureAvg(schedules);
			
		} catch (ScheduleException e){
			setMessage(startRow, column, e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			logUtil.logErrorExc(e);
			mesList.add("系统错误，请联系管理员");
		}
	}//end setScheduleNewForecast()
	
	
	/**
	 * 读取排期延长日期，并写入数据库
	 * @return
	 */
	public void readScheduleNewDate(XlsxUtil xu,TbAmpBasicActivityInfo activityInfo,int startRow,int micY,int dateY,int monthRow,int dayRow){
		
		TbAmpBasicSchedulCalendarInfo calendarInfo = null;
		List<TbAmpBasicSchedulCalendarInfo> calendarList = null;
		List<TbAmpBasicScheduleInfo> schedulelList = new LinkedList<>();
		TbAmpBasicScheduleInfo scheduleInfo = null;
		
		int row=0,column=0;
		String mic;
		int lastRow,lastColumn;
		int currentYear = 0,currentMonth = 0,currentDay;	//投放日期
		int readMonth;	//读取的月份
		String val;
		Date putDate;
		try {
			
			lastRow = xu.getLastRow();
			for (row = startRow; row <= lastRow; row++) {
				
				mic = xu.readCellData(row, micY).trim();
				
				//忽略短代码为空的行
				if("".equals(mic) || mic.length() < 18){continue;}
				calendarList = new LinkedList<>();
				scheduleInfo = new TbAmpBasicScheduleInfo();
				scheduleInfo.setMic(mic);
				
				lastColumn = xu.getLastCell(row);
				currentYear = Integer.parseInt(DateUtil.getDateStr(activityInfo.getRealityStartDate(),"yyyy"));
				currentMonth = Integer.parseInt(DateUtil.getDateStr(activityInfo.getRealityStartDate(), "MM"));
				
				//遍历投放日期
				for(column = dateY;column < lastColumn;column++){
					val = xu.readCellData(row, column);
					//忽略投放量为空日期
					if("".equals(val)){continue;}
					
					calendarInfo = new TbAmpBasicSchedulCalendarInfo();
					calendarInfo.setMic(mic);
					calendarInfo.setPutValue(val.substring(0,val.indexOf(".")));
					//获取投放月份
					readMonth = Integer.parseInt(xu.readCellData(monthRow,column).replaceAll("\\D", ""));
					if(readMonth != currentMonth){
						if(readMonth < currentMonth){
							++currentYear;
						}
						currentMonth = readMonth;
					}
					
					//获取投放日期
					String readDay = xu.readCellData(dayRow,column).trim();
					currentDay = Integer.parseInt(readDay.substring(0, readDay.indexOf(".")));
					
					//判断日期是否有效
					if(!DateUtil.isLegalDate(currentYear, currentMonth, currentDay)){
						setMessage(dayRow, column, "日期格式有误");
						continue;
					}
					
					putDate = DateUtil.getDate(currentYear,currentMonth,currentDay);
					if(putDate.compareTo(activityInfo.getActivityEndDate()) > 0){
						setMessage(row,column,"活动结束时间小于投放时间");
					}
					
					calendarInfo.setPutDate(putDate);
					calendarList.add(calendarInfo);
				}//end for
				
				scheduleInfo.setCalendarInfo(calendarList);
				schedulelList.add(scheduleInfo);
			}//end for
			
			if(mesList.size() > 0){
				return ;
			}
			
			//排期表中未找到相关识别码
			if(schedulelList.size() == 0){
				mesList.add("排期表中未找到相关识别码！");
				return ;
			}
			
			//更改活动结束时间
			monitorPlanDao.updateMonitorEdate(activityInfo);
			
			//删除原有的排期日期
			scheduleCalendarDao.deleteCalendars(schedulelList);
			//更改排期点位更新时间
			scheduleCalendarDao.updateTime(schedulelList);
			//新增排期
			for (int i = 0; i < schedulelList.size(); i++) {
				//System.out.println(schedulelList.get(i).getCalendarInfo());
				scheduleCalendarDao.insertCalendar(schedulelList.get(i).getCalendarInfo());
			}
			
		} catch (ScheduleException e){
			setMessage(row, column, e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			logUtil.logErrorExc(e);
			setMessage(row,column,"附近有误,有可能是日历日期没有填写正确，或者投放值没有填写正确");
		}
	}

	/**
	 * 设置数据统计口径
	 * 			0、标准口径
	 * 			1、去重口径
	 * 			2、百度DSP口径
	 * @param scheduleInfo	点位信息
	 * @param putLocation	投放位置
	 * @param putFunction	投放形式
	 */
	private void setDataCaliber(TbAmpBasicScheduleInfo scheduleInfo,String putLocation,String putFunction) {
		
		String mediaName = scheduleInfo.getMedia().getMediaName();
		
		if("百度DSP".equalsIgnoreCase(mediaName) || "百度SSP".equalsIgnoreCase(mediaName) || "百度睿投".equalsIgnoreCase(mediaName)){
			scheduleInfo.setDataCaliber("2");
		}else if(putLocation.contains("对联") || putLocation.contains("悬停") || putFunction.contains("对联") || putFunction.contains("悬停")){
			scheduleInfo.setDataCaliber("1");
		}else{
			scheduleInfo.setDataCaliber("0");
		}
	}//end setDataCaliber
	
	
	/**
	 * 设置是否需要频控
	 * @param scheduleInfo	需要判断是否需要频控的点位
	 * @throws Exception 
	 */
	private void setIsFrequency(TbAmpBasicScheduleInfo scheduleInfo) throws ScheduleException{
		String periodFrequency = scheduleInfo.getPeriodFrequency() == null ? "" : scheduleInfo.getPeriodFrequency() ;
		String numFrequency = scheduleInfo.getNumFrequency() == null ? "" : scheduleInfo.getNumFrequency();
		String funFrequency = scheduleInfo.getFunFrequency() == null ? "" : scheduleInfo.getFunFrequency();
		if(
			!"".equals(periodFrequency) && !"".equals(numFrequency) && !"".equals(funFrequency)
		){
			scheduleInfo.setIsFrequency("1");
		}else{
			if("".equals(periodFrequency) && "".equals(numFrequency) && "".equals(funFrequency)){
				scheduleInfo.setIsFrequency("0");
			}else{
				throw new ScheduleException("请将频控数据填写完整，如果不需要频控，请将其置空！");
			}
		}
	}//end setIsFrequency
	
}