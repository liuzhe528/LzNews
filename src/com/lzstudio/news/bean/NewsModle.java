package com.lzstudio.news.bean;

import java.io.Serializable;

public class NewsModle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String docid;
	private String title;
	private String digest;
	private String imgsrc;
	private String source;
	private String ptime;
	private String votecount;
	private ImagesModle imagesModle;
	private String tag;
	private boolean hasRead;
	private String url_3w;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl_3w() {
		return url_3w;
	}

	public void setUrl_3w(String url_3w) {
		this.url_3w = url_3w;
	}

	public boolean isHasRead() {
		return hasRead;
	}

	public void setHasRead(boolean hasRead) {
		this.hasRead = hasRead;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

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

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getImgsrc() {
		return imgsrc;
	}

	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPtime() {
		return ptime;
	}

	public void setPtime(String ptime) {
		this.ptime = ptime;
	}

	public String getVotecount() {
		return votecount;
	}

	public void setVotecount(String votecount) {
		this.votecount = votecount;
	}

	public ImagesModle getImageModle() {
		return imagesModle;
	}

	public void setImagesModle(ImagesModle imagesModle) {
		this.imagesModle = imagesModle;
	}

}
