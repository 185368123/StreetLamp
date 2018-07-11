package com.shuorigf.streetlampapp.fragment;


import com.shuorigf.streetlampapp.R;

import android.os.Bundle;
import android.view.View;

public class AllLampControlFragment extends BaseFragment {

	private static final String TAG = AllLampControlFragment.class.getSimpleName();
	

	@Override
	public View initView(Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.viewflipper_all_lamp_control, null);
		return view;
	}

}
