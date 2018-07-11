package com.shuorigf.streetlampapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.OptionsPickerView.OnOptionsSelectListener;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.NetworkData;
import com.shuorigf.streetlampapp.data.NetworkData.Data.Network;
import com.shuorigf.streetlampapp.data.ProjectData;
import com.shuorigf.streetlampapp.data.ResultCodeData;
import com.shuorigf.streetlampapp.data.ProjectData.Data.Project;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.util.locationUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zxing.android.CaptureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;

public class AddLampControlActivity extends NavigationBarActivity implements OnClickListener{
	private static final String TAG = AddLampControlActivity.class.getSimpleName();
	private static final String DECODED_CONTENT_KEY = "codedContent";
	private static final int GET_POSITION_REQUESTCODE = 1;
	private static final int GET_QRCODE_REQUESTCODE = 2;
	
	private EditText mBelongsProjectEdt;
	private EditText mBelongsNetworkEdt;
	private EditText mStreetLampNumberEdt;
	private EditText mSectionEdt;
	private EditText mWirelessModuleIDEdt;
	private EditText mLongitudeEdt; 
	private EditText mLatitudeEdt;
	
	private ImageButton mScanIBtn;
	
	private TextView mGetTv;
	private TextView mAcquisitionMapTv;
	
	private Button mAddBtn;
	
	private LoginData mLoginData;
	private Dialog mDialog;
//	private Dialog mSuccessDialog;
	private Dialog mFailDialog;
	private Dialog mWatingDialog;
	
	private AlertDialog mSuccessDialog;
	
	private List <Project> mProjects;	
	private OptionsPickerView<Project> mProjectOptionsPV;
	private List <Network> mNetworks;	
	private OptionsPickerView<Network> mNetworkOptionsPV;
	
	private String project_id;
	private String network_id;
	
	
	private LocationClient mLocClient;
	private LocationClientOption mOption;
	private MyLocationListenner myListener = new MyLocationListenner();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	private void initView() {
		mProjectOptionsPV = new OptionsPickerView<Project>(this);
		mProjectOptionsPV.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				if (mProjects!=null) {
					mBelongsProjectEdt.setText(mProjects.get(options1).getName());
					project_id = mProjects.get(options1).getId();
					getNetworkData(project_id);
				}
			}
		});
		mNetworkOptionsPV = new OptionsPickerView<Network>(this);
		mNetworkOptionsPV.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				if (mNetworks!=null) {
					mBelongsNetworkEdt.setText(mNetworks.get(options1).getName());
					network_id = mNetworks.get(options1).getId();
				}
			}
		});
		mWatingDialog = DialogFactory.creatRequestDialog(this, 0);
		mDialog = DialogFactory.creatRequestDialog(this, R.string.adding);
//		mSuccessDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_success, R.string.add_success);
		mFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.add_failed);
