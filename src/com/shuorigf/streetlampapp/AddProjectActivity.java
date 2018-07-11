package com.shuorigf.streetlampapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.OptionsPickerView.OnOptionsSelectListener;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.ProvinceData;
import com.shuorigf.streetlampapp.data.ProvinceData.Data.Province;
import com.shuorigf.streetlampapp.data.ResultCodeData;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProjectActivity extends NavigationBarActivity implements
		OnClickListener {
	private static final String TAG = AddProjectActivity.class.getSimpleName();

	private EditText mProjectNameEdt;
	private EditText mProjectNumberEdt;
	private EditText mCustomerNameEdt;
	private EditText mCompanyNameEdt;
	private EditText mDetailedAddressEdt;

	private EditText mSelectProvincesEdt;

	private Button mAddBtn;

	private LoginData mLoginData;
	private Dialog mDialog;
	private Dialog mSuccessDialog;
	private Dialog mFailDialog;

	private Dialog mWatingDialog;

	private OptionsPickerView<Province> mProvinceOptionsPV;
	private List<Province> mProvinces;
	private String province_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		mProvinceOptionsPV = new OptionsPickerView<Province>(this);
		mProvinceOptionsPV
				.setOnoptionsSelectListener(new OnOptionsSelectListener() {

					@Override
					public void onOptionsSelect(int options1, int option2,
							int options3) {
						if (mProvinces != null) {
							mSelectProvincesEdt.setText(mProvinces
									.get(options1).getName());
							province_id = mProvinces.get(options1).getId();
						}
					}
				});
		mDialog = DialogFactory.creatRequestDialog(this, R.string.adding);
		mWatingDialog = DialogFactory.creatRequestDialog(this, 0);
		mSuccessDialog = DialogFactory.creatResultDialog(this,
				R.drawable.ic_success, R.string.add_success);
		mSuccessDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent();
			    setResult(RESULT_OK, intent);
				finish();
			}
		});
		mFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail,
				R.string.add_failed);
		View view = View.inflate(this, R.layout.activity_add_project, null);
		mProjectNameEdt = (EditText) view
				.findViewById(R.id.edt_add_project_project_name_value);
		mProjectNumberEdt = (EditText) view
				.findViewById(R.id.edt_add_project_project_number_value);
		mCustomerNameEdt = (EditText) view
				.findViewById(R.id.edt_add_project_customer_name_value);
		mCompanyNameEdt = (EditText) view
				.findViewById(R.id.edt_add_project_company_name_value);
		mDetailedAddressEdt = (EditText) view
				.findViewById(R.id.edt_add_project_detailed_address_value);
		mSelectProvincesEdt = (EditText) view
				.findViewById(R.id.edt_add_project_select_provinces_value);
		mSelectProvincesEdt.setOnClickListener(this);
		mAddBtn = (Button) view.findViewById(R.id.btn_add_project_add);
		mAddBtn.setOnClickListener(this);
		setNavigationBarContentView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		setTitleText(R.string.add_project);
		mRightTV.setVisibility(View.VISIBLE);
		mRightTV.setText(R.string.cancel);
		mRightTV.setTextColor(getResources().getColor(R.color.blue));
		mRightTV.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_project_add:
			addProject();
			break;
		case R.id.tv_navigation_bar_right:
			finish();
			break;
		case R.id.edt_add_project_select_provinces_value:
			if (mProvinces != null && mProvinces.size() > 0) {
				mProvinceOptionsPV.show();
			} else {
				getProvinceData();
			}

			break;

		default:
			break;
		}
	}

	private void getProvinceData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		OkHttpUtils
				.post()
				.tag(this)
				.url(NetManager.PROVINCE_LIST_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<ProvinceData>(
								new JsonGenericsSerializator()) {

							@Override
							public void onBefore(Request request, int id) {
								if (mWatingDialog != null)
									mWatingDialog.show();
							}

							@Override
							public void onAfter(int id) {
								if (mWatingDialog != null)
									mWatingDialog.dismiss();
							}

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"ProvinceData onError:"
												+ e.getMessage());
								Toast.makeText(AddProjectActivity.this, R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(ProvinceData provinceData,
									int id) {
								Log.i(TAG, "ProvinceData onResponse:"
										+ provinceData.toString());
								if (provinceData.getStatus().equals(
										NetManager.SUCCESS)) {
									setProvinceData(provinceData);

								} else {
									Toast.makeText(AddProjectActivity.this,
											provinceData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});
	}

	private void setProvinceData(ProvinceData provinceData) {
		mProvinces = provinceData.getData().getProvinces();
		if (mProvinces != null && mProvinces.size() > 0) {
			mProvinceOptionsPV.setPicker((ArrayList<Province>) mProvinces);
			mProvinceOptionsPV.setCyclic(false);
			mProvinceOptionsPV.show();
		}
	}

	private void addProject() {
		String projectName = mProjectNameEdt.getText().toString();
		String projectNumber = mProjectNumberEdt.getText().toString();
		String customerName = mCustomerNameEdt.getText().toString();
		String companyName = mCompanyNameEdt.getText().toString();
		String detailedAddress = mDetailedAddressEdt.getText().toString();
//		String province = mSelectProvincesEdt.getText().toString();
		if (TextUtils.isEmpty(projectName)) {
			Toast.makeText(this, R.string.project_name_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(projectNumber)) {
			Toast.makeText(this, R.string.project_number_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(customerName)) {
			Toast.makeText(this, R.string.customer_name_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(companyName)) {
			Toast.makeText(this, R.string.company_name_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (TextUtils.isEmpty(province_id)) {
			Toast.makeText(this, R.string.province_in_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(detailedAddress)) {
			Toast.makeText(this, R.string.detailed_address_cannot_be_empty,
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		

		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("project_name", projectName);
		params.put("project_no", projectNumber);
		params.put("customer", customerName);
		params.put("company", companyName);
		params.put("province_id", province_id);
		params.put("address", detailedAddress);

		OkHttpUtils
				.post()
				.tag(this)
				.url(NetManager.PROJECT_ADD_OR_EDIT_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<ResultCodeData>(
								new JsonGenericsSerializator()) {
							@Override
							public void onBefore(Request request, int id) {
								if (mDialog != null)
									mDialog.show();
							}

							@Override
							public void onAfter(int id) {
								if (mDialog != null)
									mDialog.dismiss();
							}

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"Add Project onError:" + e.getMessage());
								Toast.makeText(AddProjectActivity.this, R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(
									ResultCodeData resultCodeData, int id) {
								Log.i(TAG, "Add Project onResponse:"
										+ resultCodeData.toString());
								if (resultCodeData.getStatus().equals(
										NetManager.SUCCESS)) {
									if (mSuccessDialog != null)
										mSuccessDialog.show();
								} else {
									Toast.makeText(AddProjectActivity.this, resultCodeData.getMsg(),
											Toast.LENGTH_SHORT).show();
									if (mFailDialog != null)
										mFailDialog.show();
								}
							}
						});

	}

	@Override
	public void onBackPressed() {
		if (mProvinceOptionsPV.isShowing()) {
			mProvinceOptionsPV.dismiss();
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
