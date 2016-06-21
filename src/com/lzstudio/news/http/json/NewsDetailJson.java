package com.lzstudio.news.http.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lzstudio.news.bean.NewsDetailModle;

public class NewsDetailJson {
	private static NewsDetailJson instance = new NewsDetailJson();
	private NewsDetailModle newDetailModle;

	private NewsDetailJson() {
	};

	public static NewsDetailJson getInstance() {
		return instance;
	}

	/**
	 * 把Json数据转化成NewsDetailModle
	 * 
	 * @param jsonString
	 *            json数据
	 * @param newsId
	 *            新闻的docid
	 * @return
	 */
	public NewsDetailModle jsonToNewsDetail(String jsonString, String newsId) {
		try {
			if (jsonString == null || jsonString.equals("")) {
				return null;
			}
			JSONObject jsonObject = new JSONObject(jsonString)
					.getJSONObject(newsId);
			newDetailModle = readNewModle(jsonObject);
		} catch (Exception e) {

		} finally {
			System.gc();
		}
		return newDetailModle;
	}

	private NewsDetailModle readNewModle(JSONObject jsonObject)
			throws Exception {
		NewsDetailModle newDetailModle = null;

		String docid = "";
		String title = "";
		String source = "";
		String ptime = "";
		String body = "";
		String url_mp4 = "";
		String cover = "";

		docid = getString("docid", jsonObject);
		title = getString("title", jsonObject);
		source = getString("source", jsonObject);
		ptime = getString("ptime", jsonObject);
		body = getString("body", jsonObject);

		if (jsonObject.has("video")) {
			JSONObject jsonObje = jsonObject.getJSONArray("video")
					.getJSONObject(0);
			url_mp4 = getString("url_mp4", jsonObje);
			cover = getString("cover", jsonObje);
		}

		JSONArray jsonArray = jsonObject.getJSONArray("img");

		List<String> imgList = readImgList(jsonArray);

		newDetailModle = new NewsDetailModle();

		newDetailModle.setDocid(docid);
		newDetailModle.setImgList(imgList);
		newDetailModle.setPtime(ptime);
		newDetailModle.setSource(source);
		newDetailModle.setTitle(title);
		newDetailModle.setBody(body);
		newDetailModle.setUrl_mp4(url_mp4);
		newDetailModle.setCover(cover);

		return newDetailModle;
	}

	/**
	 * 解析图片集
	 * 
	 * @param jsonArray
	 * @return
	 * @throws Exception
	 */
	public List<String> readImgList(JSONArray jsonArray) throws Exception {
		List<String> imgList = new ArrayList<String>();

		for (int i = 0; i < jsonArray.length(); i++) {
			imgList.add(getString("src", jsonArray.getJSONObject(i)));
		}

		return imgList;
	}

	/**
	 * @param key
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	private String getString(String key, JSONObject jsonObject)
			throws Exception {
		String res = "";
		if (jsonObject.has(key)) {
			if (key == null) {
				return "";
			}
			res = jsonObject.getString(key);
		}
		return res;
	}
}
