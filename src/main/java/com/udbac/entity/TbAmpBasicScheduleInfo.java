package com.udbac.entity;

import java.util.Date;
import java.util.List;

import com.udbac.exception.ScheduleException;
import com.udbac.util.DecimalTool;
import com.udbac.util.MonitoringCodeTool;

/**
 * 排期基础信息
 * 
 * @author LFQ
 * @DATE 2016-04-21
 * 变更履历
 * 2017-09-26 update by LvWentao 新增N口径
 */
public class TbAmpBasicScheduleInfo {

	private Date createTime; // 添加时间
	private String marketingCode; // 营销代码(未加密)
	private String activityCode; //活动编号
	private String mic; // 短代码
	private TbAmpBasicMediaInfo media; // 媒体信息
	private String urlPc; // 目标链接地址URL(到达链接)(URL-PC)(URL-MOBILE)
	private String terminalType; // 终端类型
	private String unit; // 投放单位
	private String urlUpdate; // 变更后URL
	private Date urlUpdateTime; // URL生效时间
	private String clickAvg; // 日均点击量预估
	private String exposureAvg; // 日均曝光量预估
	private String enable; // 是否有效
	private String isFrequency; // 是否需要频控
	private String numFrequency; // 频控次数
	private String periodFrequency; // 频控周期
	private String funFrequency; // 频控方法
	private TbAmpBasicScheduleExtenInfo extenInfo;	//排期扩展信息
	private List<TbAmpBasicSchedulCalendarInfo> calendarInfo;	//排期安排
	private TbAmpBasicActivityInfo activityInfo;	//活动信息
	private String createUser;		//添加排期的用户名称
	private String dataCaliber;		//取数口径		//0 、标准口径	1、去重口径		2、百度DSP口径
	private Integer groupId = 0;	//点位分组
	private Integer micNo = 0;		//排期排序
	private String isKeySchedule;		//关键词排期标识 0:非关键词；1：关键词
	private String impDataCaliber;		//曝光去重口径		//0 、标准口径	1、去重口径	
	private String clkDataCaliber;		//点击去重口径		//0 、标准口径	1、去重口径	
	private Date updateTime;       // 更新时间
	private String updateUser;       // 更新用户
	private String micStatMark;       // N口径                //0、N  1、N+1
	
	//private TbAmpBasicScheduleExtenInfo scheduleExten;	//排期扩展信息

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getMarketingCode() {
		return marketingCode;
	}

	public void setMarketingCode(String marketingCode){
		this.marketingCode = marketingCode;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		this.mic = mic;
	}

	public String getUrlPc() {
		return urlPc;
	}

	public void setUrlPc(String urlPc) throws ScheduleException {
		
		char c = 32;
		
		urlPc = urlPc.replace('\n',c);
		urlPc = urlPc.replaceAll(" ","");
		if(!urlPc.startsWith("http") || !String.valueOf(urlPc.charAt(urlPc.length()-1)).matches("[/\\w#$&?-_=]") || urlPc.contains("<") || urlPc.contains(">") || urlPc.contains(" ")){
			throw new ScheduleException("URL有误，应以http:// 或 https://开头，前面或者末尾有特殊字符,或者包含<>,非法的URL");
		}
		if(urlPc.contains("WT.mc_id")){
			urlPc = MonitoringCodeTool.removeUrlWt(urlPc);
		}
		this.urlPc = urlPc;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) throws ScheduleException {
		terminalType = terminalType.toUpperCase();
		//pc/wap/app/SEM/MOBILE/OTT/SEM_PC/SEM_MOBILE
		switch(terminalType){
			case "SEM":
			case "PC":
			case "WAP":
			case "APP":
			case "MOBILE":
			case "OTT":
			case "SEM_PC":
			case "SEM_MOBILE":{
				this.terminalType = terminalType;
				break;
			}
			default:{
				throw new ScheduleException("终端类型只能是：pc/wap/app/SEM/MOBILE/OTT/SEM_PC/SEM_MOBILE");
			}
		}
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) throws ScheduleException {
		unit = unit.toUpperCase();
		//天、小时、CPM、CPA、CPC、CPD、2轮/天、1/4轮播、天/轮替
		if("天".equals(unit) || unit.contains("小时") || "CPM".equals(unit) || "CPT".equals(unit) || "月".equals(unit) ||  "年".equals(unit) ||
				"CPA".equals(unit) || "CPC".equals(unit) || "CPD".equals(unit) ||
				unit.contains("轮/天") || unit.contains("轮播") || "次".equals(unit) || "篇".equals(unit) ||
				unit.contains("天/轮替") || unit.contains("条") || unit.contains("天") || unit.contains("场")
				){
			this.unit = unit;
		}else{
			throw new ScheduleException("投放单位只能是：月、年、天、小时、次、篇、CPM、CPA、CPC、CPD、CPT、n轮/天、n/m轮播、天/轮替、条、场");
		}
	}

