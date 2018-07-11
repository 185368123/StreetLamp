package com.shuorigf.streetlampapp.data;

import java.util.ArrayList;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.data.LineChartData.LineInfoData;
import com.shuorigf.streetlampapp.ui.MyMarkerView;


import android.app.Activity;
import android.graphics.Color;
import android.telephony.TelephonyManager;

public class GlobalStaticFun {

	public static String getPhoneImei()
	{
		String imei= ((TelephonyManager) StreetlampApp.getContextObject().getSystemService(Activity.TELEPHONY_SERVICE))
				.getDeviceId();
		return imei;
	}

	public static String floatToPercentageString(float val)
	{
		String ret="0%";
		ret = (int)(val*100)+"%";
		return ret;
	}

	public static void InitLineChart(LineChart lChart, MyMarkerView mv)
	{
		if(lChart == null)
			return;
		// no description text
		lChart.setDescription("");
		lChart.setNoDataTextDescription("");
		lChart.setNoDataText("");
		lChart.setDrawGridBackground(false);
		// enable value highlighting
		lChart.setHighlightEnabled(true);

		// enable touch gestures
		lChart.setTouchEnabled(true);

		// enable scaling and dragging
		lChart.setDragEnabled(false);
		lChart.setScaleEnabled(false);
		// lChart.setScaleXEnabled(true);
		// lChart.setScaleYEnabled(true);

		// if disabled, scaling can be done on x- and y-axis separately
		lChart.setPinchZoom(false);

		// set an alternative background color
		// lChart.setBackgroundColor(Color.GRAY);

		// create a custom MarkerView (extend MarkerView) and specify the layout
		// to use for it
		//   MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

		// set the marker to the chart
		lChart.setMarkerView(mv);

		lChart.setDrawMarkerViews(true);
		// x-axis limit line
		/*   LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = lChart.getXAxis();
        xAxis.addLimitLine(llXAxis);*/
		/*
        LimitLine ll1 = new LimitLine(130f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);*/
		XAxis xAxis = lChart.getXAxis();
		xAxis.setDrawGridLines(false);
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setAxisLineWidth(1);
		//     Paint xPaint = lChart.getRendererXAxis().getPaintAxisLabels();
		//      xPaint.setTextSize(Utils.convertDpToPixel(32f));
		xAxis.setTextSize(12);
		xAxis.setAvoidFirstLastClipping(true);
		//xAxis.setLabelsToSkip(0);

		YAxis leftAxis = lChart.getAxisLeft();
		leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
		// leftAxis.addLimitLine(ll1);
		//   leftAxis.addLimitLine(ll2);
		//leftAxis.setAxisMaxValue(2f);
		//leftAxis.setAxisMinValue(0f);
		leftAxis.setStartAtZero(false);
		leftAxis.setYOffset(20f);
		leftAxis.enableGridDashedLine(10f, 10f, 0f);
		leftAxis.setGridColor(Color.TRANSPARENT);
		//  leftAxis.enableGridDashedLine(0, 0, 0);

		// limit lines are drawn behind data (and not on top)
		leftAxis.setDrawLimitLinesBehindData(false);
		leftAxis.setDrawAxisLine(true);
		leftAxis.setDrawLabels(true);
		leftAxis.setAxisLineWidth(1);
		leftAxis.setEnabled(true);

		lChart.getAxisRight().setEnabled(false);

		//      lChart.setVisibleXRange(20);
		//      lChart.setVisibleYRange(20f, AxisDependency.LEFT);
		//      lChart.centerViewTo(20, 50, AxisDependency.LEFT);

		//lChart.animateX(2000, Easing.EasingOption.EaseInOutQuart);
		//     lChart.animateX(3000);
		lChart.animateXY(3000, 3000);
		//      lChart.invalidate();

		// get the legend (only possible after setting data)
		Legend l = lChart.getLegend();

		// modify the legend ...
		l.setEnabled(false);//barack
		l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setForm(LegendForm.LINE);
	}

	public static void AddLineData(LineChart lineChart,
			LineChartData lineChartData) {
		if(lineChart == null || lineChartData == null || lineChartData.mLinDataList == null || lineChartData.mXVal == null)
			return;
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		for(int i=0;i<lineChartData.mLinDataList.size();i++)
		{
			LineInfoData data = lineChartData.mLinDataList.get(i);
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			for(int j=0;j<lineChartData.mXVal.size();j++)
			{
				yVals.add(new Entry(data.yVal.get(j), j,""+j+"D"));
			}
			LineDataSet set = new LineDataSet(yVals, "DataSet"+i);

			if(data.lineType ==LineChartData.DOTTED_LINE)
				set.enableDashedLine(10f, 10f, 0f); 
			else
				set.enableDashedLine(10f,0f, 0f); 

			set.setColor(data.lineColor);
			set.setCircleColor(data.lineColor);

			set.setLineWidth(1f);
			set.setCircleSize(3f);
			set.setDrawCircleHole(false);
			set.setValueTextSize(9f);
//			set.setFillAlpha(65);
//			set.setFillColor(Color.WHITE);
			set.setDrawValues(false);
			set.setDrawCubic(true);
			set.setCubicIntensity(0.2f); 
			set.setHighLightColor(Color.WHITE);
			set.setHighlightLineWidth(1f);
			set.SetXUintStr("DD");
			set.SetYUintStr("VV");
			set.setDrawHorizontalHighlightIndicator(true);
			set.setDrawVerticalHighlightIndicator(true);
			dataSets.add(set); // add the datasets
		}


		LineData data = new LineData(lineChartData.mXVal, dataSets);
		// set data
		lineChart.setData(data);
		lineChart.invalidate();
	}
}
