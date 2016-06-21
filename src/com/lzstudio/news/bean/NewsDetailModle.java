package com.lzstudio.news.bean;

import java.util.List;

public class NewsDetailModle extends BaseModle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String docid;
	private String title;
	private String source;
	private String body;
	private String ptime;
	private String cover;
	private String url_mp4;
	private List<String> imgList;

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPtime() {
		return ptime;
	}

	public void setPtime(String ptime) {
		this.ptime = ptime;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getUrl_mp4() {
		return url_mp4;
	}

	public void setUrl_mp4(String url_mp4) {
		this.url_mp4 = url_mp4;
	}

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}
}
