package com.shuorigf.streetlampapp.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.OptionsPickerView.OnOptionsSelectListener;
import com.github.mikephil.charting.charts.LineChart;
import com.shuorigf.streetlampapp.MainActivity;
import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.SelectLampControlActivity;
import com.shuorigf.streetlampapp.TotalProjectListActivity;
import com.shuorigf.streetlampapp.adapter.LampControlReportAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.GlobalStaticFun;
import com.shuorigf.streetlampapp.data.HomeData;
import com.shuorigf.streetlampapp.data.LampControlReportData;
import com.shuorigf.streetlampapp.data.LineChartData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.ReportData;
import com.shuorigf.streetlampapp.data.ReportData.Data.Report;
import com.shuorigf.streetlampapp.data.ReportData.Data.Report.Series;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.ui.MyMarkerView;
import com.shuorigf.streetlampapp.ui.RingRate;
import com.shuorigf.streetlampapp.util.SharePreferenceUtils;
import com.viewpagerindicator.PageIndicator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ReportFragment extends BaseFragment implements OnClickListener {

	private static final String TAG = ReportFragment.class.getSimpleName();

	private static final String PREF_PROJECT_REPORT_PROJECT_ID = "project_report_project_id";
	private static final String PREF_PROJECT_REPORT_PROJECT_NAME = "project_report_project_name";
	private static final String PREF_PROJECT_LAMP_CONTROL_LIST = "project_report_lamp_control_list";
	private static final int SELECT_PROJECT_REQUESTCODE = 1;
	private static final int SELECT_LAMP_CONTROL_REQUESTCODE = 2;
	
	private static final int PROJECT_ALL_CALLBACK_ID = 100;
	private static final int PROJECT_YEAR_CALLBACK_ID = 101;
	private static final int PROJECT_MONTH_CALLBACK_ID = 102;
	private static final int PROJECT_DATE_CALLBACK_ID = 103;
	
	
	public static final int LAMP_CONTROL_ALL_CALLBACK_ID = 800;
	public static final int LAMP_CONTROL_YEAR_CALLBACK_ID = 801;
	public static final int LAMP_CONTROL_MONTH_CALLBACK_ID = 802;
	
	private static final int LAMP_CONTROL_POWER_ALL_CALLBACK_ID = 202;
	
	private static final int LAMP_CONTROL_CURRENT_ALL_CALLBACK_ID = 302;
	
	private static final int LAMP_CONTROL_TEMPER_ALL_CALLBACK_ID = 402;
	
	private static final int LAMP_CONTROL_CAPACITY_ALL_CALLBACK_ID = 502;
	
	private static final int LAMP_CONTROL_VOLTAGE_ALL_CALLBACK_ID = 602;
	
	private static final int LAMP_CONTROL_DATE_CALLBACK_ID = 701;

	private ImageButton mOpenLeftMenuIBtn;

	private RadioGroup mTitleTabRGrp;
	private ViewFlipper mViewFlipper;

	private LineChart mLineChartUp;
	private RadioGroup mProjectDateTabRGrp;
	private TextView mProjectSelectDateTV;

	private TextView mProjectNameTV;
	private TextView mLightingRateTV;
	private TextView mOnlineRateTv;
	private TextView mFailureRateTV;

	private RingRate mRingRate;

	private TextView mTotalInstalledLightsTV;
	private TextView mTotalNetworkNumberTV;

	private TextView mLocalAirQualityTV;
	private TextView mSunriseTimeTV;
	private TextView mSunsetTimeTV;

	private TextView mSaveStandardCoalTV;
	private TextView mCumulativeReductionCO2TV;
	private TextView mCumulativeReductionSO2TV;

	
	private RadioGroup mLampControlDateTabRGrp;
	private TextView mLampControlSelectDateTV;
	private ImageButton mFilterIBtn;
	private RecyclerView mRecyclerView;
	private LampControlReportAdapter mLampControlReportAdapter;
	private LinearLayoutManager mLinearLayoutManager;
	private ViewPager mViewPager;
	private PageIndicator mIndicator;
	private ArrayList<BaseFragment> mFragmentList;
	private LampControlReportFirstFragment mPowerLampControlReportFragment;
	private LampControlReportFirstFragment mCurrentLampControlReportFragment;
	private LampControlReportFirstFragment mTemperLampControlReportFragment;
	private LampControlReportFirstFragment mCapacityLampControlReportFragment;
	private LampControlReportFirstFragment mVoltageLampControlReportFragment;

	private LoginData mLoginData;
	private String project_id;
	private String project_name;
	private List<LampControlReportData> mLampControlReports;

	private OptionsPickerView<String> mProjectYearOptionsPV;
	private OptionsPickerView<String> mProjectYearMonthOptionsPV;
	private ArrayList<String> mProjectYearList;
	private ArrayList<ArrayList<String>> mProjectYearMonthList;
	
	
	private OptionsPickerView<String> mLampControlYearOptionsPV;
	private OptionsPickerView<String> mLampControlYearMonthOptionsPV;
	private ArrayList<String> mLampControlYearList;
	private ArrayList<ArrayList<String>> mLampControlYearMonthList;

	private Dialog mWatingDialog;

	private boolean isLoading1;
	private boolean isLoaded1;

	// private boolean isLoading2;
	// private boolean isLoaded2;

	private int mProjectType = PROJECT_MONTH_CALLBACK_ID;
	private int mLampControlType = LAMP_CONTROL_MONTH_CALLBACK_ID;

	private String nowTime;

	@Override
	public View initView(Bundle savedInstanceState) {
		mWatingDialog = DialogFactory.creatRequestDialog(mActivity, 0);
		View view = View.inflate(mActivity, R.layout.fragment_report, null);
		mOpenLeftMenuIBtn = (ImageButton) view
				.findViewById(R.id.imgbtn_report_accout);
		mOpenLeftMenuIBtn.setOnClickListener(this);
		mTitleTabRGrp = (RadioGroup) view.findViewById(R.id.rgrp_report_tab);
		mTitleTabRGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbtn_report_project_report:
					mViewFlipper.setDisplayedChild(0);
					break;
				case R.id.rbtn_report_lamp_control_report:
					mViewFlipper.setDisplayedChild(1);
					break;

				default:
					break;
				}
			}
		});
		mViewFlipper = (ViewFlipper) view.findViewById(R.id.vf_report);
		initProjectReportViewFlipper();
		initLampControlReportViewFlipper();
		return view;
	}

	private void initProjectReportViewFlipper() {
		mProjectYearOptionsPV = new OptionsPickerView<String>(mActivity);
		mProjectYearOptionsPV
				.setOnoptionsSelectListener(new OnOptionsSelectListener() {

					@Override
					public void onOptionsSelect(int options1, int option2,
							int options3) {
						if (mProjectYearList != null) {
							String year = mProjectYearList.get(options1);
							mProjectSelectDateTV.setText(year);
							getProjectReportData();
						}

					}
				});

		mProjectYearMonthOptionsPV = new OptionsPickerView<String>(mActivity);
		mProjectYearMonthOptionsPV
				.setOnoptionsSelectListener(new OnOptionsSelectListener() {

					@Override
					public void onOptionsSelect(int options1, int option2,
							int options3) {
						if (mProjectYearList != null && mProjectYearMonthList != null) {
							String year_month = mProjectYearList.get(options1) + "-"
									+ mProjectYearMonthList.get(options1).get(option2);
							mProjectSelectDateTV.setText(year_month);
							getProjectReportData();
						}

					}
				});
		View view = View.inflate(mActivity, R.layout.fragment_project_report,
				null);
		mProjectDateTabRGrp = (RadioGroup) view
				.findViewById(R.id.rgrp_project_report_date);
		mProjectDateTabRGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbtn_project_report_year:
					mProjectSelectDateTV.setVisibility(View.VISIBLE);
					mProjectSelectDateTV.setText(R.string.select_year);
					mProjectType = PROJECT_YEAR_CALLBACK_ID;
					break;
				case R.id.rbtn_project_report_month:
					mProjectSelectDateTV.setVisibility(View.VISIBLE);
					mProjectSelectDateTV.setText(R.string.select_month);
					mProjectType = PROJECT_MONTH_CALLBACK_ID;
					break;
				case R.id.rbtn_project_report_all:
					mProjectSelectDateTV.setVisibility(View.GONE);
					mProjectType = PROJECT_ALL_CALLBACK_ID;
					getProjectReportData();
					break;
				default:
					break;
				}
			}
		});
		mProjectSelectDateTV = (TextView) view
				.findViewById(R.id.tv_project_report_select_date);
		mProjectSelectDateTV.setOnClickListener(this);
		mRingRate = (RingRate) view
				.findViewById(R.id.ringrate_project_report_rate);
		mProjectNameTV = (TextView) view
				.findViewById(R.id.tv_project_report_project_name);
		mProjectNameTV.setOnClickListener(this);
		mOnlineRateTv = (TextView) view
				.findViewById(R.id.tv_project_report_online_rate_value);
		mFailureRateTV = (TextView) view
				.findViewById(R.id.tv_project_report_failure_rate_value);
		mLightingRateTV = (TextView) view
				.findViewById(R.id.tv_project_report_lighting_rate_value);

		mTotalInstalledLightsTV = (TextView) view
				.findViewById(R.id.tv_project_report_total_installed_lights_value);
		mTotalNetworkNumberTV = (TextView) view
				.findViewById(R.id.tv_project_report_total_network_number_value);

		mLocalAirQualityTV = (TextView) view
				.findViewById(R.id.tv_project_report_local_air_quality_value);
		mSunriseTimeTV = (TextView) view
				.findViewById(R.id.tv_project_report_sunrise_time_value);
		mSunsetTimeTV = (TextView) view
				.findViewById(R.id.tv_project_report_sunset_time_value);

		mSaveStandardCoalTV = (TextView) view
				.findViewById(R.id.tv_project_report_save_standard_coal_value);
		mCumulativeReductionCO2TV = (TextView) view
				.findViewById(R.id.tv_project_report_cumulative_reduction_co2_value);
		mCumulativeReductionSO2TV = (TextView) view
				.findViewById(R.id.tv_project_report_cumulative_reduction_so2_value);

		mLineChartUp = (LineChart) view
				.findViewById(R.id.line_chart_project_report);
		MyMarkerView mv = new MyMarkerView(mActivity,
				R.layout.custom_marker_view);
		GlobalStaticFun.InitLineChart(mLineChartUp, mv);
		mViewFlipper.addView(view);

	}

	private void initLampControlReportViewFlipper() {
		mLampControlYearOptionsPV = new OptionsPickerView<String>(mActivity);
		mLampControlYearOptionsPV
				.setOnoptionsSelectListener(new OnOptionsSelectListener() {

					@Override
					public void onOptionsSelect(int options1, int option2,
							int options3) {
						if (mLampControlYearList != null) {
							String year = mLampControlYearList.get(options1);
							mLampControlSelectDateTV.setText(year);
							getLampControlReportDatas();
						}

					}
				});

		mLampControlYearMonthOptionsPV = new OptionsPickerView<String>(mActivity);
		mLampControlYearMonthOptionsPV
				.setOnoptionsSelectListener(new OnOptionsSelectListener() {

					@Override
					public void onOptionsSelect(int options1, int option2,
							int options3) {
						if (mLampControlYearList != null && mLampControlYearMonthList != null) {
							String year_month = mLampControlYearList.get(options1) + "-"
									+ mLampControlYearMonthList.get(options1).get(option2);
							mLampControlSelectDateTV.setText(year_month);
							getLampControlReportDatas();
						}

					}
				});
		View view = View.inflate(mActivity,
				R.layout.fragment_lamp_control_report, null);
		mLampControlDateTabRGrp = (RadioGroup) view
				.findViewById(R.id.rgrp_lamp_control_report_date);
		mLampControlDateTabRGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbtn_lamp_control_report_year:
					mLampControlSelectDateTV.setVisibility(View.VISIBLE);
					mLampControlSelectDateTV.setText(R.string.select_year);
					mLampControlType = LAMP_CONTROL_YEAR_CALLBACK_ID;
					break;
				case R.id.rbtn_lamp_control_report_month:
					mLampControlSelectDateTV.setVisibility(View.VISIBLE);
					mLampControlSelectDateTV.setText(R.string.select_month);
					mLampControlType = LAMP_CONTROL_MONTH_CALLBACK_ID;
					break;
				case R.id.rbtn_lamp_control_report_all:
					mLampControlSelectDateTV.setVisibility(View.GONE);
					mLampControlType = LAMP_CONTROL_ALL_CALLBACK_ID;
					getLampControlReportDatas();
					break;
				default:
					break;
				}
			}
		});
		mLampControlSelectDateTV = (TextView) view
				.findViewById(R.id.tv_lamp_control_report_select_date);
		mLampControlSelectDateTV.setOnClickListener(this);
		mFilterIBtn = (ImageButton) view
				.findViewById(R.id.imgbtn_lamp_control_report_filter);
		mFilterIBtn.setOnClickListener(this);

		mRecyclerView = (RecyclerView) view
				.findViewById(R.id.rv_lamp_control_report);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_lamp_control_report);
		mIndicator = (PageIndicator) view
				.findViewById(R.id.indicator_lamp_control_report);
		mViewFlipper.addView(view);
	}

	public void initData() {
		mLoginData = ((StreetlampApp) mActivity.getApplication()).mLoginData;
		project_id = SharePreferenceUtils.getString(mActivity,
				PREF_PROJECT_REPORT_PROJECT_ID, null);
		project_name = SharePreferenceUtils.getString(mActivity,
				PREF_PROJECT_REPORT_PROJECT_NAME, null);
		if (project_name != null) {
			mProjectNameTV.setText(project_name);
		}

		mLinearLayoutManager = new LinearLayoutManager(mActivity);
		mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerView.setLayoutManager(mLinearLayoutManager);
		mLampControlReportAdapter = new LampControlReportAdapter(mActivity);
		mRecyclerView.setAdapter(mLampControlReportAdapter);
		mLampControlReports = SharePreferenceUtils.getDataList(mActivity,
				PREF_PROJECT_LAMP_CONTROL_LIST, null);
		mLampControlReportAdapter.changeData(mLampControlReports);
		mFragmentList = new ArrayList<BaseFragment>();
		mPowerLampControlReportFragment = LampControlReportFirstFragment
				.newInstance(LampControlReportFirstFragment.POWER);
		mCurrentLampControlReportFragment = LampControlReportFirstFragment
				.newInstance(LampControlReportFirstFragment.CURRENT);
		mTemperLampControlReportFragment = LampControlReportFirstFragment
				.newInstance(LampControlReportFirstFragment.TEMPER);
		mCapacityLampControlReportFragment = LampControlReportFirstFragment
				.newInstance(LampControlReportFirstFragment.CAPACITY);
		mVoltageLampControlReportFragment = LampControlReportFirstFragment
				.newInstance(LampControlReportFirstFragment.VOLTAGE);
		mFragmentList.add(mPowerLampControlReportFragment);
		mFragmentList.add(mCurrentLampControlReportFragment);
		mFragmentList.add(mTemperLampControlReportFragment);
		mFragmentList.add(mCapacityLampControlReportFragment);
		mFragmentList.add(mVoltageLampControlReportFragment);
		mViewPager.setOffscreenPageLimit(mFragmentList.size());
		mViewPager.setAdapter(new ContentAdapter(getChildFragmentManager()));
		mIndicator.setViewPager(mViewPager);
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int posision) {
				getLampControlReportDatas();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		getHomeData();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
		nowTime = format.format(new Date());
		mProjectSelectDateTV.setText(nowTime);
		mLampControlSelectDateTV.setText(nowTime);
		getProjectReportData();
		getLampControlReportDatas();
//		new TimeThread().start();
	}
	

	private void getLampControlReportDatas(){
		OkHttpUtils.getInstance().cancelTag("lamp_control_report_data");
		switch (mViewPager.getCurrentItem()) {
		case 0:
			getLampControlReportData(LampControlReportFirstFragment.POWER);
			break;
		case 1:
			getLampControlReportData(LampControlReportFirstFragment.CURRENT);
			break;
		case 2:
			getLampControlReportData(LampControlReportFirstFragment.TEMPER);
			break;
		case 3:
			getLampControlReportData(LampControlReportFirstFragment.CAPACITY);
			break;
		case 4:
			getLampControlReportData(LampControlReportFirstFragment.VOLTAGE);
			break;
		default:
			break;
		}
		
		
		
	}
	

