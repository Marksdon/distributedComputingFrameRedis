package com.xzc.concurr.pojo;

import java.io.Serializable;

public class Blogger implements Serializable {

	private static final long serialVersionUID = 1L;
	private String blogId;
	private String uid;
	private int transmits;
	private String url;
	
	public String getBlogId() {
		return blogId;
	}
	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public int getTransmits() {
		return transmits;
	}
	public void setTransmits(int transmits) {
		this.transmits = transmits;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
