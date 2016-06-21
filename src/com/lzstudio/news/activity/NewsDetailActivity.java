package com.lzstudio.news.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lzstudio.news.R;
import com.lzstudio.news.bean.NewsDetailModle;
import com.lzstudio.news.bean.NewsModle;
import com.lzstudio.news.http.HttpClientUtils;
import com.lzstudio.news.http.Url;
import com.lzstudio.news.http.json.NewsDetailJson;
import com.lzstudio.news.utils.Options;
import com.lzstudio.news.wedget.htmltextview.HtmlTextView;
import com.lzstudio.news.wedget.progresspieview.ProgressPieView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.umeng.analytics.MobclickAgent;

public class NewsDetailActivity extends BaseActivity implements
		ImageLoadingListener, ImageLoadingProgressListener {
	@ViewInject(R.id.news_detail_wv)
	private HtmlTextView mWebView;
	@ViewInject(R.id.root_view)
	private RelativeLayout root_view;
	@ViewInject(R.id.new_title)
	private TextView news_title;
	@ViewInject(R.id.new_time)
	private TextView news_time;
	@ViewInject(R.id.new_img)
	private ImageView news_img;
	@ViewInject(R.id.img_count)
	private TextView img_count;
	@ViewInject(R.id.progressPieView)
	private ProgressPieView progressPieView;
	@ViewInject(R.id.play)
	private ImageView play;
	protected DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private NewsDetailModle newsDetail;
	private String content;
	/**
	 * 新闻的来源和时间
	 */
	private String source_time;
	private static final int LOAD_COMPLITE = 10;
	private static final int LOAD_FAILED = 20;
	private static final int LOADING = 30;
	private static final int LOAD_STARTED = 40;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_COMPLITE:
				progressPieView.setVisibility(View.GONE);
				img_count.setText("共" + newsDetail.getImgList().size() + "张");
				img_count.setVisibility(View.VISIBLE);
				news_img.setImageBitmap((Bitmap) msg.obj);
				break;
			case LOAD_FAILED:
				progressPieView.setVisibility(View.GONE);
				break;
			case LOADING:
				int currentpro = (int) msg.obj;
				if (currentpro == 100) {
					progressPieView.setShowText(false);
					progressPieView.setVisibility(View.GONE);
				} else {
					progressPieView.setProgress(currentpro);
					progressPieView.setText(currentpro + "%");
					progressPieView.setVisibility(View.VISIBLE);
				}
				break;
			case LOAD_STARTED:
				progressPieView.setVisibility(View.VISIBLE);
				break;

			}
		};
	};

	@Override
	protected void initView() {
		setContentView(R.layout.activity_detail);
		initTitleBar();
		initLoadingView();
		ViewUtils.inject(this);
		rightBtn.setImageResource(R.drawable.icon_share);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setOnClickListener(this);
		news_img.setOnClickListener(this);
		progressPieView.setShowText(true);
	}

	private String docid;
	private String url;
	private NewsModle newsModle;

	@Override
	protected void initData() {
		newsModle = (NewsModle) getIntent().getSerializableExtra("newsModle");
		docid = newsModle.getDocid();
		url = Url.NewDetail + docid + Url.endDetailUrl;
		options = Options.getListOptions();
		imageLoader.init(Options.getConfig(ct));
		new MyLoadTask().execute();
	}

	private class MyLoadTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingView();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String result = HttpClientUtils.getJson(url);
			newsDetail = NewsDetailJson.getInstance().jsonToNewsDetail(result,
					docid);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (newsDetail == null) {
				return;
			}
			dealNewsDetail();
		}

	}

	private void dealNewsDetail() {
		content = newsDetail.getBody();
		content = content.replace("<!--VIDEO#1--></p><p>", "");
		content = content.replace("<!--VIDEO#2--></p><p>", "");
		content = content.replace("<!--VIDEO#3--></p><p>", "");
		content = content.replace("<!--VIDEO#4--></p><p>", "");
		content = content.replace("<!--REWARD#0--></p><p>", "");
		news_title.setText(newsDetail.getTitle());
		source_time = "来源：" + newsDetail.getSource() + " "
				+ newsDetail.getPtime();
		news_time.setText(source_time);
		int img_size = newsDetail.getImgList().size();
		if (!"".equals(newsDetail.getUrl_mp4())) {
			imageLoader.displayImage(newsDetail.getCover(), news_img, options,
					this, this);
			news_img.setVisibility(View.VISIBLE);
			mWebView.setHtmlFromString(content, false);
			dismissLoadingView();
		} else {
			if (img_size > 0) {
				news_img.setVisibility(View.VISIBLE);
				progressPieView.setVisibility(View.VISIBLE);
				mWebView.setHtmlFromString(content, false);
				dismissLoadingView();
				imageLoader.displayImage(newsDetail.getImgList().get(0),
						news_img, options, this, this);
			} else {
				mWebView.setHtmlFromString(content, false);
				dismissLoadingView();
			}
		}
	}

	public void imageMore() {
		if (!"".equals(newsDetail.getUrl_mp4())) {
			Intent intent = new Intent(ct, VideoDetailActivity.class);
			intent.putExtra("mp4_url", newsDetail.getUrl_mp4());
			intent.putExtra("news_title", newsDetail.getTitle());
			startActivity(intent);
		} else {
			Intent intent = new Intent(ct, ImageDetailActivity.class);
			intent.putExtra("newsDetailModle", newsDetail);
			startActivity(intent);
		}
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_right:
			// 分享
			showShare();
			break;

		case R.id.new_img:
			imageMore();
			break;
		}

	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(newsModle.getTitle());
		// text是分享文本，所有平台都需要这个字段
		oks.setText(newsModle.getDigest());
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(newsModle.getUrl());
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		oks.setImageUrl(newsModle.getImgsrc());
		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// oks.setTitleUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		// oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		// oks.setSiteUrl("http://sharesdk.cn");
		// 启动分享GUI
		oks.show(this);
	}

	@Override
	public void onProgressUpdate(String imageUri, View view, int current,
			int total) {
		int currentpro = current * 100 / total;
		Message msg = Message.obtain();
		msg.what = LOADING;
		msg.obj = currentpro;
		handler.sendMessage(msg);
	}

	@Override
	public void onLoadingStarted(String imageUri, View view) {
		Message msg = Message.obtain();
		msg.what = LOAD_STARTED;
		handler.sendMessage(msg);
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		Message msg = Message.obtain();
		msg.what = LOAD_FAILED;
		handler.sendMessage(msg);
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if (!"".equals(newsDetail.getUrl_mp4())) {
			play.setVisibility(View.VISIBLE);
			progressPieView.setVisibility(View.GONE);
		} else {
			Message msg = Message.obtain();
			msg.what = LOAD_COMPLITE;
			msg.obj = loadedImage;
			handler.sendMessage(msg);
		}
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		progressPieView.setVisibility(View.GONE);
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
