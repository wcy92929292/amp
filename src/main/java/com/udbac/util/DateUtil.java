package com.udbac.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期工具类
 * @author LFQ
 *
 */
public class DateUtil {

	//默认格式
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 获取日期字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateStr(Date date,String format){
		String dstr = "";
		if(format== null || "".equals(format)){
			dstr =  dateFormat.format(date);
		}else{
			dstr = new SimpleDateFormat(format).format(date);
		}
		return dstr;
	}
	
	
	/**
	 * 将字符日期转换成日期
	 * @param dateStr 
	 * @param format
	 * @return
	 */
	public static Date getDate(String dateStr,String format){
		
		Date d = null;
		try {
			if(format == null){
				d = dateFormat.parse(dateStr);
			}else{
				d = new SimpleDateFormat(format).parse(dateStr);
			}
		} catch (ParseException e) {}
		return d;
	}//end getDate(String dateStr,String format)
	
	/**
	 * 年月日转成日期
	 * @return
	 */
	public static Date getDate(int year,int month,int day){
		
		StringBuffer sb = new StringBuffer(15);
		sb.append(year);
		sb.append("-");
		sb.append(month);
		sb.append("-");
		sb.append(day);
		return isLegalDate(sb.toString(), "-") ? getDate(sb.toString(),null): null;
	}
	
	
	/**
	 * 获取第二天
	 * @return
	 */
	public static String torrow(){
		Date torrow = new Date(System.currentTimeMillis()+86400000);//获取次日的日期
		String torrowStr = getDateStr(torrow, "yyyy-MM-dd");
		return torrowStr;
	}
	
	/**
	 * 是否为过去的时间、
	 * @param date
	 * @return
	 */
	public static int isPast(String dateStr,String format){
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		try {
			return sdf.parse(dateStr).compareTo(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return -1;
	}//end isPast
	
	/**
	 * 是否为闰年
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year){
		return (year%100==0) ? year%400==0 : year%4==0;
	}	
	
	
	/**
	 * 是否未合法日期
	 * @return
	 */
	public static boolean isLegalDate(String dateStr,String token){
		String date[] = dateStr.split(token);
		return isLegalDate(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
	}
	
	
	/**
	 * 是否未合法日期
	 * @return
	 */
	public static boolean isLegalDate(int year,int month,int day){
		boolean z = false;
		if(year > 0){
			switch (month) {
				case 1:
				case 3:
				case 5:
				case 7:
				case 8:
				case 10:
				case 12: {
					z = (0 < day && day <= 31);
					break;
				}
				case 4:
				case 6:
				case 9:
				case 11:{ 
					z = (0 < day && day <= 30);
					break;
				}
				case 2: {
					z = (0 < day && day <= (isLeapYear(year)?29:28));
					break;
				}
				default:break;
			}
		}
		return z;
	}//end isLegalDate()
	
	/**
	 *	获取某天当月最大的日期
	 *	1 3 5 7 8 10 12 == 31
	 *	2 == leap ? 29 : 28
	 *	4 6 9 11 == 30
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getMonthLastDate(Date date){
		int lastDay;
		switch (date.getMonth()) {	//date.getMonth 是从 0开始算的
			case 3:
			case 5:
			case 8:
			case 10:{
				lastDay = 30;
				break;
			}
			case 1:{
				lastDay = (isLeapYear(date.getYear()) ? 29 : 28);
				break;
			}
			default:
				lastDay = 31;
			break;
		}
		
		return lastDay;
	}
	 public static void main(String[] args) {
		Date date=new Date();
		System.out.println(getMonthDay(date));
	}
	//获得系统的上一周
	 public static Date getWeekDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +7);
		date = calendar.getTime();
		return date;
	}
	 
	 public static Date getMonthDay(Date date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, +1);
			date = calendar.getTime();
			return date;
		}
	
}
