package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.shuorigf.streetlampapp.adapter.EditNetworkContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.NetworkData;
import com.shuorigf.streetlampapp.data.NetworkData.Data.Network;
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
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditNetworkListActivity extends NavigationBarActivity implements OnClickListener{
	private static final String TAG = EditNetworkListActivity.class.getSimpleName();
	private ListView mNetworkContentLv;
	private EditNetworkContentAdapter mEditNetworkContentAdapter;
    private CheckBox mSelectAllChk;
	private Button mComfirmBtn;
	
	
	private AlertDialog mDialog;
	private Dialog mDelDialog;
	private Dialog mSuccessDialog;
	private Dialog mFailDialog;
	private Dialog mNoSelectDialog;
	
	private LoginData mLoginData;
	
//	private List<Integer> count;
	
	private String project_id;
	
	private List <Network> mNetworks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		mDelDialog = DialogFactory.creatRequestDialog(this, R.string.deleting);
		mNoSelectDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.no_select_network);
		mSuccessDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_success, R.string.delete_success);
		mFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.delete_failed);
        mSuccessDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent();
			    setResult(RESULT_OK, intent);
				finish();
			}
		});
		View view = View.inflate(this, R.layout.activity_edit_network_list, null);
		mSelectAllChk = (CheckBox) view.findViewById(R.id.chk_edit_network_list_select_all);
		mSelectAllChk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					selectedAll();
				}else {
					unSelectedAll();
				}
			}
		});
		mNetworkContentLv = (ListView) view.findViewById(R.id.lv_edit_network_list_content);
		mNetworkContentLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				updateSeletedCount();
			}

		
		});
		
		mComfirmBtn = (Button) view.findViewById(R.id.btn_edit_network_list_comfirm);
		mComfirmBtn.setOnClickListener(this);
		setNavigationBarContentView(view);
	}

	private void initData() {
//		count = new ArrayList<Integer>();
		mLoginData = ((StreetlampApp)getApplication()).mLoginData;
		setTitleText(R.string.network);
		mComfirmBtn.setText(String.format(getString(R.string.confirm_has_been_selected), 0));
		mEditNetworkContentAdapter = new EditNetworkContentAdapter(this);
		mNetworkContentLv.setAdapter(mEditNetworkContentAdapter);
		project_id = getIntent().getStringExtra("project_id");
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
                    	  Toast.makeText(EditNetworkListActivity.this, R.string.network_is_not_smooth,
									Toast.LENGTH_SHORT).show();
                      }
					@Override
					public void onResponse(NetworkData networkData, int id) {
						Log.i(TAG, "NetworkData onResponse:"+networkData.toString());
						if(networkData.getStatus().equals(NetManager.SUCCESS)){
						    setNetworkData(networkData);
						    
					 }else {
						 Toast.makeText(EditNetworkListActivity.this, networkData.getMsg(),
									Toast.LENGTH_SHORT).show();
					 }
					}	
                  });
		
	}
	
	private void setNetworkData(NetworkData networkData) {
		mNetworks = networkData.getData().getNetworks();
		mEditNetworkContentAdapter.changeData(mNetworks);
	}
	
	 public void selectedAll(){
	        for(int i= 0; i< mEditNetworkContentAdapter.getCount(); i++){
	        	mNetworkContentLv.setItemChecked(i, true);
	        }
	        updateSeletedCount();
	    }
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
	    public void unSelectedAll(){
	    	mNetworkContentLv.clearChoices();
	    	mEditNetworkContentAdapter.notifyDataSetChanged();
	        updateSeletedCount();
	    }
	
	
	private void updateSeletedCount() {
		mComfirmBtn.setText(String.format(getString(R.string.confirm_has_been_selected), mNetworkContentLv.getCheckedItemCount()));
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_edit_network_list_comfirm:
			if (mNetworkContentLv.getCheckedItemCount()>0) {
				showDelDialog();
			}else {
				if(mNoSelectDialog!=null)
					mNoSelectDialog.show();
			}
			
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
	
	private void delNetwork() {
		if (mNetworkContentLv.getCheckedItemCount()>0) {
//			count.clear();
			boolean isFirst=true;
			StringBuffer sb = new StringBuffer();
			SparseBooleanArray checkedArray = mNetworkContentLv
					.getCheckedItemPositions();
			for (int i = 0; i < checkedArray.size(); i++) {
				if (checkedArray.valueAt(i)) {
					if (isFirst) {
						sb.append(mNetworks.get(checkedArray.keyAt(i)).getId());
						isFirst = false;
					}else {
						sb.append(","
								+ mNetworks.get(checkedArray.keyAt(i)).getId());
					}
			
			 }
			}
			    Map<String, String> params = new HashMap<String, String>();
		        params.put("username", mLoginData.getUsername());
		        params.put("client_key", mLoginData.getClient_key());
		        params.put("token", mLoginData.getData().getToken());
		        params.put("network_ids", sb.toString());
		        OkHttpUtils
		                .post()
		                .tag(this)
		                .url(NetManager.NETWORK_DEL_URL)
		                .params(params)
		                .build()
		                .execute(new GenericsCallback<ResultCodeData>(new JsonGenericsSerializator())
		                      {
		                	
		                	@Override
							public void onBefore(Request request, int id) {
		                		if(mDelDialog!=null)
		                			mDelDialog.show();
		                	}
		                	
		                      @Override
								public void onAfter(int id) {
		                    	  if(mDelDialog!=null)
		                    		  mDelDialog.dismiss();
		                      }
		                	
							@Override
		                      public void onError(Call call, Exception e, int id)
		                      {
		                    	  e.printStackTrace();
		                    	  Log.e(TAG, "ResultCodeData onError:"+e.getMessage());
		                      }
							@Override
							public void onResponse(ResultCodeData resultCodeData, int id) {
								if(resultCodeData.getStatus().equals(NetManager.SUCCESS)){
									 if(mSuccessDialog!=null)
										 mSuccessDialog.show();
//									 for (int i = count.size()-1; i >= 0; i--) {
//										 mNetworks.remove(count.get(i).intValue());
//									}
//									 mEditNetworkContentAdapter.notifyDataSetChanged();
									 
								 }else {
									 Toast.makeText(EditNetworkListActivity.this,
											 resultCodeData.getMsg(),
												Toast.LENGTH_SHORT).show();
									 if(mFailDialog!=null)
										 mFailDialog.show();
							 }
							}	
		                  });
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
	}

}
