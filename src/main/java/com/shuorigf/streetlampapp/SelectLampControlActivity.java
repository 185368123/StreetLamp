package com.shuorigf.streetlampapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.shuorigf.streetlampapp.adapter.SelectLampControlContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LampControlData;
import com.shuorigf.streetlampapp.data.LampControlData.Data.LampControl;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.network.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class SelectLampControlActivity extends NavigationBarActivity implements
		OnClickListener {
	private static final String TAG = SelectLampControlActivity.class
			.getSimpleName();
//	private static final String PREF_PROJECT_LAMP_CONTROL_LIST = "project_report_lamp_control_list";
	private static final int ALL_CALLBACK_ID = 100;
	private static final int FAULT_CALLBACK_ID = 101;
	private static final int SEARCH_CALLBACK_ID = 102;
	private static final int SELECTED_LAMP_CONTROL_REQUESTCODE = 1;
	
	private static final int MAX_ITEM = 4;
	
	private RadioGroup mTabRGrp;
	private EditText mLampControlSearchEdt;
	private ViewGroup mSearchEmptyView;
	private ListView mSearchResultLv;
	private ViewFlipper mViewFlipper;

	private ListView mFaultContentLv;
	private ListView mAllContentLv;


	private SelectLampControlContentAdapter mAllSelectLampControlContentAdapter;
	private SelectLampControlContentAdapter mFaultSelectLampControlContentAdapter;
	private SelectLampControlContentAdapter mSearchSelectLampControlContentAdapter;
	private  List<LampControl> mAllLampControls;
	private  List<LampControl> mFaultLampControls;
	

	private LoginData mLoginData;
	
	private String project_id;
//	private Bundle projectBundle;
	
	private boolean isFault;
	
//	private boolean isFilter;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		View view = View.inflate(this, R.layout.activity_select_lamp_control, null);
		mTabRGrp = (RadioGroup) view.findViewById(R.id.rgrp_select_lamp_control_tab);
		mTabRGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbtn_select_lamp_control_all:
					mViewFlipper.setDisplayedChild(0);
					mLampControlSearchEdt.setText(null);
					mAllContentLv.clearChoices();
					mAllSelectLampControlContentAdapter.notifyDataSetChanged();
					isFault = false;
					break;
				case R.id.rbtn_select_lamp_control_fault:
					mViewFlipper.setDisplayedChild(1);
					mLampControlSearchEdt.setText(null);
					mFaultContentLv.clearChoices();
					mFaultSelectLampControlContentAdapter.notifyDataSetChanged();
					isFault = true;
					break;

				default:
					break;
				}
			}
		});
		
		mSearchEmptyView = (ViewGroup) view.findViewById(R.id.llyt_select_lamp_control_search_empty);
		mLampControlSearchEdt = (EditText) view.findViewById(R.id.edt_select_lamp_control_search);
		mLampControlSearchEdt.addTextChangedListener(new TextWatcher() {
	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {}

	            @Override
	            public void afterTextChanged(Editable s) {
	            	mSearchResultLv.clearChoices();
	            	mAllContentLv.clearChoices();
	            	mFaultContentLv.clearChoices();
	                String keyword = s.toString();
	                if (TextUtils.isEmpty(keyword)) {
	                	mSearchEmptyView.setVisibility(View.GONE);
	                	mSearchResultLv.setVisibility(View.GONE);
	                } else {
	                	getSearchResult(keyword);
	                }
	            }
	        });
		mSearchResultLv = (ListView) view.findViewById(R.id.lv_select_lamp_control_search_result);
		mSearchResultLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mSearchResultLv.getCheckedItemCount()>MAX_ITEM) {
					mSearchResultLv.setItemChecked(position,false);
					Toast.makeText(SelectLampControlActivity.this, R.string.most_choose_four,
							Toast.LENGTH_SHORT).show();
				}else {
					mSearchSelectLampControlContentAdapter.notifyDataSetChanged();
				}
				
			}
		});
		mViewFlipper = (ViewFlipper) view.findViewById(R.id.vf_select_lamp_control);
		initAllViewFlipper();
		initFaultViewFlipper();
		
		setNavigationBarContentView(view);
	}
	
	
	private void initAllViewFlipper() {
		View view = View.inflate(this, R.layout.viewflipper_selected_lamp_control, null);
		mAllContentLv = (ListView) view.findViewById(R.id.lv_lamp_control_list_content);
		mAllContentLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mAllContentLv.getCheckedItemCount()>MAX_ITEM) {
					mAllContentLv.setItemChecked(position,false);
					Toast.makeText(SelectLampControlActivity.this, R.string.most_choose_four,
							Toast.LENGTH_SHORT).show();
				}else {
					mAllSelectLampControlContentAdapter.notifyDataSetChanged();
				}
			}
			
		});
		mViewFlipper.addView(view);
	
	}

	private void initFaultViewFlipper() {
		View view = View.inflate(this, R.layout.viewflipper_selected_lamp_control, null);
		mFaultContentLv = (ListView) view.findViewById(R.id.lv_lamp_control_list_content);
		mFaultContentLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mFaultContentLv.getCheckedItemCount()>MAX_ITEM) {
					mFaultContentLv.setItemChecked(position,false);
					Toast.makeText(SelectLampControlActivity.this, R.string.most_choose_four,
							Toast.LENGTH_SHORT).show();
				}else {
					mFaultSelectLampControlContentAdapter.notifyDataSetChanged();
				}
			}
			
		});
		mViewFlipper.addView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		setTitleText(R.string.selected_lamp_control);
		mRightTV.setVisibility(View.VISIBLE);
		mRightTV.setText(R.string.selected);
		mRightTV.setTextColor(getResources().getColor(R.color.blue));
		mRightTV.setOnClickListener(this);
		mAllSelectLampControlContentAdapter = new SelectLampControlContentAdapter(this, mAllContentLv);
		mAllContentLv.setAdapter(mAllSelectLampControlContentAdapter);
		mFaultSelectLampControlContentAdapter = new SelectLampControlContentAdapter(this, mFaultContentLv);
		mFaultContentLv.setAdapter(mFaultSelectLampControlContentAdapter);
		mSearchSelectLampControlContentAdapter = new SelectLampControlContentAdapter(this, mSearchResultLv);
		mSearchResultLv.setAdapter(mSearchSelectLampControlContentAdapter);
		
