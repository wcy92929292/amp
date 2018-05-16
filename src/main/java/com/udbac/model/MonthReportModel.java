package com.udbac.model;

import java.util.Date;

import org.junit.Test;

import com.udbac.util.DateUtil;
import com.udbac.util.DecimalTool;

/**
 * 月报Model
 * 
 * @author LFQ
 */
public class MonthReportModel{

	public String customerName; // 客户名称
	public String customerCode; // 客户编号
	public String activityCode; // 活动编号
	public String activityName; // 活动名称
	public String mediaName; 	// 媒体名称
	public Integer groupId;   //分组序号

	public String pointLocation; // 投放位置
	public String putFunction; // 投放形式
	public Date	putDate; // 投放日期
	public String mic; // 识别码
	public String impDataCaliber;	 //数据口径
	public String clkDataCaliber;	 //数据口径
	public String supportClick; //是否支持点击
	public String supportExposure; //是否支持曝光
	
	public Integer dirtyImpUV; // 曝光人数
	public Integer dirtyImpPV; // 曝光次数
	public Integer dirtyClkUV; // 点击人数
	public Integer dirtyClkPV; // 点击次数
	public Integer cleanImpUV; // 清洗曝光人数
	public Integer cleanImpPV; // 清洗曝光次数
	public Integer cleanClkUV; // 清洗点击人数
	public Integer cleanClkPV; // 清洗点击次数
	public Integer dCleanImpUV;//深度清洗曝光人数
	public Integer dCleanImpPV;//深度清洗曝光次数
	public Integer dCleanClkUV;//深度清洗点击人数
	public Integer dCleanClkPV;//深度清洗点击次数
	
	public Integer sumDirtyImpUV; // 汇总曝光人数
	public Integer sumDirtyImpPV; // 汇总曝光次数
	public Integer sumDirtyClkUV; // 汇总点击人数
	public Integer sumDirtyClkPV; // 汇总点击次数
	public Integer sumCleanImpUV; // 汇总清洗曝光人数
	public Integer sumCleanImpPV; // 汇总清洗曝光次数
	public Integer sumCleanClkUV; // 汇总清洗点击人数
	public Integer sumCleanClkPV; // 汇总清洗点击次数
	public Integer sumDCleanImpUV;//汇总深度清洗曝光人数
	public Integer sumDCleanImpPV;//汇总深度清洗曝光次数
	public Integer sumDCleanClkUV;//汇总深度清洗点击人数
	public Integer sumDCleanClkPV;//汇总深度清洗点击次数
	
	public Integer visit; // 访次
	public Integer visitor; // 访客
	public Integer pageView; // 浏览量
	public Integer bounceTimes; // 跳出次数
	public Double bounceRate; // 跳出率
	public Double viewTime; // 平均停留时长
	public Integer click;	//到站点击
	
	public Integer sumVisit; // 访次
	public Integer sumVisitor; // 访客
	public Integer sumPageView; // 浏览量
	public Integer sumBounceTimes; // 跳出次数
	public Double sumBounceRate; // 跳出率
	public Double sumViewTime; // 平均停留时长
	public Integer sumClick;	//到站点击
	
	public Integer putDays;		//投放天数
	public Double clickAvg;		//点击预估
	public Double exposureAvg;	//曝光预估
	public String mediaType;	//媒体类别
	public String terminalType; //终端类型
	public String url;
	private Double putValue;	//投放量
	private String unit; 		//投放单位
	
	
	public Date startDate; // 开始时间
	public Date endDate; 	// 结束时间

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getPointLocation() {
		return pointLocation;
	}

	public void setPointLocation(String pointLocation) {
		if(pointLocation != null){
			this.pointLocation = pointLocation;
		}
	}

	public String getPutFunction() {
		return putFunction;
	}

	public void setPutFunction(String putFunction) {
		if(putFunction != null){
			this.putFunction = putFunction;
		}
	}

	public Date getPutDate() {
		return putDate;
	}

	public void setPutDate(Date putDate) {
		this.putDate = putDate;
	}

	public String getMic() {
		return mic;
	}

