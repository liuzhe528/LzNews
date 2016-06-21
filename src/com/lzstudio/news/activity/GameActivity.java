package com.lzstudio.news.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzstudio.news.R;
import com.lzstudio.news.view.game.LuckyPanView;
import com.umeng.analytics.MobclickAgent;

public class GameActivity extends Activity {
	private LuckyPanView mLuckyPanView;
	private ImageView button;
	private TextView tv_title;
	private TextView tv_back;
	private TextView location;
	// 是否作弊
	private boolean isCheating = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		tv_title = (TextView) this.findViewById(R.id.common_title);
		tv_title.setText(R.string.title_game);
		tv_title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isCheating = true;
			}
		});
		tv_back = (TextView) this.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		location = (TextView) this.findViewById(R.id.location);
		location.setVisibility(View.GONE);
		mLuckyPanView = (LuckyPanView) this.findViewById(R.id.luckyPan);
		button = (ImageView) this.findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mLuckyPanView.isStart()) {
					button.setImageResource(R.drawable.stop);
					// 设置指定奖项
					int number = genarateLuckyNumber();
					mLuckyPanView.startLucky(number);
				} else {
					if (!mLuckyPanView.isShouldEnd()) {
						button.setImageResource(R.drawable.start);
						mLuckyPanView.luckyEnd();
					}
				}
			}
		});
	}

	private int genarateLuckyNumber() {
		int num = (int) (Math.random() * 1000);
		int lucky = 0;
		if (num >= 0 && num < 50) {
			lucky = 0;// 休息
		} else if (num >= 50 && num < 275) {
			lucky = 1;// 扫地
		} else if (num >= 275 && num < 500) {
			lucky = 2;// 洗碗
		} else if (num >= 500 && num < 725) {
			lucky = 4;// 做饭
		} else if (num >= 725 && num < 950) {
			lucky = 5;// 洗衣服
		} else {
			lucky = 3;// 家务全包
		}
		if (isCheating) {
			lucky = 0;
			isCheating = false;
		}
		return lucky;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
