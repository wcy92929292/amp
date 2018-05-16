package com.udbac.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.udbac.util.DateUtil;
import com.udbac.model.OnlineDataNode;

/**
 * 上线核查实时数据
 * @author LFQ
 * @date 2016-08-22
 */
public class RealTimeOnlineCheckUtil {
	
	//当天凌晨至当前上一小时的上线第一天各mic总的监测数据
	private	Map<String,OnlineDataNode> onceMicMap = new HashMap<>();
		
	//当天凌晨至当前小时的上线第一天各mic总的监测数据
	private	Map<String,OnlineDataNode> currentOnceMicMap = new HashMap<>();
		
	//当天凌晨至当前上一小时的各mic总的监测数据
	private	Map<String,OnlineDataNode> micMap = new HashMap<>();
			
	//当天凌晨至当前小时的各mic总的监测数据
	private	Map<String,OnlineDataNode> currentMicMap = new HashMap<>();
		
	//实时数据的URL地址
	private	String[] realUrls = {
				"http://s01.trafficjam.cn/q,",
				"http://s02.trafficjam.cn/q,",
				"http://s03.trafficjam.cn/q,",
				"http://s04.trafficjam.cn/q,",
				"http://s05.trafficjam.cn/q,",
				
	};
		
	//刷新内存数据的时间
	private	long refreshTime = 5000;
	
	//当前的时间  yyyyMMdd_hh
	private static String currentDate = "";
	//当前的小时
	private static Integer currentHour = 0;
	
	
	//单例化实体
	private static final RealTimeOnlineCheckUtil realTimeOnlineCheckUtil = new RealTimeOnlineCheckUtil();
	
	private RealTimeOnlineCheckUtil(){refreshData();}
	
	public static RealTimeOnlineCheckUtil getRealTimeOnlineCheckUtil(){
		return realTimeOnlineCheckUtil;
	}
	

	/**
	 * 刷新内存中的监测数据
	 */
	public void refreshData() {
		Date date = new Date();
//		20160101_01
		currentDate = DateUtil.getDateStr(date,"yyyyMMdd");
		currentHour = date.getHours();
//		currentDate = "20160926";
//		currentHour = 24;
	}//end refreshData();
	
	
	/**
	 * 解析实时URL返回来的数据，并封装将 OnlineDataNode中
	 * @return
	 */
	public OnlineDataNode analysisResult(InputStream is,OnlineDataNode dataNode) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		String line = "";
		String impStr = "";
		String clkStr = "";
		
		try {
			while ((line = br.readLine()) != null) {
				//hour=20160823_15 himp=1594965 hclk=18795 adid=r0PfKDQNgbTqwufzjeGc imp=3178 clk=81
				//hour=20160926_12 himp=2185422 hclk=37126 adid=td6tobFeUHOpme9TUgxJ imp=100372 clk=170
				//分隔 获取 imp， clk
				impStr = line.substring(line.indexOf(" imp=") + 5,line.indexOf(" clk="));
				clkStr = line.substring(line.indexOf(" clk=") + 5,line.length());
				
				//将碎片小时数据装入小时数据中
				dataNode.setHimp((dataNode.getHimp() == null ? 0 : dataNode.getHimp()) + Integer.parseInt(impStr));
				dataNode.setHclk((dataNode.getHclk() == null ? 0 : dataNode.getHclk()) + Integer.parseInt(clkStr));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dataNode;
	}//end analysisResult();

	//形参列表
	private static Class<?>[] paramClass = new Class<?>[]{OnlineDataNode.class};
	
	/**
	 * 查询实时数据
	 * @param mic
	 * @return
	 */ 
	public OnlineDataNode totailHourPVClick(String mic,Integer h){
//		 = "r0PfKDQNgbTqwufzjeGc";
		StringBuffer urlSB = new StringBuffer(50);
		
		int i = 0;
		String url = "";
		
		//保存小时数据的对象
		OnlineDataNode dataNode = new OnlineDataNode();
		dataNode.setMic(mic);
		dataNode.setHour(h);
		//实参列表
		Object[] dataNodeArr = new Object[]{dataNode};
		
		//访问的链接
		for (i=0; i < realUrls.length; i++) {
				//获取URL地址
				url = getUrl(urlSB, mic, i, h);
				//汇总
				URLUtil.connation(url, null, "GET",this,"analysisResult",paramClass,dataNodeArr);
		}//end for(i < realUrls.length)
		
		return dataNode;
	} //totailHourPVClick
	
	/**
	 * 汇总某个短代码当天的监测数据
	 */
	public List<OnlineDataNode> totailPVClick(String mic){
		
		List<OnlineDataNode> dataNodeList = new LinkedList<>();
		
		//创建将小时数据汇总任务线程
		int i =0;
		for (i = 0; i <= currentHour; i++) {
			new Thread(new HourTask(i,mic,dataNodeList)).start();
		}
		
		//等待汇总完成
		while(dataNodeList.size() != currentHour + 1){
			try {Thread.sleep(1);} catch (Exception e) {}
		}
		
		return dataNodeList;
	}//end totailPVClick

	/**
	 * 拼接访问地址
	 * 	http://s01.trafficjam.cn/q,td6tobFeUHOpme9TUgxJ,20160926_12
	 * @param urlSB
	 * @param mic
	 * @param i
	 * @param j
	 * @return
	 */ 
	public String getUrl(StringBuffer urlSB,String mic,int i,int j){
		urlSB.delete(0, urlSB.length());
		
		urlSB.append(realUrls[i]).append(mic).append(",").append(currentDate).append("_");
		
		//补前导0
		if(j < 10){
			urlSB.append("0");
		}
		urlSB.append(j);
		return urlSB.toString();
	}//end getUrl()
	
	/**
	 * 清空实时数据
	 */
	public void cleanMap() {
		onceMicMap.clear();
		currentOnceMicMap.clear();
		micMap.clear();
		currentMicMap.clear();
	}//end cleanMap

	public Map<String, OnlineDataNode> getOnceMicMap() {
		return onceMicMap;
	}

	public Map<String, OnlineDataNode> getCurrentOnceMicMap() {
		return currentOnceMicMap;
	}

	public Map<String, OnlineDataNode> getMicMap() {
		return micMap;
	}

	public Map<String, OnlineDataNode> getCurrentMicMap() {
		return currentMicMap;
	}
	
	/**
	 * 汇总小时的任务
	 */
	private class HourTask implements Runnable{
		
		final Integer hour;
		final String mic;
		final List<OnlineDataNode> dataNodelist;
		
		HourTask(Integer hour,String mic,List<OnlineDataNode> dataNodelist){
			this.hour = hour;
			this.mic = mic;
			this.dataNodelist = dataNodelist;
		}
		
		@Override
		public void run() {
			dataNodelist.add(totailHourPVClick(mic, hour));
		}
	}
	
	public static void main(String[] args) {
		RealTimeOnlineCheckUtil checkUtil = RealTimeOnlineCheckUtil.getRealTimeOnlineCheckUtil();
		checkUtil.refreshData();
		List<OnlineDataNode> totailPVClick = checkUtil.totailPVClick("td6tobFeUHOpme9TUgxJ");
		Long sumPV = sumPV(totailPVClick);
		System.out.println(sumPV);
	}
	
	public static Long sumPV(List<OnlineDataNode> list){
		Long sum = new Long(0);
		
		for (int i = 0; i < list.size(); i++) {
			sum +=  list.get(i).getHimp();
		}
		
		return sum;
	}
}
