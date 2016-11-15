package com.zxc.concurr.test;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 存取测试数据
 * <p>作为工具类
 * @author randall
 *
 */
public class DataStore {

	/**不允许实例化*/
	private DataStore() {
	}


	/**
	 * 读取文件数据
	 * @param dest 文件路径
	 * @return 文件数据，字符串对象
	 */
	public static String read(String dest) {
		//1.建立源
		File file = new File(dest);
		BufferedInputStream bis = null;
		StringBuilder builder = new StringBuilder();
		try {
			//2.选择流
			bis = new BufferedInputStream(
					new FileInputStream(file)
					);
			//3.读取
			byte[] flush = new byte[1024];
			int len = 0;
			while((len = bis.read(flush)) != -1){
				builder.append(new String(flush, 0, len));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//4.关闭资源
			close(bis);
		}
		return builder.toString();

	}



	/**
	 * 将字符串对象{@code src}写出到文件中
	 * @param destPath 目标文件路径
	 * @param src 源数据,字符串对象
	 */
	public static void write(String destPath, String src){
		File dest = new File(destPath);
		write(dest, src);
	}



	/**
	 * 将字符串对象{@code src}写出到文件{@code dest}中
	 * @param dest {@link File}对象
	 * @param src 源数据,字符串对象
	 */
	public static void write(File dest, String src){
		//1.建立源
		//2.选择流
		BufferedWriter bw = null;
		try {
			//3.写出文件
			//数据源src
			bw = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(dest)
							)
					);
			bw.write(src);
			bw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//4.关闭资源
			close(bw);
		}

	}



	/**
	 * 关闭所有{@link Closeable}的子类实现类
	 * @param t {@link Closeable}的子类实现类对象
	 */
	@SafeVarargs
	private static <T extends Closeable> void close(T ...t){
		for (T t2 : t) {
			if (t2 != null) {
				try {
					t2.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					t2 = null;
				}
			}
		}


	}


}
