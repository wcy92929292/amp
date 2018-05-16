package com.udbac.util;

public class StringBufferUtil {

	/**
	 * 字符串缓存追加
	 * @param sb
	 * @param datas
	 */
	public static StringBuffer append(StringBuffer sb,Object ... datas){
		for (int i = 0; i < datas.length; i++) {
			sb.append(datas[i]);
		}
		return sb;
	}
	
	/**
	 * 清空字符串缓存
	 * @param sb
	 */
	public static void clear(StringBuffer sb){
		sb.delete(0, sb.length());
	} 
	
	
	/**
	 * 位数不足，补前导0
	 * @param data
	 * @param length
	 */
	public static StringBuffer preZero(String data,int length){
		StringBuffer sb = new StringBuffer();
		for(;length > data.length();length--){
			sb.append("0");
		}
		return sb.append(data);
	}
	
}
