package com.lzstudio.news.activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnDismissListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.lzstudio.news.R;
import com.lzstudio.news.adapter.MenuAdapter;
import com.lzstudio.news.fragment.MainFragment;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.wechat.tools.br.AdSize;
import com.wechat.tools.br.AdView;
import com.wechat.tools.br.AdViewListener;
import com.wechat.tools.st.SpotManager;

public class MainActivity extends ActionBarActivity {
	// 声明相关变量
	private Toolbar toolbar;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView lvLeftMenu;
	private String[] lvs = { "新闻", "视频", "天气", "谁做家务", "更多" };
	private MenuAdapter menuAdapter = new MenuAdapter(this, lvs);
	private FragmentManager fm;
	private Fragment mainFragment;
	private boolean isPopupShow = false;
	private PopupMenu popup;
	private Context context;
	FeedbackAgent fb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		setOverflowButtonAlways();
		fm = getSupportFragmentManager();
		initDrawMenu();
		mainFragment = new MainFragment();
		fm.beginTransaction().replace(R.id.content_frame, mainFragment)
				.commit();
		setUpUmengFeedback();
		setSpotAd();
		showBanner();
	}

	/**
	 * 初始化slidemenu
	 */
	private void initDrawMenu() {
		findViews(); // 获取控件
		toolbar.setTitle("News");// 设置Toolbar标题
		toolbar.setTitleTextColor(Color.parseColor("#000000")); // 设置标题颜色
		setSupportActionBar(toolbar);
		toolbar.setOnMenuItemClickListener(onMenuItemClick);
		// 创建返回键，并实现打开关/闭监听
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
				R.string.open, R.string.close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
		};
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		lvLeftMenu.setDividerHeight(5);
		lvLeftMenu.setAdapter(menuAdapter);
		/**
		 * 设置侧滑菜单点击事件
		 */
		lvLeftMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					break;
				case 1:
					Intent videoIntent = new Intent(MainActivity.this,
							VideoActivity.class);
					startActivity(videoIntent);
					break;
				case 2:
					Intent weatherIntent = new Intent(MainActivity.this,
							WeatherActivity.class);
					startActivity(weatherIntent);
					break;
				case 3:
					Intent gameIntent = new Intent(MainActivity.this,
							GameActivity.class);
					startActivity(gameIntent);
					break;
				case 4:
					Intent moreIntent = new Intent(MainActivity.this,
							MoreActivity.class);
					startActivity(moreIntent);
					break;
				}
				mDrawerLayout.closeDrawers();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.maintest, menu);
		initPupopMenu(toolbar.getChildAt(2));
		return true;
	}

	/*
	 * 强制显示三点菜单
	 */
	private void setOverflowButtonAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKey = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKey.setAccessible(true);
			menuKey.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 修改显示的内容 不会重新加载 **/
	/*
	 * public void switchContent(Fragment to, Fragment from) { if (mContent !=
	 * to) { mContent = to; FragmentTransaction transaction =
	 * fm.beginTransaction(); if (!to.isAdded()) { // 先判断是否被add过
	 * transaction.hide(from).add(R.id.content_frame, to).commit(); //
	 * 隐藏当前的fragment，add下一个到Activity中 } else {
	 * transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个 } } }
	 */

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU && !isPopupShow) {
			popup.show();
			isPopupShow = true;
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	private void findViews() {
		toolbar = (Toolbar) findViewById(R.id.tl_custom);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
		lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
	}

	private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {
			switch (menuItem.getItemId()) {
			case R.id.action_more:
				popup.show();
				isPopupShow = true;
				break;
			}
			return true;
		}
	};

	private void initPupopMenu(View v) {
		popup = new PopupMenu(this, v);
		popup.getMenuInflater().inflate(R.menu.submenu, popup.getMenu());
		if (popup.getMenu().getClass().getSimpleName().equals("MenuBuilder")) {
			try {
				Method m = popup
						.getMenu()
						.getClass()
						.getDeclaredMethod("setOptionalIconsVisible",
								Boolean.TYPE);
				m.setAccessible(true);
				m.invoke(popup.getMenu(), true);
			} catch (Exception e) {
			}
		}
		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(PopupMenu arg0) {
				isPopupShow = false;
			}
		});
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.item_good:// 给个好评
					Uri uri = Uri.parse("market://details?id="
							+ getPackageName());
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					break;

				case R.id.item_feedback:// 意见反馈
					fb.startFeedbackActivity();
					break;
				case R.id.item_share:// 分享应用
					showShare();
					break;
				}
				return true;
			}
		});
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("发现一款实用的资讯软件");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("一款实用的资讯软件，快来下载，不要错过哦！");
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://lzstudio.sinaapp.com/lznews.apk");
		oks.setImageUrl("http://lzstudio.sinaapp.com/lznews.png");
		// oks.setImagePath(uri.toString());// 确保SDcard下面存在此张图片
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

	private void setUpUmengFeedback() {
		fb = new FeedbackAgent(this);
		// check if the app developer has replied to the feedback or not.
		fb.sync();
		fb.openAudioFeedback();
		fb.openFeedbackPush();

		// fb.setWelcomeInfo();
		// fb.setWelcomeInfo("Welcome to use umeng feedback app");
		// FeedbackPush.getInstance(this).init(true);
		// PushAgent.getInstance(this).setPushIntentServiceClass(MyPushIntentService.class);

		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = fb.updateUserInfo();
			}
		}).start();
	}

	/**
	 * 按两次退出
	 */
	long curr = 0;
	long last = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			curr = System.currentTimeMillis();
			if (curr - last < 1000) {
				finish();
			} else {
				Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
				last = curr;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setSpotAd() {
		// 插播接口调用

		// 加载插播资源
		SpotManager.getInstance(context).loadSpotAds();
		// 插屏出现动画效果，0:ANIM_NONE为无动画，1:ANIM_SIMPLE为简单动画效果，2:ANIM_ADVANCE为高级动画效果
		SpotManager.getInstance(context).setAnimationType(
				SpotManager.ANIM_ADVANCE);
		// 设置插屏动画的横竖屏展示方式，如果设置了横屏，则在有广告资源的情况下会是优先使用横屏图。
		SpotManager.getInstance(context).setSpotOrientation(
				SpotManager.ORIENTATION_PORTRAIT);
		SpotManager.getInstance(context).showSpotAds(context);
	}

	private void showBanner() {

		// 实例化LayoutParams(重要)
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告条的悬浮位置
		layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角
		// 实例化广告条
		AdView adView = new AdView(context, AdSize.FIT_SCREEN);
		// 调用Activity的addContentView函数

		// 监听广告条接口
		adView.setAdListener(new AdViewListener() {

			@Override
			public void onSwitchedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "广告条切换");
			}

			@Override
			public void onReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "请求广告成功");

			}

			@Override
			public void onFailedToReceivedAd(AdView arg0) {
				Log.i("YoumiAdDemo", "请求广告失败");
			}
		});
		((Activity) context).addContentView(adView, layoutParams);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
}