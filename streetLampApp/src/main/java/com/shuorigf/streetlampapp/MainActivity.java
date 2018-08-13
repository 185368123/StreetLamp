package com.shuorigf.streetlampapp;

import java.util.ArrayList;

import okhttp3.Call;

import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.fragment.BaseFragment;
import com.shuorigf.streetlampapp.fragment.DeviceFragment;
import com.shuorigf.streetlampapp.fragment.FaultFragment;
import com.shuorigf.streetlampapp.fragment.HomePageFragment;
import com.shuorigf.streetlampapp.fragment.ReportFragment;
import com.shuorigf.streetlampapp.ui.NoScrollViewPager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private static final String TAG = MainActivity.class.getSimpleName();
	
	private NoScrollViewPager mViewPager;
	private RadioGroup mTabBarRGrp;
	private ArrayList<BaseFragment> mFragmentList;

	private DrawerLayout mDrawerLayout;
	private TextView mCompanyNameTV;
	private TextView mUserAccountTV;
	private TextView mUserTypeTV;
	private ImageView mUserImageIV;
	private TextView mFaultCountTV;

	private LoginData mLoginData;

	private boolean isLoading;
	private boolean isLoaded;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// initLeftMenu();
		initView();
		initDate();
	}

	private void initView() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
		mViewPager = (NoScrollViewPager) findViewById(R.id.vp_main_content);
		mTabBarRGrp = (RadioGroup) findViewById(R.id.rgrp_main_tab_bar);
		mTabBarRGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbtn_main_homepage:
					mViewPager.setCurrentItem(0, false);
					break;
				case R.id.rbtn_main_device:
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.rbtn_main_report:
					mViewPager.setCurrentItem(2, false);
					break;
				case R.id.rbtn_main_fault:
					mViewPager.setCurrentItem(3, false);
					break;

				default:
					break;
				}
			}
		});
		((TextView) findViewById(R.id.tv_left_menu_setting))
				.setOnClickListener(this);
		((TextView) findViewById(R.id.tv_left_menu_exit))
				.setOnClickListener(this);
		mCompanyNameTV = (TextView) findViewById(R.id.tv_left_menu_company_name_value);
		mUserAccountTV = (TextView) findViewById(R.id.tv_left_menu_user_account_value);
		mUserTypeTV = (TextView) findViewById(R.id.tv_left_menu_user_type_value);
		mUserImageIV = (ImageView) findViewById(R.id.iv_left_menu_user_image);
		mFaultCountTV = (TextView) findViewById(R.id.tv_main_fault_count);
	}

	private void initDate() {
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		if (mLoginData.getData().getCompany()==null) {
			mCompanyNameTV.setText(R.string.unknown);
		}else {
			mCompanyNameTV.setText(mLoginData.getData().getCompany());
		}
		
		mUserAccountTV.setText(mLoginData.getData().getRealname());
		mUserTypeTV.setText(getUserType(mLoginData.getData().getRole()));

		mFragmentList = new ArrayList<BaseFragment>();
		mFragmentList.add(new HomePageFragment());
		mFragmentList.add(new DeviceFragment());
		mFragmentList.add(new ReportFragment());
		mFragmentList.add(new FaultFragment());
		mViewPager.setOffscreenPageLimit(mFragmentList.size());
		mViewPager.setAdapter(new ContentAdapter(getSupportFragmentManager()));
	}

	private int getUserType(int role) {
		int roleId = R.string.corporate_client;
		switch (role) {
		case 0:
			roleId = R.string.super_administrator;
			break;
		case 1:
			roleId = R.string.company_manager;
			break;
		case 2:
			roleId = R.string.corporate_client;
			break;
		default:
			break;
		}

		return roleId;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isLoaded) {
			getUserImage();
		}
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
	}

	private void getUserImage() {
		if (mLoginData.getData().getAvatar() == null) {
			return;
		}
		if (isLoading) {
			return;
		}
		isLoading = true;
		OkHttpUtils.get().tag(this).url(mLoginData.getData().getAvatar()).build()
				.execute(new BitmapCallback() {

					@Override
					public void onAfter(int id) {
						super.onAfter(id);
						isLoading = false;
					}

					@Override
					public void onResponse(Bitmap bitmap, int id) {
						isLoaded = true;
						if (bitmap != null)
							mUserImageIV.setImageBitmap(bitmap);
					}

					@Override
					public void onError(Call call, Exception e, int id) {
					  Log.e(TAG, "Get Bitmap onError:"+e.getMessage());
                  	  Toast.makeText(MainActivity.this, R.string.network_is_not_smooth,
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	// private void initLeftMenu() {
	// setBehindContentView(R.layout.left_menu);
	// SlidingMenu slidingMenu = getSlidingMenu();
	// slidingMenu.setMode(SlidingMenu.LEFT);
	// slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
	// // int width = getWindowManager().getDefaultDisplay().getWidth();
	// slidingMenu.setBehindOffset(DensityUtils.dip2px(this, 75));
	// FragmentManager fragmentManager = getSupportFragmentManager();
	// FragmentTransaction fragmentTransaction =
	// fragmentManager.beginTransaction();
	//
	// fragmentTransaction.replace(R.id.flyt_left_menu, new LeftMenuFragment());
	// fragmentTransaction.commit();
	// }

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

	private void goSetting() {
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}

	private void exit() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra("is_exit", true);
		startActivity(intent);
		finish();
	}

	public void toggle() {
		mDrawerLayout.openDrawer(GravityCompat.START);
	}
	
	public void setFaultCount(String count) {
		mFaultCountTV.setVisibility(View.VISIBLE);
		mFaultCountTV.setText(count);
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

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

}
