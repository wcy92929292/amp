package com.udbac.util;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 解析排期配置文件
 * @author LFQ
 * @DATE 2016-04-20
 */
public class ScheduleProperties {
	
	//普通排期坐标
	private static Map<int[],String> genSchedule;
	
	//集团排期坐标
	private static Map<int[],String> jtSchedule;
	
	//关键词排期坐标
	private static Map<int[],String> keySchedule;
	
	//普通排期表中排期日历前半部分的读取顺序
	private static String[] genFieldNames;
	
	//集团排期表中排期日历前半部分的读取顺序
	private static String[] jtFieldNames;
	//关键词排期表中排期PC端的读取顺序
	private static String[] pcfieldName;
	//关键词排期表中排期Mobile端的读取顺序
	private static String[] mobilefieldName;
	
	private static LogUtil logUtil = new LogUtil(ScheduleProperties.class);
	
	/**
	 *	1、解析普通排期基础格式配置信息
	 *	2、排期表中排期日历前半部分的读取顺序
	 * @param genScheduleFileName	普通排期配置信息路径
	 */
	public static void setGenSchedule(String genScheduleFileName){
		Properties p = new Properties();
		genSchedule = new HashMap<>();
		int[] xy = null;
		String name;
		String value;
		
		try {
			p.load(new FileInputStream(genScheduleFileName));
			Enumeration<?> names = p.propertyNames();
			while (names.hasMoreElements()) {
				
				name = ((String) names.nextElement()).trim();
				//基础格式位置
				if(name.matches("[\\w]+,[\\w]+")){
					value = new String(p.getProperty(name).trim().getBytes("ISO-8859-1"),"UTF-8");
					
					xy = new int[2];
					xy[0] = Integer.parseInt(name.substring(0, 1)) - 1;
					xy[1] = XlsxUtil.getColumnNum(name.substring(name.indexOf(",")+1));
					
					genSchedule.put(xy, value);
				}
				//排期表中排期日历前半部分的读取顺序
				if("fieldName".equals(name)){
					value = p.getProperty(name).trim();
					genFieldNames = value.split(",");
				}
				
			}//end while()
		} catch (Exception e) {
			logUtil.logErrorExc(e);
			e.printStackTrace();
		}
	}//end setGenSchedule()
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 *	1、解析集团排期基础格式配置信息
	 *	2、排期表中排期日历前半部分的读取顺序
	 * @param jtScheduleFileName	集团排期配置信息路径
	 */
	public static void setJTSchedule(String jtScheduleFileName){
		Properties p = new Properties();
		jtSchedule = new HashMap<>();
		int[] xy = null;
		String name;
		String value;
		
		try {
			p.load(new FileInputStream(jtScheduleFileName));
			Enumeration<?> names = p.propertyNames();
			while (names.hasMoreElements()) {
				
				name = ((String) names.nextElement()).trim();
				//基础格式位置
				if(name.matches("[\\w]+,[\\w]+")){
					value = new String(p.getProperty(name).trim().getBytes("ISO-8859-1"),"UTF-8");
					
					xy = new int[2];
					xy[0] = Integer.parseInt(name.substring(0, 1)) - 1;
					xy[1] = XlsxUtil.getColumnNum(name.substring(name.indexOf(",")+1));
					
					jtSchedule.put(xy, value);
				}
				//排期表中排期日历前半部分的读取顺序
				if("fieldName".equals(name)){
					value = p.getProperty(name).trim();
					jtFieldNames = value.split(",");
				}
				
			}//end while()
		} catch (Exception e) {
			logUtil.logErrorExc(e);
			e.printStackTrace();
		}
	}//end setJTSchedule()
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 *	1、解析集团排期基础格式配置信息
	 *	2、排期表中排期日历前半部分的读取顺序
	 * @param jtScheduleFileName	集团排期配置信息路径
	 */
	public static void setKeySchedule(String keyScheduleFileName){
		Properties p = new Properties();
		keySchedule = new HashMap<>();
		int[] xy = null;
		String name;
		String value;
		
		try {
			p.load(new FileInputStream(keyScheduleFileName));
			Enumeration<?> names = p.propertyNames();
			while (names.hasMoreElements()) {
				
				name = ((String) names.nextElement()).trim();
				//基础格式位置
				if(name.matches("[\\w]+,[\\w]+")){
					value = new String(p.getProperty(name).trim().getBytes("ISO-8859-1"),"UTF-8");
					
					xy = new int[2];
					xy[0] = Integer.parseInt(name.substring(0, 1)) - 1;
					xy[1] = XlsxUtil.getColumnNum(name.substring(name.indexOf(",")+1));
					
					keySchedule.put(xy, value);
				}
				//排期表中排期PC端
				if("pcfieldName".equals(name)){
					value = p.getProperty(name).trim();
					pcfieldName = value.split(",");
				}
				//排期表中排期MOBILE端
				if("mobilefieldName".equals(name)){
					value = p.getProperty(name).trim();
					mobilefieldName = value.split(",");
				}
				
			}//end while()
		} catch (Exception e) {
			logUtil.logErrorExc(e);
			e.printStackTrace();
		}
	}//end setKeySchedule()

	public static Map<int[], String> getGenSchedule() {
		return genSchedule;
	}

	public static String[] getGenFieldNames() {
		return genFieldNames;
	}

	public static Map<int[], String> getJtSchedule() {
		return jtSchedule;
	}

	public static String[] getJtFieldNames() {
		return jtFieldNames;
	}

	public static Map<int[], String> getKeySchedule() {
		return keySchedule;
	}

	public static String[] getPcfieldName() {
		return pcfieldName;
	}

	public static String[] getMobilefieldName() {
		return mobilefieldName;
	}
	
}
