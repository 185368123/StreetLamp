package com.shuorigf.streetlampapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.OptionsPickerView.OnOptionsSelectListener;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.ResultCodeData;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.TextView;

public class InspectionSettingActivity extends NavigationBarActivity implements OnClickListener{
	private static final String TAG = InspectionSettingActivity.class.getSimpleName();
	
	private CheckBox mSwitchChk;
	private TextView mIntervalTimeTv;
	private Button mComfirmBtn;
	
	private LoginData mLoginData;
	
	private String project_id;
	
	private Dialog mDialog;
	private Dialog mSuccessDialog;
	private Dialog mFailDialog;
	
	private OptionsPickerView<String> mOptionsPV;
	private ArrayList<String> mIntervalTimeList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	private void initView() {
		mSuccessDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_success, R.string.set_up_success);
		mFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.set_up_failed);
		mDialog = DialogFactory.creatRequestDialog(this, R.string.setting_up);
		mOptionsPV = new OptionsPickerView<String>(this);
		mOptionsPV.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				mIntervalTimeTv.setText(mIntervalTimeList.get(options1));
//				inspectionSetting();
			}
		});
		View view = View.inflate(this, R.layout.activity_inspection_setting, null);
		mSwitchChk = (CheckBox) view.findViewById(R.id.chk_inspection_setting_inspection_switch);
		mIntervalTimeTv = (TextView) view.findViewById(R.id.tv_inspection_setting_inspection_interval_time);
		mIntervalTimeTv.setOnClickListener(this);
		mComfirmBtn = (Button) view.findViewById(R.id.btn_inspection_setting_comfirm);
		mComfirmBtn.setOnClickListener(this);
//		mSwitchChk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				inspectionSetting();
//			}
//		});
		
		setNavigationBarContentView(view);
	}

	private void initData() {
		for(int i=1; i<25;i++){
			mIntervalTimeList.add(i+"h");
		}
		mOptionsPV.setPicker(mIntervalTimeList);
		mOptionsPV.setSelectOptions(0);
		mOptionsPV.setCyclic(false);
		mLoginData = ((StreetlampApp)getApplication()).mLoginData;
		setTitleText(R.string.inspection_setting);
		mIntervalTimeTv.setText(mIntervalTimeList.get(0));
		project_id = getIntent().getStringExtra("project_id");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_inspection_setting_inspection_interval_time:
			if (!mOptionsPV.isShowing()) {
				mOptionsPV.show();
			}
			break;
		case R.id.btn_inspection_setting_comfirm:
			inspectionSetting();
			break;
			
		default:
			break;
		}
	}
	
	
	private void inspectionSetting() {
		if (project_id==null) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("project_id", project_id);
        params.put("switch", mSwitchChk.isChecked()==false? "0":"1");
        params.put("interval", mIntervalTimeTv.getText().toString().substring(0, mIntervalTimeTv.getText().toString().length()-1));
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.PROJECT_INSPECTION_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<ResultCodeData>(new JsonGenericsSerializator())
                      {
                	@Override
					public void onBefore(Request request, int id) {
                		if(mDialog!=null)
                		mDialog.show();
                	}
                	
                      @Override
						public void onAfter(int id) {
                    	  if(mDialog!=null)
                    	  mDialog.dismiss();
                      }
					@Override
                      public void onError(Call call, Exception e, int id)
                      {
                    	  e.printStackTrace();
                    	  Log.e(TAG, "Inspection Setting onError:"+e.getMessage());
                    	  Toast.makeText(InspectionSettingActivity.this, R.string.network_is_not_smooth,
									Toast.LENGTH_SHORT).show();
                      }
					@Override
					public void onResponse(ResultCodeData resultCodeData, int id) {
						if(resultCodeData.getStatus().equals(NetManager.SUCCESS)){
							 if(mSuccessDialog!=null)
								 mSuccessDialog.show();
						 }else {
							 Toast.makeText(InspectionSettingActivity.this, resultCodeData.getMsg(),
										Toast.LENGTH_SHORT).show();
							 if(mFailDialog!=null)
								 mFailDialog.show();
						 }
					}
                  });
	}
	
	@Override
	public void onBackPressed() {
		if (mOptionsPV.isShowing()) {
			mOptionsPV.dismiss();
			return;
		}
		super.onBackPressed();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
	}
}
