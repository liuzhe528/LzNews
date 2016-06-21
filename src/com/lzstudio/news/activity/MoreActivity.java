package com.lzstudio.news.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzstudio.news.R;
import com.umeng.analytics.MobclickAgent;

public class MoreActivity extends Activity implements OnClickListener {
	private TextView tv_title, tv_back;
	private RelativeLayout rl_about_us, rl_introduce, rl_detail, rl_version;
	private TextView tv_about_title, tv_about_content;
	private Button bt_sure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		initView();
	}

	private void initView() {
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_back = (TextView) this.findViewById(R.id.tv_back);
		tv_about_title = (TextView) this.findViewById(R.id.tv_about_us);
		tv_about_content = (TextView) this
				.findViewById(R.id.tv_about_us_content);
		rl_about_us = (RelativeLayout) this.findViewById(R.id.rl_about_us);
		rl_introduce = (RelativeLayout) this.findViewById(R.id.rl_introduce);
		rl_detail = (RelativeLayout) this.findViewById(R.id.rl_detail);
		rl_version = (RelativeLayout) this.findViewById(R.id.rl_version);
		bt_sure = (Button) this.findViewById(R.id.bt_sure);
		bt_sure.setOnClickListener(this);
		tv_title.setText("更多");
		tv_back.setOnClickListener(this);
		rl_about_us.setOnClickListener(this);
		rl_introduce.setOnClickListener(this);
		rl_version.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.bt_sure:
			rl_detail.setVisibility(View.GONE);
			break;
		case R.id.rl_about_us:
			tv_about_title.setText("关于我们");
			tv_about_content.setText(R.string.about_us);
			rl_detail.setVisibility(View.VISIBLE);
			break;
		case R.id.rl_introduce:
			tv_about_title.setText("功能介绍");
			tv_about_content.setText(R.string.introduce);
			rl_detail.setVisibility(View.VISIBLE);
			break;
		case R.id.rl_version:
			tv_about_title.setText("当前版本");
			tv_about_content.setText(R.string.version);
			rl_detail.setVisibility(View.VISIBLE);
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
