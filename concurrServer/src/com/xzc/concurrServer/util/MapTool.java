package com.xzc.concurrServer.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MapTool {
	
	
	/**
	 * Map<String,Integer>类型的map按照value获取数值最大的topX个元素
	 * @param map 需要截取的map
	 * @param topX 截取的元素个数
	 * @return 截取出来的的topX个元素的map对象
	 */
	public static Map<String,Integer> getTopX(Map<String,Integer> map, int topX){
		
		Map<String, Integer> newMap = new HashMap<>();
		//转换
		ArrayList<Entry<String, Integer>> arrayList = new ArrayList<Entry<String, Integer>>(
					map.entrySet());
		//排序
		Collections.sort(arrayList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> map1,
						Map.Entry<String, Integer> map2) {
				return (map2.getValue() - map1.getValue());
			}
		});
		
		//输出
		int i = 0;
		for (Entry<String, Integer> entry : arrayList) {
			/*System.out.println(entry.getKey() + "\t" + entry.getValue());*/
			newMap.put(entry.getKey(), entry.getValue());
			i ++;
			if (i == topX) {
				break;
			}
		}
		return newMap;
	}
}
