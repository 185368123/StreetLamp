package com.shuorigf.streetlampapp.adapter;


import java.util.List;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.data.LampControlData.Data.LampControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class SelectedLampControlContentAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<LampControl> mLampControls;
	
	public SelectedLampControlContentAdapter(Context context){
		this.mContext = context;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void changeData(List<LampControl> list){
        mLampControls = list;
        notifyDataSetChanged();
    }
	
	public void clear() {
		if (mLampControls!=null) {
			mLampControls.clear();
			notifyDataSetChanged();
		}
	}
	
	
	public List<LampControl> getListData() {
		return mLampControls;
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
					R.layout.listitem_selected_lamp_control_content, null);
			holder = new ViewHolder();
			holder.lampControlName = (TextView) convertView
					.findViewById(R.id.tv_lamp_control_name);
			holder.networkName = (TextView) convertView
					.findViewById(R.id.tv_network_name);
			holder.projectName = (TextView) convertView
					.findViewById(R.id.tv_project_name);
			holder.delete = (ImageButton) convertView
					.findViewById(R.id.imgbtn_delete);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		LampControl lampControl = mLampControls.get(position);
		holder.lampControlName.setText(mContext
				.getString(R.string.lamp_control) + lampControl.getNumber());
		holder.networkName.setText(lampControl.getNetwork_name());
		holder.projectName.setText(lampControl.getProject_name());
		holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				remove(position);
			}
		});
		return convertView;
	}
	
	public synchronized void remove(int position) {
		mLampControls.remove(position);
		notifyDataSetChanged();
	}
	
	class ViewHolder {
		public TextView lampControlName;
		public TextView networkName;
		public TextView projectName;
		public ImageButton delete;
	}

}
