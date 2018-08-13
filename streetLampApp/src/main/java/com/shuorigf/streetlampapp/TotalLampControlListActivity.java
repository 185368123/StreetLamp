package com.shuorigf.streetlampapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.shuorigf.streetlampapp.adapter.LampControlContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LampControlData;
import com.shuorigf.streetlampapp.data.NetworkData;
import com.shuorigf.streetlampapp.data.LampControlData.Data.LampControl;
import com.shuorigf.streetlampapp.data.NetworkData.Data.Network;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.fragment.BaseFragment;
import com.shuorigf.streetlampapp.fragment.LampControlListFragment;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.util.SharePreferenceUtils;
import com.viewpagerindicator.PageIndicator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class TotalLampControlListActivity extends FragmentActivity implements
		OnClickListener {
	private static final String TAG = TotalLampControlListActivity.class
			.getSimpleName();
	
	private static final String PREF_LAMP_CONTROL_LIST_PROJECT_ID = "lamp_control_list_project_id";
	private static final String PREF_LAMP_CONTROL_LIST_PROJECT_NAME = "lamp_control_list_project_name";
	private static final int FAULT_CALLBACK_ID = 101;
	private static final int SEARCH_CALLBACK_ID = 102;
	private static final int SELECT_PROJECT_REQUESTCODE = 1;
	private static final int EDIT_PROJECT_LAMP_CONTROL_REQUESTCODE = 2;
	
	private ImageButton mBackIBtn;
	private ImageButton mDetailsIBtn;
	private ImageButton mEditIBtn;
	private TextView mTitleTv;
	private RadioGroup mTabRGrp;
	private EditText mLampControlSearchEdt;
	private ViewGroup mSearchEmptyView;
	private ListView mSearchResultLv;

	private ViewFlipper mViewFlipper;

	private ViewPager mViewPager;
	private PageIndicator mIndicator;
	private ArrayList<BaseFragment> mFragmentList;
	
	private ListView mFaultContentLv;

	private LoginData mLoginData;
	
	private LampControlContentAdapter mFaultLampControlContentAdapter;
	private LampControlContentAdapter mSearchLampControlContentAdapter;
	private  List<LampControl> mFaultLampControls;
	
	private String project_id;
	private String project_name;
	
	private boolean isFault;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_total_lamp_control_list);
		initView();
		initData();
	}

	private void initView() {
		mTitleTv = (TextView) findViewById(R.id.tv_total_lamp_control_list_title);
		mTitleTv.setOnClickListener(this);
		mBackIBtn = (ImageButton) findViewById(R.id.imgbtn_total_lamp_control_list_back);
		mBackIBtn.setOnClickListener(this);
		mDetailsIBtn = (ImageButton) findViewById(R.id.imgbtn_total_lamp_control_list_details);
		mDetailsIBtn.setOnClickListener(this);
		mEditIBtn = (ImageButton) findViewById(R.id.imgbtn_total_lamp_control_list_edit);
		mEditIBtn.setOnClickListener(this);
		mTabRGrp = (RadioGroup) findViewById(R.id.rgrp_total_lamp_control_list_tab);
		mTabRGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbtn_total_lamp_control_list_all:
					mViewFlipper.setDisplayedChild(0);
					mLampControlSearchEdt.setText(null);
					isFault = false;
					break;
				case R.id.rbtn_total_lamp_control_list_fault:
					mViewFlipper.setDisplayedChild(1);
					mLampControlSearchEdt.setText(null);
					isFault = true;
					break;

				default:
					break;
				}
			}
		});
		mSearchEmptyView = (ViewGroup) findViewById(R.id.llyt_total_lamp_control_list_search_empty);
		mLampControlSearchEdt = (EditText) findViewById(R.id.edt_total_lamp_control_list_search);
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
		mSearchResultLv = (ListView) findViewById(R.id.lv_total_lamp_control_list_search_result);
		mSearchResultLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle lamp = new Bundle();
				lamp.putString("lamp_id", mSearchLampControlContentAdapter.getItem(position).getId());
				lamp.putString("lamp_numble", mSearchLampControlContentAdapter.getItem(position).getNumber());
				lamp.putString("network_name", mSearchLampControlContentAdapter.getItem(position).getNetwork_name());
				goLampControlDetails(lamp);
			}
		});
		mViewFlipper = (ViewFlipper) findViewById(R.id.vf_total_lamp_control_list);
		initAllViewFlipper();
		initFaultViewFlipper();
		
	}

	private void initAllViewFlipper() {
		View view = View.inflate(this, R.layout.viewflipper_all_lamp_control, null);
		mIndicator = (PageIndicator) view.findViewById(R.id.indicator_all_lamp_control_list);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_all_lamp_control_list);
		mViewFlipper.addView(view);
	
	}

	private void initFaultViewFlipper() {
		View view = View.inflate(this, R.layout.viewflipper_fault_lamp_control, null);
		mFaultContentLv = (ListView) view.findViewById(R.id.lv_fault_lamp_control_list_content);
		mFaultContentLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle lamp = new Bundle();
				lamp.putString("lamp_id", mFaultLampControlContentAdapter.getItem(position).getId());
				lamp.putString("lamp_numble", mFaultLampControlContentAdapter.getItem(position).getNumber());
				lamp.putString("network_name", mFaultLampControlContentAdapter.getItem(position).getNetwork_name());
				goLampControlDetails(lamp);
			}
		});
		mViewFlipper.addView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		mFaultLampControlContentAdapter = new LampControlContentAdapter(this);
		mFaultContentLv.setAdapter(mFaultLampControlContentAdapter);
		mSearchLampControlContentAdapter = new LampControlContentAdapter(this);
		mSearchResultLv.setAdapter(mSearchLampControlContentAdapter);
		mFragmentList = new ArrayList<BaseFragment>();
	    project_id = SharePreferenceUtils.getString(this,
				PREF_LAMP_CONTROL_LIST_PROJECT_ID, null);
		project_name = SharePreferenceUtils.getString(this,
				PREF_LAMP_CONTROL_LIST_PROJECT_NAME, null);
		if (project_name!=null) {
			mTitleTv.setText(project_name);
		}
		getNetworkData();
