package com.xzc.concurrServer.util;

/**
 * 当前类未使用
 * @author randall
 *
 */
public class SqlConfig {

	private SqlConfig() {
	}
	
	public static final String DBADDR2 = "localhost:3306/urun_opinion_gz_blog";
	public static final String USER2 = "root";
//	public static final String PASSWORD2 = "****";//无设置密码
	public static final String url = "jdbc:mysql://"+DBADDR2+"?"
			+ "user="+USER2+"&password=&useUnicode=true&characterEncoding=UTF8";
	
}
