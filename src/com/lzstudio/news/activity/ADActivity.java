package com.lzstudio.news.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.wechat.tools.video.VideoAdManager;
import com.wechat.tools.video.listener.VideoAdListener;
import com.wechat.tools.video.listener.VideoAdRequestListener;

public class ADActivity extends Activity {
	private Context context;
	private static final int ENTER = 1;
	private String playUrl;
	private String title;
	private Intent intent;
	VideoAdListener videoListener = new VideoAdListener() {

		// 视频播放失败
		@Override
		public void onVideoPlayFail() {
			Log.d("videoPlay", "failed");
			handler.sendEmptyMessage(ENTER);
		}

		// 视频播放完成
		@Override
		public void onVideoPlayComplete() {
			Log.d("videoPlay", "complete");
			handler.sendEmptyMessage(ENTER);
		}

		// 视频播放中途退出
		@Override
		public void onVideoPlayInterrupt() {
			Log.d("videoPlay", "interrupt");
			handler.sendEmptyMessage(ENTER);
		}

		@Override
		public void onDownloadComplete(String id) {
			Log.e("videoPlay", "download complete: " + id);
		}

		@Override
		public void onNewApkDownloadStart() {
			Log.e("videoPlay", "开始下载");
		}

		@Override
		public void onVideoLoadComplete() {
			Log.e("videoPlay", "视频加载完成");
		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ENTER:
				startActivity(intent);
				finish();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		playUrl = getIntent().getExtras().getString("mp4_url");
		title = getIntent().getExtras().getString("news_title");
		intent = new Intent(context, VideoDetailActivity.class);
		intent.putExtra("mp4_url", playUrl);
		intent.putExtra("news_title", title);
		setVideoAd();
	}

	private void setVideoAd() {

		// 视频设置，提供视频的各种设置,如设置退出提示语，更改退出按钮，加载中的logo。
		VideoAdManager.getInstance(context).getVideoAdSetting()
				.setInterruptsTips("是否要退出视频");

		VideoAdManager.getInstance(context).requestVideoAd(
				new VideoAdRequestListener() {

					@Override
					public void onRequestSucceed() {
						Log.d("videoPlay", "请求成功");
						VideoAdManager.getInstance(context).showVideo(context,
								videoListener);
					}

					@Override
					public void onRequestFail(int errorCode) {
						// 关于错误码的解读：-1为网络连接失败，请检查网络。-2007为无广告，-3312为该设备一天的播放次数已完,其他错误码一般为设备问题。
						Log.d("videoPlay", "请求失败，错误码为:" + errorCode);
						handler.sendEmptyMessage(ENTER);
					}

				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(context);
	}

	@Override
	protected void onDestroy() {
		VideoAdManager.getInstance(context).onUIDestroy();
		VideoAdManager.getInstance(context).onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(context);
	}
}
