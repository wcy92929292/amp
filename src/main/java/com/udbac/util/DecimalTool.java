package com.udbac.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 数字工具类
 * @author LFQ
 * @date 2016-04-26
 */
public class DecimalTool {

	/**
	 * 将数字字符串转换成用千位分隔符的整数字符串
	 * @param data
	 * @return 
	 */
	public String bitSeparator(String data){
		if(data== null || (!data.matches("\\d{0,}") && !data.contains("E") && !data.contains("e"))){
			return null;
		}
		//先判断是否为科学计数法数字
		BigDecimal bd = new BigDecimal(data);
		return df.format(bd);
	} //end bitSeparator()
	private static DecimalFormat df = new DecimalFormat("###,###");
	
	
	
	/**
	 * 将小数   转换为   小数点两位百分比数
	 * @param data
	 * @return
	 */
	public static  String percent(Double data){
		if(data == null){
			return null;
		}
		return perDF.format(data);
	}//end percent
	private static final DecimalFormat perDF = new DecimalFormat("#00.00%");
	
	/**
	 * 将小数，科学数字转成十进制整数字符串
	 * @return
	 */
	public static String positiveInteger(String data){
		BigDecimal bd = null;
		bd = new BigDecimal(data);
		return poInt.format(bd);
	}	
	private static final DecimalFormat poInt = new DecimalFormat("#");
	
    /**
     * 半角数字check
     * @param str
     * @return check结果
     */
    public static boolean isNumeric(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
	
    public static Double int2Double(Integer intNum){
    	return intNum == null ? null : Double.valueOf(intNum); 
    }
}
