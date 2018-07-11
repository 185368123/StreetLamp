package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.ProjectDetailsData;
import com.shuorigf.streetlampapp.data.ProjectDetailsData.Data;
import com.shuorigf.streetlampapp.data.ResultCodeData;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectDetailsActivity extends NavigationBarActivity implements
		OnClickListener {
	private static final String TAG = ProjectDetailsActivity.class
			.getSimpleName();
	private static final int EDIT_PROJECT_REQUESTCODE = 1;
	private TextView mInspectionCountDownTv;
	private ImageButton mInspectionSettingIBtn;

	private TextView mProjectNameTv;
	private TextView mProjectNumberTv;
	private TextView mCustomerNameTv;
	private TextView mCompanyNameTv;
	private TextView mProvinceInTv;
	private TextView mDetailedAddressTv;

	private TextView mInstalledNumberTv;
	private TextView mGatewayNumberTv;

	private Button mViewNetworkBtn;
	private Button mDeleteThisProjectBtn;

	private LoginData mLoginData;
	private Data mData;

	private String project_id;
	// private String project_name;
	private Bundle projectBundle;

	private AlertDialog mDialog;
	private Dialog mDelDialog;
	private Dialog mSuccessDialog;
	private Dialog mFailDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		mDelDialog = DialogFactory.creatRequestDialog(this, R.string.deleting);
		mSuccessDialog = DialogFactory.creatResultDialog(this,
				R.drawable.ic_success, R.string.delete_success);
		mFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail,
				R.string.delete_failed);
		mSuccessDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		View view = View.inflate(this, R.layout.activity_project_details, null);
		mInspectionCountDownTv = (TextView) view
				.findViewById(R.id.tv_project_details_inspection_count_down_value);
		mInspectionSettingIBtn = (ImageButton) view
				.findViewById(R.id.imgbtn_project_details_inspection_setting);
		mInspectionSettingIBtn.setOnClickListener(this);
		mProjectNameTv = (TextView) view
				.findViewById(R.id.tv_project_details_project_name_value);
		mProjectNumberTv = (TextView) view
				.findViewById(R.id.tv_project_details_project_number_value);
		mCustomerNameTv = (TextView) view
				.findViewById(R.id.tv_project_details_customer_name_value);
		mCompanyNameTv = (TextView) view
				.findViewById(R.id.tv_project_details_company_name_value);
		mProvinceInTv = (TextView) view
				.findViewById(R.id.tv_project_details_province_in_value);
		mDetailedAddressTv = (TextView) view
				.findViewById(R.id.tv_project_details_detailed_address_value);

		mInstalledNumberTv = (TextView) view
				.findViewById(R.id.tv_project_details_installed_number_value);
		mGatewayNumberTv = (TextView) view
				.findViewById(R.id.tv_project_details_gateway_number_value);
		mViewNetworkBtn = (Button) view
				.findViewById(R.id.btn_project_details_view_network);
		mViewNetworkBtn.setOnClickListener(this);
		mDeleteThisProjectBtn = (Button) view
				.findViewById(R.id.btn_project_details_delete_this_project);
		mDeleteThisProjectBtn.setOnClickListener(this);
		setNavigationBarContentView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		mRightIBtn.setVisibility(View.VISIBLE);
		mRightIBtn.setImageResource(R.drawable.ic_edit);
		mRightIBtn.setOnClickListener(this);
		projectBundle = getIntent().getExtras();
		project_id = projectBundle.getString("project_id");
		setTitleText(projectBundle.getString("project_name"));
		getProjectDetailsData();

	}

	private void getProjectDetailsData() {
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
				.url(NetManager.PROJECT_DETAILS_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<ProjectDetailsData>(
								new JsonGenericsSerializator()) {

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"ProjectDetailsData onError:"
												+ e.getMessage());
								Toast.makeText(ProjectDetailsActivity.this,
										R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(
									ProjectDetailsData projectDetailsData,
									int id) {
								Log.i(TAG, "ProjectDetailsData onResponse:"
										+ projectDetailsData.toString());
								if (projectDetailsData.getStatus().equals(
										NetManager.SUCCESS)) {
									setProjectDetailsData(projectDetailsData);
								} else {
									Toast.makeText(ProjectDetailsActivity.this,
											projectDetailsData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});
	}

	private void setProjectDetailsData(ProjectDetailsData projectDetailsData) {
		mData = projectDetailsData.getData();
		mInspectionCountDownTv.setText(mData.getPatrol_time()
				+ getString(R.string.unit_inspection));
		mProjectNameTv.setText(mData.getProject_name());
		mProjectNumberTv.setText(mData.getProject_no());
		mCustomerNameTv.setText(mData.getCustomer());
		mCompanyNameTv.setText(mData.getCompany());
		mProvinceInTv.setText(mData.getProvince());
		mDetailedAddressTv.setText(mData.getAddress());
		mInstalledNumberTv.setText(mData.getInstall_num() + "");
		mGatewayNumberTv.setText(mData.getNetwork_num() + "");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_project_details_inspection_setting:
			goInspectionSetting();
			break;
		case R.id.btn_project_details_view_network:
			goNetworkList();
			break;
		case R.id.btn_project_details_delete_this_project:
			showDelDialog();
			break;
		case R.id.imgbtn_navigation_bar_right:
			goEditProject();
			break;
		default:
			break;
		}
	}

	private void goInspectionSetting() {
		Intent intent = new Intent(this, InspectionSettingActivity.class);
		intent.putExtras(projectBundle);
		this.startActivity(intent);
	}

	private void goEditProject() {
		Intent intent = new Intent(this, EditProjectActivity.class);
		Bundle bundle = new Bundle(projectBundle);
		bundle.putSerializable("data", mData);
		intent.putExtras(bundle);
		this.startActivityForResult(intent, EDIT_PROJECT_REQUESTCODE);
	}

	private void goNetworkList() {
		Intent intent = new Intent(this, NetworkListActivity.class);
		intent.putExtras(projectBundle);
		this.startActivity(intent);
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
				.build()
				.execute(
						new GenericsCallback<ResultCodeData>(
								new JsonGenericsSerializator()) {
							@Override
							public void onBefore(Request request, int id) {
								if (mDelDialog != null)
									mDelDialog.show();
							}

							@Override
							public void onAfter(int id) {
								if (mDelDialog != null)
									mDelDialog.dismiss();
							}

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"Del Project onError:" + e.getMessage());
								Toast.makeText(ProjectDetailsActivity.this,
										R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(
									ResultCodeData resultCodeData, int id) {
								if (resultCodeData.getStatus().equals(
										NetManager.SUCCESS)) {
									if (mSuccessDialog != null)
										mSuccessDialog.show();
								} else {
									Toast.makeText(ProjectDetailsActivity.this,
											resultCodeData.getMsg(),
											Toast.LENGTH_SHORT).show();
									if (mFailDialog != null)
										mFailDialog.show();
								}
							}
						});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case EDIT_PROJECT_REQUESTCODE:
			if (resultCode == RESULT_OK) {
				if (data != null) {
					if (data.getBooleanExtra("isDel", false)) {
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					} else {
						getProjectDetailsData();
					}
				}

			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
	}

}
