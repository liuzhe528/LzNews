package com.lzstudio.news.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzstudio.news.R;
import com.lzstudio.news.adapter.TabPageIndicatorAdapter;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;

public class MainFragment extends Fragment {
	private TabPageIndicatorAdapter adapter;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();;
	private int currentIndex = 0;
	private View view;
	private Fragment newFragment;
	private String[] TITLE = new String[] { "头条", "娱乐", "体育", "财经", "科技", "电影",
			"汽车", "时尚", "军事", "游戏", "情感", "精选", "电台", "NBA", "数码", "移动", "彩票",
			"教育", "论坛", "旅游", "手机", "博客", "社会", "家居", "暴雪", "亲子", "CBA" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.content_frame, container, false);
		initFragment();
		initPagerIndicator();
		adapter.notifyDataSetChanged();
		return view;
	}

	/**
	 * 初始化indicator
	 */
	private void initPagerIndicator() {
		adapter = new TabPageIndicatorAdapter(getChildFragmentManager(), TITLE,
				fragments);
		final ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		pager.setOffscreenPageLimit(1);
		pager.setAdapter(adapter);
		TabPageIndicator indicator = (TabPageIndicator) view
				.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				pager.setCurrentItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		indicator.setCurrentItem(currentIndex);
	}

	/**
	 * 初始化Fragment
	 */
	private void initFragment() {
		fragments.clear();
		int count = TITLE.length;
		for (int i = 0; i < count; i++) {
			String nameString = TITLE[i];
			fragments.add(initFragment(nameString));
		}
	}

	public Fragment initFragment(String channelName) {
		if (channelName.equals("头条")) {
			newFragment = new NewsFragment();
		} else if (channelName.equals("娱乐")) {
			newFragment = new YuLeFragment();
		} else if (channelName.equals("体育")) {
			newFragment = new TiYuFragment();
		} else if (channelName.equals("财经")) {
			newFragment = new CaiJingFragment();
		} else if (channelName.equals("科技")) {
			newFragment = new KeJiFragment();
		} else if (channelName.equals("电影")) {
			newFragment = new DianYingFragment();
		} else if (channelName.equals("汽车")) {
			newFragment = new QiCheFragment();
		} else if (channelName.equals("时尚")) {
			newFragment = new ShiShangFragment();
		} else if (channelName.equals("军事")) {
			newFragment = new JunShiFragment();
		} else if (channelName.equals("游戏")) {
			newFragment = new YouXiFragment();
		} else if (channelName.equals("情感")) {
			newFragment = new QingGanFragment();
		} else if (channelName.equals("精选")) {
			newFragment = new JingXuanFragment();
		} else if (channelName.equals("电台")) {
			newFragment = new DianTaiFragment();
		} else if (channelName.equals("NBA")) {
			newFragment = new NBAFragment();
		} else if (channelName.equals("数码")) {
			newFragment = new ShuMaFragment();
		} else if (channelName.equals("移动")) {
			newFragment = new YiDongFragment();
		} else if (channelName.equals("彩票")) {
			newFragment = new CaiPiaoFragment();
		} else if (channelName.equals("教育")) {
			newFragment = new JiaoYuFragment();
		} else if (channelName.equals("论坛")) {
			newFragment = new LunTanFragment();
		} else if (channelName.equals("旅游")) {
			newFragment = new LvYouFragment();
		} else if (channelName.equals("手机")) {
			newFragment = new ShouJiFragment();
		} else if (channelName.equals("博客")) {
			newFragment = new BoKeFragment();
		} else if (channelName.equals("社会")) {
			newFragment = new SheHuiFragment();
		} else if (channelName.equals("家居")) {
			newFragment = new JiaJuFragment();
		} else if (channelName.equals("暴雪")) {
			newFragment = new BaoXueYouXiFragment();
		} else if (channelName.equals("亲子")) {
			newFragment = new QinZiFragment();
		} else if (channelName.equals("CBA")) {
			newFragment = new CBAFragment();
		}
		return newFragment;
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen");
	}
}
