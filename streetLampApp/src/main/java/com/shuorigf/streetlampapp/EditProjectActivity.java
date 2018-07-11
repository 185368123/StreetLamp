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
import com.shuorigf.streetlampapp.data.ProjectDetailsData.Data;
import com.shuorigf.streetlampapp.data.ProvinceData.Data.Province;
import com.shuorigf.streetlampapp.data.ResultCodeData;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

public class EditProjectActivity extends NavigationBarActivity implements
		OnClickListener {
	private static final String TAG = EditProjectActivity.class.getSimpleName();
	private static final int SAVE_CALLBACK_ID = 100;
	private static final int DEL_CALLBACK_ID = 101;
	private EditText mProjectNameEdt;
	private EditText mProjectNumberEdt;
	private EditText mCustomerNameEdt;
	private EditText mCompanyNameEdt;
	private EditText mDetailedAddressEdt;

	private EditText mSelectProvincesEdt;

	private Button mSaveBtn;
	private Button mDeleteThisProjectBtn;

	private LoginData mLoginData;

	private AlertDialog mDialog;

	private Dialog mSaveDialog;
	private Dialog mSaveSuccessDialog;
	private Dialog mSaveFailDialog;

	private Dialog mDelDialog;
	private Dialog mDelSuccessDialog;
	private Dialog mDelFailDialog;

	private Dialog mWatingDialog;

	private OptionsPickerView<Province> mProvinceOptionsPV;
	private List<Province> mProvinces;
	private String province_id;

	private String project_id;

	private Data mData;

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
		mWatingDialog = DialogFactory.creatRequestDialog(this, 0);
		mSaveDialog = DialogFactory.creatRequestDialog(this, R.string.saving);
		mSaveSuccessDialog = DialogFactory.creatResultDialog(this,
				R.drawable.ic_success, R.string.save_success);
		mSaveFailDialog = DialogFactory.creatResultDialog(this,
				R.drawable.ic_fail, R.string.save_failed);
		mSaveSuccessDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent();
				intent.putExtra("isDel", false);
				setResult(RESULT_OK, intent);
				finish();

			}
		});
		mDelDialog = DialogFactory.creatRequestDialog(this, R.string.deleting);
		mDelSuccessDialog = DialogFactory.creatResultDialog(this,
				R.drawable.ic_success, R.string.delete_success);
		mDelFailDialog = DialogFactory.creatResultDialog(this,
				R.drawable.ic_fail, R.string.delete_failed);
		mDelSuccessDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent();
				intent.putExtra("isDel", true);
				setResult(RESULT_OK, intent);
				finish();

			}
		});
		View view = View.inflate(this, R.layout.activity_edit_project, null);
		mProjectNameEdt = (EditText) view
				.findViewById(R.id.edt_edit_project_project_name_value);
		mProjectNumberEdt = (EditText) view
				.findViewById(R.id.edt_edit_project_project_number_value);
		mCustomerNameEdt = (EditText) view
				.findViewById(R.id.edt_edit_project_customer_name_value);
		mCompanyNameEdt = (EditText) view
				.findViewById(R.id.edt_edit_project_company_name_value);
		mDetailedAddressEdt = (EditText) view
				.findViewById(R.id.edt_edit_project_detailed_address_value);
		mSelectProvincesEdt = (EditText) view
				.findViewById(R.id.edt_edit_project_select_provinces_value);
		mSelectProvincesEdt.setOnClickListener(this);
		mSaveBtn = (Button) view.findViewById(R.id.btn_edit_project_save);
		mSaveBtn.setOnClickListener(this);
		mDeleteThisProjectBtn = (Button) view
				.findViewById(R.id.btn_edit_project_delete_this_project);
		mDeleteThisProjectBtn.setOnClickListener(this);
		setNavigationBarContentView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		setTitleText(R.string.edit_project);
		mRightTV.setVisibility(View.VISIBLE);
		mRightTV.setText(R.string.cancel);
		mRightTV.setTextColor(getResources().getColor(R.color.blue));
		mRightTV.setOnClickListener(this);
		mData = (Data) getIntent().getSerializableExtra("data");
		project_id = getIntent().getStringExtra("project_id");
		mProjectNameEdt.setText(mData.getProject_name());
		mProjectNumberEdt.setText(mData.getProject_no());
		mCustomerNameEdt.setText(mData.getCustomer());
		mCompanyNameEdt.setText(mData.getCompany());
		mDetailedAddressEdt.setText(mData.getAddress());
		mSelectProvincesEdt.setText(mData.getProvince());
		province_id = mData.getProvince_id();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_edit_project_save:
			saveProject();
			break;
		case R.id.btn_edit_project_delete_this_project:
			showDelDialog();
			break;

		case R.id.tv_navigation_bar_right:
			finish();
			break;
		case R.id.edt_edit_project_select_provinces_value:
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
								Toast.makeText(EditProjectActivity.this, R.string.network_is_not_smooth,
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
									Toast.makeText(EditProjectActivity.this,
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

	
	private void showDelDialog() {
		if (mDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View view = View.inflate(this, R.layout.dialog_delete, null);
			((TextView) view.findViewById(R.id.tv_show_content))
					.setText(R.string.are_you_sure_you_want_to_delete_this_project);
			((Button) view.findViewById(R.id.btn_confirm))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							delProject();
							if (mDialog != null) {
								mDialog.dismiss();
							}
						}
					});
			((Button) view.findViewById(R.id.btn_cancel))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mDialog != null) {
								mDialog.dismiss();
							}
						}
					});
			mDialog = builder.create();
			mDialog.setView(view);
		}
		if (mDialog != null) {
			mDialog.show();
		}
	}

	private void saveProject() {
		String projectName = mProjectNameEdt.getText().toString();
		String projectNumber = mProjectNumberEdt.getText().toString();
		String customerName = mCustomerNameEdt.getText().toString();
		String companyName = mCompanyNameEdt.getText().toString();
		String detailedAddress = mDetailedAddressEdt.getText().toString();
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

		if (project_id == null) {
			return;
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("project_id", project_id);
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
				.id(SAVE_CALLBACK_ID)
				.build()
				.execute(myCallback);

	}

	private void delProject() {
		if (project_id == null) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("project_id", project_id);

		OkHttpUtils
				.post()
				.tag(this)
				.url(NetManager.PROJECT_DEL_URL)
				.params(params)
				.id(DEL_CALLBACK_ID)
				.build()
				.execute(myCallback);
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
	  
	  
	  private Callback<ResultCodeData> myCallback = new GenericsCallback<ResultCodeData>(
				new JsonGenericsSerializator()) {
			@Override
			public void onBefore(Request request, int id) {
				if (id == SAVE_CALLBACK_ID) {
					if (mSaveDialog != null)
						mSaveDialog.show();
				}else if (id == DEL_CALLBACK_ID) {
					if (mDelDialog != null)
						mDelDialog.show();
				}
				
			}

			@Override
			public void onAfter(int id) {
				if (id == SAVE_CALLBACK_ID) {
					if (mSaveDialog != null)
						mSaveDialog.dismiss();
				}else if (id == DEL_CALLBACK_ID) {
					if (mDelDialog != null)
						mDelDialog.dismiss();
				}
				
				
			}

			@Override
			public void onError(Call call, Exception e, int id) {
				e.printStackTrace();
				Log.e(TAG, "Save or Del Project onError:" + e.getMessage());
				Toast.makeText(EditProjectActivity.this,
						R.string.network_is_not_smooth, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(ResultCodeData resultCodeData, int id) {
				if (resultCodeData.getStatus().equals(
						NetManager.SUCCESS)) {
					if (id == SAVE_CALLBACK_ID) {
						if (mSaveSuccessDialog != null)
							mSaveSuccessDialog.show();
					}else if (id == DEL_CALLBACK_ID) {
						if (mDelSuccessDialog != null)
							mDelSuccessDialog.show();
					}
					
				} else {
					Toast.makeText(EditProjectActivity.this,
							resultCodeData.getMsg(), Toast.LENGTH_SHORT).show();
					if (id == SAVE_CALLBACK_ID) {
						if (mSaveFailDialog != null)
							mSaveFailDialog.show();
					}else if (id == DEL_CALLBACK_ID) {
						if (mDelFailDialog != null)
							mDelFailDialog.show();
					}
				}
			}
		};

}
