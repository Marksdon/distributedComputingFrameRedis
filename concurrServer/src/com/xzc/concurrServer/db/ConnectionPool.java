package com.xzc.concurrServer.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

public class ConnectionPool {
	private Vector<Connection> pool;
	private String url;
	private String username;
	private String password;
	private String driverClassName;
	private int poolSize = 2;
	private static ConnectionPool instance = null;


	//私有构造方法，禁止外部创建本类的对象，要想获得本类的对象，通过<code>getInstance</code>方法
	private ConnectionPool() {
		/*System.out.println("构造函数");*/
		init();
	}


	//连接池初始化方法，读取属性文件的内容，建立连接池中的初始连接
	private void init(){
		readConfig();
		pool = new Vector<Connection>(poolSize);
		addConnection();
	}

	//返回连接到连接池中
	public synchronized void release(Connection coon){
		pool.add(coon);
	}

	//关闭连接池中的所有数据库连接
	public synchronized void closePool(){
		for (int i = 0; i < pool.size(); i++) {
			try {
				((Connection)pool.get(i)).close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			pool.remove(i);
		}
	}

	//返回当前连接池的一个对象
	public static ConnectionPool getInstance(){
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	//返回连接池中的一个数据库连接
/*	public synchronized Connection getConnection(){
		if (pool.size() > 0) {
			Connection conn = pool.get(0);
			pool.remove(conn);
			return conn;
		}else {
			return null;

		}
	}*/

	/**
	 * 返回一个数据库连接对象connection
	 * @return 数据库连接对象
	 * 如果连接为空，则抛出exception，说明连接池为空
	 */
	public synchronized Connection getConnection(){
		while (pool.size() > 0) {
			Connection conn = pool.get(0);
			if (conn != null) {
				pool.remove(conn);
				return conn;
			}
		} 
		try {
			throw new Throwable("the pool is empty!");
		} catch (Throwable e) {
			e.printStackTrace();
			e.getMessage();
			return null;
		}

	}



	//在连接池中创建初始设置的数据库连接
	private void addConnection(){
		Connection coon = null;
		for (int i = 0; i < poolSize; i++) {
			try {
				Class.forName(driverClassName);
				coon = java.sql.DriverManager.getConnection(url, username, password);
				pool.add(coon);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}



	//读取设置连接池的属性文件
	private void readConfig(){
		try {
//			String path = System.getProperty("user.dir") + "\\dbpool.properties";
			InputStream is = ConnectionPool.class.getClassLoader()
			.getResourceAsStream("dbpool.properties");
//			FileInputStream is = new FileInputStream(path);
			Properties props = new Properties();
			props.load(is);
			this.driverClassName = props.getProperty("driverClassName");
			this.username = props.getProperty("username");
			this.password = props.getProperty("password");
			this.url = props.getProperty("url");
			this.poolSize = Integer.parseInt(props.getProperty("poolSize"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("属性文件找不到");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("读取属性文件出错");
		}
	}


}
