package com.lzstudio.news.fragment;

import java.util.List;

import com.lzstudio.news.bean.NewsModle;
import com.lzstudio.news.http.HttpClientUtils;
import com.lzstudio.news.http.Url;
import com.lzstudio.news.utils.JsonParser;

public class LunTanFragment extends BaseFragment {
	public LunTanFragment(){
		super.setListener(new TaskListener() {
			@Override
			public List<NewsModle> doInBackground(int index) {
				List<NewsModle> newsList = null;
				String url = getCommonUrl(index + "", Url.LunTanId);
				String result = HttpClientUtils.getJson(url);
				if (index == 0) {
					setCacheStr(Url.LunTanId, result);
				}
				newsList = JsonParser.getInstance().jsonToBean(result,
						Url.LunTanId);
				return newsList;
			}
		}, Url.LunTanId);
	}
}