package com.xzc.concurrServer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class ConnectionPoolTest {

	public static void main(String[] args) throws SQLException {
		String sql = "select * from user";
		ConnectionPool pool = null;

		for (int i = 0; i < 2; i++) {
			pool = ConnectionPool.getInstance();
			Connection conn = pool.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString(1) + "\t\t" + rs.getString(2) + "\t\t" + rs.getString(3));
			}
			rs.close();
			stmt.close();
			pool.release(conn);
		}
		pool.closePool();
	}
	
	
	/**
	 * 测试连接池
	 */
	@Test 
	public void testConnectionPool(){
		String sql = "select * from user";
		ConnectionPool pool = null;
		for (int i = 0; i < 2; i++) {

			try {
				pool = ConnectionPool.getInstance();
				Connection conn = pool.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					System.out.println(rs.getString(1) + "\t\t" + rs.getString(2) + "\t\t" + rs.getString(3));
				}
				rs.close();
				stmt.close();
				pool.release(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		pool.closePool();
	}
}
