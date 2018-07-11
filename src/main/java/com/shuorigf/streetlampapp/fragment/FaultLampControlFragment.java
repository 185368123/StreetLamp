package com.shuorigf.streetlampapp.fragment;


import com.shuorigf.streetlampapp.R;

import android.os.Bundle;
import android.view.View;

public class FaultLampControlFragment extends BaseFragment {

	private static final String TAG = FaultLampControlFragment.class.getSimpleName();
	

	@Override
	public View initView(Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.viewflipper_fault_lamp_control, null);
		return view;
	}

}
