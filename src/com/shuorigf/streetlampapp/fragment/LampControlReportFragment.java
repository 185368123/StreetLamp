package com.shuorigf.streetlampapp.fragment;


import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.LineChart;
import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.TotalLampControlListActivity.ContentAdapter;
import com.shuorigf.streetlampapp.data.GlobalStaticFun;
import com.shuorigf.streetlampapp.data.LineChartData;
import com.shuorigf.streetlampapp.ui.MyMarkerView;
import com.viewpagerindicator.PageIndicator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class LampControlReportFragment extends BaseFragment {

	private static final String TAG = LampControlReportFragment.class.getSimpleName();
    private  ViewPager mViewPager;
    private PageIndicator mIndicator;
    private ArrayList<BaseFragment> mFragmentList;
	
	@Override
	public View initView(Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.fragment_lamp_control_report, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_lamp_control_report);
		mIndicator =  (PageIndicator) view.findViewById(R.id.indicator_lamp_control_report);
		return view;
	}
	
	public void initData() {
		mFragmentList = new ArrayList<BaseFragment>();
		mFragmentList.add(new LampControlReportFirstFragment());
		mViewPager.setAdapter(new ContentAdapter(
				getChildFragmentManager()));
		mIndicator.setViewPager(mViewPager);
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

}
