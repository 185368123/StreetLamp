package com.shuorigf.streetlampapp.adapter;

import java.util.List;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.data.FaultData.Data.Fault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FaultContentAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Fault> mFaults;

	public FaultContentAdapter(Context context) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addFaults(List<Fault> faults) {
		if (mFaults == null) {
			mFaults = faults;
		} else {
			mFaults.addAll(faults);
		}
		notifyDataSetChanged();
	}

	public void clear() {
		if (mFaults!=null) {
			mFaults.clear();
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mFaults == null ? 0 : mFaults.size();
	}

	@Override
	public Fault getItem(int position) {
		return mFaults == null ? null : mFaults.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listitem_fault_content,
					null);
			holder = new ViewHolder();
			holder.faultInfo = (TextView) convertView
					.findViewById(R.id.tv_fault_info);
			holder.project = (TextView) convertView
					.findViewById(R.id.tv_project);
			holder.time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.handlingStatus = (TextView) convertView
					.findViewById(R.id.tv_handling_status);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Fault fault = mFaults.get(position);
		int stageId = R.string.unknown;
		if(fault.getStralarmtype() != null) {
			if(fault.getStralarmtype().contains("过放")) {
				stageId = R.string.fault_type_1;
			}else if(fault.getStralarmtype().contains("超压")) {
				stageId = R.string.fault_type_2;
			}else if(fault.getStralarmtype().contains("负载短路")) {
				stageId = R.string.fault_type_3;
			}else if(fault.getStralarmtype().contains("电池故障")) {
				stageId = R.string.fault_type_4;
			}else if(fault.getStralarmtype().contains("内部超温")) {
				stageId = R.string.fault_type_5;
			}else if(fault.getStralarmtype().contains("外部超温")) {
				stageId = R.string.fault_type_6;
			}else if(fault.getStralarmtype().contains("负载开路")) {
				stageId = R.string.fault_type_7;
			}else if(fault.getStralarmtype().contains("电池板过流")) {
				stageId = R.string.fault_type_8;
			}else if(fault.getStralarmtype().contains("电池板短路")) {
				stageId = R.string.fault_type_9;
			}else if(fault.getStralarmtype().contains("电池板超压")) {
				stageId = R.string.fault_type_10;
			}else if(fault.getStralarmtype().contains("电池板反接")) {
				stageId = R.string.fault_type_11;
			}else if(fault.getStralarmtype().contains("充电逆流")) {
				stageId = R.string.fault_type_12;
			}else if(fault.getStralarmtype().contains("锂电池低温关闭充电")) {
				stageId = R.string.fault_type_13;
			}
		}
		
		holder.faultInfo.setText(stageId);
		holder.project.setText(fault.getProject());
		holder.time.setText(fault.getUpdatetime());
		holder.handlingStatus.setTextColor(mContext.getResources().getColor(
				fault.getStatus() == 0 ? R.color.red : R.color.text_gray));
		holder.handlingStatus
				.setText(fault.getStatus() == 0 ? R.string.not_processed
						: R.string.processed);
		return convertView;
	}
   
	class ViewHolder {
		public TextView faultInfo;
		public TextView project;
		public TextView time;
		public TextView handlingStatus;
	}

}
