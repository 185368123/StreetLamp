package com.shuorigf.streetlampapp.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LampControlData.Data.LampControl;
import com.shuorigf.streetlampapp.data.LampControlRefreshData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.dialog.DialogFactory;
import com.shuorigf.streetlampapp.network.NetManager;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LampControlContentAdapter extends BaseAdapter {
	private static final String TAG = LampControlContentAdapter.class.getSimpleName();
	private Context mContext;
	private LayoutInflater mInflater;
	private List<LampControl> mLampControls;
	private LoginData mLoginData;
	
	private Dialog mDialog;
	private Dialog mSuccessDialog;
	private Dialog mFailDialog;

	public LampControlContentAdapter(Activity context) {
		this.mContext = context;
		mDialog = DialogFactory.creatRequestDialog(context, R.string.refreshing);
		mSuccessDialog = DialogFactory.creatResultDialog(context, R.drawable.ic_success, R.string.refresh_success);
		mFailDialog = DialogFactory.creatResultDialog(context, R.drawable.ic_fail, R.string.refresh_failed);
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLoginData = ((StreetlampApp)context.getApplication()).mLoginData;
	}
	
	public void changeData(List<LampControl> list){
        mLampControls = list;
        notifyDataSetChanged();
    }

	@Override
	public int getCount() {
		return mLampControls == null ? 0 : mLampControls.size();
	}

	@Override
	public LampControl getItem(int position) {
		return mLampControls == null ? null : mLampControls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.listitem_lamp_control_content, null);
			holder = new ViewHolder();
			holder.lampControlName = (TextView) convertView
					.findViewById(R.id.tv_lamp_control_name);
			holder.LampControlStatus = (ImageView) convertView
					.findViewById(R.id.iv_lamp_control_status);
			holder.networkName = (TextView) convertView
					.findViewById(R.id.tv_network_name);
			holder.networkStatus = (ImageView) convertView
					.findViewById(R.id.iv_network_status);
			holder.refresh = (ImageView) convertView
					.findViewById(R.id.imgbtn_refresh);
			holder.power = (TextView) convertView
					.findViewById(R.id.tv_power_values);
			holder.electricity = (TextView) convertView
					.findViewById(R.id.tv_electricity_values);
			holder.chargingStatus = (TextView) convertView
					.findViewById(R.id.tv_charging_status_values);
			holder.time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		LampControl lampControl = mLampControls.get(position);

		holder.lampControlName.setText(mContext
				.getString(R.string.lamp_control) + lampControl.getNumber());
		holder.LampControlStatus.setImageResource(mLampControls.get(position)
				.getStatus() == 0 ? R.drawable.ic_lamp_control_off_status
				: R.drawable.ic_lamp_control_on_status);
		holder.networkName.setText(lampControl.getNetwork_name());
		holder.networkStatus.setImageResource(mLampControls.get(position)
				.getIsfaulted() == 0 ? R.drawable.ic_network_on_status
				: R.drawable.ic_network_off_status);
		holder.power.setText(lampControl.getLamppower() + "W");
		holder.electricity.setText(lampControl.getBattvoltage()+"");
		
		int stageId = R.string.unknown_state;
		if(lampControl.getChargestage() != null) {
			if(lampControl.getChargestage().contains("MPPT")) {
				stageId = R.string.mppt_charge;
			}else if(lampControl.getChargestage().contains("均衡")) {
				stageId = R.string.balance_charge;
			}else if(lampControl.getChargestage().contains("提升")) {
				stageId = R.string.lifting_charge;
			}else if(lampControl.getChargestage().contains("浮充")) {
				stageId = R.string.floating_charge;
			}
		}
		
		holder.chargingStatus.setText(stageId);
		holder.time.setText(lampControl.getUpdatetime());
		holder.refresh.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					refresh(position);
				}
			});
		
		return convertView;
	}

	class ViewHolder {
		public ImageView LampControlStatus;
		public ImageView refresh;
		public TextView lampControlName;
		public TextView networkName;
		public ImageView networkStatus;
		public TextView power;
		public TextView electricity;
		public TextView chargingStatus;
		public TextView time;
	}
	
	private void refresh(final int position){
		String lamp_id = mLampControls.get(position).getId();
	    if (lamp_id==null) {
	    	return;
		}
		Map<String, String> params = new HashMap<String, String>();
	    params.put("username", mLoginData.getUsername());
	    params.put("client_key", mLoginData.getClient_key());
	    params.put("token", mLoginData.getData().getToken());
	    params.put("lamp_id", lamp_id);
	    OkHttpUtils
	            .post()
	            .url(NetManager.LAMP_CONTROL_UPDATE_URL)
	            .params(params)
	            .build()
	            .execute(new GenericsCallback<LampControlRefreshData>(new JsonGenericsSerializator())
	                  {
	            	
	            	@Override
					public void onBefore(Request request, int id) {
                		if(mDialog!=null)
                		mDialog.show();
                	}
                	
                      @Override
						public void onAfter(int id) {
                    	  if(mDialog!=null)
                    	  mDialog.dismiss();
                      }
	            	
					@Override
	                  public void onError(Call call, Exception e, int id)
	                  {
	                	  e.printStackTrace();
	                	  Log.e(TAG, "refresh LampControl onError:"+e.getMessage());
	                  }
					@Override
					public void onResponse(LampControlRefreshData lampControlDetailsData, int id) {
						Log.i(TAG, "refresh LampControl onResponse:"+lampControlDetailsData.toString());
						 if(lampControlDetailsData.getStatus().equals(NetManager.SUCCESS)){
							 LampControl lampControl = mLampControls.get(position); 
							 lampControl.setNumber(lampControlDetailsData.getData().getNumber());
							 lampControl.setStatus(lampControlDetailsData.getData().getStatus());
							 lampControl.setIsfaulted(lampControlDetailsData.getData().getIsfaulted());
							 lampControl.setNetwork_name(lampControlDetailsData.getData().getNetwork_name());
							 lampControl.setLamppower(lampControlDetailsData.getData().getLamppower());
							 lampControl.setElectricSOC(lampControlDetailsData.getData().getBattvoltage());
							 lampControl.setChargestage(lampControlDetailsData.getData().getChargestage());
							 lampControl.setUpdatetime(lampControlDetailsData.getData().getUpdatetime());
						     notifyDataSetChanged();
						     if(mSuccessDialog!=null)
								 mSuccessDialog.show();
						 }else {
							 Toast.makeText(mContext, lampControlDetailsData.getMsg(),
										Toast.LENGTH_SHORT).show();
							 if(mFailDialog!=null)
								 mFailDialog.show();
						 }
					}	
	              });
	}
	 
}