	public void setMic(String mic) {
		if(mic != null){
			this.mic = mic;
		}
	}

	public Integer getDirtyImpUV() {
		return dirtyImpUV;
	}

	public void setDirtyImpUV(Integer dirtyImpUV) {
		this.dirtyImpUV = dirtyImpUV;
	}

	public Integer getDirtyImpPV() {
		return dirtyImpPV;
	}

	public void setDirtyImpPV(Integer dirtyImpPV) {
		this.dirtyImpPV = dirtyImpPV;
	}

	public Integer getDirtyClkUV() {
		return dirtyClkUV;
	}

	public void setDirtyClkUV(Integer dirtyClkUV) {
		this.dirtyClkUV = dirtyClkUV;
	}

	public Integer getDirtyClkPV() {
		return dirtyClkPV;
	}

	public void setDirtyClkPV(Integer dirtyClkPV) {
		this.dirtyClkPV = dirtyClkPV;
	}

	public Integer getCleanImpUV() {
		return cleanImpUV;
	}

	public void setCleanImpUV(Integer cleanImpUV) {
		this.cleanImpUV = cleanImpUV;
	}

	public Integer getCleanImpPV() {
		return cleanImpPV;
	}

	public void setCleanImpPV(Integer cleanImpPV) {
		this.cleanImpPV = cleanImpPV;
	}

	public Integer getCleanClkUV() {
		return cleanClkUV;
	}

	public void setCleanClkUV(Integer cleanClkUV) {
		this.cleanClkUV = cleanClkUV;
	}

	public Integer getCleanClkPV() {
		return cleanClkPV;
	}

	public void setCleanClkPV(Integer cleanClkPV) {
		this.cleanClkPV = cleanClkPV;
	}

	public Integer getVisit() {
		return visit;
	}

	public void setVisit(Integer visit) {
		this.visit = visit;
	}

	public Integer getVisitor() {
		return visitor;
	}

	public void setVisitor(Integer visitor) {
		this.visitor = visitor;
	}

	public Integer getPageView() {
		return pageView;
	}

	public void setPageView(Integer pageView) {
		this.pageView = pageView;
	}

	public Integer getBounceTimes() {
		return bounceTimes;
	}

	public void setBounceTimes(Integer bounceTimes) {
		this.bounceTimes = bounceTimes;
	}
	

	public Integer getClick() {
		return click;
	}

	public void setClick(Integer click) {
		this.click = click;
	}

	/**
	 * 跳出率 = 跳出次数 / 访次
	 * 
	 * @return  -1.0  则表示除以零
	 * 			null 不进行计算
	 * 			其它正常
	 */
	public Double getBounceRate() {
		
		if(visit != null && bounceTimes != null){
			if(visit == 0){
				bounceRate = -1.0;
			}else{
				bounceRate = Double.parseDouble(bounceTimes.toString()) / Double.parseDouble(visit.toString());
			}
		}
		
		return bounceRate;
	}//end getBounceRate

	public void setBounceRate(Double bounceRate) {
		this.bounceRate = bounceRate;
	}

	public Double getViewTime() {
		return viewTime;
	}
	
	/**
	 * 获取平均访问时长
	 * @return
	 */
	public Double getAvgViewTime(){
		Double vt = getViewTime();
		Integer vis = getVisit();
		
		if(vt != null && vis != null){
			if(vis == 0.0){
				return -1.0;
			}
			
			return vt  / vis; 
		}
		
		return null;
	}//end getAvgViewTime
	
	
	/**
	 * 
	 * @return
	 */
	public String getStartToEndDate(){
		
		if(getStartDate() != null  && getEndDate() != null){
			return DateUtil.getDateStr(getStartDate(), "yyyy/MM/dd") + "-" +  DateUtil.getDateStr(getEndDate(), "yyyy/MM/dd");
		}
		
		return null;
	}

	public void setViewTime(Double viewTime) {
		this.viewTime = viewTime;
	}

	public Date getStartDate() {
		return startDate;
	}


	public Integer getdCleanImpUV() {
		return dCleanImpUV;
	}

	public void setdCleanImpUV(Integer dCleanImpUV) {
		this.dCleanImpUV = dCleanImpUV;
	}

