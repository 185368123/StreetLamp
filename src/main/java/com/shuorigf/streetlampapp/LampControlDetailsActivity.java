package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LampControlDetailsData;
import com.shuorigf.streetlampapp.data.LampControlDetailsData.Data;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.ResultCodeData;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.ui.RectTime;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class LampControlDetailsActivity extends Activity implements
		OnClickListener {

	private static final String TAG = LampControlDetailsActivity.class
			.getSimpleName();
	private static final int TURN_CALLBACK_ID = 100;
	private static final int DIMMING_CALLBACK_ID = 101;
	private static final int DEL_CALLBACK_ID = 102;
	private static final int EDIT_LAMP_CONTROL_REQUESTCODE = 1;

	private ImageButton mBackBtn;
	private TextView mLampControlNameTv;
	private ImageView mLampControlStateIv;
	private ImageButton mEditIBtn;
	
	private ViewGroup mUpdateHistoryRlyt;
	private ViewGroup mControllerInformationLlyt;
	private ViewGroup mStreetLampInformationLlyt;
	private ViewGroup mBatteryBoardInformationLlyt;
	private ViewGroup mStorageBatteryBoardInformationLlyt;
	private ViewGroup mDailyElectricityInformationLlyt;
	private TextView mLatestUpdateTimeTv;
	private ImageButton mRefreshIBtn;

	private CheckBox mButtonChk;
	private TextView mCloseTv;
	private TextView mOpenTv;
	
	private TextView mAimingTv;
	private ViewGroup mDimmingLlyt;
	private TextView mSeekbarUpTv;
	private SeekBar mControlDimmingSb;

	private TextView mDeviceIDTv;
	private TextView mNetworkIDTv;
	private TextView mWirelessModuleAddressTv;
	private TextView mSectionTv;
	private TextView mAddressTv;
	private TextView mSolarPanelPowerTv;

	private TextView mControlledTemperatureTv;
	private TextView mRatedVoltageTv;
	private TextView mRatedCurrentTv;

	private TextView mStreetLampStateTv;
	private TextView mStreetLampBrightnessTv;
	private TextView mStreetLampPowerTv;
	private RectTime mStreetLampRectTime;

	private TextView mBatteryBoardVoltageTv;
	private TextView mBatteryBoardPowerTv;
	private RectTime mBatteryBoardRectTime;

	private TextView mStorageBatteryBoardVoltageTv;
	private TextView mStorageBatteryBoardDayMinimumVoltageTv;
	private TextView mStorageBatteryBoardDayPeakVoltageTv;
	private TextView mStorageBatteryBoardElectricityTv;
	private TextView mStorageBatteryBoardChargingStatusTv;
	private TextView mStorageBatteryBoardChargingStageTv;
	private TextView mStorageBatteryBoardTemperatureTv;
	private TextView mStorageBatteryBoardTotalOverTimeTv;

	private TextView mRunningDaysTv;
	private TextView mOverTimeTv;

	private TextView mCumulativeGeneratingCapacityTv;
	private TextView mCumulativeElectricityConsumptionTv;

	private Button mDeleteThisLampControlBtn;
	
	private AlertDialog mDialog;
	private Dialog mRefDialog;
	private Dialog mSetDialog;
	private Dialog mDelDialog;
	private Dialog mSetSuccessDialog;
	private Dialog mSetFailDialog;
	private Dialog mDelSuccessDialog;
	private Dialog mDelFailDialog;
	private Dialog mRefSuccessDialog;
	private Dialog mRefFailDialog;

	private String lamp_id;
	private Bundle lampBundle;
	

	private LoginData mLoginData;
	private Data mData;
	
	private int mProgress;
//	private boolean isControl=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lamp_control_details);
		initView();
		initData();
	}

	private void initView() {
		mRefDialog = DialogFactory.creatRequestDialog(this, R.string.refreshing);
		mSetDialog = DialogFactory.creatRequestDialog(this, R.string.setting_up);
		mDelDialog = DialogFactory.creatRequestDialog(this, R.string.deleting);
		mSetSuccessDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_success, R.string.set_up_success);
		mSetFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.set_up_failed);
		mRefSuccessDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_success, R.string.refresh_success);
		mRefFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.refresh_failed);
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
        mBackBtn = (ImageButton) findViewById(R.id.imgbtn_lamp_control_details_back);
        mBackBtn.setOnClickListener(this);
        mLampControlNameTv = (TextView) findViewById(R.id.tv_lamp_control_details_name);
		mLampControlStateIv = (ImageView) findViewById(R.id.iv_lamp_control_details_state);
		mEditIBtn = (ImageButton) findViewById(R.id.imgbtn_lamp_control_details_edit);
		mEditIBtn.setOnClickListener(this);
		mUpdateHistoryRlyt = (ViewGroup) findViewById(R.id.rlyt_lamp_control_details_update_history);
		mUpdateHistoryRlyt.setOnClickListener(this);
		mControllerInformationLlyt= (ViewGroup) findViewById(R.id.llyt_lamp_control_details_controller_information);
		mControllerInformationLlyt.setOnClickListener(this);
		mStreetLampInformationLlyt= (ViewGroup) findViewById(R.id.llyt_lamp_control_details_street_lamp_information);
		mStreetLampInformationLlyt.setOnClickListener(this);
		mBatteryBoardInformationLlyt= (ViewGroup) findViewById(R.id.llyt_lamp_control_details_battery_board_information);
		mBatteryBoardInformationLlyt.setOnClickListener(this);
		mStorageBatteryBoardInformationLlyt= (ViewGroup) findViewById(R.id.llyt_lamp_control_details_storage_battery_board_information);
		mStorageBatteryBoardInformationLlyt.setOnClickListener(this);
		mDailyElectricityInformationLlyt= (ViewGroup) findViewById(R.id.llyt_lamp_control_details_daily_electricity_information);
		mDailyElectricityInformationLlyt.setOnClickListener(this);
		
		mLatestUpdateTimeTv = (TextView) findViewById(R.id.tv_lamp_control_details_latest_update_time_value);
		mRefreshIBtn = (ImageButton) findViewById(R.id.imgbtn_lamp_control_details_refresh);
		mRefreshIBtn.setOnClickListener(this);
		mAimingTv =  (TextView) findViewById(R.id.tv_control_aiming);
		mDimmingLlyt =  (ViewGroup) findViewById(R.id.llyt_dimming);
		mSeekbarUpTv = (TextView) findViewById(R.id.tv_control_seekbar_up);
		mButtonChk = (CheckBox) findViewById(R.id.chk_control_button);
		mCloseTv = (TextView) findViewById(R.id.tv_control_close);
		mOpenTv = (TextView) findViewById(R.id.tv_control_open);
		mButtonChk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					mCloseTv.setTextColor(getResources().getColor(R.color.text_gray));
					mOpenTv.setTextColor(getResources().getColor(R.color.blue));
				}else {
					mCloseTv.setTextColor(getResources().getColor(R.color.blue));
					mOpenTv.setTextColor(getResources().getColor(R.color.text_gray));
				}
				if (buttonView.isPressed()) {
					turnLampControl();
				}
				
