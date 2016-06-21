package com.lzstudio.news.activity;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzstudio.news.R;
import com.umeng.analytics.MobclickAgent;
import com.wechat.tools.st.SpotManager;

public class VideoDetailActivity extends Activity implements OnInfoListener,
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
		OnClickListener {
	private VideoView mVideoView;
	private ProgressBar mProgressBar;
	private RelativeLayout video_end;
	private TextView mLoadRate, tv_replay, tv_endBack;
	private TextView tv_title;
	private String playUrl;
	private String title;
	private int mPositionWhenPaused = -1;
	private ImageView iv_back;
	private View mVolumeBrightnessLayout;
	private ImageView mOperationBg;
	private ImageView mOperationPercent;
	private GestureDetector mGestureDetector;
	private AudioManager mAudioManager;
	/** 最大声音 */
	private int mMaxVolume;
	/** 当前声音 */
	private int mVolume = -1;
	/** 当前亮度 */
	private float mBrightness = -1f;
	/** 当前缩放模式 */
	private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
	/**
	 * 上下滑动
	 */
	private boolean isUp_downScroll;
	/**
	 * 左右滑动
	 */
	private boolean isFast_backScroll;
	/**
	 * 左右滑动的进度
	 */
	private float mFast_forward;
	private View mFl_Progress;
	private TextView mTv_progress;
	private ImageView mIv_Progress_bg;
	private MediaController mMediaController;
	private Context context;
	private static final int DISMISS = 1;
	/** 定时隐藏 */
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DISMISS:
				isFast_backScroll = false;
				isUp_downScroll = false;
				mVolumeBrightnessLayout.setVisibility(View.GONE);
				mFl_Progress.setVisibility(View.GONE);
				break;
			}
		}
	};

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_play_videobuffer);
		context = this;
		mVideoView = (VideoView) this.findViewById(R.id.buffer);
		mProgressBar = (ProgressBar) this.findViewById(R.id.probar);
		mLoadRate = (TextView) this.findViewById(R.id.load_rate);
		iv_back = (ImageView) this.findViewById(R.id.video_back);
		tv_title = (TextView) this.findViewById(R.id.video_title);
		mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
		mOperationBg = (ImageView) findViewById(R.id.operation_bg);
		mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
		mTv_progress = (TextView) findViewById(R.id.tv_progress);
		video_end = (RelativeLayout) findViewById(R.id.video_end);
		tv_replay = (TextView) findViewById(R.id.video_replay);
		tv_endBack = (TextView) findViewById(R.id.video_end_back);
		mFl_Progress = findViewById(R.id.fl_set_progress);
		mIv_Progress_bg = (ImageView) findViewById(R.id.iv_progress_bg);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				VideoDetailActivity.this.finish();
			}
		});
		tv_replay.setOnClickListener(this);
		tv_endBack.setOnClickListener(this);
		initData();
	};

	private void initData() {
		try {
			if (!LibsChecker.checkVitamioLibs(this))
				return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		playUrl = getIntent().getExtras().getString("mp4_url");
		title = getIntent().getExtras().getString("news_title");
		if ("".equals(playUrl) || playUrl == null) {
			Toast.makeText(this, "请求地址错误", Toast.LENGTH_SHORT).show();
			finish();
		}
		tv_title.setText(title);
		mVideoView.setVideoPath(playUrl);
		mMediaController = new MediaController(this);
		mVideoView.setMediaController(mMediaController);
		mMediaController.setFileName(title);
		mVideoView.requestFocus();
		mGestureDetector = new GestureDetector(this, new MyGestureListener());
		mVideoView.setOnInfoListener(this);
		mVideoView.setOnBufferingUpdateListener(this);
		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnPreparedListener(this);
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.setPlaybackSpeed(1.0f);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		mLoadRate.setText(percent + "%");
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				mProgressBar.setVisibility(View.VISIBLE);
				mLoadRate.setText("");
				mLoadRate.setVisibility(View.VISIBLE);
			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			mVideoView.start();
			mProgressBar.setVisibility(View.GONE);
			mLoadRate.setVisibility(View.GONE);
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			break;
		}
		return true;
	}

	public void onPause() {
		// 在活动时是停止视频的停顿.
		mPositionWhenPaused = (int) mVideoView.getCurrentPosition();
		mVideoView.stopPlayback();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void onResume() {
		// 恢复视频播放器
		if (mPositionWhenPaused >= 0) {
			mVideoView.seekTo(mPositionWhenPaused);
			mPositionWhenPaused = -1;
		}
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event))
			return true;

		// 处理手势结束
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			endGesture();
			break;
		}

		return super.onTouchEvent(event);
	}

	/** 手势结束 */
	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;
		if (isFast_backScroll) {
			onSeekProgress(mFast_forward);
		}
		// 隐藏
		mDismissHandler.removeMessages(DISMISS);
		mDismissHandler.sendEmptyMessageDelayed(DISMISS, 300);
	}

	private void onSeekProgress(float dis) {
		mVideoView.seekTo(mVideoView.getCurrentPosition() + 50 * (long) dis);
	}

	private class MyGestureListener extends SimpleOnGestureListener {

		/** 双击 */
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
				mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
			else
				mLayout++;
			if (mVideoView != null)
				mVideoView.setVideoLayout(mLayout, 0);
			return true;
		}

		/** 滑动 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			float mOldX = e1.getX(), mOldY = e1.getY();
			int x = (int) e2.getRawX();
			int y = (int) e2.getRawY();
			Display disp = getWindowManager().getDefaultDisplay();
			DisplayMetrics outMetrics = new DisplayMetrics();
			disp.getMetrics(outMetrics);
			int windowWidth = outMetrics.widthPixels;
			int windowHeight = outMetrics.heightPixels;
			if (Math.abs(x - mOldX) > Math.abs(y - mOldY) && !isUp_downScroll) {// 快进、快退
				isFast_backScroll = true;
				mFast_forward = x - mOldX;
				fast_forward(mFast_forward);
			} else {
				if (mOldX > windowWidth * 4.0 / 5 && !isFast_backScroll) {// 右边滑动
					onVolumeSlide((mOldY - y) / windowHeight);
				} else if (mOldX < windowWidth / 5.0 && !isFast_backScroll) {// 左边滑动
					onBrightnessSlide((mOldY - y) / windowHeight);
				}
			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	/**
	 * 滑动改变声音大小
	 * 
	 * @param percent
	 */
	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;
			// 显示
			mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		// 变更进度条
		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = findViewById(R.id.operation_full).getLayoutParams().width
				* index / mMaxVolume;
		mOperationPercent.setLayoutParams(lp);
	}

	/*
	 * 快进快退
	 */
	private void fast_forward(float dis) {
		long currentProgress;
		long duration = mVideoView.getDuration();
		if (mVideoView.getCurrentPosition() + 50 * (long) dis < 0)
			currentProgress = 0;
		else
			currentProgress = mVideoView.getCurrentPosition() + 50 * (long) dis;
		mTv_progress.setText(generateTime(currentProgress) + "/"
				+ generateTime(duration));
		if (dis > 0)
			mIv_Progress_bg.setImageResource(R.drawable.btn_fast_forword);
		else
			mIv_Progress_bg.setImageResource(R.drawable.btn_back_forword);
		mFl_Progress.setVisibility(View.VISIBLE);
	}

	/**
	 * 滑动改变亮度
	 * 
	 * @param percent
	 */
	private void onBrightnessSlide(float percent) {
		if (mBrightness < 0) {
			mBrightness = getWindow().getAttributes().screenBrightness;
			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;
			// 显示
			mOperationBg.setImageResource(R.drawable.video_brightness_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}
		WindowManager.LayoutParams lpa = getWindow().getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.01f)
			lpa.screenBrightness = 0.01f;
		getWindow().setAttributes(lpa);

		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
		mOperationPercent.setLayoutParams(lp);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (mVideoView != null)
			mVideoView.setVideoLayout(mLayout, 0);
		super.onConfigurationChanged(newConfig);
	}

	public static String generateTime(long time) {
		int totalSeconds = (int) (time / 1000);
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes,
				seconds) : String.format("%02d:%02d", minutes, seconds);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		video_end.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onDestroy() {
		SpotManager.getInstance(context).onDestroy();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.video_end_back:
			onBackPressed();
			break;

		case R.id.video_replay:
			mVideoView.seekTo(0);
			mVideoView.start();
			video_end.setVisibility(View.GONE);
			break;
		}
	}
}