//       mSuccessDialog.setOnDismissListener(new OnDismissListener() {
//			
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				Intent intent = new Intent();
//			    setResult(RESULT_OK, intent);
//				finish();
//			}
//		});
		View view = View.inflate(this, R.layout.activity_add_lamp_control, null);
		mBelongsProjectEdt = (EditText) view.findViewById(R.id.edt_add_lamp_control_belongs_to_the_project_value);
		mBelongsProjectEdt.setOnClickListener(this);
		mBelongsNetworkEdt = (EditText) view.findViewById(R.id.edt_add_lamp_control_belongs_to_the_network_value);
		mBelongsNetworkEdt.setOnClickListener(this);
		mStreetLampNumberEdt = (EditText) view.findViewById(R.id.edt_add_lamp_control_street_lamp_number_value);
		mSectionEdt = (EditText) view.findViewById(R.id.edt_add_lamp_control_section_value);
		mWirelessModuleIDEdt = (EditText) view.findViewById(R.id.edt_add_lamp_control_wireless_module_id_value);
		mLongitudeEdt = (EditText) view.findViewById(R.id.edt_add_lamp_control_longitude_value);
		mLatitudeEdt = (EditText) view.findViewById(R.id.edt_add_lamp_control_latitude_value);
		mGetTv = (TextView) view.findViewById(R.id.tv_add_lamp_control_get);
		mGetTv.setOnClickListener(this);
		mAcquisitionMapTv = (TextView) view.findViewById(R.id.tv_add_lamp_control_acquisition_map);
		mAcquisitionMapTv.setOnClickListener(this);
		mAddBtn = (Button) view.findViewById(R.id.btn_add_lamp_control_add);
		mAddBtn.setOnClickListener(this);
		mScanIBtn = (ImageButton) view.findViewById(R.id.imgbtn_add_lamp_control_scan);
		mScanIBtn.setOnClickListener(this);
		setNavigationBarContentView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp)getApplication()).mLoginData;
		setTitleText(R.string.add_lamp_control);
		mRightTV.setVisibility(View.VISIBLE);
		mRightTV.setText(R.string.cancel);
		mRightTV.setTextColor(getResources().getColor(R.color.blue));
		mRightTV.setOnClickListener(this);
		
		mLocClient = new LocationClient(this);
        mLocClient.setLocOption(getClientOption());
        mLocClient.registerLocationListener(myListener);
	}
	
	private LocationClientOption getClientOption(){
		if(mOption == null){
			mOption = new LocationClientOption();
			mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
			mOption.setOpenGps(true); // 打开gps
			mOption.setIsNeedAddress(true);
		}
		return mOption;
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.imgbtn_add_lamp_control_scan:
			goScanQRCode();
			break;
		case R.id.tv_add_lamp_control_get:
			if (locationUtils.isOpen(this)) {
				if(mLocClient != null && !mLocClient.isStarted()){
					mLocClient.start();
				}
			}else {
				showOpenGPSDialog();
			}
			break;
		case R.id.tv_add_lamp_control_acquisition_map:
			goGetPosition();
			break;
		case R.id.btn_add_lamp_control_add:
			addLampControl();
			break;
		case R.id.tv_navigation_bar_right:
			finish();
			break;
			
		case R.id.edt_add_lamp_control_belongs_to_the_project_value:
			if (mProjects != null && mProjects.size() > 0) {
				mProjectOptionsPV.show();
			} else {
				getProjectData();
			}
			break;
		case R.id.edt_add_lamp_control_belongs_to_the_network_value:
			if (!TextUtils.isEmpty(mBelongsProjectEdt.getText().toString())) {
				if (mNetworks!=null&& mNetworks.size() > 0) {
					mNetworkOptionsPV.show();
				}else {
					getNetworkData(project_id);
				}
			} else {
				Toast.makeText(this, R.string.please_select_project,
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	
	private void getProjectData() {
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        OkHttpUtils
                .post()
                .url(NetManager.PROJECT_LIST_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<ProjectData>(new JsonGenericsSerializator())
                      {
                	
                	@Override
					public void onBefore(Request request, int id) {
						if (mWatingDialog != null)
							mWatingDialog.show();
					}

					@Override
					public void onAfter(int id) {
						if (mWatingDialog != null)
							mWatingDialog.dismiss();
					}
                	
					@Override
                      public void onError(Call call, Exception e, int id)
                      {
                    	  e.printStackTrace();
                    	  Log.e(TAG, "ProjectData onError:"+e.getMessage());
                    	  Toast.makeText(AddLampControlActivity.this, R.string.network_is_not_smooth,
									Toast.LENGTH_SHORT).show();
                      }
					@Override
					public void onResponse(ProjectData projectData, int id) {
						Log.i(TAG, "ProjectData onResponse:"+projectData.toString());
						 if(projectData.getStatus().equals(NetManager.SUCCESS)){
							    setProjectData(projectData);
							    
						 }else {
							 Toast.makeText(AddLampControlActivity.this, projectData.getMsg(),
										Toast.LENGTH_SHORT).show();
						 }
					}	
                  });
	}
	
	private void setProjectData(ProjectData projectData) {
		mProjects = projectData.getData().getProjects();
		if (mProjects!=null&& mProjects.size() > 0) {
			mProjectOptionsPV.setPicker((ArrayList<Project>) mProjects);
			mProjectOptionsPV.setCyclic(false);
			mProjectOptionsPV.show();
		}
		}
	
	
	private void getNetworkData(String project_id) {
		if (project_id==null) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("project_id", project_id);
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.NETWORK_LIST_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<NetworkData>(new JsonGenericsSerializator())
                      {
                	
                	@Override
					public void onBefore(Request request, int id) {
						if (mWatingDialog != null)
							mWatingDialog.show();
					}

					@Override
					public void onAfter(int id) {
						if (mWatingDialog != null)
							mWatingDialog.dismiss();
					}
                	
					@Override
                      public void onError(Call call, Exception e, int id)
                      {
                    	  e.printStackTrace();
                    	  Log.e(TAG, "NetworkData onError:"+e.getMessage());
                    	  Toast.makeText(AddLampControlActivity.this, R.string.network_is_not_smooth,
									Toast.LENGTH_SHORT).show();
                    	  mNetworks = null;
						  mBelongsNetworkEdt.setText(null);
                      }
					@Override
					public void onResponse(NetworkData networkData, int id) {
						Log.i(TAG, "NetworkData onResponse:"+networkData.toString());
						 if(networkData.getStatus().equals(NetManager.SUCCESS)){
							    setNetworkData(networkData);
							    
						 }else {
							 Toast.makeText(AddLampControlActivity.this, networkData.getMsg(),
										Toast.LENGTH_SHORT).show();
							  mNetworks = null;
							  mBelongsNetworkEdt.setText(null);
						 }
					}	
                  });
	}
	
	private void setNetworkData(NetworkData networkData) {
		mNetworks = networkData.getData().getNetworks();
		if (mNetworks!=null&&mNetworks.size()>0) {
			mNetworkOptionsPV.setPicker((ArrayList<Network>) mNetworks);
			mNetworkOptionsPV.setCyclic(false);
			mBelongsNetworkEdt.setText(mNetworks.get(0).getName());
			network_id = mNetworks.get(0).getId();
		}
		}
	
	private void goGetPosition() {
		Intent intent = new Intent(this, GetPositionActivity.class);
		startActivityForResult(intent, GET_POSITION_REQUESTCODE);
	}
	
	
	private void goScanQRCode() {
		Intent intent = new Intent(this,CaptureActivity.class);
		startActivityForResult(intent, GET_QRCODE_REQUESTCODE);
		
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
        case GET_QRCODE_REQUESTCODE:
            if (resultCode == Activity.RESULT_OK) {
            	if(data!=null) {
    				String content = data.getStringExtra(DECODED_CONTENT_KEY);
    				mWirelessModuleIDEdt.setText(content);
            	}
            }
            break;
        case GET_POSITION_REQUESTCODE:
            if (resultCode == Activity.RESULT_OK) {
            	if(data!=null) {
    				String longitude = data.getStringExtra("longitude");
    				String latitude = data.getStringExtra("latitude");
    				mLongitudeEdt.setText(longitude);
    				mLatitudeEdt.setText(latitude);
            	}
            }
            break;
        default:
            break;
        }
	}

	private void addLampControl() {
//		String belongsProject = mBelongsProjectEdt.getText().toString();
//		String belongsNetwork = mBelongsNetworkEdt.getText().toString();
		String streetLampNumber = mStreetLampNumberEdt.getText().toString();
		String section = mSectionEdt.getText().toString();
		String wirelessModuleID = mWirelessModuleIDEdt.getText().toString();
		String longitude = mLongitudeEdt.getText().toString();
		String latitude = mLatitudeEdt.getText().toString();
		
		if(TextUtils.isEmpty(project_id)) {
			Toast.makeText(this, R.string.network_number_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(network_id)) {
			Toast.makeText(this, R.string.network_name_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(streetLampNumber)) {
			Toast.makeText(this, R.string.section_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(section)) {
			Toast.makeText(this, R.string.section_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(wirelessModuleID)) {
			Toast.makeText(this, R.string.longitude_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(longitude)) {
			Toast.makeText(this, R.string.latitude_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(latitude)) {
			Toast.makeText(this, R.string.latitude_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("project_id", project_id);
        params.put("network_id", network_id);
        params.put("lamp_no", streetLampNumber);
        params.put("section", section);
        params.put("address", wirelessModuleID);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.LAMP_CONTROL_ADD_OR_EDIT_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<ResultCodeData>(new JsonGenericsSerializator())
                      {
                	@Override
					public void onBefore(Request request, int id) {
                		if(mDialog!=null)
                		mDialog.show();
                	}
                	
                      @Override
						public void onAfter(int id) {
                    	  if(mDialog!=null)
                    	  mDialog.dismiss();
                      }
					@Override
                      public void onError(Call call, Exception e, int id)
                      {
                    	  e.printStackTrace();
                    	  Log.e(TAG, "Add Lamp Control onError:"+e.getMessage());
                    	  Toast.makeText(AddLampControlActivity.this, R.string.network_is_not_smooth,
									Toast.LENGTH_SHORT).show();
                      }
					@Override
					public void onResponse(ResultCodeData resultCodeData, int id) {
						Log.i(TAG, "Add Lamp Control onResponse:"+resultCodeData.toString());
						if(resultCodeData.getStatus().equals(NetManager.SUCCESS)){
							showSucessDialog();
						 }else {
							 Toast.makeText(AddLampControlActivity.this, resultCodeData.getMsg(),
										Toast.LENGTH_SHORT).show();
							 if(mFailDialog!=null)
								 mFailDialog.show();
						 }
					}	
                  });
        
        
		
	}
	
	private void showSucessDialog() {
		if (mSuccessDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
			View view = View.inflate(this, R.layout.dialog_continue, null);
			((Button) view.findViewById(R.id.btn_confirm))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mSuccessDialog != null) {
								mSuccessDialog.dismiss();
							}
							mStreetLampNumberEdt.setText(null);
						}
					});
			((Button) view.findViewById(R.id.btn_cancel))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mSuccessDialog != null) {
								mSuccessDialog.dismiss();
							}
							Intent intent = new Intent();
						    setResult(RESULT_OK, intent);
							finish();
						}
					});
			mSuccessDialog = builder.create();
			mSuccessDialog.setView(view);
		}
		if (mSuccessDialog != null) {
			mSuccessDialog.show();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (mProjectOptionsPV.isShowing()) {
			mProjectOptionsPV.dismiss();
			return;
		}
		
		if (mNetworkOptionsPV.isShowing()) {
			mNetworkOptionsPV.dismiss();
			return;
		}
		
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		if(mLocClient != null && mLocClient.isStarted()){
			mLocClient.stop();
		}
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
	}
	
	/**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
                return;
            }
            mLongitudeEdt.setText(location.getLongitude()+"");
			mLatitudeEdt.setText(location.getLatitude()+"");
			if(mLocClient != null && mLocClient.isStarted()){
				mLocClient.stop();
			}	
		}
    }
    
    private AlertDialog mOpenGPSDialog;
    private void showOpenGPSDialog() {
    	if (mOpenGPSDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
			View view = View.inflate(this, R.layout.dialog_continue, null);
			((TextView) view.findViewById(R.id.tv_show_content))
			.setText(R.string.are_you_want_to_open_location_service);
			((Button) view.findViewById(R.id.btn_confirm))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mOpenGPSDialog != null) {
								mOpenGPSDialog.dismiss();
							}
							Intent i = new Intent();
							i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(i);
						}
					});
			((Button) view.findViewById(R.id.btn_cancel))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mOpenGPSDialog != null) {
								mOpenGPSDialog.dismiss();
							}
							if(mLocClient != null && !mLocClient.isStarted()){
								mLocClient.start();
							}
						}
					});
			mOpenGPSDialog = builder.create();
			mOpenGPSDialog.setView(view);
		}
		if (mOpenGPSDialog != null) {
			mOpenGPSDialog.show();
		}
    }
}
