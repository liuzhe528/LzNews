package com.lzstudio.news.fragment;

import java.util.List;

import com.lzstudio.news.bean.NewsModle;
import com.lzstudio.news.http.HttpClientUtils;
import com.lzstudio.news.http.Url;
import com.lzstudio.news.utils.JsonParser;

public class NewsFragment extends BaseFragment {
	public NewsFragment() {
		super.setListener(new TaskListener() {
			@Override
			public List<NewsModle> doInBackground(int index) {
				List<NewsModle> newsList = null;
				String url = getCommonUrl(index + "", Url.TopId);
				String result = HttpClientUtils.getJson(url);
				if (index == 0) {
					setCacheStr(Url.TopId, result);
				}
				newsList = JsonParser.getInstance().jsonToBean(result,
						Url.TopId);
				return newsList;
			}
		}, Url.TopId);
	}
}