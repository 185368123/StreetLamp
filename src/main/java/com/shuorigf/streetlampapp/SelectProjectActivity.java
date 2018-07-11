package com.shuorigf.streetlampapp;

import android.os.Bundle;
import android.view.View;

public class SelectProjectActivity extends NavigationBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		View view = View.inflate(this, R.layout.activity_select_project, null);
		setNavigationBarContentView(view);
	}

	private void initData() {
		setTitleText("选择项目");
	}
	
	

}
