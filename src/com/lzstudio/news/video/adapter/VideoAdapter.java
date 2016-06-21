package com.lzstudio.news.video.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lzstudio.news.R;
import com.lzstudio.news.bean.VideoModle;

public class VideoAdapter extends BaseAdapter {
	private List<VideoModle> videos;
	private Context context;
	private BitmapUtils bitmapUtil;

	public VideoAdapter(Context context, List<VideoModle> videos) {
		this.videos = videos;
		this.context = context;
		bitmapUtil = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		return videos.size();
	}

	@Override
	public Object getItem(int position) {
		return videos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		VideoModle video = videos.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.video_gridview_item,
					null);
			holder.iv_video_cover = (ImageView) convertView
					.findViewById(R.id.iv_video_cover);
			holder.tv_video_title = (TextView) convertView
					.findViewById(R.id.tv_video_title);
			holder.tv_video_desc = (TextView) convertView
					.findViewById(R.id.tv_video_desc);
			holder.tv_video_playcount = (TextView) convertView
					.findViewById(R.id.tv_video_playcount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		bitmapUtil.display(holder.iv_video_cover, video.getCoverUrl());
		holder.tv_video_title.setText(video.getTitle());
		holder.tv_video_desc.setText(video.getDescription());
		holder.tv_video_playcount.setText("播放：" + video.getPlayCount() + " 次");
		return convertView;
	}

	class ViewHolder {
		ImageView iv_video_cover;
		TextView tv_video_title;
		TextView tv_video_desc;
		TextView tv_video_playcount;
	}
}
