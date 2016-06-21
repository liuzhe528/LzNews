package com.lzstudio.news.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzstudio.news.R;
import com.lzstudio.news.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageAdapter extends BaseAdapter {
	private List<String> lists;
	private Context context;
	protected DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public ImageAdapter(Context context, List<String> imgList) {
		lists = imgList;
		this.context = context;
		options = Options.getListOptions();
		imageLoader.init(Options.getConfig(context));
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
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
			convertView = View.inflate(context, R.layout.item_image, null);
			holder.tv_process = (TextView) convertView
					.findViewById(R.id.current_page);
			holder.iv_img = (ImageView) convertView
					.findViewById(R.id.current_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String text = position + 1 + "/" + lists.size();
		holder.tv_process.setText(text);
		imageLoader.displayImage(lists.get(position), holder.iv_img, options);
		return convertView;
	}

	class ViewHolder {
		TextView tv_process;
		ImageView iv_img;
	}
}
