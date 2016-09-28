package com.xzc.concurrServer.util;

/**
 * 数据库配置
 * @author randall
 *
 */
public class SqlConfig {

	private SqlConfig() {
	}
	
	
	/**
	 * 正式库
	 */
	public static final String DBADDR1 = "27.17.18.131:38191/urun_opinion_gz_blog";
	public static final String USER1 = "yunrun";
	public static final String PASSWORD1 = "Yunrun2015!@#";
	public static final String officialUrl = "jdbc:mysql://"+DBADDR1+"?"
			+ "user="+USER1+"&password="+PASSWORD1+"&useUnicode=true&characterEncoding=UTF8";
	
	
	/**
	 * 测试库
	 */
	public static final String DBADDR2 = "14.152.90.76:38013/urun_opinion_gz_blog";
	public static final String USER2 = "yunrun";
	public static final String PASSWORD2 = "Yunrun2015!@#";
	public static final String testUrl = "jdbc:mysql://"+DBADDR2+"?"
			+ "user="+USER2+"&password="+PASSWORD2+"&useUnicode=true&characterEncoding=UTF8";
	
	/**
	 * 本地测试库
	 */
	public static final String DBADDR3 = "localhost:3306/urun_opinion_gz_blog";
	public static final String USER3 = "root";
	public static final String PASSWORD3 = "";
	public static final String localUrl = "jdbc:mysql://"+DBADDR3+"?"
			+ "user="+USER3+"&password="+PASSWORD3+"&useUnicode=true&characterEncoding=UTF8";
	
	
	/**
	 * 私人本地测试库
	 */
	public static final String DBADDR4 = "localhost:3306/urun_opinion_gz_blog";
	public static final String USER4 = "root";
	public static final String PASSWORD4 = "xiezhichun";
	public static final String myLocalUrl = "jdbc:mysql://"+DBADDR4+"?"
			+ "user="+USER4+"&password="+PASSWORD4+"&useUnicode=true&characterEncoding=UTF8";

	
	
}
