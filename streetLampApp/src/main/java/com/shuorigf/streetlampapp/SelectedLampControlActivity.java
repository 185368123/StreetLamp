package com.shuorigf.streetlampapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shuorigf.streetlampapp.adapter.SelectedLampControlContentAdapter;
import com.shuorigf.streetlampapp.data.LampControlData.Data.LampControl;
import com.shuorigf.streetlampapp.data.LampControlReportData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SelectedLampControlActivity extends NavigationBarActivity implements OnClickListener{
private static final int [] COLOR = {0xff12b7f5,0xfffbcb63,0xff87d487,0xfffb6363};
private ListView mSelectedLampControlContentLv;
private SelectedLampControlContentAdapter mSelectedLampControlContentAdapter;
private Button mViewReportBtn;

private  List<LampControl> mLampControls;
//private Bundle projectBundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		View view = View.inflate(this, R.layout.activity_selected_lamp_control, null);
		mSelectedLampControlContentLv = (ListView) view
				.findViewById(R.id.lv_selected_lamp_control_content);
		mViewReportBtn = (Button) view
				.findViewById(R.id.btn_selected_lamp_control_view_report);
		mViewReportBtn.setOnClickListener(this);
		setNavigationBarContentView(view);
	}

	private void initData() {
		setTitleText(R.string.selected_lamp_control);
		mRightTV.setVisibility(View.VISIBLE);
		mRightTV.setText(R.string.empty);
		mRightTV.setTextColor(getResources().getColor(R.color.red));
		mRightTV.setOnClickListener(this);
//		projectBundle = getIntent().getExtras();
		mSelectedLampControlContentAdapter = new SelectedLampControlContentAdapter(this);
		mSelectedLampControlContentLv.setAdapter(mSelectedLampControlContentAdapter);
		mLampControls = (List<LampControl>) getIntent().getSerializableExtra("list");
		mSelectedLampControlContentAdapter.changeData(mLampControls);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_selected_lamp_control_view_report:
			goSelectedLampControl();
			break;
		case R.id.tv_navigation_bar_right:
			if (mSelectedLampControlContentAdapter!=null) {
				mSelectedLampControlContentAdapter.clear();
			}
			break;
		default:
			break;
		}
	}
	private void goSelectedLampControl() {
		List<LampControlReportData> lampControlReports = new ArrayList<LampControlReportData>();
		if (mLampControls!=null&mLampControls.size()>0) {
			for (int i= 0;i<mLampControls.size();i++ ) {
				LampControlReportData lampControlReportData = new LampControlReportData();
				lampControlReportData.setColor(COLOR[i%COLOR.length]);
				lampControlReportData.setId(mLampControls.get(i).getId());
				lampControlReportData.setNumber(mLampControls.get(i).getNumber());
				lampControlReports.add(lampControlReportData);
			}
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("list", (Serializable) lampControlReports);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
		}
		
	}
	

}
