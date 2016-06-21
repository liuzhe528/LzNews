package com.lzstudio.news.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lzstudio.news.R;
import com.lzstudio.news.http.Url;
import com.lzstudio.news.video.fragment.VideoBaseFragment;
import com.lzstudio.news.view.indicator.ViewPagerIndicator;

public class VideoActivity extends FragmentActivity {
	private TextView tv_title, tv_back;
	private ViewPagerIndicator indicator;
	private ViewPager viewpager;
	private List<String> titles = Arrays.asList("热点", "搞笑", "娱乐");
	private String[] ids = { Url.VideoReDianId, Url.VideoGaoXiaoId,
			Url.VideoYuLeId };
	private List<Fragment> contents = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private Fragment newFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		initView();
		initDatas();
		// 设置Tab上的标题
		indicator.setTabItemTitles(titles);
		viewpager.setAdapter(mAdapter);
		// 设置关联的ViewPager
		indicator.setViewPager(viewpager, 0);
	}

	private void initDatas() {
		for (int i = 0; i < ids.length; i++) {
			newFragment = new VideoBaseFragment(ids[i]);
			contents.add(newFragment);
		}
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return contents.size();
			}

			@Override
			public Fragment getItem(int position) {
				return contents.get(position);
			}
		};
	}

	private void initView() {
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_title.setText("视频");
		tv_back = (TextView) this.findViewById(R.id.tv_back);
		tv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		indicator = (ViewPagerIndicator) this.findViewById(R.id.id_indicator);
		viewpager = (ViewPager) this.findViewById(R.id.id_vp);
	}

}