	public Integer getdCleanImpPV() {
		return dCleanImpPV;
	}

	public void setdCleanImpPV(Integer dCleanImpPV) {
		this.dCleanImpPV = dCleanImpPV;
	}

	public Integer getdCleanClkUV() {
		return dCleanClkUV;
	}

	public void setdCleanClkUV(Integer dCleanClkUV) {
		this.dCleanClkUV = dCleanClkUV;
	}

	public Integer getdCleanClkPV() {
		return dCleanClkPV;
	}

	public void setdCleanClkPV(Integer dCleanClkPV) {
		this.dCleanClkPV = dCleanClkPV;
	}

	public void setStartDate(Date startDate) {
		if(startDate != null && (this.startDate == null || this.startDate.compareTo(startDate) > 0)){
			this.startDate = startDate;
		}
		//this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		if(endDate != null && (this.endDate == null || this.endDate.compareTo(endDate) < 0)){
			this.endDate = endDate;
		}
		//this.endDate = endDate;
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

	public String getSupportClick() {
		return supportClick;
	}

	public void setSupportClick(String supportClick) {
		if("是".equals(supportClick) || "1".equals(supportClick)){
			supportClick = "1";
		}else{
			supportClick = "0";
		}
		this.supportClick = supportClick;
	}

	public String getSupportExposure() {
		return supportExposure;
	}

	public void setSupportExposure(String supportExposure) {
		
		if("是".equals(supportExposure) || "1".equals(supportExposure)){
			supportExposure = "1";
		}else{
			supportExposure = "0";
		}
		
		this.supportExposure = supportExposure;
	}
	
	public Integer getPutDays() {
		return putDays;
	}

	public void setPutDays(Integer putDays) {
		if(putDays != null && (this.putDays == null || this.putDays < putDays)){
			this.putDays = putDays;
		}
	}

	public Integer getClickAvg() {
		Integer putDays = this.getPutDays();
		if(clickAvg == null){
			return null;
		}
		Double result = null;
		//辽宁以及集团的有CPM判断
		if("LN".equals(getCustomerCode()) || "JT".equals(getCustomerCode())){
			if("CPM".equalsIgnoreCase(unit) && putValue != null){
				result = putValue * clickAvg; 
				return result.intValue();
			}
		}
		if(putDays != null){
			result = putDays * clickAvg;
			return result.intValue();
		}
		
		return clickAvg.intValue();
	}

	public void setClickAvg(String clickAvg) {
		if(clickAvg != null && clickAvg.matches("\\d{1,}") && clickAvg.matches("\\d{1,}.\\d{1,}")){
			this.clickAvg = Double.parseDouble(clickAvg);
		}
	}

	public Integer getExposureAvg() {
		Integer putDays = this.getPutDays();
		Double result = null;
		
		if(exposureAvg == null){
			return null;
		}
		//辽宁以及集团的有CPM判断
		if("LN".equals(getCustomerCode()) || "JT".equals(getCustomerCode())){
			if("CPM".equalsIgnoreCase(unit) && putValue != null){
				result = putValue * exposureAvg; 
				return result.intValue();
			}
		}
		else if(putDays != null){
			result = putDays * exposureAvg;
			return result.intValue();
		}
		
		return exposureAvg.intValue();
	}

	public void setExposureAvg(String exposureAvg) {
		if(exposureAvg != null && exposureAvg.matches("\\d{1,}") && exposureAvg.matches("\\d{1,}.\\d{1,}")){
			this.exposureAvg = Double.valueOf(exposureAvg);
		}
	}

	/**
	 * 点击率  == 点击次数 / 曝光次数
	 * @return
	 */
	public Double getClickRate(){
		Double clickRate = null;
		Double clickPV = DecimalTool.int2Double(getClickPV());
		Double exposurePV = DecimalTool.int2Double(getExposurePV());
		
		if(clickPV != null && exposurePV != null){
			if(exposurePV == 0){
				return -1.0;
			}
			clickRate = clickPV / exposurePV;
		}
		return clickRate;
	}//end getClickRate
	
