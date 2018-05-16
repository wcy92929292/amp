package com.udbac.util;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 查询实时数据
 * @author Administrator
 *
 */
public class URLUtil {

	
	/**
	 * 连接目标地址，并对提供的返回流数据处理方法处理访问的页面
	 * @param urls  访问地址
	 * @param params  访问参数
	 * @param method  访问方式   GET   POST  PUT
	 * @param target  成功访问页面后的处理对象   null 不执行目标方法
	 * @param streamMethod 成功访问页面后的处理对象的流处理方法,至少需要InputStream参数
	 * @param paramAndObj  [2n]形参列表
	 * 					   [2n+1]实参列表
	 * 						n从0开始
	 * 				即：形参1、实参1、形参2、实参2
	 * 	eg:URLUtil.connation(smsUrl, params, method, this, "streamMethod");
	 * @return  流处理方法的返回值
	 * 			如果返回  404 ，则可能连接失败。
	 */
	public static Object connation(String urls , Map<String,String> params,String method,
			Object target,String streamMethod,Object[] ... paramAndObj){
		
		URL url = null;  
		HttpURLConnection http = null;  
		Object result = null;
//		BufferedReader in = null;
		InputStream in = null;
		
		try {  
		    url = new URL(urls);  
		    http = (HttpURLConnection) url.openConnection();  
		    http.setDoInput(true);  
		    http.setDoOutput(true);  
		    http.setUseCaches(false);  
		    http.setConnectTimeout(5000);//设置连接超时  
		    //如果在建立连接之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。  
		    http.setReadTimeout(10000);//设置读取超时  
		    //如果在数据可读取之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。            
		    http.setRequestMethod(method);  
		    //http.setRequestProperty("Content-Type","text/xml; charset=UTF-8");  
		    http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
		    http.connect();  
		   
		    //添加参数
		    if(params != null){
			    OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "UTF-8");  
			    osw.write(setParams(params));  
			    osw.flush();  
			    osw.close(); 
		    }
		    //如果访问成功，则执行目标方法
		    if (target !=null && http.getResponseCode() == 200) {  
//		       in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
		       in = http.getInputStream();
		       Class<? extends Object> clazz = target.getClass();
		       Method m = clazz.getDeclaredMethod(streamMethod,setMethodParam(paramAndObj));
		       m.setAccessible(true);
		       result = m.invoke(target, setMethodParamObj(in,paramAndObj));
		        
		       in.close();  
		    }  
		} catch (Exception e) {
			if(!(e instanceof SocketTimeoutException) && !(e instanceof ConnectException)){
				e.printStackTrace();
			}
			e.printStackTrace();
			return 404;
		} finally {  
		    if (http != null) http.disconnect();  
		}  
		
		return result;
	}//end connation()
	/////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 设置CallBack函数的形参列表
	 */
	public static Class<?>[] setMethodParam(Object[][] paramAndObj){
		
		List<Class<?>> paramList= null;
		paramList =  new LinkedList<>();
		paramList.add(InputStream.class);
		
		if(paramAndObj != null && paramAndObj.length == 2){
			for (int i = 0; i < paramAndObj[0].length; i++) {
				paramList.add((Class<?>) paramAndObj[0][i]);
			}
		}
		
		return paramList.toArray(new Class<?>[paramList.size()]);
	}
	
	/**
	 * 设置CallBack函数的实参列表
	 */
	public static Object[] setMethodParamObj(InputStream in,Object[][] paramAndObj){
		
		List<Object> paramList= null;
		paramList =  new LinkedList<>();
		paramList.add(in);
		
		if(paramAndObj != null && paramAndObj.length == 2){
			for (int i = 0; i < paramAndObj[1].length; i++) {
				paramList.add(paramAndObj[1][i]);
			}
		}
		
		return paramList.toArray();
	}
	
	
	/**
	 * 将Map类型的参数转换为url参数格式
	 * @return
	 */
	public static String setParams(Map<String,String> paramMap){
		 
		StringBuffer param = new StringBuffer(50);
		Set<String> keySet = paramMap.keySet();
		
		for (String key : keySet) {
			param.append("&").append(key).append("=").append(paramMap.get(key));
		}
		
		return param.toString();
	}//end setParams()
	
}
