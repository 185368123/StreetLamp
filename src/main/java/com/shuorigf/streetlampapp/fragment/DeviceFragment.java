package com.shuorigf.streetlampapp.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
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
import com.shuorigf.streetlampapp.AddDeviceActivity;
import com.shuorigf.streetlampapp.LampControlDetailsActivity;
import com.shuorigf.streetlampapp.MainActivity;
import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.TotalLampControlListActivity;
import com.shuorigf.streetlampapp.TotalProjectListActivity;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.DeviceData;
import com.shuorigf.streetlampapp.data.LampControlDetailsData;
import com.shuorigf.streetlampapp.data.DeviceData.Data.LampData;
import com.shuorigf.streetlampapp.data.LampControlDetailsData.Data;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.util.DensityUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class DeviceFragment extends BaseFragment implements OnClickListener {

	private static final String TAG = DeviceFragment.class.getSimpleName();
	private static final int SELECT_PROJECT_REQUESTCODE = 1;
	
	private ImageButton mOpenLeftMenuIBtn;
	private TextView mOpenAllTV;

	private ImageButton mAddIBtn;

	private TextView mTotalProjectTV;
	private TextView mLampControlTV;

	private CheckBox mChangeChk;
	private CheckBox mLocationChk;
	private CheckBox mfaultChk;

	MapView mMapView;
	AMap  mBaiduMap;
	private int mBaiduTaskId=0;
//	private boolean mBaiduTaskRuning=false;
	
	 private boolean isBitmapRecycle = false;
	 BitmapDescriptor mbdLampControlErr = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_lamp_control_err_status);
	 BitmapDescriptor mbdLampControlOn = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_lamp_control_on_status);
	 BitmapDescriptor mbdLampControlOff = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_lamp_control_off_status);

	 private boolean isAllLampControl = true; 
	 
	 
	 private LoginData mLoginData;
	 private List<LampData> mLampControls;
	 
	 private String project_id;
	 
	 private boolean isLoading;
	 private boolean isLoaded;
	 
	 
	 private String project_name;
	 private String lamp_numble;
	 
	
	@Override
	public View initView(Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.fragment_device, null);
		mOpenLeftMenuIBtn = (ImageButton) view
				.findViewById(R.id.imgbtn_device_accout);
		mOpenLeftMenuIBtn.setOnClickListener(this);
		
		mAddIBtn = (ImageButton) view.findViewById(R.id.imgbtn_device_add);
		mAddIBtn.setOnClickListener(this);

		mOpenAllTV = (TextView) view.findViewById(R.id.tv_device_open_all);
		mOpenAllTV.setOnClickListener(this);
		mTotalProjectTV = (TextView) view
				.findViewById(R.id.tv_device_total_project_value);
		mTotalProjectTV.setOnClickListener(this);
		mLampControlTV = (TextView) view
				.findViewById(R.id.tv_device_total_lamp_control_value);
		mLampControlTV.setOnClickListener(this);
		mMapView = (MapView) view.findViewById(R.id.bmapView_device);
		mMapView.onCreate(savedInstanceState);
		mChangeChk = (CheckBox) view.findViewById(R.id.chk_device_change);
		mChangeChk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mBaiduMap.setMapType(AMap.MAP_TYPE_SATELLITE);
					 mbdLampControlErr = BitmapDescriptorFactory.fromResource(R.drawable.ic_satellite_lamp_control_err_status);
					  mbdLampControlOn = BitmapDescriptorFactory.fromResource(R.drawable.ic_satellite_lamp_control_on_status);
					  mbdLampControlOff = BitmapDescriptorFactory.fromResource(R.drawable.ic_satellite_lamp_control_off_status);
					 new MyThread().start();
				} else {
					mBaiduMap.setMapType(AMap.MAP_TYPE_NORMAL);
					  mbdLampControlErr = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_lamp_control_err_status);
					  mbdLampControlOn = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_lamp_control_on_status);
					  mbdLampControlOff = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_lamp_control_off_status);
					  new MyThread().start();
				}
			}
		});
		mLocationChk = (CheckBox) view.findViewById(R.id.chk_device_location);
		mLocationChk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					initMyLocation();
				}
			}
		});
		mfaultChk = (CheckBox) view.findViewById(R.id.chk_device_fault);
		mfaultChk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					isAllLampControl = false;
					 new MyThread().start();
				} else{
					isAllLampControl = true;
					 new MyThread().start();
				}
			}
		});
		return view;
	}

	public void initData() {
		mLoginData = ((StreetlampApp)mActivity.getApplication()).mLoginData;
		mBaiduMap = mMapView.getMap();
		Locale locale = getResources().getConfiguration().locale;  
		 String language = locale.getLanguage();  
		 if (language.endsWith("en")) {
			 mBaiduMap.setMapLanguage(AMap.ENGLISH); 
		 }else {
			 mBaiduMap.setMapLanguage(AMap.CHINESE); 
		 }
		mBaiduMap.setOnMarkerClickListener(myOnMarkerClickListener);
		mBaiduMap.setOnInfoWindowClickListener(myOnInfoWindowClickListener);
		mBaiduMap.setInfoWindowAdapter(new InfoWindowAdapter() {
			
			@Override
			public View getInfoWindow(Marker marker) {
				View mLampControlInfoPopView = View.inflate(mActivity, R.layout.pop_show_lamp_control_info, null);
				ImageView mPopLampControlStatusIV = (ImageView) mLampControlInfoPopView.findViewById(R.id.iv_lamp_control_status);
				TextView mPopLampControlNameTV = (TextView) mLampControlInfoPopView.findViewById(R.id.tv_lamp_control_name);
				TextView mPopProjectNameTV = (TextView) mLampControlInfoPopView.findViewById(R.id.tv_project_name_value);
				mPopLampControlNameTV.setText("ID:"+marker.getTitle());
				mPopProjectNameTV.setText(project_name);
				if ("0".equals(marker.getSnippet())) {
					mPopLampControlStatusIV.setImageResource(R.drawable.ic_map_lamp_control_err_status_pop);
				}else if ("1".equals(marker.getSnippet())){
					mPopLampControlStatusIV.setImageResource(R.drawable.ic_map_lamp_control_on_status_pop);
				}else if ("2".equals(marker.getSnippet())){
					mPopLampControlStatusIV.setImageResource(R.drawable.ic_map_lamp_control_off_status_pop);
				}
				return mLampControlInfoPopView;
			}
			
			@Override
			public View getInfoContents(Marker marker) {
				return null;
			}
		});
//		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
//				LocationMode.NORMAL, true, null));
//		mLocationService = ((StreetlampApp) mActivity.getApplication()).locationService;
//		mLocationService.registerListener(myListener);
		// mLocationService.start();
        getDeviceData();
		
	}
	
	
	private void initMyLocation() {
		MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
		myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点
		mBaiduMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
		mBaiduMap.moveCamera(CameraUpdateFactory.zoomTo(15));
		//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
		mBaiduMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
	}
	
	private void getDeviceData() {
		 if (isLoading) {
				return;
			}
		 
		isLoading = true;
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        if (project_id!=null) {
        	 params.put("project_id", project_id);
		}
//        params.put("show_projects", "0");
//        params.put("show_networks", "0");
        params.put("show_lamps", "1");
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.DEVICEDATA_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<DeviceData>(new JsonGenericsSerializator())
                      {
                	
                	
					@Override
						public void onAfter(int id) {
							super.onAfter(id);
							isLoading = false;
						}
					@Override
                      public void onError(Call call, Exception e, int id)
                      {
                    	  e.printStackTrace();
                    	  Log.e(TAG, "DeviceData onError:"+e.getMessage());
                    	  Toast.makeText(mActivity, R.string.network_is_not_smooth,
  								Toast.LENGTH_SHORT).show();
                      }
					@Override
					public void onResponse(DeviceData deviceData, int id) {
						Log.i(TAG, "DeviceData onResponse:"+deviceData.toString());
						 if(deviceData.getStatus().equals(NetManager.SUCCESS)){
							 isLoaded = true;
							 setDeviceData(deviceData);
							    
						 }else {
							 Toast.makeText(mActivity, deviceData.getMsg(),
										Toast.LENGTH_SHORT).show();
						 }
					}	
                  });
	}

	private void setDeviceData(DeviceData deviceData) {
		mTotalProjectTV.setText(getString(R.string.total_project)+deviceData.getData().getTotal_project());
		mLampControlTV.setText(getString(R.string.total_lamp_control)+deviceData.getData().getTotal_lamp());
		mLampControls = deviceData.getData().getLamps();
		new MyThread().start();
		
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_device_accout:
			openLeftMenu();
			break;
		case R.id.tv_device_open_all:
			goSelectProject();
			break;
		case R.id.imgbtn_device_add:
			goAddDevice();
			break;
		case R.id.tv_device_total_project_value:
			goTotalProjectList();
			break;
		case R.id.tv_device_total_lamp_control_value:
			goTotalLampControlList();
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
	
	private void goLampControlDetails(String lamp_id) {
		if (lamp_id == null) {
			return;
		}
		Bundle lamp = new Bundle();
		lamp.putString("lamp_numble", lamp_numble);
		lamp.putString("lamp_id", lamp_id);
		Intent intent = new Intent(mActivity,LampControlDetailsActivity.class);
		intent.putExtras(lamp);
		mActivity.startActivity(intent);
	}

	private void goSelectProject() {
		Intent intent = new Intent(mActivity, TotalProjectListActivity.class);
		intent.putExtra("type", TotalProjectListActivity.SELECT_PROJECT);
		this.startActivityForResult(intent, SELECT_PROJECT_REQUESTCODE);
	}

	private void goAddDevice() {
		Intent intent = new Intent(mActivity, AddDeviceActivity.class);
		mActivity.startActivity(intent);
	}

	private void goTotalProjectList() {
		Intent intent = new Intent(mActivity, TotalProjectListActivity.class);
		intent.putExtra("type", TotalProjectListActivity.TOTAL_PROJECT);
		mActivity.startActivity(intent);
	}

	private void goTotalLampControlList() {
		Intent intent = new Intent(mActivity,
				TotalLampControlListActivity.class);
		mActivity.startActivity(intent);
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
        case SELECT_PROJECT_REQUESTCODE:
            if (resultCode == Activity.RESULT_OK) {
                if(data!=null) {
                	mOpenAllTV.setText(data.getStringExtra("project_name"));
                	project_id = data.getStringExtra("project_id");
                	OkHttpUtils.getInstance().cancelTag(this);
                	isLoaded = false;
                	getDeviceData();
                }
            }
            break;

        default:
            break;
        }
	}
	
	

	@Override
	public void onDestroy() {
		++mBaiduTaskId;
		isBitmapRecycle = true;
		mbdLampControlErr.recycle();
		mbdLampControlOff.recycle();
		mbdLampControlOn.recycle();
		super.onDestroy();
		mMapView.onDestroy();
		mMapView = null;
		OkHttpUtils.getInstance().cancelTag(this);
		OkHttpUtils.getInstance().cancelTag(NetManager.LAMP_CONTROL_DETAILS_URL);
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
	
	AMap.OnMarkerClickListener myOnMarkerClickListener = new AMap.OnMarkerClickListener() {
	    
	    @Override
	    public boolean onMarkerClick(Marker marker) {
	    	OkHttpUtils.getInstance().cancelTag(NetManager.LAMP_CONTROL_DETAILS_URL);
			
			getLampControlDetailsData(marker);
			return true;
	    }
	};
	
	
	OnInfoWindowClickListener myOnInfoWindowClickListener = new OnInfoWindowClickListener() {
		 
	    @Override
	    public void onInfoWindowClick(Marker marker) {
	    	goLampControlDetails(marker.getTitle());
	    	marker.hideInfoWindow();
	    }
	};
	
	
	
	private void getLampControlDetailsData(final Marker marker) {
		if (marker.getTitle() == null) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("lamp_id", marker.getTitle());
		OkHttpUtils
				.post()
				.tag(NetManager.LAMP_CONTROL_DETAILS_URL)
				.url(NetManager.LAMP_CONTROL_DETAILS_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<LampControlDetailsData>(
								new JsonGenericsSerializator()) {
							

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG, "LampControlDetailsData onError:"
										+ e.getMessage());
							}

							@Override
							public void onResponse(
									LampControlDetailsData lampControlDetailsData,
									int id) {
								Log.i(TAG, "LampControlDetailsData onResponse:"
										+ lampControlDetailsData.toString());
								if (lampControlDetailsData.getStatus().equals(
										NetManager.SUCCESS)) {
									setLampControlDetailsData(lampControlDetailsData,marker);

								} else {
									Toast.makeText(
											mActivity,
											lampControlDetailsData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});		
	}

	private void setLampControlDetailsData(LampControlDetailsData lampControlDetailsData, Marker marker) {
		Data data = lampControlDetailsData.getData();
		project_name = data.getProject_name();
		lamp_numble = data.getLamp_no();
		marker.showInfoWindow();
	}

	Handler mHandler = new Handler();
    private void addOverlays() {
     	int id = ++mBaiduTaskId;
//       	while(mBaiduTaskRuning);
       	
//       	mBaiduTaskRuning = true;
     	mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mBaiduMap.clear();
			}
		});
     	
     	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	if(mLampControls==null||mLampControls.size()<=0)
    	{
//    		mBaiduTaskRuning = false;
    		return;
    	}
    	for (LampData lampData : mLampControls) {
    		if(id !=  mBaiduTaskId)
    		{
    			Log.e(TAG, "mBaiduTaskId:"+mBaiduTaskId+",id:"+id);
//    			mBaiduTaskRuning = false;
    			return;
    		}
    		LatLng ll = new LatLng(lampData.getLatitude(), lampData.getLongitude());
    		if(lampData.getIsfaulted()==1) {
    			if (!isBitmapRecycle) {
    				MarkerOptions mo = new MarkerOptions().position(ll).icon(mbdLampControlErr).title(lampData.getId()).snippet("0");
        			mBaiduMap.addMarker(mo);
				}
    			
    		}else {
    			if(isAllLampControl){
    				if(lampData.getStatus()==1){
    					if (!isBitmapRecycle) {
    						MarkerOptions mo = new MarkerOptions().position(ll).icon(mbdLampControlOn).title(lampData.getId()).snippet("1");
    						mBaiduMap.addMarker(mo);
    					}
    					
    				}else {
    					if (!isBitmapRecycle) {
    						MarkerOptions mo = new MarkerOptions().position(ll).icon(mbdLampControlOff).title(lampData.getId()).snippet("2");
    						mBaiduMap.addMarker(mo);
    					}
    					
    				}
    			}
    		}
    	
    	}
//    	mBaiduTaskRuning = false;
    }
    
    
    public void onVisible(){  
    	if (isPrepared&&!isLoaded) {
    		getDeviceData();
	}
    }  
  
    public void onInvisible(){
    	
    }  	   
    
    private class MyThread extends Thread {

		@Override
		public void run() {
			super.run();
			addOverlays();
		}
    	
    }
    

}
