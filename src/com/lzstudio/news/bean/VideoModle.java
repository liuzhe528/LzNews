package com.lzstudio.news.bean;

public class VideoModle extends BaseModle {

	private static final long serialVersionUID = 1L;
	/**
	 * 视频长度
	 */
	private int length;

	/**
	 * 封面的图片
	 */
	private String coverUrl;
	/**
	 * 视频描述
	 */
	private String description;
	/**
	 * 视频地址
	 */
	private String mp4_url;
	/**
	 * 播放次数
	 */
	private int playCount;
	/**
	 * 视频发布的时间
	 */
	private String ptime;
	/**
	 * 视频标题
	 */
	private String title;
	/**
	 * 视频来源
	 */
	private String videosource;
	/**
	 * 视频id
	 */
	private String vid;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMp4_url() {
		return mp4_url;
	}

	public void setMp4_url(String mp4_url) {
		this.mp4_url = mp4_url;
	}

	public int getPlayCount() {
		return playCount;
	}

	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}

	public String getPtime() {
		return ptime;
	}

	public void setPtime(String ptime) {
		this.ptime = ptime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideosource() {
		return videosource;
	}

	public void setVideosource(String videosource) {
		this.videosource = videosource;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

}
