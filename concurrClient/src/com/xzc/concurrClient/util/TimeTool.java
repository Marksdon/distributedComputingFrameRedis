package com.xzc.concurrClient.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeTool {
	
	public static long getDays(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy",Locale.ENGLISH);
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		return getDaySub(sdf1.format(date));
	}//水军
	
	public static long getDaySub(String beginDateStr) {
		long day=0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");    
		java.util.Date beginDate;
		java.util.Date endDate ;
		try
		{
			beginDate = format.parse(beginDateStr);
			endDate= format.parse(format.format(new Date()));   
			day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
			//			System.out.println("相隔的天数="+day);   
		} catch (ParseException e)
		{
			e.printStackTrace();
		}   
		return day;
	}
	
	
	/**
	 * 时区时间格式化为一个时间戳格式的时间
	 * @param timeStr 传入的时区时间
	 * @return 返回的时间戳格式时间
	 */
	public static String formatTimeOnStamps (String timeStr) {
		SimpleDateFormat sdf = 
				new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy",Locale.ENGLISH);
		Date date = null;
		try {
			date = sdf.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String finalTime = sdf1.format(date);
		return finalTime;
	}
	
	
	/**
	 * 时区时间格式转换为日期格式
	 * @param timeStr 传入的时区时间
	 * @return 日期格式的时间字符串
	 */
	public static String formatTimeOnDate (String timeStr) {
		SimpleDateFormat sdf = 
				new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy",Locale.ENGLISH);
		Date date = null;
		try {
			date = sdf.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		String finalTime = sdf1.format(date);
		return finalTime;
	}
}
