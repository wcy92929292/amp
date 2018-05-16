package com.udbac.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MonthMediaSumList {

	// 是否有需要合并相同的分组
	private boolean isMergeByGroupId = false;
	private String mediaName = null;
	//Mic集合
	private List<Mics> micsList = new LinkedList<>();
	//活动集合
	private List<Activitys> actList = new LinkedList<>();
	
	public MonthMediaSumList(String mediaName){
		this.mediaName = mediaName;
	}

	public boolean isMergeByGroupId() {
		return isMergeByGroupId;
	}

	public String getMediaName() {
		return mediaName;
	}

	public List<Mics> getMicsList() {
		return micsList;
	}
	
	public List<Activitys> getActList(){
		return actList;
	}
	
	/**
	 * 将点位添加到相同媒体的相同活动集合中
	 */
	private void addReport2ActList(MonthReportModel report,Mics mics){
		Activitys activitys = null;
		for (int i = 0; i < actList.size(); i++) {
			activitys = actList.get(i);
			//找到相同活动的，则将
			if(activitys.getActivityName().equals(report.getActivityName())){
				activitys.getMicsList().add(mics);
				return;
			}
		}//end for
		
		//未找到相同活动的Mic
		activitys = new Activitys(report.getActivityName());
		activitys.getMicsList().add(mics);
	}//end addReport2ActList
	
	/**
	 * 添加短代码
	 * @param micsList
	 */
	public void addReport2Mics(MonthReportModel report) throws Exception{
		
		checkMeida(report);
		
		Mics mics = getMics4Mic(report);
		
		//如果未找到，则新建一个
		if(mics == null){
			mics = createMics(report);
			//将集合放入到媒体集合中
			micsList.add(mics);
		}
		
		//将点位放入到集合List中
		mics.getMicList().add(report);
		//同时将点位放入到活动集合中
		addReport2ActList(report, mics);
	}//end addReport
	
	/**
	 * 校验media的设置
	 * @param report
	 */
	private void checkMeida(MonthReportModel report) throws Exception{
		
		if(!mediaName.equals(report.getMediaName())){
			throw new Exception(new StringBuffer(50).append("To mediaName is ").append(mediaName).append(", but the mediaName is ").append(report.getMediaName()).toString());
		}
		
		//媒体名称一样，则可以将此点位加入到集合中
		if(report.getGroupId() != 0){
			isMergeByGroupId = true;
		}
	}//end checkMeida
	
	/**
	 * 新建Mic集合
	 * @param report
	 * @return
	 */
	private Mics createMics(MonthReportModel report){
		Mics mics = new Mics(report.getActivityCode(),report.getGroupId(),report.getPutDate());
		return mics;
	}
	
	/**
	 * 在Mic集合中找到对应的集合
	 * @param report
	 * @return
	 */
	private Mics getMics4Mic(MonthReportModel report) throws Exception{
		
			Mics mics = null;
			//遍历媒体List
			for (int i = 0; i < micsList.size(); i++) {
				mics = micsList.get(i);
				//首先判断是否为同一组
				if(mics.getGroupId() == report.groupId){
					//判断是否需要分组，0为不分组
					if(report.groupId == 0){
						//短代码相同，即为同一组
						if(mics.getMicList().get(0).getMic().equals(report.mic)){
							return mics;
						}
					}else{
						//活动编号以及投放日期相同为同一组
						if(mics.getActivityCode().equals(report.getActivityCode()) && mics.getPutDate().equals(report.getPutDate())){
							return mics;
						}
					}//end if(report.groupId == 0) else
				}//if(mics.getGroupId() == report.groupId)
			}//end for
			
			return null;
	}//end getMics4Mic

}
