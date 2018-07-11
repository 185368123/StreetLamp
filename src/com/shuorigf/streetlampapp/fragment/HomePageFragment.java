package com.shuorigf.streetlampapp.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.shuorigf.streetlampapp.MainActivity;
import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.SwichCityActivity;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.GlobalStaticFun;
import com.shuorigf.streetlampapp.data.HomeData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.WeatherData;
import com.shuorigf.streetlampapp.data.WeatherData.Result;
import com.shuorigf.streetlampapp.data.WeatherData.Result.Weather;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.ui.RingRate;
import com.shuorigf.streetlampapp.util.SharePreferenceUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomePageFragment extends BaseFragment implements OnClickListener {

	private static final String TAG = HomePageFragment.class.getSimpleName();
	private static final int SWITCH_CITY_REQUESTCODE = 1;

	private static final String PREF_CURRENT_CITY = "current_city";
	private ImageButton mOpenLeftMenuIBtn;
	private TextView mAddressTV;

	private RingRate mRingRate;

	private ImageView mWeatherIV;
	private TextView mWeatherTemperatureTV;
	private TextView mWeatherPhenomenonTV;
	private TextView mWeatherAirQualityTV;

	private TextView mLightingRateTV;
	private TextView mOnlineRateTv;
	private TextView mFailureRateTV;

	private TextView mTotalGeneratingCapacityTV;
	private TextView mTotalInstalledCapacityTV;
	private TextView mTotalInstalledLightsTV;
	private TextView mTotalNetworkNumberTV;

	private TextView mSaveStandardCoalTV;
	private TextView mCumulativeReductionCO2TV;
	private TextView mCumulativeReductionSO2TV;

	private LoginData mLoginData;

	private LocationClient mLocClient;
	private LocationClientOption mOption;
	private MyLocationListenner myListener = new MyLocationListenner();

	private boolean isLoading;
	private boolean isLoaded;

	private String mCity;

	@Override
	public View initView(Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.fragment_homepage, null);
		mOpenLeftMenuIBtn = (ImageButton) view
				.findViewById(R.id.imgbtn_homepage_accout);
		mOpenLeftMenuIBtn.setOnClickListener(this);
		mRingRate = (RingRate) view.findViewById(R.id.ringrate_homepage_rate);
		mAddressTV = (TextView) view.findViewById(R.id.tv_homepage_address);
		mAddressTV.setOnClickListener(this);
		mOnlineRateTv = (TextView) view
				.findViewById(R.id.tv_homepage_online_rate_value);
		mFailureRateTV = (TextView) view
				.findViewById(R.id.tv_homepage_failure_rate_value);
		mLightingRateTV = (TextView) view
				.findViewById(R.id.tv_homepage_lighting_rate_value);
		mWeatherIV = (ImageView) view
				.findViewById(R.id.iv_homepage_weather);
		
		mWeatherTemperatureTV = (TextView) view
				.findViewById(R.id.tv_homepage_weather_temperature);
		mWeatherPhenomenonTV = (TextView) view
				.findViewById(R.id.tv_homepage_weather_phenomenon);
		mWeatherAirQualityTV = (TextView) view
				.findViewById(R.id.tv_homepage_weather_air_quality_value);

		mTotalGeneratingCapacityTV = (TextView) view
				.findViewById(R.id.tv_homepage_total_generating_capacity_value);
		mTotalInstalledCapacityTV = (TextView) view
				.findViewById(R.id.tv_homepage_total_installed_capacity_value);
		mTotalInstalledLightsTV = (TextView) view
				.findViewById(R.id.tv_homepage_total_installed_lights_value);
		mTotalNetworkNumberTV = (TextView) view
				.findViewById(R.id.tv_homepage_total_network_number_value);

		mSaveStandardCoalTV = (TextView) view
				.findViewById(R.id.tv_homepage_save_standard_coal_value);
		mCumulativeReductionCO2TV = (TextView) view
				.findViewById(R.id.tv_homepage_cumulative_reduction_co2_value);
		mCumulativeReductionSO2TV = (TextView) view
				.findViewById(R.id.tv_homepage_cumulative_reduction_so2_value);

		return view;
	}

	public void initData() {
		mLoginData = ((StreetlampApp) mActivity.getApplication()).mLoginData;
		mCity = SharePreferenceUtils.getString(mActivity, PREF_CURRENT_CITY,
				null);
		mAddressTV.setText(mCity);
		getHomeData();
		getWeatherData();
		mLocClient = new LocationClient(mActivity);
		mLocClient.setLocOption(getClientOption());
		mLocClient.registerLocationListener(myListener);
		mLocClient.start();

		// mLocationService = new
		// LocationService(mActivity.getApplicationContext());
		// mLocationService.setLocationOption(getClientOption());
		// mLocationService.registerListener(myListener);
		// mLocationService.start();
	}

	private void getWeatherData() {
		if (mCity == null) {
			return;
		}
		OkHttpUtils
				.get()
				.tag("weather")
				.url("http://api.map.baidu.com/telematics/v3/weather?location="
						+ mCity
						+ "&output=json&ak=OylRCxyZWxNEcXZOdqRbun1FWwVl4Rkr&mcode=E1:23:45:B2:AF:CE:D4:9B:2C:F0:28:FA:FC:3A:CB:9C:B1:73:5D:D7;com.shuorigf.streetlampapp")
				.build()
				.execute(
						new GenericsCallback<WeatherData>(
								new JsonGenericsSerializator()) {

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"WeatherData onError:" + e.getMessage());
								if (e.toString().contains("closed")) {
									return;
								}
		                  	  Toast.makeText(mActivity, R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(WeatherData weatherData,
									int id) {
								Log.i(TAG, "WeatherData onResponse:"
										+ weatherData.toString());
								if (WeatherData.SUCCESS_CODE.equals(weatherData.getError())) {
									setWeatherData(weatherData);
								}else {
									Toast.makeText(mActivity,
											weatherData.getStatus(),
											Toast.LENGTH_SHORT).show();
								}
							}

						});
	}
	
	private void setWeatherData(WeatherData weatherData) {
		 List<Result> results = weatherData.getResults();
		 if (results!=null&&results.size()>0) {
			 String temperature = results.get(0).getWeather_data().get(0).getDate();
			 if (temperature!=null&&temperature.indexOf("(")!=-1&&temperature.indexOf(")")!=-1) {
				 String str = temperature.substring(temperature.indexOf("(")+1, temperature.indexOf(")"));
				 if(str.length() > 3) {
					 mWeatherTemperatureTV.setText(str.substring(3));
				 }else {
					 mWeatherTemperatureTV.setText(str);
				 }
				
			}
			 mWeatherAirQualityTV.setText(getPM25ToString(results.get(0).getPm25()));
			 List<Weather> weathers = results.get(0).getWeather_data();
			 if (weathers!=null&&weathers.size()>0) {
				 int res = R.string.wheather_1;
				 if(weathers.get(0).getWeather() != null) {
					 if(weathers.get(0).getWeather().contains("晴")) {
						 res = R.string.wheather_1;
					 }else if(weathers.get(0).getWeather().contains("阴")) {
						 res = R.string.wheather_2;
					 }else if(weathers.get(0).getWeather().contains("雨")) {
						 res = R.string.wheather_3;
					 }else if(weathers.get(0).getWeather().contains("雪")) {
						 res = R.string.wheather_4;
					 }else if(weathers.get(0).getWeather().contains("雾")) {
						 res = R.string.wheather_5;
					 }else if(weathers.get(0).getWeather().contains("多云")) {
						 res = R.string.wheather_6;
					 }else if(weathers.get(0).getWeather().contains("台风")) {
						 res = R.string.wheather_7;
					 }else if(weathers.get(0).getWeather().contains("沙尘暴")) {
						 res = R.string.wheather_8;
					 }
				 }
				 mWeatherPhenomenonTV.setText(res);
				 getWeatherImage(weathers.get(0).getDayPictureUrl());
			}
			 
			 
		}
		 
	}
	private int getPM25ToString(int pm25) {
		int id = R.string.excellent;
		if (pm25>0&&pm25<=35) {
			id = R.string.excellent;
		}else if (pm25<=75) {
			id = R.string.good;
		}else if (pm25<=115) {
			id = R.string.slight_pollution;
		}else if (pm25<=150) {
			id = R.string.moderate_pollution;
		}else if (pm25<=250) {
			id = R.string.heavy_pollution;
		}else if (pm25>250) {
			id = R.string.severe_pollution;
		}
		return id;
	}
	
	private void getWeatherImage(String url) {
		if (url == null) {
			return;
		}
		OkHttpUtils.get().tag("weather").url(url).build()
				.execute(new BitmapCallback() {

					@Override
					public void onAfter(int id) {
						super.onAfter(id);
					}

					@Override
					public void onResponse(Bitmap bitmap, int id) {
						isLoaded = true;
						if (bitmap != null)
							mWeatherIV.setImageBitmap(bitmap);
					}

					@Override
					public void onError(Call call, Exception e, int id) {
					  Log.e(TAG, "Get Bitmap onError:"+e.getMessage());
					  if (e.toString().contains("closed")) {
							return;
						}
                  	  Toast.makeText(mActivity, R.string.network_is_not_smooth,
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void getHomeData() {
		if (isLoading) {
			return;
		}
		isLoading = true;
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		OkHttpUtils
				.post()
				.tag(this)
				.url(NetManager.HOMEDATA_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<HomeData>(
								new JsonGenericsSerializator()) {

							@Override
							public void onAfter(int id) {
								super.onAfter(id);
								isLoading = false;
							}

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG, "HomeData onError:" + e.getMessage());
								Toast.makeText(mActivity,
										R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(HomeData homeData, int id) {
								Log.i(TAG,
										"HomeData onResponse:"
												+ homeData.toString());
								if (homeData.getStatus().equals(
										NetManager.SUCCESS)) {
									isLoaded = true;
									setHomeData(homeData);

								} else {
									Toast.makeText(mActivity,
											homeData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});
	}

	private void setHomeData(HomeData homeData) {
		mOnlineRateTv.setText(GlobalStaticFun.floatToPercentageString(homeData
				.getData().getOnline_rate()));
		mFailureRateTV.setText(GlobalStaticFun.floatToPercentageString(homeData
				.getData().getFailure_rate()));
		mLightingRateTV
				.setText(GlobalStaticFun.floatToPercentageString(homeData
						.getData().getLighting_rate()));
		mRingRate.setCircleRadian(homeData.getData().getOnline_rate(), homeData
				.getData().getLighting_rate(), homeData.getData()
				.getFailure_rate());
		mTotalGeneratingCapacityTV.setText(homeData.getData().getTotal_power()
				+ "");
		mTotalInstalledCapacityTV.setText(homeData.getData().getTotal_install()
				+ "");
		mTotalInstalledLightsTV
				.setText(homeData.getData().getTotal_lamp() + "");
		mTotalNetworkNumberTV.setText(homeData.getData().getTotal_network()
				+ "");
		mSaveStandardCoalTV.setText(homeData.getData().getCoal_saving() + "");
		mCumulativeReductionCO2TV.setText(homeData.getData().getCo2_emission()
				+ "");
		mCumulativeReductionSO2TV.setText(homeData.getData().getSo2_emission()
				+ "");
	}

	public LocationClientOption getClientOption() {
		if (mOption == null) {
			mOption = new LocationClientOption();
			mOption.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
			mOption.setOpenGps(true); // 打开gps
			mOption.setIsNeedAddress(true);
		}
		return mOption;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_homepage_accout:
			openLeftMenu();
			break;
		case R.id.tv_homepage_address:
			goSwichCity();
			break;
		default:
			break;
		}
	}

	private void openLeftMenu() {
		MainActivity mainActivity = (MainActivity) mActivity;
		// mainActivity.getSlidingMenu().toggle();
		mainActivity.toggle();
	}

	private void goSwichCity() {
		Intent intent = new Intent(mActivity, SwichCityActivity.class);
		this.startActivityForResult(intent, SWITCH_CITY_REQUESTCODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SWITCH_CITY_REQUESTCODE:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					mCity = data.getStringExtra("city");
					mAddressTV.setText(mCity);
					SharePreferenceUtils.putString(mActivity,
							PREF_CURRENT_CITY, mCity);
					OkHttpUtils.getInstance().cancelTag("WeatherData");
					getWeatherData();
				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null) {
				return;
			}
			String city = location.getCity();
			if (!TextUtils.isEmpty(city)) {
				if (city.substring(city.length() - 1).equals("市")) {
					mCity = city.substring(0, city.length() - 1);
				}
				mAddressTV.setText(mCity);
				SharePreferenceUtils.putString(mActivity, PREF_CURRENT_CITY,
						city);
				OkHttpUtils.getInstance().cancelTag("WeatherData");
				getWeatherData();
			}

			if (mLocClient != null && mLocClient.isStarted()) {
				mLocClient.stop();
			}
		}

	}


	public void onVisible() {
		if (isPrepared && !isLoaded) {
			getHomeData();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
		OkHttpUtils.getInstance().cancelTag("WeatherData");
	}

	public void onInvisible() {
		if (mLocClient != null && mLocClient.isStarted()) {
			mLocClient.stop();
		}
	}

}
