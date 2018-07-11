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
import com.shuorigf.streetlampapp.data.BatterySettingData;
import com.shuorigf.streetlampapp.data.LampControlDetailsData.Data;
import com.shuorigf.streetlampapp.data.LoadSettingData;
import com.shuorigf.streetlampapp.data.NetworkData.Data.Network;
import com.shuorigf.streetlampapp.data.ProjectData.Data.Project;
import com.shuorigf.streetlampapp.data.LampControlDetailsData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.NetworkData;
import com.shuorigf.streetlampapp.data.ProjectData;
import com.shuorigf.streetlampapp.data.ResultCodeData;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.util.locationUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zxing.android.CaptureActivity;

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
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class EditLampControlActivity extends NavigationBarActivity implements
		OnClickListener {
	private static final String TAG = EditLampControlActivity.class
			.getSimpleName();
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

	private CheckBox mLoadSettingChk;
	private CheckBox mStorageBatterySettingChk;

	private ViewStub mLoadSettingVs;
	private View mLoadSettingView;
	private EditText mStreetLampLoadModeEdt;
	private EditText mFirstWorkingHoursEdt;
	private EditText mFirstWorkingPowerEdt;
	private EditText mSecondWorkingHoursEdt;
	private EditText mSecondWorkingPowerEdt;
	private EditText mThirdWorkingHoursEdt;
	private EditText mThirdWorkingPowerEdt;
	private EditText mMorningLightTimeEdt;
	private EditText mMorningLightPowerEdt;
	private EditText mLightControlVoltageEdt;
	private EditText mLightControlDelayTimeEdt;
	private EditText mLedLoadCurrentEdt;
	private CheckBox mIntelligentPowerControlChk;
	private CheckBox mEveryNightLightingFunctionChk;

	private ViewStub mStorageBatterySettingVs;
	private View mStorageBatterySettingView;
	private EditText mStorageBatteryCapacityEdt;
	private EditText mStorageBatteryTypeEdt;
	private EditText mOverpressureVoltageEdt;
	private EditText mChargingLimitVoltageEdt;
	private EditText mBalanceChargeVoltageEdt;
	private EditText mLiftingChargeVoltageEdt;
	private EditText mFloatingChargeVoltageEdt;
	private EditText mLiftingChargeRecoveryVoltageEdt;
	private EditText mOverRecoveryVoltageEdt;
	private EditText mUndervoltageWarningVoltageEdt;
	private EditText mOverVoltageEdt;
	private EditText mBalanceChargeTimeEdt;
	private EditText mLiftingChargeTimeEdt;
	private EditText mBalanceChargeIntervalEdt;
	private EditText mTemperatureCompensationCoefficientEdt;
	private EditText mTemperatureCompensationCoefficientPeakTemperatureEdt;
	private EditText mTemperatureCompensationCoefficientMinimumTemperatureEdt;

	private TextView mGetTv;
	private TextView mAcquisitionMapTv;

	private Button mSaveBtn;

	private LoginData mLoginData;
	private Dialog mDialog;
	private Dialog mSuccessDialog;
	private Dialog mFailDialog;
	private Dialog mWatingDialog;

	private Data mData;
	private String lamp_id;
	private String network_id;
	private String project_id;

	private List<Project> mProjects;
	private OptionsPickerView<Project> mProjectOptionsPV;
	private List<Network> mNetworks;
	private OptionsPickerView<Network> mNetworkOptionsPV;

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
		mProjectOptionsPV
				.setOnoptionsSelectListener(new OnOptionsSelectListener() {

					@Override
					public void onOptionsSelect(int options1, int option2,
							int options3) {
						if (mProjects != null) {
							mBelongsProjectEdt.setText(mProjects.get(options1)
									.getName());
							project_id = mProjects.get(options1).getId();
							getNetworkData(project_id);
						}
					}
				});
		mNetworkOptionsPV = new OptionsPickerView<Network>(this);
		mNetworkOptionsPV
				.setOnoptionsSelectListener(new OnOptionsSelectListener() {

					@Override
					public void onOptionsSelect(int options1, int option2,
							int options3) {
						if (mNetworks != null) {
							mBelongsNetworkEdt.setText(mNetworks.get(options1)
									.getName());
							network_id = mNetworks.get(options1).getId();
						}
					}
				});
		mWatingDialog = DialogFactory.creatRequestDialog(this, 0);
		mDialog = DialogFactory.creatRequestDialog(this, R.string.saving);
		mSuccessDialog = DialogFactory.creatResultDialog(this,
				R.drawable.ic_success, R.string.save_success);
		mFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail,
				R.string.save_failed);
		mSuccessDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();

			}
		});
		View view = View.inflate(this, R.layout.activity_edit_lamp_control,
				null);
		mBelongsProjectEdt = (EditText) view
				.findViewById(R.id.edt_edit_lamp_control_belongs_to_the_project_value);
		mBelongsProjectEdt.setOnClickListener(this);
		mBelongsNetworkEdt = (EditText) view
				.findViewById(R.id.edt_edit_lamp_control_belongs_to_the_network_value);
		mBelongsNetworkEdt.setOnClickListener(this);
		mStreetLampNumberEdt = (EditText) view
				.findViewById(R.id.edt_edit_lamp_control_street_lamp_number_value);
		mSectionEdt = (EditText) view
				.findViewById(R.id.edt_edit_lamp_control_section_value);
		mWirelessModuleIDEdt = (EditText) view
				.findViewById(R.id.edt_edit_lamp_control_wireless_module_id_value);
		mLongitudeEdt = (EditText) view
				.findViewById(R.id.edt_edit_lamp_control_longitude_value);
		mLatitudeEdt = (EditText) view
				.findViewById(R.id.edt_edit_lamp_control_latitude_value);
		mGetTv = (TextView) view.findViewById(R.id.tv_edit_lamp_control_get);
		mGetTv.setOnClickListener(this);
		mAcquisitionMapTv = (TextView) view
				.findViewById(R.id.tv_edit_lamp_control_acquisition_map);
		mAcquisitionMapTv.setOnClickListener(this);
		mSaveBtn = (Button) view.findViewById(R.id.btn_edit_lamp_control_save);
		mSaveBtn.setOnClickListener(this);
		mScanIBtn = (ImageButton) view
				.findViewById(R.id.imgbtn_edit_lamp_control_scan);
		mScanIBtn.setOnClickListener(this);
		mLoadSettingVs = (ViewStub) view
				.findViewById(R.id.vs_edit_lamp_control_load_setting);
		mStorageBatterySettingVs = (ViewStub) view
				.findViewById(R.id.vs_edit_lamp_control_storage_battery_setting);
		mLoadSettingChk = (CheckBox) view
				.findViewById(R.id.chk_edit_lamp_control_load_setting);
		mStorageBatterySettingChk = (CheckBox) view
				.findViewById(R.id.chk_edit_lamp_control_storage_battery_setting);
		mLoadSettingChk
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							if (mLoadSettingView == null) {
								mLoadSettingView = mLoadSettingVs.inflate();
								mStreetLampLoadModeEdt = (EditText) mLoadSettingView
										.findViewById(R.id.edt_load_setting_street_lamp_load_mode_value);
								mFirstWorkingHoursEdt = (EditText)mLoadSettingView
										.findViewById(R.id.edt_load_setting_first_working_hours_value);
								mFirstWorkingPowerEdt = (EditText)mLoadSettingView
										.findViewById(R.id.edt_load_setting_first_working_power_value);
								mSecondWorkingHoursEdt = (EditText)mLoadSettingView
										.findViewById(R.id.edt_load_setting_second_working_hours_value);
								mSecondWorkingPowerEdt = (EditText)mLoadSettingView
										.findViewById(R.id.edt_load_setting_second_working_power_value);
								mThirdWorkingHoursEdt = (EditText)mLoadSettingView
										.findViewById(R.id.edt_load_setting_third_working_hours_value);
								mThirdWorkingPowerEdt = (EditText)mLoadSettingView
										.findViewById(R.id.edt_load_setting_third_working_power_value);
								mMorningLightTimeEdt = (EditText)mLoadSettingView
										.findViewById(R.id.edt_load_setting_morning_light_time_value);
								mMorningLightPowerEdt = (EditText)mLoadSettingView
										.findViewById(R.id.edt_load_setting_morning_light_power_value);
								mLightControlVoltageEdt = (EditText)mLoadSettingView
										.findViewById(R.id.edt_load_setting_light_control_voltage_value);
								mLightControlDelayTimeEdt = (EditText)mLoadSettingView
										.findViewById(R.id.edt_load_setting_light_control_delay_time_value);
								mLedLoadCurrentEdt = (EditText)mLoadSettingView
										.findViewById(R.id.edt_load_setting_led_load_current_value);
								mIntelligentPowerControlChk = (CheckBox)mLoadSettingView
										.findViewById(R.id.chk_load_setting_intelligent_power_control);
								mEveryNightLightingFunctionChk = (CheckBox)mLoadSettingView
										.findViewById(R.id.chk_load_setting_every_night_lighting_function);
								getLoadSettingData();
							} else {
								mLoadSettingView.setVisibility(View.VISIBLE);
							}
						} else {
							if (mLoadSettingView != null)
								mLoadSettingView.setVisibility(View.GONE);
						}
					}
				});

		mStorageBatterySettingChk
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							if (mStorageBatterySettingView == null) {
								mStorageBatterySettingView = mStorageBatterySettingVs
										.inflate();
								mStorageBatteryCapacityEdt = (EditText) mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_storage_battery_capacity_value);
								mStorageBatteryTypeEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_storage_battery_type_value);
								mOverpressureVoltageEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_overpressure_voltage_value);
								mChargingLimitVoltageEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_charging_limit_voltage_value);
								mBalanceChargeVoltageEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_balance_charge_voltage_value);
								mLiftingChargeVoltageEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_lifting_charge_voltage_value);
								mFloatingChargeVoltageEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_floating_charge_voltage_value);
								mLiftingChargeRecoveryVoltageEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_lifting_charge_recovery_voltage_value);
								mOverRecoveryVoltageEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_over_recovery_voltage_value);
								mUndervoltageWarningVoltageEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_undervoltage_warning_voltage_value);
								mOverVoltageEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_over_voltage_value);
								mBalanceChargeTimeEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_balance_charge_time_value);
								mLiftingChargeTimeEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_lifting_charge_time_value);
								mBalanceChargeIntervalEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_balance_charge_interval_value);
								mTemperatureCompensationCoefficientEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_temperature_compensation_coefficient_value);
								mTemperatureCompensationCoefficientPeakTemperatureEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_temperature_compensation_coefficient_peak_temperature_value);
								mTemperatureCompensationCoefficientMinimumTemperatureEdt = (EditText)mStorageBatterySettingView
										.findViewById(R.id.edt_storage_battery_setting_temperature_compensation_coefficient_minimum_temperature_value);
								getBatterySettingData();
							} else {
								mStorageBatterySettingView
										.setVisibility(View.VISIBLE);
							}
						} else {
							if (mStorageBatterySettingView != null)
								mStorageBatterySettingView
										.setVisibility(View.GONE);
						}
					}
				});
		setNavigationBarContentView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		setTitleText(R.string.edit_lamp_control);
		mRightTV.setVisibility(View.VISIBLE);
		mRightTV.setText(R.string.cancel);
		mRightTV.setTextColor(getResources().getColor(R.color.blue));
		mRightTV.setOnClickListener(this);
		mLocClient = new LocationClient(this);
		mLocClient.setLocOption(getClientOption());
		mLocClient.registerLocationListener(myListener);
		mData = (Data) getIntent().getSerializableExtra("data");
		lamp_id = getIntent().getStringExtra("lamp_id");
		
		if (mData!=null) {
			setLampControlDetailsData(mData);
		}else {
			getLampControlDetailsData();
		}
		
	}
	
	
	private void getLampControlDetailsData() {
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
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG, "LampControlDetailsData onError:"
										+ e.getMessage());
								Toast.makeText(EditLampControlActivity.this, R.string.network_is_not_smooth,
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
									setLampControlDetailsData(lampControlDetailsData.getData());

								} else {
									Toast.makeText(
											EditLampControlActivity.this,
											lampControlDetailsData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});		
	}
	
	private void setLampControlDetailsData(Data data) {
		network_id = data.getNetwork_id();
		project_id = data.getProject_id();
		mBelongsNetworkEdt.setText(getIntent().getStringExtra("network_name"));
		mBelongsProjectEdt.setText(data.getProject_name());
		mStreetLampNumberEdt.setText(data.getLamp_no());
		mSectionEdt.setText(data.getSection());
		mWirelessModuleIDEdt.setText(data.getAddress());
		mLongitudeEdt.setText(data.getLongitude() + "");
		mLatitudeEdt.setText(data.getLatitude() + "");
	}

	private LocationClientOption getClientOption() {
		if (mOption == null) {
			mOption = new LocationClientOption();
			mOption.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
			mOption.setOpenGps(true); // 打开gps
			mOption.setIsNeedAddress(true);
		}
		return mOption;
	}
	
	private void getLoadSettingData() {
		if (lamp_id == null) {
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
				.url(NetManager.LAMP_CONTROL_LOAD_SETTING_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<LoadSettingData>(
								new JsonGenericsSerializator()) {
							
							
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
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"LoadSettingData onError:"
												+ e.getMessage());
								Toast.makeText(EditLampControlActivity.this,
										R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(
									LoadSettingData loadSettingData,
									int id) {
								Log.i(TAG, "LoadSettingData onResponse:"
										+ loadSettingData.toString());
								if (loadSettingData.getStatus().equals(
										NetManager.SUCCESS)) {
									setLoadSettingData(loadSettingData);
								} else {
									Toast.makeText(EditLampControlActivity.this,
											loadSettingData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});
	}

	private void setLoadSettingData(LoadSettingData loadSettingData) {
		String workMode = loadSettingData.getData().getWorkmode();
		int res = R.string.lamp_control_load_mode_0;
		if ("0".equals(workMode)) {
			res = R.string.lamp_control_load_mode_0;
		}else if ("1".equals(workMode)){
			res = R.string.lamp_control_load_mode_1;
		}else if ("2".equals(workMode)){
			res = R.string.lamp_control_load_mode_2;
		}else if ("3".equals(workMode)){
			res = R.string.lamp_control_load_mode_3;
		}else if ("4".equals(workMode)){
			res = R.string.lamp_control_load_mode_4;
		}else if ("5".equals(workMode)){
			res = R.string.lamp_control_load_mode_5;
		}else if ("6".equals(workMode)){
			res = R.string.lamp_control_load_mode_6;
		}else if ("7".equals(workMode)){
			res = R.string.lamp_control_load_mode_7;
		}else if ("8".equals(workMode)){
			res = R.string.lamp_control_load_mode_8;
		}else if ("9".equals(workMode)){
			res = R.string.lamp_control_load_mode_9;
		}else if ("10".equals(workMode)){
			res = R.string.lamp_control_load_mode_10;
		}else if ("11".equals(workMode)){
			res = R.string.lamp_control_load_mode_11;
		}else if ("12".equals(workMode)){
			res = R.string.lamp_control_load_mode_12;
		}else if ("13".equals(workMode)){
			res = R.string.lamp_control_load_mode_13;
		}else if ("14".equals(workMode)){
			res = R.string.lamp_control_load_mode_14;
		}else if ("15".equals(workMode)){
			res = R.string.lamp_control_load_mode_15;
		}else if ("16".equals(workMode)){
			res = R.string.lamp_control_load_mode_16;
		}else if ("17".equals(workMode)){
			res = R.string.lamp_control_load_mode_17;
		}
		mStreetLampLoadModeEdt.setText(res);
		mFirstWorkingHoursEdt.setText(loadSettingData.getData().getWorktimefirst());
		mFirstWorkingPowerEdt.setText(loadSettingData.getData().getWorkpowerfirst());
		mSecondWorkingHoursEdt.setText(loadSettingData.getData().getWorktimesencond());
		mSecondWorkingPowerEdt.setText(loadSettingData.getData().getWorkpowersencond());
		mThirdWorkingHoursEdt.setText(loadSettingData.getData().getWorktimethird());
		mThirdWorkingPowerEdt.setText(loadSettingData.getData().getWorkpowerthird());
		mMorningLightTimeEdt.setText(loadSettingData.getData().getWorktimeforth());
		mMorningLightPowerEdt.setText(loadSettingData.getData().getWorkpowerforth());
		mLightControlVoltageEdt.setText(loadSettingData.getData().getVoptically());
		mLightControlDelayTimeEdt.setText(loadSettingData.getData().getDelaytime());
		mLedLoadCurrentEdt.setText(loadSettingData.getData().getLedloadcurrent());
		mIntelligentPowerControlChk.setChecked(loadSettingData.getData().getPowercmd()==0?false:true);
		mEveryNightLightingFunctionChk.setChecked(loadSettingData.getData().getSwitchfeature()==0?false:true);

	}
	
	private void getBatterySettingData() {
		if (lamp_id == null) {
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
				.url(NetManager.LAMP_CONTROL_BATTERY_SETTING_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<BatterySettingData>(
								new JsonGenericsSerializator()) {
							
							
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
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"BatterySettingData onError:"
												+ e.getMessage());
								Toast.makeText(EditLampControlActivity.this,
										R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(
									BatterySettingData batterySettingData,
									int id) {
								Log.i(TAG, "BatterySettingData onResponse:"
										+ batterySettingData.toString());
								if (batterySettingData.getStatus().equals(
										NetManager.SUCCESS)) {
									setBatterySettingData(batterySettingData);
								} else {
									Toast.makeText(EditLampControlActivity.this,
											batterySettingData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});
		
		
	}
	
	private void setBatterySettingData(BatterySettingData batterySettingData) {
		mStorageBatteryCapacityEdt.setText(batterySettingData.getData().getCapacity());
		mStorageBatteryTypeEdt.setText(batterySettingData.getData().getBatterytype().equals("0")?R.string.lead_acid_battery:R.string.lithium_battery);
		mOverpressureVoltageEdt.setText(batterySettingData.getData().getVovervoltage());
		mChargingLimitVoltageEdt.setText(batterySettingData.getData().getVlimitedcharge());
		mBalanceChargeVoltageEdt.setText(batterySettingData.getData().getVbalancecharge());
		mLiftingChargeVoltageEdt.setText(batterySettingData.getData().getVpromotecharge());
		mFloatingChargeVoltageEdt.setText(batterySettingData.getData().getVfloatingcharge());
		mLiftingChargeRecoveryVoltageEdt.setText(batterySettingData.getData().getVpromoterecover());
		mOverRecoveryVoltageEdt.setText(batterySettingData.getData().getVoverdischargerecover());
		mUndervoltageWarningVoltageEdt.setText(batterySettingData.getData().getVundervoltagewarn());
	
		mOverVoltageEdt.setText(batterySettingData.getData().getVoverdischarge());
		mBalanceChargeTimeEdt.setText(batterySettingData.getData().getBalancechargetime());
		mLiftingChargeTimeEdt.setText(batterySettingData.getData().getPromotechargetime());
		
		mBalanceChargeIntervalEdt.setText(batterySettingData.getData().getBalanceinterval());
		mTemperatureCompensationCoefficientEdt.setText(batterySettingData.getData().getTempcompensation());
		mTemperatureCompensationCoefficientPeakTemperatureEdt.setText(batterySettingData.getData().getTempcompmax());
		mTemperatureCompensationCoefficientMinimumTemperatureEdt.setText(batterySettingData.getData().getTempcompmin());
	}
	private void getNetworkData(String project_id) {
		if (project_id == null) {
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
				.execute(
						new GenericsCallback<NetworkData>(
								new JsonGenericsSerializator()) {

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
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"NetworkData onError:" + e.getMessage());
								mNetworks = null;
								mBelongsNetworkEdt.setText(null);
							}

							@Override
							public void onResponse(NetworkData networkData,
									int id) {
								Log.i(TAG, "NetworkData onResponse:"
										+ networkData.toString());
								if (networkData.getStatus().equals(
										NetManager.SUCCESS)) {
									setNetworkData(networkData);

								} else {
									Toast.makeText(
											EditLampControlActivity.this,
											networkData.getMsg(),
											Toast.LENGTH_SHORT).show();
									mNetworks = null;
									mBelongsNetworkEdt.setText(null);
								}
							}
						});
	}

	private void setNetworkData(NetworkData networkData) {
		mNetworks = networkData.getData().getNetworks();
		if (mNetworks != null && mNetworks.size() > 0) {
			mNetworkOptionsPV.setPicker((ArrayList<Network>) mNetworks);
			mNetworkOptionsPV.setCyclic(false);
			mBelongsNetworkEdt.setText(mNetworks.get(0).getName());
			network_id = mNetworks.get(0).getId();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.imgbtn_edit_lamp_control_scan:
			goScanQRCode();
			break;
		case R.id.tv_edit_lamp_control_get:
			if (locationUtils.isOpen(this)) {
				if(mLocClient != null && !mLocClient.isStarted()){
					mLocClient.start();
				}
			}else {
				showOpenGPSDialog();
			}
			break;
		case R.id.tv_edit_lamp_control_acquisition_map:
			goGetPosition();
			break;
		case R.id.btn_edit_lamp_control_save:
			saveLampControl();
			break;
		case R.id.tv_navigation_bar_right:
			finish();
			break;
		case R.id.edt_edit_lamp_control_belongs_to_the_project_value:
			if (mProjects != null && mProjects.size() > 0) {
				mProjectOptionsPV.show();
			} else {
				getProjectData();
			}
			break;
		case R.id.edt_edit_lamp_control_belongs_to_the_network_value:
			if (!TextUtils.isEmpty(mBelongsProjectEdt.getText().toString())) {
				if (mNetworks != null && mNetworks.size() > 0) {
					mNetworkOptionsPV.show();
				} else {
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
				.tag(this)
				.url(NetManager.PROJECT_LIST_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<ProjectData>(
								new JsonGenericsSerializator()) {

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
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"ProjectData onError:" + e.getMessage());
							}

							@Override
							public void onResponse(ProjectData projectData,
									int id) {
								Log.i(TAG, "ProjectData onResponse:"
										+ projectData.toString());
								if (projectData.getStatus().equals(
										NetManager.SUCCESS)) {
									setProjectData(projectData);

								} else {
									Toast.makeText(
											EditLampControlActivity.this,
											projectData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});
	}

	private void setProjectData(ProjectData projectData) {
		mProjects = projectData.getData().getProjects();
		if (mProjects != null && mProjects.size() > 0) {
			mProjectOptionsPV.setPicker((ArrayList<Project>) mProjects);
			mProjectOptionsPV.setCyclic(false);
			mProjectOptionsPV.show();
		}
	}

	private void goGetPosition() {
		Intent intent = new Intent(this, GetPositionActivity.class);
		startActivityForResult(intent, GET_POSITION_REQUESTCODE);
	}

	private void goScanQRCode() {
		Intent intent = new Intent(this, CaptureActivity.class);
		startActivityForResult(intent, GET_QRCODE_REQUESTCODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GET_QRCODE_REQUESTCODE:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					String content = data.getStringExtra(DECODED_CONTENT_KEY);
					mWirelessModuleIDEdt.setText(content);
				}
			}
			break;
		case GET_POSITION_REQUESTCODE:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
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

	private void saveLampControl() {
		// String belongsProject = mBelongsProjectEdt.getText().toString();
		// String belongsNetwork = mBelongsNetworkEdt.getText().toString();
		String streetLampNumber = mStreetLampNumberEdt.getText().toString();
		String section = mSectionEdt.getText().toString();
		String wirelessModuleID = mWirelessModuleIDEdt.getText().toString();
		String longitude = mLongitudeEdt.getText().toString();
		String latitude = mLatitudeEdt.getText().toString();

		if (TextUtils.isEmpty(project_id)) {
			Toast.makeText(this, R.string.network_number_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(network_id)) {
			Toast.makeText(this, R.string.network_name_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(streetLampNumber)) {
			Toast.makeText(this, R.string.section_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(section)) {
			Toast.makeText(this, R.string.sim_number_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(wirelessModuleID)) {
			Toast.makeText(this, R.string.longitude_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(longitude)) {
			Toast.makeText(this, R.string.latitude_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(latitude)) {
			Toast.makeText(this, R.string.latitude_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("lamp_id", lamp_id);
		params.put("project_id", project_id);
		params.put("network_id", network_id);
		params.put("lamp_no", streetLampNumber);
		params.put("section", section);
		params.put("address", wirelessModuleID);
		params.put("longitude", longitude);
		params.put("latitude", latitude);
		
		if (mLoadSettingView != null) {
			String streetLampLoadMode = mStreetLampLoadModeEdt.getText().toString();
			if (!TextUtils.isEmpty(streetLampLoadMode)) {
				params.put("workmode", streetLampLoadMode);
			}
			String firstWorkingHours = mFirstWorkingHoursEdt.getText().toString();
			if (!TextUtils.isEmpty(firstWorkingHours)) {
				params.put("worktimefirst", firstWorkingHours);
			}
			String firstWorkingPower = mFirstWorkingPowerEdt.getText().toString();
			if (!TextUtils.isEmpty(firstWorkingPower)) {
				params.put("workpowerfirst", firstWorkingPower);
			}
			String secondWorkingHours = mSecondWorkingHoursEdt.getText().toString();
			if (!TextUtils.isEmpty(secondWorkingHours)) {
				params.put("worktimesencond", secondWorkingHours);
			}
			String secondWorkingPower = mSecondWorkingPowerEdt.getText().toString();
			if (!TextUtils.isEmpty(secondWorkingPower)) {
				params.put("workpowersencond", secondWorkingPower);
			}
			String thirdWorkingHours = mThirdWorkingHoursEdt.getText().toString();
			if (!TextUtils.isEmpty(thirdWorkingHours)) {
				params.put("worktimethird", thirdWorkingHours);
			}
			String thirdWorkingPower = mThirdWorkingPowerEdt.getText().toString();
			if (!TextUtils.isEmpty(thirdWorkingPower)) {
				params.put("workpowerthird", thirdWorkingPower);
			}
			String morningLightTime = mMorningLightTimeEdt.getText().toString();
			if (!TextUtils.isEmpty(morningLightTime)) {
				params.put("worktimeforth", morningLightTime);
			}
			String morningLightPower = mMorningLightPowerEdt.getText().toString();
			if (!TextUtils.isEmpty(morningLightPower)) {
				params.put("workpowerforth", morningLightPower);
			}
			String lightControlVoltage = mLightControlVoltageEdt.getText().toString();
			if (!TextUtils.isEmpty(lightControlVoltage)) {
				params.put("voptically", lightControlVoltage);
			}
			String lightControlDelayTime = mLightControlDelayTimeEdt.getText().toString();
			if (!TextUtils.isEmpty(lightControlDelayTime)) {
				params.put("delaytime", lightControlDelayTime);
			}
			String ledLoadCurrent = mLedLoadCurrentEdt.getText().toString();
			if (!TextUtils.isEmpty(ledLoadCurrent)) {
				params.put("ledloadcurrent", ledLoadCurrent);
			}
			params.put("powercmd", mIntelligentPowerControlChk.isChecked()==false? "0":"1");
			params.put("switchfeature", mEveryNightLightingFunctionChk.isChecked()==false? "0":"1");
		}
		
		if (mStorageBatterySettingView != null) {
			String storageBatteryCapacity = mStorageBatteryCapacityEdt.getText().toString();
			if (!TextUtils.isEmpty(storageBatteryCapacity)) {
				params.put("capacity", storageBatteryCapacity);
			}
			String storageBatteryType = mStorageBatteryTypeEdt.getText().toString();
			if (!TextUtils.isEmpty(storageBatteryType)) {
				params.put("batterytype", storageBatteryType);
			}
			String overpressureVoltage = mOverpressureVoltageEdt.getText().toString();
			if (!TextUtils.isEmpty(overpressureVoltage)) {
				params.put("vovervoltage", overpressureVoltage);
			}
			String chargingLimitVoltage = mChargingLimitVoltageEdt.getText().toString();
			if (!TextUtils.isEmpty(chargingLimitVoltage)) {
				params.put("vlimitedcharge", chargingLimitVoltage);
			}
			String balanceChargeVoltage = mBalanceChargeVoltageEdt.getText().toString();
			if (!TextUtils.isEmpty(balanceChargeVoltage)) {
				params.put("vbalancecharge", balanceChargeVoltage);
			}
			String liftingChargeVoltage = mLiftingChargeVoltageEdt.getText().toString();
			if (!TextUtils.isEmpty(liftingChargeVoltage)) {
				params.put("vpromotecharge", liftingChargeVoltage);
			}
			String floatingChargeVoltage = mFloatingChargeVoltageEdt.getText().toString();
			if (!TextUtils.isEmpty(floatingChargeVoltage)) {
				params.put("vfloatingcharge", floatingChargeVoltage);
			}
			String liftingChargeRecoveryVoltage = mLiftingChargeRecoveryVoltageEdt.getText().toString();
			if (!TextUtils.isEmpty(liftingChargeRecoveryVoltage)) {
				params.put("vpromoterecover", liftingChargeRecoveryVoltage);
			}
			String overRecoveryVoltage = mOverRecoveryVoltageEdt.getText().toString();
			if (!TextUtils.isEmpty(overRecoveryVoltage)) {
				params.put("voverdischargerecover", overRecoveryVoltage);
			}
			String undervoltageWarningVoltage = mUndervoltageWarningVoltageEdt.getText().toString();
			if (!TextUtils.isEmpty(undervoltageWarningVoltage)) {
				params.put("vundervoltagewarn", undervoltageWarningVoltage);
			}
			String overVoltage = mOverVoltageEdt.getText().toString();
			if (!TextUtils.isEmpty(overVoltage)) {
				params.put("voverdischarge", overVoltage);
			}
			String balanceChargeTime = mBalanceChargeTimeEdt.getText().toString();
			if (!TextUtils.isEmpty(balanceChargeTime)) {
				params.put("balancechargetime", balanceChargeTime);
			}
			String liftingChargeTime = mLiftingChargeTimeEdt.getText().toString();
			if (!TextUtils.isEmpty(liftingChargeTime)) {
				params.put("promotechargetime", liftingChargeTime);
			}
			String balanceChargeInterval = mBalanceChargeIntervalEdt.getText().toString();
			if (!TextUtils.isEmpty(balanceChargeInterval)) {
				params.put("balanceinterval", balanceChargeInterval);
			}
			String temperatureCompensationCoefficient = mTemperatureCompensationCoefficientEdt.getText().toString();
			if (!TextUtils.isEmpty(temperatureCompensationCoefficient)) {
				params.put("tempcompensation", temperatureCompensationCoefficient);
			}
			String temperatureCompensationCoefficientPeakTemperature = mTemperatureCompensationCoefficientPeakTemperatureEdt.getText().toString();
			if (!TextUtils.isEmpty(temperatureCompensationCoefficientPeakTemperature)) {
				params.put("tempcompmax", temperatureCompensationCoefficientPeakTemperature);
			}
			String temperatureCompensationCoefficientMinimumTemperature = mTemperatureCompensationCoefficientMinimumTemperatureEdt.getText().toString();
			if (!TextUtils.isEmpty(temperatureCompensationCoefficientMinimumTemperature)) {
				params.put("tempcompmin", temperatureCompensationCoefficientMinimumTemperature);
			}
		}
		
		OkHttpUtils
				.post()
				.tag(this)
				.url(NetManager.LAMP_CONTROL_ADD_OR_EDIT_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<ResultCodeData>(
								new JsonGenericsSerializator()) {
							@Override
							public void onBefore(Request request, int id) {
								if (mDialog != null)
									mDialog.show();
							}

							@Override
							public void onAfter(int id) {
								if (mDialog != null)
									mDialog.dismiss();
							}

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"Edit Lamp Control onError:"
												+ e.getMessage());
							}

							@Override
							public void onResponse(
									ResultCodeData resultCodeData, int id) {
								Log.i(TAG, "Edit Lamp Control onResponse:"
										+ resultCodeData.toString());
								if (resultCodeData.getStatus().equals(
										NetManager.SUCCESS)) {
									if (mSuccessDialog != null)
										mSuccessDialog.show();
								} else {
									Toast.makeText(
											EditLampControlActivity.this,
											resultCodeData.getMsg(),
											Toast.LENGTH_SHORT).show();
									if (mFailDialog != null)
										mFailDialog.show();
								}
							}
						});

	}

	@Override
	protected void onDestroy() {
		if (mLocClient != null && mLocClient.isStarted()) {
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
			if (mLocClient != null && mLocClient.isStarted()) {
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
