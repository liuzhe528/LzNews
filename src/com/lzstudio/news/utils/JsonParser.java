package com.lzstudio.news.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.lzstudio.news.bean.ImagesModle;
import com.lzstudio.news.bean.NewsModle;
import com.lzstudio.news.bean.VideoModle;

public class JsonParser {
	// -----------单例模式------------
	private static JsonParser jsonParser = new JsonParser();

	private JsonParser() {
	};

	public static JsonParser getInstance() {
		return jsonParser;
	}

	public List<NewsModle> newModles;

	/**
	 * 把json转化成NewsModle;
	 * 
	 * @param jsonString
	 *            json数据
	 * @param id
	 *            分类的Id
	 * @return
	 */
	public List<NewsModle> jsonToBean(String jsonString, String id) {
		newModles = new ArrayList<NewsModle>();
		try {
			if (jsonString == null || jsonString.equals("")) {
				return null;
			}
			NewsModle newsModle = null;
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObject.getJSONArray(id);
			for (int i = 0; i < jsonArray.length(); i++) {
				newsModle = new NewsModle();
				JSONObject js = jsonArray.getJSONObject(i);
				if (js.has("skipType")
						&& js.getString("skipType").equals("special")) {
					continue;
				}
				if (js.has("TAGS") && !js.has("TAG")) {
					continue;
				}
				if (js.has("imgextra")) {
					newsModle.setTitle(getString("title", js));
					newsModle.setDocid(getString("docid", js));
					ImagesModle imagesModle = new ImagesModle();
					List<String> list;
					list = readImgList(js.getJSONArray("imgextra"));
					list.add(getString("imgsrc", js));
					imagesModle.setImgList(list);
					newsModle.setImagesModle(imagesModle);
				} else {
					newsModle = readNewModle(js);
				}
				newModles.add(newsModle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
		return newModles;
	}

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
			imgList.add(getString("imgsrc", jsonArray.getJSONObject(i)));
		}

		return imgList;
	}

	/**
	 * 获取图文列表
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public NewsModle readNewModle(JSONObject jsonObject) throws Exception {
		NewsModle newsModle = null;

		String docid = "";
		String title = "";
		String digest = "";
		String imgsrc = "";
		String source = "";
		String ptime = "";
		String tag = "";
		String votecount = "";
		String url_3w = "";
		String url = "";
		docid = getString("docid", jsonObject);
		title = getString("title", jsonObject);
		digest = getString("digest", jsonObject);
		imgsrc = getString("imgsrc", jsonObject);
		source = getString("source", jsonObject);
		ptime = getString("ptime", jsonObject);
		votecount = getString("votecount", jsonObject);
		tag = getString("TAG", jsonObject);
		url_3w = getString("url_3w", jsonObject);
		url = getString("url", jsonObject);
		newsModle = new NewsModle();

		newsModle.setDigest(digest);
		newsModle.setDocid(docid);
		newsModle.setImgsrc(imgsrc);
		newsModle.setTitle(title);
		newsModle.setPtime(ptime);
		newsModle.setSource(source);
		newsModle.setTag(tag);
		newsModle.setVotecount(votecount);
		newsModle.setUrl_3w(url_3w);
		newsModle.setUrl(url);
		return newsModle;
	}

	/**
	 * json转换成video 对象
	 * 
	 * @param result
	 * @param cacheName
	 * @return
	 */
	public List<VideoModle> jsonToVideoBean(String result, String cacheName) {
		List<VideoModle> videoList = new ArrayList<VideoModle>();
		try {
			if (result == null || result.equals("")) {
				return null;
			}
			VideoModle video = null;
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray(cacheName);
			for (int i = 0; i < jsonArray.length(); i++) {
				video = new VideoModle();
				JSONObject js = jsonArray.getJSONObject(i);
				video.setCoverUrl(getString("cover", js));
				video.setDescription(getString("description", js));
				video.setLength(js.getInt("length"));
				video.setMp4_url(getString("mp4_url", js));
				video.setPlayCount(js.getInt("playCount"));
				video.setPtime(getString("ptime", js));
				video.setTitle(getString("title", js));
				video.setVid(getString("vid", js));
				video.setVideosource(getString("videosource", js));
				videoList.add(video);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
		return videoList;
	}
}
