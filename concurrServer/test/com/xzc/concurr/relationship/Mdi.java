package com.xzc.concurr.relationship;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import com.xzc.concurr.pojo.TransmitDetail;

public class Mdi {
	
	private ArrayList<TransmitDetail> sourceList;
	private ArrayList<TransmitDetail> fianlList;
	private BlockingQueue<TransmitDetail> queue;
	public ArrayList<TransmitDetail> getSourceList() {
		return sourceList;
	}
	public void setSourceList(ArrayList<TransmitDetail> sourceList) {
		this.sourceList = sourceList;
	}
	public ArrayList<TransmitDetail> getFianlList() {
		return fianlList;
	}
	public void setFianlList(ArrayList<TransmitDetail> fianlList) {
		this.fianlList = fianlList;
	}
	public BlockingQueue<TransmitDetail> getQueue() {
		return queue;
	}
	public void setQueue(BlockingQueue<TransmitDetail> queue) {
		this.queue = queue;
	}
	
	
	
	
}
