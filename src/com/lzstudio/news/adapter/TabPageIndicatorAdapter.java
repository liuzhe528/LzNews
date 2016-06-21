package com.lzstudio.news.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

public class TabPageIndicatorAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();;
	private final FragmentManager fm;
	private String[] TITLE;

	public TabPageIndicatorAdapter(FragmentManager fm, String[] TITLE,
			ArrayList<Fragment> fragments) {
		super(fm);
		this.fm = fm;
		this.TITLE = TITLE;
		this.fragments = fragments;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLE[position];
	}

	public void appendList(ArrayList<Fragment> fragment) {
		fragments.clear();
		if (!fragments.containsAll(fragment) && fragment.size() > 0) {
			fragments.addAll(fragment);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void setFragments(ArrayList<Fragment> fragments) {
		if (this.fragments != null) {
			FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : this.fragments) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			fm.executePendingTransactions();
		}
		this.fragments = fragments;
		notifyDataSetChanged();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// 这里Destroy的是Fragment的视图层次，并不是Destroy Fragment对象
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (fragments.size() <= position) {
			position = position % fragments.size();
		}
		Object obj = super.instantiateItem(container, position);
		return obj;
	}
}
