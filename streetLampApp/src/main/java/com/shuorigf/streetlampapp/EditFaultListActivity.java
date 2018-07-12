package com.shuorigf.streetlampapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.shuorigf.streetlampapp.adapter.EditFaultContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.FaultData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.FaultData.DataBean.ListBean.HistoryListBean;
import com.shuorigf.streetlampapp.data.ResultCodeData;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.domain.Page;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.xlistview.XListView;
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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class EditFaultListActivity extends NavigationBarActivity implements OnClickListener,XListView.IXListViewListener{
	private static final String TAG = EditFaultListActivity.class.getSimpleName();
	private XListView mFaultcontentLv;
	private EditFaultContentAdapter mEditFaultContentAdapter;
    private CheckBox mSelectAllChk;
	
	private AlertDialog mDialog;
	private Dialog mDelDialog;
	private Dialog mSuccessDialog;
	private Dialog mFailDialog;
	private Dialog mNoSelectDialog;
	
	private LoginData mLoginData;
	
	private Page mPage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		mDelDialog = DialogFactory.creatRequestDialog(this, R.string.marking);
		mNoSelectDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.no_select_fault_information);
		mSuccessDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_success, R.string.marke_success);
		mFailDialog = DialogFactory.creatResultDialog(this, R.drawable.ic_fail, R.string.marke_failed);
        mSuccessDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent();
			    setResult(RESULT_OK, intent);
				finish();
			}
		});
		View view = View.inflate(this, R.layout.activity_edit_fault_list, null);
		mSelectAllChk = (CheckBox) view.findViewById(R.id.chk_edit_fault_list_select_all);
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
		mFaultcontentLv = (XListView) view.findViewById(R.id.lv_edit_fault_list_fault_content);
		mFaultcontentLv.setPullRefreshEnable(false);
		mFaultcontentLv.setPullLoadEnable(true);
		mFaultcontentLv.setAutoLoadEnable(true);
		mFaultcontentLv.setXListViewListener(this);
		
		setNavigationBarContentView(view);
	}

	private void initData() {
		mLoginData = ((StreetlampApp)getApplication()).mLoginData;
		setTitleText(R.string.fault);
		mRightTV.setVisibility(View.VISIBLE);
		mRightTV.setText(R.string.marked_as_processed);
		mRightTV.setTextColor(getResources().getColor(R.color.blue));
		mRightTV.setOnClickListener(this);
		mEditFaultContentAdapter = new EditFaultContentAdapter(this);
		mFaultcontentLv.setAdapter(mEditFaultContentAdapter);
		mPage = new Page();
	    getFaultData("1", "20",true);
	}
	
	private void getFaultData(String page,String count,final boolean isRefresh) {
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("page", page);
        params.put("count", count);
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.FAULT_LIST_URL_NEW)
                .params(params)
                .build()
                .execute(new GenericsCallback<FaultData>(new JsonGenericsSerializator())
                      {
                	
                	
					@Override
						public void onAfter(int id) {
							super.onAfter(id);
							mFaultcontentLv.stopLoadMore();
						}
					@Override
                      public void onError(Call call, Exception e, int id)
                      {
                    	  e.printStackTrace();
                    	  Log.e(TAG, "FaultData onError:"+e.getMessage());
                    	  Toast.makeText(EditFaultListActivity.this, R.string.network_is_not_smooth,
  								Toast.LENGTH_SHORT).show();
                      }
					@Override
					public void onResponse(FaultData faultData, int id) {
						Log.i(TAG, "FaultData onResponse:"+faultData.toString());
						 if(faultData.getStatus().equals(NetManager.SUCCESS)){
							   if (isRefresh) {
								   mPage.setPageStart();
							   }
							   mPage.addPage();
							   setFaultData(faultData);
							    
						 }else {
							 Toast.makeText(EditFaultListActivity.this, faultData.getMsg(),
										Toast.LENGTH_SHORT).show();
						 }
					}	
                  });
	}
	
	private void setFaultData(FaultData faultData) {
		List<HistoryListBean> mFaults = faultData.getData().getList().getHistory_list();
		if (mFaults!=null) {
			mEditFaultContentAdapter.addFaults(mFaults);
			if (mSelectAllChk.isChecked()) {
				selectedAll();
			}
		}
	}
	
	 public void selectedAll(){
	        for(int i= 1; i< mEditFaultContentAdapter.getCount()+1; i++){
	        	mFaultcontentLv.setItemChecked(i, true);
	        }
	    }
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
	    public void unSelectedAll(){
	    	mFaultcontentLv.clearChoices();
	    	mEditFaultContentAdapter.notifyDataSetChanged();
	    }
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_navigation_bar_right:
			if (mFaultcontentLv.getCheckedItemCount()>0) {
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
					.setText(R.string.are_you_sure_you_want_to_marked_as_processed);
			((Button) view.findViewById(R.id.btn_confirm))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							markedAsProcessed();
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
	
	private void markedAsProcessed() {
		if (mFaultcontentLv.getCheckedItemCount()>0) {
			boolean isFirst=true;
			StringBuffer sb = new StringBuffer();
			SparseBooleanArray checkedArray = mFaultcontentLv
					.getCheckedItemPositions();
			for (int i = 0; i < checkedArray.size(); i++) {
				if (checkedArray.valueAt(i)) {
					if (isFirst) {
						sb.append(mEditFaultContentAdapter.getListData().get(checkedArray.keyAt(i)-1).getId());
						isFirst = false;
					}else {
						sb.append(","
								+ mEditFaultContentAdapter.getListData().get(checkedArray.keyAt(i)-1).getId());
					}
			
			 }
			}
			
			Map<String, String> params = new HashMap<String, String>();
	        params.put("username", mLoginData.getUsername());
	        params.put("client_key", mLoginData.getClient_key());
	        params.put("token", mLoginData.getData().getToken());
	        params.put("ids", sb.toString());
	        params.put("status", "1");
	        
	        OkHttpUtils
	                .post()
	                .tag(this)
	                .url(NetManager.FAULT_UPDATE_URL)
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
	                    	  Toast.makeText(EditFaultListActivity.this, R.string.network_is_not_smooth,
										Toast.LENGTH_SHORT).show();
	                      }
						@Override
						public void onResponse(ResultCodeData resultCodeData, int id) {
							 if(resultCodeData.getStatus().equals(NetManager.SUCCESS)){
								 if(mSuccessDialog!=null)
									 mSuccessDialog.show();
							 }else {
								 Toast.makeText(EditFaultListActivity.this,
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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		getFaultData(mPage.getCurrentPage(), "10",false);		
	}

}
