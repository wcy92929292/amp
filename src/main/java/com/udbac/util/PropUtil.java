/*
 * @(#)PropUtil.java 
 * 
 * Copyright (c) 2016 UDBAC.
 */
package com.udbac.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;


/**
 * 读取属性文件
 * 
 * @author Li Yan
 * @version 1.0 2016/4/6
 */
public class PropUtil {

	/** 属性情报 */
	private static Properties properties =null;

	/**
	 * 读取属性文件信息
	 * 
	 * @throws Exception
	 * 
	 */
	static {
		InputStream in = PropUtil.class.getClassLoader().getResourceAsStream("message.properties");
		properties = new Properties();
		try {
			properties.load(in);
			in.close();
		} catch (IOException e) {
			System.out.println("No config.properties defined error");
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				in = null;
			}
		}
	}

/*	 public static void load() throws Exception {
		// clear
		properties.clear();

		// 取得属性文件路径
		 String strMsgFilePath ="F://workspace1//amp//src//main//resources//message.properties";
		

		FileInputStream msgFileStream = null;
		try {
			msgFileStream = new FileInputStream(strMsgFilePath);
			properties.load(msgFileStream);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (msgFileStream != null) {
					msgFileStream.close();
				}
			} catch (IOException e) {
				msgFileStream = null;
			}
		}
	}*/
	/**
	 * 取得指定key的属性信息
	 * 
	 * @param strKey
	 *            属性的key
	 * @return 属性信息
	 */
	public static String getProperty(String strKey) {
		if (strKey == null) {
			return null;
		}
		if (properties == null) {
			return null;
		}
		return properties.getProperty(strKey);
	}

	/**
	 * 取得message内容
	 * 
	 * @param strMsgID
	 * 
	 * @return massage
	 */
	public static String getMessage(String strMsgID) {
		String[] strParams = null;
		return getMessage(strMsgID, strParams);
	}

	/**
	 * 取得指定msgid的msg内容
	 * 
	 * @param strMsgID
	 *            msgid
	 * @param strParams
	 *            msg参数（复数）
	 * @return msg内容
	 */
	public static String getMessage(String strMsgID, String[] strParams) {

		String strMsg = null;

		// 取得msg内容
		if (strMsgID != null) {
			strMsg = properties.getProperty(strMsgID);
		}

		if (strMsg == null) {
			return "无此msgId。msgId:" + strMsgID;
		}

		if (strParams == null) {
			return strMsg;
		}
		return MessageFormat.format(strMsg, (Object[]) strParams);
	}
	public static void main(String[] args) {
		String[] par={"参数1","参数2","参数3"};
		String message = PropUtil.getMessage("OLM-EA00001",par);
		System.out.println("InfoLog执行=========Message内容为：" + message);
	}

}
