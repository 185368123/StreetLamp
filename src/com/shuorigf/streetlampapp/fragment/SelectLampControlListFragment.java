package com.shuorigf.streetlampapp.fragment;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.adapter.LampControlContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LampControlData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.LampControlData.Data.LampControl;
import com.shuorigf.streetlampapp.network.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class SelectLampControlListFragment extends BaseFragment{

	private static final String TAG = SelectLampControlListFragment.class.getSimpleName();
	
	  public static SelectLampControlListFragment newInstance(String network_id) {
		  SelectLampControlListFragment lampControllistFragment = new SelectLampControlListFragment();

		  lampControllistFragment.network_id = network_id;

	        return lampControllistFragment;
	    }
	  
	private String network_id;  
	
	private ListView mLampControlContentLv;
	private LampControlContentAdapter mLampControlContentAdapter;
	private  List<LampControl> mLampControls;
	
	private LoginData mLoginData;
	private LampControlData mLampControlData;
	

	@Override
	public View initView(Bundle savedInstanceState) {
		View view = View.inflate(mActivity, R.layout.fragment_select_lamp_control_list, null);
		mLampControlContentLv = (ListView) view.findViewById(R.id.lv_select_lamp_control_list_content);
		return view;
	}
	
	public void initData() {
		mLoginData = ((StreetlampApp)mActivity.getApplication()).mLoginData;
		mLampControlContentAdapter = new LampControlContentAdapter(mActivity);
		mLampControlContentLv.setAdapter(mLampControlContentAdapter);
		getLampControlData();
	}
	
	
	private void getLampControlData(){
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        if (network_id!=null) {
        	params.put("network_id", network_id);
		}
        
        OkHttpUtils
                .post()
                .url(NetManager.LAMP_CONTROL_LIST_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<LampControlData>(new JsonGenericsSerializator())
                      {
                	
					@Override
                      public void onError(Call call, Exception e, int id)
                      {
                    	  e.printStackTrace();
                    	  Log.e(TAG, "LampControlData onError:"+e.getMessage());
                      }
					@Override
					public void onResponse(LampControlData lampControlData, int id) {
						Log.i(TAG, "LampControlData onResponse:"+lampControlData.toString());
						if(lampControlData.getStatus().equals(NetManager.SUCCESS)){
						    mLampControlData = lampControlData;
						    setLampControlData();
					 }else {
						 Toast.makeText(mActivity, lampControlData.getMsg(),
									Toast.LENGTH_SHORT).show();
					 }
					}	
                  });
	}
	
	private void setLampControlData() {
		mLampControls = mLampControlData.getData().getLamps();
		mLampControlContentAdapter.changeData(mLampControls);
		
	}

}
