package com.shuorigf.streetlampapp.adapter;


import java.util.List;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.UpdateHistoryActivity;
import com.shuorigf.streetlampapp.data.UpdateHistoryData.Data.UpdateHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UpdateHistoryContentAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	
	private List<UpdateHistory> mUpdateHistorys;
	private int mType;
	
	public UpdateHistoryContentAdapter(Context context,int type){
		this.mContext = context;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mType = type;
	}
	
	public void addUpdateHistorys(List<UpdateHistory> list){
        if (mUpdateHistorys == null){
        	mUpdateHistorys = list;
        }else{
        	mUpdateHistorys.addAll(list);
        }
        notifyDataSetChanged();
    }

	@Override
	public int getCount() {
		return mUpdateHistorys == null ? 0 : mUpdateHistorys.size();
	}

	@Override
	public UpdateHistory getItem(int position) {
		return mUpdateHistorys == null ? null : mUpdateHistorys.get(position);
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
					R.layout.listitem_update_history_content, null);
			holder = new ViewHolder();
			holder.date = (TextView) convertView
					.findViewById(R.id.tv_date);
			holder.time = (TextView) convertView
					.findViewById(R.id.tv_time);
			holder.voltage = (TextView) convertView
					.findViewById(R.id.tv_voltage_value);
			holder.electricity = (TextView) convertView
					.findViewById(R.id.tv_electricity_value);
			holder.temperature = (TextView) convertView
					.findViewById(R.id.tv_temperature_value);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UpdateHistory updateHistory = mUpdateHistorys.get(position);
		String[] updatetime = updateHistory.getUpdatetime().split(" ");
		holder.date.setText(updatetime[0]);
		holder.time.setText(updatetime[1]);
		switch (mType) {
		case UpdateHistoryActivity.SYSTEM_INFORMATION:
			holder.voltage.setText(updateHistory.getSysvoltage());
			holder.electricity.setText(updateHistory.getSyscurrent());
			holder.temperature.setText(updateHistory.getTemper());
			break;
		case UpdateHistoryActivity.STREET_LAMP_INFORMATION:
			holder.voltage.setText(updateHistory.getLampvoltage());
			holder.electricity.setText(updateHistory.getLampcurrent());
			holder.temperature.setText(updateHistory.getLamppower());
			break;
		case UpdateHistoryActivity.BATTERY_BOARD_INFORMATION:
			holder.voltage.setText(updateHistory.getSolarvoltage());
			holder.electricity.setText(updateHistory.getSolarcurrent());
			holder.temperature.setText(updateHistory.getSolarpower());
			break;
		case UpdateHistoryActivity.STORAGE_BATTERY_BOARD_INFORMATION:
			holder.voltage.setText(updateHistory.getBattvoltage());
			holder.electricity.setText(updateHistory.getElectricleft());
			holder.temperature.setText(updateHistory.getBatttemper());
			break;
		default:
			break;
		}
		return convertView;
	}
	
	class ViewHolder {
		public TextView date;
		public TextView time;
		public TextView voltage;
		public TextView electricity;
		public TextView temperature;
	}

}
