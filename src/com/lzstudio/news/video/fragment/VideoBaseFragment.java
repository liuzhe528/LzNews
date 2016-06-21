package com.lzstudio.news.video.fragment;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.GridView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lzstudio.news.R;
import com.lzstudio.news.activity.ADActivity;
import com.lzstudio.news.bean.VideoModle;
import com.lzstudio.news.http.HttpClientUtils;
import com.lzstudio.news.http.Url;
import com.lzstudio.news.utils.ACache;
import com.lzstudio.news.utils.CommonUtil;
import com.lzstudio.news.utils.JsonParser;
import com.lzstudio.news.video.adapter.VideoAdapter;
import com.umeng.analytics.MobclickAgent;

public class VideoBaseFragment extends Fragment {
	private PullToRefreshGridView gridView;
	private View view;
	private FrameLayout fl;
	private String cacheName;
	private boolean isRefresh = false;
	private boolean isUpRefresh = false;
	private List<VideoModle> videos = new ArrayList<VideoModle>();
	private List<VideoModle> list;
	private VideoAdapter mAdapter;
	private int index = 0;
	private static final int IS_REFRESH = 1;
	private static final int IS_UP_REFRESH = 2;
	private static final int NO_NETWORK = 3;
	private static final int ERROR = 4;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case IS_REFRESH:
				isRefresh = false;
				gridView.onRefreshComplete();
				Toast.makeText(getActivity(), "貌似没有网络了哦！", Toast.LENGTH_SHORT)
						.show();
				break;
			case IS_UP_REFRESH:
				isUpRefresh = false;
				gridView.onRefreshComplete();
				Toast.makeText(getActivity(), "貌似没有网络了哦！请稍后再试...",
						Toast.LENGTH_SHORT).show();
				break;
			case NO_NETWORK:
				if (CommonUtil.isNetworkAvailable(getActivity()) != 0) {
					Toast.makeText(getActivity(), "请检查您的网络....",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case ERROR:
				Toast.makeText(getActivity(), "获取失败，请稍候再试", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		};
	};

	public VideoBaseFragment() {
	}

	public VideoBaseFragment(String id) {
		this.cacheName = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		videos.clear();
		view = inflater.inflate(R.layout.fragment_video_list, container, false);
		gridView = (PullToRefreshGridView) view
				.findViewById(R.id.pull_refresh_grid);
		fl = (FrameLayout) view.findViewById(R.id.video_loading_view);
		new MyVideoTask().execute();
		gridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				if (!isUpRefresh) {
					index = 0;
					setLastUpdateTime();
					isRefresh = true;
					new MyVideoTask().execute();
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				if (!isRefresh) {
					index += 10;
					isUpRefresh = true;
					new MyVideoTask().execute();
				}
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				VideoModle video = videos.get(position);
				if (CommonUtil.isNetworkAvailable(getActivity()) != 0) {
					Intent intent = new Intent(getActivity(), ADActivity.class);
					intent.putExtra("mp4_url", video.getMp4_url());
					intent.putExtra("news_title", video.getTitle());
					startActivity(intent);
				} else {
					handler.sendEmptyMessage(NO_NETWORK);
				}
			}
		});
		return view;
	}

	private class MyVideoTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			fl.setVisibility(View.INVISIBLE);
			if (list != null && list.size() > 0) {
				if (videos.size() == 0) {
					videos.addAll(list);
					mAdapter = new VideoAdapter(getActivity(), videos);
					gridView.getRefreshableView().setAdapter(mAdapter);
				} else {
					if (isRefresh) {
						isRefresh = false;
						videos.removeAll(videos);
						videos.addAll(list);
						gridView.onRefreshComplete();
					} else if (isUpRefresh) {
						isUpRefresh = false;
						videos.addAll(list);
						gridView.onRefreshComplete();
					}
				}
				mAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(getActivity(), "网络不佳,请稍候重试!", Toast.LENGTH_SHORT)
						.show();
			}
			this.cancel(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (CommonUtil.isNetworkAvailable(getActivity()) != 0) {
				String result = HttpClientUtils.getJson(getCommonUrl(
						index + "", cacheName));
				if (!TextUtils.isEmpty(result)) {
					list = JsonParser.getInstance().jsonToVideoBean(result,
							cacheName);
				} else {
					handler.sendEmptyMessage(ERROR);
				}
			} else {
				if (!isRefresh && !isUpRefresh) {
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
		String urlString = Url.Video + itemId + Url.VideoCenter + index
				+ Url.videoEndUrl;
		return urlString;
	}

	/**
	 * 设置缓存数据（key,value）
	 */
	public void setCacheStr(String key, String value) {
		if (!TextUtils.isEmpty(value)) {
			ACache.get(getActivity()).put(key, value);
		}
	}

	/**
	 * 获取缓存数据根据key
	 */
	public String getCacheStr(String key) {
		return ACache.get(getActivity()).getAsString(key);
	}

	private void setLastUpdateTime() {
		String text = CommonUtil.getStringDate();
		gridView.getLoadingLayoutProxy().setLastUpdatedLabel(text);
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
