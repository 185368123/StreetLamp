package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.shuorigf.streetlampapp.adapter.UpdateHistoryContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.UpdateHistoryData;
import com.shuorigf.streetlampapp.data.UpdateHistoryData.Data.UpdateHistory;
import com.shuorigf.streetlampapp.domain.Page;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateHistoryActivity extends NavigationBarActivity implements
		OnClickListener ,XListView.IXListViewListener{
	private static final String TAG = UpdateHistoryActivity.class
			.getSimpleName();
	
	public static final int SYSTEM_INFORMATION = 0;
	public static final int STREET_LAMP_INFORMATION = 1;
	public static final int BATTERY_BOARD_INFORMATION = 2;
	public static final int STORAGE_BATTERY_BOARD_INFORMATION = 3;
	public static final int DAILY_ELECTRICITY_INFORMATION = 4;
	public static final int HISTORY_DATA_INFORMATION = 5;
	
	
	private TextView mTemperatureTv;
	private TextView mEelectricityTv;
	private XListView mUpdateHistoryContentLv;
	private UpdateHistoryContentAdapter mUpdateHistoryContentAdapter;
	
	
	private LoginData mLoginData;
	private String lamp_id;
	
	private Page mPage;
	
	private int mType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		View view = View.inflate(this, R.layout.activity_update_history, null);
		mTemperatureTv = (TextView) view
				.findViewById(R.id.tv_update_history_temperature);
		mEelectricityTv = (TextView) view
				.findViewById(R.id.tv_update_history_electricity);
		mUpdateHistoryContentLv = (XListView) view
				.findViewById(R.id.lv_update_history_content);
		mUpdateHistoryContentLv.setPullRefreshEnable(false);
		mUpdateHistoryContentLv.setPullLoadEnable(true);
		mUpdateHistoryContentLv.setAutoLoadEnable(true);
		mUpdateHistoryContentLv.setXListViewListener(this);
		setNavigationBarContentView(view);
	}

	private void initData() {
		mType = getIntent().getIntExtra("type",SYSTEM_INFORMATION);
		lamp_id = getIntent().getStringExtra("lamp_id");
		mLoginData = ((StreetlampApp) getApplication()).mLoginData;
		setTitleText(R.string.update_history);
		mRightIBtn.setVisibility(View.VISIBLE);
		if (mType==STREET_LAMP_INFORMATION||mType==BATTERY_BOARD_INFORMATION) {
			mTemperatureTv.setText(R.string.power_W);
		}
		if (mType==STORAGE_BATTERY_BOARD_INFORMATION) {
			mEelectricityTv.setText(R.string.electricity);
		}
		mRightIBtn.setImageResource(R.drawable.ic_filter_nor);
		mRightIBtn.setOnClickListener(this);
		mUpdateHistoryContentAdapter = new UpdateHistoryContentAdapter(this,mType);
		mUpdateHistoryContentLv.setAdapter(mUpdateHistoryContentAdapter);
		mPage = new Page();
	    mPage.setPageStart();
		getUpdateHistoryData("1","20");
	}
	
	
	private void getUpdateHistoryData(String page,String count) {
		if (lamp_id==null) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", mLoginData.getUsername());
		params.put("client_key", mLoginData.getClient_key());
		params.put("token", mLoginData.getData().getToken());
		params.put("lamp_id", lamp_id);
		params.put("datatype", mType+"");
		params.put("page", page);
		params.put("count", count);
		OkHttpUtils
				.post()
				.tag(this)
				.url(NetManager.LAMP_UPDATE_HISTORY_URL)
				.params(params)
				.build()
				.execute(
						new GenericsCallback<UpdateHistoryData>(
								new JsonGenericsSerializator()) {
							
							@Override
							public void onAfter(int id) {
								super.onAfter(id);
								 mUpdateHistoryContentLv.stopLoadMore();
							}

							@Override
							public void onError(Call call, Exception e, int id) {
								e.printStackTrace();
								Log.e(TAG, "UpdateHistoryData onError:"
										+ e.getMessage());
								Toast.makeText(UpdateHistoryActivity.this, R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onResponse(
									UpdateHistoryData updateHistoryData,
									int id) {
								Log.i(TAG, "UpdateHistoryData onResponse:"
										+ updateHistoryData.toString());
								if (updateHistoryData.getStatus().equals(
										NetManager.SUCCESS)) {
									    mPage.addPage();
									    setUpdateHistoryData(updateHistoryData);
								} else {
									 mUpdateHistoryContentLv.stopLoadMore();
									Toast.makeText(
											UpdateHistoryActivity.this,
											updateHistoryData.getMsg(),
											Toast.LENGTH_SHORT).show();
								}
							}
						});		
	}
	
	
	

	private void setUpdateHistoryData(UpdateHistoryData updateHistoryData) {
	    List<UpdateHistory> mUpdateHistorys = updateHistoryData.getData().getList();
	    if(mUpdateHistorys!=null) {
	    	mUpdateHistoryContentAdapter.addUpdateHistorys(mUpdateHistorys);
	    }
	    
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_navigation_bar_right:
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		getUpdateHistoryData(mPage.getCurrentPage(), "10");		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
	}

}
