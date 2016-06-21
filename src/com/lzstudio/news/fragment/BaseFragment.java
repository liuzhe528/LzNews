package com.lzstudio.news.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.lzstudio.news.R;
import com.lzstudio.news.activity.ImageDetailActivity;
import com.lzstudio.news.activity.NewsDetailActivity;
import com.lzstudio.news.adapter.NewsAdapter;
import com.lzstudio.news.bean.NewsModle;
import com.lzstudio.news.http.Url;
import com.lzstudio.news.utils.ACache;
import com.lzstudio.news.utils.CommonUtil;
import com.lzstudio.news.utils.JsonParser;
import com.lzstudio.news.view.pulltorefresh.PullToRefreshBase;
import com.lzstudio.news.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.lzstudio.news.view.pulltorefresh.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends Fragment {
	private List<NewsModle> newsList;
	private List<NewsModle> list = new ArrayList<NewsModle>();
	private PullToRefreshListView lv;
	private FrameLayout fl;
	private int index = 0;
	private NewsAdapter adapter;
	private boolean isRefresh = false;
	private boolean isUpRefresh = false;
	private TaskListener listener;
	private String cacheName;
	private static final int IS_REFRESH = 1;
	private static final int IS_UP_REFRESH = 2;
	private static final int NO_NETWORK = 3;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case IS_REFRESH:
				isRefresh = false;
				lv.onPullDownRefreshComplete();
				lv.setPullRefreshEnabled(true);
				Toast.makeText(getMyActivity(), "貌似没有网络了哦！", Toast.LENGTH_SHORT)
						.show();
				break;
			case IS_UP_REFRESH:
				isUpRefresh = false;
				lv.onPullUpRefreshComplete();
				lv.setPullLoadEnabled(true);
				Toast.makeText(getMyActivity(), "貌似没有网络了哦！请稍后再试...",
						Toast.LENGTH_SHORT).show();
				break;
			case NO_NETWORK:
				if (CommonUtil.isNetworkAvailable(getMyActivity()) != 0) {
					Toast.makeText(getMyActivity(), "请检查您的网络....",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};

	public void setListener(TaskListener listener, String itemId) {
		this.listener = listener;
		cacheName = itemId;
	}

	protected interface TaskListener {
		public List<NewsModle> doInBackground(int index);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		list.clear();
		View contextView = inflater.inflate(R.layout.pulltorefresh_fragment,
				container, false);
		lv = (PullToRefreshListView) contextView
				.findViewById(R.id.lv_item_news);
		fl = (FrameLayout) contextView.findViewById(R.id.loading_view);
		lv.setScrollLoadEnabled(true);
		lv.setPullLoadEnabled(true);
		lv.setPullRefreshEnabled(true);
		if (listener != null) {
			new MyLoadTask().execute();
		}
		lv.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						list.get(position).setHasRead(true);
						View cell = lv.getRefreshableView().getChildAt(
								position
										- lv.getRefreshableView()
												.getFirstVisiblePosition());
						if (cell != null) {
							adapter.getView(position, cell,
									lv.getRefreshableView());
						}
						if (CommonUtil.isNetworkAvailable(getMyActivity()) == 0) {
							handler.sendEmptyMessage(NO_NETWORK);
						} else {
							// 判断进入普通新闻详情页还是图片新闻详情页
							if (list.get(position).getImageModle() == null) {
								Intent intent = new Intent(getActivity(),
										NewsDetailActivity.class);
								intent.putExtra("newsModle", list.get(position));
								startActivity(intent);
							} else {
								Intent intent = new Intent(getActivity(),
										ImageDetailActivity.class);
								intent.putExtra("newsModle", list.get(position));
								startActivity(intent);
							}
						}
					}
				});
		lv.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				setLastUpdateTime();
				index = 0;
				isRefresh = true;
				if (listener != null) {
					lv.setPullRefreshEnabled(false);
					new MyLoadTask().execute();
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				index += 20;
				isUpRefresh = true;
				if (listener != null) {
					lv.setPullLoadEnabled(false);
					new MyLoadTask().execute();
				}
			}
		});
		return contextView;
	}

	private List<NewsModle> getCacheDate() {
		List<NewsModle> cacheList = null;
		String result = getCacheStr(cacheName);
		if (!TextUtils.isEmpty(result)) {
			cacheList = JsonParser.getInstance().jsonToBean(result, cacheName);
		}
		return cacheList;
	}

	private class MyLoadTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			fl.setVisibility(View.INVISIBLE);
			if (newsList != null) {
				if (list.size() == 0) {
					list.addAll(newsList);
					adapter = new NewsAdapter(getActivity(), list);
					lv.getRefreshableView().setAdapter(adapter);
				} else {
					if (isRefresh) {
						isRefresh = false;
						list.removeAll(list);
						list.addAll(newsList);
						lv.onPullDownRefreshComplete();
						lv.setPullRefreshEnabled(true);
					} else if (isUpRefresh) {
						isUpRefresh = false;
						list.addAll(newsList);
						lv.onPullUpRefreshComplete();
						lv.setPullLoadEnabled(true);
					}
				}
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(getActivity(), "网络不佳,请稍候重试!", Toast.LENGTH_SHORT)
						.show();
			}
			this.cancel(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (CommonUtil.isNetworkAvailable(getMyActivity()) != 0) {
				newsList = listener.doInBackground(index);
			} else {
				if (!isRefresh && !isUpRefresh) {
					newsList = getCacheDate();
					handler.sendEmptyMessage(NO_NETWORK);
				} else {
					if (isRefresh) {
						handler.sendEmptyMessage(IS_REFRESH);
					} else if (isUpRefresh) {
						handler.sendEmptyMessage(IS_UP_REFRESH);
					}
				}
			}
			return null;
		}
	}

	public String getCommonUrl(String index, String itemId) {
		cacheName = itemId;
		String urlString = Url.CommonUrl + itemId + "/" + index + Url.endUrl;
		return urlString;
	}

	/**
	 * 设置缓存数据（key,value）
	 */
	public void setCacheStr(String key, String value) {
		if (!TextUtils.isEmpty(value)) {
			ACache.get(getMyActivity()).put(key, value);
		}
	}

	/**
	 * 获取缓存数据根据key
	 */
	public String getCacheStr(String key) {
		return ACache.get(getMyActivity()).getAsString(key);
	}

	public Context getMyActivity() {
		return getActivity();
	}

	private void setLastUpdateTime() {
		String text = CommonUtil.getStringDate();
		lv.setLastUpdatedLabel(text);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen");
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}
}
