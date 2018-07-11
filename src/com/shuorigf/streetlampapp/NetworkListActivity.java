package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.shuorigf.streetlampapp.adapter.NetworkContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.NetworkData;
import com.shuorigf.streetlampapp.data.NetworkData.Data.Network;
import com.shuorigf.streetlampapp.network.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NetworkListActivity extends NavigationBarActivity implements OnClickListener{
	private static final String TAG = NetworkListActivity.class.getSimpleName();

	private static final int NETWORK_DETAILS_REQUESTCODE = 1;
	private static final int EDIT_NETWORK_REQUESTCODE = 2;
	
	private TextView mbelongsToTheProjectTv;
	private ListView mNetworkContentLv;
	private NetworkContentAdapter mNetworkContentAdapter;
	
	private LoginData mLoginData;
	private List <Network> mNetworks;
	
	private String project_id;
//	private String project_name;
	private Bundle projectBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		View view = View.inflate(this, R.layout.activity_network_list, null);
		mbelongsToTheProjectTv = (TextView) view.findViewById(R.id.tv_network_belongs_to_the_project_value);
		mNetworkContentLv = (ListView) view.findViewById(R.id.lv_network_content);
		mNetworkContentLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle network = new Bundle(projectBundle);
				network.putString("network_id", mNetworkContentAdapter.getItem(position).getId());
				network.putString("network_name", mNetworkContentAdapter.getItem(position).getName());
				goNetworkDetails(network);
			}
		
		});
		setNavigationBarContentView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp)getApplication()).mLoginData;
		mRightIBtn.setVisibility(View.VISIBLE);
		mRightIBtn.setImageResource(R.drawable.ic_edit);
		mRightIBtn.setOnClickListener(this);
		setTitleText(R.string.network);
		projectBundle = getIntent().getExtras();
	    project_id = projectBundle.getString("project_id");
	    mbelongsToTheProjectTv.setText(projectBundle.getString("project_name"));
	    mNetworkContentAdapter = new NetworkContentAdapter(this);
		mNetworkContentLv.setAdapter(mNetworkContentAdapter);
		getNetworkData();
		
	}
	

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
                .execute(new GenericsCallback<NetworkData>(new JsonGenericsSerializator())
                      {
                	
					@Override
                      public void onError(Call call, Exception e, int id)
                      {
                    	  e.printStackTrace();
                    	  Log.e(TAG, "NetworkData onError:"+e.getMessage());
                    	  Toast.makeText(NetworkListActivity.this, R.string.network_is_not_smooth,
									Toast.LENGTH_SHORT).show();
                      }
					@Override
					public void onResponse(NetworkData networkData, int id) {
						Log.i(TAG, "NetworkData onResponse:"+networkData.toString());
						if(networkData.getStatus().equals(NetManager.SUCCESS)){
						    setNetworkData(networkData);
						    
					 }else {
						 Toast.makeText(NetworkListActivity.this, networkData.getMsg(),
									Toast.LENGTH_SHORT).show();
					 }
					}	
                  });
		
	}

	private void setNetworkData(NetworkData networkData) {
		mNetworks = networkData.getData().getNetworks();
		mNetworkContentAdapter.changeData(mNetworks);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_navigation_bar_right:
			goEditNetworkList();
			break;
		default:
			break;
		}
	}

	private void goNetworkDetails(Bundle network) {
			Intent intent = new Intent(this,NetworkDetailsActivity.class);
			intent.putExtras(network);
			startActivityForResult(intent, NETWORK_DETAILS_REQUESTCODE);
	}
	
	private void goEditNetworkList() {
		Intent intent = new Intent(this,EditNetworkListActivity.class);
		intent.putExtra("project_id", project_id);
		startActivityForResult(intent, EDIT_NETWORK_REQUESTCODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		 case NETWORK_DETAILS_REQUESTCODE:
	            if (resultCode == RESULT_OK) {
	            	getNetworkData();
	            }
	            break;
        case EDIT_NETWORK_REQUESTCODE:
            if (resultCode == RESULT_OK) {
//            	mNetworkContentAdapter.notifyDataSetChanged();
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

}
