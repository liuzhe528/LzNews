package com.lzstudio.news.view.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.lzstudio.news.R;

public class LuckyPanView extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder mHolder;
	/**
	 * 用于绘制的线程
	 */
	private Thread mThread;
	/**
	 * 与surfaceHolder绑定的canvas
	 */
	private Canvas mCanvas;
	/**
	 * 线程的控制开关
	 */
	private boolean isRunning;

	/**
	 * 抽奖文字
	 */
	private String[] mStrs = new String[] { "休息", "扫地", "洗碗", "家务全包", "做饭",
			"洗衣服" };

	/**
	 * 盘块背景颜色
	 */
	private int[] mColors = new int[] { 0xFFFFC300, 0xFFF17E01, 0xFFFFC300,
			0xFFF17E01, 0xFFFFC300, 0xFFF17E01 };
	/**
	 * 与文字对应的图片
	 */
	private int[] mImgs = new int[] { R.drawable.game_xiuxi,
			R.drawable.game_saodi, R.drawable.game_xiwan, R.drawable.game_all,
			R.drawable.game_zhufang, R.drawable.game_xiyifu };
	/**
	 * 与文字对应图片的bitmap数组
	 */
	private Bitmap[] mImgsBitmap;
	/**
	 * 盘块的个数
	 */
	private int mItemCount = 6;
	/**
	 * 绘制盘块的范围
	 */
	private RectF mRange = new RectF();
	/**
	 * 圆的直径
	 */
	private int mRadius;
	/**
	 * 绘制盘块的画笔
	 */
	private Paint mArcPaint;

	/**
	 * 绘制文字的画笔
	 */
	private Paint mTextPaint;

	/**
	 * 滚动的速度
	 */
	private double mSpeed;
	private volatile float mStartAngle = 0;
	/**
	 * 是否点击了停止
	 */
	private boolean isShouldEnd;

	/**
	 * 控件的中心位置
	 */
	private int mCenter;
	/**
	 * 控件的padding，这里我们认为4个padding的值一致，以paddingleft为标准
	 */
	private int mPadding;

	/**
	 * 背景图的bitmap
	 */
	private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.bg2);
	/**
	 * 文字的大小
	 */
	private float mTextSize = TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());
	/**
	 * 每个盘块的角度
	 */
	private float sweepAngle = (float) (360 / mItemCount);

	public LuckyPanView(Context context) {
		this(context, null);
	}

	public LuckyPanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder();
		mHolder.addCallback(this);
		// 设置可获得焦点
		setFocusable(true);
		setFocusableInTouchMode(true);
		// 设置常亮
		this.setKeepScreenOn(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = Math.min(getMeasuredHeight(), getMeasuredWidth());
		// 获取圆的直径
		mRadius = width - getPaddingLeft() - getPaddingRight();
		// 获取中心点
		mCenter = width / 2;
		// padding值
		mPadding = getPaddingLeft();
		setMeasuredDimension(width, width);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 初始化绘画圆形画笔
		mArcPaint = new Paint();
		mArcPaint.setAntiAlias(true);
		mArcPaint.setDither(true);
		// 初始化绘画文字画笔
		mTextPaint = new Paint();
		mTextPaint.setColor(0xFFffffff);
		mTextPaint.setTextSize(mTextSize);
		// 圆弧的绘制范围
		mRange = new RectF(mPadding, mPadding, mPadding + mRadius, mPadding
				+ mRadius);
		// 初始化图片
		mImgsBitmap = new Bitmap[mItemCount];
		for (int i = 0; i < mImgsBitmap.length; i++) {
			mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(),
					mImgs[i]);
		}
		// 开启线程
		isRunning = true;
		mThread = new Thread(this);
		mThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 通知关闭线程
		isRunning = false;
	}

	@Override
	public void run() {
		// 不断的进行draw
		while (isRunning) {
			// 保证绘制的速度为50ms/张以上
			try {
				long start = System.currentTimeMillis();
				draw();
				long end = System.currentTimeMillis();
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void draw() {
		try {
			// 获得canvas
			mCanvas = mHolder.lockCanvas();
			if (mCanvas != null) {
				// 绘制背景图片
				drawBg();
				// 绘制盘块,盘块上的文字，图片
				float tempAngle = mStartAngle;
				for (int i = 0; i < mItemCount; i++) {
					mArcPaint.setColor(mColors[i]);
					mCanvas.drawArc(mRange, tempAngle, sweepAngle, true,
							mArcPaint);
					// 绘制文本
					drawText(tempAngle, sweepAngle, mStrs[i]);
					// 绘制图片
					drawIcon(tempAngle, mImgsBitmap[i]);
					tempAngle += sweepAngle;
				}
				// 如果mSpeed不等于0，则相当于在滚动
				mStartAngle += mSpeed;
				// 点击停止时，设置mSpeed为递减，为0值转盘停止
				if (isShouldEnd) {
					mSpeed -= 1;
				}
				if (mSpeed <= 0) {
					mSpeed = 0;
					isShouldEnd = false;
				}
			}
		} catch (Exception e) {
		} finally {
			if (mCanvas != null)
				mHolder.unlockCanvasAndPost(mCanvas);
		}
	}

	/**
	 * 开始旋转
	 * 
	 * @param index
	 *            内定的中奖项
	 */
	public void startLucky(int index) {
		// 计算中奖的范围
		float from = 270 - (index + 1) * sweepAngle;
		float to = from + sweepAngle;
		// 设置停止时转动的距离
		float targetFrom = from + 4 * 360;
		float targetTo = 4 * 360 + to;
		// 其实速度就是决定最后中奖项的因素(等差数列)
		float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetFrom) - 1) / 2;
		float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;
		mSpeed = genarateSpeed(v1, v2);
		isShouldEnd = false;
	}

	private float genarateSpeed(float v1, float v2) {
		float speed = (float) (v1 + Math.random() * (v2 - v1));
		if (speed == v1 || speed == v2) {
			speed = (float) (v1 + (v2 - v1) * 0.5);
		}
		return speed;
	}

	/**
	 * 停止旋转
	 */
	public void luckyEnd() {
		mStartAngle = 0;
		isShouldEnd = true;
	}

	/**
	 * 绘制盘块上的图标
	 * 
	 * @param startAngle
	 * @param bitmap
	 */
	private void drawIcon(float startAngle, Bitmap bitmap) {
		// 设置图标的宽度为直径的1/8
		int imgWidth = mRadius / 8;
		float angle = (float) ((360 / mItemCount / 2 + startAngle) * (Math.PI / 180));
		// 计算图标中心点坐标
		int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
		int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));
		// 图标绘制的区域
		Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth
				/ 2, y + imgWidth / 2);
		mCanvas.drawBitmap(bitmap, null, rect, null);
	}

	/**
	 * 绘制文字
	 * 
	 * @param tempAngle
	 * @param sweepAngle2
	 * @param string
	 */
	private void drawText(float tempAngle, float sweepangle, String string) {
		Path path = new Path();
		path.addArc(mRange, tempAngle, sweepangle);
		// 测量文字的宽度
		float textWidth = mTextPaint.measureText(string);
		// 水平偏移量
		float hOffset = (float) (Math.PI * mRadius / mItemCount / 2 - textWidth / 2);
		// 垂直偏移量
		float vOffset = mRadius / 2 / 6;
		mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
	}

	/**
	 * 绘制背景图片
	 */
	private void drawBg() {
		mCanvas.drawColor(0xffffffff);
		mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2,
				mPadding / 2, getMeasuredWidth() - mPadding / 2,
				getMeasuredWidth() - mPadding / 2), null);
	}

	public boolean isStart() {
		return mSpeed != 0;
	}

	public boolean isShouldEnd() {
		return isShouldEnd;
	}
}
