package com.lzstudio.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzstudio.news.R;

public class MenuAdapter extends BaseAdapter {
	private Context context;
	private String[] menu;
	private int[] items = { R.drawable.biz_navigation_tab_news,
			R.drawable.biz_navigation_tab_video,
			R.drawable.biz_navigation_tab_weather,
			R.drawable.biz_navigation_tab_game,
			R.drawable.biz_navigation_tab_more };

	public MenuAdapter(Context context, String[] menus) {
		this.context = context;
		this.menu = menus;
	}

	@Override
	public int getCount() {
		return menu.length;
	}

	@Override
	public Object getItem(int position) {
		return menu[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.menu_item, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_menu);
			holder.tv = (TextView) convertView.findViewById(R.id.tv_menu);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setBackgroundColor(Color.TRANSPARENT);
		holder.tv.setText(menu[position]);
		holder.iv.setImageResource(items[position]);
		return convertView;
	}

	class ViewHolder {
		ImageView iv;
		TextView tv;
	}
}