//				isControl = true;
			}
		});
		mControlDimmingSb = (SeekBar) findViewById(R.id.sb_control_dimming);
		mControlDimmingSb
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						dimmingLampControl();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						mProgress = seekBar.getProgress();
					}

					@SuppressLint("NewApi") @Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						mSeekbarUpTv.setText(progress + "");
						if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
							Rect rect = seekBar.getThumb().getBounds();
							mSeekbarUpTv.setX(rect.centerX());
						}
						
					}
				});

		mDeviceIDTv = (TextView) findViewById(R.id.tv_lamp_control_details_device_id_value);
		mNetworkIDTv = (TextView) findViewById(R.id.tv_lamp_control_details_network_id_value);
		mWirelessModuleAddressTv = (TextView) findViewById(R.id.tv_lamp_control_details_wireless_module_address_value);
		mSectionTv = (TextView) findViewById(R.id.tv_lamp_control_details_section_value);
		mAddressTv = (TextView) findViewById(R.id.tv_lamp_control_details_address_value);
		mSolarPanelPowerTv = (TextView) findViewById(R.id.tv_lamp_control_details_solar_panel_power_value);

		mControlledTemperatureTv = (TextView) findViewById(R.id.tv_lamp_control_details_controller_temperature_value);
		mRatedVoltageTv = (TextView) findViewById(R.id.tv_lamp_control_details_rated_voltage_value);
		mRatedCurrentTv = (TextView) findViewById(R.id.tv_lamp_control_details_rated_current_value);

		mStreetLampStateTv = (TextView) findViewById(R.id.tv_lamp_control_details_street_lamp_state_value);
		mStreetLampBrightnessTv = (TextView) findViewById(R.id.tv_lamp_control_details_street_lamp_brightness_value);
		mStreetLampPowerTv = (TextView) findViewById(R.id.tv_lamp_control_details_street_lamp_power_value);
		mStreetLampRectTime = (RectTime) findViewById(R.id.recttime_lamp_control_details_street_lamp);
		
		
		mBatteryBoardVoltageTv = (TextView) findViewById(R.id.tv_lamp_control_details_battery_board_voltage_value);
		mBatteryBoardPowerTv = (TextView) findViewById(R.id.tv_lamp_control_details_battery_board_power_value);
		mBatteryBoardRectTime = (RectTime) findViewById(R.id.recttime_lamp_control_details_battery_board);
		
		mStorageBatteryBoardVoltageTv = (TextView) findViewById(R.id.tv_lamp_control_details_storage_battery_board_voltage_value);
		mStorageBatteryBoardDayMinimumVoltageTv = (TextView) findViewById(R.id.tv_lamp_control_details_storage_battery_board_day_minimum_voltage_value);
		mStorageBatteryBoardDayPeakVoltageTv = (TextView) findViewById(R.id.tv_lamp_control_details_storage_battery_board_day_peak_voltage_value);
		mStorageBatteryBoardElectricityTv = (TextView) findViewById(R.id.tv_lamp_control_details_storage_battery_board_electricity_value);
		mStorageBatteryBoardChargingStatusTv = (TextView) findViewById(R.id.tv_lamp_control_details_storage_battery_board_charging_status_value);
		mStorageBatteryBoardChargingStageTv  = (TextView) findViewById(R.id.tv_lamp_control_details_storage_battery_board_charging_stage_value);
		mStorageBatteryBoardTemperatureTv = (TextView) findViewById(R.id.tv_lamp_control_details_storage_battery_board_temperature_value);
		mStorageBatteryBoardTotalOverTimeTv = (TextView) findViewById(R.id.tv_lamp_control_details_storage_battery_board_total_over_time_value);

		mRunningDaysTv = (TextView) findViewById(R.id.tv_lamp_control_details_running_days_value);
		mOverTimeTv = (TextView) findViewById(R.id.tv_lamp_control_details_over_time_value);

		mCumulativeGeneratingCapacityTv = (TextView) findViewById(R.id.tv_lamp_control_details_cumulative_generating_capacity_value);
		mCumulativeElectricityConsumptionTv = (TextView) findViewById(R.id.tv_lamp_control_details_cumulative_electricity_consumption_value);

		mDeleteThisLampControlBtn = (Button) findViewById(R.id.btn_lamp_control_details_delete_this_lamp_control);
		mDeleteThisLampControlBtn.setOnClickListener(this);
	}

	private void initData() {
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		mStreetLampRectTime.setFirstTextAndImage(R.string.night_length, 0);
		mStreetLampRectTime.setSecondTextAndImage(R.string.lighting_time, R.drawable.ic_time);
		mBatteryBoardRectTime.setFirstTextAndImage(R.string.sunshine_time,  R.drawable.ic_sunshine);
		mBatteryBoardRectTime.setSecondTextAndImage(R.string.charging_time, R.drawable.ic_time);
		lampBundle = getIntent().getExtras();
		lamp_id = lampBundle.getString("lamp_id");
		mLampControlNameTv.setText(getString(R.string.lamp_control) + lampBundle.getString("lamp_numble"));
		
		getLampControlDetailsData(false);

	}

	private void getLampControlDetailsData(final boolean isRefresh) {
		if (lamp_id==null) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("lamp_id", lamp_id);
		OkHttpUtils
				.post()
				.tag(this)
				.url(NetManager.LAMP_CONTROL_DETAILS_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<LampControlDetailsData>(
								new JsonGenericsSerializator()) {
							
							@Override
							public void onBefore(Request request, int id) {
								if (isRefresh) {
									if(mRefDialog!=null)
										mRefDialog.show();
								}
		                		
		                	}
		                	
		                      @Override
								public void onAfter(int id) {
		                    	  if (isRefresh) {
		                    		  if(mRefDialog!=null)
		                    			  mRefDialog.dismiss();
		                    	  }
		                    	  
		                      }

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG, "LampControlDetailsData onError:"
										+ e.getMessage());
								Toast.makeText(LampControlDetailsActivity.this, R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(
									LampControlDetailsData lampControlDetailsData,
									int id) {
								Log.i(TAG, "LampControlDetailsData onResponse:"
										+ lampControlDetailsData.toString());
								if (lampControlDetailsData.getStatus().equals(
										NetManager.SUCCESS)) {
									if (isRefresh) {
										 if(mRefSuccessDialog!=null)
											 mRefSuccessDialog.show();
									}
									setLampControlDetailsData(lampControlDetailsData);

								} else {
									if (isRefresh) {
										 if(mRefFailDialog!=null)
											 mRefFailDialog.show();
									}
									Toast.makeText(
											LampControlDetailsActivity.this,
											lampControlDetailsData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});		
	}

	private void setLampControlDetailsData(LampControlDetailsData lampControlDetailsData) {
		mData = lampControlDetailsData.getData();
		if (mData.getIsfaulted()==1) {
			mLampControlStateIv.setImageResource(R.drawable.ic_fault);
		}

		mLatestUpdateTimeTv.setText(mData.getUpdatetime());
//		isControl = false;
		setLampStatus(mData.getStatus()==1);
		mControlDimmingSb.setProgress(mData.getLighteness());
		mSeekbarUpTv.setText(mData.getLighteness()+"");
		mSeekbarUpTv.post(new Runnable() {

			@SuppressLint("NewApi")
			@Override
			public void run() {
				if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
					Rect rect = mControlDimmingSb.getThumb().getBounds();
					mSeekbarUpTv.setX(rect.centerX());
				}
			}
		});
		
		
		mDeviceIDTv.setText(mData.getLamp_no());
		mNetworkIDTv.setText(mData.getNetwork_no());
		mWirelessModuleAddressTv.setText(mData.getAddress());
		mSectionTv.setText(mData.getSection());
        mAddressTv.setText(mData.getLocation());
		mSolarPanelPowerTv.setText(mData.getBoardpower()+"");

		mControlledTemperatureTv.setText(mData.getTemper()+"°C");
		mRatedVoltageTv.setText(mData.getSysvoltage()+"V");
		mRatedCurrentTv.setText(mData.getSyscurrent()+"A");

		mStreetLampPowerTv.setText(String.format("%.2f", mData.getLampvoltage()*mData.getLampcurrent())+"W");
		mStreetLampRectTime.setRectHeight(mData.getNightlength(), mData.getLighttime());
		
		mBatteryBoardVoltageTv.setText(mData.getSolarvoltage()+"V");
		mBatteryBoardPowerTv.setText(String.format("%.2f", mData.getSolarvoltage()*mData.getSolarcurrent())+"W");
		mBatteryBoardRectTime.setRectHeight(mData.getSolartime(), mData.getChargetime());
		
		
		mStorageBatteryBoardVoltageTv.setText(mData.getBattvoltage()+"V");
		mStorageBatteryBoardDayMinimumVoltageTv.setText(mData.getVoltagedaymin()+"V");
		mStorageBatteryBoardDayPeakVoltageTv.setText(mData.getVoltagedaymax()+"V");
		mStorageBatteryBoardElectricityTv.setText(mData.getElectricSOC()+"%");
		mStorageBatteryBoardChargingStatusTv.setText(getChargingStatus(mData.getBattstatus()));
		mStorageBatteryBoardChargingStageTv.setText(getChargingStage(mData.getChargestage()));
		mStorageBatteryBoardTemperatureTv.setText(mData.getBatttemper()+"°C");
		mStorageBatteryBoardTotalOverTimeTv.setText(mData.getOvertimes()+getString(R.string.unit_over_time));

		mRunningDaysTv.setText(mData.getRundays()+getString(R.string.unit_days));
		mOverTimeTv.setText(mData.getFulltimes()+getString(R.string.unit_over_time));

		mCumulativeGeneratingCapacityTv.setText(mData.getTotalgeneration()+"kw/h");
		mCumulativeElectricityConsumptionTv.setText(mData.getTotalconsumption()+"kw/h");
		
	}
	
	private void setLampStatus(boolean value) {
		if (value) {
			mStreetLampBrightnessTv.setText(mData==null?"0":mData.getLighteness()+"");
			mButtonChk.setChecked(true);
			mAimingTv.setVisibility(View.VISIBLE);
			mDimmingLlyt.setVisibility(View.VISIBLE);
			mStreetLampStateTv.setText(R.string.open_light);
		}else {
			mButtonChk.setChecked(false);
			mAimingTv.setVisibility(View.INVISIBLE);
			mDimmingLlyt.setVisibility(View.INVISIBLE);
			mStreetLampBrightnessTv.setText("0");
			mStreetLampStateTv.setText(R.string.close_light);
		}
	}
	
	private int getChargingStatus(int status) {
		int statusId = R.string.free;
		switch (status) {
		case 0:
			statusId = R.string.free;
			break;
		case 1:
			statusId = R.string.discharge;
			break;
		case 2:
			statusId = R.string.charge;
			break;
		case 3:
			statusId = R.string.charging_and_discharging;
			break;

		default:
			break;
		}
		
		return statusId;
	}
	
	private int getChargingStage(int stage) {
		int stageId = R.string.no_charge;
		switch (stage) {
		case 0:
			stageId = R.string.no_charge;
			break;
		case 16:
			stageId = R.string.mppt_charge;
			break;
		case 32:
			stageId = R.string.balance_charge;
			break;
		case 48:
			stageId = R.string.lifting_charge;
			break;
		case 64:
			stageId = R.string.floating_charge;
			break;
		default:
			break;
		}
		
		return stageId;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_lamp_control_details_back:
			finish();
			break;
		case R.id.btn_lamp_control_details_delete_this_lamp_control:
			showDelDialog();
			break;
		case R.id.imgbtn_lamp_control_details_edit:
			goEditLampControl();
			break;
		case R.id.imgbtn_lamp_control_details_refresh:
			getLampControlDetailsData(true);
			break;	
		case R.id.rlyt_lamp_control_details_update_history:
//			goUpdateHistory(UpdateHistoryActivity.HISTORY_DATA_INFORMATION);
			break;
			
		case R.id.llyt_lamp_control_details_controller_information:
			goUpdateHistory(UpdateHistoryActivity.SYSTEM_INFORMATION);
			break;
		case R.id.llyt_lamp_control_details_street_lamp_information:
			goUpdateHistory(UpdateHistoryActivity.STREET_LAMP_INFORMATION);
			break;
		case R.id.llyt_lamp_control_details_battery_board_information:
			goUpdateHistory(UpdateHistoryActivity.BATTERY_BOARD_INFORMATION);
			break;
		case R.id.llyt_lamp_control_details_storage_battery_board_information:
			goUpdateHistory(UpdateHistoryActivity.STORAGE_BATTERY_BOARD_INFORMATION);
			break;
			
		case R.id.llyt_lamp_control_details_daily_electricity_information:
//			goUpdateHistory(UpdateHistoryActivity.DAILY_ELECTRICITY_INFORMATION);
			break;
			
		default:
			break;
		}
	}
	
	
	private void goUpdateHistory(int type) {
		Intent intent = new Intent(this, UpdateHistoryActivity.class);
		intent.putExtra("type", type);
		intent.putExtra("lamp_id", lamp_id);
		startActivity(intent);
	}

	private void goEditLampControl() {
			Intent intent = new Intent(this, EditLampControlActivity.class);
			Bundle bundle = new Bundle(lampBundle);
			bundle.putSerializable("data", mData);
			intent.putExtras(bundle);
			startActivityForResult(intent, EDIT_LAMP_CONTROL_REQUESTCODE);
	}
	
	
	private void showDelDialog() {
		if (mDialog==null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View view = View.inflate(this, R.layout.dialog_delete, null);
			((TextView) view.findViewById(R.id.tv_show_content)).setText(R.string.are_you_sure_you_want_to_delete_this_lamp_control);
			((Button) view.findViewById(R.id.btn_confirm)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					delLampControl();
					if (mDialog!=null) {
						mDialog.dismiss();
					}
				}
			});
			((Button) view.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (mDialog!=null) {
						mDialog.dismiss();
					}
				}
			});
			mDialog = builder.create();
			mDialog.setView(view);
		}
		if (mDialog!=null) {
		    mDialog.show();
		}
	}
	
	private void delLampControl() {
		 if (lamp_id==null) {
	        	return;
			}
    	Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("lamp_ids", lamp_id);
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.LAMP_CONTROL_DEL_URL)
                .params(params)
                .id(DEL_CALLBACK_ID)
                .build()
                .execute(myCallback);
	}
	
	
	private void turnLampControl() {
        if (lamp_id==null) {
        	return;
		}
    	Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("lamp_id", lamp_id);
        params.put("type", mButtonChk.isChecked()?"1":"0");
        params.put("mode", "0");
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.LAMP_CONTROL_TURN_ON_OFF_URL)
                .params(params)
                .id(TURN_CALLBACK_ID)
                .build()
                .execute(myCallback);
	}
	
	
	@SuppressLint("NewApi") private void dimmingLampControl() {
        if (lamp_id==null) {
        	return;
		}
    	Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("lamp_id", lamp_id);
        params.put("type", "1");
        params.put("lightness", mControlDimmingSb.getProgress()+"");
        params.put("mode", "0");
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.LAMP_CONTROL_DIMMING_URL)
                .params(params)
                .id(DIMMING_CALLBACK_ID)
                .build()
                .execute(myCallback);
	}
	
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode) {
	        case EDIT_LAMP_CONTROL_REQUESTCODE:
	            if (resultCode == RESULT_OK) {
	            	getLampControlDetailsData(false);
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
		
		
		
	private Callback<ResultCodeData> myCallback = new GenericsCallback<ResultCodeData>(
				new JsonGenericsSerializator()) {
			@Override
			public void onBefore(Request request, int id) {
				if (id == DEL_CALLBACK_ID) {
					if (mDelDialog != null)
						mDelDialog.show();
				}else {
					if(mSetDialog!=null)
            			mSetDialog.show();
				}
				
			}

			@Override
			public void onAfter(int id) {
				if (id == DEL_CALLBACK_ID) {
					if (mDelDialog != null)
						mDelDialog.dismiss();
				}else {
					if (mSetDialog != null)
						mSetDialog.dismiss();
				}
				
				
			}

			@Override
			public void onError(Call call, Exception e, int id) {
				e.printStackTrace();
				Log.e(TAG, "DIMMING or TURN_ON_OFF or Del LampControl onError:" + e.getMessage());
				Toast.makeText(LampControlDetailsActivity.this,
						R.string.network_is_not_smooth, Toast.LENGTH_SHORT).show();
				if (id == TURN_CALLBACK_ID) {
//					isControl = false;
					 mButtonChk.setChecked(!mButtonChk.isChecked());
				}else if (id == DIMMING_CALLBACK_ID) {
					 mControlDimmingSb.setProgress(mProgress);
					 mSeekbarUpTv.setText(mProgress + "");
				}
			}

			@Override
			public void onResponse(ResultCodeData resultCodeData, int id) {
				if (resultCodeData.getStatus().equals(
						NetManager.SUCCESS)) {
					if (id == DEL_CALLBACK_ID) {
						if (mDelSuccessDialog != null)
							mDelSuccessDialog.show();
					}else {
						 if(mSetSuccessDialog!=null)
							 mSetSuccessDialog.show();
						 if (id == TURN_CALLBACK_ID) {
							 setLampStatus(mButtonChk.isChecked());
						 }
					}
					
				} else {
					Toast.makeText(LampControlDetailsActivity.this,
							resultCodeData.getMsg(), Toast.LENGTH_SHORT).show();
					if (id == DEL_CALLBACK_ID) {
						if (mDelFailDialog != null)
							mDelFailDialog.show();
					}else if (id == TURN_CALLBACK_ID) {
						if (mSetFailDialog != null)
							mSetFailDialog.show();
//						isControl = false;
						 mButtonChk.setChecked(!mButtonChk.isChecked());
					}else if (id == DIMMING_CALLBACK_ID) {
						if (mSetFailDialog != null)
							mSetFailDialog.show();
						 mControlDimmingSb.setProgress(mProgress);
						 mSeekbarUpTv.setText(mProgress + "");
					}
				}
			}
		};

}
