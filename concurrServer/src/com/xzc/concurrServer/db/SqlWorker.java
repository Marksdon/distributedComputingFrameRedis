package com.xzc.concurrServer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xzc.concurr.pojo.Blogger;
import com.xzc.concurrServer.util.JDBCUtil;

public class SqlWorker {

	//getDataSource()
	//saveReuslt()

	/**
	 * 获取数据库的源数据
	 * @param conn 数据库连接
	 * @return 数据库源数据
	 */
	public static List<Blogger> getDateSource(Connection conn) {

//		String sql = "SELECT `BlogID`,`UID`,`Transmits` FROM `weibo` WHERE `Status` = 0";
		String sql = "SELECT `BlogID`,`UID`,`Transmits` FROM `weibo` limit 5";

		PreparedStatement ps0 = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		List<Blogger> list = new ArrayList<>();
		try {
			ps0 = conn.prepareStatement(sql);
			rs = ps0.executeQuery();
			while(rs.next()){
				//get data
				String blogId = rs.getString("BlogID");
				String uid = rs.getString("UID");
				String transmits = rs.getString("Transmits");
				Blogger blogger = new Blogger();
				blogger.setBlogId(blogId);
				blogger.setUid(uid);
				blogger.setTransmits(transmits);
				list.add(blogger);
				/*ps1 = conn.prepareStatement("update weibo set Status=1 where BlogID=?");
				ps1.setObject(1, blogId);
				ps1.executeUpdate();*/
				System.out.println("got a BlogID :--" + new Date());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(ps0, ps1, rs);
		}
		return list;
	}






}
