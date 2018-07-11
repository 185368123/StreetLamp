package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.DeviceData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.network.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddDeviceActivity extends NavigationBarActivity implements OnClickListener{
	private static final String TAG = AddDeviceActivity.class.getSimpleName();
	private static final int ADD_DEVICE_REQUESTCODE = 1;
	
	private Button mAddProjectBtn;
	private Button mAddNetworkBtn;
	private Button mAddLampControlBtn;
	
	private TextView mProjectTv;
	private TextView mNetworkTv;
	private TextView mLampControlTv;
	
	private LoginData mLoginData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		View view = View.inflate(this, R.layout.activity_add_device, null);
		mAddProjectBtn = (Button) view.findViewById(R.id.btn_add_device_add_project);
		mAddProjectBtn.setOnClickListener(this);
		mAddNetworkBtn = (Button) view.findViewById(R.id.btn_add_device_add_network);
		mAddNetworkBtn.setOnClickListener(this);
		mAddLampControlBtn = (Button) view.findViewById(R.id.btn_add_device_add_lamp_control);
		mAddLampControlBtn.setOnClickListener(this);
		mProjectTv = (TextView) view.findViewById(R.id.tv_add_device_project_value);
		mNetworkTv = (TextView) view.findViewById(R.id.tv_add_device_network_value);
		mLampControlTv = (TextView) view.findViewById(R.id.tv_add_device_lamp_control_value);
		setNavigationBarContentView(view);		
	}

	private void initData() {
		setTitleText(R.string.add_device);
		mLoginData = ((StreetlampApp)getApplication()).mLoginData;
		getDeviceData();
	}
	
	private void getDeviceData() {
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.DEVICEDATA_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<DeviceData>(new JsonGenericsSerializator())
                      {
                	
					@Override
                      public void onError(Call call, Exception e, int id)
                      {
                    	  e.printStackTrace();
                    	  Log.e(TAG, "DeviceData onError:"+e.getMessage());
                    	  Toast.makeText(AddDeviceActivity.this, R.string.network_is_not_smooth,
									Toast.LENGTH_SHORT).show();
                      }
					@Override
					public void onResponse(DeviceData deviceData, int id) {
						Log.i(TAG, "DeviceData onResponse:"+deviceData.toString());
						 if(deviceData.getStatus().equals(NetManager.SUCCESS)){
							    setDeviceData(deviceData);
							    
						 }else {
							 Toast.makeText(AddDeviceActivity.this, deviceData.getMsg(),
										Toast.LENGTH_SHORT).show();
						 }
					}	
                  });		
	}

	private void setDeviceData(DeviceData deviceData) {
		mProjectTv.setText(deviceData.getData().getTotal_project()+"");
		mNetworkTv.setText(deviceData.getData().getTotal_network()+"");
		mLampControlTv.setText(deviceData.getData().getTotal_lamp()+"");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_device_add_project:
			goAddProject();
			break;
		case R.id.btn_add_device_add_network:
			goAddNetwork();
			break;
		case R.id.btn_add_device_add_lamp_control:
			goAddLampControl();
			break;
		default:
			break;
		}
	}
	
	private void goAddProject() {
		Intent intent = new Intent(this,AddProjectActivity.class);
		this.startActivityForResult(intent, ADD_DEVICE_REQUESTCODE);;
	}

	private void goAddNetwork() {
		Intent intent = new Intent(this,AddNetworkActivity.class);
		this.startActivityForResult(intent, ADD_DEVICE_REQUESTCODE);;
	}
	
	private void goAddLampControl() {
		Intent intent = new Intent(this,AddLampControlActivity.class);
		this.startActivityForResult(intent, ADD_DEVICE_REQUESTCODE);;
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
        case ADD_DEVICE_REQUESTCODE:
            if (resultCode == RESULT_OK) {
            	getDeviceData();
            }
            break;

        default:
            break;
        }
	}
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
	}
}
