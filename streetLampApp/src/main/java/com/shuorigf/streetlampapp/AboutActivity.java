package com.shuorigf.streetlampapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends NavigationBarActivity {
	private TextView mVersionTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	private void initView() {
		View view = View.inflate(this, R.layout.activity_about, null);
		mVersionTV = (TextView) view.findViewById(R.id.tv_about_version_value);
		
		setNavigationBarContentView(view);
	}

	private void initData() {
		setTitleText(R.string.about);
		mVersionTV.setText("V"+getVersion());
	}
	
	
	private String getVersion() {
		
		try {
			PackageManager pm = getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	

}