	/**
	 * 独立点击率 = 点击人数   / 曝光人数
	 * @return
	 */
	public Double getOloneClickRate(){
		Double clickRate = null;
		Double clickUV = DecimalTool.int2Double(getClickUV());
		Double exposureUV = DecimalTool.int2Double(getExposureUV());
		
		if(clickUV != null && exposureUV != null){
			if(exposureUV == 0){
				return -1.0;
			}
			clickRate = clickUV / exposureUV;
		}
		return clickRate;
	}//end getCloneClickRate
	
	/**
	 * 到达率 = 前端点击次数 / 访次
	 * @return
	 */
	public Double getArriveRate(){
		Double arriveRate = null;
		Double clickPV = DecimalTool.int2Double(getClickPV());
		if(clickPV != null && visit != null){
			if(visit == 0){
				return -1.0;
			}
			arriveRate = clickPV / visit.doubleValue();
		}
		return arriveRate;
		
	}//end getArriveRate()

	/**
	 * 曝光次数
	 * @return
	 */
	public Integer getExposurePV(){
		if("2".equals(impDataCaliber)){
			return getdCleanImpPV();
		}else if("1".equals(impDataCaliber)){
			return getCleanImpPV();
		}else{
			return getDirtyImpPV();
		}
	}//end getExposurePV();
	
	/**
	 * 曝光人数
	 * @return
	 */
	public Integer getExposureUV(){
		if("2".equals(impDataCaliber)){
			return getdCleanImpUV();
		}else if("1".equals(impDataCaliber)){
			return getCleanImpUV();
		}else{
			return getDirtyImpUV();
		}
	}
	
	/**
	 * 点击次数
	 * @return
	 */
	public Integer getClickPV(){
		if("2".equals(clkDataCaliber)){
			return getdCleanClkPV();
		}else if("1".equals(clkDataCaliber)){
			return getCleanClkPV();
		}else{
			return getDirtyClkPV();
		}
	}//end getClickPV()
	
	/**
	 * 点击人数
	 * @return
	 */
	public Integer getClickUV(){
		if("2".equals(clkDataCaliber)){
			return getdCleanClkUV();
		}else if("1".equals(clkDataCaliber)){
			return getCleanClkUV();
		}else{
			return getDirtyClkUV();
		}
	}
	
	
	/**
	 * 曝光汇总次数
	 * @return
	 */
	public Integer getSumExposurePV(){
		if("2".equals(impDataCaliber)){
			return getSumDCleanImpPV();
		}else if("1".equals(impDataCaliber)){
			return getSumCleanImpPV();
		}else{
			return getSumDirtyImpPV();
		}
	}//end getExposurePV();
	
	/**
	 * 曝光汇总人数
	 * @return
	 */
	public Integer getSumExposureUV(){
		if("2".equals(impDataCaliber)){
			return getSumDCleanImpUV();
		}else if("1".equals(impDataCaliber)){
			return getSumCleanImpUV();
		}else{
			return getSumDirtyImpUV();
		}
	}
	
	/**
	 * 点击汇总次数
	 * @return
	 */
	public Integer getSumClickPV(){
		if("2".equals(clkDataCaliber)){
			return getSumDCleanClkPV();
		}else if("1".equals(clkDataCaliber)){
			return getSumCleanClkPV();
		}else{
			return getSumDirtyClkPV();
		}
	}//end getClickPV()
	
	/**
	 * 点击汇总人数
	 * @return
	 */
	public Integer getSumClickUV(){
		if("2".equals(clkDataCaliber)){
			return getSumDCleanClkUV();
		}else if("1".equals(clkDataCaliber)){
			return getSumCleanClkUV();
		}else{
			return getSumDirtyClkUV();
		}
	}
	
	/**
	 * 曝光完成率  = 曝光次数 / 曝光预估
	 * @return
	 */
	public Double getExposureFinishRate(){
		
		Double exposureFinishRate = null;
		Integer exposurePV = getExposurePV();
		Integer exposureAvg = getExposureAvg();
		
		if(exposurePV != null && exposureAvg != null){
			exposureFinishRate = exposurePV.doubleValue() / exposureAvg.doubleValue();
		}
		
		return exposureFinishRate;
	}//end getExposureFinishRate
	
