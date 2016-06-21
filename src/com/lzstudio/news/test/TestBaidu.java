package com.lzstudio.news.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.lzstudio.news.R;

public class TestBaidu extends Activity implements OnClickListener {
	private TextView tv;
	private Button bt;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private String tempcoor = "gcj02";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_baidu);
		initLocation();
		tv = (TextView) this.findViewById(R.id.tv);
		bt = (Button) this.findViewById(R.id.bt);
		bt.setOnClickListener(this);
	}

	private void initLocation() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 5000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	@Override
	public void onClick(View v) {
		mLocationClient.start();
	}

	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());
			sb.append("\naddr : ");
			sb.append(location.getCity());
			tv.setText(sb);
			System.out.println(sb.toString());
			mLocationClient.stop();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
	}
}
