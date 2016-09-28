package com.xzc.concurr.pojo;

public class Transmitstatistics {
	private String analysisBlogId;
	private int levelID;
	private int transmits;
	private int covers;
	public String getAnalysisBlogId() {
		return analysisBlogId;
	}
	public void setAnalysisBlogId(String analysisBlogId) {
		this.analysisBlogId = analysisBlogId;
	}
	public int getLevelID() {
		return levelID;
	}
	public void setLevelID(int levelID) {
		this.levelID = levelID;
	}
	public int getTransmits() {
		return transmits;
	}
	public void setTransmits(int transmits) {
		this.transmits = transmits;
	}
	public int getCovers() {
		return covers;
	}
	public void setCovers(int covers) {
		this.covers = covers;
	}
	
	@Override
	public boolean equals(Object obj) {
		Transmitstatistics td =  (Transmitstatistics)obj;
		return analysisBlogId.equals(td.analysisBlogId) && levelID == td.levelID;
	}
	
	@Override
	public int hashCode() {
		String in = analysisBlogId + levelID + transmits + covers;
		return in.hashCode();
	}
}
