package com.shuorigf.streetlampapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends NavigationBarActivity implements OnClickListener{
	private Button mPushSettingBtn;
	private Button mAboutBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		View view = View.inflate(this, R.layout.activity_setting, null);
		mPushSettingBtn = (Button) view.findViewById(R.id.btn_setting_push_setting);
		mPushSettingBtn.setOnClickListener(this);
		mAboutBtn = (Button) view.findViewById(R.id.btn_setting_about);
		mAboutBtn.setOnClickListener(this);
		setNavigationBarContentView(view);
	}

	private void initData() {
		setTitleText(R.string.setting);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_setting_push_setting:
			goPushSetting();
			break;
		case R.id.btn_setting_about:
			goSetting();
			break;
			
		default:
			break;
		}
	}
	
	private void goPushSetting() {
		Intent intent = new Intent(this,PushSettingActivity.class);
		this.startActivity(intent);
	}
	
	private void goSetting() {
		Intent intent = new Intent(this,AboutActivity.class);
		this.startActivity(intent);
	}

}
