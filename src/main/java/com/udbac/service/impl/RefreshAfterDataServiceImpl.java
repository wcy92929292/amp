package com.udbac.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.dao.RefreshAfterDataDao;
import com.udbac.entity.SMSDayData;
import com.udbac.service.RefreshAfterDataService;
import com.udbac.util.BeanUtil;
import com.udbac.util.LogUtil;
import com.udbac.util.URLUtil;


@Service
public class RefreshAfterDataServiceImpl implements RefreshAfterDataService {

	@Autowired
	private RefreshAfterDataDao  refreshAfterDataDao;
	
	private LogUtil logUtil = new LogUtil(RefreshAfterDataServiceImpl.class);
	
	@Override
	public void refreshAfter(String sdateStr, String edateStr) {
		
		//"2016-08-15_00:00:00"
		Map<String,String> params = new HashMap<>();
		params.put("sdateStr", sdateStr);
		params.put("edateStr", edateStr);
		
		String method = "GET";
		
		Object data = URLUtil.connation(smsUrl, params, method, this, "streamMethod");
		
		if(data instanceof List<?>){
			List<SMSDayData> dataList =  (List<SMSDayData>) data;
			
			refreshAfterDataDao.InsertToTmpData(dataList);
			
			refreshAfterDataDao.summaryAfterData();
		}else{
			logUtil.logError("OLM-IA99998",
					new String[]{smsUrl,sdateStr,edateStr,"RefreshAfterDataServiceImpl.refreshAfter"});
			//refreshAfter(sdateStr,edateStr);
		}
	}

	/**
	 * 对数据返回进行处理
	 */
	public List<SMSDayData> streamMethod(InputStream in) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
		
		String s = "";
		SMSDayData data = null;
		Class<SMSDayData> dataClazz = SMSDayData.class;
		List<SMSDayData> dataSet = new LinkedList<>();
		int j, i; 
		ObjectMapper mapper = new ObjectMapper();
		try {
			while((s=br.readLine())!=null){
				s = s.replace("},{", "\n");
				s = s.replace("[","");
				s = s.replace("}","");
				s = s.replace("{","");
				s = s.replace("]","");
				s = s.replaceAll("\"","");
//				dataSet = mapper.readValue(s, TypeFactory.collectionType(List.class, SMSDayData.class));
//				dataSet = mapper.readValue(s,dataSet.getClass());
//				System.out.println(dataSet.size());
				
				String[] dataArr = s.split("\n");
				
				for (i = 0; i < dataArr.length; i++) {
					String[] split = dataArr[i].split(",");
					
					//过滤无效数据
					if(split.length != 13){continue;}
					data = dataClazz.newInstance();
					
					for (j = 0; j < split.length; j++) {
						String[] entry = split[j].split(":");
						if(entry.length != 2){continue;}
						
						if(entry[0].equals("date")){
							data.setDate(new Date(Long.parseLong(entry[1])));
						}else{
							BeanUtil.setProperties(data, entry[1], entry[0]);
						}
					}//end for(j < split.length)
					
					if(data.getMic() != null && ! "null".equals(data.getMic()) && data.getMic().length() < 24){
						if(data.getVisit() != null || data.getVisitor() != null || data.getPv() != null || 
								data.getClick() != null || data.getBounceVisit() != null || data.getViewtime() != null){
							dataSet.add(data);
						}
					}
					
				}//end for (i < dataArr.length)
			}//end while
		} catch (Exception e) {
			e.printStackTrace();
		}//end try - catch
		System.out.println(dataSet.size());
		return dataSet;
	}//end streamMethod
	
}
