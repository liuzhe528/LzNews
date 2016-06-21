package com.lzstudio.news.fragment;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestFragment extends Fragment {
	private String mTitle = "Defualt";
	public static String TITLE = "title";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (getArguments() != null) {
			mTitle = getArguments().getString(TITLE);
		}
		TextView tv = new TextView(getActivity());
		tv.setTextSize(20);
		tv.setBackgroundColor(Color.parseColor("#ffffffff"));
		tv.setGravity(Gravity.CENTER);
		tv.setText(mTitle);
		return tv;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
