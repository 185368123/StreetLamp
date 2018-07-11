package com.shuorigf.streetlampapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.shuorigf.streetlampapp.R;

/**
 * author zaaach on 2016/1/26.
 */
public class HotCityAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mCities;
    private LayoutInflater mInflater;

    public HotCityAdapter(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCities = new ArrayList<String>();
        mCities.add("北京");
        mCities.add("上海");
        mCities.add("广州");
        mCities.add("深圳");
        mCities.add("杭州");
        mCities.add("武汉");
        mCities.add("成都");
        mCities.add("天津");
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public String getItem(int position) {
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
        	convertView = mInflater.inflate(R.layout.griditem_hot_city, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_hot_city_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mCities.get(position));
        return convertView;
    }

    public static class ViewHolder{
        TextView name;
    }
}
