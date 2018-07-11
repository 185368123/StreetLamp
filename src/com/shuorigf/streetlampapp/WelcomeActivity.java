package com.shuorigf.streetlampapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity {
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
        mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(WelcomeActivity.this,
						LoginActivity.class);
				WelcomeActivity.this.startActivity(intent);
				WelcomeActivity.this.finish();
			}
		}, 2000);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHandler!=null) {
			mHandler.removeCallbacksAndMessages(null);
		}
		
	}
	
	

}
