package com.xzc.concurrServer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xzc.concurr.pojo.BaseAnalysisPojo;
import com.xzc.concurr.pojo.Blogger;
import com.xzc.concurr.pojo.TransmitDetail;
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
		String sql = "SELECT `BlogID`,`UID`,`Transmits` "
				+ "FROM `urun_opinion_gz_blog`.`weibo` WHERE `Status` = 0 limit 5";

		PreparedStatement ps0 = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		List<Blogger> list = new ArrayList<>();
		try {
			ps0 = conn.prepareStatement(sql);
			rs = ps0.executeQuery();
			while(rs.next()){
				//get data
				String blogId = rs.getString("BlogID");
				String uid = rs.getString("UID");
				int transmits = rs.getInt("Transmits");
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
			
			/**
			 * 测试
			 */
//			ps1 = conn.prepareStatement("update urun_opinion_gz_blog.weibo set STATUS = 3 where weibo.BlogID = '4021790348672058';");
//			ps1.executeUpdate();
//			
//			ps2 = conn.prepareStatement("update urun_opinion_gz_blog.weibo set STATUS = 0 where weibo.BlogID = '3374583573236496'");
//			ps2.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(ps0, rs);
			JDBCUtil.close(ps0, ps1, ps2, rs);
			
		}
		return list;
	}



	/**
	 * 测试入库方法
	 * @param conn
	 * @param list
	 * @return
	 */
	public static void SaveBaseAnalysisList(Connection conn, List<BaseAnalysisPojo> list) {

		PreparedStatement ps0 = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		String flagId = null;
		try {
			for(BaseAnalysisPojo baseAnalysis : list) {

				try{
					ps0 = conn.prepareStatement("select OutID from baseanalysis_copy where OutID=? and TypeID=? and AnalysisType=? and Name=? and Date=?");
					ps0.setObject(1, baseAnalysis.getOutId());
					ps0.setObject(2, baseAnalysis.getTypeId());
					ps0.setObject(3, baseAnalysis.getAnalysisType());
					ps0.setObject(4, baseAnalysis.getName());
					ps0.setObject(5, baseAnalysis.getDate());
					rs = ps0.executeQuery();
					if (!rs.next()) {
						try {
							ps1 = conn.prepareStatement("insert into baseanalysis_copy (OutID,TypeID,AnalysisType,Name,Count,Code,Date,Time) values (?,?,?,?,?,?,?,?) ");
							ps1.setObject(1, baseAnalysis.getOutId());
							ps1.setObject(2, baseAnalysis.getTypeId());
							ps1.setObject(3, baseAnalysis.getAnalysisType());
							ps1.setObject(4, baseAnalysis.getName());
							ps1.setObject(5, baseAnalysis.getCount());
							ps1.setObject(6, baseAnalysis.getCode());
							ps1.setObject(7, baseAnalysis.getDate());
							ps1.setObject(8, baseAnalysis.getDateTime());
							ps1.executeUpdate();
							flagId = baseAnalysis.getOutId();
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("=|=|=|=抛出异常catch到的： " + baseAnalysis.getName());
							baseAnalysis.setName("temp");
							System.out.println("|||----after abs:" + baseAnalysis.getName());
						}
					} 
					else {
						try {
							ps2 = conn.prepareStatement("update baseanalysis_copy set Count=?,Date=?,Time=? where OutID=? and TypeID=? and AnalysisType=? and Name=? and Date=?");
							ps2.setObject(1, baseAnalysis.getCount());
							ps2.setObject(2, baseAnalysis.getDate());
							ps2.setObject(3, baseAnalysis.getDateTime());
							ps2.setObject(4, baseAnalysis.getOutId());
							ps2.setObject(5, baseAnalysis.getTypeId());
							ps2.setObject(6, baseAnalysis.getAnalysisType());
							ps2.setObject(7, baseAnalysis.getName());
							ps2.setObject(8, baseAnalysis.getDate());
							ps2.executeUpdate();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch(Exception ex){
					ex.printStackTrace();
				}
			}

			ps3 = conn.prepareStatement("update baseanalysis_copy,face set baseanalysis_copy.Code = face.ID where baseanalysis_copy.Name = face.Phrase");
			ps3.executeUpdate();
			ps4 = conn.prepareStatement("update weibo set Status=0 where BlogID=?");
			ps4.setObject(1, flagId);
			ps4.executeUpdate();
			System.out.println("完成入库");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(ps0, ps1, ps2, ps3, ps4,rs);

		}
	}




	/**
	 * 测试入库
	 * @param conn
	 * @param list
	 */
	public static void SaveBaseAnalysisListTest(Connection conn, List<BaseAnalysisPojo> list) {

		PreparedStatement ps0 = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		String flagId = null;
		try {
			for(BaseAnalysisPojo baseAnalysis : list) {
				try{

					ps1 = conn.prepareStatement("insert into baseanalysis_copy (OutID,TypeID,AnalysisType,Name,Count,Code,Date,Time) values (?,?,?,?,?,?,?,?) ");
					ps1.setObject(1, baseAnalysis.getOutId());
					ps1.setObject(2, baseAnalysis.getTypeId());
					ps1.setObject(3, baseAnalysis.getAnalysisType());
					ps1.setObject(4, baseAnalysis.getName());
					ps1.setObject(5, baseAnalysis.getCount());
					ps1.setObject(6, baseAnalysis.getCode());
					ps1.setObject(7, baseAnalysis.getDate());
					ps1.setObject(8, baseAnalysis.getDateTime());
					ps1.executeUpdate();
					flagId = baseAnalysis.getOutId();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("=|=|=|=抛出异常catch到的： " + baseAnalysis.getName());
					baseAnalysis.setName("temp");
					System.out.println("|||----after abs:" + baseAnalysis.getName());
				}
			} 
			ps3 = conn.prepareStatement("update baseanalysis_copy,face set baseanalysis_copy.Code = face.ID where baseanalysis_copy.Name = face.Phrase");
			ps3.executeUpdate();
			ps4 = conn.prepareStatement("update weibo set Status=0 where BlogID=?");
			ps4.setObject(1, flagId);
			ps4.executeUpdate();
			System.out.println("完成入库");

		} catch(Exception ex){
			ex.printStackTrace();
		} finally {
			JDBCUtil.close(ps0, ps1, ps2, ps3, ps4,rs);

		}

	}



	public static int saveTransDetailList (Connection conn, List<TransmitDetail> list) {

		PreparedStatement ps = null;
		try {
			for(TransmitDetail td : list) {
				try {

					ps = conn.prepareStatement("insert into transmitdetail_copy"
							+ " (AnalysisBlogID,LevelID,UID,Name,HeadUrl,Url,Transmits,Province,City,Posts,Fans,FromUID,BlogID,Comments,Content,Time)"
							+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					ps.setObject(1, td.getAnalysisBlogId());
					ps.setObject(2, td.getLevelId());
					ps.setObject(3, td.getUid());
					ps.setObject(4, td.getName());
					ps.setObject(5, td.getHeadUrl());
					ps.setObject(6, td.getUrl());
					ps.setObject(7, td.getTransmits());
					ps.setObject(8, td.getProvince());
					ps.setObject(9, td.getCity());
					ps.setObject(10, td.getPosts());
					ps.setObject(11, td.getFans());
					ps.setObject(12, td.getFromUid());
					ps.setObject(13, td.getBlogId());
					ps.setObject(14, td.getComments());
					ps.setObject(15, td.getContent());
					ps.setObject(16, td.getTime());
					ps.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("insert DB wrong:" + td.getContent());
				}
			}
		} finally {
			JDBCUtil.close(ps);
		}
		return 1;
	}

}

