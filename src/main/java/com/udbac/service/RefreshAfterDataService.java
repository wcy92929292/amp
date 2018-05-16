package com.udbac.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.udbac.entity.SMSDayData;

/**
 * 同步后端数据
 * @author Administrator
 *
 */
public interface RefreshAfterDataService {

	//旧系统的后端数据地址
	String smsUrl = "http://112.74.196.154:8080/sms/getAfterData";
	
	/**
	 * 请求并将返回的数据存入新库
	 * @param sdateStr
	 * @param edateStr
	 */
	void refreshAfter(String sdateStr,String edateStr);
	
	/**
	 * 处理页面返回的数据
	 * @param in
	 * @return
	 * @throws IOException
	 */
	List<SMSDayData> streamMethod(InputStream in) throws IOException; 

}