	public String getUrlUpdate() {
		return urlUpdate;
	}

	public void setUrlUpdate(String urlUpdate) {
		this.urlUpdate = urlUpdate;
	}

	public Date getUrlUpdateTime() {
		return urlUpdateTime;
	}

	public void setUrlUpdateTime(Date urlUpdateTime) {
		this.urlUpdateTime = urlUpdateTime;
	}

	public String getClickAvg() {
		return clickAvg;
	}

	public void setClickAvg(String clickAvg) throws ScheduleException{
		if(clickAvg == null|| "".equals(clickAvg) || "-".equals(clickAvg) || "——".equals(clickAvg)){
			return;
		}
		try{
			clickAvg = DecimalTool.positiveInteger(clickAvg);
		}catch(Exception e){
			throw new ScheduleException("点击预估只能为正整数、-、——");
		}
		this.clickAvg = clickAvg;
	}

	public String getExposureAvg() {
		return exposureAvg;
	}

	public void setExposureAvg(String exposureAvg) throws ScheduleException {
		if(exposureAvg == null || "".equals(exposureAvg) || "-".equals(exposureAvg) || "——".equals(exposureAvg)){
			return;
		}
		try{
			exposureAvg = DecimalTool.positiveInteger(exposureAvg);
		}catch(Exception e){
			throw new ScheduleException("曝光预估只能为正整数、-、——");
		}
		this.exposureAvg = exposureAvg;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getIsFrequency() {
		return isFrequency;
	}

	public void setIsFrequency(String isFrequency) {
		this.isFrequency = isFrequency;
	}

	public String getNumFrequency() {
		return numFrequency;
	}

	public void setNumFrequency(String numFrequency) throws ScheduleException {
		
		if("".equals(numFrequency) || numFrequency == null){
			return;
		}
		
		int num;
		try{
			num = Integer.parseInt(DecimalTool.positiveInteger(numFrequency));
			
			if(num < 1 || num > 21){
				throw new ScheduleException();
			}
		}catch(Exception e){
			throw new ScheduleException("频控次数值只能是 ：1 ~ 21");
		}
		
		this.numFrequency = String.valueOf(num);
	}

	public String getPeriodFrequency() {
		return periodFrequency;
	}

	public void setPeriodFrequency(String periodFrequency) throws ScheduleException {
//		0、全周期
//		1、自然周
//		2、投放周
//		3、每日
		if("".equalsIgnoreCase(periodFrequency) || periodFrequency == null){
			return ;
		}
		
		if("全周期".equals(periodFrequency) || "0".equals(periodFrequency)){
			this.periodFrequency = "0";
		}else if("自然周".equals(periodFrequency) || "1".equals(periodFrequency)){
			this.periodFrequency = "1";
		}else if("投放周".equals(periodFrequency) || "2".equals(periodFrequency)){
			this.periodFrequency = "2";
		}else if("每天".equals(periodFrequency) || "每日".equals(periodFrequency) || "3".equals(periodFrequency)){
			this.periodFrequency = "3";
		}else{
			throw new ScheduleException("投放周期值只能是：全周期/自然周/投放周/每天");
		}
	}

	public String getFunFrequency() {
		return funFrequency;
	}

	public void setFunFrequency(String funFrequency) throws ScheduleException {
//		0、前n次有效 == n
//		1、n次以上滤除 == 0
		if("".equalsIgnoreCase(funFrequency) || funFrequency == null){
			return ;
		}
		
		if("前n次有效".equals(funFrequency) || "0".equals(funFrequency)){
			this.funFrequency = "0";
		}else if("n次以上滤除".equals(funFrequency) || "1".equals(funFrequency)){
			this.funFrequency = "1";
		}else{
			throw new ScheduleException("频控方法规则值只能是：前n次有效/n次以上滤除");
		}
	}

//	public TbAmpBasicScheduleExtenInfo getScheduleExten() {
//		return scheduleExten;
//	}
//
//	public void setScheduleExten(TbAmpBasicScheduleExtenInfo scheduleExten) {
//		this.scheduleExten = scheduleExten;
//	}

	public TbAmpBasicMediaInfo getMedia() {
		return media;
	}

	public void setMedia(TbAmpBasicMediaInfo media) {
		this.media = media;
	}

	public List<TbAmpBasicSchedulCalendarInfo> getCalendarInfo() {
		return calendarInfo;
	}

	public void setCalendarInfo(List<TbAmpBasicSchedulCalendarInfo> calendarInfo) {
		this.calendarInfo = calendarInfo;
	}

	public TbAmpBasicScheduleExtenInfo getExtenInfo() {
		return extenInfo;
	}

	public void setExtenInfo(TbAmpBasicScheduleExtenInfo extenInfo) {
		this.extenInfo = extenInfo;
	}

	public TbAmpBasicActivityInfo getActivityInfo() {
		return activityInfo;
	}

	public void setActivityInfo(TbAmpBasicActivityInfo activityInfo) {
		this.activityInfo = activityInfo;
	}
	
	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getDataCaliber() {
		return dataCaliber;
	}

	public void setDataCaliber(String dataCaliber) {
		this.dataCaliber = dataCaliber;
	}
	
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		if(groupId == null){
			groupId = 0;
		}
		this.groupId = groupId;
	}

