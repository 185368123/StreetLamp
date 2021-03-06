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
import com.shuorigf.streetlampapp.data.ProjectData;
import com.shuorigf.streetlampapp.data.ResultCodeData;
import com.shuorigf.streetlampapp.data.ProjectData.Data.Project;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.util.locationUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddNetworkActivity extends NavigationBarActivity implements OnClickListener{
	private static final String TAG = AddNetworkActivity.class.getSimpleName();
	private static final int GET_POSITION_REQUESTCODE = 1;
	
	
	private EditText mBelongsProjectEdt;
	private EditText mNetworkNumberEdt;
	private EditText mNetworkNameEdt;
	private EditText mSectionEdt;
	private EditText mSIMNumberEdt;
	private EditText mLongitudeEdt; 
	private EditText mLatitudeEdt; 
	
	private TextView mGetTv;
	private TextView mAcquisitionMapTv;
	
	private Button mAddBtn;
	
	private LoginData mLoginData;
	private Dialog mDialog;
	private Dialog mSuccessDialog;
	private Dialog mFailDialog;
	
	private Dialog mWatingDialog;
	
	private List <Project> mProjects;
	
	private String project_id;
	
	private OptionsPickerView<Project> mOptionsPV;
	
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
		mOptionsPV = new OptionsPickerView<Project>(this);
		mOptionsPV.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				if (mProjects!=null) {
					mBelongsProjectEdt.setText(mProjects.get(options1).getName());
					project_id = mProjects.get(options1).getId();
				}
			}
		});
		mWatingDialog = DialogFactory.creatRequestDialog(this, 0);
		mDialog = DialogFactory.creatRequestDialog(this, R.string.adding);
		mSuccessDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_success, R.string.add_success);
		mFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.add_failed);
		mSuccessDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent();
			    setResult(RESULT_OK, intent);
				finish();
			}
		});
		View view = View.inflate(this, R.layout.activity_add_network, null);
		mBelongsProjectEdt = (EditText) view.findViewById(R.id.edt_add_network_belongs_to_the_project_value);
		mBelongsProjectEdt.setOnClickListener(this);
		mNetworkNumberEdt = (EditText) view.findViewById(R.id.edt_add_network_network_number_value);
		mNetworkNameEdt = (EditText) view.findViewById(R.id.edt_add_network_network_name_value);
		mSectionEdt = (EditText) view.findViewById(R.id.edt_add_network_section_value);
		mSIMNumberEdt = (EditText) view.findViewById(R.id.edt_add_network_sim_number_value);
		mLongitudeEdt = (EditText) view.findViewById(R.id.edt_add_network_longitude_value);
		mLatitudeEdt = (EditText) view.findViewById(R.id.edt_add_network_latitude_value);
		
		mGetTv = (TextView) view.findViewById(R.id.tv_add_network_get);
		mGetTv.setOnClickListener(this);
		mAcquisitionMapTv = (TextView) view.findViewById(R.id.tv_add_network_acquisition_map);
		mAcquisitionMapTv.setOnClickListener(this);
		mAddBtn = (Button) view.findViewById(R.id.btn_add_network_add);
		mAddBtn.setOnClickListener(this);
		setNavigationBarContentView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp)getApplication()).mLoginData;
		setTitleText(R.string.add_network);
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

	private void getProjectData() {
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        OkHttpUtils
                .post()
                .tag(this)
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
                    	  Toast.makeText(AddNetworkActivity.this, R.string.network_is_not_smooth,
									Toast.LENGTH_SHORT).show();
                      }
					@Override
					public void onResponse(ProjectData projectData, int id) {
						Log.i(TAG, "ProjectData onResponse:"+projectData.toString());
						 if(projectData.getStatus().equals(NetManager.SUCCESS)){
							    setProjectData(projectData);
							    
						 }else {
							 Toast.makeText(AddNetworkActivity.this, projectData.getMsg(),
										Toast.LENGTH_SHORT).show();
						 }
					}	
                  });
		
	}

	private void setProjectData(ProjectData projectData) {
		mProjects = projectData.getData().getProjects();
		if (mProjects!=null && mProjects.size() > 0) {
			mOptionsPV.setPicker((ArrayList<Project>) mProjects);
			mOptionsPV.setCyclic(false);
			mOptionsPV.show();
		}
		}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edt_add_network_belongs_to_the_project_value:
			if (mProjects != null && mProjects.size() > 0) {
				mOptionsPV.show();
			} else {
				getProjectData();
			}
			break;
		case R.id.tv_add_network_get:
			if (locationUtils.isOpen(this)) {
				if(mLocClient != null && !mLocClient.isStarted()){
					mLocClient.start();
				}
			}else {
				showOpenGPSDialog();
			}
			
			break;
		case R.id.tv_add_network_acquisition_map:
			goGetPosition();
			break;
		case R.id.btn_add_network_add:
			addNetwork();
			break;
		case R.id.tv_navigation_bar_right:
			finish();
			break;
		default:
			break;
		}
	}
	
	
	private void goGetPosition() {
		Intent intent = new Intent(this, GetPositionActivity.class);
		startActivityForResult(intent, GET_POSITION_REQUESTCODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
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
	
	private void addNetwork() {
//		String belongsProject = mBelongsProjectEdt.getText().toString();
		String networkNumber = mNetworkNumberEdt.getText().toString();
		String networkName = mNetworkNameEdt.getText().toString();
		String section = mSectionEdt.getText().toString();
		String simNumber = mSIMNumberEdt.getText().toString();
		String longitude = mLongitudeEdt.getText().toString();
		String latitude = mLatitudeEdt.getText().toString();
		
		if(TextUtils.isEmpty(project_id)) {
			Toast.makeText(this, R.string.belongs_to_the_project_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(networkNumber)) {
			Toast.makeText(this, R.string.network_number_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(networkName)) {
			Toast.makeText(this, R.string.network_name_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(section)) {
			Toast.makeText(this, R.string.section_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(simNumber)) {
			Toast.makeText(this, R.string.sim_number_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(longitude)) {
			Toast.makeText(this, R.string.longitude_cannot_be_empty,
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
        params.put("network_no", networkNumber);
        params.put("network_name", networkName);
        params.put("section", section);
        params.put("simcard", simNumber);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.NETWORK_ADD_OR_EDIT_URL)
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
                    	  Log.e(TAG, "Add Network onError:"+e.getMessage());
                    	  Toast.makeText(AddNetworkActivity.this, R.string.network_is_not_smooth,
									Toast.LENGTH_SHORT).show();
                      }
					@Override
					public void onResponse(ResultCodeData resultCodeData, int id) {
						 if(resultCodeData.getStatus().equals(NetManager.SUCCESS)){
							 if(mSuccessDialog!=null)
								 mSuccessDialog.show();
						 }else {
							 Toast.makeText(AddNetworkActivity.this,
									 resultCodeData.getMsg(),
										Toast.LENGTH_SHORT).show();
							 if(mFailDialog!=null)
								 mFailDialog.show();
						 }
					}	
                  });
		
	}
	
	@Override
	public void onBackPressed() {
		if (mOptionsPV.isShowing()) {
			mOptionsPV.dismiss();
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
