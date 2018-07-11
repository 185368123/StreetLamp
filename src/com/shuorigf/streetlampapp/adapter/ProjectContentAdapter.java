package com.shuorigf.streetlampapp.adapter;


import java.util.HashMap;
import java.util.List;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.data.ProjectData.Data.Project;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProjectContentAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Project> mProjects;
	private HashMap<String, Integer> letterIndexes;
	
	public ProjectContentAdapter(Context context){
		this.mContext = context;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 letterIndexes = new HashMap<String, Integer>();
	}
	
	public void changeData(List<Project> list){
		if (list!=null) {
			int size = list.size();
	        for (int index = 0; index < size; index++){
	            String currentLetter = list.get(index).getTag().toUpperCase();
	            //上个首字母，如果不存在设为""
	            String previousLetter = index >= 1 ? list.get(index - 1).getTag().toUpperCase() : "";
	            if (!TextUtils.equals(currentLetter, previousLetter)){
	                letterIndexes.put(currentLetter, index);
	            }
	        }
		}
        mProjects = list;
        notifyDataSetChanged();
    }
	
    public int getLetterPosition(String letter){
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

	@Override
	public int getCount() {
		return mProjects == null ? 0 : mProjects.size();
	}

	@Override
	public Project getItem(int position) {
		return mProjects == null ? null : mProjects.get(position);
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
					R.layout.listitem_project_content, null);
			holder = new ViewHolder();
			holder.alphabet = (TextView) convertView
			.findViewById(R.id.tv_alphabet);
			holder.fistProjectName = (TextView) convertView
					.findViewById(R.id.tv_fist_project_name);
			holder.projectName = (TextView) convertView
					.findViewById(R.id.tv_project_name);
			holder.networkNumber = (TextView) convertView
					.findViewById(R.id.tv_network_number);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Project project = mProjects.get(position);
		holder.fistProjectName.setText(project.getName().substring(0, 1));
		holder.projectName.setText(project.getName());
		holder.networkNumber.setText(project.getNetwork()+mContext.getString(R.string.unit_network));
		if (letterIndexes.containsValue(position)) {
			holder.alphabet.setVisibility(View.VISIBLE);
			holder.alphabet.setText(project.getTag().toUpperCase());
		}else {
			holder.alphabet.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	class ViewHolder {
		public TextView fistProjectName;
		public TextView projectName;
		public TextView networkNumber;
		public TextView alphabet;
	}

}
