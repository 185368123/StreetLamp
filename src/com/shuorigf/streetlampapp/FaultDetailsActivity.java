package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.FaultDetailsData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.ResultCodeData;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FaultDetailsActivity extends NavigationBarActivity implements OnClickListener{
	private static final String TAG = FaultDetailsActivity.class.getSimpleName();
	private static final int UPDATE_CALLBACK_ID = 100;
	private static final int DEL_CALLBACK_ID = 101;
	
	private TextView mProjectNameTv;
	private TextView mNetworkNumberTv;
	private TextView mStreetLampNumberTv;
	private TextView mPositionInformationTv;
	private TextView mAlarmEventTv;
	private TextView mAlarmTimeTv;
	private TextView mWirelessModuleTimeTv;

	private Button mViewLampControlBtn;
	private Button mMarkedAsAlreadyProcessedBtn;
	
	private LoginData mLoginData;
	
	private String fault_id;
	private int fault_status;
	
	private AlertDialog mMakeAlertDialog;
//	private TextView mContentTv;
	private AlertDialog mDelAlertDialog;
	private Dialog mDelDialog;
	private Dialog mDelSuccessDialog;
	private Dialog mDelFailDialog;
	
	private Dialog mMakeDialog;
	private Dialog mMakeSuccessDialog;
	private Dialog mMakeFailDialog;
	
//	private String mDetail;
	
	private String lamp_id;
	private String lamp_numble;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		mDelDialog = DialogFactory.creatRequestDialog(this, R.string.deleting);
		mDelSuccessDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_success, R.string.delete_success);
		mDelFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.delete_failed);
        mDelSuccessDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent();
			    setResult(RESULT_OK, intent);
				finish();
			}
		});
        
        mMakeDialog = DialogFactory.creatRequestDialog(this, R.string.marking);
		mMakeSuccessDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_success, R.string.marke_success);
		mMakeFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.marke_failed);
        mMakeSuccessDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent();
			    setResult(RESULT_OK, intent);
				finish();
			}
		});
		View view = View.inflate(this, R.layout.activity_fault_details, null);
		mProjectNameTv = (TextView) view
				.findViewById(R.id.tv_fault_details_project_name_value);
		mNetworkNumberTv = (TextView) view
				.findViewById(R.id.tv_fault_details_network_number_value);
		mStreetLampNumberTv = (TextView) view
				.findViewById(R.id.tv_fault_details_street_lamp_number_value);
		mPositionInformationTv = (TextView) view
				.findViewById(R.id.tv_fault_details_position_information_value);
		mAlarmEventTv = (TextView) view
				.findViewById(R.id.tv_fault_details_alarm_event_value);
		mAlarmTimeTv = (TextView) view
				.findViewById(R.id.tv_fault_details_alarm_time_value);
		mWirelessModuleTimeTv = (TextView) view
				.findViewById(R.id.tv_fault_details_wireless_module_time_value);
		mViewLampControlBtn = (Button) view
				.findViewById(R.id.btn_fault_details_view_lamp_control);
		mViewLampControlBtn.setOnClickListener(this);
		mMarkedAsAlreadyProcessedBtn = (Button) view
				.findViewById(R.id.btn_fault_details_marked_as_already_processed);
		mMarkedAsAlreadyProcessedBtn.setOnClickListener(this);
		setNavigationBarContentView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp)getApplication()).mLoginData;
		setTitleText(R.string.fault_details);
		mRightTV.setVisibility(View.VISIBLE);
		mRightTV.setText(R.string.delete);
		mRightTV.setTextColor(getResources().getColor(R.color.red));
		mRightTV.setOnClickListener(this);
		fault_id = getIntent().getStringExtra("fault_id");
		fault_status = getIntent().getIntExtra("fault_status", 0);
		mMarkedAsAlreadyProcessedBtn.setText(fault_status==0?R.string.marked_as_processed:R.string.marked_as_not_processed);
		
		getFaultDetailsData();
	      
	}
	
	
	private void getFaultDetailsData() {
		if (fault_id==null) {
        	return;
		}
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("id", fault_id);
        
        OkHttpUtils
                .post()
                .url(NetManager.FAULT_DETAILS_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<FaultDetailsData>(new JsonGenericsSerializator())
                      {
                	
					@Override
                      public void onError(Call call, Exception e, int id)
                      {
                    	  e.printStackTrace();
                    	  Log.e(TAG, "FaultDetailsData onError:"+e.getMessage());
                      }
					@Override
					public void onResponse(FaultDetailsData faultDetailsData, int id) {
						Log.i(TAG, "FaultDetailsData onResponse:"+faultDetailsData.toString());
						 if(faultDetailsData.getStatus().equals(NetManager.SUCCESS)){
							    setFaultDetailsData(faultDetailsData);
							    
						 }else {
							 Toast.makeText(FaultDetailsActivity.this, faultDetailsData.getMsg(),
										Toast.LENGTH_SHORT).show();
						 }
					}	
                  });		
	}

	private void setFaultDetailsData(FaultDetailsData faultDetailsData) {
		    lamp_id = faultDetailsData.getData().getLampid();
		    lamp_numble = faultDetailsData.getData().getLamp_no();
		    mProjectNameTv.setText(faultDetailsData.getData().getProject_name());
		    mNetworkNumberTv.setText(faultDetailsData.getData().getNetwork_no());
		    mStreetLampNumberTv.setText(faultDetailsData.getData().getLamp_no());
		    mPositionInformationTv.setText(faultDetailsData.getData().getLocation());
		    mAlarmEventTv.setText(faultDetailsData.getData().getAlarm_event());
		    mAlarmTimeTv.setText(faultDetailsData.getData().getAlarm_time());
		    mWirelessModuleTimeTv.setText(faultDetailsData.getData().getNetwork_time());
//		    mDetail = faultDetailsData.getData().getProject_name()+"  "+getString(R.string.number_as)+faultDetailsData.getData().getLamp_no()
//					+"\n"+getString(R.string.abnormal_reason)+faultDetailsData.getData().getAlarm_event();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_navigation_bar_right:
			showDelDialog();
			break;
		case R.id.btn_fault_details_view_lamp_control:
			goLampControlDetails();
			break;
		case R.id.btn_fault_details_marked_as_already_processed:
		    showProcessDialog();
			break;
		default:
			break;
		}
	}
	
	private void goLampControlDetails() {
		if (lamp_id==null) {
			return;
		}
		Intent intent = new Intent(this,LampControlDetailsActivity.class);
		Bundle lamp = new Bundle();
		lamp.putString("lamp_id", lamp_id);
		lamp.putString("lamp_numble", lamp_numble);
		intent.putExtras(lamp);
		startActivity(intent);
	}
	
	private void showProcessDialog() {
		if (mMakeAlertDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View view = View.inflate(this, R.layout.dialog_delete, null);
			((TextView) view.findViewById(R.id.tv_show_content))
					.setText(fault_status==0?R.string.are_you_sure_you_want_to_marked_as_processed:R.string.are_you_sure_you_want_to_marked_as_not_processed);
			((Button) view.findViewById(R.id.btn_confirm))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							processFault();
							if (mMakeAlertDialog != null) {
								mMakeAlertDialog.dismiss();
							}
						}
					});
			((Button) view.findViewById(R.id.btn_cancel))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mMakeAlertDialog != null) {
								mMakeAlertDialog.dismiss();
							}
						}
					});
			mMakeAlertDialog = builder.create();
			mMakeAlertDialog.setView(view);
		}
		if (mMakeAlertDialog != null) {
			mMakeAlertDialog.show();
		}
	}

	private void processFault() {
		if (fault_id==null) {
        	return;
		}
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("ids", fault_id);
        params.put("status", fault_status==0?"1":"0");
        
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.FAULT_UPDATE_URL)
                .params(params)
                .id(UPDATE_CALLBACK_ID)
                .build()
                .execute(myCallback);
		
	}
	
	private void showDelDialog() {
		if (mDelAlertDialog==null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View view = View.inflate(this, R.layout.dialog_delete, null);
			((TextView) view.findViewById(R.id.tv_show_content)).setText(R.string.are_you_sure_you_want_to_delete_this_fault_info);
			((Button) view.findViewById(R.id.btn_confirm)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					delFaultInfo();
					if (mDelAlertDialog!=null) {
						mDelAlertDialog.dismiss();
					}
				}
			});
			((Button) view.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (mDelAlertDialog!=null) {
						mDelAlertDialog.dismiss();
					}
				}
			});
			mDelAlertDialog = builder.create();
			mDelAlertDialog.setView(view);
		}
		if (mDelAlertDialog!=null) {
			mDelAlertDialog.show();
		}
	}
	
	private void delFaultInfo() {
		if (fault_id==null) {
        	return;
		}
    	Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("id", fault_id);
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.FAULT_DEL_URL)
                .params(params)
                .id(DEL_CALLBACK_ID)
                .build()
                .execute(myCallback);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
	}
	
	
	private Callback<ResultCodeData> myCallback = new GenericsCallback<ResultCodeData>(
			new JsonGenericsSerializator()) {
		@Override
		public void onBefore(Request request, int id) {
			if (id == UPDATE_CALLBACK_ID) {
				if (mMakeDialog != null)
					mMakeDialog.show();
			}else if (id == DEL_CALLBACK_ID) {
				if (mDelDialog != null)
					mDelDialog.show();
			}
			
		}

		@Override
		public void onAfter(int id) {
			if (id == UPDATE_CALLBACK_ID) {
				if (mMakeDialog != null)
					mMakeDialog.dismiss();
			}else if (id == DEL_CALLBACK_ID) {
				if (mDelDialog != null)
					mDelDialog.dismiss();
			}
			
			
		}

		@Override
		public void onError(Call call, Exception e, int id) {
			e.printStackTrace();
			Log.e(TAG, "Update or Del Fault onError:" + e.getMessage());
			Toast.makeText(FaultDetailsActivity.this,
					R.string.network_is_not_smooth, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onResponse(ResultCodeData resultCodeData, int id) {
			if (resultCodeData.getStatus().equals(
					NetManager.SUCCESS)) {
				if (id == UPDATE_CALLBACK_ID) {
					if (mMakeSuccessDialog != null)
						mMakeSuccessDialog.show();
				}else if (id == DEL_CALLBACK_ID) {
					if (mDelSuccessDialog != null)
						mDelSuccessDialog.show();
				}
				
			} else {
				Toast.makeText(FaultDetailsActivity.this,
						resultCodeData.getMsg(), Toast.LENGTH_SHORT).show();
				if (id == UPDATE_CALLBACK_ID) {
					if (mMakeFailDialog != null)
						mMakeFailDialog.show();
				}else if (id == DEL_CALLBACK_ID) {
					if (mDelFailDialog != null)
						mDelFailDialog.show();
				}
			}
		}
	};

}
