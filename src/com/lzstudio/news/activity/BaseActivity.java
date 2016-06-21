package com.lzstudio.news.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lzstudio.news.R;
import com.lzstudio.news.http.Url;
import com.umeng.analytics.MobclickAgent;
import com.wechat.tools.br.AdSize;
import com.wechat.tools.br.AdView;
import com.wechat.tools.br.AdViewListener;

public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener {

	protected Context ct;
	@ViewInject(R.id.loading_view)
	protected View loadingView;
	@ViewInject(R.id.ll_load_fail)
	protected LinearLayout loadfailView;
	@ViewInject(R.id.btn_left)
	protected Button leftBtn;
	protected ImageButton rightBtn;
	protected ImageButton leftImgBtn;
	protected ImageButton rightImgBtn;
	protected TextView titleTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ct = this;
		initView();
		initData();
	}

	protected void showBanner() {

		// 实例化LayoutParams(重要)
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
		// 实例化广告条
		AdView adView = new AdView(ct, AdSize.FIT_SCREEN);
		// 调用Activity的addContentView函数

		// 监听广告条接口
		adView.setAdListener(new AdViewListener() {

			@Override
			public void onSwitchedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "广告条切换");
			}

			@Override
			public void onReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "请求广告成功");

			}

			@Override
			public void onFailedToReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "请求广告失败");
			}
		});
		((Activity) ct).addContentView(adView, layoutParams);
	}

	public String getUrl(String newId) {
		return Url.NewDetail + newId + Url.endDetailUrl;
	}

	protected void initLoadingView() {
		loadingView = findViewById(R.id.loading_view);
		loadfailView = (LinearLayout) findViewById(R.id.ll_load_fail);
	}

	protected void initTitleBar() {
		leftBtn = (Button) findViewById(R.id.btn_left);
		rightBtn = (ImageButton) findViewById(R.id.btn_right);
		if (leftBtn != null) {
			leftBtn.setVisibility(View.GONE);
		}
		if (rightBtn != null) {
			rightBtn.setVisibility(View.GONE);
		}
		leftImgBtn = (ImageButton) findViewById(R.id.imgbtn_left);
		rightImgBtn = (ImageButton) findViewById(R.id.imgbtn_right);
		if (rightImgBtn != null) {
			rightImgBtn.setVisibility(View.INVISIBLE);
		}
		if (leftImgBtn != null) {
			leftImgBtn.setImageResource(R.drawable.back);
		}
		titleTv = (TextView) findViewById(R.id.txt_title);
		titleTv.setText("新闻详情");
		if (leftImgBtn != null) {
			leftImgBtn.setOnClickListener(this);
		}
		if (rightBtn != null) {
			rightBtn.setOnClickListener(this);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_left:
			this.finish();
			break;

		default:
			break;
		}
		processClick(v);

	}

	public void showLoadingView() {
		if (loadingView != null)
			loadingView.setVisibility(View.VISIBLE);
	}

	public void dismissLoadingView() {
		if (loadingView != null)
			loadingView.setVisibility(View.INVISIBLE);
		showBanner();
	}

	public void showLoadFailView() {
		if (loadingView != null) {
			loadingView.setVisibility(View.VISIBLE);
			loadfailView.setVisibility(View.VISIBLE);
		}

	}

	public void dismissLoadFailView() {
		if (loadingView != null)
			loadfailView.setVisibility(View.INVISIBLE);
	}

	protected abstract void initView();

	protected abstract void initData();

	protected abstract void processClick(View v);

	/**
	 * 返回
	 */
	public void doBack(View view) {
		onBackPressed();
	}
}