//	private class TimeThread extends Thread {
//
//		@Override
//		public void run() {
//			super.run();
//			getCurrentTime();
//		}
//
//	}

//	private void getCurrentTime() {
//		try {
//			URL url = new URL("http://www.bjtime.cn");
//			URLConnection uc = url.openConnection();
//			uc.connect();
//			long id = uc.getDate();
//			Date date = new Date(id);
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
//			nowTime = format.format(date);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		mHandler.sendEmptyMessage(0);
//	
//	}

//	Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if (TextUtils.isEmpty(nowTime)) {
//				SimpleDateFormat format = new SimpleDateFormat("yyyy-M");
//				nowTime = format.format(new Date());
//			}
//			mSelectDateTV.setText(nowTime);
//			getSubReportData(nowTime,SUB_NO_DIALOG_CALLBACK_ID);
//		}
//	};

	private void getHomeData() {
		if (project_id == null) {
			return;
		}
		if (isLoading1) {
			return;
		}
		isLoading1 = true;
		OkHttpUtils.getInstance().cancelTag("project_home_data");
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("project_id", project_id);
		OkHttpUtils
				.post()
				.tag("project_home_data")
				.url(NetManager.HOMEDATA_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<HomeData>(
								new JsonGenericsSerializator()) {

							@Override
							public void onAfter(int id) {
								super.onAfter(id);
								isLoading1 = false;
							}

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG, "HomeData onError:" + e.getMessage());
								if (e.toString().contains("closed")) {
									return;
								}
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
									isLoaded1 = true;
									setHomeDate(homeData);

								} else {
									Toast.makeText(mActivity,
											homeData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});

	}

	private void setHomeDate(HomeData homeData) {

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
		mTotalInstalledLightsTV
				.setText(homeData.getData().getTotal_lamp() + "");
		mTotalNetworkNumberTV.setText(homeData.getData().getTotal_network()
				+ "");
		mSaveStandardCoalTV.setText(homeData.getData().getCoal_saving() + "");
		mCumulativeReductionCO2TV.setText(homeData.getData().getCo2_emission()
				+ "");
		mCumulativeReductionSO2TV.setText(homeData.getData().getSo2_emission()
				+ "");

		mLocalAirQualityTV.setText("优");
		mSunriseTimeTV.setText("6时13分");
		mSunsetTimeTV.setText("18时22分");
	}
	
	
	private void getLampControlReportData(String type) {
		if (mLampControlReports == null || mLampControlReports.size() <= 0) {
			return;
		}
		

		boolean isFirst = true;
		StringBuffer sb = new StringBuffer();
		for (LampControlReportData lampControlReportData : mLampControlReports) {
			if (isFirst) {
				sb.append(lampControlReportData.getId());
				isFirst = false;
			} else {
				sb.append("," + lampControlReportData.getId());
			}
		}
		int id = 0;
		if (LampControlReportFirstFragment.POWER.equals(type)) {
			id = LAMP_CONTROL_POWER_ALL_CALLBACK_ID;
		}else if (LampControlReportFirstFragment.CURRENT.equals(type)) {
			id = LAMP_CONTROL_CURRENT_ALL_CALLBACK_ID;
		}else if (LampControlReportFirstFragment.TEMPER.equals(type)) {
			id = LAMP_CONTROL_TEMPER_ALL_CALLBACK_ID;
		}else if (LampControlReportFirstFragment.CAPACITY.equals(type)) {
			id = LAMP_CONTROL_CAPACITY_ALL_CALLBACK_ID;
		}else if (LampControlReportFirstFragment.VOLTAGE.equals(type)) {
			id = LAMP_CONTROL_VOLTAGE_ALL_CALLBACK_ID;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("type", type);
		params.put("show_type", "lamp");
		params.put("item_ids", sb.toString());
		if (mLampControlType != LAMP_CONTROL_ALL_CALLBACK_ID) {
			String date = mLampControlSelectDateTV.getText().toString();
			if (TextUtils.isEmpty(date)) {
				return;
			}
			if (mLampControlType == LAMP_CONTROL_YEAR_CALLBACK_ID) {
				params.put("date_type", "year");
			} else if (mLampControlType == LAMP_CONTROL_MONTH_CALLBACK_ID) {
				params.put("date_type", "month");
			}
			params.put("date", date);
		}
		OkHttpUtils
				.post()
				.tag("lamp_control_report_data")
				.url(NetManager.REPORT_URL)
				.params(params)
				.id(id)
				.build()
				.execute(myCallback);
	}

	private void getProjectReportData() {
		if (project_id == null) {
			return;
		}
		// if (isLoading2) {
		// return;
		// }
		// isLoading2 = true;
		OkHttpUtils.getInstance().cancelTag("project_report_data");
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("type", "power");
		params.put("show_type", "project");
		params.put("item_ids", project_id);
		if (mProjectType != PROJECT_ALL_CALLBACK_ID) {
			String date = mProjectSelectDateTV.getText().toString();
			if (TextUtils.isEmpty(date)) {
				return;
			}
			if (mProjectType == PROJECT_YEAR_CALLBACK_ID) {
				params.put("date_type", "year");
			} else if (mProjectType == PROJECT_MONTH_CALLBACK_ID) {
				params.put("date_type", "month");
			}
			params.put("date", date);
		}
		OkHttpUtils.post().tag("project_report_data").url(NetManager.REPORT_URL).params(params)
				.id(mProjectType).build().execute(myCallback);
	}

	private void setProjectReportData(ReportData reportData) {
		Report report1 = reportData.getDate().getReport1();
		Report report2 = reportData.getDate().getReport2();
		if (report1 != null && report2 != null) {
			List<String> xVal1 = report1.getCategories();
			List<Series> series1 = report1.getSeries();
			List<Series> series2 = report2.getSeries();
			List<Float> yVal1 = null;
			List<Float> yVal2 = null;
			if (series1 != null && series1.size() > 0) {
				yVal1 = series1.get(0).getData();// line 1
			}

			if (series2 != null && series2.size() > 0) {
				yVal2 = series2.get(0).getData();// line 1
			}

			if (xVal1 != null && yVal1 != null && yVal2 != null) {

				float max1 = Collections.max(yVal1);
				float max2 = Collections.max(yVal2);
				mLineChartUp.getAxisLeft().setAxisMaxValue(
						Math.max(max1, max2) * 1.2f);
				if (mProjectType == PROJECT_ALL_CALLBACK_ID) {
					mLineChartUp.getXAxis().setLabelsToSkip(xVal1.size() / 6);
				}else if (mProjectType == PROJECT_YEAR_CALLBACK_ID) {
					mLineChartUp.getXAxis().setLabelsToSkip(3);
				} else if (mProjectType == PROJECT_MONTH_CALLBACK_ID ){
					mLineChartUp.getXAxis().setLabelsToSkip(12);
				}
				LineChartData lineChartData = new LineChartData(xVal1);
				lineChartData.AddLine(yVal1, 0xff12b7f5,
						LineChartData.SOLID_LINE);
				lineChartData.AddLine(yVal2, 0xfffb6363,
						LineChartData.SOLID_LINE);
				GlobalStaticFun.AddLineData(mLineChartUp, lineChartData);
			}

		}

	}
	

	public class ContentAdapter extends FragmentPagerAdapter {

		public ContentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_report_accout:
			openLeftMenu();
			break;
		case R.id.tv_project_report_project_name:
			goSelectProject();
			break;
		case R.id.imgbtn_lamp_control_report_filter:
			goSelectLampControl();
			break;

		case R.id.tv_project_report_select_date:
			if (mProjectYearList != null && mProjectYearList.size() > 0) {
				if (mProjectType==PROJECT_YEAR_CALLBACK_ID) {
					mProjectYearOptionsPV.show();
				} else if (mProjectType==PROJECT_MONTH_CALLBACK_ID){
					mProjectYearMonthOptionsPV.show();
				}
			} else {
				getProjectDate();
			}
			break;
			
		case R.id.tv_lamp_control_report_select_date:
			if (mLampControlYearList != null && mLampControlYearList.size() > 0) {
				if (mLampControlType==LAMP_CONTROL_YEAR_CALLBACK_ID) {
					mLampControlYearOptionsPV.show();
				} else if (mLampControlType==LAMP_CONTROL_MONTH_CALLBACK_ID){
					mLampControlYearMonthOptionsPV.show();
				}
			} else {
				getLampControlDate();
			}
			break;

		default:
			break;
		}
	}

	private void getProjectDate() {
		if (project_id == null) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("type", LampControlReportFirstFragment.POWER);
		params.put("show_type", "project");
		params.put("item_ids", project_id);
		OkHttpUtils.post().tag(this).url(NetManager.REPORT_URL).params(params)
				.id(PROJECT_DATE_CALLBACK_ID).build().execute(myCallback);
	}

	private void setProjectDate(ReportData reportData) {
		Report report1 = reportData.getDate().getReport1();
		if (report1 != null) {
			mProjectYearList = (ArrayList<String>) report1.getCategories();

			if (mProjectYearList != null && mProjectYearList.size() > 0) {
				mProjectYearOptionsPV.setPicker(mProjectYearList);
				mProjectYearOptionsPV.setCyclic(false);
				mProjectYearMonthList = new ArrayList<ArrayList<String>>();
				for (int i = 0; i < mProjectYearList.size(); i++) {
					ArrayList<String> monthList = new ArrayList<String>();
					for (int j = 1; j < 13; j++) {
						monthList.add(j + "");
					}
					mProjectYearMonthList.add(monthList);
				}
				mProjectYearMonthOptionsPV.setPicker(mProjectYearList, mProjectYearMonthList, true);
				mProjectYearMonthOptionsPV.setCyclic(false);
				if (mProjectType==PROJECT_YEAR_CALLBACK_ID) {
					mProjectYearOptionsPV.show();
				} else if (mProjectType==PROJECT_MONTH_CALLBACK_ID){
					mProjectYearMonthOptionsPV.show();
				}
			}

		}
	}
	
	private void getLampControlDate() {
	if (mLampControlReports == null || mLampControlReports.size() <= 0) {
		return;
	}

	boolean isFirst = true;
	StringBuffer sb = new StringBuffer();
	for (LampControlReportData lampControlReportData : mLampControlReports) {
		if (isFirst) {
			sb.append(lampControlReportData.getId());
			isFirst = false;
		} else {
			sb.append("," + lampControlReportData.getId());
		}
	}
	Map<String, String> params = new HashMap<String, String>();
	params.put("username", mLoginData.getUsername());
	params.put("client_key", mLoginData.getClient_key());
	params.put("token", mLoginData.getData().getToken());
	params.put("type", LampControlReportFirstFragment.POWER);
	params.put("show_type", "lamp");
	params.put("item_ids", sb.toString());
	// params.put("date_type", "year");
	// params.put("date", "2016");
	OkHttpUtils
			.post()
			.tag(this)
			.url(NetManager.REPORT_URL)
			.params(params)
			.id(LAMP_CONTROL_DATE_CALLBACK_ID)
			.build()
			.execute(myCallback);
}

