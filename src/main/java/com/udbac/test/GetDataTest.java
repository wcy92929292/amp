package com.udbac.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.udbac.entity.SMSDayData;
import com.udbac.util.BeanUtil;
import com.udbac.util.URLUtil;

public class GetDataTest {
	
	public static void main(String[] args) {
		
		GetDataTest gdt = new GetDataTest();
		
		String url = "http://112.74.196.154:8080/sms/getAfterData";
//		String url = "http://112.74.196.154:8080/sms/getBeforeData";
		
		Map<String,String> params = new HashMap<>();
		params.put("sdateStr", "2016-08-14_00:00:00");
		params.put("edateStr", "2016-08-15_00:00:00");
		
		String method = "GET";
		
		Object data = URLUtil.connation(url, params, method, gdt, "streamMethod");
		System.out.println(data);
	}
	
	public void streamMethod(InputStream in) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
		
		String s = "";
		SMSDayData data = null;
		Class<SMSDayData> dataClazz = SMSDayData.class;
		List<SMSDayData> dataList = new LinkedList<>();
		int j, i; 
		
		try {
			while((s=br.readLine())!=null){
				s = s.replace("},{", "\n");
				s = s.replace("[","");
				s = s.replace("}","");
				s = s.replace("{","");
				s = s.replace("]","");
				s = s.replaceAll("\"","");
				
				String[] dataArr = s.split("\n");
				
				for (i = 0; i < dataArr.length; i++) {
					String[] split = dataArr[i].split(",");
					
	//				//过滤无效数据
	//				if(split.length != 13){continue;}
					data = dataClazz.newInstance();
	//				
					for (j = 0; j < split.length; j++) {
						String[] entry = split[j].split(":");
						if(entry[0].equals("date")){
							data.setDate(new Date(Long.parseLong(entry[1])));
						}else{
							BeanUtil.setProperties(data, entry[1], entry[0]);
						}
					}//end for(j < split.length)
	//				
					dataList.add(data);
					
				}//end for (i < dataArr.length)
			}//end while
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(dataList.size());
		
	}
	
	@Test
	public void test(){
		String message = "http://www.10086.cn/cq/index_230_230.html ";
		message = message.replaceAll(" ", "").replaceAll("/^\n$/", "").replaceAll("/^\r&/","");
		System.out.println(message+"===");
		
	}
}
