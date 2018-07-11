package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.shuorigf.streetlampapp.adapter.LampControlContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LampControlData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.LampControlData.Data.LampControl;
import com.shuorigf.streetlampapp.network.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class LampControlListActivity extends NavigationBarActivity implements OnClickListener{
	private static final String TAG = LampControlListActivity.class
			.getSimpleName();
	private static final int ALL_CALLBACK_ID = 100;
	private static final int FAULT_CALLBACK_ID = 101;
	private static final int SEARCH_CALLBACK_ID = 102;
	private static final int EDIT_NETWORT_LAMP_CONTROL_REQUESTCODE = 1;

	private RadioGroup mTabRGrp;
	private EditText mLampControlSearchEdt;
	private ViewGroup mSearchEmptyView;
	private ListView mSearchResultLv;
	private ViewFlipper mViewFlipper;

	private ListView mFaultContentLv;
	private ListView mAllContentLv;

	private LoginData mLoginData;

	private LampControlContentAdapter mFaultLampControlContentAdapter;
	private LampControlContentAdapter mAllLampControlContentAdapter;
	private LampControlContentAdapter mSearchLampControlContentAdapter;
	private  List<LampControl> mFaultLampControls;
	private  List<LampControl> mAllLampControls;
	
	private String network_id;
	private String network_name;
	private Bundle networkBundle;
	
	private boolean isFault;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		View view = View.inflate(this, R.layout.activity_network_lamp_control_list, null);
		mTabRGrp = (RadioGroup) view.findViewById(R.id.rgrp_network_lamp_control_list_tab);
		mTabRGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbtn_network_lamp_control_list_all:
					mViewFlipper.setDisplayedChild(0);
					mLampControlSearchEdt.setText(null);
					isFault = false;
					break;
				case R.id.rbtn_network_lamp_control_list_fault:
					mViewFlipper.setDisplayedChild(1);
					mLampControlSearchEdt.setText(null);
					isFault = true;
					break;

				default:
					break;
				}
			}
		});
		mSearchEmptyView = (ViewGroup) view.findViewById(R.id.llyt_network_lamp_control_list_search_empty);
		mLampControlSearchEdt = (EditText) view.findViewById(R.id.edt_network_lamp_control_list_search);
		mLampControlSearchEdt.addTextChangedListener(new TextWatcher() {
	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {}

	            @Override
	            public void afterTextChanged(Editable s) {
	                String keyword = s.toString();
	                if (TextUtils.isEmpty(keyword)) {
	                	mSearchEmptyView.setVisibility(View.GONE);
	                	mSearchResultLv.setVisibility(View.GONE);
	                } else {
	                	getSearchResult(keyword);

	                }
	            }
	        });
		mSearchResultLv = (ListView) view.findViewById(R.id.lv_network_lamp_control_list_search_result);
		mSearchResultLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle lamp = new Bundle(networkBundle);
				lamp.putString("lamp_id", mSearchLampControlContentAdapter.getItem(position).getId());
				lamp.putString("lamp_numble", mSearchLampControlContentAdapter.getItem(position).getNumber());
				goLampControlDetails(lamp);
			}
		});
		mViewFlipper = (ViewFlipper) view.findViewById(R.id.vf_network_lamp_control_list);
		initAllViewFlipper();
		initFaultViewFlipper();
		
		setNavigationBarContentView(view);
		
	}

	private void initAllViewFlipper() {
		View view = View.inflate(this, R.layout.viewflipper_fault_lamp_control, null);
		mAllContentLv = (ListView) view.findViewById(R.id.lv_fault_lamp_control_list_content);
		mAllContentLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle lamp = new Bundle(networkBundle);
				lamp.putString("lamp_id", mAllLampControlContentAdapter.getItem(position).getId());
				lamp.putString("lamp_numble", mAllLampControlContentAdapter.getItem(position).getNumber());
				goLampControlDetails(lamp);
			}
			
		});
		mViewFlipper.addView(view);
	
	}

	private void initFaultViewFlipper() {
		View view = View.inflate(this, R.layout.viewflipper_fault_lamp_control, null);
		mFaultContentLv = (ListView) view.findViewById(R.id.lv_fault_lamp_control_list_content);
		mFaultContentLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle(networkBundle);
				bundle.putString("lamp_id", mFaultLampControlContentAdapter.getItem(position).getId());
				bundle.putString("lamp_numble", mFaultLampControlContentAdapter.getItem(position).getNumber());
				goLampControlDetails(bundle);
			}
			
		});
		mViewFlipper.addView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		mRightIBtn.setVisibility(View.VISIBLE);
		mRightIBtn.setImageResource(R.drawable.ic_edit);
		mRightIBtn.setOnClickListener(this);
		mAllLampControlContentAdapter = new LampControlContentAdapter(this);
		mAllContentLv.setAdapter(mAllLampControlContentAdapter);
		mFaultLampControlContentAdapter = new LampControlContentAdapter(this);
		mFaultContentLv.setAdapter(mFaultLampControlContentAdapter);
		mSearchLampControlContentAdapter = new LampControlContentAdapter(this);
		mSearchResultLv.setAdapter(mSearchLampControlContentAdapter);
		networkBundle = getIntent().getExtras();
		network_id = networkBundle.getString("network_id");
		network_name = networkBundle.getString("network_name");
		setTitleText(network_name);
		getLampControlData();

	}
	
	private void getLampControlData(){
		if (network_id==null) {
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
                .url(NetManager.LAMP_CONTROL_LIST_URL)
                .params(params)
                .id(ALL_CALLBACK_ID)
                .build()
                .execute(myCallback);
        
    	params.put("status", "1");
		
		 OkHttpUtils
        .post()
        .tag(this)
        .url(NetManager.LAMP_CONTROL_LIST_URL)
        .params(params)
        .id(FAULT_CALLBACK_ID)
        .build()
        .execute(myCallback);
        
        
	}
	 private void getSearchResult(String keyword) {
		 if (network_id==null) {
				return;
			}
			Map<String, String> params = new HashMap<String, String>();
	        params.put("username", mLoginData.getUsername());
	        params.put("client_key", mLoginData.getClient_key());
	        params.put("token", mLoginData.getData().getToken());
	       	params.put("network_id", network_id);
	       	if (isFault) {
	       		params.put("status", "1");
			}
	       	params.put("keyword", keyword);
	        OkHttpUtils
	                .post()
	                .tag(this)
	                .url(NetManager.LAMP_CONTROL_LIST_URL)
	                .params(params)
	                .id(SEARCH_CALLBACK_ID)
	                .build()
	                .execute(myCallback);
	 }
	
	
	private void setAllLampControlData(LampControlData lampControlData) {
		mAllLampControls = lampControlData.getData().getLamps();
		mAllLampControlContentAdapter.changeData(mAllLampControls);
		
	}
	
	private void setFaultLampControlData(LampControlData lampControlData) {
		mFaultLampControls = lampControlData.getData().getLamps();
		mFaultLampControlContentAdapter.changeData(mFaultLampControls);
		
	}
	
	private void setSearchLampControlData(LampControlData lampControlData) {
		mSearchResultLv.setVisibility(View.VISIBLE);
		List<LampControl> lampControls = lampControlData.getData().getLamps();
        if (lampControls == null || lampControls.size() == 0) {
        	mSearchEmptyView.setVisibility(View.VISIBLE);
        } else {
        	mSearchEmptyView.setVisibility(View.GONE);
        	mSearchLampControlContentAdapter.changeData(lampControls);
        }
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_navigation_bar_right:
			goEditLampControlList();
			break;
			
		default:
			break;
		}
	}
	
	private void goLampControlDetails(Bundle lamp) {
		Intent intent = new Intent(this,LampControlDetailsActivity.class);
		intent.putExtras(lamp);
		startActivity(intent);
	}
	
	private void goEditLampControlList() {
		Intent intent = new Intent(this, EditLampControlListActivity.class);
		intent.putExtra("type", EditLampControlListActivity.EDIT_NETWORK_LAMP_CONTROL);
		intent.putExtra("id", network_id);
		intent.putExtra("name", network_name);
		intent.putExtra("isFault", isFault);
		startActivityForResult(intent, EDIT_NETWORT_LAMP_CONTROL_REQUESTCODE);
	}
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode) {
	        case EDIT_NETWORT_LAMP_CONTROL_REQUESTCODE:
	            if (resultCode == RESULT_OK) {
	            	getLampControlData();
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

 private Callback<LampControlData> myCallback = new GenericsCallback<LampControlData>(new JsonGenericsSerializator())
     {

	 @Override
     public void onError(Call call, Exception e, int id)
     {
   	  e.printStackTrace();
   	  Log.e(TAG, "LampControlData onError:"+e.getMessage());
   	  Toast.makeText(LampControlListActivity.this, R.string.network_is_not_smooth,
					Toast.LENGTH_SHORT).show();
     }
	@Override
	public void onResponse(LampControlData lampControlData, int id) {
		Log.i(TAG, "LampControlData onResponse:"+lampControlData.toString());
		if(lampControlData.getStatus().equals(NetManager.SUCCESS)){
			switch (id) {
			case ALL_CALLBACK_ID:
				setAllLampControlData(lampControlData);
				break;
			case FAULT_CALLBACK_ID:
				setFaultLampControlData(lampControlData);
				break;
			case SEARCH_CALLBACK_ID:
				setSearchLampControlData(lampControlData);
				break;
			}
	 }else {
		 Toast.makeText(LampControlListActivity.this, lampControlData.getMsg(),
					Toast.LENGTH_SHORT).show();
	 }
	}	
 };
	 
}
