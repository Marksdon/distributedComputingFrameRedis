package com.zxc.concurr.test;

/**
 * 数据处理工具类
 * @author randall
 *
 */
public class DataProcessUtil {

	/**不允许实例化*/
	private DataProcessUtil() {
	}
	
	
	/**
	 * 判断json数据是否可以解析
	 * @param src json数据,字符串对象 
	 */
	public static boolean isValid(String src){
		if (src == null || src.trim().equals("") 
				|| !src.startsWith("{")) {
			return false;
		}
		return true;

	}
	
	
	
	
}
