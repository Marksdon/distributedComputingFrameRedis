package com.xzc.concurrClient.men;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.xzc.concurr.pojo.BaseAnalysis;
import com.xzc.concurr.pojo.Result;
import com.xzc.concurr.pojo.Task;
import com.xzc.concurr.pojo.TransmitDetail;
import com.xzc.concurrClient.util.CountUtil;
import com.xzc.concurrClient.util.DownLoader;
import com.xzc.concurrClient.util.EmojiFilterUtils;
import com.xzc.concurrClient.util.RedisUtilSyn;
import com.xzc.concurrClient.util.SerializeUtil;
import com.xzc.concurrClient.util.ThreadUtil;
import com.xzc.concurrClient.util.TimeTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

public class Consumer implements Runnable{

	@Override
	public void run() {
		Task task;
		Result result;
		Jedis jedis = null;
		while (true) {
			try {
				jedis = RedisUtilSyn.getJedis(1);
				byte[] bArr = jedis.lpop("taskQueueTest".getBytes());
				
				if (bArr == null) {
					//暂停2秒
					ThreadUtil.slowThread();
					System.out.println("barr is null on consumer");
				} else if (bArr != null) {
					task = (Task)SerializeUtil.unserialize(bArr);
					System.out.println("consume task: " + task.getiTask());
					//计算任务
					result = countTask(task);
					//计算结果进入队列
					jedis.rpush("resultQueueTest".getBytes(),
							SerializeUtil.serialize(result));
					System.out.println("rpush one: " + result.getiResult());
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (jedis != null) {
					RedisUtilSyn.returnResource(jedis);
				}
			}

		}
	}

	/**
	 * 传入任务对象{@code task}，获得数据，解析计算数据获得结果对象result
	 * 1.获取数据;2.解析数据;3.封装对象;4.返回数据
	 * @param task 需要计算的任务对象
	 * @return 封装好的结果对象result
	 */
	private Result countTask(Task task){
		//初始化结果对象
		Result result = initResult(task);
		//获取数据
		String data = DownLoader.getData(task);
		//检验数据是否可用
		if (!isValiable(data)) {
			return result;//任务计算失败
		}
		//解析并封装数据
		result = encapsulateResult(data,result);
		result.getBaseAnalysis().setAnalysisBlogId(result.getResultId());
		return result;
	}


	/**
	 * 解析数据{@code data},并且封装到结果对象result
	 * @param data 传进来的数据
	 * @return 封装好的result对象
	 */
	private Result encapsulateResult(String data, Result result){
		//具体解析
		JSONObject jsonObject = JSONObject.fromObject(data);//为处理异常
		JSONArray aJsonArray = jsonObject.getJSONArray("reposts");
		Map<String, Map<String, Integer>> tranComMap = new HashMap<>();
		Map<String, Integer> expressionMap = new HashMap<>();
		Map<String, Integer> keywordMap = new HashMap<>();
		
		for(int j=0;j<aJsonArray.size();j++) {
			JSONObject jb = aJsonArray.getJSONObject(j);
			//text级别,没有加上可用性判断
			String text = EmojiFilterUtils.filterEmoji(jb.getString("text"));
			String date = TimeTool.formatTimeOnDate(jb.getString("created_at"));
			tranComMap = CountUtil.parseTranCom(text, date, tranComMap);//转发数
			expressionMap = CountUtil.parseExpression(text, expressionMap);//表情
			keywordMap = CountUtil.parseKeyword(text, keywordMap);//关键字
			//产生一个TransmitDetail对象并封装部分属性
			TransmitDetail td = CountUtil.setTransmitDetail(jb, text);
			//用户系列,用户级别
			String user = jb.getString("user");
			result = CountUtil.countUserSerial(user, result, td);
			//TransmitDetail对象数据并没有完全封装
		}
		result.getBaseAnalysis().setTranCom(tranComMap);
		result.getBaseAnalysis().setExpressionMap(expressionMap);
		result.getBaseAnalysis().setKeyworkMap(keywordMap);
		result.setFlag(true);//计算任务成功
		return result;
	}


	/**
	 * 确保数据是否有效可用
	 * @param data 已经获得的数据
	 * @return true有效可用，false无效不可用
	 */
	private boolean isValiable(String data){
		return (data != null && data.length() > 250) ? true : false;
	}
	
	/**
	 * 初始化结果对象，设置结果对象属性，任务id,第i个任务，总的任务数，相关结果对象中有
	 * @param task 任务对象
	 * @return 初始化后的结果对象
	 */
	private Result initResult(Task task){
		Result result = new Result();
		result.setResultId((String)task.getObj());
		result.setiResult(task.getiTask());
		result.setSumResult(task.getSumTask());
		result.setBaseAnalysis(new BaseAnalysis());
		result.setTransmitDetails(new ArrayList<>());
		return result;
	}


}