//		getProjectData();

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
	        } else {
	        	mSearchEmptyView.setVisibility(View.GONE);
	        	mSearchLampControlContentAdapter.changeData(lampControls);
	        }
			
		}


//	private void getProjectData() {
//		Map<String, String> params = new HashMap<String, String>();
//        params.put("username", mLoginData.getUsername());
//        params.put("client_key", mLoginData.getClient_key());
//        params.put("token", mLoginData.getData().getToken());
//        OkHttpUtils
//                .post()
//                .url(NetManager.PROJECT_LIST_URL)
//                .params(params)
//                .build()
//                .execute(new GenericsCallback<ProjectData>(new JsonGenericsSerializator())
//                      {
//
//					@Override
//                      public void onError(Call call, Exception e, int id)
//                      {
//                    	  e.printStackTrace();
//                    	  Log.e(TAG, "ProjectData onError:"+e.getMessage());
//                      }
//					@Override
//					public void onResponse(ProjectData projectData, int id) {
//						Log.i(TAG, "ProjectData onResponse:"+projectData.toString());
//						 if(projectData.getStatus().equals(NetManager.SUCCESS)){
////							    mProjectData = projectData;
//							    setProjectData(projectData);
//						 }else {
//							 Toast.makeText(TotalLampControlListActivity.this, projectData.getMsg(),
//										Toast.LENGTH_SHORT).show();
//						 }
//					}
//                  });
//
//	}
//
//	private void setProjectData(ProjectData projectData) {
//		List<Project>  projects = projectData.getData().getProjects();
//		if (projects!=null&&projects.size()>0) {
//		    project_id = projects.get(0).getId();
//		    project_name = projects.get(0).getName();
//			mTitleTv.setText(project_name);
//			getNetworkData(project_id);
//		}
//
//	}
	
	private void getNetworkData() {
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
				.url(NetManager.NETWORK_LIST_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<NetworkData>(
								new JsonGenericsSerializator()) {

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG,
										"NetworkData onError:" + e.getMessage());
								 Toast.makeText(TotalLampControlListActivity.this, R.string.network_is_not_smooth,
											Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(NetworkData networkData,
									int id) {
								Log.i(TAG, "NetworkData onResponse:"
										+ networkData.toString());
								if (networkData.getStatus().equals(
										NetManager.SUCCESS)) {
									setNetworkData(networkData);

								} else {
									Toast.makeText(
											TotalLampControlListActivity.this,
											networkData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});
		
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

	private void setNetworkData(NetworkData networkData) {
		List<Network> networks = networkData.getData().getNetworks();
		if (networks != null&&networks.size()>0) {
			mFragmentList.clear();
			for (Network network : networks) {
				BaseFragment baseFragment = LampControlListFragment
						.newInstance(network.getId());
				mFragmentList.add(baseFragment);
			}
			mViewPager.setAdapter(new ContentAdapter(
					getSupportFragmentManager()));
			mIndicator.setViewPager(mViewPager);
		}

	}
	
	private void setFaultLampControlData(LampControlData lampControlData) {
		mFaultLampControls = lampControlData.getData().getLamps();
		mFaultLampControlContentAdapter.changeData(mFaultLampControls);
		
	}
	
	
	

	public class ContentAdapter extends FragmentStatePagerAdapter {

		public ContentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}

		@Override
		public int getCount() {
			return mFragmentList.size();
		}
		
		@Override
		public int getItemPosition(Object object) {
			return mFragmentList!=null&&mFragmentList.size()==0?POSITION_NONE:super.getItemPosition(object);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_total_lamp_control_list_back:
			finish();
			break;
		case R.id.imgbtn_total_lamp_control_list_details:
			goProjectDetails();
			break;
		case R.id.imgbtn_total_lamp_control_list_edit:
			goEditLampControlList();
			break;
		case R.id.tv_total_lamp_control_list_title:
			goSelectProject();
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
	
	
	private void goSelectProject() {
		Intent intent = new Intent(this, TotalProjectListActivity.class);
		intent.putExtra("type", TotalProjectListActivity.SELECT_PROJECT);
		startActivityForResult(intent, SELECT_PROJECT_REQUESTCODE);
	}

	private void goProjectDetails() {
		if (project_id==null) {
			return;
		}
		Intent intent = new Intent(this, ProjectDetailsActivity.class);
		intent.putExtra("project_id", project_id);
		intent.putExtra("project_name", project_name);
		this.startActivity(intent);
	}

	private void goEditLampControlList() {
		Intent intent = new Intent(this, EditLampControlListActivity.class);
		intent.putExtra("type", EditLampControlListActivity.EDIT_PROJECT_LAMP_CONTROL);
		intent.putExtra("id", project_id);
		intent.putExtra("name", project_name);
		intent.putExtra("isFault", isFault);
		startActivityForResult(intent, EDIT_PROJECT_LAMP_CONTROL_REQUESTCODE);
	}
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode) {
	        case SELECT_PROJECT_REQUESTCODE:
	            if (resultCode == RESULT_OK) {
	            	if (data!=null) {
	            		project_id = data.getStringExtra("project_id");
	            	    project_name = data.getStringExtra("project_name");
	            	    SharePreferenceUtils.putString(this,
	    						PREF_LAMP_CONTROL_LIST_PROJECT_ID, project_id);
	            	    SharePreferenceUtils.putString(this,
	    						PREF_LAMP_CONTROL_LIST_PROJECT_NAME, project_name);
	            		mTitleTv.setText(project_name);
	            		mFragmentList.clear();
	            		if(mViewPager.getAdapter()!=null)
	    				   mViewPager.getAdapter().notifyDataSetChanged();
	            		getNetworkData();
					}
	            }
	            break;
	            
	        case EDIT_PROJECT_LAMP_CONTROL_REQUESTCODE:
	            if (resultCode == RESULT_OK) {
	            	getNetworkData();
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
		   	  Toast.makeText(TotalLampControlListActivity.this, R.string.network_is_not_smooth,
							Toast.LENGTH_SHORT).show();
		     }
			@Override
			public void onResponse(LampControlData lampControlData, int id) {
				Log.i(TAG, "LampControlData onResponse:"+lampControlData.toString());
				if(lampControlData.getStatus().equals(NetManager.SUCCESS)){
					switch (id) {
					case FAULT_CALLBACK_ID:
						setFaultLampControlData(lampControlData);
						break;
					case SEARCH_CALLBACK_ID:
						setSearchLampControlData(lampControlData);
						break;
					}
			 }else {
				 Toast.makeText(TotalLampControlListActivity.this, lampControlData.getMsg(),
							Toast.LENGTH_SHORT).show();
			 }
			}	
		 };
}
