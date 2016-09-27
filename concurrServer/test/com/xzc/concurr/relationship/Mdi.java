package com.xzc.concurr.relationship;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.xzc.concurr.pojo.TransmitDetail;

public class Mdi {
	
	private List<TransmitDetail> sourceList;
	private List<TransmitDetail> fianlList;
	private BlockingQueue<TransmitDetail> queue;
	public List<TransmitDetail> getSourceList() {
		return sourceList;
	}
	public void setSourceList(List<TransmitDetail> sourceList) {
		this.sourceList = sourceList;
	}
	public List<TransmitDetail> getFianlList() {
		return fianlList;
	}
	public void setFianlList(List<TransmitDetail> fianlList) {
		this.fianlList = fianlList;
	}
	public BlockingQueue<TransmitDetail> getQueue() {
		return queue;
	}
	public void setQueue(BlockingQueue<TransmitDetail> queue) {
		this.queue = queue;
	}
	
	
	
}
