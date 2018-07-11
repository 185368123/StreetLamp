package com.shuorigf.streetlampapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.domain.City;

public class CitySearchResultAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<City> mCities;

    public CitySearchResultAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void changeData(List<City> list){
        if (mCities == null){
            mCities = list;
        }else{
            mCities.clear();
            mCities.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public City getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
        if (convertView == null){
        	convertView = mInflater.inflate(R.layout.listitem_city_search_result, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_city_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mCities.get(position).getName());
        return convertView;
    }

    public static class ViewHolder{
        TextView name;
    }
}
