package com.xzc.concurrServer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xzc.concurr.pojo.BaseAnalysis;
import com.xzc.concurr.pojo.BaseAnalysisPojo;
import com.xzc.concurr.pojo.Blogger;
import com.xzc.concurr.pojo.Result;
import com.xzc.concurr.pojo.TransmitDetail;
import com.xzc.concurr.pojo.Transmitstatistics;
import com.xzc.concurrServer.util.CollectUtil;
import com.xzc.concurrServer.util.JDBCUtil;
import com.xzc.concurrServer.util.SqlConfig;

public class SqlWorker {

	/**
	 * 获取数据库的源数据
	 * @param conn 数据库连接
	 * @return 数据库源数据
	 */
	public static List<Blogger> getDateSource(Connection conn) {

		String sql = "SELECT `BlogID`,`UID`,`Transmits` "
				+ "FROM `urun_opinion_gz_blog`.`weibo` WHERE `Status` = 0 limit 5";

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
				int transmits = rs.getInt("Transmits");
				Blogger blogger = new Blogger();
				blogger.setBlogId(blogId);
				blogger.setUid(uid);
				blogger.setTransmits(transmits);
				list.add(blogger);
				ps1 = conn.prepareStatement("update weibo set Status=1 where BlogID=?");
				ps1.setObject(1, blogId);
				ps1.executeUpdate();
				System.out.println("got a BlogID "+blogId+":--" + new Date());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(ps0, ps1, rs);
			System.out.println("获取数据源------ " + new Date());

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
		ResultSet rs = null;
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

			System.out.println("完成入库");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(ps0, ps1, ps2, ps3, rs);

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
		ResultSet rs = null;
		try {
			for(BaseAnalysisPojo baseAnalysis : list) {
				try{

					ps1 = conn.prepareStatement("insert into baseanalysis (OutID,TypeID,AnalysisType,Name,Count,Code,Date,Time) values (?,?,?,?,?,?,?,?) ");
					ps1.setObject(1, baseAnalysis.getOutId());
					ps1.setObject(2, baseAnalysis.getTypeId());
					ps1.setObject(3, baseAnalysis.getAnalysisType());
					ps1.setObject(4, baseAnalysis.getName());
					ps1.setObject(5, baseAnalysis.getCount());
					ps1.setObject(6, baseAnalysis.getCode());
					ps1.setObject(7, baseAnalysis.getDate());
					ps1.setObject(8, baseAnalysis.getDateTime());
					ps1.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("=|=|=|=抛出异常catch到的： " + baseAnalysis.getName());
					baseAnalysis.setName("temp");
					System.out.println("|||----after abs:" + baseAnalysis.getName());
				}
			} 
			ps3 = conn.prepareStatement("update baseanalysis,face set baseanalysis.Code = face.ID where baseanalysis.Name = face.Phrase");
			ps3.executeUpdate();

			System.out.println("完成入库");

		} catch(Exception ex){
			ex.printStackTrace();
		} finally {
			JDBCUtil.close(ps0, ps1, ps2, ps3,rs);

		}

	}



