package com.xzc.concurr.relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import com.xzc.concurr.pojo.TransmitDetail;

public class Relationship {
	
	/**
	 * 测试版方法，建立关系
	 * @param sourceList
	 * @return
	 */
	public static ArrayList<TransmitDetail> buildRelationship(ArrayList<TransmitDetail> sourceList,
			TransmitDetail sourceTd){
		if (sourceList.size() > 0) {
			sourceList.remove(0);
		}
		ArrayList<TransmitDetail> fianlList = new ArrayList<>();
		Mdi mdi = f1(sourceList, fianlList,sourceTd, 3);
		for(int i = 2; i < 6; i ++){
			mdi = fk(mdi, i);
		}
		mdi.getFianlList().addAll(mdi.getQueue());

		ArrayList<TransmitDetail> list = mdi.getFianlList();
		
		travel(list);
		System.out.println(list.size());
		return list;
	}
	
	
	/**
	 * 建立第一层关系
	 * @param sourceList
	 * @param random  3-5
	 */
	public static Mdi f1(ArrayList<TransmitDetail> sourceList, 
			ArrayList<TransmitDetail> fianlList,
			TransmitDetail sourceTd, int random){

		BlockingQueue<TransmitDetail> queue = new LinkedBlockingQueue<>();
//		sourceTd.setName("源头");
		if (sourceTd == null) {
			sourceTd = new TransmitDetail();
			sourceTd.setUid("00000000");
		}
		sourceTd.setLevelId(0);
		fianlList.add(sourceTd);
		
		for(int i = 0; i < random; i ++){
			if (sourceList.size() > 0) {
				//获取random个T对象出来，设置fromId,next=0
				TransmitDetail td = sourceList.get(0);
				sourceList.remove(0);
				//set td
				td.setFromUid(sourceTd.getUid());
				td.setLevelId(1);
				queue.offer(td);//设置完属性后，进入队列
			}
		}

		//返回sourceList对象和queue对象
		Mdi mdi = new Mdi();
		mdi.setFianlList(fianlList);
		mdi.setQueue(queue);
		mdi.setSourceList(sourceList);
		return mdi;	
	}


	/**
	 * 测试，建立第k层关系
	 * @param mdi
	 * @param k
	 * @return
	 */
	public static Mdi fk(Mdi mdi, int k){

		ArrayList<TransmitDetail> sourceList = mdi.getSourceList();
		BlockingQueue<TransmitDetail> queue = mdi.getQueue();
		ArrayList<TransmitDetail> finalList = mdi.getFianlList();

		while(queue.size() > 0){
			System.out.println(queue.size());
			//获取random个T对象出来，设置fromId,next=0
			TransmitDetail ctd;
			try {
				ctd = queue.take();
				String previousUid = ctd.getUid();
				if (ctd.getLevelId() > k-1) {
					break;
				}

				finalList.add(ctd);//可以加入树，成为第二层

				int random = 2;//随机生成1-3
				for(int i = 0; i < random; i ++){
					if (sourceList.size() > 0) {
						//获取random个T对象出来，设置fromId,next=0
						TransmitDetail td = sourceList.get(0);
						sourceList.remove(0);
						//set td
						td.setFromUid(previousUid);
						td.setLevelId(k);
						queue.offer(td);//设置完属性后，进入队列
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		//返回队列queue,sourceList,finalList
		mdi.setFianlList(finalList);
		mdi.setQueue(queue);
		mdi.setSourceList(sourceList);
		return mdi;	
	}
	
	
	
	
	public static List<TransmitDetail> initTD(int random){
		List<TransmitDetail> list = new ArrayList<>();
		Random ra =new Random();
		for(int i = 0; i < random; i ++){
			TransmitDetail td = new TransmitDetail();
			td.setAnalysisBlogId("11111111111111");
			td.setName("名"+i*3);
			td.setUid(""+ra.nextInt(1000000000)+1);
			td.setLevelId(1);
			list.add(td);
		}
		return list;
	}


	@Test
	public void test(){
		//		List<TransmitDetail> list = initTD(10);
		//		for(TransmitDetail td: list){
		//			System.out.println(td.getAnalysisBlogId()+"|"+td.getName()+"|"
		//					+td.getUid()+"|"+td.getLevelId());
		//		}
		int i = 0;
		while(i < 100){
			System.out.println(getRandomInt());
			i ++;
		}
	}



	/**
	 * 产生3-5的随机数
	 * @return
	 */
	public static int getRandomInt(){
		Random ra = new Random();
		int random = ra.nextInt(10)+1;
		return (random%5 < 2) ? random%5+4 : random%5+1;
	}


	public static void travel(List<TransmitDetail> list){
		for(TransmitDetail td: list){
			System.out.println(td.getAnalysisBlogId()+"|"+td.getName()+"|"
					+td.getUid()+"|"+td.getLevelId()+"|"+td.getFromUid());
		}
	}

}