private void setLampControlDate(ReportData reportData) {
	Report report1 = reportData.getDate().getReport1();
	if (report1!=null) {
		mLampControlYearList = (ArrayList<String>)report1.getCategories();
		if (mLampControlYearList!=null&&mLampControlYearList.size()>0) {
			mLampControlYearOptionsPV.setPicker(mLampControlYearList);
			mLampControlYearOptionsPV.setCyclic(false);
			mLampControlYearMonthList = new ArrayList<ArrayList<String>>();
			for (int i = 0; i < mLampControlYearList.size(); i++) {
				 ArrayList<String> monthList=new ArrayList<String>();
				 for (int j = 1; j < 13; j++) {
					 monthList.add(j+"");
				}
				 mLampControlYearMonthList.add(monthList);
			}
			mLampControlYearMonthOptionsPV.setPicker(mLampControlYearList, mLampControlYearMonthList, true);
			mLampControlYearMonthOptionsPV.setCyclic(false);
			if (mLampControlType==LAMP_CONTROL_YEAR_CALLBACK_ID) {
				mLampControlYearOptionsPV.show();
			} else if (mLampControlType==LAMP_CONTROL_MONTH_CALLBACK_ID){
				mLampControlYearMonthOptionsPV.show();
			}
	}
	
}
}

	private void goSelectLampControl() {
		if (project_id == null) {
			Toast.makeText(mActivity, R.string.please_select_project,
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(mActivity, SelectLampControlActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("project_id", project_id);
		// bundle.putBoolean("isFilter", true);
		intent.putExtras(bundle);
		this.startActivityForResult(intent, SELECT_LAMP_CONTROL_REQUESTCODE);
	}

	private void goSelectProject() {
		Intent intent = new Intent(mActivity, TotalProjectListActivity.class);
		intent.putExtra("type", TotalProjectListActivity.SELECT_PROJECT);
		this.startActivityForResult(intent, SELECT_PROJECT_REQUESTCODE);
	}

	private void openLeftMenu() {
		MainActivity mainActivity = (MainActivity) mActivity;
		// mainActivity.getSlidingMenu().toggle();
		mainActivity.toggle();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SELECT_PROJECT_REQUESTCODE:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					project_id = data.getStringExtra("project_id");
					project_name = data.getStringExtra("project_name");
					// mLampControlReports = (List<LampControlReportData>) data
					// .getSerializableExtra("list");
					// mLampControlReportAdapter.changeData(mLampControlReports);
					SharePreferenceUtils.putString(mActivity,
							PREF_PROJECT_REPORT_PROJECT_ID, project_id);
					SharePreferenceUtils.putString(mActivity,
							PREF_PROJECT_REPORT_PROJECT_NAME, project_name);
					// SharePreferenceUtils
					// .setDataList(mActivity,
					// PREF_PROJECT_LAMP_CONTROL_LIST,
					// mLampControlReports);
					mProjectNameTV.setText(project_name);
					mProjectYearList = null;
					isLoaded1 = false;
					// isLoaded2 = false;
					mProjectDateTabRGrp.check(R.id.rbtn_project_report_month);
					getHomeData();
					mProjectSelectDateTV.setText(nowTime);
					getProjectReportData();
//					if (nowTime == null) {
//						new TimeThread().start();
//					} else {
//						mSelectDateTV.setText(nowTime);
//						getSubReportData(nowTime,SUB_NO_DIALOG_CALLBACK_ID);
//					}

					// mPowerLampControlReportFragment.changeData(mLampControlReports);
					// mCurrentLampControlReportFragment.changeData(mLampControlReports);
					// mTemperLampControlReportFragment.changeData(mLampControlReports);
					// mCapacityLampControlReportFragment.changeData(mLampControlReports);
					// mVoltageLampControlReportFragment.changeData(mLampControlReports);
				}
			}
			break;

		case SELECT_LAMP_CONTROL_REQUESTCODE:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					mLampControlReports = (List<LampControlReportData>) data
							.getSerializableExtra("list");
					mLampControlReportAdapter.changeData(mLampControlReports);
					SharePreferenceUtils
							.setDataList(mActivity,
									PREF_PROJECT_LAMP_CONTROL_LIST,
									mLampControlReports);
					mLampControlYearList = null;
					mLampControlDateTabRGrp.check(R.id.rbtn_lamp_control_report_month);
					mLampControlSelectDateTV.setText(nowTime);
					getLampControlReportDatas();
				}
			}
			break;

		default:
			break;
		}
	}

	public void onVisible() {
		if (isPrepared && !isLoaded1) {
			getHomeData();
		}
		// if (isPrepared && !isLoaded2) {
		// getReportData();
		// }
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag("project_home_data");
		OkHttpUtils.getInstance().cancelTag("project_report_data");
		OkHttpUtils.getInstance().cancelTag("lamp_control_report_data");
		OkHttpUtils.getInstance().cancelTag(this);
	}

	private Callback<ReportData> myCallback = new GenericsCallback<ReportData>(
			new JsonGenericsSerializator()) {

		@Override
		public void onBefore(Request request, int id) {
			if (id==PROJECT_DATE_CALLBACK_ID||id==LAMP_CONTROL_DATE_CALLBACK_ID) {
				if (mWatingDialog != null)
					mWatingDialog.show();
			}
			
		}

		@Override
		public void onAfter(int id) {
			if (id==PROJECT_DATE_CALLBACK_ID||id==LAMP_CONTROL_DATE_CALLBACK_ID) {
				if (mWatingDialog != null)
					mWatingDialog.dismiss();
			}
			
		}

		@Override
		public void onError(Call call, Exception e, int id) {
			Log.e(TAG, "ReportData onError:" + e.getMessage());
			if (e.toString().contains("closed")||e.toString().contains("Canceled")) {
				return;
			}
			Toast.makeText(mActivity, R.string.network_is_not_smooth,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onResponse(ReportData reportData, int id) {
			Log.i(TAG, "ReportData onResponse:" + reportData.toString());
			if (reportData.getStatus().equals(NetManager.SUCCESS)) {
				switch (id) {
				case PROJECT_ALL_CALLBACK_ID:
				case PROJECT_YEAR_CALLBACK_ID:
				case PROJECT_MONTH_CALLBACK_ID:
					setProjectReportData(reportData);
					break;
				case PROJECT_DATE_CALLBACK_ID:
					setProjectDate(reportData);
					break;
					
				case LAMP_CONTROL_POWER_ALL_CALLBACK_ID:
					if (mPowerLampControlReportFragment!=null) {
						mPowerLampControlReportFragment.setLampControlReportData(reportData, mLampControlReports,mLampControlType);
					}
					break;
					
					
	            case LAMP_CONTROL_CURRENT_ALL_CALLBACK_ID:
					if (mCurrentLampControlReportFragment!=null) {
						mCurrentLampControlReportFragment.setLampControlReportData(reportData, mLampControlReports,mLampControlType);
					}
					break;
					
					
	            case LAMP_CONTROL_TEMPER_ALL_CALLBACK_ID:
					if (mTemperLampControlReportFragment!=null) {
						mTemperLampControlReportFragment.setLampControlReportData(reportData, mLampControlReports,mLampControlType);
					}
					break;
					
					
	            case LAMP_CONTROL_CAPACITY_ALL_CALLBACK_ID:
					if (mCapacityLampControlReportFragment!=null) {
						mCapacityLampControlReportFragment.setLampControlReportData(reportData, mLampControlReports,mLampControlType);
					}
					break;
					
					
	            case LAMP_CONTROL_VOLTAGE_ALL_CALLBACK_ID:
					if (mVoltageLampControlReportFragment!=null) {
						mVoltageLampControlReportFragment.setLampControlReportData(reportData, mLampControlReports,mLampControlType);
					}
					break;
					
					
				case LAMP_CONTROL_DATE_CALLBACK_ID:
					setLampControlDate(reportData);
					break;
				}
			} else {
				Toast.makeText(mActivity, reportData.getMsg(),
						Toast.LENGTH_SHORT).show();
			}
		}

	};

}
