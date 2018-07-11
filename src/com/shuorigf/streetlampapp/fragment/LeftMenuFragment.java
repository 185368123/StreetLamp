package com.shuorigf.streetlampapp.fragment;


import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.SettingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 左侧菜单Fragment
 * 
 * 
 */
public class LeftMenuFragment extends BaseFragment implements OnClickListener{

	private static final String TAG = LeftMenuFragment.class.getSimpleName();
	
	private TextView mCompanyNameTV;
	private TextView mUserAccountTV;
	private TextView mUserTypeTV;
	

	@Override
	public View initView(Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		((TextView)view.findViewById(R.id.tv_left_menu_setting)).setOnClickListener(this);
		((TextView)view.findViewById(R.id.tv_left_menu_exit)).setOnClickListener(this);
		mCompanyNameTV = (TextView) view.findViewById(R.id.tv_left_menu_company_name_value);
		mUserAccountTV = (TextView) view.findViewById(R.id.tv_left_menu_user_account_value);
		mUserTypeTV = (TextView) view.findViewById(R.id.tv_left_menu_user_type_value);
		return view;
	}
	
	public void initData() {
		
	}
	
	
	private void goSetting() {
		Intent intent = new Intent(mActivity,SettingActivity.class);
		mActivity.startActivity(intent);
	}
	
	private void exit() {
		mActivity.finish();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_left_menu_setting:
			goSetting();
			break;
			
		case R.id.tv_left_menu_exit:
			exit();
			break;
			
		default:
			break;
		}
	}

}
