package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.NetworkDetailsData;
import com.shuorigf.streetlampapp.data.NetworkDetailsData.Data;
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
import android.widget.TextView;
import android.widget.Toast;

public class NetworkDetailsActivity extends NavigationBarActivity implements
		OnClickListener {

	private static final String TAG = NetworkDetailsActivity.class
			.getSimpleName();
	private static final int EDIT_NETWORK_REQUESTCODE = 1;

	private TextView mNetworkNumberTv;
	private TextView mNetworkNameTv;
	private TextView mNetworkstateTv;
	private TextView mSectionTv;
	private TextView mSIMNumberTv;
	private TextView mRegistrationPackageTv;
	private TextView mLongitudeTv;
	private TextView mLatitudeTv;

	private TextView mInstalledNumberTv;
	private TextView mCreateTimeTv;

	private Button mViewLampControlBtn;
	private Button mDeleteThisNetworkBtn;

	private LoginData mLoginData;
	private Data mData;

	private String network_id;
	private Bundle networkBundle;

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
		View view = View.inflate(this, R.layout.activity_network_details, null);
		mNetworkNumberTv = (TextView) view
				.findViewById(R.id.tv_network_details_network_number_value);
		mNetworkNameTv = (TextView) view
				.findViewById(R.id.tv_network_details_network_name_value);
		mNetworkstateTv = (TextView) view
				.findViewById(R.id.tv_network_details_network_state_value);
		mSectionTv = (TextView) view
				.findViewById(R.id.tv_network_details_section_value);
		mSIMNumberTv = (TextView) view
				.findViewById(R.id.tv_network_details_sim_number_value);
		mRegistrationPackageTv = (TextView) view
				.findViewById(R.id.tv_network_details_registration_package_value);
		mLongitudeTv = (TextView) view
				.findViewById(R.id.tv_network_details_longitude_value);
		mLatitudeTv = (TextView) view
				.findViewById(R.id.tv_network_details_latitude_value);

		mInstalledNumberTv = (TextView) view
				.findViewById(R.id.tv_network_details_installed_number_value);
		mCreateTimeTv = (TextView) view
				.findViewById(R.id.tv_network_details_create_time_value);
		mViewLampControlBtn = (Button) view
				.findViewById(R.id.btn_network_details_view_lamp_control);
		mViewLampControlBtn.setOnClickListener(this);
		mDeleteThisNetworkBtn = (Button) view
				.findViewById(R.id.btn_network_details_delete_this_network);
		mDeleteThisNetworkBtn.setOnClickListener(this);
		setNavigationBarContentView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		mRightIBtn.setVisibility(View.VISIBLE);
		mRightIBtn.setImageResource(R.drawable.ic_edit);
		mRightIBtn.setOnClickListener(this);
		networkBundle = getIntent().getExtras();
		network_id = networkBundle.getString("network_id");
		setTitleText(networkBundle.getString("network_name"));
		getNetworkDetailsData();
	}

	private void getNetworkDetailsData() {
		if (network_id == null) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("network_id", network_id);
		OkHttpUtils
				.post()
				.tag(this)
				.url(NetManager.NETWORK_DETAILS_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<NetworkDetailsData>(
								new JsonGenericsSerializator()) {

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"NetworkDetailsData onError:"
												+ e.getMessage());
								Toast.makeText(NetworkDetailsActivity.this,
										R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(
									NetworkDetailsData networkDetailsData,
									int id) {
								Log.i(TAG, "NetworkDetailsData onResponse:"
										+ networkDetailsData.toString());
								if (networkDetailsData.getStatus().equals(
										NetManager.SUCCESS)) {
									setNetworkDetailsData(networkDetailsData);

								} else {
									Toast.makeText(NetworkDetailsActivity.this,
											networkDetailsData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});
	}

	private void setNetworkDetailsData(NetworkDetailsData networkDetailsData) {
		mData = networkDetailsData.getData();
		mNetworkNumberTv.setText(mData.getNetwork_no());
		mNetworkNameTv.setText(mData.getNetwork_name());
		mNetworkstateTv.setText(mData.getStatus() == 0 ? R.string.not_connected
				: R.string.connected);
		mSectionTv.setText(mData.getSection());
		mSIMNumberTv.setText(mData.getSimcard());
		mRegistrationPackageTv.setText(mData.getRegpack());
		mLongitudeTv.setText(mData.getLongitude() + "°E");
		mLatitudeTv.setText(mData.getLatitude() + "°N");
		mInstalledNumberTv.setText(mData.getLampcount() + "");
		if (mData.getCreatetime() != null) {
			String[] time = mData.getCreatetime().split(" ");
			if (time.length > 0) {
				mCreateTimeTv.setText(time[0]);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_network_details_view_lamp_control:
			goLampControlList();
			break;
		case R.id.btn_network_details_delete_this_network:
			showDelDialog();
			break;
		case R.id.imgbtn_navigation_bar_right:
			goEditNetwork();
			break;
		default:
			break;
		}
	}

	private void showDelDialog() {
		if (mDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View view = View.inflate(this, R.layout.dialog_delete, null);
			((TextView) view.findViewById(R.id.tv_show_content))
					.setText(R.string.are_you_sure_you_want_to_delete_this_network);
			((Button) view.findViewById(R.id.btn_confirm))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							delNetwork();
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

	private void goEditNetwork() {
		Intent intent = new Intent(this, EditNetworkActivity.class);
		Bundle bundle = new Bundle(networkBundle);
		bundle.putSerializable("data", mData);
		intent.putExtras(bundle);
		startActivityForResult(intent, EDIT_NETWORK_REQUESTCODE);
	}

	private void goLampControlList() {
		Intent intent = new Intent(this, LampControlListActivity.class);
		intent.putExtras(networkBundle);
		startActivity(intent);
	}

	private void delNetwork() {
		if (network_id == null) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("network_ids", network_id);
		OkHttpUtils
				.post()
				.tag(this)
				.url(NetManager.NETWORK_DEL_URL)
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
								Toast.makeText(NetworkDetailsActivity.this,
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
									Toast.makeText(NetworkDetailsActivity.this,
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
		case EDIT_NETWORK_REQUESTCODE:
			if (resultCode == RESULT_OK) {
				if (data != null) {
					if (data.getBooleanExtra("isDel", false)) {
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					} else {
						getNetworkDetailsData();
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
