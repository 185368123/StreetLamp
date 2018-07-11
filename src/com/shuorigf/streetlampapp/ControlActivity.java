package com.shuorigf.streetlampapp;

import com.zhy.http.okhttp.OkHttpUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ControlActivity extends NavigationBarActivity implements OnCheckedChangeListener{
	private CheckBox mButtonChk;
	private TextView mCloseTv;
	private TextView mOpenTv;
	private SeekBar mAimingSb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		View view = View.inflate(this, R.layout.activity_control, null);
		mButtonChk = (CheckBox) view.findViewById(R.id.chk_control_button);
		mButtonChk.setOnCheckedChangeListener(this);
		mCloseTv = (TextView) view.findViewById(R.id.tv_control_close);
		mOpenTv = (TextView) view.findViewById(R.id.tv_control_open); 
		mAimingSb = (SeekBar) view.findViewById(R.id.sb_control_dimming); 
		setNavigationBarContentView(view);
	}

	private void initData() {
		setTitleText(R.string.control);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if(isChecked) {
			mCloseTv.setTextColor(getResources().getColor(R.color.text_gray));
			mOpenTv.setTextColor(getResources().getColor(R.color.blue));
		}else {
			mCloseTv.setTextColor(getResources().getColor(R.color.blue));
			mOpenTv.setTextColor(getResources().getColor(R.color.text_gray));
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
	}
}
