package com.lzstudio.news.activity;

import java.util.List;

import android.view.View;
import android.widget.TextView;

import com.lzstudio.news.R;
import com.lzstudio.news.adapter.ImageAdapter;
import com.lzstudio.news.bean.NewsDetailModle;
import com.lzstudio.news.bean.NewsModle;
import com.lzstudio.news.wedget.flipview.FlipView;
import com.lzstudio.news.wedget.flipview.OverFlipMode;
import com.umeng.analytics.MobclickAgent;

public class ImageDetailActivity extends BaseActivity {
	private TextView tv_title;
	private FlipView mFlipView;
	private NewsDetailModle newsDetailModle;
	private List<String> imgList;
	private String titleString;
	private ImageAdapter adapter;
	private NewsModle newsModle;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_image_detail);
		tv_title = (TextView) this.findViewById(R.id.news_title);
		mFlipView = (FlipView) this.findViewById(R.id.flip_view);
	}

	@Override
	protected void initData() {
		if (getIntent().getSerializableExtra("newsDetailModle") != null) {
			newsDetailModle = (NewsDetailModle) getIntent()
					.getSerializableExtra("newsDetailModle");
			imgList = newsDetailModle.getImgList();
			titleString = newsDetailModle.getTitle();
		} else {
			newsModle = (NewsModle) getIntent().getSerializableExtra(
					"newsModle");
			imgList = newsModle.getImageModle().getImgList();
			titleString = newsModle.getTitle();
		}
		tv_title.setText(titleString);
		adapter = new ImageAdapter(this, imgList);
		mFlipView.setAdapter(adapter);
		mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
		showBanner();
	}

	@Override
	protected void processClick(View v) {

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
}