	/**
	 * 汇总曝光完成率  = 汇总曝光次数 / 汇总曝光预估
	 * @return
	 */
	public Double getSumExposureFinishRate(){
		
		Double exposureFinishRate = null;
		Integer exposurePV = getSumExposurePV();
		Integer exposureAvg = getExposureAvg();
		
		if(exposurePV != null && exposureAvg != null){
			exposureFinishRate = exposurePV.doubleValue() / exposureAvg.doubleValue();
		}
		
		return exposureFinishRate;
	}//end getExposureFinishRate
	
	/**
	 * 点击完成率 = 点击次数 / 点击预估
	 * @return
	 */
	public Double getClickFinishRate(){
		Double clickFinishRate = null;
		Integer clickPV = getClickPV();
		Integer clickAvg = getClickAvg();
		
		if(clickPV != null && clickAvg != null){
			clickFinishRate =  clickPV.doubleValue() / clickAvg.doubleValue();
		}
		
		return clickFinishRate;
	}
	
	
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	
	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Double getPutValue() {
		return putValue;
	}

	public void setPutValue(Double putValue) {
		this.putValue = putValue;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public Integer getSumDirtyImpUV() {
		return sumDirtyImpUV;
	}

	public void setSumDirtyImpUV(Integer sumDirtyImpUV) {
		this.sumDirtyImpUV = sumDirtyImpUV;
	}

	public Integer getSumDirtyImpPV() {
		return sumDirtyImpPV;
	}

	public void setSumDirtyImpPV(Integer sumDirtyImpPV) {
		this.sumDirtyImpPV = sumDirtyImpPV;
	}

	public Integer getSumDirtyClkUV() {
		return sumDirtyClkUV;
	}

	public void setSumDirtyClkUV(Integer sumDirtyClkUV) {
		this.sumDirtyClkUV = sumDirtyClkUV;
	}

	public Integer getSumDirtyClkPV() {
		return sumDirtyClkPV;
	}

	public void setSumDirtyClkPV(Integer sumDirtyClkPV) {
		this.sumDirtyClkPV = sumDirtyClkPV;
	}

	public Integer getSumCleanImpUV() {
		return sumCleanImpUV;
	}

	public void setSumCleanImpUV(Integer sumCleanImpUV) {
		this.sumCleanImpUV = sumCleanImpUV;
	}

	public Integer getSumCleanImpPV() {
		return sumCleanImpPV;
	}

	public void setSumCleanImpPV(Integer sumCleanImpPV) {
		this.sumCleanImpPV = sumCleanImpPV;
	}

	public Integer getSumCleanClkUV() {
		return sumCleanClkUV;
	}

	public void setSumCleanClkUV(Integer sumCleanClkUV) {
		this.sumCleanClkUV = sumCleanClkUV;
	}

	public Integer getSumCleanClkPV() {
		return sumCleanClkPV;
	}

	public void setSumCleanClkPV(Integer sumCleanClkPV) {
		this.sumCleanClkPV = sumCleanClkPV;
	}

	public Integer getSumDCleanImpUV() {
		return sumDCleanImpUV;
	}

	public void setSumDCleanImpUV(Integer sumDCleanImpUV) {
		this.sumDCleanImpUV = sumDCleanImpUV;
	}

	public Integer getSumDCleanImpPV() {
		return sumDCleanImpPV;
	}

	public void setSumDCleanImpPV(Integer sumDCleanImpPV) {
		this.sumDCleanImpPV = sumDCleanImpPV;
	}

	public Integer getSumDCleanClkUV() {
		return sumDCleanClkUV;
	}

	public void setSumDCleanClkUV(Integer sumDCleanClkUV) {
		this.sumDCleanClkUV = sumDCleanClkUV;
	}

	public Integer getSumDCleanClkPV() {
		return sumDCleanClkPV;
	}

	public void setSumDCleanClkPV(Integer sumDCleanClkPV) {
		this.sumDCleanClkPV = sumDCleanClkPV;
	}

	public Integer getSumVisit() {
		return sumVisit;
	}

	public void setSumVisit(Integer sumVisit) {
		this.sumVisit = sumVisit;
	}

	public Integer getSumVisitor() {
		return sumVisitor;
	}

	public void setSumVisitor(Integer sumVisitor) {
		this.sumVisitor = sumVisitor;
	}

	public Integer getSumPageView() {
		return sumPageView;
	}

	public void setSumPageView(Integer sumPageView) {
		this.sumPageView = sumPageView;
	}

	public Integer getSumBounceTimes() {
		return sumBounceTimes;
	}

	public void setSumBounceTimes(Integer sumBounceTimes) {
		this.sumBounceTimes = sumBounceTimes;
	}

	public Double getSumBounceRate() {
		return sumBounceRate;
	}

	public void setSumBounceRate(Double sumBounceRate) {
		this.sumBounceRate = sumBounceRate;
	}

	public Double getSumViewTime() {
		return sumViewTime;
	}

	public void setSumViewTime(Double sumViewTime) {
		this.sumViewTime = sumViewTime;
	}

	public Integer getSumClick() {
		return sumClick;
	}

	public void setSumClick(Integer sumClick) {
		this.sumClick = sumClick;
	}
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "MonthReportModel [customerName=" + customerName + ", customerCode=" + customerCode + ", activityCode="
				+ activityCode + ", activityName=" + activityName + ", mediaName=" + mediaName + ", groupId=" + groupId
				+ ", pointLocation=" + pointLocation + ", putFunction=" + putFunction + ", putDate=" + putDate
				+ ", mic=" + mic + ", impDataCaliber=" + impDataCaliber + ", clkDataCaliber=" + clkDataCaliber
				+ ", supportClick=" + supportClick + ", supportExposure=" + supportExposure + ", dirtyImpUV="
				+ dirtyImpUV + ", dirtyImpPV=" + dirtyImpPV + ", dirtyClkUV=" + dirtyClkUV + ", dirtyClkPV="
				+ dirtyClkPV + ", cleanImpUV=" + cleanImpUV + ", cleanImpPV=" + cleanImpPV + ", cleanClkUV="
				+ cleanClkUV + ", cleanClkPV=" + cleanClkPV + ", dCleanImpUV=" + dCleanImpUV + ", dCleanImpPV="
				+ dCleanImpPV + ", dCleanClkUV=" + dCleanClkUV + ", dCleanClkPV=" + dCleanClkPV + ", sumDirtyImpUV="
				+ sumDirtyImpUV + ", sumDirtyImpPV=" + sumDirtyImpPV + ", sumDirtyClkUV=" + sumDirtyClkUV
				+ ", sumDirtyClkPV=" + sumDirtyClkPV + ", sumCleanImpUV=" + sumCleanImpUV + ", sumCleanImpPV="
				+ sumCleanImpPV + ", sumCleanClkUV=" + sumCleanClkUV + ", sumCleanClkPV=" + sumCleanClkPV
				+ ", sumDCleanImpUV=" + sumDCleanImpUV + ", sumDCleanImpPV=" + sumDCleanImpPV + ", sumDCleanClkUV="
				+ sumDCleanClkUV + ", sumDCleanClkPV=" + sumDCleanClkPV + ", visit=" + visit + ", visitor=" + visitor
				+ ", pageView=" + pageView + ", bounceTimes=" + bounceTimes + ", bounceRate=" + bounceRate
				+ ", viewTime=" + viewTime + ", click=" + click + ", sumVisit=" + sumVisit + ", sumVisitor="
				+ sumVisitor + ", sumPageView=" + sumPageView + ", sumBounceTimes=" + sumBounceTimes
				+ ", sumBounceRate=" + sumBounceRate + ", sumViewTime=" + sumViewTime + ", sumClick=" + sumClick
				+ ", putDays=" + putDays + ", clickAvg=" + clickAvg + ", exposureAvg=" + exposureAvg + ", mediaType="
				+ mediaType + ", terminalType=" + terminalType + ", url=" + url + ", putValue=" + putValue + ", unit="
				+ unit + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	

	

	
	
}

