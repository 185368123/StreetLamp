package com.shuorigf.streetlampapp.adapter;

import java.util.List;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.data.LampControlReportData;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LampControlReportAdapter extends
		RecyclerView.Adapter<LampControlReportAdapter.MyViewHolder> {

	private Context mContext;
	private List<LampControlReportData> mLampControlReports;

	public LampControlReportAdapter(Context context) {
		this.mContext = context;
	}

	public void changeData(List<LampControlReportData> list){
        mLampControlReports = list;
        notifyDataSetChanged();
    }
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder;
		holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
				R.layout.rvitem_lamp_control_report, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		LampControlReportData lampControlReportData = mLampControlReports.get(position);
		holder.color.setBackgroundColor(lampControlReportData.getColor());
		holder.lampControlName.setText(mContext
				.getString(R.string.lamp_control) + lampControlReportData.getNumber());

	}

	@Override
	public int getItemCount() {
		return mLampControlReports == null ? 0 : mLampControlReports.size();
	}

	class MyViewHolder extends ViewHolder {
		public TextView lampControlName;
		public View color;

		public MyViewHolder(View view) {
			super(view);
			lampControlName = (TextView) view
					.findViewById(R.id.tv_name);
			color = view.findViewById(R.id.v_color);

		}
	}

}
