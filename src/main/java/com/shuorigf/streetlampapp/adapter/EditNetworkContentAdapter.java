package com.shuorigf.streetlampapp.adapter;


import java.util.List;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.data.NetworkData.Data.Network;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EditNetworkContentAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Network> mNetworks;
	
	public EditNetworkContentAdapter(Context context){
		this.mContext = context;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void changeData(List<Network> list){
        mNetworks = list;
        notifyDataSetChanged();
    }

	@Override
	public int getCount() {
		return mNetworks == null ? 0 : mNetworks.size();
	}

	@Override
	public Network getItem(int position) {
		 return mNetworks == null ? null : mNetworks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.listitem_edit_network_content, null);
			holder = new ViewHolder();
//			holder.edit = (CheckBox) convertView
//					.findViewById(R.id.chk_edit);
			holder.lampControlNumber = (TextView) convertView
					.findViewById(R.id.tv_lamp_control_number);
			holder.networkName = (TextView) convertView
					.findViewById(R.id.tv_network_name);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Network network = mNetworks.get(position);
		
		holder.lampControlNumber.setText(network.getLamp()+mContext.getString(R.string.unit_lamp_control));
		holder.networkName.setText(network.getName());
		return convertView;
	}

	class ViewHolder {
//		public CheckBox edit;
		public TextView lampControlNumber;
		public TextView networkName;
	}

}
