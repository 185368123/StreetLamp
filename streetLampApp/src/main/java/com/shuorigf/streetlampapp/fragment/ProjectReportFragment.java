package com.shuorigf.streetlampapp.fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.github.mikephil.charting.charts.LineChart;
import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.TotalProjectListActivity;
import com.shuorigf.streetlampapp.adapter.ProjectContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.GlobalStaticFun;
import com.shuorigf.streetlampapp.data.HomeData;
import com.shuorigf.streetlampapp.data.LineChartData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.ProjectData;
import com.shuorigf.streetlampapp.data.ProjectData.Data.Project;
import com.shuorigf.streetlampapp.data.ReportData;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.ui.MyMarkerView;
import com.shuorigf.streetlampapp.ui.RingRate;
import com.zhy.http.okhttp.OkHttpUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ProjectReportFragment extends BaseFragment implements OnClickListener{

	private static final String TAG = ProjectReportFragment.class.getSimpleName();

	private LineChart mLineChartUp;
	private RadioGroup mTabRGrp;
	private TextView mSelectDateTV;
	
	private TextView mProjectNameTV;
	private TextView mLightingRateTV;
	private TextView mOnlineRateTv;
	private TextView mFailureRateTV;

	private RingRate mRingRate;

	private TextView mTotalInstalledLightsTV;
	private TextView mTotalNetworkNumberTV;

	private TextView mSaveStandardCoalTV;
	private TextView mCumulativeReductionCO2TV;
	private TextView mCumulativeReductionSO2TV;

	private HomeData mHomeData;
	private LoginData mLoginData;
	private ProjectData mProjectData;
	private ReportData mReportData;
	private List <Project> mProjects;
	


	@Override
	public View initView(Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.fragment_project_report, null);
		mTabRGrp = (RadioGroup) view.findViewById(R.id.rgrp_project_report_date);
		mTabRGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbtn_project_report_year:
					mSelectDateTV.setVisibility(View.VISIBLE);
					break;
				case R.id.rbtn_project_report_month:
					mSelectDateTV.setVisibility(View.VISIBLE);
					break;
				case R.id.rbtn_project_report_all:
					mSelectDateTV.setVisibility(View.GONE);
					break;
				default:
					break;
				}
			}
		});
		mSelectDateTV = (TextView) view
				.findViewById(R.id.tv_project_report_select_date);
		mRingRate = (RingRate) view.findViewById(R.id.ringrate_project_report_rate);
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

		mSaveStandardCoalTV = (TextView) view
				.findViewById(R.id.tv_project_report_save_standard_coal_value);
		mCumulativeReductionCO2TV = (TextView) view
				.findViewById(R.id.tv_project_report_cumulative_reduction_co2_value);
		mCumulativeReductionSO2TV = (TextView) view
				.findViewById(R.id.tv_project_report_cumulative_reduction_so2_value);
		
		mLineChartUp =(LineChart)view.findViewById(R.id.line_chart_project_report);
		MyMarkerView mv = new MyMarkerView(mActivity, R.layout.custom_marker_view);
		GlobalStaticFun.InitLineChart(mLineChartUp, mv);
		return view;
	}


	public void initData() {
		mLoginData = ((StreetlampApp)mActivity.getApplication()).mLoginData;
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
			public void onError(Call call, Exception e, int id)
			{
				e.printStackTrace();
				Log.e(TAG, "ProjectData onError:"+e.getMessage());
			}
			@Override
			public void onResponse(ProjectData projectData, int id) {
				Log.i(TAG, "ProjectData onResponse:"+projectData.toString());
				if(projectData.getStatus().equals(NetManager.SUCCESS)){
					mProjectData = projectData;
					setProjectData();

				}else {
					Toast.makeText(mActivity, projectData.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
			}	
				});
	}

	private void setProjectData() {
		mProjects = mProjectData.getData().getProjects();
		if (mProjects!=null&&mProjects.size()>0) {
			mProjectNameTV.setText(mProjects.get(0).getName());
			getHomeData(mProjects.get(0).getId()+"");
			getReportData(mProjects.get(0).getId()+"");
		}

	}

	private void getHomeData(String project_id) {
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("project_id", project_id);
		OkHttpUtils
		.post()
		.url(NetManager.HOMEDATA_URL)
		.params(params)
		.build()
		.execute(new GenericsCallback<HomeData>(new JsonGenericsSerializator())
				{

			@Override
			public void onError(Call call, Exception e, int id)
			{
				e.printStackTrace();
				Log.e(TAG, "HomeData onError:"+e.getMessage());
			}
			@Override
			public void onResponse(HomeData homeData, int id) {
				Log.i(TAG, "HomeData onResponse:"+homeData.toString());
				if(homeData.getStatus().equals(NetManager.SUCCESS)){
					mHomeData = homeData;
					setHomeDate();

				}else {
					Toast.makeText(mActivity, homeData.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
			}	
				});
		
	}

	private void setHomeDate() {

		mOnlineRateTv.setText(GlobalStaticFun
				.floatToPercentageString(mHomeData.getData().getOnline_rate()));
		mFailureRateTV.setText(GlobalStaticFun
				.floatToPercentageString(mHomeData.getData().getFailure_rate()));
		mLightingRateTV.setText(GlobalStaticFun.floatToPercentageString(mHomeData.getData().getLighting_rate()));
		mRingRate.setCircleRadian(mHomeData.getData().getOnline_rate(), mHomeData.getData().getLighting_rate(), mHomeData.getData().getFailure_rate());
		mTotalInstalledLightsTV.setText(mHomeData.getData().getTotal_lamp()+"");
		mTotalNetworkNumberTV.setText(mHomeData.getData().getTotal_network()+"");
		mSaveStandardCoalTV.setText(mHomeData.getData().getCoal_saving()+"");
		mCumulativeReductionCO2TV.setText(mHomeData.getData().getCo2_emission()+"");
		mCumulativeReductionSO2TV.setText(mHomeData.getData().getSo2_emission()+"");
	}
	
	
	private void getReportData(String project_id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("show_type", "project");
		params.put("project_id", project_id);
//		params.put("date_type", "year");
//		params.put("date", "2016");
		OkHttpUtils
		.post()
		.url(NetManager.REPORT_URL)
		.params(params)
		.build()
		.execute(new GenericsCallback<String>(new JsonGenericsSerializator())
				{

			@Override
			public void onError(Call call, Exception e, int id)
			{
				e.printStackTrace();
				Log.e(TAG, "ReportData onError:"+e.getMessage());
			}
			@Override
			public void onResponse(String reportData, int id) {
				Log.i(TAG, "ReportData onResponse:"+reportData.toString());
//				if(reportData.getStatus().equals(NetManager.SUCCESS)){
//					mReportData = reportData;
//					setReportData();
//				}else {
//					Toast.makeText(mActivity, reportData.getMsg(),
//							Toast.LENGTH_SHORT).show();
//				}
			}
				});
	}
	
	private void setReportData() {
		List<String> xVal = mReportData.getDate().getReport1().getCategories();
		List<Float>yVal1 = mReportData.getDate().getReport1().getSeries().get(0).getData();//line 1
		List<Float>yVal2 = mReportData.getDate().getReport2().getSeries().get(0).getData();//line 2
		LineChartData lineChartData = new LineChartData(xVal);
		lineChartData.AddLine(yVal1, Color.rgb(0x12, 0xb7, 0xf5), LineChartData.SOLID_LINE);
		lineChartData.AddLine(yVal2, Color.rgb(0xfb, 0x63, 0x63), LineChartData.SOLID_LINE);
		GlobalStaticFun.AddLineData(mLineChartUp,lineChartData);
	}	


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_project_report_project_name:
			break;	
		default:
			break;
		}		
	}

}
