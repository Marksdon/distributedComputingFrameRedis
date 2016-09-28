package com.xzc.concurr.result;

import java.util.Map;

import com.xzc.concurr.pojo.Result;

/**
 * 封装结果池和登记池的类
 * @author randall
 *
 */
public class CollectionPool {
	
	private Map<String, Result> map;//结果池
	private RegistorCollection rc; //登记池
	public Map<String, Result> getMap() {
		return map;
	}
	public void setMap(Map<String, Result> map) {
		this.map = map;
	}
	public RegistorCollection getRc() {
		return rc;
	}
	public void setRc(RegistorCollection rc) {
		this.rc = rc;
	}
	
	public CollectionPool() {
	}
	
	public CollectionPool(Map<String, Result> map,
			RegistorCollection rc) {
		this.map = map;
		this.rc = rc;
	}
	
}
