package com.shuorigf.streetlampapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.data.ProjectData.Data.Project;

public class ProjectSearchResultAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List <Project> mProjects;

    public ProjectSearchResultAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void changeData(List <Project> projects){
        if (mProjects == null){
        	mProjects = projects;
        }else{
        	mProjects.clear();
        	mProjects.addAll(projects);
        }
        notifyDataSetChanged();
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
		return convertView;
	}

    public static class ViewHolder{
    	public TextView fistProjectName;
		public TextView projectName;
		public TextView networkNumber;
    }
}
