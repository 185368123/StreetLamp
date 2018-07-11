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
import android.widget.ListView;
import android.widget.TextView;

public class SelectLampControlContentAdapter extends BaseAdapter {
	private Context mContext;
	private ListView mListView;
	private LayoutInflater mInflater;
	private List<LampControl> mLampControls;
	
	public SelectLampControlContentAdapter(Context context,ListView listView){
		this.mContext = context;
		this.mListView = listView;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void changeData(List<LampControl> list){
        mLampControls = list;
        notifyDataSetChanged();
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
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.listitem_select_lamp_control_content, null);
			holder = new ViewHolder();
			holder.lampControlName = (TextView) convertView
					.findViewById(R.id.tv_lamp_control_name);
			holder.choose = (ImageView) convertView
					.findViewById(R.id.iv_choose);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		LampControl lampControl = mLampControls.get(position);
		
		holder.lampControlName.setText(mContext
				.getString(R.string.lamp_control) + lampControl.getNumber());
		if (mListView.isItemChecked(position)) {
			holder.choose.setVisibility(View.VISIBLE);
			holder.lampControlName.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
		}else {
			holder.choose.setVisibility(View.GONE);
			holder.lampControlName.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		}
		
		return convertView;
	}

	class ViewHolder {
		public TextView lampControlName;
		public ImageView choose;
	}

}
