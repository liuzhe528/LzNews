package com.lzstudio.news.fragment;

import java.util.List;

import com.lzstudio.news.bean.NewsModle;
import com.lzstudio.news.http.HttpClientUtils;
import com.lzstudio.news.http.Url;
import com.lzstudio.news.utils.JsonParser;

public class KeJiFragment extends BaseFragment {
	public KeJiFragment() {
		super.setListener(new TaskListener() {
			@Override
			public List<NewsModle> doInBackground(int index) {
				List<NewsModle> newsList = null;
				String url = getCommonUrl(index + "", Url.KeJiId);
				String result = HttpClientUtils.getJson(url);
				if (index == 0) {
					setCacheStr(Url.KeJiId, result);
				}
				newsList = JsonParser.getInstance().jsonToBean(result,
						Url.KeJiId);
				return newsList;
			}
		}, Url.KeJiId);
	}
}