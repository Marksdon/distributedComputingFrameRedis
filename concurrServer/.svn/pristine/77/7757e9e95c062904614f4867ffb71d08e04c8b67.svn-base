package com.xzc.concurrServer.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

/**
 * 提供MySQL，Oracle 等数据库操作工具
 * 包括获得数据库链接，关闭数据库结果集，关闭数据库会话等方法
 */
public class JDBCUtil {

	private static String url;
	public JDBCUtil(String inUrl) {
		url = inUrl;
	}

	/**
	 * 获取MySQL的连接，如果获取不成功将将连续获取10次
	 * @return MySQL数据库连接connection
	 */
	public static Connection getMySQLConnection() {
		int flag = 0;
		while(flag < 10) {
			try {
				//载入MySQL驱动
				Class.forName("com.mysql.jdbc.Driver");
				//获取connection	
				Connection conn = DriverManager.getConnection(url);
				//返回connection
				if (conn != null) {
					return conn;
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			flag ++;
		}
		return null;
	}
	
	
	/**
	 * 获取MySQL的连接，如果获取不成功将将连续获取10次
	 * @return MySQL数据库连接connection
	 */
	public static Connection getMySQLConnection(String inUrl) {
		int flag = 0;
		while(flag < 10) {
			try {
				//载入MySQL驱动
				Class.forName("com.mysql.jdbc.Driver");
				//获取connection	
				Connection conn = DriverManager.getConnection(inUrl);
				//返回connection
				if (conn != null) {
					return conn;
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			flag ++;
		}
		return null;
	}


	/**
	 * 关闭数据库连
	 * @param conn 数据库connection
	 */
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				conn = null;
			}
		}
	}


	/**
	 * 关闭一个数据会话
	 * @param stmt 数据库会话
	 */
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				stmt = null;
			}
		}
	}

	/**
	 * 关闭一个数据库会话，一个结果集
	 * @param stmt 数据库会话
	 * @param rs 结果集
	 */
	public static void close(Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs = null;
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				stmt = null;
			}
		}

	}
	
	
	
	/**
	 * 关闭一个数据库会话 preparedStatement ，一个结果集
	 * @param ps 数据库会话
	 * @param rs 结果集
	 */
	public static void close(PreparedStatement ps, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs = null;
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ps = null;
			}
		}

	}
	
	
	/**
	 * 关闭两个数据库会话，一个结果集
	 * @param ps0 数据库会话
	 * @param ps1 数据库会话
	 * @param rs 结果集
	 */
	public static void close(PreparedStatement ps0, PreparedStatement ps1, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs = null;
			}
		}
		if (ps0 != null) {
			try {
				ps0.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ps0 = null;
			}
		}
		if (ps1 != null) {
			try {
				ps1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ps1 = null;
			}
		}

	}


	/**
	 * 关闭两个数据库会话
	 * @param stmt0 数据库会话
	 * @param stmt1 数据库会话
	 */
	public static void close(Statement stmt0,Statement stmt1) {
		if (stmt0 != null) {
			try {
				stmt0.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				stmt0 = null;
			}
		}
		if (stmt1 != null) {
			try {
				stmt1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				stmt1 = null;
			}
		}
	}

	/**
	 * 关闭两个数据库会话和一个结果集
	 * @param stmt0 数据库会话
	 * @param stmt1 数据库会话
	 * @param rs 结果集
	 */
	public static void close(Statement stmt0,Statement stmt1,ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs = null;
			}
		}
		if (stmt0 != null) {
			try {
				stmt0.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				stmt0 = null;
			}
		}
		if (stmt1 != null) {
			try {
				stmt1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				stmt1 = null;
			}
		}
	}





}
