package com.shuorigf.streetlampapp.fragment;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.shuorigf.streetlampapp.EditFaultListActivity;
import com.shuorigf.streetlampapp.FaultDetailsActivity;
import com.shuorigf.streetlampapp.MainActivity;
import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.adapter.FaultContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.FaultData;
import com.shuorigf.streetlampapp.data.FaultData.Data.Fault;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.domain.Page;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class FaultFragment extends BaseFragment implements OnClickListener,XListView.IXListViewListener{

	private static final String TAG = FaultFragment.class.getSimpleName();
	private static final int FAULT_DETAILS_REQUESTCODE = 1;
	private static final int FAULT_EDIT_REQUESTCODE = 2;
	
	private ImageButton mOpenLeftMenuIBtn;
	private ImageButton mEditIBtn;
	private XListView mFaultcontentLv;
	private FaultContentAdapter mFaultContentAdapter;
	
	private LoginData mLoginData;
	
	private Page mPage;
	
	private boolean isLoading;
    private boolean isLoaded;
	

	@Override
	public View initView(Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.fragment_fault, null);
		mOpenLeftMenuIBtn = (ImageButton) view.findViewById(R.id.imgbtn_fault_accout);
		mOpenLeftMenuIBtn.setOnClickListener(this);
		mEditIBtn = (ImageButton) view.findViewById(R.id.imgbtn_fault_edit);
		mEditIBtn.setOnClickListener(this);
		mFaultcontentLv = (XListView) view.findViewById(R.id.lv_fault_content);
		mFaultcontentLv.setPullRefreshEnable(true);
		mFaultcontentLv.setPullLoadEnable(true);
		mFaultcontentLv.setAutoLoadEnable(true);
		mFaultcontentLv.setXListViewListener(this);
		mFaultcontentLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position<1) {
					return;
				}
				Bundle fault = new Bundle();
				fault.putString("fault_id",mFaultContentAdapter.getItem(position-1).getId());
				fault.putInt("fault_status", mFaultContentAdapter.getItem(position-1).getStatus());
				goFaultDetails(fault);
			}
		});
		return view;
	}
	
	public void initData() {
	   mLoginData = ((StreetlampApp)mActivity.getApplication()).mLoginData;
	   mFaultContentAdapter = new FaultContentAdapter(mActivity);
	   mFaultcontentLv.setAdapter(mFaultContentAdapter);
       mPage = new Page();
       getFaultData("1", "20", true);
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_fault_accout:
			openLeftMenu();
			break;
		case R.id.imgbtn_fault_edit:
			goEditFaultList();
			break;
		default:
			break;
		}		
	}
	
	private void openLeftMenu() {
		MainActivity mainActivity = (MainActivity) mActivity;
//		mainActivity.getSlidingMenu().toggle();
		mainActivity.toggle();
	}
	
	private void goFaultDetails(Bundle fault) {
		Intent intent = new Intent(mActivity, FaultDetailsActivity.class);
		intent.putExtras(fault);
		this.startActivityForResult(intent, FAULT_DETAILS_REQUESTCODE);
	}
	
	private void goEditFaultList() {
		Intent intent = new Intent(mActivity, EditFaultListActivity.class);
		this.startActivityForResult(intent, FAULT_EDIT_REQUESTCODE);
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case FAULT_DETAILS_REQUESTCODE:
			if (resultCode == Activity.RESULT_OK) {
				   OkHttpUtils.getInstance().cancelTag(this);
         	       isLoaded = false;
			       getFaultData("1", "20", true);
				}
			break;
			
		case FAULT_EDIT_REQUESTCODE:
			if (resultCode == Activity.RESULT_OK) {
				   OkHttpUtils.getInstance().cancelTag(this);
            	   isLoaded = false;
			       getFaultData("1", "20", true);
				}
			break;
		default:
			break;
		}
	}


	@Override
	public void onLoadMore() {
		getFaultData(mPage.getCurrentPage(), "10", false);
	}
	
	
	
	private void getFaultData(String page,String count,final boolean isfresh) {
		if (isLoading) {
			return;
		}
		isLoading = true;
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("page", page);
        params.put("count", count);
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.FAULT_LIST_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<FaultData>(new JsonGenericsSerializator())
                      {
                	
                	
					@Override
						public void onAfter(int id) {
							super.onAfter(id);
							mFaultcontentLv.stopRefresh();
							mFaultcontentLv.stopLoadMore();
							isLoading = false;
						}
					@Override
                      public void onError(Call call, Exception e, int id)
                      {
                    	  e.printStackTrace();
                    	  Log.e(TAG, "FaultData onError:"+e.getMessage());
                    	  Toast.makeText(mActivity, R.string.network_is_not_smooth,
  								Toast.LENGTH_SHORT).show();
                      }
					@Override
					public void onResponse(FaultData faultData, int id) {
						Log.i(TAG, "FaultData onResponse:"+faultData.toString());
						 if(faultData.getStatus().equals(NetManager.SUCCESS)){
							 if (isfresh) {
								 mPage.setPageStart();
								 mFaultContentAdapter.clear();
							   }
							   isLoaded = true;
							   mPage.addPage();
							   setFaultData(faultData);
							    
						 }else {
							 Toast.makeText(mActivity, faultData.getMsg(),
										Toast.LENGTH_SHORT).show();
						 }
					}	
                  });
	}
	
	private void setFaultData(FaultData faultData) {
		MainActivity mainActivity = (MainActivity) mActivity;
		mainActivity.setFaultCount(faultData.getData().getTotal()+"");
		List<Fault> mFaults = faultData.getData().getAlarm();
		if (mFaults!=null) {
			mFaultContentAdapter.addFaults(mFaults);
		}
	}
	

	@Override
	public void onRefresh() {
	     getFaultData("1", "20", true);
	}
	
	 public void onVisible(){  
	    	if (isPrepared&&!isLoaded) {
	    		getFaultData("1", "20", true);
			}
	    } 
	    
	
	 @Override
		public void onDestroy() {
			super.onDestroy();
			OkHttpUtils.getInstance().cancelTag(this);
		}

}
