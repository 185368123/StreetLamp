package com.shuorigf.streetlampapp.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.mikephil.charting.charts.LineChart;
import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.data.GlobalStaticFun;
import com.shuorigf.streetlampapp.data.LampControlReportData;
import com.shuorigf.streetlampapp.data.LineChartData;
import com.shuorigf.streetlampapp.data.ReportData;
import com.shuorigf.streetlampapp.data.ReportData.Data.Report;
import com.shuorigf.streetlampapp.data.ReportData.Data.Report.Series;
import com.shuorigf.streetlampapp.ui.MyMarkerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LampControlReportFirstFragment extends BaseFragment{

	private static final String TAG = LampControlReportFirstFragment.class
			.getSimpleName();
	
	public static final String POWER = "power";
	public static final String CURRENT = "current";
	public static final String TEMPER = "temper";
	public static final String CAPACITY = "capacity";
	public static final String VOLTAGE = "voltage";
	

	private LineChart mLineChartUp;
	private LineChart mLineChartDown;
	
	private TextView mLineChartUpTv;
	private TextView mLineChartDownTv;
	
	private TextView mMaximumUpTv;
	private TextView mMinimumUpTv;
	private TextView mAverageUpTv;
	
	private TextView mMaximumDownTv;
	private TextView mMinimumDownTv;
	private TextView mAverageDownTv;

	
	private String mType;
	private String mUnit;
	

	public static LampControlReportFirstFragment newInstance(String type) {
		LampControlReportFirstFragment lampControlReportFirstFragment = new LampControlReportFirstFragment();
		lampControlReportFirstFragment.mType = type;
		return lampControlReportFirstFragment;
	}

	@Override
	public View initView(Bundle savedInstanceState) {
		View view = View.inflate(mActivity,
				R.layout.fragment_lamp_control_report_first, null);
		mMaximumUpTv = (TextView) view.findViewById(R.id.tv_lamp_control_report_up_maximum_value);
		mMinimumUpTv = (TextView) view.findViewById(R.id.tv_lamp_control_report_up_minimum_value);
		mAverageUpTv = (TextView) view.findViewById(R.id.tv_lamp_control_report_up_average_value);
		mMaximumDownTv = (TextView) view.findViewById(R.id.tv_lamp_control_report_down_maximum_value);
		mMinimumDownTv = (TextView) view.findViewById(R.id.tv_lamp_control_report_down_minimum_value);
		mAverageDownTv = (TextView) view.findViewById(R.id.tv_lamp_control_report_down_average_value);
		mLineChartUpTv = (TextView) view.findViewById(R.id.tv_lamp_control_report_up);
		mLineChartDownTv = (TextView) view.findViewById(R.id.tv_lamp_control_report_down);
		mLineChartUp = (LineChart) view.findViewById(R.id.line_chart_up);
		MyMarkerView mv = new MyMarkerView(getActivity(),
				R.layout.custom_marker_view);
		GlobalStaticFun.InitLineChart(mLineChartUp, mv);
		mLineChartDown = (LineChart) view.findViewById(R.id.line_chart_down);
		GlobalStaticFun.InitLineChart(mLineChartDown, mv);
		return view;
	}

	public void initData() {
		if (POWER.equals(mType)) {
			mLineChartUpTv.setText(R.string.generating_capacity);
			mLineChartDownTv.setText(R.string.electricity_consumption);
			mUnit = "kw/h";
		}else if (CURRENT.equals(mType)) {
			mLineChartUpTv.setText(R.string.maximum_charge_current);
			mLineChartDownTv.setText(R.string.maximum_discharge_current);
			mUnit = "A";
		}else if (TEMPER.equals(mType)) {
			mLineChartUpTv.setText(R.string.minimum_temperature);
			mLineChartDownTv.setText(R.string.maximum_temperature);
			mUnit = "°C";
		}else if (CAPACITY.equals(mType)) {
			mLineChartUpTv.setText(R.string.maximum_charge_power);
			mLineChartDownTv.setText(R.string.maximum_discharge_power);
			mUnit = "W";
		}else if (VOLTAGE.equals(mType)) {
			mLineChartUpTv.setText(R.string.minimum_voltage);
			mLineChartDownTv.setText(R.string.maximum_voltage);
			mUnit = "C";
		}
	}
	
	

	public void setLampControlReportData(ReportData reportData,List<LampControlReportData> lampControlReports,int type) {
		Report report1 = reportData.getDate().getReport1();
		Report report2 = reportData.getDate().getReport2();
		if (report1!=null&&report2!=null) {
			List<String> xVal1 = (ArrayList<String>)report1.getCategories();
			List<Series> series1 = report1.getSeries();
			List<Series> series2 = report2.getSeries();
			
			if (xVal1!=null&&series1!=null&&series1.size()>0&&series2!=null&&series2.size()>0) {
				LineChartData lineChartData1 = new LineChartData(xVal1);
				LineChartData lineChartData2 = new LineChartData(xVal1);
				float max1=0;
				float max2=0;
				float min1=0;
				float min2=0;
				float sum1 = 0;
				int count1 = 0;
				float sum2 = 0;
				int count2 = 0;
				for (int i = 0; i < series1.size(); i++) {
					lineChartData1.AddLine(series1.get(i).getData(), lampControlReports.get(i).getColor(),
							LineChartData.SOLID_LINE);
					max1 = Math.max(max1, Collections.max(series1.get(i).getData()));
					min1 = Math.min(min1, Collections.min(series1.get(i).getData()));
					for (Float f : series1.get(i).getData()) {
						sum1+=f;
					}
					count1+= series1.get(i).getData().size();
				}
				for (int i = 0; i < series2.size(); i++) {
					lineChartData2.AddLine(series2.get(i).getData(), lampControlReports.get(i).getColor(),
							LineChartData.SOLID_LINE);
					max2 = Math.max(max2, Collections.max(series2.get(i).getData()));
					min2 = Math.min(min2, Collections.min(series2.get(i).getData()));
					for (Float f : series2.get(i).getData()) {
						sum2+=f;
					}
					count2+= series2.get(i).getData().size();
				}
				mLineChartUp.getAxisLeft().setAxisMaxValue(
						max1 * 1.2f);
				mLineChartDown.getAxisLeft().setAxisMaxValue(
						max2 * 1.2f);
				if (type == ReportFragment.LAMP_CONTROL_ALL_CALLBACK_ID) {
					mLineChartUp.getXAxis().setLabelsToSkip(xVal1.size() / 6);
					mLineChartDown.getXAxis().setLabelsToSkip(xVal1.size() / 6);
				}else if (type == ReportFragment.LAMP_CONTROL_YEAR_CALLBACK_ID) {
					mLineChartUp.getXAxis().setLabelsToSkip(3);
					mLineChartDown.getXAxis().setLabelsToSkip(3);
				} else if (type == ReportFragment.LAMP_CONTROL_MONTH_CALLBACK_ID ){
					mLineChartUp.getXAxis().setLabelsToSkip(12);
					mLineChartDown.getXAxis().setLabelsToSkip(12);
				}
				
				GlobalStaticFun.AddLineData(mLineChartUp, lineChartData1);
				GlobalStaticFun.AddLineData(mLineChartDown, lineChartData2);
				mMaximumUpTv.setText(max1+mUnit);
				mMinimumUpTv.setText(min1+mUnit);
				mAverageUpTv.setText(String.format("%.2f", sum1/count1)+mUnit);
				mMaximumDownTv.setText(max2+mUnit);
				mMinimumDownTv.setText(min2+mUnit);
				mAverageDownTv.setText(String.format("%.2f", sum2/count2)+mUnit);
			}
			
		}

		
	}
	
	
}
