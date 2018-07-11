package com.shuorigf.streetlampapp.adapter;

import java.util.List;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.data.LampControlData.Data.LampControl;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EditLampControlContentAdapter extends BaseAdapter {
	private static final String TAG = EditLampControlContentAdapter.class.getSimpleName();
	private Context mContext;
	private LayoutInflater mInflater;
	private List<LampControl> mLampControls;
	

	public EditLampControlContentAdapter(Context context) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
					R.layout.listitem_edit_lamp_control_content, null);
			holder = new ViewHolder();
//			holder.edit = (CheckBox) convertView
//					.findViewById(R.id.chk_edit);
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
		holder.electricity.setText(lampControl.getElectricSOC() + "%");
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
		
		return convertView;
	}

	class ViewHolder {
//		public CheckBox edit;
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
	
	 
}
