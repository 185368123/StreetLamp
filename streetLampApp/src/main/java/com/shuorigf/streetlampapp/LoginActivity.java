package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.GlobalStaticFun;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.util.SharePreferenceUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	private static final String TAG = LoginActivity.class
			.getSimpleName();
	private static final String PREF_IS_REMEMBER_PASSWORD = "is_remember_password";
	private static final String PREF_IS_AUTOMATIC_LOGIN = "is_automatic_login";
	private static final String PREF_LOGIN_ACCOUNT = "login_account";
	private static final String PREF_LOGIN_PASSWORD = "login_password";
	private EditText edt_login_password;
	private EditText edt_login_account;
	private ImageButton mClearIBtn;
	private ImageButton mShowPasswordIBtn;
	private Button btn_login_login;
	private CheckBox mRememberPasswordChk;
	private CheckBox mAtomaticLoginChk;

	private Dialog mDialog;
	
	private boolean isShowPassword;
	private boolean isExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		initView();
		initData();
	}

	private void initView() {
		mDialog = DialogFactory.creatRequestDialog(this, R.string.logging_in);
		mDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				OkHttpUtils.getInstance().cancelTag(LoginActivity.this);
			}
		});
		edt_login_password = (EditText) findViewById(R.id.edt_login_password);
		edt_login_account = (EditText) findViewById(R.id.edt_login_account);
		btn_login_login = (Button) findViewById(R.id.btn_login_login);
		btn_login_login.setOnClickListener(this);
		mClearIBtn =  (ImageButton) findViewById(R.id.imgbtn_login_clear);
		mClearIBtn.setOnClickListener(this);
		mShowPasswordIBtn =  (ImageButton) findViewById(R.id.imgbtn_login_show_password);
		mShowPasswordIBtn.setOnClickListener(this);
		mRememberPasswordChk = (CheckBox) findViewById(R.id.chk_login_remember_password);
		mAtomaticLoginChk = (CheckBox) findViewById(R.id.chk_login_automatic_logon);
		mRememberPasswordChk
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (!isChecked) {
							mAtomaticLoginChk.setChecked(false);
						}
					}
				});

		mAtomaticLoginChk
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mRememberPasswordChk.setChecked(true);
						}

					}
				});
	}

	private void initData() {
		isExit = getIntent().getBooleanExtra("is_exit", false);
		edt_login_account.setText(SharePreferenceUtils.getString(this,
				PREF_LOGIN_ACCOUNT, null));
		if (SharePreferenceUtils.getBoolean(this, PREF_IS_REMEMBER_PASSWORD,
				false)) {
			mRememberPasswordChk.setChecked(true);
			edt_login_password.setText(SharePreferenceUtils.getString(this,
					PREF_LOGIN_PASSWORD, null));
			if (SharePreferenceUtils.getBoolean(this, PREF_IS_AUTOMATIC_LOGIN,
					false)) {
				mAtomaticLoginChk.setChecked(true);
				if (!isExit){
					startLogin();
				}
				
			}

		}

	}

	private void startLogin() {
		String user = edt_login_account.getText().toString();
		String pwd = edt_login_password.getText().toString();
		String imei = GlobalStaticFun.getPhoneImei();

		if (TextUtils.isEmpty(imei)) {
			Toast.makeText(this, R.string.imei_is_null,
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)) {
			Toast.makeText(this,
					R.string.user_name_or_password_not_null, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", user);
		params.put("password", pwd);
		params.put("client_key", imei);
		OkHttpUtils
				.post()
				.tag(this)
				.url(NetManager.LOGIN_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<LoginData>(
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
										"LoginData onError:" + e.getMessage());
								if (e.toString().contains("closed")) {
									return;
								}
								Toast.makeText(LoginActivity.this, R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(LoginData loginData, int id) {
								Log.i(TAG,
										"LoginData onResponse:"
												+ loginData.toString());
								loginDataParser(loginData);
							}
						});
	}

	private void loginDataParser(LoginData loginData) {
		if (loginData.getStatus().equals(NetManager.SUCCESS)) {
			SharePreferenceUtils.putString(LoginActivity.this,
					PREF_LOGIN_ACCOUNT, edt_login_account.getText().toString());
			if (mRememberPasswordChk.isChecked()) {
				SharePreferenceUtils.putBoolean(LoginActivity.this,
						PREF_IS_REMEMBER_PASSWORD, true);
				SharePreferenceUtils.putString(LoginActivity.this,
						PREF_LOGIN_PASSWORD, edt_login_password.getText()
								.toString());
			} else {
				SharePreferenceUtils.putBoolean(LoginActivity.this,
						PREF_IS_REMEMBER_PASSWORD, false);
			}
			SharePreferenceUtils.putBoolean(LoginActivity.this,
					PREF_IS_AUTOMATIC_LOGIN, mAtomaticLoginChk.isChecked());
			((StreetlampApp) getApplication()).mLoginData = loginData;
			((StreetlampApp) getApplication()).mLoginData
					.setUsername(edt_login_account.getText().toString());
			((StreetlampApp) getApplication()).mLoginData
					.setClient_key(GlobalStaticFun.getPhoneImei());

			Toast.makeText(LoginActivity.this, R.string.login_success,
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, MainActivity.class);
			LoginActivity.this.startActivity(intent);
			LoginActivity.this.finish();
		} else {
			Toast.makeText(LoginActivity.this, loginData.getMsg(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_login_login:
			startLogin();
			break;
		case R.id.imgbtn_login_clear:
			edt_login_account.setText(null);
			break;
		case R.id.imgbtn_login_show_password:
			if (isShowPassword) {
				isShowPassword= false;
				edt_login_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
			}else {
				isShowPassword= true;
				edt_login_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			break;
			
		default:
			break;
		}
	}
	
	 @Override
		public void onDestroy() {
			super.onDestroy();
			OkHttpUtils.getInstance().cancelTag(this);
		}

}
