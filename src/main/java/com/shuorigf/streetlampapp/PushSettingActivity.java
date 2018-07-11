package com.shuorigf.streetlampapp;

import android.os.Bundle;
import android.view.View;

public class PushSettingActivity extends NavigationBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	private void initView() {
		View view = View.inflate(this, R.layout.activity_push, null);
		setNavigationBarContentView(view);
	}

	private void initData() {
		setTitleText(R.string.push);
	}
	
	

}