	public Integer getMicNo() {
		return micNo;
	}

	public void setMicNo(Integer micNo) {
		if(micNo != null){
			this.micNo = micNo;
		}
	}

	/**
	 * @return the isKeySchedule
	 */
	public String getIsKeySchedule() {
		return isKeySchedule;
	}

	/**
	 * @param isKeySchedule the isKeySchedule to set
	 */
	public void setIsKeySchedule(String isKeySchedule) {
		this.isKeySchedule = isKeySchedule;
	}
	public String getImpDataCaliber() {
		return impDataCaliber;
	}

	public void setImpDataCaliber(String impDataCaliber) {
		this.impDataCaliber = impDataCaliber;
	}
	
	public String getClkDataCaliber() {
		return clkDataCaliber;
	}

	public void setClkDataCaliber(String clkDataCaliber) {
		this.clkDataCaliber = clkDataCaliber;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public String getMicStatMark() {
		return micStatMark;
	}

	public void setMicStatMark(String micStatMark) {
		this.micStatMark = micStatMark;
	}
	@Override
	public String toString() {
		return "TbAmpBasicScheduleInfo [createTime=" + createTime
				+ "\n marketingCode=" + marketingCode + "\n mic=" + mic
				+ "\n media=" + media
				+ "\n urlPc=" + urlPc + "\n terminalType=" + terminalType
				+ "\n unit=" + unit + "\n urlUpdate=" + urlUpdate
				+ "\n urlUpdateTime=" + urlUpdateTime + "\n clickAvg=" + clickAvg
				+ "\n exposureAvg=" + exposureAvg + "\n enable=" + enable
				+ "\n isFrequency=" + isFrequency + "\n numFrequency="
				+ numFrequency + "\n periodFrequency=" + periodFrequency
				+ "\n funFrequency=" + funFrequency + "\n extenInfo=" + extenInfo
				+ "\n calendarInfo=" + calendarInfo + "\n activityInfo="
				+ activityInfo + "\n isKeySchedule=" + isKeySchedule
				+ "\n impDataCaliber=" + impDataCaliber + "\n clkDataCaliber="
				+ clkDataCaliber + "\n updateTime=" + updateTime + "\n updateUser="
				+ updateUser + "\n micStatMark=" + micStatMark +
				 "]";
	}

}