//		projectBundle = getIntent().getExtras();
	    project_id = getIntent().getStringExtra("project_id");
//	    isFilter = projectBundle.getBoolean("isFilter", false);
	    getLampControlData();
	}
	
	
	private void getLampControlData(){
		if (project_id==null) {
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
	
	
	private void setAllLampControlData(LampControlData lampControlData) {
		mAllLampControls = lampControlData.getData().getLamps();
		mAllSelectLampControlContentAdapter.changeData(mAllLampControls);
//		if (isFilter) {
//			List<LampControlReportData> lampControlReports = SharePreferenceUtils.getDataList(this,
//					PREF_PROJECT_LAMP_CONTROL_LIST, null);
//			if (lampControlReports!=null&&lampControlReports.size()>0) {
//				for (LampControlReportData lampControlReportData : lampControlReports) {
//					for (int i = 0; i < mAllLampControls.size(); i++) {
//						if (mAllLampControls.get(i).getId().equals(lampControlReportData.getId())) {
//							mAllContentLv.setItemChecked(i, true);
//						}
//					}
//				}
//				mAllSelectLampControlContentAdapter.notifyDataSetChanged();
//			}
//		}
	}
	
	private void setFaultLampControlData(LampControlData lampControlData) {
		mFaultLampControls = lampControlData.getData().getLamps();
		mFaultSelectLampControlContentAdapter.changeData(mFaultLampControls);
	}
	
	
	 private void getSearchResult(String keyword) {
		 if (project_id==null) {
				return;
			}
			Map<String, String> params = new HashMap<String, String>();
	        params.put("username", mLoginData.getUsername());
	        params.put("client_key", mLoginData.getClient_key());
	        params.put("token", mLoginData.getData().getToken());
	       	params.put("project_id", project_id);
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
	 
	 
		private void setSearchLampControlData(LampControlData lampControlData) {
			mSearchResultLv.setVisibility(View.VISIBLE);
			List<LampControl> lampControls = lampControlData.getData().getLamps();
			if (lampControls == null || lampControls.size() == 0) {
	        	mSearchEmptyView.setVisibility(View.VISIBLE);
	        	mSearchSelectLampControlContentAdapter.changeData(lampControls);
	        } else {
	        	mSearchEmptyView.setVisibility(View.GONE);
	        	mSearchSelectLampControlContentAdapter.changeData(lampControls);
	        }
			
		}
	


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_navigation_bar_right:
			goSelectedLampControl();
			break;
		default:
			break;
		}
	}

	private void goSelectedLampControl() {
		List<LampControl> lampControls = new ArrayList<LampControl>();
		if (mSearchSelectLampControlContentAdapter.getListData()!=null&&mSearchSelectLampControlContentAdapter.getListData().size()>0) {
			if (mSearchResultLv.getCheckedItemCount() > 0) {
			    
				SparseBooleanArray checkedArray = mSearchResultLv
						.getCheckedItemPositions();
				for (int i = 0; i < checkedArray.size(); i++) {
					if (checkedArray.valueAt(i)) {
						lampControls.add(mSearchSelectLampControlContentAdapter.getListData().get(checkedArray.keyAt(i)));
					}
				}
			}
		}else {
			if (isFault) {
				if (mFaultContentLv.getCheckedItemCount() > 0) {
				    
					SparseBooleanArray checkedArray = mFaultContentLv
							.getCheckedItemPositions();
					for (int i = 0; i < checkedArray.size(); i++) {
						if (checkedArray.valueAt(i)) {
							lampControls.add(mFaultLampControls.get(checkedArray.keyAt(i)));
						}
					}
				}
			}else {
				if (mAllContentLv.getCheckedItemCount() > 0) {
				    
					SparseBooleanArray checkedArray = mAllContentLv
							.getCheckedItemPositions();
					for (int i = 0; i < checkedArray.size(); i++) {
						if (checkedArray.valueAt(i)) {
							lampControls.add(mAllLampControls.get(checkedArray.keyAt(i)));
						}
					}
			}
		}
	}
		
		if (lampControls.size()>0) {
			Intent intent = new Intent(this, SelectedLampControlActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("list", (Serializable) lampControls);
			intent.putExtras(bundle);
			startActivityForResult(intent, SELECTED_LAMP_CONTROL_REQUESTCODE);
		}

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
        case SELECTED_LAMP_CONTROL_REQUESTCODE:
            if (resultCode == RESULT_OK) {
    			setResult(RESULT_OK, data);
    			finish();
            }
            break;
        default:
            break;
        }
	}

	
	 private Callback<LampControlData> myCallback = new GenericsCallback<LampControlData>(new JsonGenericsSerializator())
		     {

			 @Override
		     public void onError(Call call, Exception e, int id)
		     {
		   	  e.printStackTrace();
		   	  Log.e(TAG, "LampControlData onError:"+e.getMessage());
		   	  Toast.makeText(SelectLampControlActivity.this, R.string.network_is_not_smooth,
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
				 Toast.makeText(SelectLampControlActivity.this, lampControlData.getMsg(),
							Toast.LENGTH_SHORT).show();
			 }
			}	
		 };
	
}
