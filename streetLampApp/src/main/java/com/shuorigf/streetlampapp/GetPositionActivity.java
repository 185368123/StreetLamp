package com.shuorigf.streetlampapp;

import java.util.Locale;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.shuorigf.streetlampapp.util.DensityUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GetPositionActivity extends NavigationBarActivity {
	MapView mMapView;
	AMap mBaiduMap;
	
	BitmapDescriptor mbdCurrentPosition = BitmapDescriptorFactory
			.fromResource(R.drawable.ic_current_position);
	
	
	private double longitude;
	private double latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(savedInstanceState);
		initData();
	}

	private void initView(Bundle savedInstanceState) {
		View view = View.inflate(this, R.layout.activity_get_position, null);
		mMapView = (MapView) view.findViewById(R.id.bmapView_get_position);
		mMapView.onCreate(savedInstanceState);
		setNavigationBarContentView(view);
		
	}

	private void initData() {
		setTitleText(R.string.get_latitude_and_longitude);
		mBaiduMap = mMapView.getMap();
		Locale locale = getResources().getConfiguration().locale;  
		 String language = locale.getLanguage();  
		 if (language.endsWith("en")) {
			 mBaiduMap.setMapLanguage(AMap.ENGLISH); 
		 }else {
			 mBaiduMap.setMapLanguage(AMap.CHINESE); 
		 }
		MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
		myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点
		mBaiduMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
		mBaiduMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
		mBaiduMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
		mBaiduMap.setInfoWindowAdapter(new InfoWindowAdapter() {
			
			@Override
			public View getInfoWindow(Marker marker) {
				View mPositionView = View.inflate(GetPositionActivity.this,R.layout.pop_show_current_position, null);
				TextView mLongitudeTv = (TextView) mPositionView
						.findViewById(R.id.tv_current_position_longitude_value);
				TextView mLatitudeTv = (TextView) mPositionView
						.findViewById(R.id.tv_current_position_latitude_value);
				Button mSureBtn = (Button) mPositionView
						.findViewById(R.id.btn_current_position_sure);
				mSureBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						back();
					}
				});
				mLongitudeTv.setText(marker.getTitle());
				mLatitudeTv.setText(marker.getSnippet());
				return mPositionView;
			}
			
			@Override
			public View getInfoContents(Marker marker) {
				
				return null;
			}
		});
		mBaiduMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			
			@Override
			public void onCameraChangeFinish(CameraPosition cameraPosition) {
				mBaiduMap.clear();
				latitude = cameraPosition.target.latitude;
				longitude = cameraPosition.target.longitude;
				
				LatLng ll = new LatLng(latitude, longitude);
				MarkerOptions mo = new MarkerOptions().position(ll).icon(
						mbdCurrentPosition).title(longitude + "°E").snippet(latitude + "°N");
				mBaiduMap.addMarker(mo);
				
				if(mBaiduMap.getMapScreenMarkers() != null 
						&& mBaiduMap.getMapScreenMarkers().size() > 0){
					mBaiduMap.getMapScreenMarkers().get(0).showInfoWindow();
				}
				
			}
			
			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
				
			}
		});
	}



	@Override
	public void onDestroy() {
		mBaiduMap.clear();
		mbdCurrentPosition.recycle();
		super.onDestroy();
		mMapView.onDestroy();
		mMapView = null;
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    mMapView.onSaveInstanceState(outState);
	  } 

	
	private void back(){
		Intent data = new Intent();
		data.putExtra("longitude", longitude+"");
		data.putExtra("latitude", latitude+"");
		setResult(RESULT_OK, data);
		finish();
	}

}
