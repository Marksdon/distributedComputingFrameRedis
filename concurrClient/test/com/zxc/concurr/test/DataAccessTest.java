package com.zxc.concurr.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.xzc.concurr.pojo.BaseAnalysis;
import com.xzc.concurr.pojo.Result;
import com.xzc.concurrClient.men.Consumer;

/**
 * 文件数据交互测试
 * @author randall
 *
 */
public class DataAccessTest {

	public static void main(String[] args) {
		//1.请求获取数据
		/*Task task = new Task();
		task.setObj("4039569701839013");
		task.setiTask(1);
		String data = DownLoader.getData(task);
		//判断数据的可用性
		if (!DataProcessUtil.isValid(data)) {
			return ;
		}

		System.out.println(DataProcessUtil.isValid(data)?"可用":"无效");
		System.out.println(data);
		//2.持久化数据到文件
		DataStore.write("d:/data.txt", data);*/
		//3.读取数据
		String data = DataStore.read("d:/data.txt");

		System.out.println(data);
		System.out.println(DataProcessUtil.isValid(data)?"可用":"无效");

		//4.处理数据
		/*	String json = JsonUtil.getArticleIdentityList(ai, map);*/


		/**
		 * 
		 * 模拟处理数据
		 */

		Result result = new Result();
		result.setResultId("4039569701839013");
		result.setiResult(1);
		result.setSumResult(20);
		result.setBaseAnalysis(new BaseAnalysis());
		result.setTransmitDetails(new ArrayList<>());


		Consumer consumer = new Consumer();
		/**
		 * 封装算法,测试,已经设置私有
		 */
	/*	result = consumer.encapsulateResult(data, result);*/
		
		System.out.println(result);
		
		
		
		









	}




	private void test(String data){
		Result result = new Result();
		result.setResultId("4039569701839013");
		result.setiResult(1);
		result.setSumResult(1);
		result.setBaseAnalysis(new BaseAnalysis());
		result.setTransmitDetails(new ArrayList<>());

		/**
		 * 反射获取封装方法
		 */

		try {
			Class<?> clazz = Class.forName("com.xzc.concurrClient.men.Consumer");
			Method m = clazz.getDeclaredMethod("encapsulateResult", String.class, Result.class);
			m.setAccessible(true);
			result = (Result)m.invoke(clazz, data,result);//这里修改封装数据方法

			result.getBaseAnalysis().setAnalysisBlogId(result.getResultId());




		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}












	/**
	 * 获取请求参数
	 * @return 请求参数
	 */
	/*private static Map<String, String> accessParam(ArticleInfo ai){
		Map<String, String> map = new HashMap<>();
		map.put("starttime", "20161027");
		map.put("endtime", "20161102");
		map.put("MD2", ai.getMd2());
		map.put("page", "1");
		map.put("rows", "30");
		return map;
	}*/












}