	public static int saveTransDetailList (Connection conn, List<TransmitDetail> list) {

		PreparedStatement ps = null;
		try {
			for(TransmitDetail td : list) {
				try {

					ps = conn.prepareStatement("insert into transmitdetail"
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



	/**
	 * 测试方法，结果对线入库
	 * @param result
	 */
	public static void saveResult(Result result){
		Connection conn = null;
		try {
			conn = JDBCUtil.getMySQLConnection(SqlConfig.localUrl);
			BaseAnalysis localBa = result.getBaseAnalysis();
			ArrayList<TransmitDetail> finalTds = result.getTransmitDetails();
			//入库处理
			List<BaseAnalysisPojo> list = CollectUtil.toBaseAnalysisPojoList(localBa);
			SaveBaseAnalysisListTest(conn, list);
			saveTransDetailList(conn, finalTds);


			/**
			 * 保存transmitDetialstatis
			 */
			List<Transmitstatistics> transmitstaList = new ArrayList<>();
			for(int i = 1; i < 6; i ++){
				transmitstaList.addAll(
						findForStatistics(result.getResultId(), i, conn));
			}
			saveTransmitStasticsList(transmitstaList, conn);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}

	}




	/**
	 * 按层级查出来，放到list中
	 * @param AnalysisBlogID
	 * @param levelId
	 * @param connection
	 * @return
	 */
	public static List<Transmitstatistics> findForStatistics (
			String AnalysisBlogID, int levelId, Connection connection) {

		List<Transmitstatistics> list = new ArrayList<Transmitstatistics>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection
					.prepareStatement("select AnalysisBlogID,LevelID, SUM(Transmits)+1 Transtmis,SUM(Fans) Covers from `urun_opinion_gz_blog`.`transmitdetail`"
							+ " where LevelID=" + levelId + " and AnalysisBlogID=" + AnalysisBlogID);
			
			System.out.println("select AnalysisBlogID,LevelID, SUM(Transmits)+1 Transtmis,SUM(Fans) Covers from `urun_opinion_gz_blog`.`transmitdetail`"
							+ " where LevelID=" + levelId + " and AnalysisBlogID=" + AnalysisBlogID);
			rs = ps.executeQuery();
			while(rs.next()) {
				Transmitstatistics ts = new Transmitstatistics();
				if (rs.getString("AnalysisBlogID") != null) {
					ts.setAnalysisBlogId(rs.getString("AnalysisBlogID"));
					ts.setLevelID(rs.getInt("LevelID"));
					ts.setTransmits(rs.getInt("Transtmis"));;
					ts.setCovers(rs.getInt("Covers"));
					list.add(ts);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(ps, rs);
		}
		return list;
	}


	@SuppressWarnings("resource")
	public static int saveTransmitStasticsList (List<Transmitstatistics> list, Connection connection) {

		PreparedStatement ps = null;
		ResultSet resultSet = null;
		String tempAnalysisBlogId = "";
		try {
			for(Transmitstatistics tsl : list) {
				try{
					ps = connection
							.prepareStatement("select * from `urun_opinion_gz_blog`.`transmitstatistics`"
									+ " where AnalysisBlogID=? and LevelID=?");
					ps.setObject(1, tsl.getAnalysisBlogId());
					ps.setObject(2, tsl.getLevelID());
					resultSet = ps.executeQuery();
					if (!resultSet.next()) {
						ps = null;
						ps = connection
								.prepareStatement("insert into `urun_opinion_gz_blog`.`transmitstatistics`"
										+ " (AnalysisBlogID,LevelID,Transtmis,Covers)"
										+ " values (?,?,?,?)");
						ps.setObject(1, tsl.getAnalysisBlogId());
						ps.setObject(2, tsl.getLevelID());
						ps.setObject(3, tsl.getTransmits());
						ps.setObject(4, tsl.getCovers());
						ps.executeUpdate();
						tempAnalysisBlogId = tsl.getAnalysisBlogId();
						System.out.println("i am right here do this stuff ON ANALYSISI");
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			//set weibo.Status = 2
			setWeiboStatus(tempAnalysisBlogId,connection);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			if(connection == null)
				return 0;
			else
				return 1;
		} finally {
			try {
				if(resultSet != null)
					resultSet.close();
				if (ps != null) {
					ps.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				resultSet = null;
				ps = null;
			}
			//			con.closeConnection();
		}
	}



	public static int setWeiboStatus (String analysisBlogId, Connection connection) {

		PreparedStatement ps = null;
		ResultSet resultSet = null;

		try {
			try {
				ps = null;
				ps = connection.prepareStatement("update `urun_opinion_gz_blog`.`weibo` set Status=2 where BlogID=?");
				ps.setObject(1, analysisBlogId);
				ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			if(connection == null)
				return 0;
			else
				return 1;
		} finally {
			try {
				if(resultSet != null)
					resultSet.close();
				if (ps != null) {
					ps.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				resultSet = null;
				ps = null;
			}
			//			con.closeConnection();
		}
	}



}

