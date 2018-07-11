package com.shuorigf.streetlampapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public abstract class NavigationBarActivity extends Activity {
	private TextView mTitleTv;
	private FrameLayout mContentFlyt;
	protected ImageButton mRightIBtn;
	protected TextView mRightTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_navigation_bar);
		mTitleTv = (TextView) findViewById(R.id.tv_navigation_bar_title);
		mContentFlyt = (FrameLayout) findViewById(R.id.flyt_navigation_bar_content);
		mRightIBtn = (ImageButton) findViewById(R.id.imgbtn_navigation_bar_right);
		mRightTV = (TextView) findViewById(R.id.tv_navigation_bar_right);
	}
	
	public void setTitleText(int resid) {
		mTitleTv.setText(resid);
	}
	
	public void setTitleText(String text) {
		mTitleTv.setText(text);
	}
	
	public void setNavigationBarContentView(View view) {
		mContentFlyt.addView(view);
	}
	
	
	public void back(View view) {
		finish();
	}
	
	

}
