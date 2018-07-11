package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.shuorigf.streetlampapp.adapter.EditLampControlContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LampControlData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.ResultCodeData;
import com.shuorigf.streetlampapp.data.LampControlData.Data.LampControl;
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
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EditLampControlListActivity extends NavigationBarActivity
		implements OnClickListener {
	private static final String TAG = EditLampControlListActivity.class
			.getSimpleName();
	public static final int EDIT_PROJECT_LAMP_CONTROL = 0;
	public static final int EDIT_NETWORK_LAMP_CONTROL = 1;

	private ListView mLampControlContentLv;
	private EditLampControlContentAdapter mEditLampControlContentAdapter;
	private CheckBox mSelectAllChk;
	private Button mDeleteBtn;
//	private Button mControlBtn;

	private LoginData mLoginData;

	private int type;
	private String id;
	private String name;
	private boolean isFault;

	private List<LampControl> mLampControls;

	private AlertDialog mDialog;
	private Dialog mDelDialog;
	private Dialog mSuccessDialog;
	private Dialog mFailDialog;
	private Dialog mNoSelectDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		mDelDialog = DialogFactory.creatRequestDialog(this, R.string.deleting);
		mNoSelectDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.no_select_lamp_control);
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
		View view = View.inflate(this,
				R.layout.activity_edit_lamp_control_list, null);
		mLampControlContentLv = (ListView) view
				.findViewById(R.id.lv_edit_lamp_control_list_content);
		mSelectAllChk = (CheckBox) view
				.findViewById(R.id.chk_edit_lamp_control_list_select_all);
		mSelectAllChk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					selectedAll();
				} else {
					unSelectedAll();
				}
			}
		});
		mDeleteBtn = (Button) view
				.findViewById(R.id.btn_edit_lamp_control_list_delete);
		mDeleteBtn.setOnClickListener(this);
//		mControlBtn = (Button) view
//				.findViewById(R.id.btn_edit_lamp_control_list_control);
//		mControlBtn.setOnClickListener(this);
		setNavigationBarContentView(view);

	}

	private void initData() {
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		mEditLampControlContentAdapter = new EditLampControlContentAdapter(this);
		mLampControlContentLv.setAdapter(mEditLampControlContentAdapter);
		type = getIntent().getIntExtra("type", EDIT_PROJECT_LAMP_CONTROL);
		id = getIntent().getStringExtra("id");
		name = getIntent().getStringExtra("name");
		isFault = getIntent().getBooleanExtra("isFault", false);
		setTitleText(name);
		getLampControlData();
	}

	private void getLampControlData() {
		if (id == null) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		if (type == EDIT_PROJECT_LAMP_CONTROL) {
			params.put("project_id", id);
		} else {
			params.put("network_id", id);
		}
		if (isFault) {
			params.put("status", "1");
		}

		OkHttpUtils
				.post()
				.tag(this)
				.url(NetManager.LAMP_CONTROL_LIST_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<LampControlData>(
								new JsonGenericsSerializator()) {

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"LampControlData onError:"
												+ e.getMessage());
								Toast.makeText(
										EditLampControlListActivity.this,
										R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(
									LampControlData lampControlData, int id) {
								Log.i(TAG, "LampControlData onResponse:"
										+ lampControlData.toString());
								if (lampControlData.getStatus().equals(
										NetManager.SUCCESS)) {
									setLampControlData(lampControlData);
								} else {
									Toast.makeText(
											EditLampControlListActivity.this,
											lampControlData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});

	}

	private void setLampControlData(LampControlData lampControlData) {
		mLampControls = lampControlData.getData().getLamps();
		mEditLampControlContentAdapter.changeData(mLampControls);

	}

	public void selectedAll() {
		for (int i = 0; i < mEditLampControlContentAdapter.getCount(); i++) {
			mLampControlContentLv.setItemChecked(i, true);
		}
	}

	public void unSelectedAll() {
		mLampControlContentLv.clearChoices();
		mEditLampControlContentAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_edit_lamp_control_list_delete:
			if (mLampControlContentLv.getCheckedItemCount() > 0) {
				showDelDialog();
			}else {
				if(mNoSelectDialog!=null)
					mNoSelectDialog.show();
			}
			
			break;
//		case R.id.btn_edit_lamp_control_list_control:
//			 goControl();
//			break;
		default:
			break;
		}
	}
	
//	private void goControl() {
//		Intent intent = new Intent(this, ControlActivity.class);
//		startActivity(intent);
//	}

	private void showDelDialog() {
		if (mDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View view = View.inflate(this, R.layout.dialog_delete, null);
			((TextView) view.findViewById(R.id.tv_show_content))
					.setText(R.string.are_you_sure_you_want_to_delete_this_lamp_control);
			((Button) view.findViewById(R.id.btn_confirm))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							delLampControl();
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

	private void delLampControl() {
		if (mLampControlContentLv.getCheckedItemCount() > 0) {
			boolean isFirst=true;
			StringBuffer sb = new StringBuffer();
			SparseBooleanArray checkedArray = mLampControlContentLv
					.getCheckedItemPositions();
			for (int i = 0; i < checkedArray.size(); i++) {
				if (checkedArray.valueAt(i)) {
					if (isFirst) {
						sb.append(mLampControls.get(checkedArray.keyAt(i)).getId());
						isFirst = false;
					}else {
						sb.append(","
								+ mLampControls.get(checkedArray.keyAt(i)).getId());
					}
					
				}
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", mLoginData.getUsername());
			params.put("client_key", mLoginData.getClient_key());
			params.put("token", mLoginData.getData().getToken());
			params.put("lamp_ids", sb.toString());
			OkHttpUtils
					.post()
					.tag(this)
					.url(NetManager.LAMP_CONTROL_DEL_URL)
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
								public void onError(Call call, Exception e,
										int id) {
									e.printStackTrace();
									Log.e(TAG,
											"Del LampControl onError:"
													+ e.getMessage());
									Toast.makeText(
											EditLampControlListActivity.this,
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
										Toast.makeText(
												EditLampControlListActivity.this,
												resultCodeData.getMsg(),
												Toast.LENGTH_SHORT).show();
										if (mFailDialog != null)
											mFailDialog.show();
									}
								}
							});
		}
	}
	
	

}
