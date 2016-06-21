package com.lzstudio.news.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lzstudio.news.R;
import com.lzstudio.news.bean.NewsModle;

public class NewsAdapter extends BaseAdapter {
	private List<NewsModle> list;
	private Context context;
	private BitmapUtils bitmapUtil;

	public NewsAdapter(Context context, List<NewsModle> list) {
		this.list = list;
		this.context = context;
		bitmapUtil = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		NewsModle news = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_new, null);
			holder.articleLayout = (RelativeLayout) convertView
					.findViewById(R.id.article_top_layout);
			holder.leftImage = (ImageView) convertView
					.findViewById(R.id.left_image);
			holder.itemTitle = (TextView) convertView
					.findViewById(R.id.item_title);
			holder.itemContent = (TextView) convertView
					.findViewById(R.id.item_content);
			holder.imageLayout = (LinearLayout) convertView
					.findViewById(R.id.layout_image);
			holder.itemAbstract = (TextView) convertView
					.findViewById(R.id.item_abstract);
			holder.itemImage_0 = (ImageView) convertView
					.findViewById(R.id.item_image_0);
			holder.itemImage_1 = (ImageView) convertView
					.findViewById(R.id.item_image_1);
			holder.itemImage_2 = (ImageView) convertView
					.findViewById(R.id.item_image_2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (news.getImageModle() == null) {
			holder.articleLayout.setVisibility(View.VISIBLE);
			holder.imageLayout.setVisibility(View.GONE);
			holder.itemTitle.setText(news.getTitle());
			if (news.isHasRead()) {
				holder.itemTitle.setTextColor(context.getResources().getColor(
						R.color.news_item_has_read_textcolor));
			} else {
				holder.itemTitle.setTextColor(context.getResources().getColor(
						R.color.news_item_no_read_textcolor));
			}
			holder.itemContent.setText(news.getDigest());
			bitmapUtil.display(holder.leftImage, news.getImgsrc());
		} else {
			holder.imageLayout.setVisibility(View.VISIBLE);
			holder.articleLayout.setVisibility(View.GONE);
			holder.itemAbstract.setText(news.getTitle());
			if (news.isHasRead()) {
				holder.itemAbstract.setTextColor(context.getResources()
						.getColor(R.color.news_item_has_read_textcolor));
			} else {
				holder.itemAbstract.setTextColor(context.getResources()
						.getColor(R.color.news_item_no_read_textcolor));
			}
			bitmapUtil.display(holder.itemImage_0, news.getImageModle()
					.getImgList().get(0));
			bitmapUtil.display(holder.itemImage_1, news.getImageModle()
					.getImgList().get(1));
			bitmapUtil.display(holder.itemImage_2, news.getImageModle()
					.getImgList().get(2));
		}
		// if(news.isRead){
		// holder.title.setTextColor(context.getResources().getColor(R.color.news_item_has_read_textcolor));
		// }else{
		// holder.title.setTextColor(context.getResources().getColor(R.color.news_item_no_read_textcolor));
		// }
		return convertView;
	}

	class ViewHolder {
		RelativeLayout articleLayout;
		ImageView leftImage;
		TextView itemTitle;
		TextView itemContent;
		LinearLayout imageLayout;
		TextView itemAbstract;
		ImageView itemImage_0;
		ImageView itemImage_1;
		ImageView itemImage_2;
	}
}
